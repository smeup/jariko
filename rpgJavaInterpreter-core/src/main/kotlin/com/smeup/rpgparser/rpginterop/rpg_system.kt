package com.smeup.rpgparser.rpginterop

import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.SourceProgram
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

interface RpgProgramFinder {
    fun findRpgProgram(nameOrSource: String): RpgProgram?
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
}

class DirRpgProgramFinder(val directory: File? = null) : RpgProgramFinder {

    init {
        directory?.let { require(it.exists()) { "The specified directory should exist: ${directory.path} -> ${directory.absolutePath}" } }
    }

    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        val file = File(prefix() + nameAndSuffix(nameOrSource))

        // InputStream from '.rpgle' program
        if (nameOrSource.endsWith(SourceProgram.RPGLE.extension) && file.exists()) {
            return RpgProgram.fromInputStream(FileInputStream(file), nameOrSource, SourceProgram.RPGLE)
        }

        // InputStream from '.bin' program
        if (nameOrSource.endsWith(SourceProgram.BINARY.extension) && file.exists()) {
            return RpgProgram.fromInputStream(FileInputStream(file), nameOrSource, SourceProgram.BINARY)
        }

        // No extension, should be '.rpgle' or '.bin'
        if (!nameOrSource.endsWith(SourceProgram.RPGLE.extension) &&
            !nameOrSource.endsWith(SourceProgram.BINARY.extension)) {
            var anonymouosFile = File("${prefix()}$nameOrSource.${SourceProgram.RPGLE.extension}")
            if (anonymouosFile.exists()) {
                return RpgProgram.fromInputStream(FileInputStream(anonymouosFile), nameOrSource, SourceProgram.RPGLE)
            } else {
                anonymouosFile = File("${prefix()}$nameOrSource.${SourceProgram.BINARY.extension}")
                if (anonymouosFile.exists()) {
                    return RpgProgram.fromInputStream(FileInputStream(anonymouosFile), nameOrSource, SourceProgram.BINARY)
                } else {
                    return null
                }
            }
        }
        return null
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
}

object RpgSystem {

    private val programFinders = mutableSetOf<RpgProgramFinder>()

    @Synchronized
    fun addProgramFinders(programFindersList: List<RpgProgramFinder>) {
        programFinders.addAll(programFindersList)
    }

    @Synchronized
    fun addProgramFinder(programFinder: RpgProgramFinder) {
        programFinders.add(programFinder)
    }

    @Synchronized
    fun getProgram(programName: String): RpgProgram {
        programFinders.forEach {
            val program = it.findRpgProgram(programName)
            if (program != null) {
                return program
            }
        }
        throw RuntimeException("Program $programName not found")
    }

    @Synchronized
    fun log(logHandlers: List<InterpreterLogHandler>) {
        programFinders.forEach {
            logHandlers.log(RpgProgramFinderLogEntry(it.toString()))
        }
    }
}
