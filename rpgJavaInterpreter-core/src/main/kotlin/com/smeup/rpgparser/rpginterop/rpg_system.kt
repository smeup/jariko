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

package com.smeup.rpgparser.rpginterop

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.interpreter.InterpreterLogHandler
import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.interpreter.RpgProgramFinderLogEntry
import com.smeup.rpgparser.interpreter.log
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.key
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path

interface RpgProgramFinder {
    fun findRpgProgram(nameOrSource: String): RpgProgram?

    fun findCopy(copyId: CopyId): Copy?

    fun findApiDescriptor(apiId: ApiId): ApiDescriptor?

    fun findApi(apiId: ApiId): Api?
}

class SourceProgramFinder : RpgProgramFinder {
    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        if (nameOrSource.contains("\n") || nameOrSource.contains("\r")) {
            return RpgProgram.fromInputStream(ByteArrayInputStream(nameOrSource.toByteArray(Charsets.UTF_8)), nameOrSource)
        }
        return null
    }

    override fun toString(): String {
        return "source:"
    }

    override fun findCopy(copyId: CopyId): Copy? {
        // source program finder cannot know how to look for a copy
        return null
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor? {
        TODO("Not yet implemented")
    }

    override fun findApi(apiId: ApiId): Api? {
        TODO("Not yet implemented")
    }
}

open class DirRpgProgramFinder(val directory: File? = null) : RpgProgramFinder {

    private var foundProgram: (path: Path) -> Unit = { _: Path -> }

    init {
        directory?.let { require(it.exists()) { "The specified directory should exist: ${directory.path} -> ${directory.absolutePath}" } }
    }

    /**
     * Called when this finder found a program
     * */
    open fun foundProgram(foundProgram: (path: Path) -> Unit) {
        this.foundProgram = foundProgram
    }

    private fun File.notifyFound() = foundProgram.invoke(this.toPath())

    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        // TODO
        // refactor to avoid code duplication and a better handle of
        // new SourceProgram extensions
        val file = File(prefix() + nameAndSuffix(nameOrSource))

        // InputStream from '.rpgle' program
        if (nameOrSource.endsWith(SourceProgram.RPGLE.extension) && file.exists()) {
            file.notifyFound()
            return RpgProgram.fromInputStream(FileInputStream(file), nameOrSource, SourceProgram.RPGLE)
        }

        // InputStream from '.bin' program
        if (nameOrSource.endsWith(SourceProgram.BINARY.extension) && file.exists()) {
            file.notifyFound()
            return RpgProgram.fromInputStream(FileInputStream(file), nameOrSource, SourceProgram.BINARY)
        }

        // No extension, should be '.rpgle' or '.bin'
        if (!nameOrSource.endsWith(SourceProgram.RPGLE.extension) &&
            !nameOrSource.endsWith(SourceProgram.BINARY.extension)) {
            var anonymouosFile = File("${prefix()}$nameOrSource.${SourceProgram.RPGLE.extension}")
            if (anonymouosFile.exists()) {
                anonymouosFile.notifyFound()
                return RpgProgram.fromInputStream(FileInputStream(anonymouosFile), nameOrSource, SourceProgram.RPGLE)
            } else {
                anonymouosFile = File("${prefix()}$nameOrSource.${SourceProgram.BINARY.extension}")
                if (anonymouosFile.exists()) {
                    anonymouosFile.notifyFound()
                    return RpgProgram.fromInputStream(FileInputStream(anonymouosFile), nameOrSource, SourceProgram.BINARY)
                } else {
                    return null
                }
            }
        }
        return null
    }

    override fun findCopy(copyId: CopyId): Copy? {
        val file = copyId.toFile(directory, SourceProgram.RPGLE).takeIf {
            it.exists()
        } ?: copyId.toFile(directory, SourceProgram.BINARY).takeIf {
            it.exists()
        }
        return file?.let {
            Copy.fromInputStream(FileInputStream(file.absoluteFile))
        }
    }

    private fun prefix(): String {
        if (directory != null) {
            return directory.absolutePath + File.separator
        }
        return ""
    }

    private fun nameAndSuffix(name: String): String {
        // If name 'ABC' or 'ABC.rpgle' assume 'ABC.rpgle'
        // If name 'ABC.bin' assume 'ABC.bin'
        if (name.endsWith(SourceProgram.RPGLE.extension) ||
            name.endsWith(SourceProgram.BINARY.extension)) {
            return name
        }
        return "$name.${SourceProgram.RPGLE.extension}"
    }

    override fun toString(): String {
        val path = if (directory == null) "" else directory.absolutePath.toString()
        return "directory: $path "
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor? {
        val rpgleFile = apiId.toFileApiDescriptor(directory, SourceProgram.RPGLE)
        val binaryFile = apiId.toFileApiDescriptor(directory, SourceProgram.BINARY)
        val file = rpgleFile.takeIf {
            it.exists()
        } ?: binaryFile.takeIf {
            it.exists()
        }
        return file?.let {
            val extension = file.name.substringAfterLast(".")
            Api.loadApiDescriptor(file.inputStream(), SourceProgram.getByExtension(extension))
        }
    }

    override fun findApi(apiId: ApiId): Api? {
        val file = apiId.toFile(directory, SourceProgram.RPGLE).takeIf {
            it.exists()
        } ?: apiId.toFile(directory, SourceProgram.BINARY).takeIf {
            it.exists()
        }
        return file?.let {
            val extension = file.name.substringAfterLast(".")
            Api.loadApi(file.inputStream(), SourceProgram.getByExtension(extension))
        }
    }
}

open class RpgSystem {

    internal val programFinders = mutableSetOf<RpgProgramFinder>()

    companion object {
        var SINGLETON_RPG_SYSTEM: RpgSystem? = null
    }

    @Synchronized
    fun addProgramFinders(programFindersList: List<RpgProgramFinder>) {
        programFinders.addAll(programFindersList)
    }

    @Synchronized
    fun addProgramFinder(programFinder: RpgProgramFinder) {
        programFinders.add(programFinder)
    }

    @Synchronized
    open fun getProgram(programName: String): RpgProgram {
        programFinders.forEach {
            val program = it.findRpgProgram(programName)
            if (program != null) {
                return program
            }
        }
        SINGLETON_RPG_SYSTEM?.let {
            if (this != it && it.programFinders.isNotEmpty()) {
                return it.getProgram(programName)
            }
        }
        throw RuntimeException("Program $programName not found")
    }

    @Synchronized
    open fun getCopy(id: CopyId): Copy {
        programFinders.forEach {
            val copy = it.findCopy(id)
            if (copy != null) {
                return copy
            }
        }
        SINGLETON_RPG_SYSTEM?.let {
            if (this != it && it.programFinders.isNotEmpty()) {
                return it.getCopy(id)
            }
        }
        throw RuntimeException("Cannot retrieve copy $id from: $programFinders")
    }

    @Synchronized
    fun log(logHandlers: List<InterpreterLogHandler>) {
        programFinders.forEach {
            logHandlers.log(RpgProgramFinderLogEntry(it.toString()))
        }
    }

    fun findApiDescriptor(apiId: ApiId): ApiDescriptor {
        programFinders.forEach {
            val apiDescriptor = it.findApiDescriptor(apiId)
            if (apiDescriptor != null) {
                return apiDescriptor
            }
        }
        SINGLETON_RPG_SYSTEM?.let {
            if (this != it && it.programFinders.isNotEmpty()) {
                return it.findApiDescriptor(apiId)
            }
        }
        return ApiDescriptor().apply {
            println("ApiDescriptor for $apiId not found, returning $this".yellow())
        }
    }

    fun findApi(apiId: ApiId): Api {
        programFinders.forEach {
            val api = it.findApi(apiId)
            if (api != null) {
                return api
            }
        }
        SINGLETON_RPG_SYSTEM?.let {
            if (this != it && it.programFinders.isNotEmpty()) {
                return it.findApi(apiId)
            }
        }
        throw RuntimeException("Api $apiId not found")
    }
}

private fun CopyId.toFile(dir: File?, sourceProgram: SourceProgram) = File(dir, this.key(sourceProgram))

private fun ApiId.toFile(dir: File?, sourceProgram: SourceProgram) = File(dir, this.key(sourceProgram))

private fun ApiId.toFileApiDescriptor(dir: File?, sourceProgram: SourceProgram) = File(dir, this.apiDescriptorKey(sourceProgram))
