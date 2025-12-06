#include <algorithm>
#include <cstring>
#include <iomanip>
#include <iostream>
#include <string>
#include <vector>

// Usamos extern "C" para evitar el "name mangling" de C++
// y que las funciones sean fáciles de llamar desde LLVM IR.
extern "C" {

struct Movimiento {
  char *nombre;
  double monto;
  char *categoria;
};

// Base de datos en memoria
std::vector<Movimiento> historial;

// Función para registrar un gasto en el historial
void runtime_agregar_gasto(char *nombre, double monto, char *categoria) {
  Movimiento m;
  // Copiamos las cadenas para que persistan
  m.nombre = strdup(nombre);
  m.monto = monto;
  m.categoria = strdup(categoria);
  historial.push_back(m);
}

// Función para listar todos los gastos
void runtime_listar_gastos() {
  std::cout << "\n--- Historial de Gastos ---" << std::endl;
  std::cout << std::left << std::setw(20) << "Concepto" << std::setw(10)
            << "Monto"
            << "Categoria" << std::endl;
  std::cout << "----------------------------------------" << std::endl;

  for (const auto &m : historial) {
    std::cout << std::left << std::setw(20) << m.nombre << std::fixed
              << std::setprecision(2) << m.monto << "   " << m.categoria
              << std::endl;
  }
  std::cout << "----------------------------------------\n" << std::endl;
}

// Función para filtrar gastos por categoría
void runtime_filtrar_gastos(char *filtro_categoria) {
  std::string filtro = filtro_categoria;
  std::cout << "\n--- Gastos en: " << filtro << " ---" << std::endl;

  double total = 0.0;
  for (const auto &m : historial) {
    if (std::string(m.categoria) == filtro) {
      std::cout << "- " << m.nombre << ": " << std::fixed
                << std::setprecision(2) << m.monto << std::endl;
      total += m.monto;
    }
  }
  std::cout << "Total en " << filtro << ": " << total << "\n" << std::endl;
}
}
