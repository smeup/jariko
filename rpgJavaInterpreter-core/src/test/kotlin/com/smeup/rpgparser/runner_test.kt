package com.smeup.rpgparser

import com.smeup.rpgparser.interpreter.AssignmentsLogHandler
import com.smeup.rpgparser.interpreter.EvalLogHandler
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.utils.StringOutputStream
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail
import java.io.PrintStream
import kotlin.test.assertTrue


class RunnerTest {

    @Test
    fun programsReturnValues() {
        val systemInterface = JavaSystemInterface()
        val program = getProgram("CALCFIB", systemInterface)

        val parms = program.singleCall(listOf("7")) ?: fail("Result values should not be null")

        val parmList = parms.parmsList ?: fail("Result value list should not be null")
        assertEquals(1, parmList.size)
        assertEquals("13", parmList[0].trim())
    }

    @Test
    fun commandLineProgramsRetainsStatusOnSetOnRT() {
        val systemInterface = JavaSystemInterface()

        val program = getProgram("COUNTRT", systemInterface)

        systemInterface.clearConsole()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 1"))

        systemInterface.clearConsole()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 2"))

        systemInterface.clearConsole()

        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 3"))
    }

    @Test
    fun commandLineProgramsDoesNotRetainStatusOnSetOnLR() {
        val systemInterface = JavaSystemInterface()
        val program = getProgram("COUNTLR", systemInterface)

        systemInterface.clearConsole()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 1"))

        systemInterface.clearConsole()
        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Counter: 1"))

        systemInterface.clearConsole()
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

    @Test
    fun commandLineProgramCanBeInstrumentedWithAssignmentsLogHandler() {
        val systemInterface = JavaSystemInterface()
        val source = """
|     D Msg§            S             12
|     C                   Eval      Msg§ = 'Hello World!'
|     C                   dsply                   Msg§
|     C                   SETON                                          LR
        """.trimMargin()
        val program = getProgram(source,  systemInterface)
        val logOutputStream = StringOutputStream()
        val printStream = PrintStream(logOutputStream)
        val assignmentsLogHandler = AssignmentsLogHandler(printStream)
        val evalLogHandler = EvalLogHandler(printStream)

        program.addLogHandler(evalLogHandler)
        program.addLogHandler(assignmentsLogHandler)

        program.singleCall(listOf())

        assertTrue(logOutputStream.written)

        println(logOutputStream)
    }
}