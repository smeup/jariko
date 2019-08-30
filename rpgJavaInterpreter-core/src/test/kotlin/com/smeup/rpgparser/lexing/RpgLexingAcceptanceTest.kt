package com.smeup.rpgparser.lexing

import com.smeup.rpgparser.AcceptanceTest
import com.smeup.rpgparser.assertCanBeLexed
import com.smeup.rpgparser.utils.processFilesInDirectory
import org.junit.Test
import org.junit.experimental.categories.Category
import java.io.File
import kotlin.test.assertEquals

class RpgLexingAcceptanceTest {

    @Test
    @Category(AcceptanceTest::class)
    fun lexAllDataExamples() {
        var failures = 0
        processFilesInDirectory(File("src/test/resources/data"), 13) { rpgFile ->
            try {
                val tokens = assertCanBeLexed(rpgFile)
            } catch (e : AssertionError) {
                System.err.println("Failed to lex ${rpgFile.absolutePath}: ${e.message}")
                failures++
            }
            assertEquals(0, failures, "Cannot lex some files")
        }
    }
}