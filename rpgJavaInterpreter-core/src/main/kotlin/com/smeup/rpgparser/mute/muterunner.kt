package com.smeup.rpgparser.mute

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.file
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.DummySystemInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.interpreter.line
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.validation.Error
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

data class ExecutionResult(val resolved: Int, val executed: Int, val failed: Int, val exceptions: LinkedList<Throwable>, val syntaxErrors: List<Error>)

fun executeWithMutes(
    filename: String,
    verbose: Boolean = false,
    logConfigurationFile: File?
): ExecutionResult {
    var failed = 0
    var executed = 0
    var resolved: List<MuteAnnotationResolved> = listOf()
    var exceptions = LinkedList<Throwable>()

    val result = RpgParserFacade()
            .apply { this.muteSupport = true }
            .parse(File(filename).inputStream())

    if (result.correct) {
        val cu = result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root.muteContexts!!)

            if (verbose) {
                val sorted = resolved.sortedWith(compareBy { it.muteLine })
                sorted.forEach {
                    println("Mute annotation at line ${it.muteLine} attached to statement ${it.statementLine}")
                }
            }
        }
        println()
        cu.resolve()
        val interpreter = InternalInterpreter(DummySystemInterface)
        if (logConfigurationFile != null) {
            interpreter.useLogConfigurationFile(logConfigurationFile)
        }

        try {
            interpreter.execute(cu, mapOf())
            val sorted = interpreter.executedAnnotation.toSortedMap()
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
        } catch (e: Throwable) {
            exceptions.add(e)
        }
    } else {
        result.errors.forEach {
            println("Line: ${it.position.line()} - $it")
        }
    }

    println("$filename - Total annotation: ${resolved.size}, executed: $executed, failed: $failed, exceptions: ${exceptions.size}, syntax errors: ${result.errors.size}")
    exceptions.forEach {
        println(it)
    }
    println()
    return ExecutionResult(resolved.size, executed, failed, exceptions, result.errors)
}

data class MuteRunnerStatus(var files: Int = 0, var resolved: Int = 0, var executed: Int = 0, var failed: Int = 0) {
    val exceptions = LinkedList<Throwable>()
    val syntaxErrors = LinkedList<Error>()

    val errors: Int
        get() = exceptions.size + syntaxErrors.size

    val successful: Boolean
        get() = failed == 0 && exceptions.isEmpty() && syntaxErrors.isEmpty()
}

object MuteRunner {
    val successful: Boolean
        get() = status.successful
    var verbose: Boolean = true
    var status = MuteRunnerStatus()
    var logConfigurationFile: File? = null

    private fun processPath(path: Path) {
        val filename = path.toString()

        if (filename.endsWith(".rpgle")) {
            if (verbose) {
                println(filename)
            }
            try {
                val result = executeWithMutes(filename, verbose, logConfigurationFile = logConfigurationFile)
                status.resolved += result.resolved
                status.executed += result.executed
                status.failed += result.failed
                status.files++
                status.exceptions.addAll(result.exceptions)
                status.syntaxErrors.addAll(result.syntaxErrors)
            } catch (e: Throwable) {
                status.exceptions.add(e)
                System.err.println(e)
            }
        }
    }

    fun processPaths(pathsToProcess: List<Path>) {
        pathsToProcess.forEach { path ->
            // Check if the Path is a directory
            if (Files.isDirectory(path)) {
                val fileDirMap = Files.list(path).collect(Collectors.partitioningBy { Files.isDirectory(it) })
                fileDirMap[false]?.forEach {
                    processPath(it)
                }
            } else {
                processPath(path)
            }
        }
    }
}

val FAILURE_EXIT_CODE = 1

class MuteRunnerCLI : CliktCommand() {
    val verbosity by option().switch("--verbose" to true, "-v" to true, "--silent" to false, "-s" to false).default(false)
    val logConfigurationFile by option("-lc", "--log-configuration").file(exists = true, readable = true)
    val pathsToProcessArgs by argument(name = "Paths to process").file(exists = true, folderOkay = true, fileOkay = true).multiple(required = false)

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
        println("Total files: ${MuteRunner.status.files}, resolved: ${MuteRunner.status.resolved}, executed: ${MuteRunner.status.executed}, failed: ${MuteRunner.status.failed}, errors: ${MuteRunner.status.errors};")
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

/**
 * This program accepts the flags --verbose (default) and --silent.
 * All the other arguments are treated as paths to examine. If no paths are specified
 * the current directory is used.
 */
fun main(args: Array<String>) {
    MuteRunnerCLI().main(args)
}
