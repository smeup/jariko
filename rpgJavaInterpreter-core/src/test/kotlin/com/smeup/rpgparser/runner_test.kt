package com.smeup.rpgparser

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test
import kotlin.test.assertEquals


class RunnerTest {

    @Test
    fun commandLineProgramsRetainsStatusOnSetOnRT() {
        val program = getProgram("COUNTRT", JavaSystemInterface)

        JavaSystemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(JavaSystemInterface.consoleOutput, listOf("Counter: 1"))

        JavaSystemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(JavaSystemInterface.consoleOutput, listOf("Counter: 2"))

        JavaSystemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(JavaSystemInterface.consoleOutput, listOf("Counter: 3"))
    }

    @Test
    fun commandLineProgramsDoesNotRetainStatusOnSetOnLR() {
        val program = getProgram("COUNTLR", JavaSystemInterface)

        JavaSystemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(JavaSystemInterface.consoleOutput, listOf("Counter: 1"))

        JavaSystemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(JavaSystemInterface.consoleOutput, listOf("Counter: 1"))

        JavaSystemInterface.consoleOutput.clear()
        program.singleCall(listOf())
        assertEquals(JavaSystemInterface.consoleOutput, listOf("Counter: 1"))
    }
}