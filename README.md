# FinZen v2 – DSL de Finanzas Personales (Versión C++ / LLVM)

Compilador para el lenguaje FinZen, implementado en C++ utilizando ANTLR4 para el frontend y LLVM para la generación de código intermedio.

## Requisitos
- CMake (3.10+)
- Make
- Compilador C++ (g++, clang++)
- LLVM (versión compatible con `lli`)
- ANTLR4 Runtime para C++

## Estructura del Proyecto
```text
FinZen/
├── CMakeLists.txt       # Configuración de compilación
├── src/
│   ├── main.cpp         # Punto de entrada
│   ├── FinZen.g4        # Gramática
│   ├── FinZenDriver.h   # Cabecera del generador IR
│   └── FinZenDriver.cpp # Implementación de la generación
└── build/               # Directorio de compilación
```

- `src/`: Código fuente C++ (.cpp, .h) y Gramática (.g4)
- `build/`: Directorio de compilación

## Compilación

Desde la terminal (WSL):

```bash
mkdir -p build
cd build
cmake ..
make
```

## Ejecución

### 1. Compilación Nativa con Runtime:
Para que funcionen los comandos avanzados (`listar_gastos`, `filtrar_gastos`), debes enlazar con la librería C++:

```bash
# 1. Genera el .ll
./FinZen ../mi_dia.finzen > mi_dia.ll

# 2. Compila enlazando el Runtime

clang mi_dia.ll ../src/FinZenRuntime.cpp -o mi_programa_final -lstdc++

clang++ mi_dia.ll ../src/FinZenRuntime.cpp -o mi_programa_final

# 3. Ejecutar
./mi_programa_final
```

### 2. Compilación con Vizualización Web:

Desde la terminal (PowerShell):

```bash

# 1. Instalar dependencias
pip install -r web/requirements.txt

# 2. Ejecutar Streamlit
python -m streamlit run web/app.py
```

## Funcionalidades Soportadas (Hito 2)
El compilador procesa y calcula automáticamente:
- **Presupuestos**: `presupuesto "Comida" = 500`
- **Movimientos**: `gasto "Pizza" monto=50 categoria="Comida"`
- **Comandos de Reporte**:
    - `total_ingresos`
    - `total_gastos`
    - `diferencia`
    - `listar_gastos`
    - `filtrar_gastos`
    - `diferencia_presupuestos`

## Ejemplo de Código FinZen
```finzen
finanzas {
  presupuesto "Ocio" = 300
  ingreso "Sueldo" monto=2500
  gasto "Cine" actual=50 categoria="Ocio"
}
total_ingresos
total_gastos
diferencia
```
