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

package com.smeup.rpgparser.execution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.file
import com.smeup.dbnative.ConnectionConfig
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.defaultLoggingConfiguration
import com.smeup.rpgparser.logging.loadLogConfiguration
import com.smeup.rpgparser.parsing.ast.Api
import com.smeup.rpgparser.parsing.ast.ApiDescriptor
import com.smeup.rpgparser.parsing.ast.ApiId
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.rpginterop.*
import org.apache.commons.io.input.BOMInputStream
import java.io.File

class CommandLineParms internal constructor(
    val parmsList: List<String>,
    val namedParams: Map<String, Value>? = null,
    val namedParamsProducer: (compilationUnit: CompilationUnit) -> Map<String, Value>?
) {
    constructor(parmsList: List<String>) : this(
        parmsList = parmsList,
        namedParams = null,
        namedParamsProducer = { null }
    )
    constructor(namedParams: Map<String, Value>) : this(
        parmsList = emptyList(),
        namedParams = namedParams,
        namedParamsProducer = { null }
    )
    constructor(namedParamsProducer: (compilationUnit: CompilationUnit) -> Map<String, Value>) : this(
        parmsList = emptyList(),
        namedParams = null,
        namedParamsProducer = namedParamsProducer
    )
}

class CommandLineProgramNameSource(val name: String) : ProgramNameSource<CommandLineParms> {
    override fun nameFor(rpgFacade: RpgFacade<CommandLineParms>): String = name
}

class CommandLineProgram(name: String, systemInterface: SystemInterface) : RpgFacade<CommandLineParms>((CommandLineProgramNameSource(name)), systemInterface) {

    override fun toInitialValues(program: Program, params: CommandLineParms): LinkedHashMap<String, Value> {
        val result = LinkedHashMap<String, Value> ()
        // che cu is available only if the first program is not doped
        val producedNamedParams = when (program) {
            is RpgProgram -> params.namedParamsProducer.invoke(program.cu)
            else -> null
        }
        if (producedNamedParams != null) {
            result.putAll(producedNamedParams)
        } else if (params.namedParams != null) {
            result.putAll(params.namedParams)
        } else {
            val values = params.parmsList.map { parameter -> StringValue(parameter) }
            val zipped = program.params()
                .map { dataDefinition -> dataDefinition.name }
                .zip(values)
            zipped.forEach {
                result[it.first] = it.second
            }
        }
        return result
    }

    override fun toResults(params: CommandLineParms, resultValues: LinkedHashMap<String, Value>): CommandLineParms {
        // paramsList empty and namePrams null means no params pass to jariko
        val paramsList = if (params.namedParams == null && params.parmsList.isEmpty()) {
            params.parmsList
        } else {
            resultValues.values.map { it.asString().value }
        }
        return CommandLineParms(
            parmsList = paramsList,
            namedParams = resultValues
        ) { null }
    }

    @JvmOverloads fun singleCall(parms: List<String>, configuration: Configuration = Configuration()) =
        singleCall(CommandLineParms(parms), configuration = configuration)

    @JvmOverloads fun singleCall(parms: Map<String, Value>, configuration: Configuration = Configuration()) =
        singleCall(CommandLineParms(parms), configuration = configuration)

    @JvmOverloads fun singleCall(parmsProducer: (compilationUnit: CompilationUnit) -> Map<String, Value>, configuration: Configuration = Configuration()) =
        singleCall(CommandLineParms(parmsProducer), configuration = configuration)
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

    override fun findCopy(copyId: CopyId): Copy? {
        // this program finder cannot know how to find a copy
        return null
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor? {
        TODO("Not yet implemented")
    }

    override fun findApi(apiId: ApiId): Api? {
        TODO("Not yet implemented")
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
    if (systemInterface is JavaSystemInterface) {
        systemInterface.rpgSystem.addProgramFinders(programFinders)
        programFinders.forEach {
            systemInterface.getAllLogHandlers().log(RpgProgramFinderLogEntry(it.toString()))
        }
    } else {
        // for compatibility with other system interfaces using singleton instance
        RpgSystem?.SINGLETON_RPG_SYSTEM?.addProgramFinders(programFinders)
        RpgSystem?.SINGLETON_RPG_SYSTEM?.log(systemInterface.getAllLogHandlers())
    }

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
    private val logConfigurationFile by option("-lc", "--log-configuration").file(exists = true, readable = true)
    private val programsSearchDirs by option("-psd", "--programs-search-dirs").split(",")
    private val copySearchDirs by option("-csd", "--copies-search-dirs").split(",")
    private val compiledProgramsDir by option("-cpd", "--compiled-programs-dir").file(exists = true, readable = true)
    private val programArgs by argument().multiple(required = false)
    private val reloadConfigurationFile by option("-rc", "--reload-config").file(exists = true, readable = true)

    override fun run() {
        val programName = if (programArgs.isNotEmpty()) programArgs[0] else null
        if (programName != null) {
            val args = if (programArgs.size > 1) programArgs.subList(1, programArgs.size) else emptyList()
            exec(programName, args)
        } else {
            SimpleShell.repl { programName, programArgs ->
                exec(programName, programArgs)
            }
        }
    }

    private fun exec(programName: String, programArgs: List<String>) {
        val allProgramFinders = defaultProgramFinders +
            ((programsSearchDirs?.map { DirRpgProgramFinder(File(it)) } ?: emptyList())) +
            ((copySearchDirs?.map { DirRpgProgramFinder(File(it)) } ?: emptyList()))
        val configuration = Configuration()
        configuration.options?.compiledProgramsDir = compiledProgramsDir

        // 'Reload' database configurations from properties file passed as cli argument
        reloadConfigurationFile?.let { loadReloadConfig(it, configuration) }
        executePgmWithStringArgs(programName, programArgs, logConfigurationFile, programFinders = allProgramFinders,
            configuration = configuration)
    }
}

private fun loadReloadConfig(reloadConfigurationFile: File, configuration: Configuration) {

    val simpleReloadConfig = SimpleReloadConfig.createInstance(reloadConfigurationFile.inputStream())
    val dbNativeAccessConfig = DBNativeAccessConfig(simpleReloadConfig.connectionConfigs.map {
        ConnectionConfig(
            fileName = it.fileName,
            url = it.url,
            user = it.user,
            password = it.password,
            driver = it.driver,
            impl = it.impl
        )
    })
    val reloadConfig = ReloadConfig(
        nativeAccessConfig = dbNativeAccessConfig,
        metadataProducer = { dbFile -> simpleReloadConfig.getMetadata(dbFile) }
    )
    configuration.reloadConfig = reloadConfig
}

/**
 * This program can be used to either launch an RPG program or the shell.
 */
fun main(args: Array<String>) {
    RunnerCLI.main(args)
}