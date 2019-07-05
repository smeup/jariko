package com.smeup.rpgparser

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail


class RunnerTest {

    @Test
    fun programsReturnValues() {
        val systemInterface = JavaSystemInterface()
        val program = getProgram("CALCFIB", systemInterface)

        val parms = program.singleCall(listOf("7")) ?: fail("Result values should not be null")

        val parmList = parms.parmsList ?: fail("Result value list should not be null")
        assertEquals(1, parmList.size)
        assertEquals("13", parmList[0])
    }

    @Test
    fun commandLineProgramsRetainsStatusOnSetOnRT() {
        val systemInterface = JavaSystemInterface()

        val program = getProgram("COUNTRT", systemInterface)

        systemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 1"))

        systemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 2"))

        systemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 3"))
    }

    @Test
    fun commandLineProgramsDoesNotRetainStatusOnSetOnLR() {
        val systemInterface = JavaSystemInterface()
        val program = getProgram("COUNTLR", systemInterface)

        systemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 1"))

        systemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 1"))

        systemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 1"))
    }

    @Test
    fun commandLineProgramsCanReadSourcesFromString() {
        val systemInterface = JavaSystemInterface()

        val source = """
|     C     'Hello World' DSPLY
|     C                   SETON                                          LR
        """.trimMargin()

        val program = getProgram(source, systemInterface)

        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Hello World"))
    }

    @Test
    fun commandLineProgramsCanReadSourcesFromUTF8String() {
        val systemInterface = JavaSystemInterface()

        val source = """
|     D Msg§            S             12
|     C                   Eval      Msg§ = 'Hello World!'
|     C                   dsply                   Msg§
|     C                   SETON                                          LR
        """.trimMargin()

        val program = getProgram(source, systemInterface)

        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Hello World!"))
    }
}