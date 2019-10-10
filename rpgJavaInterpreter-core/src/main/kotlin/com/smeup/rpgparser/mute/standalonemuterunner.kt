package com.smeup.rpgparser.mute

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
    val result = executeWithMutes(muteSource.toPath(), false, null)
    println(result)
}