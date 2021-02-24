package com.smeup.rpgparser.rpginterop

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.CopyId
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.ast.key
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

interface RpgProgramFinder {
    fun findRpgProgram(nameOrSource: String): RpgProgram?

    fun findCopy(copyId: CopyId): Copy?
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
        TODO("Not yet implemented")
    }
}

class DirRpgProgramFinder(val directory: File? = null) : RpgProgramFinder {

    init {
        directory?.let { require(it.exists()) { "The specified directory should exist: ${directory.path} -> ${directory.absolutePath}" } }
    }

    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        // TODO
        // refactor to avoid code duplication and a better handle of
        // new SourceProgram exetensions
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

    override fun findCopy(copyId: CopyId): Copy? {
        val file = copyId.toFile(directory, SourceProgram.RPGLE).takeIf {
            it.exists()
        } ?: copyId.toFile(directory, SourceProgram.BINARY).takeIf {
            it.exists()
        }
        return file?.let {
            Copy.fromInputStream(
                inputStream = FileInputStream(file.absoluteFile),
                copyId = copyId,
                sourceProgram = it.toSourceProgram()
            )
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
}

private fun String.printStackTrace() {
    println("$this at:".yellow())
    Thread.currentThread().stackTrace.filter {
        it.className.startsWith("com.smeup.")
    }.forEach {
        println("${it.className}.${it.methodName}".yellow())
    }
}

/**
 * Introduced only for compatibility with test cases that used still RpgSystem as singleton object
 * */
object SingletonRpgSystem : RpgSystem() {

    override fun getProgram(programName: String): RpgProgram {
        ("Check stacktrace because in production environment the use of SingletonRpgSystem will be deprecated").printStackTrace()
        return super.getProgram(programName)
    }

    override fun getCopy(id: CopyId): Copy {
        ("Check stacktrace because in production environment the use of SingletonRpgSystem will be deprecated").printStackTrace()
        return super.getCopy(id)
    }
}

open class RpgSystem {

    val programFinders = mutableSetOf<RpgProgramFinder>()

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
        if (this != SingletonRpgSystem && SingletonRpgSystem.programFinders.isNotEmpty()) {
            return SingletonRpgSystem.getProgram(programName)
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
        // very bad but is needed for compatibility
        if (this != SingletonRpgSystem && SingletonRpgSystem.programFinders.isNotEmpty()) {
            return SingletonRpgSystem.getCopy(id)
        }
        throw RuntimeException("Cannot retrieve copy $id from: $programFinders")
    }

    @Synchronized
    fun log(logHandlers: List<InterpreterLogHandler>) {
        programFinders.forEach {
            logHandlers.log(RpgProgramFinderLogEntry(it.toString()))
        }
    }
}

private fun CopyId.toFile(dir: File?, sourceProgram: SourceProgram) = File(dir, this.key(sourceProgram))

fun File.toSourceProgram() = absolutePath.substringAfterLast('.', SourceProgram.RPGLE.extension).toSourceProgram()

fun String.toSourceProgram() = SourceProgram.values().first {
    it.extension == this
}
