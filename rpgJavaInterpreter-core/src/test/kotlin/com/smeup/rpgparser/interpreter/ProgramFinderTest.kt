package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.adaptForTestCase
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import com.smeup.rpgparser.utils.compile
import org.junit.Test
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProgramFinderTest : AbstractTest() {
    @Test
    fun playWithBinaryAndSources() {
        // TEST FLOW:
        // 01. set the 'program finder' to search for program into 'system temporary' directory (filled
        // with needed resources)
        // 02. call program with no suffix (es. ECHOPGM) -> OK, it uses 'ECHOPGM.rpgle'
        // 03. call program with 'rpgle' suffix (es. ECHOPGM.rpgle) -> OK, it uses 'ECHOPGM.rpgle'
        // 04. call program with 'bin' suffix (es. ECHOPGM.bin) -> OK only if NOT found 'ECHOPGM.bin'
        // 05. compile (generate .bin) into 'system temporary' directory
        // 06. call compiled program (es. ECHOPGM.bin) -> OK, now it uses 'ECHOPGM.bin'
        // 07. remove 'rpgle' source from 'system temporary' directory
        // 08. call program with 'rpgle' suffix (es. ECHOPGM.rpgle) -> OK only if NOT found 'ECHOPGM.rpgle'
        // 09. call program with no suffix (es. ECHOPGM) -> OK, it uses 'ECHOPGM.bin' (however use any of found)
        // 10. delete compiled program (es. ECHOPGM.bin) and try to call it -> OK only if not found
        // 11. call program with no suffix (es. ECHOPGM) -> OK only if not found

        val resourcesDir = File(System.getProperty("java.io.tmpdir"))
        val sourceFile = File("src/test/resources/DUMMY_FOR_TEST.rpgle")
        val sourceDestFile = File("${System.getProperty("java.io.tmpdir")}${File.separator}ECHOPGM.rpgle")
        if (sourceDestFile.exists()) {
            sourceDestFile.delete()
        }
        sourceFile.copyTo(sourceDestFile, true)
        val compiledProgramFile = File("${System.getProperty("java.io.tmpdir")}${File.separator}ECHOPGM.bin")
        if (compiledProgramFile.exists()) {
            compiledProgramFile.delete()
        }

        // 01.
        val programFinders: List<RpgProgramFinder> = listOf(DirRpgProgramFinder(resourcesDir))

        // To simulate real use cases it is necessary create a new instance of system
        // interface for each call
        // 02.
        var jariko = getProgram("ECHOPGM", JavaSystemInterface(), programFinders)
        var results = jariko.singleCall(listOf("Hi, you called ECHOPGM"), Configuration().adaptForTestCase(this))
        assertEquals("Hi, you called ECHOPGM", results!!.parmsList[0].trim())

        // 03.
        jariko = getProgram("ECHOPGM.rpgle", JavaSystemInterface(), programFinders)
        results = jariko.singleCall(listOf("Hi, you called ECHOPGM.rpgle"), Configuration().adaptForTestCase(this))
        assertEquals("Hi, you called ECHOPGM.rpgle", results!!.parmsList[0].trim())

        // 04.
        assertFalse(compiledProgramFile.exists())
        jariko = getProgram("ECHOPGM.bin", JavaSystemInterface(), programFinders)
        kotlin
            .runCatching {
                results = jariko.singleCall(listOf("Hi, you called ECHOPGM.bin"), Configuration().adaptForTestCase(this))
            }.onFailure {
                assertEquals("Program ECHOPGM.bin not found", it.message)
            }.onSuccess {
                assertFalse("Program ECHOPGM.bin must not exists yet") { true }
            }

        // 05.
        compile(sourceDestFile, resourcesDir)
        assertTrue(compiledProgramFile.exists())

        // 06.
        jariko = getProgram("ECHOPGM.bin", JavaSystemInterface(), programFinders)
        results = jariko.singleCall(listOf("Hi, you called ECHOPGM.bin"), Configuration().adaptForTestCase(this))
        assertEquals("Hi, you called ECHOPGM.bin", results!!.parmsList[0].trim())

        // 07.
        sourceDestFile.delete()
        assertFalse(sourceDestFile.exists())

        // 08.
        jariko = getProgram("ECHOPGM.rpgle", JavaSystemInterface(), programFinders)
        kotlin
            .runCatching {
                results = jariko.singleCall(listOf("Hi, you called ECHOPGM.rpgle"), Configuration().adaptForTestCase(this))
            }.onFailure {
                assertEquals("Program ECHOPGM.rpgle not found", it.message)
            }.onSuccess {
                assertFalse("Program ECHOPGM.rpgle must not exist here: ${resourcesDir.absolutePath}") { true }
            }

        // 09.
        jariko = getProgram("ECHOPGM", JavaSystemInterface(), programFinders)
        results = jariko.singleCall(listOf("Hi, you called ECHOPGM"), Configuration().adaptForTestCase(this))
        assertEquals("Hi, you called ECHOPGM", results!!.parmsList[0].trim())

        // 10.
        compiledProgramFile.delete()
        assertFalse(compiledProgramFile.exists())
        jariko = getProgram("ECHOPGM.bin", JavaSystemInterface(), programFinders)
        kotlin
            .runCatching {
                results = jariko.singleCall(listOf("Hi, you called ECHOPGM.bin"), Configuration().adaptForTestCase(this))
            }.onFailure {
                assertEquals("Program ECHOPGM.bin not found", it.message)
            }.onSuccess {
                assertFalse("Program ECHOPGM.bin must not exist anymore here: ${resourcesDir.absolutePath}") { true }
            }

        // 11.
        jariko = getProgram("ECHOPGM", JavaSystemInterface(), programFinders)
        kotlin
            .runCatching {
                results = jariko.singleCall(listOf("Hi, you called ECHOPGM"), Configuration().adaptForTestCase(this))
            }.onFailure {
                assertEquals("Program ECHOPGM not found", it.message)
            }.onSuccess {
                assertFalse("Program ECHOPGM must not exist anymore here: ${resourcesDir.absolutePath}") { true }
            }
    }

    @Test
    fun findHelloSqlrpgle() {
        val path = Paths.get({}.javaClass.getResource("/HELLO3.sqlrpgle")?.toURI() ?: error("HELLO3.sqlrpgle not found"))
        lateinit var foundProgramPath: Path

        val finder =
            DirRpgProgramFinder(path.parent.toFile()).apply {
                foundProgram { programPath -> foundProgramPath = programPath }
            }

        // If we have both HELLO2.rpgle and HELLO2.sqlrpgle the precedence is HELLO2.rpgle
        getProgram(nameOrSource = "HELLO2", programFinders = listOf(finder)).singleCall(
            emptyList(),
        )
        assertEquals("HELLO2.rpgle", foundProgramPath.fileName.toString())

        // In this case we have only
        getProgram(nameOrSource = "HELLO3.sqlrpgle", programFinders = listOf(finder)).singleCall(
            emptyList(),
        )
        assertEquals("HELLO3.sqlrpgle", foundProgramPath.fileName.toString())
    }
}
