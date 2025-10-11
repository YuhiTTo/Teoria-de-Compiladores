import sys
from antlr4 import InputStream, CommonTokenStream, FileStream
from FinZenLexer import FinZenLexer
from FinZenParser import FinZenParser
from FinZenExec import FinZenExec

EXAMPLE = """
finanzas {
  // Presupuestos por categoría
  presupuesto "Gastos fijos" = 800
  presupuesto "Ocio" = 300

  // Movimientos
  ingreso "Sueldo" monto=2500 categoria="Trabajo"
  ingreso "Freelance" monto=800 categoria="Trabajo"

  gasto "Casa" actual=450 categoria="Gastos fijos"
  gasto "Comida" actual=280 categoria="Gastos fijos"
  gasto "Transporte" actual=80 categoria="Gastos fijos"
  gasto "Internet" actual=20 categoria="Gastos fijos"

  gasto "Netflix" actual=45 categoria="Entretenimiento"
  gasto "Salidas" actual=110 categoria="Ocio"

  ahorro "Cuenta de ahorro" monto=400
}
total_ingresos
total_gastos
diferencia
listar_gastos
filtrar_gastos "Ocio"
resumen_categoria "Gastos fijos"
diferencia_presupuestos
""".strip()

def main():
    if len(sys.argv) > 1:
        stream = FileStream(sys.argv[1], encoding='utf-8')
    else:
        stream = InputStream(EXAMPLE)

    lexer = FinZenLexer(stream)
    tokens = CommonTokenStream(lexer)
    parser = FinZenParser(tokens)
    tree = parser.programa()

    executor = FinZenExec()
    out = executor.visit(tree)
    print(out)

if __name__ == "__main__":
    main()