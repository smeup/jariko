package com.smeup.rpgparser

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test
import kotlin.test.assertEquals


class RunnerTest {

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
}