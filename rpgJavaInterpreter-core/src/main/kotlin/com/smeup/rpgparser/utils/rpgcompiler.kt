package com.smeup.rpgparser.utils

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.encodeToByteArray
import com.smeup.rpgparser.parsing.ast.encodeToString
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import java.io.*

enum class Format(val ext: String) {
    JSON("json"), BIN("bin")
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

private fun compileFile(file: File, targetDir: File, format: Format, muteSupport: Boolean, force: Boolean = true): CompilationResult {
    runCatching {
        println("Compiling $file")
        val compiledFile = File(
            targetDir, file.name.replaceAfterLast(
                '.',
                format.ext,
                "${file.name}.${format.ext}"
            )
        )
        if (force || !compiledFile.exists() || compiledFile.lastModified() < file.lastModified()) {
            FileInputStream(file).use {
                var cu: CompilationUnit? = null
                runCatching {
                    cu = RpgParserFacade().apply {
                        this.muteSupport = muteSupport
                    }.parseAndProduceAst(it)
                }.onFailure {
                    println("Compilation skipped because of following ast creating error".yellow())
                    val errStream = ByteArrayOutputStream()
                    it.printStackTrace(PrintStream(errStream))
                    println(String(errStream.toByteArray()).yellow())
                    return CompilationResult(file, null, null, it)
                }
                when (format) {
                    Format.BIN -> compiledFile.writeBytes(cu!!.encodeToByteArray())
                    Format.JSON -> compiledFile.writeText(cu!!.encodeToString())
                }
                println("Compiled in $compiledFile")
            }
        } else {
            println("File is up to date")
        }
        return CompilationResult(file, compiledFile)
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
 * @param muteSupport Enable muteSupport. Default false
 * @param force If true skip check last modified version and src will be always compiled
 * @param systemInterface callback function for system interface creating
 * */
@JvmOverloads
fun compile(
    src: File,
    compiledProgramsDir: File,
    format: Format = Format.BIN,
    muteSupport: Boolean = false,
    force: Boolean = true,
    systemInterface: (dir: File) -> SystemInterface = { dir ->
        JavaSystemInterface().apply { rpgSystem.addProgramFinder(DirRpgProgramFinder(dir)) } }
): Collection<CompilationResult> {
    // In MainExecutionContext to avoid warning on idProvider reset
    val compilationResult = mutableListOf<CompilationResult>()
    if (src.isFile) {
        val systemInterface = systemInterface.invoke(src.parentFile)
        MainExecutionContext.execute(systemInterface = systemInterface) {
            it.executionProgramName = src.name
            compilationResult.add(compileFile(src, compiledProgramsDir, format, muteSupport, force))
        }
    } else if (src.exists()) {
        val systemInterface = systemInterface.invoke(src.absoluteFile)
        src.listFiles { file ->
            file.name.endsWith(".rpgle")
        }?.forEach { file ->
            MainExecutionContext.execute(systemInterface = systemInterface) {
                it.executionProgramName = file.name
                compilationResult.add(compileFile(file, compiledProgramsDir, format, muteSupport, force))
            }
        }
    } else {
        println("$src not exists".yellow())
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
    muteSupport: Boolean? = false,
    programFinders: List<RpgProgramFinder>? = null
) {

    // Compilation within MainExecutionContext should ensure comparability among rpgle program compiled in
    // different times
    MainExecutionContext.execute(systemInterface = JavaSystemInterface().apply {
        programFinders?.let { rpgSystem.addProgramFinders(it) }
    }) {
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
}