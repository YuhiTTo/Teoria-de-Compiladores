#!/bin/bash

# Verificar argumentos
if [ "$#" -ne 1 ]; then
    echo "Uso: ./run.sh <archivo.finzen>"
    exit 1
fi

INPUT_FILE=$1
BASENAME=$(basename "$INPUT_FILE" .finzen)
BUILD_DIR="build"

# 1. Generar el código intermedio (.ll)
echo "[1/3] Generando codigo intermedio (LLVM IR)..."
./build/FinZen "$INPUT_FILE" > "${BUILD_DIR}/${BASENAME}.ll"

if [ $? -ne 0 ]; then
    echo "Error: Falló la compilación de FinZen."
    exit 1
fi

# 2. Compilar a binario nativo (enlazando con el Runtime)
echo "[2/3] Compilando ejecutable nativo..."
clang "${BUILD_DIR}/${BASENAME}.ll" src/FinZenRuntime.cpp -o "${BUILD_DIR}/${BASENAME}" -lstdc++ -Wno-override-module

if [ $? -ne 0 ]; then
    echo "Error: Falló la compilación con Clang."
    exit 1
fi

# 3. Ejecutar
echo "[3/3] Ejecutando programa..."
echo "---------------------------------------------------"
"./${BUILD_DIR}/${BASENAME}"
echo "---------------------------------------------------"
