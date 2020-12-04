package com.smeup.rpgparser.execution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.file
import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.defaultLoggingConfiguration
import com.smeup.rpgparser.logging.loadLogConfiguration
import com.smeup.rpgparser.rpginterop.*
import org.apache.commons.io.input.BOMInputStream
import java.io.File

class CommandLineParms(val parmsList: List<String>)

class CommandLineProgramNameSource(val name: String) : ProgramNameSource<CommandLineParms> {
    override fun nameFor(rpgFacade: RpgFacade<CommandLineParms>): String = name
}

class CommandLineProgram(name: String, systemInterface: SystemInterface) :
    RpgFacade<CommandLineParms>((CommandLineProgramNameSource(name)), systemInterface) {
    override fun toInitialValues(rpgProgram: RpgProgram, params: CommandLineParms): LinkedHashMap<String, Value> {
        val result = LinkedHashMap<String, Value>()
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
        return CommandLineParms(resultValues.values.map { it.asString().value })
    }

    @JvmOverloads
    fun singleCall(parms: List<String>, configuration: Configuration = Configuration()) =
        singleCall(CommandLineParms(parms), configuration = configuration)
}

class ResourceProgramFinder(val path: String) : RpgProgramFinder {
    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        val resourceStream = ResourceProgramFinder::class.java.getResourceAsStream("$path$nameOrSource.rpgle")
        return if (resourceStream != null) {
            RpgProgram.fromInputStream(BOMInputStream(resourceStream), nameOrSource)
        } else {
            null
        }
    }

    override fun toString(): String {
        return "resource: $path"
    }
}

val defaultProgramFinders = listOf(
    SourceProgramFinder(),
    DirRpgProgramFinder(),
    ResourceProgramFinder("/")
)

@JvmOverloads
fun getProgram(
    nameOrSource: String,
    systemInterface: SystemInterface = JavaSystemInterface(),
    programFinders: List<RpgProgramFinder> = defaultProgramFinders
): CommandLineProgram {

    programFinders.forEach {
        RpgSystem.addProgramFinder(it)
    }

    RpgSystem.log(systemInterface.getAllLogHandlers())

    return CommandLineProgram(nameOrSource, systemInterface)
}

fun executePgmWithStringArgs(
    programName: String,
    programArgs: List<String>,
    logConfigurationFile: File? = null,
    programFinders: List<RpgProgramFinder> = defaultProgramFinders,
    configuration: Configuration = Configuration()
) {
    val systemInterface = JavaSystemInterface()
    systemInterface.loggingConfiguration =
        logConfigurationFile
            ?.let(::loadLogConfiguration)
            ?: defaultLoggingConfiguration()
    val commandLineProgram = getProgram(programName, systemInterface, programFinders)
    commandLineProgram.singleCall(programArgs, configuration = configuration)
}

object RunnerCLI : CliktCommand() {
    val logConfigurationFile by option("-lc", "--log-configuration").file(exists = true, readable = true)
    val programsSearchDirs by option("-psd").split(",")
    val compiledProgramDir by option("-cpd", "--compiled-program-dir").file(exists = true, readable = true)
    val programName by argument("program name")
    val programArgs by argument().multiple(required = false)

    override fun run() {
        val allProgramFinders =
            defaultProgramFinders + (programsSearchDirs?.map { DirRpgProgramFinder(File(it)) } ?: emptyList())
        val configuration = Configuration()
        configuration.options?.compiledProgramsDir = compiledProgramDir
        executePgmWithStringArgs(
            programName, programArgs, logConfigurationFile, programFinders = allProgramFinders,
            configuration = configuration
        )
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