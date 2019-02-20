package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.RContext
import org.antlr.v4.runtime.*
import java.io.InputStream
import java.util.*

data class Point(val line: Int, val charPositionInLine: Int)

data class Position(val start: Point, val end: Point)

enum class ErrorType {
    LEXICAL,
    SYNTACTIC,
    SEMANTIC
}

data class Error(val type: ErrorType, val message: String, val point: Point? = null)

data class ParsingResult<C>(val errors: List<Error>, val root: C?) {
    val correct : Boolean
        get() = errors.isEmpty()
}

typealias RpgParserResult = ParsingResult<RContext>
typealias RpgLexerResult = ParsingResult<List<Token>>

class RpgParserFacade {

    fun lex(inputStream: InputStream) : RpgLexerResult {
        val errors = LinkedList<Error>()
        val lexer = RpgLexer(ANTLRInputStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.LEXICAL, errorMessage ?: "unspecified", point = Point(line, charPositionInLine)))
            }
        })
        val tokens = LinkedList<Token>()
        do {
            val t = lexer.nextToken()
            tokens.add(t)
        } while (t.type != -1)
        return RpgLexerResult(errors, tokens)
    }

    fun parse(inputStream: InputStream) : RpgParserResult {
        val errors = LinkedList<Error>()
        val lexer = RpgLexer(ANTLRInputStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.LEXICAL, errorMessage ?: "unspecified", point = Point(line, charPositionInLine)))
            }
        })
        val parser = RpgParser(CommonTokenStream(lexer))
        parser.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, p2: Int, p3: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.SYNTACTIC, errorMessage ?: "unspecified"))
            }
        })
        parser.removeErrorListeners()
        val root = parser.r()
        return RpgParserResult(errors, root)
    }

}
