package com.smeup.rpgparser.mute

import com.andreapivetta.kolor.green
import com.andreapivetta.kolor.red
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.file
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice
import com.smeup.rpgparser.parsing.ast.MuteAnnotationExecuted
import com.smeup.rpgparser.parsing.ast.MuteComparisonAnnotationExecuted
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.facade.RpgParserResult
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import com.smeup.rpgparser.utils.asDouble
import com.strumenta.kolasu.validation.Error
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

data class ExecutionResult(
    val file: File,
    val resolved: Int,
    val executed: Int,
    val failed: Int,
    val exceptions: LinkedList<Throwable>,
    val syntaxErrors: List<Error>
) {
    fun success(): Boolean = failed == 0 && exceptions.isEmpty() && syntaxErrors.isEmpty()

    override fun toString(): String {
        return buildString {
            appendln("------------")
            val message =
                "$file - Total annotation: $resolved, executed: $executed, failed: $failed, exceptions: ${exceptions.size}, syntax errors: ${syntaxErrors.size}"
            appendln(message.color(success()))
            exceptions.forEach {
                appendln(it.toExecutionMessage())
            }
            syntaxErrors.forEach {
                appendln(it)
            }
        }.addFileLink()
    }

    private fun String.addFileLink(): String {
        var lineNumber = substringAfter(" at line ").substringBefore(".").asDouble().toInt()
        if (lineNumber == 0) lineNumber = substringAfter("Position(start=Line ").substringBefore(",").asDouble().toInt()
        if (lineNumber > 0) {
            return this + System.lineSeparator() + "See ${file.linkTo(lineNumber)}"
        }
        return this
    }
}

fun Throwable.toExecutionMessage(): String {
    val sw = StringWriter()
    val printWriter = PrintWriter(sw)
    printStackTrace(printWriter)
    sw.flush()
    return sw.toString()
}

fun String.color(success: Boolean) = if (success) this.green() else this.red()

fun File.linkTo(line: Int) = if (showSourceAbsolutePath()) {
    // For linking to source from Visual Studio Code console
    "${this.absolutePath}:$line"
} else {
    // For linking to source from IDEA console
    "${this.name}(${this.name}:$line)"
}

fun showSourceAbsolutePath(): Boolean = "true" == System.getProperty("showSourceAbsolutePath")

fun executeWithMutes(
    path: Path,
    verbose: Boolean = false,
    logConfigurationFile: File?,
    programFinders: List<RpgProgramFinder> = emptyList(),
    output: PrintStream? = null,
    configuration: Configuration = Configuration(options = Options(muteSupport = true))
): ExecutionResult {
    var failed = 0
    var executed = 0
    val exceptions = LinkedList<Throwable>()

    val systemInterface =
        SimpleSystemInterface(programFinders = programFinders, output = output).useConfigurationFile(
            logConfigurationFile
        )
    return MainExecutionContext.execute(systemInterface = systemInterface, configuration = configuration) {
        var parserResult: RpgParserResult? = null
        val file = File(path.toString())
        try {
            it.executionProgramName = file.name
            parserResult =
                RpgParserFacade()
                    .parse(file.inputStream())
            if (parserResult.correct) {
                parserResult.executeMuteAnnotations(
                    verbose,
                    systemInterface,
                    programName = file.name.removeSuffix(".rpgle")
                ).forEach { (line, annotation) ->
                    if (verbose || annotation.failed()) {
                        println(
                            "Mute annotation at line $line ${annotation.resultAsString()} - ${annotation.headerDescription()} - ${
                                file.linkTo(
                                    line
                                )
                            }".color(annotation.succeeded())
                        )
                        if (annotation.failed()) {
                            failed++
                            if (annotation is MuteComparisonAnnotationExecuted) {
                                println("   Value 1: ${annotation.value1Expression.render()} -> ${annotation.value1Result}")
                                println("   Value 2: ${annotation.value2Expression.render()} -> ${annotation.value2Result}")
                            }
                        }
                    }
                    executed++
                }
            }
        } catch (e: Throwable) {
            exceptions.add(e)
        }
        ExecutionResult(
            file,
            parserResult?.root?.muteContexts?.size ?: 0,
            executed,
            failed,
            exceptions,
            parserResult?.errors ?: emptyList()
        )
    }
}

fun executeMuteAnnotations(
    programSrc: File,
    systemInterface: SystemInterface,
    parameters: List<String> = listOf(),
    configuration: Configuration = Configuration()
): SortedMap<Int, MuteAnnotationExecuted>? {
    getProgram(
        nameOrSource = programSrc.name,
        programFinders = listOf(DirRpgProgramFinder(programSrc.parentFile)),
        systemInterface = systemInterface
    ).apply {
        singleCall(parameters, configuration)
    }
    return systemInterface.executedAnnotationInternal.toSortedMap()
}

@Deprecated("Will be removed in the future")
fun executeMuteAnnotations(
    programStream: InputStream,
    systemInterface: SystemInterface,
    verbose: Boolean = false,
    parameters: Map<String, Value> = mapOf(),
    programName: String = "<UNKNOWN>",
    configuration: Configuration = Configuration()
): SortedMap<Int, MuteAnnotationExecuted>? {
    return MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
        it.executionProgramName = programName
        val parserResult =
            RpgParserFacade().apply { muteSupport = true }
                .parse(programStream)
        if (parserResult.correct) {
            parserResult.executeMuteAnnotations(
                verbose = verbose, systemInterface = systemInterface, parameters = parameters,
                programName = programName, configuration = configuration
            )
        } else {
            null
        }
    }
}

fun RpgParserResult.executeMuteAnnotations(
    verbose: Boolean,
    systemInterface: SystemInterface,
    parameters: Map<String, Value> = mapOf(),
    programName: String = "<UNKONWN>",
    configuration: Configuration = Configuration()
): SortedMap<Int, MuteAnnotationExecuted> {
    val root = this.root!!
    val cu = root.rContext.toAst().apply {
        val resolved = this.injectMuteAnnotation(root.muteContexts!!)

        if (verbose) {
            val sorted = resolved.sortedWith(compareBy { it.muteLine })
            sorted.forEach {
                println("Mute annotation at line ${it.muteLine} attached to statement ${it.statementLine}")
            }
        }
    }
    cu.resolveAndValidate()
    val interpreter = InternalInterpreter(systemInterface).apply {
        interpretationContext = object : InterpretationContext {
            private var iDataWrapUpChoice: DataWrapUpChoice? = null
            override val currentProgramName: String
                get() = programName

            override fun shouldReinitialize() = false

            override var dataWrapUpChoice: DataWrapUpChoice?
                get() = iDataWrapUpChoice
                set(value) {
                    iDataWrapUpChoice = value
                }
        }
    }
    interpreter.execute(cu, parameters)
    return interpreter.systemInterface.executedAnnotationInternal.toSortedMap()
}

object MuteRunner {
    val successful: Boolean
        get() = results.all { it.success() }
    var verbose: Boolean = true
    var results = mutableListOf<ExecutionResult>()
    var logConfigurationFile: File? = null

    fun processPaths(pathsToProcess: List<Path>) {
        pathsToProcess.forEach { path ->
            // Check if the Path is a directory
            if (Files.isDirectory(path)) {
                Files.list(path).forEach {
                    processPaths(listOf(it))
                }
            } else {
                if (isRpgle(path)) {
                    results.add(executeWithMutes(path, verbose, logConfigurationFile = logConfigurationFile))
                }
            }
        }
    }

    private fun isRpgle(path: Path) = path.toString().toLowerCase().endsWith(".rpgle")
    fun resolved(): Int = results.sumBy { it.resolved }
    fun executed(): Int = results.sumBy { it.executed }
    fun failed(): Int = results.sumBy { it.failed }
    fun errors(): Int = results.sumBy { it.syntaxErrors.size }
    fun exceptions(): Int = results.sumBy { it.exceptions.size }
}

const val FAILURE_EXIT_CODE = 1

class MuteRunnerCLI : CliktCommand() {
    val verbosity by option().switch("--verbose" to true, "-v" to true, "--silent" to false, "-s" to false).default(
        false
    )

    val logConfigurationFile by option("-lc", "--log-configuration").file(exists = true, readable = true)

    val pathsToProcessArgs by argument(name = "Paths to process").file(
        exists = true,
        folderOkay = true,
        fileOkay = true
    ).multiple(required = false)

    override fun run() {
        MuteRunner.verbose = verbosity
        MuteRunner.logConfigurationFile = logConfigurationFile

        println("MUTE Runner")
        if (MuteRunner.verbose) {
            println("log configuration file: ${logConfigurationFile?.canonicalFile?.absolutePath ?: "<unspecified>"}")
        }
        val pathsToProcess = mutableListOf<Path>()
        pathsToProcess.addAll(pathsToProcessArgs.map { it.toPath() })
        if (pathsToProcess.isEmpty()) {
            pathsToProcess.add(Paths.get("."))
        }
        if (MuteRunner.verbose) {
            println("(running in verbose mode)")
            println("paths to process: $pathsToProcess")
        }
        val invalidPaths = pathsToProcess.filter { !it.toFile().exists() }
        if (invalidPaths.isNotEmpty()) {
            System.err.println("Unexisting paths found:")
            invalidPaths.forEach { System.err.println(it) }
            System.exit(FAILURE_EXIT_CODE)
        }

        MuteRunner.processPaths(pathsToProcess)
        MuteRunner.results.forEach { println(it) }
        printBox("Total files: ${MuteRunner.results.size}, resolved: ${MuteRunner.resolved()}, executed: ${MuteRunner.executed()}, failed: ${MuteRunner.failed()}, errors: ${MuteRunner.errors()}, exceptions: ${MuteRunner.exceptions()}")
        if (MuteRunner.successful) {
            println()
            println("SUCCESS".green())
        } else {
            println()
            println("FAILURE".red())
        }
        if (!MuteRunner.successful) {
            System.exit(FAILURE_EXIT_CODE)
        }
    }
}

private fun printBox(msg: String) {
    println("--------------------------------------------------------------------------------------")
    println(msg)
    println("--------------------------------------------------------------------------------------")
}

/**
 * This program accepts the flags --verbose (default) and --silent.
 * All the other arguments are treated as paths to examine. If no paths are specified
 * the current directory is used.
 */
fun main(args: Array<String>) {
    MuteRunnerCLI().main(args)
}
