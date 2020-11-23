package com.smeup.rpgparser.utils

import com.smeup.rpgparser.parsing.ast.encodeToByteArray
import com.smeup.rpgparser.parsing.ast.encodeToString
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File
import java.io.FileInputStream

enum class Format {
    JSON, BIN
}

/**
 * Represents compilation result
 * @param srcFile Source file
 * @param compiledFile Compiled file
 * @param error Compilation error
 * */
data class CompilationResult(val srcFile: File, val compiledFile: File? = null, val error: Throwable? = null)

@ExperimentalSerializationApi
private fun compileFile(file: File, targetDir: File, format: Format): CompilationResult {
    runCatching {
        FileInputStream(file).use {
            val cu = RpgParserFacade().parseAndProduceAst(it)
            val compiledFile = when (format) {
                Format.BIN -> File(
                    targetDir, file.name.replaceAfterLast(
                        '.',
                        "bin",
                        "${file.name}.bin"
                    )
                ).apply { writeBytes(cu.encodeToByteArray()) }
                Format.JSON -> File(
                    targetDir, file.name.replaceAfterLast(
                        '.',
                        "json",
                        "${file.name}.json"
                    )
                ).apply { writeText(cu.encodeToString()) }
            }
            return CompilationResult(file, compiledFile)
        }
    }.onFailure {
        return CompilationResult(file, null, it)
    }
    error("Something has gone wrong")
}

/**
 * Compile programs
 * @param src Source file or dir
 * @param compiledProgramsDir The programs will be compiled in this directory
 * @param format Compiled file format. Default Format.BIN
 * @return Compilation results
 * */
@JvmOverloads
fun compile(src: File, compiledProgramsDir: File, format: Format = Format.BIN): Collection<CompilationResult> {
    val compilationResult = mutableListOf<CompilationResult>()
    if (src.isFile) {
        compilationResult.add(compileFile(src, compiledProgramsDir, format))
    } else {
        src.listFiles { file ->
            file.name.endsWith(".rpgle")
        }?.forEach { file -> compilationResult.add(compileFile(file, compiledProgramsDir, format)) }
    }
    return compilationResult
}
