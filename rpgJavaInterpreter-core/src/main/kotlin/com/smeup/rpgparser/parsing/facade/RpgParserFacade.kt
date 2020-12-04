package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.MuteLexer
import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.RpgLexer
import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.createCompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.setOverlayOn
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.utils.parseTreeToXml
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.endPoint
import com.strumenta.kolasu.model.startPoint
import com.strumenta.kolasu.validation.Error
import com.strumenta.kolasu.validation.ErrorType
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode
import org.apache.commons.io.input.BOMInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.system.measureTimeMillis

typealias MutesMap = MutableMap<Int, MuteParser.MuteLineContext>
typealias MutesImmutableMap = Map<Int, MuteParser.MuteLineContext>

open class ParsingResult<C>(val errors: List<Error>, val root: C?) {
    val correct: Boolean
        get() = errors.isEmpty()
}

fun List<Error>.firstLine(): String {
    return this.firstOrNull {
        it.position != null
    }?.position.line()
}

data class ParseTrees(
    val rContext: RContext,
    val muteContexts: MutesImmutableMap? = null
)

class RpgParserResult(errors: List<Error>, root: ParseTrees, private val parser: Parser) :
    ParsingResult<ParseTrees>(errors, root) {
    fun toTreeString(): String = parseTreeToXml(root!!.rContext, parser)
}

typealias RpgLexerResult = ParsingResult<List<Token>>

class RpgParserFacade {

    // Should be 'false' as default to avoid unnecessary search of 'mute annotation' into rpg program source.
    var muteSupport: Boolean = MainExecutionContext.getConfiguration().options?.muteSupport ?: false
    private var muteVerbose = MainExecutionContext.getConfiguration().options?.muteVerbose ?: false

    private val executionProgramName: String by lazy {
        MainExecutionContext.getExecutionProgramName().let {
            val name = File(it).name.replaceAfterLast(".", "")
            if (name.endsWith(".")) {
                name.substring(0, name.length - 1)
            } else {
                name
            }
        }
    }

    private fun inputStreamWithLongLines(inputStream: InputStream, threshold: Int = 80): CharStream {
        val code = inputStreamToString(inputStream)
        val lines = code.lines()
        val longLines = lines.map { it.padEnd(threshold) }
        val paddedCode = longLines.joinToString(System.lineSeparator())
        return CharStreams.fromStream(paddedCode.byteInputStream(StandardCharsets.UTF_8))
    }

    private fun inputStreamToString(inputStream: InputStream): String =
        inputStream.bufferedReader().use(BufferedReader::readText)

    fun lex(inputStream: InputStream): RpgLexerResult {
        val errors = LinkedList<Error>()
        val lexer = RpgLexer(inputStreamWithLongLines(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(
                p0: Recognizer<*, *>?,
                p1: Any?,
                line: Int,
                charPositionInLine: Int,
                errorMessage: String?,
                p5: RecognitionException?
            ) {
                errors.add(
                    Error(
                        ErrorType.LEXICAL, errorMessage
                            ?: "unspecified", position = Point(line, charPositionInLine).asPosition
                    )
                )
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
        val lexer =
            MuteLexer(if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(
                p0: Recognizer<*, *>?,
                p1: Any?,
                line: Int,
                charPositionInLine: Int,
                errorMessage: String?,
                p5: RecognitionException?
            ) {
                errors.add(
                    Error(
                        ErrorType.LEXICAL, errorMessage
                            ?: "unspecified", position = Point(line, charPositionInLine).asPosition
                    )
                )
            }
        })
        val commonTokenStream = CommonTokenStream(lexer)
        val parser = MuteParser(commonTokenStream)
        parser.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(
                p0: Recognizer<*, *>?,
                p1: Any?,
                p2: Int,
                p3: Int,
                errorMessage: String?,
                p5: RecognitionException?
            ) {
                errors.add(
                    Error(
                        ErrorType.SYNTACTIC, errorMessage
                            ?: "unspecified"
                    )
                )
            }
        })
        parser.removeErrorListeners()
        return parser
    }

    fun createParser(inputStream: InputStream, errors: MutableList<Error>, longLines: Boolean): RpgParser {
        MainExecutionContext.log(RpgLoadLogStart(executionProgramName))
        val charInput: CharStream?
        val elapsedLoad = measureTimeMillis {
            charInput = if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream)
        }
        MainExecutionContext.log(RpgLoadLogEnd(executionProgramName, elapsedLoad, charInput))
        MainExecutionContext.log(LexerLogStart(executionProgramName))
        val lexer: RpgLexer
        val elapsedLexer = measureTimeMillis {
            lexer = RpgLexer(charInput)
            lexer.removeErrorListeners()
            lexer.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(
                    p0: Recognizer<*, *>?,
                    p1: Any?,
                    line: Int,
                    charPositionInLine: Int,
                    errorMessage: String?,
                    p5: RecognitionException?
                ) {
                    errors.add(
                        Error(
                            ErrorType.LEXICAL, errorMessage
                                ?: "unspecified", position = Point(line, charPositionInLine).asPosition
                        )
                    )
                }
            })
        }
        MainExecutionContext.log(LexerLogEnd(executionProgramName, elapsedLexer))
        MainExecutionContext.log(ParserLogStart(executionProgramName))
        val parser: RpgParser
        val elapsedParser = measureTimeMillis {
            val commonTokenStream = CommonTokenStream(lexer)
            parser = RpgParser(commonTokenStream)
            parser.removeErrorListeners()
            parser.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(
                    p0: Recognizer<*, *>?,
                    p1: Any?,
                    p2: Int,
                    p3: Int,
                    errorMessage: String?,
                    p5: RecognitionException?
                ) {
                    errors.add(
                        Error(
                            ErrorType.SYNTACTIC, errorMessage
                                ?: "unspecified"
                        )
                    )
                }
            })
        }
        MainExecutionContext.log(ParserLogEnd(executionProgramName, elapsedParser))
        return parser
    }

    private fun verifyParseTree(parser: Parser, errors: MutableList<Error>, root: ParserRuleContext) {
        MainExecutionContext.log(CheckParseTreeLogStart(executionProgramName))
        val elapsed = measureTimeMillis {
            val commonTokenStream = parser.tokenStream as CommonTokenStream
            val lastToken = commonTokenStream.get(commonTokenStream.index())
            if (lastToken.type != Token.EOF) {
                errors.add(Error(ErrorType.SYNTACTIC, "Not whole input consumed", lastToken!!.endPoint.asPosition))
            }

            root.processDescendantsAndErrors({
                if (it.exception != null) {
                    errors.add(
                        Error(
                            ErrorType.SYNTACTIC,
                            "Recognition exception: ${it.exception.message}",
                            it.start.startPoint.asPosition
                        )
                    )
                }
            }, {
                errors.add(Error(ErrorType.SYNTACTIC, "Error node found", it.toPosition(true)))
            })
        }
        MainExecutionContext.log(CheckParseTreeLogEnd(executionProgramName, elapsed))
    }

    private fun parseMute(code: String, errors: MutableList<Error>): MuteParser.MuteLineContext {
        val muteParser = createMuteParser(
            BOMInputStream(code.byteInputStream(Charsets.UTF_8)), errors,
            longLines = true
        )
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
        MainExecutionContext.log(FindMutesLogStart(executionProgramName))
        val mutes: MutesMap = HashMap()
        val elapsed = measureTimeMillis {
            val lexResult = lex(BOMInputStream(code))
            errors.addAll(lexResult.errors)
            lexResult.root?.forEachIndexed { index, token0 ->
                if (index + 2 < lexResult.root.size) {
                    val token1 = lexResult.root[index + 1]
                    val token2 = lexResult.root[index + 2]
                    // Please note the leading spaces added
                    if (token0.type == LEAD_WS5_Comments && token0.text == "".padStart(4) + "M" &&
                        token1.type == COMMENT_SPEC_FIXED && token1.text == "U*" &&
                        token2.type == COMMENTS_TEXT
                    ) {
                        // Please note the leading spaces added to the token
                        var preproc = preprocess(token2.text)
                        mutes[token2.line] = parseMute("".padStart(8) + preproc, errors)
                    }
                }
            }
        }
        MainExecutionContext.log(FindMutesLogEnd(executionProgramName, elapsed))
        return mutes
    }

    fun parse(inputStream: InputStream): RpgParserResult {
        val parserResult: RpgParserResult
        val errors = LinkedList<Error>()
        val code = inputStreamToString(inputStream)
        val parser = createParser(BOMInputStream(code.byteInputStream(Charsets.UTF_8)), errors, longLines = true)
        val root: RContext
        MainExecutionContext.log(RContextLogStart(executionProgramName))
        val elapsedRoot = measureTimeMillis {
            root = parser.r()
        }
        MainExecutionContext.log(RContextLogEnd(executionProgramName, elapsedRoot))
        var mutes: MutesImmutableMap? = null
        if (muteSupport) {
            mutes = findMutes(code, errors)
        }
        verifyParseTree(parser, errors, root)
        parserResult = RpgParserResult(errors, ParseTrees(root, mutes), parser)
        return parserResult
    }

    private fun tryToLoadCompilationUnit(): CompilationUnit? {
        return MainExecutionContext.getConfiguration().options?.compiledProgramsDir?.let { compiledDir ->
            val start = System.currentTimeMillis()
            val compiledFile = File(compiledDir, "$executionProgramName.bin")
            if (compiledFile.exists()) {
                MainExecutionContext.log(AstLogStart(executionProgramName))
                compiledFile.readBytes().createCompilationUnit().apply {
                    MainExecutionContext.log(AstLogEnd(executionProgramName, System.currentTimeMillis() - start))
                }
            } else {
                null
            }
        }?.apply {
            dataDefinitions.forEach { dataDefinition ->
                dataDefinition.fields.forEach { fieldDefinition ->
                    dataDefinition.setOverlayOn(fieldDefinition)
                }
            }
        }
    }

    private fun createAst(
        inputStream: InputStream
    ): CompilationUnit {
        val result = parse(inputStream)
        require(result.correct) { "Errors: ${result.errors.joinToString(separator = ", ")}" }
        val compilationUnit: CompilationUnit
        MainExecutionContext.log(AstLogStart(executionProgramName))
        val elapsed = measureTimeMillis {
            compilationUnit = result.root!!.rContext.toAst(
                MainExecutionContext.getConfiguration().options?.toAstConfiguration ?: ToAstConfiguration()
            ).apply {
                if (muteSupport) {
                    val resolved = this.injectMuteAnnotation(result.root.muteContexts!!)
                    if (muteVerbose) {
                        val sorted = resolved.sortedWith(compareBy { it.muteLine })
                        sorted.forEach {
                            println("Mute annotation at line ${it.muteLine} attached to statement ${it.statementLine}")
                        }
                    }
                }
            }
        }
        MainExecutionContext.log(AstLogEnd(executionProgramName, elapsed))
        return compilationUnit
    }

    fun parseAndProduceAst(
        inputStream: InputStream
    ): CompilationUnit {
        return (tryToLoadCompilationUnit() ?: createAst(inputStream)).apply {
            MainExecutionContext.getConfiguration().jarikoCallback.afterAstCreation.invoke(this)
        }
    }

    fun parseExpression(
        inputStream: InputStream,
        longLines: Boolean = true,
        printTree: Boolean = false
    ): ParsingResult<ExpressionContext> {
        // Nothing to do with Mute support, as annotations can be only on statements
        val errors = LinkedList<Error>()
        val parser = createParser(inputStream, errors, longLines = longLines)
        val root = parser.expression()
        if (printTree) println(parseTreeToXml(root, parser))
        verifyParseTree(parser, errors, root)
        return ParsingResult(errors, root)
    }

    fun parseStatement(inputStream: InputStream, longLines: Boolean = true): ParsingResult<StatementContext> {
        val errors = LinkedList<Error>()
        val parser = createParser(inputStream, errors, longLines = longLines)
        val root = parser.statement()
        verifyParseTree(parser, errors, root)
        val result = ParsingResult(errors, root)
        val mutes: MutesMap?
        if (muteSupport && result.correct) {
            inputStream.reset()
            mutes = findMutes(inputStream, errors)
            result.root!!.toAst().apply {
                this.injectMuteAnnotation(mutes)
            }
        }
        return result
    }
}

private fun TerminalNode.toPosition(considerPosition: Boolean = true): Position? {
    return if (considerPosition) {
        Position(this.symbol.startPoint, this.symbol.endPoint)
    } else {
        null
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

fun <T : ParserRuleContext> ParserRuleContext.findAllDescendants(
    type: KClass<T>,
    includingMe: Boolean = true
): List<T> {
    val list = LinkedList<T>()
    this.processDescendants({
        if (type.isInstance(it)) {
            list.add(type.cast(it))
        }
    }, includingMe)
    return list
}

fun ParserRuleContext.processDescendantsAndErrors(
    operationOnParserRuleContext: (ParserRuleContext) -> Unit,
    operationOnError: (ErrorNode) -> Unit,
    includingMe: Boolean = true
) {
    if (includingMe) {
        operationOnParserRuleContext(this)
    }
    if (this.children != null) {
        this.children.filterIsInstance(ParserRuleContext::class.java).forEach {
            it.processDescendantsAndErrors(operationOnParserRuleContext, operationOnError, includingMe = true)
        }
        this.children.filterIsInstance(ErrorNode::class.java).forEach {
            operationOnError(it)
        }
    }
}
