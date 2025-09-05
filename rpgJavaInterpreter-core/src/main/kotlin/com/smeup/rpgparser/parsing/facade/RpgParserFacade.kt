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
import com.smeup.rpgparser.ProfilingLexer
import com.smeup.rpgparser.ProfilingParser
import com.smeup.rpgparser.RpgLexer
import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import com.smeup.rpgparser.interpreter.StatementReference
import com.smeup.rpgparser.interpreter.line
import com.smeup.rpgparser.logging.ProgramUsageType
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.ast.createCompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.injectProfilingAnnotations
import com.smeup.rpgparser.parsing.parsetreetoast.setOverlayOn
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.utils.insLineNumber
import com.smeup.rpgparser.utils.parseTreeToXml
import com.smeup.rpgparser.utils.popIfPresent
import com.smeup.rpgparser.utils.pushIfNotAlreadyPresent
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
import kotlin.collections.withIndex
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

typealias MutesMap = MutableMap<Int, MuteParser.MuteLineContext>
typealias MutesImmutableMap = Map<Int, MuteParser.MuteLineContext>

/**
 * Map of profiling lines by the line they are declared at.
 */
typealias ProfilingMap = MutableMap<Int, ProfilingParser.ProfilingLineContext>

/**
 * Immutable version of [ProfilingMap].
 */
typealias ProfilingImmutableMap = Map<Int, ProfilingParser.ProfilingLineContext>

open class ParsingResult<C>(
    val errors: List<Error>,
    val root: C?,
) {
    val correct: Boolean
        get() = errors.isEmpty()
}

fun List<Error>.firstLine(): String =
    this
        .firstOrNull {
            it.position != null
        }?.position
        .line()

data class ParseTrees(
    val rContext: RContext,
    val muteContexts: MutesImmutableMap? = null,
    val profilingContexts: ProfilingImmutableMap? = null,
    val copyBlocks: CopyBlocks? = null,
)

class RpgParserResult(
    errors: List<Error>,
    root: ParseTrees,
    private val parser: Parser,
    val src: String,
) : ParsingResult<ParseTrees>(errors, root) {
    fun toTreeString(): String = parseTreeToXml(root!!.rContext, parser)

    fun dumpError(): String =
        if (MainExecutionContext.getConfiguration().options.mustDumpSource()) {
            "${errors.dumpError(root!!.copyBlocks)}\n${src.dumpSource()}"
        } else {
            errors.dumpError(root!!.copyBlocks)
        }

    internal fun fireErrorEvents() {
        errors.fireErrorEvents()
    }
}

fun String.dumpSource(): String {
    val parsingProgramName =
        if (MainExecutionContext.getParsingProgramStack().isNotEmpty()) {
            MainExecutionContext.getParsingProgramStack().peek().name
        } else {
            getExecutionProgramNameWithNoExtension()
        }
    val programName =
        parsingProgramName.let {
            if (it.lines().size > 1) {
                "PROGRAM NAME NOT SET"
            } else {
                it
            }
        }
    val header = "********* SRC $programName"
    val src =
        this.insLineNumber(5) {
            // for now return al lines
            // linesInError.contains(it)
            true
        }
    return "$header\n$src"
}

typealias RpgLexerResult = ParsingResult<List<Token>>

class RpgParserFacade {
    // Should be 'false' as default to avoid unnecessary search of 'mute annotation' into rpg program source.
    var muteSupport: Boolean = MainExecutionContext.getConfiguration().options.muteSupport
    private var muteVerbose = MainExecutionContext.getConfiguration().options.muteVerbose

    // Should be 'false' as default to avoid unnecessary search of 'profiling annotation' into rpg program source.
    var profilingSupport: Boolean = MainExecutionContext.getConfiguration().options.profilingSupport

    private val executionProgramName: String by lazy {
        getExecutionProgramNameWithNoExtension()
    }

    private fun inputStreamWithLongLines(
        inputStream: InputStream,
        threshold: Int = 80,
    ): CharStream {
        val code = inputStreamToString(inputStream)
        val lines = code.lines()
        val longLines = lines.map { it.padEnd(threshold) }
        val paddedCode = longLines.joinToString(System.lineSeparator())
        return CharStreams.fromStream(paddedCode.byteInputStream(StandardCharsets.UTF_8))
    }

    private fun inputStreamToString(inputStream: InputStream): String = inputStream.bufferedReader().use(BufferedReader::readText)

    fun lex(inputStream: InputStream): RpgLexerResult {
        val errors = LinkedList<Error>()
        val lexer = RpgLexer(inputStreamWithLongLines(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(
            object : BaseErrorListener() {
                override fun syntaxError(
                    p0: Recognizer<*, *>?,
                    p1: Any?,
                    line: Int,
                    charPositionInLine: Int,
                    errorMessage: String?,
                    p5: RecognitionException?,
                ) {
                    errors.add(
                        Error(
                            ErrorType.LEXICAL,
                            errorMessage
                                ?: "unspecified",
                            position = Point(line, charPositionInLine).asPosition,
                        ),
                    )
                }
            },
        )
        val tokens = LinkedList<Token>()
        do {
            val t = lexer.nextToken()
            if (t == null) {
                break
            } else {
                tokens.add(t)
            }
        } while (t.type != Token.EOF)

        if (tokens.last().type != Token.EOF) {
            errors.add(Error(ErrorType.SYNTACTIC, "Not whole input consumed", tokens.last().endPoint.asPosition))
        }

        return RpgLexerResult(errors, tokens)
    }

    /**
     * Create parser for MUTE annotations.
     *
     * @param inputStream The input stream.
     * @param errors A mutable list where output errors will be reported to.
     * @param longLines Use long lines.
     */
    fun createMuteParser(
        inputStream: InputStream,
        errors: MutableList<Error>,
        longLines: Boolean,
    ): MuteParser {
        val lexer = MuteLexer(if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(
            object : BaseErrorListener() {
                override fun syntaxError(
                    p0: Recognizer<*, *>?,
                    p1: Any?,
                    line: Int,
                    charPositionInLine: Int,
                    errorMessage: String?,
                    p5: RecognitionException?,
                ) {
                    errors.add(
                        Error(
                            ErrorType.LEXICAL,
                            errorMessage
                                ?: "unspecified",
                            position = Point(line, charPositionInLine).asPosition,
                        ),
                    )
                }
            },
        )
        val commonTokenStream = CommonTokenStream(lexer)
        val parser = MuteParser(commonTokenStream)
        parser.addErrorListener(
            object : BaseErrorListener() {
                override fun syntaxError(
                    p0: Recognizer<*, *>?,
                    p1: Any?,
                    p2: Int,
                    p3: Int,
                    errorMessage: String?,
                    p5: RecognitionException?,
                ) {
                    errors.add(
                        Error(
                            ErrorType.SYNTACTIC,
                            errorMessage
                                ?: "unspecified",
                        ),
                    )
                }
            },
        )
        parser.removeErrorListeners()
        return parser
    }

    /**
     * Create parser for PROFILING annotations.
     *
     * @param inputStream The input stream.
     * @param errors A mutable list where output errors will be reported to.
     * @param longLines Use long lines.
     */
    fun createProfilingParser(
        inputStream: InputStream,
        errors: MutableList<Error>,
        longLines: Boolean,
    ): ProfilingParser {
        val lexer = ProfilingLexer(if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(
            object : BaseErrorListener() {
                override fun syntaxError(
                    p0: Recognizer<*, *>?,
                    p1: Any?,
                    line: Int,
                    charPositionInLine: Int,
                    errorMessage: String?,
                    p5: RecognitionException?,
                ) {
                    errors.add(
                        Error(
                            ErrorType.LEXICAL,
                            errorMessage
                                ?: "unspecified",
                            position = Point(line, charPositionInLine).asPosition,
                        ),
                    )
                }
            },
        )
        val commonTokenStream = CommonTokenStream(lexer)
        val parser = ProfilingParser(commonTokenStream)
        parser.addErrorListener(
            object : BaseErrorListener() {
                override fun syntaxError(
                    p0: Recognizer<*, *>?,
                    p1: Any?,
                    p2: Int,
                    p3: Int,
                    errorMessage: String?,
                    p5: RecognitionException?,
                ) {
                    errors.add(
                        Error(
                            ErrorType.SYNTACTIC,
                            errorMessage
                                ?: "unspecified",
                        ),
                    )
                }
            },
        )
        parser.removeErrorListeners()
        return parser
    }

    /**
     * Create parser for RPGLE.
     *
     * @param inputStream The input stream.
     * @param errors A mutable list where output errors will be reported to.
     * @param longLines Use long lines.
     */
    fun createParser(
        inputStream: InputStream,
        errors: MutableList<Error>,
        longLines: Boolean,
    ): RpgParser {
        val logSource = { LogSourceData(executionProgramName, "") }
        val callback = MainExecutionContext.getConfiguration().jarikoCallback
        val rpgLoadTrace = JarikoTrace(JarikoTraceKind.Parsing, "RPGLOAD")
        var charInput: CharStream? = null
        callback.traceBlock(rpgLoadTrace) {
            MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "RPGLOAD", "START"))
            MainExecutionContext.log(LazyLogEntry.produceParsingStart(logSource, "RPGLOAD"))
            val elapsedLoad =
                measureNanoTime {
                    charInput =
                        if (longLines) inputStreamWithLongLines(inputStream) else CharStreams.fromStream(inputStream)
                }.nanoseconds

            val lines = charInput?.toString()?.split(Pattern.compile("\\r\\n|\\r|\\n"))?.size ?: 0
            val endLogSource = { LogSourceData(executionProgramName, lines.toString()) }

            MainExecutionContext.log(LazyLogEntry.produceStatement(endLogSource, "RPGLOAD", "END"))
            MainExecutionContext.log(LazyLogEntry.produceParsingEnd(endLogSource, "RPGLOAD", elapsedLoad))
            MainExecutionContext.log(
                LazyLogEntry.producePerformanceAndUpdateAnalytics(
                    endLogSource,
                    ProgramUsageType.Parsing,
                    "RPGLOAD",
                    elapsedLoad,
                ),
            )
        }

        val lexerTrace = JarikoTrace(JarikoTraceKind.Parsing, "LEXER")
        val lexer =
            callback.traceBlock(lexerTrace) {
                val lexer: Lexer
                MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "LEXER", "START"))
                MainExecutionContext.log(LazyLogEntry.produceParsingStart(logSource, "LEXER"))

                val elapsedLexer =
                    measureNanoTime {
                        lexer = RpgLexer(charInput)
                        lexer.removeErrorListeners()
                        lexer.addErrorListener(
                            object : BaseErrorListener() {
                                override fun syntaxError(
                                    p0: Recognizer<*, *>?,
                                    p1: Any?,
                                    line: Int,
                                    charPositionInLine: Int,
                                    errorMessage: String?,
                                    p5: RecognitionException?,
                                ) {
                                    errors.add(
                                        Error(
                                            ErrorType.LEXICAL,
                                            errorMessage
                                                ?: "unspecified",
                                            position = Point(line, charPositionInLine).asPosition,
                                        ),
                                    )
                                }
                            },
                        )
                    }.nanoseconds

                MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "LEXER", "END"))
                MainExecutionContext.log(LazyLogEntry.produceParsingEnd(logSource, "LEXER", elapsedLexer))
                MainExecutionContext.log(
                    LazyLogEntry.producePerformanceAndUpdateAnalytics(
                        logSource,
                        ProgramUsageType.Parsing,
                        "LEXER",
                        elapsedLexer,
                    ),
                )
                lexer
            }

        val parserTrace = JarikoTrace(JarikoTraceKind.Parsing, "PARSER")
        val parser =
            callback.traceBlock(parserTrace) {
                val parser: RpgParser
                MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "PARSER", "START"))
                MainExecutionContext.log(LazyLogEntry.produceParsingStart(logSource, "PARSER"))
                val elapsedParser =
                    measureNanoTime {
                        val commonTokenStream = CommonTokenStream(lexer)
                        parser = RpgParser(commonTokenStream)
                        parser.removeErrorListeners()
                        parser.addErrorListener(
                            object : BaseErrorListener() {
                                override fun syntaxError(
                                    p0: Recognizer<*, *>?,
                                    p1: Any?,
                                    line: Int,
                                    charPositionInLine: Int,
                                    errorMessage: String?,
                                    p5: RecognitionException?,
                                ) {
                                    errors.add(
                                        Error(
                                            ErrorType.SYNTACTIC,
                                            errorMessage
                                                ?: "unspecified",
                                            position = Point(line, charPositionInLine).asPosition,
                                        ),
                                    )
                                }
                            },
                        )
                    }.nanoseconds
                MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "PARSER", "END"))
                MainExecutionContext.log(LazyLogEntry.produceParsingEnd(logSource, "PARSER", elapsedParser))
                MainExecutionContext.log(
                    LazyLogEntry.producePerformanceAndUpdateAnalytics(
                        logSource,
                        ProgramUsageType.Parsing,
                        "PARSER",
                        elapsedParser,
                    ),
                )
                parser
            }
        return parser
    }

    private fun verifyParseTree(
        parser: Parser,
        errors: MutableList<Error>,
        root: ParserRuleContext,
    ) {
        val callback = MainExecutionContext.getConfiguration().jarikoCallback
        val trace = JarikoTrace(JarikoTraceKind.Parsing, "CHKPTREE")
        callback.traceBlock(trace) {
            val logSource = { LogSourceData(executionProgramName, "") }
            MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "CHKPTREE", "START"))
            MainExecutionContext.log(LazyLogEntry.produceParsingStart(logSource, "CHKPTREE"))
            val elapsed =
                measureNanoTime {
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
                                    it.start.startPoint.asPosition,
                                ),
                            )
                        }
                    }, {
                        errors.add(Error(ErrorType.SYNTACTIC, "Error node found", it.toPosition(true)))
                    })
                }.nanoseconds
            MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "CHKPTREE", "END"))
            MainExecutionContext.log(LazyLogEntry.produceParsingEnd(logSource, "CHKPTREE", elapsed))
            MainExecutionContext.log(
                LazyLogEntry.producePerformanceAndUpdateAnalytics(
                    logSource,
                    ProgramUsageType.Parsing,
                    "CHKPTREE",
                    elapsed,
                ),
            )
        }
    }

    private fun InputStream.bomInputStream(): InputStream =
        BOMInputStream
            .builder()
            .setInputStream(this)
            .get()

    /**
     * Parse mute annotation.
     *
     * @param code The code to parse.
     * @param errors A mutable list where output errors will be reported to.
     */
    private fun parseMute(
        code: String,
        errors: MutableList<Error>,
    ): MuteParser.MuteLineContext {
        val muteParser = createMuteParser(code.byteInputStream(Charsets.UTF_8).bomInputStream(), errors, longLines = true)
        val root = muteParser.muteLine()
        verifyParseTree(muteParser, errors, root)
        return root
    }

    /**
     * Parse profiling annotation.
     *
     * @param code The code to parse.
     * @param errors A mutable list where output errors will be reported to.
     */
    private fun parseProfiling(
        code: String,
        errors: MutableList<Error>,
    ): ProfilingParser.ProfilingLineContext {
        val inputStream = code.byteInputStream(Charsets.UTF_8).bomInputStream()
        val parser = createProfilingParser(inputStream, errors, longLines = true)
        val root = parser.profilingLine()
        verifyParseTree(parser, errors, root)
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

    private fun findMutes(
        code: String,
        errors: MutableList<Error>,
    ) = findMutes(code.byteInputStream(Charsets.UTF_8), errors)

    private fun findMutes(
        code: InputStream,
        errors: MutableList<Error>,
    ): MutesMap {
        val logSource = { LogSourceData(executionProgramName, "") }
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "FINDMUTES", "START"))
        val mutes: MutesMap = HashMap()
        val elapsed =
            measureNanoTime {
                val lexResult = lex(code.bomInputStream())
                errors.addAll(lexResult.errors)
                lexResult.root?.forEachIndexed { index, token0 ->
                    if (index + 2 < lexResult.root.size) {
                        val token1 = lexResult.root[index + 1]
                        val token2 = lexResult.root[index + 2]
                        // Please note the leading spaces added
                        if (token0.type == LEAD_WS5_Comments &&
                            token0.text == "".padStart(4) + "M" &&
                            token1.type == COMMENT_SPEC_FIXED &&
                            token1.text == "U*" &&
                            token2.type == COMMENTS_TEXT
                        ) {
                            // Please note the leading spaces added to the token
                            val preproc = preprocess(token2.text)
                            mutes[token2.line] = parseMute("".padStart(8) + preproc, errors)
                        }
                    }
                }
            }.nanoseconds
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "FINDMUTES", "END"))
        MainExecutionContext.log(
            LazyLogEntry.producePerformanceAndUpdateAnalytics(logSource, ProgramUsageType.Parsing, "FINDMUTES", elapsed),
        )
        return mutes
    }

    /**
     * Find profiling annotations.
     *
     * @param code The code where to look for annotations.
     * @param errors A mutable list where output errors will be reported to.
     */
    private fun findProfiling(
        code: String,
        errors: MutableList<Error>,
    ) = findProfiling(code.byteInputStream(Charsets.UTF_8), errors)

    /**
     * Find profiling annotations.
     *
     * @param code The input stream where to look for annotations.
     * @param errors A mutable list where output errors will be reported to.
     */
    private fun findProfiling(
        code: InputStream,
        errors: MutableList<Error>,
    ): ProfilingMap {
        val logSource = { LogSourceData(executionProgramName, "") }
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "FINDPROF", "START"))
        val profiling: ProfilingMap = HashMap()
        val elapsed =
            measureNanoTime {
                val profilingTokens = listOf("@starttrace", "@stoptrace")
                val inputStream = code.bomInputStream()
                val lexResult = lex(inputStream)
                errors.addAll(lexResult.errors)
                val isTracingEnabled = this.checkTracingEnabled(lexResult.root ?: emptyList())
                if (!isTracingEnabled) {
                    return@measureNanoTime
                }
                lexResult.root?.forEachIndexed { index, token0 ->
                    val isInRange = index < lexResult.root.size - 1
                    if (isInRange) {
                        val token1 = lexResult.root[index + 1]
                        // Please note the leading spaces added
                        if (token0.type == COMMENT_SPEC_FIXED &&
                            token0.text.endsWith("*") &&
                            token1.type == COMMENTS_TEXT &&
                            profilingTokens.any {
                                token1.text
                                    .lowercase()
                                    .trim()
                                    .startsWith(it)
                            }

                        ) {
                            // Please note the leading spaces added to the token
                            val preproc = preprocess(token1.text)
                            profiling[token1.line] = parseProfiling("".padStart(8) + preproc, errors)
                        }
                    }
                }
            }.nanoseconds
        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "FINDPROF", "END"))
        MainExecutionContext.log(
            LazyLogEntry.producePerformanceAndUpdateAnalytics(logSource, ProgramUsageType.Parsing, "FINDPROF", elapsed),
        )
        return profiling
    }

    private fun checkTracingEnabled(tokens: List<Token>): Boolean {
        return tokens.withIndex().any { (index, token0) ->
            val isInRange = index < tokens.size - 2
            if (!isInRange) {
                return@any false
            }
            val token1 = tokens[index + 1]
            val token2 = tokens[index + 2]
            // Please note the leading spaces added
            return@any token0.type == LEAD_WS5_Comments &&
                token0.text == "".padStart(3) + "CO" &&
                token1.type == COMMENT_SPEC_FIXED &&
                token1.text == "P*" &&
                token2.type == COMMENTS_TEXT
        }
    }

    fun parse(inputStream: InputStream): RpgParserResult {
        val parserResult: RpgParserResult
        val logSource = { LogSourceData(executionProgramName, "") }
        val errors = LinkedList<Error>()
        val copyBlocks: CopyBlocks? = if (MainExecutionContext.getConfiguration().options.mustCreateCopyBlocks()) CopyBlocks() else null
        // val includedCopySet = mutableSetOf<CopyId>()
        val code =
            inputStream
                .preprocess(
                    findCopy = { copyId ->
                        MainExecutionContext.getSystemInterface()?.findCopy(copyId)?.source.let { source ->
                            MainExecutionContext.getConfiguration().jarikoCallback.beforeCopyInclusion(copyId, source)
                            source
                        }
                    },
                    onStartInclusion = { copyId, start -> copyBlocks?.onStartCopyBlock(copyId = copyId, start = start) },
                    onEndInclusion = { end -> copyBlocks?.onEndCopyBlock(end = end) },
                    // beforeInclusion = { copyId -> includedCopySet.add(copyId) }
                ).apply {
                    if (copyBlocks != null) MainExecutionContext.getConfiguration().jarikoCallback.afterCopiesInclusion(copyBlocks)
                }.let { code ->
                    MainExecutionContext
                        .getConfiguration()
                        .jarikoCallback.beforeParsing
                        .invoke(code)
                }
        if (!MainExecutionContext.getParsingProgramStack().empty()) {
            MainExecutionContext.getParsingProgramStack().peek().copyBlocks = copyBlocks
            MainExecutionContext.getParsingProgramStack().peek().sourceLines = code.split("\\r\\n|\\n".toRegex())
        }
        val parser = createParser(code.byteInputStream(Charsets.UTF_8).bomInputStream(), errors, longLines = true)
        val callback = MainExecutionContext.getConfiguration().jarikoCallback
        val trace = JarikoTrace(JarikoTraceKind.Parsing, "RCONTEXT")
        val root =
            callback.traceBlock(trace) {
                val root: RContext
                MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "RCONTEXT", "START"))
                MainExecutionContext.log(LazyLogEntry.produceParsingStart(logSource, "RCONTEXT"))
                val elapsedRoot =
                    measureNanoTime {
                        root = parser.r()
                    }.nanoseconds
                MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "RCONTEXT", "END"))
                MainExecutionContext.log(LazyLogEntry.produceParsingEnd(logSource, "RCONTEXT", elapsedRoot))
                MainExecutionContext.log(
                    LazyLogEntry.producePerformanceAndUpdateAnalytics(
                        logSource,
                        ProgramUsageType.Parsing,
                        "RCONTEXT",
                        elapsedRoot,
                    ),
                )
                root
            }
        val mutes = if (muteSupport) findMutes(code, errors) else null
        val profiling = if (profilingSupport) findProfiling(code, errors) else null
        verifyParseTree(parser, errors, root)
        parserResult =
            RpgParserResult(
                errors,
                ParseTrees(rContext = root, muteContexts = mutes, profilingContexts = profiling, copyBlocks = copyBlocks),
                parser,
                code,
            )
        return parserResult
    }

    private fun tryToLoadCompilationUnit(): CompilationUnit? =
        MainExecutionContext
            .getConfiguration()
            .options.compiledProgramsDir
            ?.let { compiledDir ->
                val start = System.nanoTime()
                val compiledFile = File(compiledDir, "$executionProgramName.bin")
                if (compiledFile.exists()) {
                    val callback = MainExecutionContext.getConfiguration().jarikoCallback
                    val trace = JarikoTrace(JarikoTraceKind.Parsing, "AST")
                    callback.traceBlock(trace) {
                        val logSource = { LogSourceData(executionProgramName, "") }
                        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "START"))
                        MainExecutionContext.log(LazyLogEntry.produceParsingStart(logSource, "AST"))
                        compiledFile.readBytes().createCompilationUnit().apply {
                            val elapsed = (System.nanoTime() - start).nanoseconds
                            MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "END"))
                            MainExecutionContext.log(LazyLogEntry.produceParsingEnd(logSource, "AST", elapsed))
                            MainExecutionContext.log(
                                LazyLogEntry.producePerformanceAndUpdateAnalytics(
                                    logSource,
                                    ProgramUsageType.Parsing,
                                    "AST",
                                    elapsed,
                                ),
                            )
                        }
                    }
                } else {
                    null
                }
            }?.apply {
                afterLoadAst()
            }

    private fun CompilationUnit.afterLoadAst() {
        dataDefinitions.forEach { dataDefinition ->
            dataDefinition.fields.forEach { fieldDefinition ->
                dataDefinition.setOverlayOn(fieldDefinition)
            }
        }
        procedures?.onEach { procedureUnit ->
            procedureUnit.parent = this
            procedureUnit.afterLoadAst()
        }
    }

    private fun createAst(inputStream: InputStream): CompilationUnit {
        val result = parse(inputStream)
        require(result.correct) {
            result.fireErrorEvents()
            result.dumpError()
        }
        return kotlin
            .runCatching {
                val logSource = { LogSourceData(executionProgramName, "") }
                val callback = MainExecutionContext.getConfiguration().jarikoCallback
                val trace = JarikoTrace(JarikoTraceKind.Parsing, "AST")
                callback.traceBlock(trace) {
                    val compilationUnit: CompilationUnit
                    MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "START"))
                    MainExecutionContext.log(LazyLogEntry.produceParsingStart(logSource, "AST"))
                    val elapsed =
                        measureNanoTime {
                            compilationUnit =
                                result.root!!
                                    .rContext
                                    .toAst(
                                        conf = MainExecutionContext.getConfiguration().options.toAstConfiguration,
                                        source =
                                            if (MainExecutionContext.getConfiguration().options.mustDumpSource()) {
                                                result.src
                                            } else {
                                                null
                                            },
                                        copyBlocks =
                                            if (MainExecutionContext.getParsingProgramStack().empty()) {
                                                null
                                            } else {
                                                MainExecutionContext
                                                    .getParsingProgramStack()
                                                    .peek()
                                                    .copyBlocks
                                            },
                                    ).apply {
                                        if (muteSupport) {
                                            val resolved = this.injectMuteAnnotation(result.root.muteContexts!!)
                                            if (muteVerbose) {
                                                val sorted = resolved.sortedWith(compareBy { it.muteLine })
                                                sorted.forEach {
                                                    println(
                                                        "Mute annotation at line ${it.muteLine} attached to statement ${it.statementLine}",
                                                    )
                                                }
                                            }
                                        }
                                        if (profilingSupport) {
                                            this.injectProfilingAnnotations(result.root.profilingContexts!!)
                                        }
                                    }
                        }.nanoseconds
                    MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "AST", "END"))
                    MainExecutionContext.log(LazyLogEntry.produceParsingEnd(logSource, "AST", elapsed))
                    MainExecutionContext.log(
                        LazyLogEntry.producePerformanceAndUpdateAnalytics(logSource, ProgramUsageType.Parsing, "AST", elapsed),
                    )
                    compilationUnit
                }
            }.onFailure {
                throw AstCreatingException(result.src, it)
            }.getOrThrow()
    }

    fun parseAndProduceAst(
        inputStream: InputStream,
        sourceProgram: SourceProgram? = SourceProgram.RPGLE,
    ): CompilationUnit {
        MainExecutionContext.getParsingProgramStack().pushIfNotAlreadyPresent(ParsingProgram(executionProgramName))
        val cu =
            if (sourceProgram?.sourceType == true) {
                (tryToLoadCompilationUnit() ?: createAst(inputStream)).apply {
                    MainExecutionContext
                        .getConfiguration()
                        .jarikoCallback.afterAstCreation
                        .invoke(this)
                }
            } else {
                inputStream.readBytes().createCompilationUnit().apply {
                    afterLoadAst()
                    MainExecutionContext
                        .getConfiguration()
                        .jarikoCallback.afterAstCreation
                        .invoke(this)
                }
            }
        // This is a trick to pass the popped ParsingProgramStack for further processing
        MainExecutionContext.getParsingProgramStack().popIfPresent()?.apply {
            addLastPoppedParsingProgram(this)
        }

        return cu
    }

    fun parseExpression(
        inputStream: InputStream,
        longLines: Boolean = true,
        printTree: Boolean = false,
    ): ParsingResult<ExpressionContext> {
        // Nothing to do with Mute support, as annotations can be only on statements
        val errors = LinkedList<Error>()
        val parser = createParser(inputStream, errors, longLines = longLines)
        val root = parser.expression()
        if (printTree) println(parseTreeToXml(root, parser))
        verifyParseTree(parser, errors, root)
        return ParsingResult(errors, root)
    }

    fun parseStatement(
        inputStream: InputStream,
        longLines: Boolean = true,
    ): ParsingResult<StatementContext> {
        val errors = LinkedList<Error>()
        val parser = createParser(inputStream, errors, longLines = longLines)
        val root = parser.statement()
        verifyParseTree(parser, errors, root)
        val result = ParsingResult(errors, root)

        // Injection step
        if (result.correct) {
            tryInjectMute(inputStream, errors, result)
            tryInjectProfiling(inputStream, errors, result)
        }

        return result
    }

    /**
     * Inject MUTE annotations if needed.
     *
     * @param code The input stream where to look for annotations.
     * @param errors A mutable list where output errors will be reported to.
     * @param parsingResult The result of the RPGLE parsing.
     */
    fun tryInjectMute(
        inputStream: InputStream,
        errors: MutableList<Error>,
        parsingResult: ParsingResult<StatementContext>,
    ) {
        if (!muteSupport) return

        inputStream.reset()
        val mutes = findMutes(inputStream, errors)
        parsingResult.root!!.toAst().apply {
            this.injectMuteAnnotation(mutes)
        }
    }

    /**
     * Inject PROFILING annotations if needed.
     *
     * @param code The input stream where to look for annotations.
     * @param errors A mutable list where output errors will be reported to.
     * @param parsingResult The result of the RPGLE parsing.
     */
    fun tryInjectProfiling(
        inputStream: InputStream,
        errors: MutableList<Error>,
        parsingResult: ParsingResult<StatementContext>,
    ) {
        if (!profilingSupport) return

        inputStream.reset()
        val profiling = findProfiling(inputStream, errors)
        parsingResult.root!!.toAst().apply {
            this.injectProfilingAnnotations(profiling)
        }
    }
}

private fun TerminalNode.toPosition(considerPosition: Boolean = true): Position? =
    if (considerPosition) {
        Position(this.symbol.startPoint, this.symbol.endPoint)
    } else {
        null
    }

fun ParserRuleContext.processDescendants(
    operation: (ParserRuleContext) -> Unit,
    includingMe: Boolean = true,
) {
    if (includingMe) {
        operation(this)
    }
    if (this.children != null) {
        this.children.filterIsInstance<ParserRuleContext>().forEach { it.processDescendants(operation) }
    }
}

fun <T : ParserRuleContext> ParserRuleContext.findAllDescendants(
    type: KClass<T>,
    includingMe: Boolean = true,
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
    includingMe: Boolean = true,
) {
    if (includingMe) {
        operationOnParserRuleContext(this)
    }
    if (this.children != null) {
        this.children.filterIsInstance<ParserRuleContext>().forEach {
            it.processDescendantsAndErrors(operationOnParserRuleContext, operationOnError, includingMe = true)
        }
        this.children.filterIsInstance<ErrorNode>().forEach {
            operationOnError(it)
        }
    }
}

/**
 * @return The execution program name belonging to MainExecutionContext
 * */
fun getExecutionProgramNameWithNoExtension(): String =
    MainExecutionContext.getExecutionProgramName().let {
        val name = File(it).name.replaceAfterLast(".", "")
        if (name.endsWith(".")) {
            name.substring(0, name.length - 1)
        } else {
            name
        }
    }

private fun List<Error>.groupByLine() =
    this.filter { it.message != "Error node found" }.groupBy {
        it.position?.start?.line
    }

private typealias ErrorAtLine = Pair<Int?, String>

private fun Map.Entry<Int?, List<Error>>.toErrorAtLine(): ErrorAtLine {
    val messages =
        this.value.distinctBy { it.message }.joinToString(",") {
            it.message
        }
    return ErrorAtLine(this.key, messages)
}

private fun List<Error>.dumpError(copyBlocks: CopyBlocks?): String =
    StringBuilder().let { sb ->
        adaptInFunctionOf(copyBlocks).groupByLine().forEach { errorEntry ->
            val errorAtLine = errorEntry.toErrorAtLine()
            val line = "Errors at line: ${errorAtLine.first}"
            val messages = errorAtLine.second
            sb.append(line).append(" messages: $messages\n")
        }
        sb.toString()
    }

private fun List<Error>.adaptInFunctionOf(copyBlocks: CopyBlocks?): List<Error> =
    copyBlocks?.let {
        this.map { error ->
            error.copy(position = error.position?.adaptInFunctionOf(copyBlocks))
        }
    } ?: this

private fun List<Error>.fireErrorEvents() {
    val programName =
        if (MainExecutionContext.getParsingProgramStack().empty()) {
            null
        } else {
            MainExecutionContext.getParsingProgramStack().peek().name
        }
    val copyBlocks = programName?.let { MainExecutionContext.getParsingProgramStack().peek().copyBlocks }
    this.filter { !it.message.matches(Regex("Recognition exception: null|Error nod found")) }.groupByLine().forEach { errorEntry ->
        ErrorEvent(
            error = IllegalStateException(errorEntry.value[0].message),
            errorEventSource = ErrorEventSource.Parser,
            absoluteLine =
                errorEntry.value[0]
                    .position
                    ?.start
                    ?.line,
            sourceReference =
                errorEntry.value[0]
                    .position
                    ?.relative(programName, copyBlocks)
                    ?.second,
        ).apply {
            MainExecutionContext.getConfiguration().jarikoCallback.onError(this)
        }
    }
}

class AstCreatingException(
    val src: String,
    cause: Throwable,
) : IllegalStateException(
        src.let {
            val sw = StringWriter()
            cause.printStackTrace(PrintWriter(sw))
            sw.flush()
            if (MainExecutionContext.getConfiguration().options.mustDumpSource()) {
                "$sw\n${src.dumpSource()}"
            } else {
                "$sw"
            }
        },
    )

enum class SourceReferenceType {
    Program,
    Copy,
}

/**
 * Models a source reference related to a statement
 * @param sourceReferenceType The type of the source
 * @param sourceId The id of the source
 * @param relativeLine of the statement inside the source
 * @param position The position of the statement inside the source
 * */
data class SourceReference
    @JvmOverloads
    constructor(
        val sourceReferenceType: SourceReferenceType,
        val sourceId: String,
        val relativeLine: Int,
        val position: Position? = null,
    )

fun Position.relative(
    programName: String?,
    copyBlocks: CopyBlocks?,
): StatementReference =
    if (programName == null) {
        val position = Position(Point(this.start.line, this.start.column), Point(this.end.line, this.end.column))
        StatementReference(
            first = this.start.line,
            second =
                SourceReference(
                    sourceReferenceType = SourceReferenceType.Program,
                    sourceId = "UNKNOWN",
                    relativeLine = position.start.line,
                    position = position,
                ),
        )
    } else {
        val copyBlock = copyBlocks?.getCopyBlock(this.start.line)
        val position = this.adaptInFunctionOf(copyBlocks)
        StatementReference(
            first = this.start.line,
            second =
                SourceReference(
                    sourceReferenceType = copyBlock?.let { SourceReferenceType.Copy } ?: SourceReferenceType.Program,
                    sourceId = copyBlock?.copyId?.toString() ?: programName,
                    relativeLine = position.start.line,
                    position = position,
                ),
        )
    }

fun Position.adaptInFunctionOf(copyBlocks: CopyBlocks?): Position =
    kotlin
        .runCatching {
            Position(
                Point(copyBlocks?.relativeLine(this.start.line)?.first ?: this.start.line, this.start.column),
                Point(copyBlocks?.relativeLine(this.end.line)?.first ?: this.end.line, this.end.column),
            )
        }.getOrElse {
            System.err.println("Error on adaptInFunctionOf: $it")
            Position(
                Point(this.start.line, this.start.column),
                Point(this.end.line, this.end.column),
            )
        }

private fun addLastPoppedParsingProgram(parsingProgram: ParsingProgram) {
    MainExecutionContext.getAttributes()["${RpgParserFacade::javaClass.name}.lastPoppedParsingProgram"] = parsingProgram
}

internal fun getLastPoppedParsingProgram(): ParsingProgram? =
    MainExecutionContext.getAttributes()["${RpgParserFacade::javaClass.name}.lastPoppedParsingProgram"] as ParsingProgram?
