package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.RContext
import me.tomassetti.kolasu.mapping.toPosition
import me.tomassetti.kolasu.model.Point
import org.antlr.v4.runtime.*
import java.io.InputStream
import java.util.*

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
                errors.add(Error(ErrorType.LEXICAL, errorMessage ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
            }
        })
        val tokens = LinkedList<Token>()
        do {
            val t = lexer.nextToken()
            if (t == null) {
                break
            } else {
                tokens.add(t)
            }
        } while (t.type != Token.EOF)

        if (tokens.last.type != Token.EOF) {
            errors.add(Error(ErrorType.SYNTACTIC, "Not whole input consumed", tokens.last!!.endPoint.asPosition))
        }

        return RpgLexerResult(errors, tokens)
    }

    fun parse(inputStream: InputStream) : RpgParserResult {
        val errors = LinkedList<Error>()
        val lexer = RpgLexer(ANTLRInputStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.LEXICAL, errorMessage ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
            }
        })
        val commonTokenStream = CommonTokenStream(lexer)
        val parser = RpgParser(commonTokenStream)
        parser.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, p2: Int, p3: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.SYNTACTIC, errorMessage ?: "unspecified"))
            }
        })
        parser.removeErrorListeners()
        val root = parser.r()

        val lastToken = commonTokenStream.get(commonTokenStream.index())
        if (lastToken.type != Token.EOF) {
            errors.add(Error(ErrorType.SYNTACTIC, "Not whole input consumed", lastToken!!.endPoint.asPosition))
        }

        root.processDescendants({
            if (it.exception != null) {
                errors.add(Error(ErrorType.SYNTACTIC, "Recognition exception", it.toPosition(true)))
            }
        })


        return RpgParserResult(errors, root)
    }

}

fun ParserRuleContext.processDescendants(operation: (ParserRuleContext) -> Unit, includingMe: Boolean = true) {
    if (includingMe) {
        operation(this)
    }
    this.children.filterIsInstance(ParserRuleContext::class.java).forEach { it.processDescendants(operation) }
}
