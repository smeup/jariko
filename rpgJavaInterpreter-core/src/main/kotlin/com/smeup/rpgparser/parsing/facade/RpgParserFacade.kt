package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.MuteLexer
import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.RpgLexer
import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.interpreter.line
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.endPoint
import com.strumenta.kolasu.model.startPoint
import com.strumenta.kolasu.validation.Error
import com.strumenta.kolasu.validation.ErrorType
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ErrorNode
import org.apache.commons.io.input.BOMInputStream

typealias MutesMap = MutableMap<Int, MuteParser.MuteLineContext>
typealias MutesImmutableMap = Map<Int, MuteParser.MuteLineContext>

data class ParsingResult<C>(val errors: List<Error>, val root: C?) {
    val correct: Boolean
        get() = errors.isEmpty()
}

fun List<Error>.firstLine(): String {
    return this.firstOrNull {
        it.position != null
    }?.position.line() ?: "unknown"
}

data class ParseTrees(
    val rContext: RContext,
    val muteContexts: MutesImmutableMap? = null
)

typealias RpgParserResult = ParsingResult<ParseTrees>
typealias RpgLexerResult = ParsingResult<List<Token>>

class RpgParserFacade {

    var muteSupport: Boolean = true

    private fun inputStreamWithLongLines(inputStream: InputStream, threshold: Int = 80): ANTLRInputStream {
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

    fun lex(inputStream: InputStream): RpgLexerResult {
        val errors = LinkedList<Error>()
        val lexer = RpgLexer(inputStreamWithLongLines(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.LEXICAL, errorMessage
                        ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
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

    fun createMuteParser(inputStream: InputStream, errors: MutableList<Error>, longLines: Boolean): MuteParser {
        val lexer = MuteLexer(if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.LEXICAL, errorMessage
                        ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
            }
        })
        val commonTokenStream = CommonTokenStream(lexer)
        val parser = MuteParser(commonTokenStream)
        parser.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, p2: Int, p3: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.SYNTACTIC, errorMessage
                        ?: "unspecified"))
            }
        })
        parser.removeErrorListeners()
        return parser
    }

    fun createParser(inputStream: InputStream, errors: MutableList<Error>, longLines: Boolean): RpgParser {
        val lexer = RpgLexer(if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.LEXICAL, errorMessage
                        ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
            }
        })
        val commonTokenStream = CommonTokenStream(lexer)
        val parser = RpgParser(commonTokenStream)
        parser.removeErrorListeners()
        parser.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, p2: Int, p3: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.SYNTACTIC, errorMessage
                        ?: "unspecified"))
            }
        })
        return parser
    }

    private fun verifyParseTree(parser: Parser, errors: MutableList<Error>, root: ParserRuleContext) {
        val commonTokenStream = parser.tokenStream as CommonTokenStream
        val lastToken = commonTokenStream.get(commonTokenStream.index())
        if (lastToken.type != Token.EOF) {
            errors.add(Error(ErrorType.SYNTACTIC, "Not whole input consumed", lastToken!!.endPoint.asPosition))
        }

        root.processDescendants({
            if (it.exception != null) {
                errors.add(Error(ErrorType.SYNTACTIC, "Recognition exception: ${it.exception.message}", it.start.startPoint.asPosition))
            } else if (it is ErrorNode) {
                errors.add(Error(ErrorType.SYNTACTIC, "Error node found", it.toPosition(true)))
            }
        })
    }

    private fun parseMute(code: String, errors: MutableList<Error>): MuteParser.MuteLineContext {
        val muteParser = createMuteParser(BOMInputStream(code.byteInputStream(Charsets.UTF_8)), errors,
                longLines = true)
        val root = muteParser.muteLine()
        verifyParseTree(muteParser, errors, root)
        return root
    }

    private fun preprocess(text: String): String {
        var s = text
        var level = 0
        var end = s.lastIndexOf("COMP")

        s.forEachIndexed { i, c ->
            if (i < end) {
                if (c == '(') {
                    level++
                    if (level == 1) {
                        s = s.replaceRange(i, i + 1, "[")
                    }
                }
                if (c == ')') {
                    if (level == 1) {
                        s = s.replaceRange(i, i + 1, "]")
                    }
                    if (level > 0) level--
                }
            }
        }
        return s
    }

    private fun findMutes(code: String, errors: MutableList<Error>) =
            findMutes(code.byteInputStream(Charsets.UTF_8), errors)

    private fun findMutes(code: InputStream, errors: MutableList<Error>): MutesMap {
        val lexResult = lex(BOMInputStream(code))
        errors.addAll(lexResult.errors)
        // Find sequence 3, 5, 590
        val mutes: MutesMap = HashMap()
        lexResult.root?.forEachIndexed { index, token0 ->
            if (index + 2 < lexResult.root.size) {
                val token1 = lexResult.root[index + 1]
                val token2 = lexResult.root[index + 2]
                // Please note the leading spaces added
                if (token0.type == LEAD_WS5_Comments && token0.text == "".padStart(4) + "M" &&
                        token1.type == COMMENT_SPEC_FIXED && token1.text == "U*" &&
                        token2.type == COMMENTS_TEXT) {
                    // Please note the leading spaces added to the token
                    var preproc = preprocess(token2.text)
                    mutes[token2.line] = parseMute("".padStart(8) + preproc, errors)
                }
            }
        }
        return mutes
    }

    fun parse(inputStream: InputStream): RpgParserResult {
        val errors = LinkedList<Error>()
        val code = inputStreamToString(inputStream)
        val parser = createParser(BOMInputStream(code.byteInputStream(Charsets.UTF_8)), errors, longLines = true)
        val root = parser.r()
        var mutes: MutesImmutableMap? = null
        if (muteSupport) {
            mutes = findMutes(code, errors)
        }
        verifyParseTree(parser, errors, root)
        return RpgParserResult(errors, ParseTrees(root, mutes))
    }

    fun parseAndProduceAst(inputStream: InputStream): CompilationUnit {
        val result = RpgParserFacade().parse(inputStream)
        require(result.correct) { "Errors: ${result.errors.joinToString(separator = ", ")}" }
        return result.root!!.rContext.toAst().apply {
            if (muteSupport) {
                this.injectMuteAnnotation(result.root.muteContexts!!)
            }
        }
    }

    fun parseExpression(inputStream: InputStream, longLines: Boolean = true): ParsingResult<ExpressionContext> {
        // Nothing to do with Mute support, as annotations can be only on statements
        val errors = LinkedList<Error>()
        val parser = createParser(inputStream, errors, longLines = longLines)
        val root = parser.expression()
        verifyParseTree(parser, errors, root)
        return ParsingResult(errors, root)
    }

    fun parseStatement(inputStream: InputStream, longLines: Boolean = true): ParsingResult<StatementContext> {
        val errors = LinkedList<Error>()
        val parser = createParser(inputStream, errors, longLines = longLines)
        val root = parser.statement()
        verifyParseTree(parser, errors, root)
        val result = ParsingResult(errors, root)
        var mutes: MutesMap?
        if (muteSupport) {
            inputStream.reset()
            mutes = findMutes(inputStream, errors)
            result.root!!.toAst().apply {
                this.injectMuteAnnotation(mutes)
            }
        }
        return result
    }
}

fun ParserRuleContext.processDescendants(operation: (ParserRuleContext) -> Unit, includingMe: Boolean = true) {
    if (includingMe) {
        operation(this)
    }
    if (this.children != null) {
        this.children.filterIsInstance(ParserRuleContext::class.java).forEach { it.processDescendants(operation) }
    }
}
