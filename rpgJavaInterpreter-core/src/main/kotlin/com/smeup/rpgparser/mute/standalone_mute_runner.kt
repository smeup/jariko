package com.smeup.rpgparser.mute

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import java.io.File

data class FilesToRun(val directory: File, val files: List<File>)

private object RunnerCLI : CliktCommand() {
    private val logConfigurationFile by option("-lc", "--log-configuration").file(exists = true, readable = true)
    private val programArgs by argument().file(exists = true)

    override fun run() {
        val filesToRun = findFilesToRun(programArgs)
        val programFinders = listOf<RpgProgramFinder>(DirRpgProgramFinder(filesToRun.directory))
        filesToRun.files.forEach {
            println("Running $it")
            val result =
                executeWithMutes(
                    it.toPath(),
                    true,
                    logConfigurationFile,
                    programFinders = programFinders,
                    output = System.out
                )
            println(result)
        }
    }
}

object StandaloneMuteRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        RunnerCLI.main(args)
    }
}

private fun findFilesToRun(muteSource: File): FilesToRun =
    if (muteSource.isDirectory) {
        FilesToRun(muteSource, muteSource.listFiles { _, name ->
            name.endsWith("rpgle", true)
        }.toList())
    } else {
        FilesToRun(muteSource.parentFile, listOf(muteSource))
    }
