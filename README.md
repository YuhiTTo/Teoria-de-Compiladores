# Teoria-de-Compiladores

# FinZen v2 – DSL de Finanzas con Presupuestos por Categoría

Novedades:
- Declaración de **presupuestos por categoría**: `presupuesto "Categoria" = 300`
- Nuevos comandos: `listar_gastos`, `filtrar_gastos "Categoria"`, `diferencia_presupuestos`
- Salidas con **totales por categoría** cuando listas o filtras.

## 1) Generar ANTLR (Python + Visitor)

```bash
java -jar antlr-4.13.2-complete.jar -Dlanguage=Python3 -visitor FinZen.g4
```

## 2) Instalar dependencias
```bash
pip install -r requirements.txt
```

## 3) Ejecutar con ejemplo embebido
```bash
python run_finzen.py
```

## 4) Ejecutar con archivo
```bash
python run_finzen.py ejemplo_v2.finzen
```

## Comandos soportados
- `total_ingresos` – suma de todos los ingresos
- `total_gastos` – suma de todos los gastos
- `diferencia` – ingresos – gastos
- `resumen_categoria "Nombre"` – total de gastos en esa categoría
- `listar_gastos` – lista gastos agrupados por categoría e imprime subtotal
- `filtrar_gastos "Nombre"` – lista gastos de la categoría e imprime subtotal
- `diferencia_presupuestos` – por cada categoría: Presupuesto vs Actual vs Ahorro (=Presupuesto-Actual)