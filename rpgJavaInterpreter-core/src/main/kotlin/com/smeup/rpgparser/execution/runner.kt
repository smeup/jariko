package com.smeup.rpgparser.execution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.defaultLoggingConfiguration
import com.smeup.rpgparser.logging.loadLogConfiguration
import com.smeup.rpgparser.rgpinterop.*
import java.io.File
import org.apache.commons.io.input.BOMInputStream

class CommandLineParms(val parmsList: List<String>)

class CommandLineProgramNameSource(val name: String) : ProgramNameSource<CommandLineParms> {
    override fun nameFor(rpgFacade: RpgFacade<CommandLineParms>): String = name
}

class CommandLineProgram(name: String, systemInterface: SystemInterface) : RpgFacade<CommandLineParms>((CommandLineProgramNameSource(name)), systemInterface) {
    override fun toInitialValues(params: CommandLineParms): LinkedHashMap<String, Value> {
        val result = LinkedHashMap<String, Value> ()
        val values = params.parmsList.map { parameter -> StringValue(parameter) }
        val zipped = rpgProgram.params()
                .map { dataDefinition -> dataDefinition.name }
                .zip(values)
        zipped.forEach {
            result[it.first] = it.second
        }
        return result
    }

    override fun toResults(params: CommandLineParms, resultValues: LinkedHashMap<String, Value>): CommandLineParms {
        if (params.parmsList.isEmpty()) {
            return params
        }
        return CommandLineParms(resultValues.values.map { it.asString().valueWithoutPadding })
    }

    fun singleCall(parms: List<String>) = singleCall(CommandLineParms(parms))
}

class ResourceProgramFinder(val path: String) : RpgProgramFinder {
    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        val resourceStream = ResourceProgramFinder::class.java.getResourceAsStream("$path$nameOrSource.rpgle")
        return if (resourceStream != null) {
            RpgProgram.fromInputStream(BOMInputStream(resourceStream))
        } else {
            println("Resource $path not found")
            null
        }
    }
}

@JvmOverloads
fun getProgram(nameOrSource: String, systemInterface: SystemInterface = JavaSystemInterface()): CommandLineProgram {
    // TODO move this to some configuration file
    RpgSystem.addProgramFinder(SourceProgramFinder())
    RpgSystem.addProgramFinder(DirRpgProgramFinder())
//    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("examples/rpg")))
//    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("rpgJavaInterpreter-core/src/test/resources")))
//    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("/")))
//    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("/rpg")))
    RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
    return CommandLineProgram(nameOrSource, systemInterface)
}

fun executePgmWithStringArgs(
    programName: String,
    programArgs: List<String>,
    logConfigurationFile: File? = null
) {
    val systemInterface = JavaSystemInterface()
    systemInterface.loggingConfiguration = logConfigurationFile?.let { loadLogConfiguration(logConfigurationFile) } ?: defaultLoggingConfiguration()
    val commandLineProgram = getProgram(programName, systemInterface)
    commandLineProgram.singleCall(programArgs)
}

object RunnerCLI : CliktCommand() {
    val logConfigurationFile by option("-lc", "--log-configuration").file(exists = true, readable = true)
    val programName by argument("program name")
    val programArgs by argument().multiple(required = false)

    override fun run() {
        executePgmWithStringArgs(programName, programArgs, logConfigurationFile)
    }
}

fun startShell() {
    SimpleShell.repl { programName, programArgs ->
        executePgmWithStringArgs(programName, programArgs)
    }
}

/**
 * This program can be used to either launch an RPG program or the shell.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        startShell()
    } else {
        RunnerCLI.main(args)
    }
}
