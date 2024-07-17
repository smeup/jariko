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

package com.smeup.rpgparser.utils

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.encodeToByteArray
import com.smeup.rpgparser.parsing.ast.encodeToString
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.parsetreetoast.getAstCreationErrors
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
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
 * @param parsingError Parsing error
 * */
data class CompilationResult(
    val srcFile: File,
    val compiledFile: File? = null,
    val error: Throwable? = null,
    val parsingError: Throwable? = null
)

private fun compileFile(
    file: File,
    targetDir: File,
    format: Format,
    muteSupport: Boolean,
    force: Boolean = true,
    allowCompilationError: (file: File, error: Throwable) -> Boolean = { _, _ -> false }
): CompilationResult {
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
            // I need to delete compiledFile because, in case of configuration.options.compiledProgramsDir
            // being set and compiledFile already exist, binary file is not rebuilt
            if (compiledFile.exists()) {
                println("Deleting $compiledFile")
                require(compiledFile.delete()) { "Cannot delete $compiledFile" }
            }
            FileInputStream(file).use {
                var cu: CompilationUnit? = null
                runCatching {
                    cu = RpgParserFacade().apply {
                        this.muteSupport = muteSupport
                    }.parseAndProduceAst(it)
                    // In case of errors in API parseAndProduceAst(it) could not be blocking
                    // and then, I verify if there are errors
                    if (getAstCreationErrors().isNotEmpty()) {
                        throw getAstCreationErrors().first()
                    }
                }.onFailure { error ->
                    if (allowCompilationError(file, error)) {
                        println("Ast creating error not blocking because this program is in a white list".yellow())
                        val errStream = ByteArrayOutputStream()
                        error.printStackTrace(PrintStream(errStream))
                        println(String(errStream.toByteArray()).yellow())
                        return CompilationResult(file, null, null, error)
                    } else {
                        throw error
                    }
                }
                when (format) {
                    Format.BIN -> compiledFile.writeBytes(cu!!.encodeToByteArray())
                    Format.JSON -> compiledFile.writeText(cu!!.encodeToString())
                }
                // I cannot resolve and validate the cu before serializing elsewhere
                // I have unexpected behaviours when I try to use it
                runCatching {
                    cu!!.resolveAndValidate()
                }.onFailure {
                    compiledFile.delete()
                    return CompilationResult(file, null, null, it)
                }
                println("Compiled in $compiledFile")
            }
        } else {
            println("File is up to date")
        }
        return CompilationResult(file, compiledFile)
    }.onFailure {
        System.err.println("Error during AST serializing")
        it.printStackTrace()
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
 * @param configuration Could be useful to pass this parameter in order to enable a few of advanced settings. For
 * example, you can pass an option to enable the source dump in case of error, this feature for default is not
 * enabled for performances reason.
 * @param allowFile if true that file will be compiled if its extension is .rpgle else that file will not be compiled
 * @param allowCompilationError if true the error on compilation is considered not blocking
 * */
@JvmOverloads
fun compile(
    src: File,
    compiledProgramsDir: File,
    format: Format = Format.BIN,
    muteSupport: Boolean = false,
    force: Boolean = true,
    systemInterface: (dir: File) -> SystemInterface = { dir ->
        JavaSystemInterface().apply { rpgSystem.addProgramFinder(DirRpgProgramFinder(dir)) } },
    configuration: Configuration = Configuration(),
    allowFile: (file: File) -> Boolean = { true },
    allowCompilationError: (file: File, error: Throwable) -> Boolean = { _, _ -> false }
): Collection<CompilationResult> {
    // In MainExecutionContext to avoid warning on idProvider reset
    val compilationResult = mutableListOf<CompilationResult>()
    if (src.isFile) {
        val si = systemInterface.invoke(src.parentFile)
        MainExecutionContext.execute(systemInterface = si, configuration = configuration) {
            it.executionProgramName = src.name
            compilationResult.add(
                compileFile(
                    file = src,
                    targetDir = compiledProgramsDir,
                    format = format,
                    muteSupport = muteSupport,
                    force = force,
                    allowCompilationError = allowCompilationError
                )
            )
        }
    } else if (src.exists()) {
        val si = systemInterface.invoke(src.absoluteFile)
        src.listFiles { file ->
            if (allowFile.invoke(file)) {
                file.name.endsWith(".rpgle")
            } else false
        }?.forEach { file ->
            MainExecutionContext.execute(systemInterface = si, configuration = configuration) {
                it.executionProgramName = file.name
                compilationResult.add(
                    compileFile(
                        file = file,
                        targetDir = compiledProgramsDir,
                        format = format,
                        muteSupport = muteSupport,
                        force = force,
                        allowCompilationError = allowCompilationError
                    )
                )
            }
        }
    } else {
        println("$src not exists".yellow())
    }
    return compilationResult
}

/**
 * Compile a src file or directory in compiledProgramsDir. This method hides all parameters that generally
 * are not useful in production environments.
 * @param src Source file or dir
 * @param compiledProgramsDir The programs will be compiled in this directory
 * @param configuration Could be useful to pass this parameter in order to enable a few of advanced settings. For
 * example, you can pass an option to enable the source dump in case of error, this feature for default is not
 * enabled for performances reason.
 * */
fun compile(src: File, compiledProgramsDir: File, configuration: Configuration): Collection<CompilationResult> {
    return compile(src = src, compiledProgramsDir = compiledProgramsDir,
        format = Format.BIN, configuration = configuration)
}

/**
 * Compile program
 * @param src Source (rpgle content) as inputstream
 * @param out Output (compiled source) as outpustream
 * @param format Compiled file format. Default Format.BIN
 * @param muteSupport Support for mute programs. Default false
 * @param programFinders The program finders. This parameter is necessary in case of compiled program
 * that contains a copy directive.
 * */
@JvmOverloads
fun compile(
    src: InputStream,
    out: OutputStream,
    format: Format? = Format.BIN,
    muteSupport: Boolean? = false,
    programFinders: List<RpgProgramFinder>? = null,
    configuration: Configuration = Configuration()
) {
    // Compilation within MainExecutionContext should ensure comparability among rpgle programs compiled in
    // different times
    MainExecutionContext.execute(systemInterface = JavaSystemInterface().apply {
        programFinders?.let { rpgSystem.addProgramFinders(it) }
    }, configuration = configuration) {
        doCompilationAtRuntime(src = src, out = out, format = format, muteSupport = muteSupport)
    }
}

/**
 * Compile program at runtime.
 * This method is available only if called during execution, for example because
 * you needs to serialize a called program before its loading
 * @param src Source (rpgle content) as inputstream
 * @param out Output (compiled source) as outpustream
 * @param format Compiled file format. Default Format.BIN
 * @param muteSupport Support for mute programs. Default false
 * */
@JvmOverloads
fun doCompilationAtRuntime(
    src: InputStream,
    out: OutputStream,
    format: Format? = Format.BIN,
    muteSupport: Boolean? = false
) {
    require(MainExecutionContext.isCreated()) {
        "This method can be used just for runtime compilations"
    }
    println("Compiling inputstream to outputstream... ")
    var cu: CompilationUnit?
    cu = RpgParserFacade().apply {
        this.muteSupport = muteSupport!!
    }.parseAndProduceAst(src)

    runCatching {
        when (format) {
            Format.BIN -> out.use { it.write(cu.encodeToByteArray()) }
            Format.JSON -> out.use { it.write(cu.encodeToString().toByteArray(Charsets.UTF_8)) }
            else -> error("$format not handled")
        }
    }.onFailure { error ->
        MainExecutionContext.getConfiguration().jarikoCallback.onCompilationUnitEncodingError(error, cu, format)
    }

    cu.resolveAndValidate()
    println("... done.")
}
