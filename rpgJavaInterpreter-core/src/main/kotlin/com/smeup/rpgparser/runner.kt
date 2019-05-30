package com.smeup.rpgparser

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rgpinterop.*
import org.apache.commons.io.input.BOMInputStream
import java.io.File

class CommandLineParms(val parmsList: List<String>)

class CommandLineProgramNameSource(val name: String) : ProgramNameSource<CommandLineParms> {
    override fun nameFor(rpgFacade: RpgFacade<CommandLineParms>): String = name
}

class CommandLineProgram(val name: String, systemInterface: SystemInterface) : RpgFacade<CommandLineParms>((CommandLineProgramNameSource(name))) {
    override fun toInitialValues(params: CommandLineParms) : Map<String, Value> {
        if (params.parmsList.isEmpty()) {
            return mapOf()
        }
        val values = params.parmsList.map { parameter -> StringValue(parameter ?: "") }
        return rpgProgram.params()
                .map {dataDefinition -> dataDefinition.name }
                .zip(values)
                .toMap()
    }

    fun singleCall(parms: List<String>) =  singleCall(CommandLineParms(parms))
}

class ResourceProgramFinder(val path: String): RpgProgramFinder {
    override fun findRpgProgram(name: String): RpgProgram? {
        val resourceStream = ResourceProgramFinder::class.java.getResourceAsStream("$path$name.rpgle")
        return if (resourceStream != null) {
            RpgProgram.fromInputStream(BOMInputStream(resourceStream))
        } else {
            println("Resource ${path} not found")
            null
        }
    }
}

fun getProgram(name: String, systemInterface: SystemInterface = JavaSystemInterface) : CommandLineProgram {
    RpgSystem.addProgramFinder(DirRpgProgramFinder())
    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("examples/rpg")))
    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("rpgJavaInterpreter-core/src/test/resources")))
    RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
    return CommandLineProgram(name, systemInterface)
}

fun main(args : Array<String>) {
    if (args.isEmpty()) {
        println("Please provide the name of a .rpgle file to interpret")
        return
    }
    getProgram(args[0]).singleCall(args.asList().subList(1, args.size))
}

