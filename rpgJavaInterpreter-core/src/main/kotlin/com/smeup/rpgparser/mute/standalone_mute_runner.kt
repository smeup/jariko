@file:JvmName("StandaloneMuteRunner")

package com.smeup.rpgparser.mute

import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgProgramFinder
import java.io.File

fun main(args: Array<String>) {
    require(args.size > 0) {
        "You must specify a path to a mute program as first parameter"
    }
    val muteSource = File(args[0])
    require(muteSource.exists()) {
        "${muteSource.canonicalPath} does not exist"
    }
    println("Running ${muteSource.canonicalPath}")
    val programFinders = listOf<RpgProgramFinder>(DirRpgProgramFinder(muteSource.parentFile))
    val result = executeWithMutes(muteSource.toPath(), true, null, programFinders = programFinders)
    println(result)
}