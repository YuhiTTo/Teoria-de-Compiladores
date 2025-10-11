// Generated from c:/Users/Lucero/Documents/Teoria-de-Compiladores/FinZen.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class FinZenParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, FINANZAS=2, INGRESO=3, GASTO=4, AHORRO=5, MONTO=6, PRESUPUESTO=7, 
		ACTUAL=8, CATEGORIA=9, TOTAL_INGRESOS=10, TOTAL_GASTOS=11, DIFERENCIA=12, 
		RESUMEN_CATEGORIA=13, LISTAR_GASTOS=14, FILTRAR_GASTOS=15, DIFERENCIA_PRESUPUESTOS=16, 
		LBRACE=17, RBRACE=18, NUMERO=19, CADENA=20, WS=21, LINE_COMMENT=22, BLOCK_COMMENT=23;
	public static final int
		RULE_programa = 0, RULE_presupuesto_stmt = 1, RULE_movimiento = 2, RULE_comando = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"programa", "presupuesto_stmt", "movimiento", "comando"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "'finanzas'", "'ingreso'", "'gasto'", "'ahorro'", "'monto'", 
			"'presupuesto'", "'actual'", "'categoria'", "'total_ingresos'", "'total_gastos'", 
			"'diferencia'", "'resumen_categoria'", "'listar_gastos'", "'filtrar_gastos'", 
			"'diferencia_presupuestos'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "FINANZAS", "INGRESO", "GASTO", "AHORRO", "MONTO", "PRESUPUESTO", 
			"ACTUAL", "CATEGORIA", "TOTAL_INGRESOS", "TOTAL_GASTOS", "DIFERENCIA", 
			"RESUMEN_CATEGORIA", "LISTAR_GASTOS", "FILTRAR_GASTOS", "DIFERENCIA_PRESUPUESTOS", 
			"LBRACE", "RBRACE", "NUMERO", "CADENA", "WS", "LINE_COMMENT", "BLOCK_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "FinZen.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FinZenParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramaContext extends ParserRuleContext {
		public TerminalNode FINANZAS() { return getToken(FinZenParser.FINANZAS, 0); }
		public TerminalNode LBRACE() { return getToken(FinZenParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(FinZenParser.RBRACE, 0); }
		public TerminalNode EOF() { return getToken(FinZenParser.EOF, 0); }
		public List<Presupuesto_stmtContext> presupuesto_stmt() {
			return getRuleContexts(Presupuesto_stmtContext.class);
		}
		public Presupuesto_stmtContext presupuesto_stmt(int i) {
			return getRuleContext(Presupuesto_stmtContext.class,i);
		}
		public List<MovimientoContext> movimiento() {
			return getRuleContexts(MovimientoContext.class);
		}
		public MovimientoContext movimiento(int i) {
			return getRuleContext(MovimientoContext.class,i);
		}
		public List<ComandoContext> comando() {
			return getRuleContexts(ComandoContext.class);
		}
		public ComandoContext comando(int i) {
			return getRuleContext(ComandoContext.class,i);
		}
		public ProgramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programa; }
	}

	public final ProgramaContext programa() throws RecognitionException {
		ProgramaContext _localctx = new ProgramaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programa);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			match(FINANZAS);
			setState(9);
			match(LBRACE);
			setState(12); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(12);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PRESUPUESTO:
					{
					setState(10);
					presupuesto_stmt();
					}
					break;
				case INGRESO:
				case GASTO:
				case AHORRO:
					{
					setState(11);
					movimiento();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(14); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 184L) != 0) );
			setState(16);
			match(RBRACE);
			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 130048L) != 0)) {
				{
				{
				setState(17);
				comando();
				}
				}
				setState(22);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(23);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Presupuesto_stmtContext extends ParserRuleContext {
		public TerminalNode PRESUPUESTO() { return getToken(FinZenParser.PRESUPUESTO, 0); }
		public TerminalNode CADENA() { return getToken(FinZenParser.CADENA, 0); }
		public TerminalNode NUMERO() { return getToken(FinZenParser.NUMERO, 0); }
		public Presupuesto_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_presupuesto_stmt; }
	}

	public final Presupuesto_stmtContext presupuesto_stmt() throws RecognitionException {
		Presupuesto_stmtContext _localctx = new Presupuesto_stmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_presupuesto_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			match(PRESUPUESTO);
			setState(26);
			match(CADENA);
			setState(27);
			match(T__0);
			setState(28);
			match(NUMERO);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MovimientoContext extends ParserRuleContext {
		public List<TerminalNode> CADENA() { return getTokens(FinZenParser.CADENA); }
		public TerminalNode CADENA(int i) {
			return getToken(FinZenParser.CADENA, i);
		}
		public TerminalNode INGRESO() { return getToken(FinZenParser.INGRESO, 0); }
		public TerminalNode GASTO() { return getToken(FinZenParser.GASTO, 0); }
		public TerminalNode AHORRO() { return getToken(FinZenParser.AHORRO, 0); }
		public TerminalNode MONTO() { return getToken(FinZenParser.MONTO, 0); }
		public List<TerminalNode> NUMERO() { return getTokens(FinZenParser.NUMERO); }
		public TerminalNode NUMERO(int i) {
			return getToken(FinZenParser.NUMERO, i);
		}
		public TerminalNode PRESUPUESTO() { return getToken(FinZenParser.PRESUPUESTO, 0); }
		public TerminalNode ACTUAL() { return getToken(FinZenParser.ACTUAL, 0); }
		public TerminalNode CATEGORIA() { return getToken(FinZenParser.CATEGORIA, 0); }
		public MovimientoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_movimiento; }
	}

	public final MovimientoContext movimiento() throws RecognitionException {
		MovimientoContext _localctx = new MovimientoContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_movimiento);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 56L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(31);
			match(CADENA);
			setState(35);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MONTO) {
				{
				setState(32);
				match(MONTO);
				setState(33);
				match(T__0);
				setState(34);
				match(NUMERO);
				}
			}

			setState(40);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(37);
				match(PRESUPUESTO);
				setState(38);
				match(T__0);
				setState(39);
				match(NUMERO);
				}
				break;
			}
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ACTUAL) {
				{
				setState(42);
				match(ACTUAL);
				setState(43);
				match(T__0);
				setState(44);
				match(NUMERO);
				}
			}

			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CATEGORIA) {
				{
				setState(47);
				match(CATEGORIA);
				setState(48);
				match(T__0);
				setState(49);
				match(CADENA);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComandoContext extends ParserRuleContext {
		public TerminalNode TOTAL_INGRESOS() { return getToken(FinZenParser.TOTAL_INGRESOS, 0); }
		public TerminalNode TOTAL_GASTOS() { return getToken(FinZenParser.TOTAL_GASTOS, 0); }
		public TerminalNode DIFERENCIA() { return getToken(FinZenParser.DIFERENCIA, 0); }
		public TerminalNode RESUMEN_CATEGORIA() { return getToken(FinZenParser.RESUMEN_CATEGORIA, 0); }
		public TerminalNode CADENA() { return getToken(FinZenParser.CADENA, 0); }
		public TerminalNode LISTAR_GASTOS() { return getToken(FinZenParser.LISTAR_GASTOS, 0); }
		public TerminalNode FILTRAR_GASTOS() { return getToken(FinZenParser.FILTRAR_GASTOS, 0); }
		public TerminalNode DIFERENCIA_PRESUPUESTOS() { return getToken(FinZenParser.DIFERENCIA_PRESUPUESTOS, 0); }
		public ComandoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comando; }
	}

	public final ComandoContext comando() throws RecognitionException {
		ComandoContext _localctx = new ComandoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_comando);
		try {
			setState(61);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TOTAL_INGRESOS:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				match(TOTAL_INGRESOS);
				}
				break;
			case TOTAL_GASTOS:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				match(TOTAL_GASTOS);
				}
				break;
			case DIFERENCIA:
				enterOuterAlt(_localctx, 3);
				{
				setState(54);
				match(DIFERENCIA);
				}
				break;
			case RESUMEN_CATEGORIA:
				enterOuterAlt(_localctx, 4);
				{
				setState(55);
				match(RESUMEN_CATEGORIA);
				setState(56);
				match(CADENA);
				}
				break;
			case LISTAR_GASTOS:
				enterOuterAlt(_localctx, 5);
				{
				setState(57);
				match(LISTAR_GASTOS);
				}
				break;
			case FILTRAR_GASTOS:
				enterOuterAlt(_localctx, 6);
				{
				setState(58);
				match(FILTRAR_GASTOS);
				setState(59);
				match(CADENA);
				}
				break;
			case DIFERENCIA_PRESUPUESTOS:
				enterOuterAlt(_localctx, 7);
				{
				setState(60);
				match(DIFERENCIA_PRESUPUESTOS);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0017@\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0004\u0000\r\b\u0000\u000b\u0000\f\u0000\u000e\u0001"+
		"\u0000\u0001\u0000\u0005\u0000\u0013\b\u0000\n\u0000\f\u0000\u0016\t\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002$\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		")\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002.\b\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0003\u00023\b\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0003\u0003>\b\u0003\u0001\u0003\u0000\u0000\u0004"+
		"\u0000\u0002\u0004\u0006\u0000\u0001\u0001\u0000\u0003\u0005H\u0000\b"+
		"\u0001\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004\u001e"+
		"\u0001\u0000\u0000\u0000\u0006=\u0001\u0000\u0000\u0000\b\t\u0005\u0002"+
		"\u0000\u0000\t\f\u0005\u0011\u0000\u0000\n\r\u0003\u0002\u0001\u0000\u000b"+
		"\r\u0003\u0004\u0002\u0000\f\n\u0001\u0000\u0000\u0000\f\u000b\u0001\u0000"+
		"\u0000\u0000\r\u000e\u0001\u0000\u0000\u0000\u000e\f\u0001\u0000\u0000"+
		"\u0000\u000e\u000f\u0001\u0000\u0000\u0000\u000f\u0010\u0001\u0000\u0000"+
		"\u0000\u0010\u0014\u0005\u0012\u0000\u0000\u0011\u0013\u0003\u0006\u0003"+
		"\u0000\u0012\u0011\u0001\u0000\u0000\u0000\u0013\u0016\u0001\u0000\u0000"+
		"\u0000\u0014\u0012\u0001\u0000\u0000\u0000\u0014\u0015\u0001\u0000\u0000"+
		"\u0000\u0015\u0017\u0001\u0000\u0000\u0000\u0016\u0014\u0001\u0000\u0000"+
		"\u0000\u0017\u0018\u0005\u0000\u0000\u0001\u0018\u0001\u0001\u0000\u0000"+
		"\u0000\u0019\u001a\u0005\u0007\u0000\u0000\u001a\u001b\u0005\u0014\u0000"+
		"\u0000\u001b\u001c\u0005\u0001\u0000\u0000\u001c\u001d\u0005\u0013\u0000"+
		"\u0000\u001d\u0003\u0001\u0000\u0000\u0000\u001e\u001f\u0007\u0000\u0000"+
		"\u0000\u001f#\u0005\u0014\u0000\u0000 !\u0005\u0006\u0000\u0000!\"\u0005"+
		"\u0001\u0000\u0000\"$\u0005\u0013\u0000\u0000# \u0001\u0000\u0000\u0000"+
		"#$\u0001\u0000\u0000\u0000$(\u0001\u0000\u0000\u0000%&\u0005\u0007\u0000"+
		"\u0000&\'\u0005\u0001\u0000\u0000\')\u0005\u0013\u0000\u0000(%\u0001\u0000"+
		"\u0000\u0000()\u0001\u0000\u0000\u0000)-\u0001\u0000\u0000\u0000*+\u0005"+
		"\b\u0000\u0000+,\u0005\u0001\u0000\u0000,.\u0005\u0013\u0000\u0000-*\u0001"+
		"\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000.2\u0001\u0000\u0000\u0000"+
		"/0\u0005\t\u0000\u000001\u0005\u0001\u0000\u000013\u0005\u0014\u0000\u0000"+
		"2/\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u00003\u0005\u0001\u0000"+
		"\u0000\u00004>\u0005\n\u0000\u00005>\u0005\u000b\u0000\u00006>\u0005\f"+
		"\u0000\u000078\u0005\r\u0000\u00008>\u0005\u0014\u0000\u00009>\u0005\u000e"+
		"\u0000\u0000:;\u0005\u000f\u0000\u0000;>\u0005\u0014\u0000\u0000<>\u0005"+
		"\u0010\u0000\u0000=4\u0001\u0000\u0000\u0000=5\u0001\u0000\u0000\u0000"+
		"=6\u0001\u0000\u0000\u0000=7\u0001\u0000\u0000\u0000=9\u0001\u0000\u0000"+
		"\u0000=:\u0001\u0000\u0000\u0000=<\u0001\u0000\u0000\u0000>\u0007\u0001"+
		"\u0000\u0000\u0000\b\f\u000e\u0014#(-2=";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}