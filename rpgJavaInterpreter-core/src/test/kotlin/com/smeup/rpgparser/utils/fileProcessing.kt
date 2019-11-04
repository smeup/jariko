package com.smeup.rpgparser.utils

import java.io.File
import kotlin.test.assertEquals

fun processFilesInDirectory(path: String, expectedNbOfFiles: Int? = null, processor: (File) -> Unit) {
    var counter = 0
    val dir = testDirectory(path)
    dir.walkTopDown()
            .filter { it.isFile && it.extension == "rpgle" }
            .forEach {
                processor(it)
                counter++
            }
    if (expectedNbOfFiles != null) {
        assertEquals(expectedNbOfFiles, counter)
    }
}

fun testDirectory(path: String): File {
    val dir = File(path)
    return if (dir.exists()) {
        // Running tests with gradle
        dir
    } else {
        // Running tests with IDEA
        File("rpgJavaInterpreter-core/$path")
    }
}