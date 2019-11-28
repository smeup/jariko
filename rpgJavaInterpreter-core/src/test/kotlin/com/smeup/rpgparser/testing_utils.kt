@file:Suppress("DEPRECATION")
package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.MuteAnnotationExecuted
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.facade.RpgParserResult
import com.smeup.rpgparser.parsing.facade.firstLine
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.rgpinterop.RpgProgramFinder
import com.strumenta.kolasu.model.ReferenceByName
import junit.framework.Assert
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Token
import org.apache.commons.io.input.BOMInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

// Used only to get a class to be used for getResourceAsStream
class Dummy

interface PerformanceTest

fun parseFragmentToCompilationUnit(
    code: String,
    toAstConfiguration: ToAstConfiguration = ToAstConfiguration(considerPosition = false)
): CompilationUnit {
    val completeCode = """
|     H/COPY QILEGEN,£INIZH
|      *---------------------------------------------------------------
|     I/COPY QILEGEN,£TABB£1DS
|     I/COPY QILEGEN,£PDS
|     $code
        """.trimMargin("|")
    val rContext = assertCodeCanBeParsed(completeCode)
    return rContext.toAst(toAstConfiguration)
}

fun parseFragmentToCompilationUnit(
    codeLines: List<String>,
    toAstConfiguration: ToAstConfiguration = ToAstConfiguration(considerPosition = false)
): CompilationUnit {
    val codeLinesAsSingleString = codeLines.joinToString("\n|     ")
    return parseFragmentToCompilationUnit(codeLinesAsSingleString, toAstConfiguration)
}

fun assertIsIntValue(value: Value, intValue: Long) {
    assertTrue(value is IntValue, "IntValue expected but found instead $value")
    assertEquals(intValue, value.value)
}

fun inputStreamFor(exampleName: String): InputStream {
    val resourceStream = Dummy::class.java.getResourceAsStream("/$exampleName.rpgle") ?: throw RuntimeException("$exampleName not found")
    return BOMInputStream(resourceStream)
}

fun inputStreamForCode(code: String): InputStream {
    return code.byteInputStream(StandardCharsets.UTF_8)
}

fun assertExampleCanBeLexed(exampleName: String, onlyVisibleTokens: Boolean = true): List<Token> {
    return assertCanBeLexed(inputStreamFor(exampleName), onlyVisibleTokens)
}

fun assertCodeCanBeLexed(code: String, onlyVisibleTokens: Boolean = true): List<Token> {
    return assertCanBeLexed(inputStreamForCode(code), onlyVisibleTokens)
}

fun assertCanBeLexed(file: File, onlyVisibleTokens: Boolean = true): List<Token> {
    return assertCanBeLexed(FileInputStream(file), onlyVisibleTokens)
}

fun assertCanBeLexed(inputStream: InputStream, onlyVisibleTokens: Boolean = true): List<Token> {
    val result = RpgParserFacade().lex(inputStream)
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return if (onlyVisibleTokens) {
        result.root!!.filter { it.channel != Lexer.HIDDEN }
    } else {
        result.root!!
    }
}

fun assertCanBeParsed(inputStream: InputStream, withMuteSupport: Boolean = false): RContext {
    val result = RpgParserFacade()
            .apply { this.muteSupport = withMuteSupport }
            .parse(inputStream)
    assertTrue(result.correct,
            message = "Errors: (line ${result.errors.firstLine()}) ${result.errors.joinToString(separator = ", ")}")
    return result.root!!.rContext
}

fun assertCanBeParsed(exampleName: String, withMuteSupport: Boolean = false, printTree: Boolean = false): RContext {
    return assertCanBeParsedResult(exampleName, withMuteSupport, printTree).root!!.rContext
}

fun assertCanBeParsedResult(exampleName: String, withMuteSupport: Boolean = false, printTree: Boolean = false): RpgParserResult {
    val result = RpgParserFacade()
            .apply { this.muteSupport = withMuteSupport }
            .parse(inputStreamFor(exampleName))

    if (printTree) println(result.toTreeString())

    assertTrue(result.correct,
            message = "Errors: (line ${result.errors.firstLine()}) ${result.errors.joinToString(separator = ", ")}")
    return result
}

fun assertCanBeParsed(exampleName: String, withMuteSupport: Boolean = false): RContext {
    return assertCanBeParsed(inputStreamFor(exampleName), withMuteSupport)
}

fun assertCanBeParsed(file: File, withMuteSupport: Boolean = false): RContext {
    return assertCanBeParsed(FileInputStream(file), withMuteSupport)
}

fun assertASTCanBeProduced(
    exampleName: String,
    considerPosition: Boolean = false,
    withMuteSupport: Boolean = false,
    printTree: Boolean = false
): CompilationUnit {
    val result = assertCanBeParsedResult(exampleName, withMuteSupport, printTree)
    val parseTreeRoot = result.root!!.rContext
    val ast = parseTreeRoot.toAst(ToAstConfiguration(
            considerPosition = considerPosition))
    if (withMuteSupport) {
        if (!considerPosition) {
            throw IllegalStateException("Mute annotations can be injected only when retaining the position")
        }
        ast.injectMuteAnnotation(result.root!!.muteContexts!!)
    }
    return ast
}

fun assertCodeCanBeParsed(code: String): RContext {
    val result = RpgParserFacade().parse(inputStreamForCode(code))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!.rContext
}

fun assertExpressionCanBeParsed(code: String): ExpressionContext {
    val result = RpgParserFacade().parseExpression(inputStreamForCode(code))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!
}

fun expressionAst(code: String): Expression {
    return assertExpressionCanBeParsed(code).toAst(ToAstConfiguration(considerPosition = false))
}

fun assertStatementCanBeParsed(code: String, addPrefix: Boolean = false): StatementContext {
    val codeToUse = if (addPrefix) "     C                   $code" else code
    val result = RpgParserFacade().parseStatement(inputStreamForCode(codeToUse))
    if (!result.correct) {
        val lexingResult = RpgParserFacade().lex(inputStreamForCode(code))
        if (lexingResult.correct) {
            println("Lexing completed successfully:")
            println("CODE <<<$code>>>")
            lexingResult.root!!.forEach {
                println(" * ${RpgLexer.VOCABULARY.getDisplayName(it.type)} '${it.text}'")
            }
        } else {
            println("Issue already at the lexical level")
        }
    }
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!
}

fun CompilationUnit.assertDataDefinitionIsPresent(
    name: String,
    dataType: Type,
    fields: List<FieldDefinition> = emptyList()
): DataDefinition {
    assertTrue(this.hasDataDefinition(name), message = "Data definition $name not found in Compilation Unit")
    val dataDefinition = this.getDataDefinition(name)
    assertEquals(dataType, dataDefinition.type)
    assertEquals(fields.size, dataDefinition.fields.size, "Expected ${fields.size} fields, found ${dataDefinition.fields.size}")
    for (i in 0 until fields.size) {
        assertEquals(fields[i], dataDefinition.fields[i], "Expected field $i was ${fields[i]}, found ${dataDefinition.fields[i]}")
    }
    assertEquals(fields, dataDefinition.fields)
    return dataDefinition
}

fun CompilationUnit.assertFileDefinitionIsPresent(name: String): FileDefinition {
    assertTrue(this.hasFileDefinition(name), message = "File definition $name not found in Compilation Unit")
    val fileDefinition = this.getFileDefinition(name)
    return fileDefinition
}

fun assertToken(expectedTokenType: Int, expectedTokenText: String, token: Token, trimmed: Boolean = true) {
    assertEquals(expectedTokenType, token.type)
    if (trimmed) {
        val expected = expectedTokenText.trim()
        val actual = token.text.trim()
        assertEquals(expected, actual, "Expected <$expected> but got <$actual>")
    } else {
        assertEquals(expectedTokenText, token.text)
    }
}

fun dataRef(name: String) = DataRefExpr(ReferenceByName(name))

open class CollectorSystemInterface(var loggingConfiguration: LoggingConfiguration? = null) : SystemInterface {
    override var executedAnnotationInternal: HashMap<Int, MuteAnnotationExecuted> = HashMap<Int, MuteAnnotationExecuted>()
    override var extraLogHandlers: MutableList<InterpreterLogHandler> = mutableListOf()

    override fun loggingConfiguration(): LoggingConfiguration? {
        return this.loggingConfiguration
    }

    val displayed = LinkedList<String>()
    val programs = HashMap<String, Program>()
    val functions = HashMap<String, Function>()
    var printOutput = false
    var databaseInterface: DBInterface = DummyDBInterface
        set(value) {
            field = value
        }

    override val db: DBInterface
        get() = databaseInterface

    override fun findProgram(name: String) = programs[name]
    override fun findFunction(globalSymbolTable: SymbolTable, name: String) = functions[name]

    override fun display(value: String) {
        displayed.add(value)
        if (printOutput) println(value)
    }

    override fun getExecutedAnnotation(): HashMap<Int, MuteAnnotationExecuted> {
        return this.executedAnnotationInternal
    }

    override fun addExecutedAnnotation(line: Int, annotation: MuteAnnotationExecuted) {
        executedAnnotationInternal[line] = annotation
    }
}

fun execute(
    cu: CompilationUnit,
    initialValues: Map<String, Value>,
    systemInterface: SystemInterface? = null,
    logHandlers: List<InterpreterLogHandler> = emptyList()
): InternalInterpreter {
    val si = systemInterface ?: DummySystemInterface
    if (si == DummySystemInterface) {
        si.executedAnnotationInternal.clear()
    }
    si.addExtraLogHandlers(logHandlers)
    val interpreter = InternalInterpreter(si)
    try {
        interpreter.execute(cu, initialValues)
    } catch (e: InterruptForDebuggingPurposes) {
        // nothing to do here
    }
    return interpreter
}

fun assertStartsWith(lines: List<String>, value: String) {
    if (lines.isEmpty()) {
        fail("Empty output")
    }
    assertTrue(lines.get(0).startsWith(value), Assert.format("Output not matching", value, lines))
}

fun outputOf(programName: String, initialValues: Map<String, Value> = mapOf(), printTree: Boolean = false): List<String> {
    val interpreter = execute(programName, initialValues, logHandlers = SimpleLogHandler.fromFlag(TRACE), printTree = printTree)
    val si = interpreter.systemInterface as CollectorSystemInterface
    return si.displayed.map(String::trimEnd)
}

private const val TRACE = false

fun execute(programName: String, initialValues: Map<String, Value>, si: CollectorSystemInterface = ExtendedCollectorSystemInterface(), logHandlers: List<InterpreterLogHandler> = SimpleLogHandler.fromFlag(TRACE), printTree: Boolean = false): InternalInterpreter {
    val cu = assertASTCanBeProduced(programName, true, printTree = printTree)
    cu.resolve()
    si.addExtraLogHandlers(logHandlers)
    return execute(cu, initialValues, si)
}

fun rpgProgram(name: String): RpgProgram {
    return RpgProgram.fromInputStream(Dummy::class.java.getResourceAsStream("/$name.rpgle"), name)
}

fun executeAnnotations(annotations: SortedMap<Int, MuteAnnotationExecuted>): Int {
    var failed: Int = 0
    annotations.forEach { (line, annotation) ->
        try {
            assertTrue(annotation.result.asBoolean().value)
        } catch (e: AssertionError) {
            println("${annotation.programName}: $line ${annotation.headerDescription()} ${annotation.result.asBoolean().value}")
            failed++
        }
    }
    return failed
}

class DummyProgramFinder(val path: String) : RpgProgramFinder {
    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        return RpgProgram.fromInputStream(Dummy::class.java.getResourceAsStream("$path$nameOrSource.rpgle"), nameOrSource)
    }
}

open class ExtendedCollectorSystemInterface() : CollectorSystemInterface() {
    private val rpgPrograms = HashMap<String, RpgProgram>()

    override fun findProgram(name: String): Program? {
        return super.findProgram(name) ?: findRpgProgram(name)
    }

    private fun findRpgProgram(name: String): Program? {
        return rpgPrograms.getOrPut(name) {
            rpgProgram(name)
        }
    }
}

open class MockDBFile : DBFile {
    override fun setll(key: Value) = TODO()
    override fun setll(keys: List<RecordField>) = TODO()
    override fun chain(key: Value): Record = TODO()
    override fun chain(keys: List<RecordField>): Record = TODO()
    override fun readEqual(): Record = TODO()
    override fun readEqual(key: Value): Record = TODO()
    override fun readEqual(keys: List<RecordField>): Record = TODO()
    override fun eof(): Boolean = TODO()
}
