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
    private val dirProgramFinder by argument().file(exists = true)

    override fun run() {
        val filesToRun = findFilesToRun(programArgs)
        val dirProgramFinder = findFilesToRun(dirProgramFinder)
        val programFinders = listOf<RpgProgramFinder>(
            DirRpgProgramFinder(filesToRun.directory),
            DirRpgProgramFinder(dirProgramFinder.directory)
        )
        filesToRun.files.forEach {
            println("Running $it")
            val result =
                executeWithMutes(it.toPath(), true, logConfigurationFile, programFinders = programFinders, output = System.out)
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
