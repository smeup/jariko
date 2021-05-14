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

@file:Suppress("DEPRECATION")
@file:JvmName("TestingUtils")

package com.smeup.rpgparser

import com.andreapivetta.kolor.yellow
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.jvminterop.JvmMockProgram
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.*
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgSystem
import com.smeup.rpgparser.utils.Format
import com.smeup.rpgparser.utils.compile
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
import kotlin.reflect.full.isSubclassOf
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

// Used only to get a class to be used for getResourceAsStream
class Dummy

interface PerformanceTest

val testCompiledDir = File(System.getProperty("java.io.tmpdir"), "jariko/test/bin").apply {
    if (!this.exists()) {
        this.mkdirs()
    }
}

private val rpgTestSrcDir = File(Dummy::class.java.getResource("/ABSTEST.rpgle").file).parent

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
    val resourceStream =
        Dummy::class.java.getResourceAsStream("/$exampleName.rpgle") ?: throw RuntimeException("$exampleName not found")
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
    val result = MainExecutionContext.execute(systemInterface = JavaSystemInterface()) {
        RpgParserFacade().lex(inputStream)
    }
    assertTrue(
        result.correct,
        message = "Errors: ${result.errors.joinToString(separator = ", ")}"
    )
    return if (onlyVisibleTokens) {
        result.root!!.filter { it.channel != Lexer.HIDDEN }
    } else {
        result.root!!
    }
}

fun assertCanBeParsed(inputStream: InputStream, withMuteSupport: Boolean = false): RContext {
    return MainExecutionContext.execute(systemInterface = createJavaSystemInterface()) {
        val result = RpgParserFacade()
            .apply { this.muteSupport = withMuteSupport }
            .parse(inputStream)
        assertTrue(
            result.correct,
            message = result.dumpError()
        )
        result.root!!.rContext
    }
}

fun assertCanBeParsed(exampleName: String, withMuteSupport: Boolean = false, printTree: Boolean = false): RContext {
    return assertCanBeParsedResult(exampleName, withMuteSupport, printTree).root!!.rContext
}

private class TestJavaSystemInterface : JavaSystemInterface() {

    override fun findCopy(copyId: CopyId): Copy? {

        return getExecutionProgramNameWithNoExtension().let {
            // println("Finding copy: $copyId for program: $it")
            if (it == "" || it.matches(Regex("JD_001.*|JD_002|JD_003|JD_000.*"))) {
                println("Returning null copy for program: $it just to avoid test units regression".yellow())
                null
            } else {
                // println("Delegating superclass")
                super.findCopy(copyId)
            }
        }
    }
}

private fun createJavaSystemInterface(): JavaSystemInterface {
    return TestJavaSystemInterface().apply {
        rpgSystem.addProgramFinder(DirRpgProgramFinder(File(rpgTestSrcDir)))
        rpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/")))
    }
}

fun assertCanBeParsedResult(
    exampleName: String,
    withMuteSupport: Boolean = false,
    printTree: Boolean = false
): RpgParserResult {
    val result = MainExecutionContext.execute(systemInterface = createJavaSystemInterface()) {
        it.executionProgramName = exampleName
        RpgParserFacade()
            .apply { this.muteSupport = withMuteSupport }
            .parse(inputStreamFor(exampleName))
    }

    if (printTree) println(result.toTreeString())
    assertTrue(
        result.correct,
        message = "Errors: (line ${result.errors.firstLine()}) ${result.errors.joinToString(separator = ", ")}"
    )
    return result
}

/**
 * This test does not provide copy directive inclusion
 * */
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
    printTree: Boolean = false,
    compiledProgramsDir: File?,
    // Workaround to solve problem related datadefinition creation outer of the execution context used in experimental data access
    afterAstCreation: (ast: CompilationUnit) -> Unit = {}
): CompilationUnit {
    val ast: CompilationUnit
    // if printTree true it is necessary create parserResult, then I can't load ast from bin
    if (printTree) {
        val result = assertCanBeParsedResult(exampleName, withMuteSupport, printTree)
        val parseTreeRoot = result.root!!.rContext
        ast = parseTreeRoot.toAst(
            ToAstConfiguration(
                considerPosition = considerPosition
            )
        )
        if (withMuteSupport) {
            if (!considerPosition) {
                throw IllegalStateException("Mute annotations can be injected only when retaining the position")
            }
            ast.injectMuteAnnotation(result.root!!.muteContexts!!)
            afterAstCreation.invoke(ast)
        }
    } else {
        val configuration =
            Configuration(options = Options(
                muteSupport = withMuteSupport,
                compiledProgramsDir = compiledProgramsDir,
                toAstConfiguration = ToAstConfiguration(considerPosition)),
                jarikoCallback = JarikoCallback(afterAstCreation = afterAstCreation)
            )
        ast = MainExecutionContext.execute(systemInterface = createJavaSystemInterface(), configuration = configuration) {
            it.executionProgramName = exampleName
            RpgParserFacade().parseAndProduceAst(inputStreamFor(exampleName))
        }
    }
    return ast
}

fun assertCodeCanBeParsed(code: String): RContext {
    val result = RpgParserFacade().parse(inputStreamForCode(code))
    assertTrue(
        result.correct,
        message = "Errors: ${result.errors.joinToString(separator = "\n")}"
    )
    return result.root!!.rContext
}

fun assertExpressionCanBeParsed(code: String): ExpressionContext {
    val result = RpgParserFacade().parseExpression(inputStreamForCode(code), printTree = true)
    assertTrue(
        result.correct,
        message = "Errors: ${result.errors.joinToString(separator = "\n")}"
    )
    return result.root!!
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
    assertTrue(
        result.correct,
        message = "Errors: ${result.errors.joinToString(separator = ", ")}"
    )
    return result.root!!
}

fun CompilationUnit.assertNrOfMutesAre(expected: Int) {
    val actual =
        this.allDataDefinitions.map { it.muteAnnotations.size }.sum() +
                this.main.stmts.nrOfMutes() +
                this.subroutines.map { it.stmts.nrOfMutes() }.sum()
    assertEquals(expected, actual, "Expected $expected mutes, but were $actual")
}

fun List<Statement>.nrOfMutes() = this.map { it.muteAnnotations.size }.sum()

fun CompilationUnit.assertDataDefinitionIsPresent(
    name: String,
    dataType: Type,
    fields: List<FieldDefinition> = emptyList()
): DataDefinition {
    assertTrue(this.hasDataDefinition(name), message = "Data definition $name not found in Compilation Unit")
    val dataDefinition = this.getDataDefinition(name)
    assertEquals(dataType, dataDefinition.type)
    assertEquals(
        fields.size,
        dataDefinition.fields.size,
        "Expected ${fields.size} fields, found ${dataDefinition.fields.size}"
    )
    for (i in 0 until fields.size) {
        assertEquals(
            fields[i],
            dataDefinition.fields[i],
            "Expected field $i was ${fields[i]}, found ${dataDefinition.fields[i]}"
        )
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
    override var executedAnnotationInternal: LinkedHashMap<Int, MuteAnnotationExecuted> =
        LinkedHashMap<Int, MuteAnnotationExecuted>()
    override var extraLogHandlers: MutableList<InterpreterLogHandler> = mutableListOf()

    override fun loggingConfiguration(): LoggingConfiguration? {
        return this.loggingConfiguration
    }

    val displayed = LinkedList<String>()
    val programs = HashMap<String, Program>()
    val functions = HashMap<String, Function>()
    var printOutput = false

    override fun findProgram(name: String): Program? {
        // I can't use getOrPut because SingletonRpgSystem.getProgram(name) could return null.
        // This implementation is a little bit mess but I don't matter because this is just a test interface never used
        // in real environment, moreover in the future I would remove it
        val program = programs[name]
        return if (program == null) {
            val foundProgram = kotlin.runCatching {
                SingletonRpgSystem.getProgram(name)
            }.getOrNull()
            if (foundProgram != null) {
                programs[name] = foundProgram
                foundProgram
            } else {
                null
            }
        } else {
            program
        }
    }
    override fun findFunction(globalSymbolTable: ISymbolTable, name: String): Function? {
        // this way I enable function searching also within compilation unit
        return functions.computeIfAbsent(name) {
            findFunctionInPackages(name) ?: RpgFunction.fromCurrentProgram(name)
        }
    }

    private val javaInteropPackages = LinkedList<String>()

    private fun findFunctionInPackages(programName: String): Function? {
        if (javaInteropPackages.isEmpty()) {
            javaInteropPackages.add("com.smeup.rpgparser.jvminterop")
        }
        return javaInteropPackages.asSequence().map { packageName ->
            try {
                val javaClass = this.javaClass.classLoader.loadClass("$packageName.$programName")
                instantiateFunction(javaClass)
            } catch (e: Throwable) {
                null
            }
        }.filter {
            it != null
        }.firstOrNull()
    }

    open fun instantiateFunction(javaClass: Class<*>): Function? {
        return if (javaClass.kotlin.isSubclassOf(Function::class)) {
            javaClass.kotlin.constructors.filter {
                it.parameters.isEmpty()
            }.first().call() as Function
        } else {
            null
        }
    }

    override fun findCopy(copyId: CopyId): Copy? {
        TODO("Not yet implemented")
    }

    override fun display(value: String) {
        displayed.add(value)
        if (printOutput) println(value)
    }

    override fun getExecutedAnnotation(): LinkedHashMap<Int, MuteAnnotationExecuted> {
        return this.executedAnnotationInternal
    }

    override fun addExecutedAnnotation(line: Int, annotation: MuteAnnotationExecuted) {
        executedAnnotationInternal[line] = annotation
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor {
        TODO("Not yet implemented")
    }

    override fun findApi(apiId: ApiId): Api {
        TODO("Not yet implemented")
    }
}

fun execute(
    cu: CompilationUnit,
    initialValues: Map<String, Value>,
    systemInterface: SystemInterface? = null,
    logHandlers: List<InterpreterLogHandler> = emptyList(),
    programName: String = "<UNSPECIFIED>"
): InternalInterpreter {
    val si = systemInterface ?: DummySystemInterface
    if (si == DummySystemInterface) {
        si.executedAnnotationInternal.clear()
    }
    si.addExtraLogHandlers(logHandlers)
    val interpreter = InternalInterpreter(si)
    try {
        // create just to pass to interpreter programName
        val interpretationContext = object : InterpretationContext {
            override val currentProgramName: String
                get() = programName
            override var dataWrapUpChoice: DataWrapUpChoice?
                get() = null
                set(value) {}

            override fun shouldReinitialize(): Boolean {
                return true
            }
        }
        interpreter.setInterpretationContext(interpretationContext)
        interpreter.execute(cu, initialValues)
        interpreter.doSomethingAfterExecution()
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

fun outputOf(
    programName: String,
    initialValues: Map<String, Value> = mapOf(),
    printTree: Boolean = false,
    si: CollectorSystemInterface = ExtendedCollectorSystemInterface(),
    compiledProgramsDir: File?,
    configuration: Configuration = Configuration()
): List<String> {
    execute(programName, initialValues, logHandlers = SimpleLogHandler.fromFlag(TRACE), printTree = printTree, si = si,
        compiledProgramsDir = compiledProgramsDir, configuration = configuration)
    return si.displayed.map(String::trimEnd)
}

const val TRACE = false

fun execute(
    programName: String,
    initialValues: Map<String, Value>,
    si: CollectorSystemInterface = ExtendedCollectorSystemInterface(),
    logHandlers: List<InterpreterLogHandler> = SimpleLogHandler.fromFlag(TRACE),
    printTree: Boolean = false,
    compiledProgramsDir: File?,
    // TODO inject configuration even for program without procedures
    configuration: Configuration = Configuration()
): InternalInterpreter {
    val cu = assertASTCanBeProduced(programName, true, printTree = printTree, compiledProgramsDir = compiledProgramsDir)
    cu.resolveAndValidate()
    si.addExtraLogHandlers(logHandlers)
    return if (cu.procedures == null) {
        execute(cu, initialValues, si)
    } else {
        // if we have some procedures we have to push in programstack current program see RpgFunction.fromCurrentProgram
        MainExecutionContext.execute(systemInterface = si, configuration = configuration) { mainExecutionContext ->
            mainExecutionContext.executionProgramName = programName
            mainExecutionContext.programStack.push(RpgProgram(cu, programName).apply {
                // setup activationGroup is mandatory
                // view InternalInterpreter.getActivationGroupAssignedName
                activationGroup = ActivationGroup(
                    type = NamedActivationGroup(configuration.defaultActivationGroupName),
                    assignedName = configuration.defaultActivationGroupName)
            })
            val interpreter = execute(cu, initialValues, si, programName = programName)
            mainExecutionContext.programStack.pop()
            interpreter
        }
    }
}

fun rpgProgram(name: String): RpgProgram {
    return RpgProgram.fromInputStream(Dummy::class.java.getResourceAsStream("/$name.rpgle"), name)
}

fun executeAnnotations(annotations: SortedMap<Int, MuteAnnotationExecuted>): Int {
    var failed = 0
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

class DummyProgramFinder(private val path: String) : RpgProgramFinder {

    fun getFile(name: String) = File(Dummy::class.java.getResource("$path$name.rpgle").file)

    fun rpgSourceInputStream(nameOrSource: String): InputStream? =
        Dummy::class.java.getResourceAsStream("$path$nameOrSource.rpgle")

    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        return rpgSourceInputStream(nameOrSource)?.let {
            RpgProgram.fromInputStream(it, nameOrSource)
        }
    }

    override fun findCopy(copyId: CopyId): Copy? {
        TODO("Not yet implemented")
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor? {
        TODO("Not yet implemented")
    }

    override fun findApi(apiId: ApiId): Api? {
        TODO("Not yet implemented")
    }
}

open class ExtendedCollectorSystemInterface(val jvmMockPrograms: List<JvmMockProgram> = emptyList()) :
    CollectorSystemInterface() {

    val programFinders = mutableListOf<RpgProgramFinder>(DummyProgramFinder("/"))
    private val rpgPrograms = HashMap<String, RpgProgram>()
    private val nameToMockPrograms: Map<String, Program>

    init {
        nameToMockPrograms = jvmMockPrograms.map { it.name to it }.toMap()
    }

    override fun findProgram(name: String): Program? {
        return nameToMockPrograms[name] ?: super.findProgram(name) ?: findWithFinders(name)
    }

    private fun findWithFinders(name: String): Program {
        return rpgPrograms.getOrPut(name) {
            find(name)
        }
    }

    private fun find(name: String): RpgProgram {
        programFinders.forEach {
            val pgm = it.findRpgProgram(name)
            if (pgm != null) return pgm
        }
        throw IllegalArgumentException("Program $name cannot be found")
    }
}

fun compileAllMutes(dirs: List<String>, format: Format = Format.BIN) {
    println("Deleting $testCompiledDir")
    testCompiledDir.deleteRecursively()
    testCompiledDir.mkdirs()
    dirs.forEach { it ->
        val muteSupport = it != "performance-ast"
        val srcDir = File(rpgTestSrcDir, it)
        println("Compiling dir ${srcDir.absolutePath} with muteSupport: $muteSupport")

        val compiled = compile(
            src = srcDir,
            compiledProgramsDir = testCompiledDir,
            muteSupport = muteSupport,
            format = format,
            systemInterface = { dir ->
                TestJavaSystemInterface().apply {
                    rpgSystem.addProgramFinder(DirRpgProgramFinder(dir))
                }
            }
        )
        // now error are displayed during the compilation
        if (compiled.any { it.error != null }) {
            error("Compilation error view logs")
        }
    }
}

private class CompileAllMutes : CliktCommand(
    help = "Compile all rpg programs located in resources",
    name = "compileAllMutes"

) {
    private val dirs: String by option(
        "-dirs",
        help = "list of relative directories relative to path: $rpgTestSrcDir"
    ).default(
        listOf(
            ".", "data/ds", "data/interop", "primitives", "db", "logging", "mute",
            "overlay", "performance", "performance-ast", "struct"
        ).joinToString()
    )
    private val format: String by option(
        "-format",
        help = "Compiled file format: [BIN|JSON]"
    ).default("BIN")

    override fun run() {
        compileAllMutes(dirs = dirs.split(",").map { it.trim() }, format = Format.valueOf(format))
    }
}

/**
 * Introduced only for compatibility with test cases that used still RpgSystem as singleton object
 * */
object SingletonRpgSystem : RpgSystem() {

    init {
        SINGLETON_RPG_SYSTEM = this
    }

    fun reset() {
        programFinders.clear()
    }
}

fun main(args: Array<String>) {
    CompileAllMutes().main(args)
}
