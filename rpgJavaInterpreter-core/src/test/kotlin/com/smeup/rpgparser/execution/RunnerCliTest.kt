package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.rgpinterop.RpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail
import com.smeup.rpgparser.execution.main as runnerMain

class RunnerCliTest {

    private fun assertRunnerExit(args: Array<String>) {
        // we cannot mock exitProcess, as it is inlined
        // so we mock System.exit, but by doing so we prevent the JVM from terminating,
        // which cause an exception in exitProcess

        mockkStatic("com.smeup.rpgparser.execution.RunnerKt")
        mockkStatic(System::class)
        mockkObject(RunnerCLI)

        var shellStarted = 0
        var programStarted = 0
        var exit = 0

        every { startShell() } answers { shellStarted++ }
        every { RunnerCLI.run() } answers { programStarted++ }
        every { System.exit(1) } answers { exit++ }

        try {
            runnerMain(args)
            fail("exception expected")
        } catch (e: RuntimeException) {
            assertEquals("System.exit returned normally, while it was supposed to halt JVM.", e.message)
        }
        assertEquals(0, shellStarted)
        assertEquals(0, programStarted)
        assertEquals(1, exit)
    }

    @Test
    fun withNoArgsReplIsStarted() {
        // we cannot mock exitProcess, as it is inlined
        // so we mock System.exit, but by doing so we prevent the JVM from terminating,
        // which cause an exception in exitProcess

        mockkStatic("com.smeup.rpgparser.execution.RunnerKt")
        mockkStatic(System::class)
        mockkObject(RunnerCLI)

        var shellStarted = 0
        var programStarted = 0
        var exit = 0

        every { startShell() } answers { shellStarted++ }
        every { RunnerCLI.run() } answers { programStarted++ }
        every { System.exit(1) } answers { exit++ }

        runnerMain(arrayOf())

        assertEquals(1, shellStarted)
        assertEquals(0, programStarted)
        assertEquals(0, exit)
    }

    @Test
    fun withUnexistingLogFile() {
        assertRunnerExit(arrayOf("-lc", "unexisting.logging"))
    }

    @Test
    fun withUnexistingOption() {
        assertRunnerExit(arrayOf("-foo"))
    }

    @Test
    fun withProgramNameAndNoProgramArgs() {
        // we cannot mock exitProcess, as it is inlined
        // so we mock System.exit, but by doing so we prevent the JVM from terminating,
        // which cause an exception in exitProcess

        mockkStatic("com.smeup.rpgparser.execution.RunnerKt")
        mockkStatic(System::class)
        mockkObject(RunnerCLI)

        var shellStarted = 0
        var programStarted = 0
        var exit = 0

        RpgSystem.addProgramFinder(object : RpgProgramFinder {
            override fun findRpgProgram(nameOrSource: String): RpgProgram? {
                return RpgProgram(CompilationUnit.empty())
            }
        })

        every { startShell() } answers { shellStarted++ }
        every { RunnerCLI.run() } answers {
            assertEquals("myProgram", RunnerCLI.programName)
            assertEquals(listOf(), RunnerCLI.programArgs)
            assertEquals(null, RunnerCLI.logConfigurationFile)
            programStarted++ }
        every { System.exit(1) } answers { exit++ }

        runnerMain(arrayOf("myProgram"))

        assertEquals(0, shellStarted)
        assertEquals(1, programStarted)
        assertEquals(0, exit)
    }

    @Test
    fun withProgramNameAndWithProgramArgs() {
        // we cannot mock exitProcess, as it is inlined
        // so we mock System.exit, but by doing so we prevent the JVM from terminating,
        // which cause an exception in exitProcess

        mockkStatic("com.smeup.rpgparser.execution.RunnerKt")
        mockkStatic(System::class)
        mockkObject(RunnerCLI)

        var shellStarted = 0
        var programStarted = 0
        var exit = 0

        RpgSystem.addProgramFinder(object : RpgProgramFinder {
            override fun findRpgProgram(nameOrSource: String): RpgProgram? {
                return RpgProgram(CompilationUnit.empty())
            }
        })

        every { startShell() } answers { shellStarted++ }
        every { RunnerCLI.run() } answers {
            assertEquals("myProgram", RunnerCLI.programName)
            assertEquals(listOf("p1", "p2"), RunnerCLI.programArgs)
            assertEquals(null, RunnerCLI.logConfigurationFile)
            programStarted++ }
        every { System.exit(1) } answers { exit++ }

        runnerMain(arrayOf("myProgram", "p1", "p2"))

        assertEquals(0, shellStarted)
        assertEquals(1, programStarted)
        assertEquals(0, exit)
    }
}