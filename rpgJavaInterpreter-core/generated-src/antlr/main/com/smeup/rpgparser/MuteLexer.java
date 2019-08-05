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
		OPEN_PAREN=1, CLOSE_PAREN=2, EQUAL=3, VAL1=4, VAL2=5, NOXMI=6, EQ=7, NE=8, 
		GT=9, GE=10, LT=11, LE=12, COMP=13, TYPE=14, WS=15, EXP=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"OPEN_PAREN", "CLOSE_PAREN", "EQUAL", "VAL1", "VAL2", "NOXMI", "EQ", "NE", 
		"GT", "GE", "LT", "LE", "COMP", "TYPE", "WS", "EXP"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'='", "'VAL1'", "'VAL2'", "'\"NOXMI\"'", "'(EQ)'", 
		"'(NE)'", "'(GT)'", "'(GE)'", "'(LT)'", "'(LE)'", "'COMP'", "'Type'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OPEN_PAREN", "CLOSE_PAREN", "EQUAL", "VAL1", "VAL2", "NOXMI", "EQ", 
		"NE", "GT", "GE", "LT", "LE", "COMP", "TYPE", "WS", "EXP"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22s\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\3"+
		"\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3"+
		"\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3"+
		"\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\6\20e\n\20\r"+
		"\20\16\20f\3\20\3\20\3\21\3\21\7\21m\n\21\f\21\16\21p\13\21\3\21\3\21"+
		"\3n\2\22\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22\3\2\3\5\2\13\f\17\17\"\"\2t\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\3#\3\2\2\2\5%\3\2\2\2\7\'\3\2\2"+
		"\2\t)\3\2\2\2\13.\3\2\2\2\r\63\3\2\2\2\17;\3\2\2\2\21@\3\2\2\2\23E\3\2"+
		"\2\2\25J\3\2\2\2\27O\3\2\2\2\31T\3\2\2\2\33Y\3\2\2\2\35^\3\2\2\2\37d\3"+
		"\2\2\2!j\3\2\2\2#$\7*\2\2$\4\3\2\2\2%&\7+\2\2&\6\3\2\2\2\'(\7?\2\2(\b"+
		"\3\2\2\2)*\7X\2\2*+\7C\2\2+,\7N\2\2,-\7\63\2\2-\n\3\2\2\2./\7X\2\2/\60"+
		"\7C\2\2\60\61\7N\2\2\61\62\7\64\2\2\62\f\3\2\2\2\63\64\7$\2\2\64\65\7"+
		"P\2\2\65\66\7Q\2\2\66\67\7Z\2\2\678\7O\2\289\7K\2\29:\7$\2\2:\16\3\2\2"+
		"\2;<\7*\2\2<=\7G\2\2=>\7S\2\2>?\7+\2\2?\20\3\2\2\2@A\7*\2\2AB\7P\2\2B"+
		"C\7G\2\2CD\7+\2\2D\22\3\2\2\2EF\7*\2\2FG\7I\2\2GH\7V\2\2HI\7+\2\2I\24"+
		"\3\2\2\2JK\7*\2\2KL\7I\2\2LM\7G\2\2MN\7+\2\2N\26\3\2\2\2OP\7*\2\2PQ\7"+
		"N\2\2QR\7V\2\2RS\7+\2\2S\30\3\2\2\2TU\7*\2\2UV\7N\2\2VW\7G\2\2WX\7+\2"+
		"\2X\32\3\2\2\2YZ\7E\2\2Z[\7Q\2\2[\\\7O\2\2\\]\7R\2\2]\34\3\2\2\2^_\7V"+
		"\2\2_`\7{\2\2`a\7r\2\2ab\7g\2\2b\36\3\2\2\2ce\t\2\2\2dc\3\2\2\2ef\3\2"+
		"\2\2fd\3\2\2\2fg\3\2\2\2gh\3\2\2\2hi\b\20\2\2i \3\2\2\2jn\7]\2\2km\13"+
		"\2\2\2lk\3\2\2\2mp\3\2\2\2no\3\2\2\2nl\3\2\2\2oq\3\2\2\2pn\3\2\2\2qr\7"+
		"_\2\2r\"\3\2\2\2\5\2fn\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}