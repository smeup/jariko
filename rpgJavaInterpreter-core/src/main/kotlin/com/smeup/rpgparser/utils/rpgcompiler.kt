package com.smeup.rpgparser.utils

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.encodeToByteArray
import com.smeup.rpgparser.parsing.ast.encodeToString
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import java.io.*

enum class Format {
    JSON, BIN
}

data class CompilationOption(val format: Format = Format.BIN, val muteSupport: Boolean = false)

/**
 * Represents compilation result
 * @param srcFile Source file
 * @param compiledFile Compiled file
 * @param error Compilation error
 * @param parseError Parsing error
 * */
data class CompilationResult(
    val srcFile: File,
    val compiledFile: File? = null,
    val error: Throwable? = null,
    val parsingError: Throwable? = null
)

private fun compileFile(file: File, targetDir: File, format: Format, muteSupport: Boolean): CompilationResult {
    runCatching {
        println("Compiling $file")
        FileInputStream(file).use {
            var cu: CompilationUnit? = null
            runCatching {
                cu = RpgParserFacade().apply {
                    this.muteSupport = muteSupport
                }.parseAndProduceAst(it)
            }.onFailure {
                return CompilationResult(file, null, null, it)
            }
            val compiledFile = when (format) {
                Format.BIN -> File(
                    targetDir, file.name.replaceAfterLast(
                        '.',
                        "bin",
                        "${file.name}.bin"
                    )
                ).apply { writeBytes(cu!!.encodeToByteArray()) }
                Format.JSON -> File(
                    targetDir, file.name.replaceAfterLast(
                        '.',
                        "json",
                        "${file.name}.json"
                    )
                ).apply { writeText(cu!!.encodeToString()) }
            }
            println("Compiled in $compiledFile")
            return CompilationResult(file, compiledFile)
        }
    }.onFailure {
        return CompilationResult(file, null, it)
    }
    error("Something went wrong")
}

/**
 * Compile programs
 * @param src Source file or dir
 * @param compiledProgramsDir The programs will be compiled in this directory
 * @param format Compiled file format. Default Format.BIN
 * @param configuration Configuration. Default empty configuration
 * @return muteSupport Enable muteSupport. Default false
 * */
@JvmOverloads
fun compile(
    src: File,
    compiledProgramsDir: File,
    format: Format = Format.BIN,
    muteSupport: Boolean = false
): Collection<CompilationResult> {
    val systemInterface = JavaSystemInterface()
    // In MainExecutionContext to avoid warning on idProvider reset
    val compilationResult = mutableListOf<CompilationResult>()
    if (src.isFile) {
        MainExecutionContext.execute(systemInterface = systemInterface) {
            compilationResult.add(compileFile(src, compiledProgramsDir, format, muteSupport))
        }
    } else {
        src.listFiles { file ->
            file.name.endsWith(".rpgle")
        }?.forEach { file ->
            MainExecutionContext.execute(systemInterface = systemInterface) {
                compilationResult.add(compileFile(file, compiledProgramsDir, format, muteSupport))
            }
        }
    }
    return compilationResult
}

/**
 * Compile programs
 * @param src Source (rpgle content) as inputstream
 * @param out Output (compiled source) as outpustream
 * @param format Compiled file format. Default Format.BIN
 * @param muteSupport Support for mute programs. Default false
 * */
@JvmOverloads
fun compile(
    src: InputStream,
    out: OutputStream,
    format: Format? = Format.BIN,
    muteSupport: Boolean? = false
) {
        println("Compiling inputstream to outputstream... ")

        var cu: CompilationUnit? = null
        cu = RpgParserFacade().apply {
            this.muteSupport = muteSupport!!
        }.parseAndProduceAst(src)

        when (format) {
            Format.BIN -> out.use { it.write(cu!!.encodeToByteArray()) }
            Format.JSON -> out.use { it.write(cu!!.encodeToString().toByteArray(Charsets.UTF_8)) }
        }

        println("... done.")
}