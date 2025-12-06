grammar FinZen;

@header {
#include <string>
}


// Parser rules
programa
    : FINANZAS LBRACE (presupuesto_stmt | movimiento)+ RBRACE comando* EOF
    ;

presupuesto_stmt
    : PRESUPUESTO CADENA '=' NUMERO
    ;

movimiento
    : (INGRESO | GASTO | AHORRO) CADENA
      ( MONTO '=' NUMERO )?
      ( PRESUPUESTO '=' NUMERO )?
      ( ACTUAL '=' NUMERO )?
      ( CATEGORIA '=' CADENA )?
    ;

comando
    : TOTAL_INGRESOS
    | TOTAL_GASTOS
    | DIFERENCIA
    | RESUMEN_CATEGORIA CADENA
    | LISTAR_GASTOS
    | FILTRAR_GASTOS CADENA
    | DIFERENCIA_PRESUPUESTOS
    ;

// Lexer rules
FINANZAS               : 'finanzas' ;
INGRESO                : 'ingreso' ;
GASTO                  : 'gasto' ;
AHORRO                 : 'ahorro' ;
MONTO                  : 'monto' ;
PRESUPUESTO            : 'presupuesto' ;
ACTUAL                 : 'actual' ;
CATEGORIA              : 'categoria' ;

TOTAL_INGRESOS         : 'total_ingresos' ;
TOTAL_GASTOS           : 'total_gastos' ;
DIFERENCIA             : 'diferencia' ;
RESUMEN_CATEGORIA      : 'resumen_categoria' ;

LISTAR_GASTOS          : 'listar_gastos' ;
FILTRAR_GASTOS         : 'filtrar_gastos' ;
DIFERENCIA_PRESUPUESTOS: 'diferencia_presupuestos' ;

LBRACE                 : '{' ;
RBRACE                 : '}' ;

NUMERO
    : '-'? DIGIT+ ('.' DIGIT+)?
    ;

fragment DIGIT : [0-9] ;

CADENA
    : '"' ( ~["\\] | '\\' . )* '"'
    ;

WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;