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

class CommandLineProgram(name: String, systemInterface: SystemInterface) : RpgFacade<CommandLineParms>((CommandLineProgramNameSource(name)),  systemInterface) {
    override fun toInitialValues(params: CommandLineParms) : LinkedHashMap<String, Value> {
        val result = LinkedHashMap<String, Value> ()
        val values = params.parmsList.map { parameter -> StringValue(parameter ) }
        val zipped = rpgProgram.params()
                .map {dataDefinition -> dataDefinition.name }
                .zip(values)
        zipped.forEach{
            result[it.first] = it.second
        }
        return result;
    }

    override fun toResults(params: CommandLineParms, resultValues: LinkedHashMap<String, Value>) : CommandLineParms {
        if (params.parmsList.isEmpty()) {
            return params
        }
        return CommandLineParms(resultValues.values.map { it.asString().valueWithoutPadding })
    }

    fun singleCall(parms: List<String>) =  singleCall(CommandLineParms(parms))
}

class ResourceProgramFinder(val path: String): RpgProgramFinder {
    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        val resourceStream = ResourceProgramFinder::class.java.getResourceAsStream("$path$nameOrSource.rpgle")
        return if (resourceStream != null) {
            RpgProgram.fromInputStream(BOMInputStream(resourceStream))
        } else {
            println("Resource ${path} not found")
            null
        }
    }
}

//Method for Java programs
fun getProgram(nameOrSource: String) : CommandLineProgram = getProgram(nameOrSource,  JavaSystemInterface())

fun getProgram(nameOrSource: String, systemInterface: SystemInterface = JavaSystemInterface()) : CommandLineProgram {
    RpgSystem.addProgramFinder(SourceProgramFinder())
    RpgSystem.addProgramFinder(DirRpgProgramFinder())
    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("examples/rpg")))
    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("rpgJavaInterpreter-core/src/test/resources")))
    RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
    RpgSystem.addProgramFinder(ResourceProgramFinder("/rpg"))
    return CommandLineProgram(nameOrSource, systemInterface)
}

fun main(args : Array<String>) {
    if (args.isEmpty()) {
        SimpleShell().repl(::executePgmWithStringArgs)
    } else {
        executePgmWithStringArgs(args)
    }
}

fun executePgmWithStringArgs(args: Array<String>) {
    getProgram(args[0]).singleCall(args.asList().subList(1, args.size))
}

