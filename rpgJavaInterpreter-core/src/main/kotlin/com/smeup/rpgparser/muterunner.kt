package com.smeup.rpgparser

import com.smeup.rpgparser.ast.MuteAnnotationResolved
import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.DummySystemInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsetreetoast.resolve
import com.smeup.rpgparser.parsetreetoast.toAst
import java.io.File
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.UnsupportedOperationException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

data class ExecutionResult(val resolved: Int, val executed: Int, val failed: Int)

fun executeWithMutes(filename: String, verbose: Boolean = false): ExecutionResult {
    var failed = 0
    var executed = 0
    var resolved: List<MuteAnnotationResolved> = listOf()

    val result = RpgParserFacade()
            .apply { this.muteSupport = true }
            .parse(File(filename).inputStream())

    if (result.correct) {
        val cu = result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)

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

                    if (verbose) {
                        println("Mute annotation at line $line ${annotation.expression.render()} failed")
                    }
                    failed++
                }
                executed++
            }
        } catch (e: Exception) {
        }
    } else {
        result.errors.forEach {
            System.err.println(it)
        }
    }

    println("Total annotation: ${resolved.size}, executed: $executed, failed: $failed")
    println()
    return ExecutionResult(resolved.size, executed, failed)
}

fun main(args: Array<String>) {
    var files = 0
    var resolved = 0
    var executed = 0
    var failed = 0
    var verbose = true
    val path = Paths.get(if (args.isEmpty()) {
        "."
    } else {
        args[0]
    })

    // Check if the Path is a directory
    if (Files.isDirectory(path)) {
        val fileDirMap = Files.list(path).collect(Collectors.partitioningBy({ it -> Files.isDirectory(it) }))

        fileDirMap[false]?.forEach { it ->
            val filename = it.toString()

            if (filename.endsWith(".rpgle")) {
                println(filename)
                try {
                    val result = executeWithMutes(filename, verbose)
                    resolved += result.resolved
                    executed += result.executed
                    failed += result.failed
                    files++
                } catch (e: NotImplementedError) {
                    System.err.println(e)
                } catch (e: NullPointerException) {
                    System.err.println(e)
                } catch (e: UnsupportedOperationException) {
                    System.err.println(e)
                } catch (e: IllegalArgumentException) {
                    println(e)
                }
            }
        }
        Thread.sleep(2000)
        println("Total files: $files, resolved: $resolved, executed: $executed, failed:$failed")
    } else {
        val filename = path.toString()
        if (filename.endsWith(".rpgle")) {
            println(filename)
            try {
                executeWithMutes(filename, verbose)
                files++
            } catch (e: NotImplementedError) {
                System.err.println(e)
            } catch (e: NullPointerException) {
                System.err.println(e)
            } catch (e: UnsupportedOperationException) {
                System.err.println(e)
            } catch (e: IllegalArgumentException) {
                println(e)
            }
        }
    }
}
