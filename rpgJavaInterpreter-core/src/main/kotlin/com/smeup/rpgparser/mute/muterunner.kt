package com.smeup.rpgparser.mute

import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.DummySystemInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import java.io.File
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.UnsupportedOperationException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

data class ExecutionResult(val resolved: Int, val executed: Int, val failed: Int, val exceptions: LinkedList<Throwable>)

fun executeWithMutes(filename: String, verbose: Boolean = false): ExecutionResult {
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

        try {
            interpreter.execute(cu, mapOf())
            interpreter.executedAnnotation.forEach { (line, annotation) ->
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
        } catch (e: Exception) {
            exceptions.add(e)
        }
    } else {
        result.errors.forEach {
            System.err.println(it)
        }
    }

    println("Total annotation: ${resolved.size}, executed: $executed, failed: $failed")
    println()
    return ExecutionResult(resolved.size, executed, failed, exceptions)
}

data class MuteRunnerStatus(var files: Int = 0, var resolved: Int = 0, var executed: Int = 0, var failed: Int = 0) {
    var exceptions = LinkedList<Throwable>()
    val successful: Boolean
        get() = failed == 0 && exceptions.isEmpty()
}

object MuteRunner {
    val successful: Boolean
        get() = status.successful
    var verbose: Boolean = true
    var status = MuteRunnerStatus()

    private fun processPath(path: Path) {
        val filename = path.toString()

        if (filename.endsWith(".rpgle")) {
            if (verbose) {
                println(filename)
            }
            try {
                val result = executeWithMutes(filename, verbose)
                status.resolved += result.resolved
                status.executed += result.executed
                status.failed += result.failed
                status.files++
                status.exceptions.addAll(result.exceptions)
            } catch (e: NotImplementedError) {
                status.exceptions.add(e)
                System.err.println(e)
            } catch (e: NullPointerException) {
                status.exceptions.add(e)
                System.err.println(e)
            } catch (e: UnsupportedOperationException) {
                status.exceptions.add(e)
                System.err.println(e)
            } catch (e: IllegalArgumentException) {
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

/**
 * This program accepts the flags -verbose (default) and -silent.
 * All the other arguments are treated as paths to examine. If no paths are specified
 * the current directory is used.
 */
fun main(args: Array<String>) {
    println("MUTE Runner")
    val pathsToProcess = mutableListOf<Path>()
    args.forEach {
        when (it) {
            "-verbose" -> MuteRunner.verbose = true
            "-silent" -> MuteRunner.verbose = false
            else -> pathsToProcess.add(Paths.get(it))
        }
    }
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
    println("Total files: ${MuteRunner.status.files}, resolved: ${MuteRunner.status.resolved}, executed: ${MuteRunner.status.executed}, failed:${MuteRunner.status.failed}")
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
