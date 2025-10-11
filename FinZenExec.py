from collections import defaultdict
from FinZenVisitor import FinZenVisitor
from FinZenParser import FinZenParser

def _unq(s: str) -> str:
    return s[1:-1] if s and s[0] == '"' and s[-1] == '"' else s

class FinZenExec(FinZenVisitor):
    def __init__(self):
        self.ingresos = []
        self.gastos = []
        self.ahorros = []
        self.pres_cat = {}
        self.out = []

    def visitPrograma(self, ctx: FinZenParser.ProgramaContext):
        for p in ctx.presupuesto_stmt():
            self.visit(p)
        for m in ctx.movimiento():
            self.visit(m)
        for c in ctx.comando():
            self.visit(c)
        return "\n".join(self.out)

    def visitPresupuesto_stmt(self, ctx: FinZenParser.Presupuesto_stmtContext):
        categoria = _unq(ctx.CADENA().getText())
        monto = float(ctx.NUMERO().getText())
        self.pres_cat[categoria] = monto
        return None

    def visitMovimiento(self, ctx: FinZenParser.MovimientoContext):
        tipo = ctx.getChild(0).getText()
        nombre = _unq(ctx.getChild(1).getText())
        monto = presupuesto = actual = None
        categoria = None

        i = 2
        n = ctx.getChildCount()
        while i < n:
            t = ctx.getChild(i).getText()
            if t in ('monto','presupuesto','actual','categoria') and i+2 < n and ctx.getChild(i+1).getText()=='=':
                val = ctx.getChild(i+2).getText()
                if t == 'categoria':
                    categoria = _unq(val)
                else:
                    num = float(val)
                    if t == 'monto': monto = num
                    elif t == 'presupuesto': presupuesto = num
                    elif t == 'actual': actual = num
                i += 3
            else:
                i += 1

        if tipo == 'ingreso':
            if monto is None: raise ValueError(f"El ingreso '{nombre}' requiere monto=.")
            self.ingresos.append(dict(nombre=nombre, monto=monto, categoria=categoria or ""))

        elif tipo == 'gasto':
            if actual is None: raise ValueError(f"El gasto '{nombre}' requiere actual=.")
            self.gastos.append(dict(nombre=nombre, actual=actual, presupuesto=presupuesto if presupuesto is not None else None, categoria=categoria or ""))

        elif tipo == 'ahorro':
            if monto is None: raise ValueError(f"El ahorro '{nombre}' requiere monto=.")
            self.ahorros.append(dict(nombre=nombre, monto=monto, categoria=categoria or ""))

        return None

    def visitComando(self, ctx: FinZenParser.ComandoContext):
        k = ctx.getChild(0).getText()

        if k == 'total_ingresos':
            total = sum(i['monto'] for i in self.ingresos)
            self.out.append(self._fmt('total_ingresos', total))

        elif k == 'total_gastos':
            total = sum(g['actual'] for g in self.gastos)
            self.out.append(self._fmt('total_gastos', total))

        elif k == 'diferencia':
            total_i = sum(i['monto'] for i in self.ingresos)
            total_g = sum(g['actual'] for g in self.gastos)
            self.out.append(self._fmt('diferencia', total_i - total_g))

        elif k == 'resumen_categoria':
            cat = _unq(ctx.getChild(1).getText())
            total = sum(g['actual'] for g in self.gastos if g['categoria'] == cat)
            self.out.append(self._fmt(f"resumen_categoria {cat}", total))

        elif k == 'listar_gastos':
            if not self.gastos:
                self.out.append("Gastos:\n (sin registros)")
                return None
            agrup = defaultdict(list)
            subtot = defaultdict(float)
            for g in self.gastos:
                agrup[g['categoria']].append(g)
                subtot[g['categoria']] += g['actual']
            lines = ["Gastos:"]
            for cat in sorted(agrup.keys(), key=lambda s: s.lower()):
                lines.append(f" {cat or '(sin categoría)'}:")
                for g in agrup[cat]:
                    lines.append(f"  - {g['nombre']}: {self._num(g['actual'])}")
                lines.append(f"  Total {cat or '(sin categoría)'}: {self._num(subtot[cat])}")
            self.out.append("\n".join(lines))

        elif k == 'filtrar_gastos':
            cat = _unq(ctx.getChild(1).getText())
            sel = [g for g in self.gastos if g['categoria'] == cat]
            if not sel:
                self.out.append(f"Gastos en {cat}: (sin registros)")
                return None
            total = sum(g['actual'] for g in sel)
            lines = [f"Gastos en {cat}:"]
            for g in sel:
                lines.append(f" - {g['nombre']}: {self._num(g['actual'])}")
            lines.append(f" Total {cat}: {self._num(total)}")
            self.out.append("\n".join(lines))

        elif k == 'diferencia_presupuestos':
            cats = set(self.pres_cat.keys()) | {g['categoria'] for g in self.gastos}
            if not cats:
                self.out.append("Diferencias por categoría: (sin datos)")
                return None
            suma_cat = defaultdict(float)
            for g in self.gastos:
                suma_cat[g['categoria']] += g['actual']
            lines = ["Diferencias por categoría:"]
            for cat in sorted(cats, key=lambda s: s.lower()):
                presup = self.pres_cat.get(cat, 0.0)
                actual = suma_cat.get(cat, 0.0)
                diff = presup - actual
                lines.append(f" - {cat or '(sin categoría)'}: Presupuesto={self._num(presup)}, Actual={self._num(actual)}, Ahorro={self._num(diff)}")
            self.out.append("\n".join(lines))

        return None

    @staticmethod
    def _fmt(label: str, valor: float):
        return f"{label}: {FinZenExec._num(valor)}"

    @staticmethod
    def _num(valor: float):
        return str(int(round(valor))) if abs(valor - round(valor)) < 1e-9 else f"{valor:.2f}"