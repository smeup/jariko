// Generated from MuteLexer.g4 by ANTLR 4.7.1
package com.smeup.rpgparser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MuteLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OPEN_PAREN=1, CLOSE_PAREN=2, VAL1=3, VAL2=4, EQ=5, NE=6, GT=7, GE=8, LT=9, 
		LE=10, COMP=11, TYPE=12, WS=13, EXP=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"OPEN_PAREN", "CLOSE_PAREN", "VAL1", "VAL2", "EQ", "NE", "GT", "GE", "LT", 
		"LE", "COMP", "TYPE", "WS", "EXP"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'VAL1'", "'VAL2'", "'(EQ)'", "'(NE)'", "'(GT)'", 
		"'(GE)'", "'(LT)'", "'(LE)'", "'COMP'", "'Type'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OPEN_PAREN", "CLOSE_PAREN", "VAL1", "VAL2", "EQ", "NE", "GT", "GE", 
		"LT", "LE", "COMP", "TYPE", "WS", "EXP"
	};
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


	public MuteLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MuteLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20e\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b"+
		"\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\6\16W\n\16\r\16"+
		"\16\16X\3\16\3\16\3\17\3\17\7\17_\n\17\f\17\16\17b\13\17\3\17\3\17\3`"+
		"\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\3\2\3\5\2\13\f\17\17\"\"\2f\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\3\37\3\2\2\2\5!\3\2\2\2\7#\3\2\2\2\t(\3\2\2\2\13-\3\2\2\2\r\62\3\2\2"+
		"\2\17\67\3\2\2\2\21<\3\2\2\2\23A\3\2\2\2\25F\3\2\2\2\27K\3\2\2\2\31P\3"+
		"\2\2\2\33V\3\2\2\2\35\\\3\2\2\2\37 \7*\2\2 \4\3\2\2\2!\"\7+\2\2\"\6\3"+
		"\2\2\2#$\7X\2\2$%\7C\2\2%&\7N\2\2&\'\7\63\2\2\'\b\3\2\2\2()\7X\2\2)*\7"+
		"C\2\2*+\7N\2\2+,\7\64\2\2,\n\3\2\2\2-.\7*\2\2./\7G\2\2/\60\7S\2\2\60\61"+
		"\7+\2\2\61\f\3\2\2\2\62\63\7*\2\2\63\64\7P\2\2\64\65\7G\2\2\65\66\7+\2"+
		"\2\66\16\3\2\2\2\678\7*\2\289\7I\2\29:\7V\2\2:;\7+\2\2;\20\3\2\2\2<=\7"+
		"*\2\2=>\7I\2\2>?\7G\2\2?@\7+\2\2@\22\3\2\2\2AB\7*\2\2BC\7N\2\2CD\7V\2"+
		"\2DE\7+\2\2E\24\3\2\2\2FG\7*\2\2GH\7N\2\2HI\7G\2\2IJ\7+\2\2J\26\3\2\2"+
		"\2KL\7E\2\2LM\7Q\2\2MN\7O\2\2NO\7R\2\2O\30\3\2\2\2PQ\7V\2\2QR\7{\2\2R"+
		"S\7r\2\2ST\7g\2\2T\32\3\2\2\2UW\t\2\2\2VU\3\2\2\2WX\3\2\2\2XV\3\2\2\2"+
		"XY\3\2\2\2YZ\3\2\2\2Z[\b\16\2\2[\34\3\2\2\2\\`\7]\2\2]_\13\2\2\2^]\3\2"+
		"\2\2_b\3\2\2\2`a\3\2\2\2`^\3\2\2\2ac\3\2\2\2b`\3\2\2\2cd\7_\2\2d\36\3"+
		"\2\2\2\5\2X`\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}