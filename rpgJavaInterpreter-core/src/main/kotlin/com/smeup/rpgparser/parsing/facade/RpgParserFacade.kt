/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.MuteLexer
import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.RpgLexer
import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.ErrorEventSource
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.execution.ParsingProgram
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.ast.createCompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.setOverlayOn
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.utils.insLineNumber
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
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.measureTime

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
    val muteContexts: MutesImmutableMap? = null,
    val copyBlocks: CopyBlocks? = null
)

class RpgParserResult(errors: List<Error>, root: ParseTrees, private val parser: Parser, val src: String) : ParsingResult<ParseTrees>(errors, root) {

    fun toTreeString(): String = parseTreeToXml(root!!.rContext, parser)

    fun dumpError(): String {
        return if (MainExecutionContext.getConfiguration().options.mustDumpSource()) {
            "${errors.dumpError(root!!.copyBlocks)}\n${src.dumpSource()}"
        } else {
            errors.dumpError(root!!.copyBlocks)
        }
    }

    internal fun fireErrorEvents() {
        errors.fireErrorEvents()
    }
}

fun String.dumpSource(): String {
    val parsingProgramName = if (MainExecutionContext.getParsingProgramStack().isNotEmpty()) {
        MainExecutionContext.getParsingProgramStack().peek().name
    } else {
        getExecutionProgramNameWithNoExtension()
    }
    val programName = parsingProgramName.let {
        if (it.lines().size > 1) {
            "PROGRAM NAME NOT SET"
        } else {
            it
        }
    }
    val header = "********* SRC $programName"
    val src = this.insLineNumber(5) {
        // for now return al lines
        // linesInError.contains(it)
        true
    }
    return "$header\n$src"
}

typealias RpgLexerResult = ParsingResult<List<Token>>

@OptIn(ExperimentalTime::class)
class RpgParserFacade {

    // Should be 'false' as default to avoid unnecessary search of 'mute annotation' into rpg program source.
    var muteSupport: Boolean = MainExecutionContext.getConfiguration().options.muteSupport
    private var muteVerbose = MainExecutionContext.getConfiguration().options.muteVerbose

    private val executionProgramName: String by lazy {
        getExecutionProgramNameWithNoExtension()
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
        val logSource = LogSourceData(executionProgramName, "")
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "RPGLOAD", "START"))
        val charInput: CharStream?
        val elapsedLoad = measureTime {
            charInput = if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream)
        }

        val lines = charInput?.toString()?.split(Pattern.compile("\\r\\n|\\r|\\n"))?.size ?: 0
        val endLogSource = logSource.projectLine(lines.toString())

        MainExecutionContext.log(LazyLogEntry.produceStatement(endLogSource, "RPGLOAD", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformance(endLogSource, "RPGLOAD", elapsedLoad))

        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "LEXER", "START"))

        val lexer: RpgLexer
        val elapsedLexer = measureTime {
            lexer = RpgLexer(charInput)
            lexer.removeErrorListeners()
            lexer.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                    errors.add(Error(ErrorType.LEXICAL, errorMessage
                        ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
                }
            })
        }

        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "LEXER", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformance(logSource, "LEXER", elapsedLexer))

        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "PARSER", "START"))
        val parser: RpgParser
        val elapsedParser = measureTime {
            val commonTokenStream = CommonTokenStream(lexer)
            parser = RpgParser(commonTokenStream)
            parser.removeErrorListeners()
            parser.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                    errors.add(Error(ErrorType.SYNTACTIC, errorMessage
                        ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
                }
            })
        }
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "LEXER", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformance(logSource, "LEXER", elapsedParser))
        return parser
    }

    private fun verifyParseTree(parser: Parser, errors: MutableList<Error>, root: ParserRuleContext) {
        val logSource = LogSourceData(executionProgramName, "")
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "CHKPTREE", "START"))
        val elapsed = measureTime {
            val commonTokenStream = parser.tokenStream as CommonTokenStream
            val lastToken = commonTokenStream.get(commonTokenStream.index())
            if (lastToken.type != Token.EOF) {
                errors.add(Error(ErrorType.SYNTACTIC, "Not whole input consumed", lastToken!!.endPoint.asPosition))
            }

            root.processDescendantsAndErrors({
                if (it.exception != null) {
                    errors.add(Error(ErrorType.SYNTACTIC, "Recognition exception: ${it.exception.message}", it.start.startPoint.asPosition))
                }
            }, {
                errors.add(Error(ErrorType.SYNTACTIC, "Error node found", it.toPosition(true)))
            })
        }
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "CHKPTREE", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformance(logSource, "CHKPTREE", elapsed))
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
        val end = s.lastIndexOf("COMP")

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
        val logSource = LogSourceData(executionProgramName, "")
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "FINDMUTES", "START"))
        val mutes: MutesMap = HashMap()
        val elapsed = measureTime {
            val lexResult = lex(BOMInputStream(code))
            errors.addAll(lexResult.errors)
            lexResult.root?.forEachIndexed { index, token0 ->
                if (index + 2 < lexResult.root.size) {
                    val token1 = lexResult.root[index + 1]
                    val token2 = lexResult.root[index + 2]
                    // Please note the leading spaces added
                    if (token0.type == LEAD_WS5_Comments && token0.text == "".padStart(4) + "M" &&
                        token1.type == COMMENT_SPEC_FIXED && token1.text == "U*" &&
                        token2.type == COMMENTS_TEXT) {
                        // Please note the leading spaces added to the token
                        val preproc = preprocess(token2.text)
                        mutes[token2.line] = parseMute("".padStart(8) + preproc, errors)
                    }
                }
            }
        }
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "FINDMUTES", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformance(logSource, "FINDMUTES", elapsed))
        return mutes
    }

    fun parse(inputStream: InputStream): RpgParserResult {
        val parserResult: RpgParserResult
        val logSource = LogSourceData(executionProgramName, "")
        val errors = LinkedList<Error>()
        val copyBlocks: CopyBlocks? = if (MainExecutionContext.getConfiguration().options.mustCreateCopyBlocks()) CopyBlocks() else null
        // val includedCopySet = mutableSetOf<CopyId>()
        val code = inputStream.preprocess(
            findCopy = { copyId ->
                MainExecutionContext.getSystemInterface()?.findCopy(copyId)?.source.let { source ->
                    MainExecutionContext.getConfiguration().jarikoCallback.beforeCopyInclusion(copyId, source)
                    source
                }
            },
            onStartInclusion = { copyId, start -> copyBlocks?.onStartCopyBlock(copyId = copyId, start = start) },
            onEndInclusion = { end -> copyBlocks?.onEndCopyBlock(end = end) }
            // beforeInclusion = { copyId -> includedCopySet.add(copyId) }
        ).apply {
            if (copyBlocks != null) MainExecutionContext.getConfiguration().jarikoCallback.afterCopiesInclusion(copyBlocks)
        }.let { code ->
            MainExecutionContext.getConfiguration().jarikoCallback.beforeParsing.invoke(code)
        }
        if (!MainExecutionContext.getParsingProgramStack().empty()) {
            MainExecutionContext.getParsingProgramStack().peek().copyBlocks = copyBlocks
            MainExecutionContext.getParsingProgramStack().peek().sourceLines = code.split("\\r\\n|\\n".toRegex())
        }
        val parser = createParser(BOMInputStream(code.byteInputStream(Charsets.UTF_8)), errors, longLines = true)
        val root: RContext
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "RCONTEXT", "START"))
        val elapsedRoot = measureTime {
            root = parser.r()
        }
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "RCONTEXT", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformance(logSource, "RCONTEXT", elapsedRoot))
        var mutes: MutesImmutableMap? = null
        if (muteSupport) {
            mutes = findMutes(code, errors)
        }
        verifyParseTree(parser, errors, root)
        parserResult = RpgParserResult(errors, ParseTrees(rContext = root, muteContexts = mutes, copyBlocks = copyBlocks), parser, code)
        return parserResult
    }

    private fun tryToLoadCompilationUnit(): CompilationUnit? {
        return MainExecutionContext.getConfiguration().options.compiledProgramsDir?.let { compiledDir ->
            val timeSource = TimeSource.Monotonic
            val start = timeSource.markNow()
            val compiledFile = File(compiledDir, "$executionProgramName.bin")
            if (compiledFile.exists()) {
                val logSource = LogSourceData(executionProgramName, "")
                MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "START"))
                compiledFile.readBytes().createCompilationUnit().apply {
                    val elapsed = timeSource.markNow() - start
                    MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "END"))
                    MainExecutionContext.log(LazyLogEntry.producePerformance(logSource, "AST", elapsed))
                }
            } else {
                null
            }
        }?.apply {
            afterLoadAst()
        }
    }

    private fun CompilationUnit.afterLoadAst() {
        dataDefinitions.forEach { dataDefinition ->
            dataDefinition.fields.forEach { fieldDefinition ->
                dataDefinition.setOverlayOn(fieldDefinition)
            }
        }
        procedures?.onEach { procedureUnit ->
            procedureUnit.parent = this
        }
    }

    private fun createAst(
        inputStream: InputStream
    ): CompilationUnit {
        val result = parse(inputStream)
        require(result.correct) {
            result.fireErrorEvents()
            result.dumpError()
        }
        return kotlin.runCatching {
            val compilationUnit: CompilationUnit
            val logSource = LogSourceData(executionProgramName, "")
            MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "START"))
            val elapsed = measureTime {
                compilationUnit = result.root!!.rContext.toAst(
                    conf = MainExecutionContext.getConfiguration().options.toAstConfiguration,
                    source = if (MainExecutionContext.getConfiguration().options.mustDumpSource()) {
                        result.src
                    } else {
                        null
                    },
                    copyBlocks = if (MainExecutionContext.getParsingProgramStack().empty()) null else MainExecutionContext.getParsingProgramStack().peek().copyBlocks
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
            MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "END"))
            MainExecutionContext.log(LazyLogEntry.producePerformance(logSource, "AST", elapsed))
            compilationUnit
        }.onFailure {
            throw AstCreatingException(result.src, it)
        }.getOrThrow()
    }

    fun parseAndProduceAst(
        inputStream: InputStream,
        sourceProgram: SourceProgram? = SourceProgram.RPGLE
    ): CompilationUnit {
        MainExecutionContext.getParsingProgramStack().push(ParsingProgram(executionProgramName))
        val cu = if (sourceProgram?.sourceType == true) {
            (tryToLoadCompilationUnit() ?: createAst(inputStream)).apply {
                MainExecutionContext.getConfiguration().jarikoCallback.afterAstCreation.invoke(this)
            }
        } else {
            inputStream.readBytes().createCompilationUnit().apply {
                afterLoadAst()
                MainExecutionContext.getConfiguration().jarikoCallback.afterAstCreation.invoke(this)
            }
        }
        // This is a trick to pass the popped ParsingProgramStack for further processing
        addLastPoppedParsingProgram(MainExecutionContext.getParsingProgramStack().pop())
        return cu
    }

    fun parseExpression(inputStream: InputStream, longLines: Boolean = true, printTree: Boolean = false): ParsingResult<ExpressionContext> {
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

fun <T : ParserRuleContext> ParserRuleContext.findAllDescendants(type: KClass<T>, includingMe: Boolean = true): List<T> {
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

/**
 * @return The execution program name belonging to MainExecutionContext
 * */
fun getExecutionProgramNameWithNoExtension(): String {
    return MainExecutionContext.getExecutionProgramName().let {
        val name = File(it).name.replaceAfterLast(".", "")
        if (name.endsWith(".")) {
            name.substring(0, name.length - 1)
        } else {
            name
        }
    }
}

private fun List<Error>.groupByLine() = this.filter { it.message != "Error node found" }.groupBy {
    it.position?.start?.line
}

private typealias ErrorAtLine = Pair<Int?, String>

private fun Map.Entry<Int?, List<Error>>.toErrorAtLine(): ErrorAtLine {
    val messages = this.value.distinctBy { it.message }.joinToString(",") {
        it.message
    }
    return ErrorAtLine(this.key, messages)
}

private fun List<Error>.dumpError(copyBlocks: CopyBlocks?): String {
    return StringBuilder().let { sb ->
        adaptInFunctionOf(copyBlocks).groupByLine().forEach { errorEntry ->
            val errorAtLine = errorEntry.toErrorAtLine()
            val line = "Errors at line: ${errorAtLine.first}"
            val messages = errorAtLine.second
            sb.append(line).append(" messages: $messages\n")
        }
        sb.toString()
    }
}

private fun List<Error>.adaptInFunctionOf(copyBlocks: CopyBlocks?): List<Error> {
    return copyBlocks?.let {
        this.map { error ->
            error.copy(position = error.position?.adaptInFunctionOf(copyBlocks))
        }
    } ?: this
}

private fun List<Error>.fireErrorEvents() {
    val programName = if (MainExecutionContext.getParsingProgramStack().empty()) {
        null
    } else {
        MainExecutionContext.getParsingProgramStack().peek().name
    }
    val copyBlocks = programName?.let { MainExecutionContext.getParsingProgramStack().peek().copyBlocks }
    this.filter { !it.message.matches(Regex("Recognition exception: null|Error nod found")) }.groupByLine().forEach { errorEntry ->
        ErrorEvent(
            error = IllegalStateException(errorEntry.value[0].message),
            errorEventSource = ErrorEventSource.Parser,
            absoluteLine = errorEntry.value[0].position?.start?.line,
            sourceReference = errorEntry.value[0].position?.relative(programName, copyBlocks)?.second
        ).apply {
            MainExecutionContext.getConfiguration().jarikoCallback.onError(this)
        }
    }
}

class AstCreatingException(val src: String, cause: Throwable) :
    IllegalStateException(
    src.let {
        val sw = StringWriter()
        cause.printStackTrace(PrintWriter(sw))
        sw.flush()
        if (MainExecutionContext.getConfiguration().options.mustDumpSource()) {
            "$sw\n${src.dumpSource()}"
        } else {
            "$sw"
        }
    }
)

enum class SourceReferenceType {
    Program, Copy
}

/**
 * Models a source reference related to a statement
 * @param sourceReferenceType The type of the source
 * @param sourceId The id of the source
 * @param relativeLine of the statement inside the source
 * @param position The position of the statement inside the source
 * */
data class SourceReference @JvmOverloads constructor (val sourceReferenceType: SourceReferenceType, val sourceId: String, val relativeLine: Int, val position: Position? = null)

fun Position.relative(programName: String?, copyBlocks: CopyBlocks?): StatementReference {
    return if (programName == null) {
        val position = Position(Point(this.start.line, this.start.column), Point(this.end.line, this.end.column))
        StatementReference(
            first = this.start.line,
            second = SourceReference(
                sourceReferenceType = SourceReferenceType.Program,
                sourceId = "UNKNOWN",
                relativeLine = position.start.line,
                position = position
            )
        )
    } else {
        val copyBlock = copyBlocks?.getCopyBlock(this.start.line)
        val position = this.adaptInFunctionOf(copyBlocks)
        StatementReference(
            first = this.start.line,
            second = SourceReference(
                sourceReferenceType = copyBlock?.let { SourceReferenceType.Copy } ?: SourceReferenceType.Program,
                sourceId = copyBlock?.copyId?.toString() ?: programName,
                relativeLine = position.start.line,
                position = position
            )
        )
    }
}

fun Position.adaptInFunctionOf(copyBlocks: CopyBlocks?): Position {
    return kotlin.runCatching {
        Position(
            Point(copyBlocks?.relativeLine(this.start.line)?.first ?: this.start.line, this.start.column),
            Point(copyBlocks?.relativeLine(this.end.line)?.first ?: this.end.line, this.end.column)
        )
    }.getOrElse {
        System.err.println("Error on adaptInFunctionOf: " + it.toString())
        Position(
            Point(this.start.line, this.start.column),
            Point(this.end.line, this.end.column)
        )
    }
}

private fun addLastPoppedParsingProgram(parsingProgram: ParsingProgram) {
    MainExecutionContext.getAttributes()["${RpgParserFacade::javaClass.name}.lastPoppedParsingProgram"] = parsingProgram
}

internal fun getLastPoppedParsingProgram(): ParsingProgram? {
    return MainExecutionContext.getAttributes()["${RpgParserFacade::javaClass.name}.lastPoppedParsingProgram"] as ParsingProgram?
}
