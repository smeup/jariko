package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.AcceptanceTest
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.utils.processFilesInDirectory
import org.junit.Ignore
import org.junit.Test
import org.junit.experimental.categories.Category
import java.io.File
import kotlin.test.assertEquals

class RpgParsingAcceptanceTest {

    @Ignore // working on grammar
    @Test
    @Category(AcceptanceTest::class)
    fun parseAllDataExamples() {
        var failures = 0
        processFilesInDirectory(File("src/test/resources/data"), 13) { rpgFile ->
            try {
                val parseTree = assertCanBeParsed(rpgFile)
            } catch (e: AssertionError) {
                System.err.println("Failed to parse ${rpgFile.absolutePath}: ${e.message}")
                failures++
            }
            assertEquals(0, failures, "Cannot parse some files")
        }
    }
}