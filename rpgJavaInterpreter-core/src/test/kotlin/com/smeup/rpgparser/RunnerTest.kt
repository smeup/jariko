package com.smeup.rpgparser

import com.smeup.rpgparser.execution.CommandLineProgram
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.AssignmentsLogHandler
import com.smeup.rpgparser.interpreter.EvalLogHandler
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.utils.StringOutputStream
import org.junit.Test
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class RunnerTest {
    @Test
    fun programsReturnValues() {
        val systemInterface = JavaSystemInterface()
        val program = getProgram("CALCFIB", systemInterface)

        val parms = program.singleCall(listOf("7")) ?: fail("Result values should not be null")

        val parmList = parms.parmsList
        assertEquals(1, parmList.size)
        assertEquals("13", parmList[0].trim())
    }

    @Test
    fun commandLineProgramsRetainsStatusOnSetOnRT() {
        val systemInterface = JavaSystemInterface()

        val program = getProgram("COUNTRT", systemInterface)

        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Counter: 1"))
        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Counter: 2"))
        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Counter: 3"))
    }

    @Test
    fun commandLineProgramsDoesNotRetainStatusOnSetOnLR() {
        val systemInterface = JavaSystemInterface()
        val program = getProgram("COUNTLR", systemInterface)

        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Counter: 1"))
        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Counter: 1"))
        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Counter: 1"))
    }

    @Test
    fun exitingWithLRCleansStatus() {
        val systemInterface = JavaSystemInterface()
        val program = getProgram("COUNTLRRT", systemInterface)

        assertEquals(listOf("Counter: 1"), systemInterface.outputOfSingleCall(program, listOf("RT")))
        assertEquals(listOf("Counter: 2"), systemInterface.outputOfSingleCall(program, listOf("LR")))
        assertEquals(listOf("Counter: 1"), systemInterface.outputOfSingleCall(program, listOf("RT")))
        assertEquals(listOf("Counter: 2"), systemInterface.outputOfSingleCall(program, listOf("RT")))
        assertEquals(listOf("Counter: 3"), systemInterface.outputOfSingleCall(program, listOf("RT")))
    }

    @Test
    fun commandLineProgramsCanReadSourcesFromString() {
        val systemInterface = JavaSystemInterface()

        val source =
            """
|     C     'Hello World' DSPLY
|     C                   SETON                                          LR
            """.trimMargin()

        val program = getProgram(source, systemInterface)

        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Hello World"))
    }

    @Test
    fun commandLineProgramsCanReadSourcesFromUTF8String() {
        val systemInterface = JavaSystemInterface()

        val source =
            """
|     D Msg§            S             12
|     C                   Eval      Msg§ = 'Hello World!'
|     C                   dsply                   Msg§
|     C                   SETON                                          LR
            """.trimMargin()

        val program = getProgram(source, systemInterface)

        assertEquals(systemInterface.outputOfSingleCall(program), listOf("Hello World!"))
    }

    @Test
    fun commandLineProgramsCanRunMutes() {
        val systemInterface = JavaSystemInterface()

        val source =
            """
|     D Msg             S             12
|    MU* VAL1(Msg) VAL2('This should fail') COMP(EQ)
|    MU* VAL1(Msg) VAL2('') COMP(EQ)
|     C     'Hello World' DSPLY
|     C                   SETON                                          LR
            """.trimMargin()

        val program = getProgram(source, systemInterface)

        program.singleCall(listOf())
        assertEquals(systemInterface.consoleOutput, listOf("Hello World"))
        val executedAnnotations = systemInterface.getExecutedAnnotation()
        assertEquals(0, executedAnnotations.size)
        assertEquals(0, executedAnnotations.values.count { it.failed() })
        assertEquals(0, executedAnnotations.values.count { it.succeeded() })
    }

    @Test
    fun commandLineProgramCanBeInstrumentedWithAssignmentsLogHandler() {
        val systemInterface = JavaSystemInterface()
        val source =
            """
|     D Msg§            S             12
|     C                   Eval      Msg§ = 'Hello World!'
|     C                   dsply                   Msg§
|     C                   SETON                                          LR
            """.trimMargin()
        val program = getProgram(source, systemInterface)
        val logOutputStream = StringOutputStream()
        val printStream = PrintStream(logOutputStream)
        val assignmentsLogHandler = AssignmentsLogHandler(printStream)
        val evalLogHandler = EvalLogHandler(printStream)

        systemInterface.addExtraLogHandlers(listOf(evalLogHandler, assignmentsLogHandler))

        program.singleCall(listOf())

        assertTrue(logOutputStream.written)

        println(logOutputStream)
    }

    @Test
    fun executeHELLO() {
        // Classic Hello World
        val systemInterface = JavaSystemInterface()
        val program = getProgram("HELLO", systemInterface)
        val logOutputStream = StringOutputStream()
        val printStream = PrintStream(logOutputStream)
        val assignmentsLogHandler = AssignmentsLogHandler(printStream)
        val evalLogHandler = EvalLogHandler(printStream)

        systemInterface.addExtraLogHandlers(listOf(evalLogHandler, assignmentsLogHandler))

        program.singleCall(listOf())

        assertTrue(logOutputStream.written)

        println(logOutputStream)
    }

    @Test
    fun executeHELLO2() {
        // Hello World multilanguage
        val systemInterface = JavaSystemInterface()
        val program = getProgram("HELLO2", systemInterface)
        val logOutputStream = StringOutputStream()
        val printStream = PrintStream(logOutputStream)
        val assignmentsLogHandler = AssignmentsLogHandler(printStream)
        val evalLogHandler = EvalLogHandler(printStream)

        systemInterface.addExtraLogHandlers(listOf(evalLogHandler, assignmentsLogHandler))

        program.singleCall(listOf())

        assertTrue(logOutputStream.written)

        println(logOutputStream)
    }

    fun JavaSystemInterface.outputOfSingleCall(
        program: CommandLineProgram,
        parameters: List<String> = emptyList(),
    ): List<String> {
        clearConsole()
        program.singleCall(parameters)
        return consoleOutput
    }
}
