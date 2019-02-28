package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.RContext
import me.tomassetti.kolasu.mapping.toPosition
import me.tomassetti.kolasu.model.Point
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ErrorNode
import java.io.InputStream
import java.util.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets


data class ParsingResult<C>(val errors: List<Error>, val root: C?) {
    val correct : Boolean
        get() = errors.isEmpty()
}

typealias RpgParserResult = ParsingResult<RContext>
typealias RpgLexerResult = ParsingResult<List<Token>>

class RpgParserFacade {

    private fun inputStreamWithLongLines(inputStream: InputStream, threshold: Int = 80) : ANTLRInputStream {
        val code = inputStreamToString(inputStream)
        val lines = code.lines()
        val longLines = lines.map { it.padEnd(threshold) }
        val paddedCode = longLines.joinToString(System.lineSeparator())
        return ANTLRInputStream(paddedCode.byteInputStream(StandardCharsets.UTF_8))
    }

    private fun inputStreamToString(inputStream: InputStream): String {
        ByteArrayOutputStream().use { result ->
            val buffer = ByteArray(1024)
            var length: Int
            do {
                length = inputStream.read(buffer)
                if (length == -1) {
                    break
                } else {
                    result.write(buffer, 0, length)
                }
            } while (true)

            return result.toString(Charsets.UTF_8.name())
        }
    }

    fun lex(inputStream: InputStream) : RpgLexerResult {
        val errors = LinkedList<Error>()
        val lexer = RpgLexer(inputStreamWithLongLines(inputStream))
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
        val lexer = RpgLexer(inputStreamWithLongLines(inputStream))
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
                errors.add(Error(ErrorType.SYNTACTIC, "Recognition exception: ${it.exception.message}", it.toPosition(true)))
            } else if (it is ErrorNode) {
                errors.add(Error(ErrorType.SYNTACTIC, "Error node found", it.toPosition(true)))
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
