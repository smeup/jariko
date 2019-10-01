package com.smeup.rpgparser.utils

import java.io.File
import kotlin.test.assertEquals

fun processFilesInDirectory(dir: File, expectedNbOfFiles: Int? = null, processor: (File) -> Unit) {
    var counter = 0
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