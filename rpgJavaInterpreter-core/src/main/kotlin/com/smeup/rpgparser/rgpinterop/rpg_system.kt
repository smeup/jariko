package com.smeup.rpgparser.rgpinterop

import com.smeup.rpgparser.interpreter.RpgProgram
import java.io.File
import java.io.FileInputStream
import java.util.*

interface RpgProgramFinder {
    fun findRpgProgram(name: String) : RpgProgram?
}

class DirRpgProgramFinder(val directory: File) : RpgProgramFinder {
    override fun findRpgProgram(name: String): RpgProgram? {
        val file = File(directory.absolutePath + File.separator + name + ".rpgle")
        return if (file.exists()) {
            RpgProgram.fromInputStream(FileInputStream(file), name)
        } else {
            null
        }
    }

}

object RpgSystem {
    private val programFinders = LinkedList<RpgProgramFinder>()

    fun addProgramFinder(programFinder: RpgProgramFinder) {
        programFinders.add(programFinder)
    }

    fun getProgram(programName: String): RpgProgram {
        programFinders.forEach {
            val program = it.findRpgProgram(programName)
            if (program != null) {
                return program
            }
        }
        throw RuntimeException("Program $programName not found")
    }
}