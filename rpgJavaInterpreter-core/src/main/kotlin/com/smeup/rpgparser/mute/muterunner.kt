package com.smeup.rpgparser.mute

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.file
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.interpreter.SimpleSystemInterface
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.facade.RpgParserResult
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.validation.Error
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

data class ExecutionResult(
    val fileName: String,
    val resolved: Int,
    val executed: Int,
    val failed: Int,
    val exceptions: LinkedList<Throwable>,
    val syntaxErrors: List<Error>
) {
    fun success(): Boolean = failed == 0 && exceptions.isEmpty() && syntaxErrors.isEmpty()

    override fun toString(): String {
        val sb = StringBuilder()
        sb.appendln("------------")
        sb.appendln("$fileName - Total annotation: $resolved, executed: $executed, failed: $failed, exceptions: ${exceptions.size}, syntax errors: ${syntaxErrors.size}")
        val sw = StringWriter()
        exceptions.forEach {
            it.printStackTrace(PrintWriter(sw))
            sw.appendln()
        }
        sw.flush()
        sb.append(sw)
        syntaxErrors.forEach {
            sb.appendln(it)
        }
        return sb.toString()
    }
}

fun executeWithMutes(
    fileName: String,
    verbose: Boolean = false,
    logConfigurationFile: File?
): ExecutionResult {
    var failed = 0
    var executed = 0
    var resolved: List<MuteAnnotationResolved> = listOf()
    var exceptions = LinkedList<Throwable>()

    var result: RpgParserResult? = null

    try {
        result = RpgParserFacade().apply { this.muteSupport = true }
            .parse(File(fileName).inputStream())
        if (result.correct) {
            val cu = result.root!!.rContext.toAst().apply {
                resolved = this.injectMuteAnnotation(result.root?.muteContexts!!)

                if (verbose) {
                    val sorted = resolved.sortedWith(compareBy { it.muteLine })
                    sorted.forEach {
                        println("Mute annotation at line ${it.muteLine} attached to statement ${it.statementLine}")
                    }
                }
            }
            cu.resolve()
            val interpreter = InternalInterpreter(SimpleSystemInterface().useConfigurationFile(logConfigurationFile))

            interpreter.execute(cu, mapOf())
            val sorted = interpreter.systemInterface.executedAnnotationInternal.toSortedMap()
            sorted.forEach { (line, annotation) ->
                if (!annotation.result.asBoolean().value) {
                    println("Mute annotation at line $line ${annotation.expression.render()} failed")
                    if (verbose) {
                        println("  Value 1: ${annotation.value1Expression.render()} -> ${annotation.value1Result}")
                        println("  Value 2: ${annotation.value2Expression.render()} -> ${annotation.value2Result}")
                    }
                    failed++
                }
                executed++
            }
        }
    } catch (e: Throwable) {
        exceptions.add(e)
    }
    return ExecutionResult(fileName, resolved.size, executed, failed, exceptions, result?.errors ?: emptyList())
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
                    results.add(executeWithMutes(path.toString(), verbose, logConfigurationFile = logConfigurationFile))
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
            println("SUCCESS")
        } else {
            println()
            println("FAILURE")
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
