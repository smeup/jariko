package com.smeup.rpgparser.execution

import com.smeup.rpgparser.utils.StringOutputStream
import org.apache.commons.io.input.ReaderInputStream
import org.junit.Test
import java.io.PrintStream
import java.io.StringReader
import java.nio.charset.Charset
import kotlin.test.assertTrue
import com.smeup.rpgparser.execution.main as runnerMain

class RunnerCliTest {

    @Test
    fun withNoArgsReplIsStarted() {
        SimpleShell.inputStream = ReaderInputStream(StringReader("signoff"), Charset.defaultCharset())
        val out = StringOutputStream()
        System.setOut(PrintStream(out))

        runnerMain(arrayOf())

        assertTrue(out.written)
        assertTrue(out.toString().contains("Goodbye"))
    }

//    @Test
//    fun withUnexistingLogFile() {
//        runnerMain(arrayOf("-lc", "unexisting.logging"))
//    }
//
//    @Test
//    fun withUnexistingOption() {
//        assertRunnerExit(arrayOf("-foo"))
//    }
//
//    @Test
//    fun withProgramNameAndNoProgramArgs() {
//        // we cannot mock exitProcess, as it is inlined
//        // so we mock System.exit, but by doing so we prevent the JVM from terminating,
//        // which cause an exception in exitProcess
//
//        mockkStatic("com.smeup.rpgparser.execution.RunnerKt")
//        mockkStatic(System::class)
//        mockkObject(RunnerCLI)
//
//        var shellStarted = 0
//        var programStarted = 0
//        var exit = 0
//
//        RpgSystem.addProgramFinder(object : RpgProgramFinder {
//            override fun findRpgProgram(nameOrSource: String): RpgProgram? {
//                return RpgProgram(CompilationUnit.empty())
//            }
//        })
//
//        every { startShell() } answers { shellStarted++ }
//        every { RunnerCLI.run() } answers {
//            assertEquals("myProgram", RunnerCLI.programName)
//            assertEquals(listOf(), RunnerCLI.programArgs)
//            assertEquals(null, RunnerCLI.logConfigurationFile)
//            programStarted++ }
//        every { System.exit(1) } answers { exit++ }
//
//        runnerMain(arrayOf("myProgram"))
//
//        assertEquals(0, shellStarted)
//        assertEquals(1, programStarted)
//        assertEquals(0, exit)
//    }
//
//    @Test
//    fun withProgramNameAndWithProgramArgs() {
//        // we cannot mock exitProcess, as it is inlined
//        // so we mock System.exit, but by doing so we prevent the JVM from terminating,
//        // which cause an exception in exitProcess
//
//        mockkStatic("com.smeup.rpgparser.execution.RunnerKt")
//        mockkStatic(System::class)
//        mockkObject(RunnerCLI)
//
//        var shellStarted = 0
//        var programStarted = 0
//        var exit = 0
//
//        RpgSystem.addProgramFinder(object : RpgProgramFinder {
//            override fun findRpgProgram(nameOrSource: String): RpgProgram? {
//                return RpgProgram(CompilationUnit.empty())
//            }
//        })
//
//        every { startShell() } answers { shellStarted++ }
//        every { RunnerCLI.run() } answers {
//            assertEquals("myProgram", RunnerCLI.programName)
//            assertEquals(listOf("p1", "p2"), RunnerCLI.programArgs)
//            assertEquals(null, RunnerCLI.logConfigurationFile)
//            programStarted++ }
//        every { System.exit(1) } answers { exit++ }
//
//        runnerMain(arrayOf("myProgram", "p1", "p2"))
//
//        assertEquals(0, shellStarted)
//        assertEquals(1, programStarted)
//        assertEquals(0, exit)
//    }
}