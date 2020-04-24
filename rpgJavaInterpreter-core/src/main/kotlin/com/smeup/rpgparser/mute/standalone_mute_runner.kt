@file:JvmName("StandaloneMuteRunner")

package com.smeup.rpgparser.mute

import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import java.io.File

data class FilesToRun(val directory: File, val files: List<File>)

fun main(args: Array<String>) {
    require(args.size > 0) {
        "You must specify a path to a mute program as first parameter"
    }
    val muteSource = File(args[0])
    require(muteSource.exists()) {
        "${muteSource.canonicalPath} does not exist"
    }

    val filesToRun = findFilesToRun(muteSource)
    val programFinders = listOf<RpgProgramFinder>(DirRpgProgramFinder(filesToRun.directory))

    filesToRun.files.forEach {
        println("Running $it")
        val result =
            executeWithMutes(it.toPath(), true, null, programFinders = programFinders, output = System.out)
        println(result)
    }
}

private fun findFilesToRun(muteSource: File): FilesToRun =
    if (muteSource.isDirectory) {
        FilesToRun(muteSource, muteSource.listFiles { _, name ->
            name.endsWith(".rpgle", true)
        }.toList())
    } else {
        FilesToRun(muteSource.parentFile, listOf(muteSource))
    }
