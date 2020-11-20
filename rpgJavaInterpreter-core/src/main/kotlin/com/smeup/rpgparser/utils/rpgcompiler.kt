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

@ExperimentalSerializationApi
private fun compileFile(file: File, targetDir: File, format: Format): File {
    val compiledFile: File
    FileInputStream(file).use {
        val cu = RpgParserFacade().parseAndProduceAst(it)
        compiledFile = when (format) {
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
    }
    return compiledFile
}

/**
 * Compile programs
 * @param src Source file or dir
 * @param compiledProgramsDir The programs will be compiled in this directory
 * @param format Compiled file format. Default Format.BIN
 * @return Compiled files
 * */
@JvmOverloads
fun compile(src: File, compiledProgramsDir: File, format: Format = Format.BIN): Collection<File> {
    val compiledFiles = mutableListOf<File>()
    if (src.exists()) {
        if (src.isFile) {
            compiledFiles.add(compileFile(src, compiledProgramsDir, format))
        } else {
            src.listFiles { file ->
                file.name.endsWith(".rpgle")
            }.forEach { file -> compiledFiles.add(compileFile(file, compiledProgramsDir, format)) }
        }
    }
    return compiledFiles
}
