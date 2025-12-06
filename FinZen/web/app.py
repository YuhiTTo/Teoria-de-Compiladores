
import streamlit as st
import subprocess
import os
import re
from pathlib import Path

import pandas as pd


# Configuración general de página

st.set_page_config(page_title="FinZen Compiler", layout="wide")

st.title("🧘 FinZen: Compilador de Finanzas Personales")
st.markdown("Escribe tu código FinZen a la izquierda y ve los resultados a la derecha.")

# Paths base (asumiendo estructura FinZen_v2/web/app.py)
WEB_DIR = Path(__file__).resolve().parent
BASE_DIR = WEB_DIR.parent          # carpeta FinZen
RUN_SH = BASE_DIR / "run.sh"       # script de compilación/ejecución


# Función para ejecutar FinZen

def run_finzen(code: str) -> str:
    """
    Guarda el código en temp.finzen y llama a run.sh desde la carpeta FinZen_v2.
    Usa rutas relativas para que bash en WSL/Git Bash no se confunda con C:\...
    """
    # Archivo temporal en la raíz del proyecto
    temp_name = "temp.finzen"
    temp_finzen = BASE_DIR / temp_name
    temp_finzen.write_text(code, encoding="utf-8")

    run_sh = BASE_DIR / "run.sh"
    if not run_sh.exists():
        raise RuntimeError(f"No se encontró run.sh en {run_sh}")

    # Llamamos: bash run.sh temp.finzen, con cwd=BASE_DIR
    try:
        result = subprocess.run(
            ["bash", "run.sh", temp_name],
            cwd=BASE_DIR,
            capture_output=True,
            text=True,
        )
    except FileNotFoundError as e:
        raise RuntimeError(
            "No se pudo ejecutar 'bash'. "
            "En Windows, usa WSL o Git Bash, y asegúrate de que esté en el PATH."
        ) from e

    if result.returncode != 0:
        # Mezclamos stdout y stderr para ver el error completo
        raise RuntimeError(result.stdout + "\n" + result.stderr)

    return result.stdout


# Funciones de parseo (Regex)

TOTAL_INGRESOS_RE = re.compile(r"Total Ingresos:\s*([0-9]+(?:\.[0-9]+)?)")
TOTAL_GASTOS_RE = re.compile(r"Total Gastos:\s*([0-9]+(?:\.[0-9]+)?)")
DIFERENCIA_RE = re.compile(r"Diferencia:\s*([0-9]+(?:\.[0-9]+)?)")
RESUMEN_RE = re.compile(r"Resumen (.+?):\s*([0-9]+(?:\.[0-9]+)?)")

DIF_PRES_LINE_RE = re.compile(
    r"\s*(.+?):\s*Pres=([0-9]+(?:\.[0-9]+)?),\s*Actual=([0-9]+(?:\.[0-9]+)?),\s*Ahorro=([0-9]+(?:\.[0-9]+)?)"
)

ALERTA_RE = re.compile(r"ALERTA: Presupuesto excedido en (.+)!")
HISTORIAL_HEADER = "--- Historial de Gastos ---"


def parse_totales(output: str):
    m_ing = TOTAL_INGRESOS_RE.search(output)
    m_gas = TOTAL_GASTOS_RE.search(output)
    m_diff = DIFERENCIA_RE.search(output)

    total_ingresos = float(m_ing.group(1)) if m_ing else None
    total_gastos = float(m_gas.group(1)) if m_gas else None
    diferencia = float(m_diff.group(1)) if m_diff else None

    return total_ingresos, total_gastos, diferencia


def parse_diferencia_presupuestos(output: str) -> pd.DataFrame:
    rows = []
    for match in DIF_PRES_LINE_RE.finditer(output):
        cat, pres, actual, ahorro = match.groups()
        rows.append(
            {
                "Categoria": cat.strip(),
                "Presupuesto": float(pres),
                "Actual": float(actual),
                "Ahorro": float(ahorro),
            }
        )
    return pd.DataFrame(rows)


def parse_resumenes(output: str) -> pd.DataFrame:
    """Líneas tipo: 'Resumen Comida: 120.00'."""
    rows = []
    for match in RESUMEN_RE.finditer(output):
        cat, total = match.groups()
        rows.append({"Categoria": cat.strip(), "Total": float(total)})
    return pd.DataFrame(rows)


def parse_historial_gastos(output: str) -> pd.DataFrame:
    """
    Tabla impresa por runtime_listar_gastos():
      --- Historial de Gastos ---
      Concepto            Monto     Categoria
      ----------------------------------------
      Cena                25.00     Comida
      ...
    La parseamos asumiendo que las dos últimas columnas son Monto y Categoria.
    """
    lines = output.splitlines()
    rows = []
    in_table = False

    for line in lines:
        if HISTORIAL_HEADER in line:
            in_table = False
            continue

        if "Concepto" in line and "Monto" in line and "Categoria" in line:
            in_table = True
            continue

        if in_table:
            s = line.strip()
            if not s:
                continue
            # Línea de guiones (inicio/fin de tabla)
            if set(s) == {"-"}:
                continue

            parts = s.split()
            if len(parts) < 3:
                continue

            categoria = parts[-1]
            monto_str = parts[-2].replace(",", ".")
            try:
                monto = float(monto_str)
            except ValueError:
                continue

            concepto = " ".join(parts[:-2])
            rows.append(
                {"Concepto": concepto.strip(), "Monto": monto, "Categoria": categoria}
            )

    return pd.DataFrame(rows)


def parse_filtrar_gastos(output: str) -> list[tuple[str, pd.DataFrame]]:
    """
    Parsea bloques de filtro como:
    
    --- Gastos en: Comida ---
    - Almuerzo: 40.00
    - Cena de Lujo: 80.00
    Total en Comida: 120.00
    
    Retorna una lista de tuplas (categoria, dataframe).
    """
    results = []
    # Dividimos por el encabezado de filtro
    blocks = re.split(r'--- Gastos en: (.*?) ---', output)
    
    # El split nos da: [texto_previo, cat1, contenido1, cat2, contenido2, ...]
    if len(blocks) > 1:
        for i in range(1, len(blocks), 2):
            category = blocks[i].strip()
            content = blocks[i+1]
            
            # Extraemos líneas tipo "- Item: Monto"
            items = []
            for line in content.splitlines():
                line = line.strip()
                match = re.search(r'- (.*?):\s*([0-9]+(?:\.[0-9]+)?)', line)
                if match:
                    items.append({
                        "Concepto": match.group(1).strip(), 
                        "Monto": float(match.group(2)),
                        "Categoria": category
                    })
            
            if items:
                results.append((category, pd.DataFrame(items)))
                
    return results


def parse_alertas(output: str):
    return [m.group(1).strip() for m in ALERTA_RE.finditer(output)]



# Layout principal

col1, col2 = st.columns(2)

with col1:
    st.subheader("📝 Editor de Código")

    default_code = """finanzas {
  presupuesto "Comida" = 500
  presupuesto "Transporte" = 200

  ingreso "Sueldo" monto=2000 categoria="Trabajo"
  gasto "Pizza" actual=50 categoria="Comida"
  gasto "Metro" actual=10 categoria="Transporte"
}

total_ingresos
total_gastos
diferencia
resumen_categoria "Comida"
listar_gastos
diferencia_presupuestos
"""
    code = st.text_area("Código Fuente", value=default_code, height=400, key="codigo_fuente")

    if st.button("🚀 Compilar y Ejecutar", type="primary"):
        try:
            output = run_finzen(code)
            st.session_state["output"] = output
            st.session_state["error"] = ""
        except Exception as e:
            st.session_state["output"] = ""
            st.session_state["error"] = str(e)

with col2:
    st.subheader("📊 Resultados")

    # Mostrar errores si los hay
    if st.session_state.get("error"):
        st.error(st.session_state["error"])

    if st.session_state.get("output"):
        raw_output = st.session_state["output"]

        # 1) Salida de consola cruda
        with st.expander("Ver salida completa (compilador + programa)", expanded=True):
            st.code(raw_output, language="text")

        # 2) Métricas clave: ingresos, gastos, diferencia
        total_ingresos, total_gastos, diferencia = parse_totales(raw_output)
        if any(v is not None for v in (total_ingresos, total_gastos, diferencia)):
            st.markdown("### 📌 Resumen numérico")
            mcol1, mcol2, mcol3 = st.columns(3)

            if total_ingresos is not None:
                mcol1.metric("Total Ingresos", f"{total_ingresos:,.2f} S/")
            if total_gastos is not None:
                mcol2.metric("Total Gastos", f"{total_gastos:,.2f} S/")
            if diferencia is not None:
                mcol3.metric("Diferencia", f"{diferencia:,.2f} S/")

        # 3) Diferencia de presupuestos (por categoría)
        df_diff = parse_diferencia_presupuestos(raw_output)
        if not df_diff.empty:
            st.markdown("### 🧮 Diferencia de Presupuestos por Categoría")
            st.dataframe(df_diff, use_container_width=True)

            st.bar_chart(df_diff, x="Categoria", y=["Presupuesto", "Actual"])

        # 4) Historial de gastos (tabla runtime_listar_gastos)
        df_hist = parse_historial_gastos(raw_output)
        if not df_hist.empty:
            st.markdown("### 📜 Historial de Gastos")
            st.dataframe(df_hist, use_container_width=True)

            # Gráfico de gasto total por categoría
            st.markdown("#### Gasto por categoría")
            df_cat = df_hist.groupby("Categoria")["Monto"].sum().reset_index()
            st.bar_chart(df_cat, x="Categoria", y="Monto")

        # 5) Resúmenes individuales por categoría (resumen_categoria X)
        df_res = parse_resumenes(raw_output)
        if not df_res.empty:
            st.markdown("### 📂 Resumen por Categoría (resumen_categoria)")
            st.dataframe(df_res, use_container_width=True)

        # 6) Filtros (filtrar_gastos)
        filtros = parse_filtrar_gastos(raw_output)
        if filtros:
            for cat, df_filtro in filtros:
                st.markdown(f"### 🔍 Filtro: {cat}")
                st.dataframe(df_filtro, use_container_width=True)
                # Gráfico opcional
                st.bar_chart(df_filtro.set_index("Concepto")["Monto"])

        # 6) Alertas de presupuesto
        alertas = parse_alertas(raw_output)
        if alertas:
            st.markdown("### ⚠️ Alertas de Presupuesto")
            for cat in alertas:
                st.warning(f"Presupuesto excedido en **{cat}**")
