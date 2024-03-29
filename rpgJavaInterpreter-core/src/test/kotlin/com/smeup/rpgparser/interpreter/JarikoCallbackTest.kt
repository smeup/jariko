/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.parsing.facade.SourceReferenceType
import org.junit.Assert
import java.io.StringReader
import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test suite to test Jariko callback features
 * */
class JarikoCallbackTest : AbstractTest() {

    @Test
    fun onEnterStatement() {
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        var enteredTimes = 0
        executePgm(systemInterface = systemInterface, programName = "HELLO", configuration = Configuration().apply {
            jarikoCallback.onEnterStatement = { _: Int, _: SourceReference -> enteredTimes++ }
            options = Options().apply { debuggingInformation = true }
        })
        Assert.assertEquals(3, enteredTimes)
    }

    @Test
    fun onEnterStatementWithCall() {
        val expectedEnteredTimes = mapOf("CAL01" to 5, "CAL02" to 4)
        val actualEnteredTimes = mutableMapOf("CAL01" to 0, "CAL02" to 0)
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        executePgm(systemInterface = systemInterface, programName = "CAL01", configuration = Configuration().apply {
            jarikoCallback.onEnterStatement = { _: Int, sourceReference: SourceReference ->
                println(sourceReference)
                actualEnteredTimes[sourceReference.sourceId] = actualEnteredTimes[sourceReference.sourceId]!! + 1
            }
            options = Options().apply { debuggingInformation = true }
        })
        Assert.assertEquals(expectedEnteredTimes, actualEnteredTimes)
    }

    @Test
    fun programLifeCycle() {
        val enteredTimes = mutableMapOf("CAL04" to 0, "CAL02" to 0)
        val exitedTimes = mutableMapOf("CAL04" to 0, "CAL02" to 0)
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        executePgm(systemInterface = systemInterface, programName = "CAL04", configuration = Configuration().apply {
            jarikoCallback.onEnterPgm = {
                    programName: String, symbolTable: ISymbolTable ->
                println("onEnterPgm - programName: $programName, symbolTable: $symbolTable")
                enteredTimes[programName] = enteredTimes[programName]!! + 1
            }
            jarikoCallback.onExitPgm = {
                    programName: String, symbolTable: ISymbolTable, _: Throwable? ->
                println("onExitPgm - programName: $programName, symbolTable: $symbolTable")
                exitedTimes[programName] = exitedTimes[programName]!! + 1
            }
        })
        Assert.assertEquals(1, enteredTimes["CAL04"])
        Assert.assertEquals(1, exitedTimes["CAL04"])
        Assert.assertEquals(5, enteredTimes["CAL02"])
        Assert.assertEquals(5, exitedTimes["CAL02"])
    }

    @Test
    fun onEnterStatementWithCopyBlock() {

        lateinit var copyDefinitions: Map<CopyId, String>
        lateinit var pgm: String
        lateinit var postProcessed: String
        val configuration = Configuration().apply {
            options = Options().apply {
                debuggingInformation = true
                dumpSourceOnExecutionError = true
            }
            jarikoCallback = JarikoCallback().apply {
                afterAstCreation = { ast -> postProcessed = ast.source!! }
                onEnterStatement = { lineNumber: Int, sourceReference: SourceReference ->
                    if (sourceReference.sourceReferenceType == SourceReferenceType.Copy) {
                        println("Copy - copyId: ${sourceReference.sourceId}, relativeLineNumber: ${sourceReference.relativeLine}, lineNumber: $lineNumber")
                    } else {
                        println("Program - relativeLineNumber: ${sourceReference.relativeLine}, lineNumber: $lineNumber")
                    }
                    val src = when (sourceReference.sourceReferenceType) {
                        SourceReferenceType.Copy -> copyDefinitions[CopyId(member = sourceReference.sourceId)]
                        SourceReferenceType.Program -> pgm
                    }
                    require(src != null)
                    val relativeStatement = src.lines()[sourceReference.relativeLine - 1]
                    println("relativeStatement: $relativeStatement")
                    val absoluteStatement = postProcessed.lines()[lineNumber - 1]
                    println("absoluteStatement: $absoluteStatement")
                    Assert.assertEquals(absoluteStatement, relativeStatement)
                }
            }
        }
        executeInlinePgmContainingCopy(configuration = configuration, consumeSources = { myPgm, myCopyDefinitions ->
            pgm = myPgm
            copyDefinitions = myCopyDefinitions
        })
    }

    @Test
    fun copyLifeCycleWithInlinePgm() {
        val copiesWillEnter = mutableSetOf<CopyId>()
        val copiesDidEnter = mutableSetOf<CopyId>()
        val copiesWillExit = mutableSetOf<CopyId>()
        val copiesDidExit = mutableSetOf<CopyId>()
        val enterSequence = mutableListOf<String>()
        val exitSequence = mutableListOf<String>()
        val configuration = Configuration().apply {
            options = Options().apply { debuggingInformation = true }
            jarikoCallback = JarikoCallback().apply {
                onEnterCopy = { copyId ->
                    println("entering $copyId")
                    enterSequence.add(copyId.member)
                    copiesDidEnter.add(copyId)
                }
                onExitCopy = { copyId ->
                    println("exiting $copyId")
                    exitSequence.add(copyId.member)
                    copiesDidExit.add(copyId)
                }
            }
        }
        val expectedEnterSequence = listOf("COPY1", "COPY2", "COPY21", "COPY22")
        val expectedExitSequence = listOf("COPY1", "COPY21", "COPY22", "COPY2")
        executeInlinePgmContainingCopy(configuration, consumeSources = { _, copyDefinitions ->
            copyDefinitions.keys.forEach { copyId ->
                copiesWillEnter.add(copyId)
                copiesWillExit.add(copyId)
            }
        })
        Assert.assertEquals(copiesWillEnter, copiesDidEnter)
        Assert.assertEquals(copiesWillExit, copiesDidExit)
        Assert.assertEquals(expectedEnterSequence, enterSequence)
        Assert.assertEquals(expectedExitSequence, exitSequence)
    }

    @Test
    fun copyLifeCycle() {
        var entered = 0
        var exited = 0
        val configuration = Configuration().apply {
            options = Options().apply { debuggingInformation = true }
            jarikoCallback = JarikoCallback().apply {
                onEnterCopy = {
                    println("Entering $it")
                    entered++
                }
                onExitCopy = {
                    println("Exiting $it")
                    exited++
                }
            }
        }
        executePgm(programName = "TSTCPY01", configuration = configuration)
        Assert.assertEquals(2, entered)
        Assert.assertEquals(2, exited)
    }

    private fun executeInlinePgmContainingCopy(
        configuration: Configuration,
        consumeSources: (pgm: String, copyDefinitions: Map<CopyId, String>) -> Unit
    ) {
        val pgm = """
2    D Msg             S             30      
3    C                   EVAL      Msg = 'Include COPY1'
4    C      Msg          DSPLY
5    I/COPY COPY1
6    C                   EVAL      Msg = 'Include COPY2'
7    C      Msg          DSPLY
8    I/COPY COPY2     
9    C                   EVAL      Msg = 'Exit from COPY2'
10   C      Msg          DSPLY
        """.trimEnd()
        val copy1 = """
2    D Msg1            S             30
3    C                   EVAL      Msg1 = 'I AM COPY1'
4    C      Msg1         DSPLY
        """
        val copy2 = """
2    D Msg2            S             30
3    C                   EVAL      Msg2 = 'I AM COPY2'
4    C      Msg2         DSPLY
5    C                   EVAL      Msg2 = 'Include COPY21'
6    C      Msg2         DSPLY
7     /COPY COPY21     
8    C      Msg2         EVAL      Msg2 = 'After COPY21'
9    C      Msg2         DSPLY
10   C                   EVAL      Msg2 = 'Include COPY22'
11   C      Msg2         DSPLY
12    /COPY COPY22     
        """
        val copy21 = """
2    D Msg21           S             30
3    C                   EVAL      Msg21 = 'I AM COPY21'
4    C      Msg21        DSPLY
        """
        val copy22 = """
2    D Msg22           S             30
3    C                   EVAL      Msg21 = 'I AM COPY22'
4    C      Msg22        DSPLY
        """
        val copyDefinitions = mapOf(
            CopyId(member = "COPY1") to copy1,
            CopyId(member = "COPY2") to copy2,
            CopyId(member = "COPY21") to copy21,
            CopyId(member = "COPY22") to copy22
        )

        consumeSources.invoke(pgm, copyDefinitions)
        val systemInterface = object : JavaSystemInterface() {
            override fun findCopy(copyId: CopyId): Copy? {
                return copyDefinitions[copyId]?.let { Copy.fromInputStream(it.byteInputStream(charset("UTF-8"))) }
            }
            override fun display(value: String) {
                // println(value)
            }
        }
        executePgm(programName = pgm, systemInterface = systemInterface, configuration = configuration)
    }

    @Test
    fun procedure1CallBackTest() {
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        var enteredTimes = 0
        var exitedTimes = 0
        val functionParams = mutableListOf<FunctionValue>()
        executePgm(systemInterface = systemInterface, programName = "PROCEDURE1", configuration = Configuration().apply {
            jarikoCallback.onEnterFunction = { functionName: String, params: List<FunctionValue>, _: ISymbolTable ->
                enteredTimes++
                functionParams.addAll(params)
                Assert.assertEquals("CALL1", functionName)
                Assert.assertEquals(11, params[0].value.asInt().value)
                Assert.assertEquals(22, params[1].value.asInt().value)
                Assert.assertEquals(ZeroValue, params[2].value)
            }
            jarikoCallback.onExitFunction = { functionName: String, _: Value ->
                exitedTimes++
                Assert.assertEquals("CALL1", functionName)
                Assert.assertEquals(33, functionParams[2].value.asInt().value)
            }
        })
        Assert.assertEquals(enteredTimes, exitedTimes)
    }

    @Test
    fun procedure2CallBackTest() {
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        var enteredTimes = 0
        var exitedTimes = 0
        val functionParams = mutableListOf<FunctionValue>()
        executePgm(systemInterface = systemInterface, programName = "PROCEDURE2", configuration = Configuration().apply {
            jarikoCallback.onEnterFunction = { functionName: String, params: List<FunctionValue>, _: ISymbolTable ->
                enteredTimes++
                functionParams.addAll(params)
                Assert.assertEquals("CALL1", functionName)
                Assert.assertEquals(11, params[0].value.asInt().value)
                Assert.assertEquals(22, params[1].value.asInt().value)
            }
            jarikoCallback.onExitFunction = { functionName: String, returnValue: Value ->
                exitedTimes++
                Assert.assertEquals("CALL1", functionName)
                Assert.assertEquals(33, returnValue.asInt().value)
            }
        })
        Assert.assertEquals(enteredTimes, exitedTimes)
    }

    @Test
    fun executeERROR01CallBackTest() {
        // Runtime error in program
        executePgmCallBackTest("ERROR01", SourceReferenceType.Program, "ERROR01", listOf(4))
    }

    @Test
    fun executeERROR01SourceLineTest() {
        executeSourceLineTest("ERROR01")
    }

    @Test
    fun executeERROR02CallBackTest() {
        // Syntax error in program
        executePgmCallBackTest("ERROR02", SourceReferenceType.Program, "ERROR02", listOf(6, 7))
    }

    @Test
    fun executeERROR02SourceLineTest() {
        executeSourceLineTest("ERROR02")
    }

    @Test
    fun executeERROR03CallBackTest() {
        // Invalid opcode in cpy
        executePgmCallBackTest("ERROR03", SourceReferenceType.Copy, "QILEGEN,CPERR01", listOf(7, 5))
    }

    @Test
    fun executeERROR03SourceLineTest() {
        executeSourceLineTest("ERROR03")
    }

    @Test
    fun executeERROR04CallBackTest() {
        // Syntax error in cpy
        executePgmCallBackTest("ERROR04", SourceReferenceType.Copy, "QILEGEN,CPERR02", listOf(5))
    }

    @Test
    fun executeERROR04SourceLineTest() {
        executeSourceLineTest("ERROR04")
    }

    @Test
    fun executeERROR05CallBackTest() {
        // Validating AST in copy
        executePgmCallBackTest("ERROR05", SourceReferenceType.Copy, "QILEGEN,CPERR03", listOf(5, 6))
    }

    @Test
    fun executeERROR05SourceLineTest() {
        executeSourceLineTest("ERROR05")
    }

    @Test
    fun executeERROR06CallBackTest() {
        // More than one error in data definitions
        executePgmCallBackTest("ERROR06", SourceReferenceType.Program, "ERROR06", listOf(7, 8, 10, 11, 12, 13))
    }

    @Test
    fun executeERROR06SourceLineTest() {
        executeSourceLineTest("ERROR06")
    }

    @Test
    fun executeERROR07CallBackTest() {
        // Repeated not supported operation code
        executePgmCallBackTest("ERROR07", SourceReferenceType.Program, "ERROR07", listOf(6, 9))
    }

    @Test
    fun executeERROR07SourceLineTest() {
        executeSourceLineTest("ERROR07")
    }

    @Test
    fun executeERROR08CallBackTest() {
        // Errors in block statements
        executePgmCallBackTest("ERROR08", SourceReferenceType.Program, "ERROR08", listOf(7, 8, 9, 14, 15, 16, 21, 22))
    }

    @Test
    fun executeERROR08SourceLineTest() {
        executeSourceLineTest("ERROR08")
    }

    @Test
    fun executeERROR09CallBackTest() {
        executePgmCallBackTest("ERROR09", SourceReferenceType.Program, "ERROR09", listOf(6, 7, 7, 8, 8))
    }

    @Test
    fun executeERROR09SourceLineTest() {
        executeSourceLineTest("ERROR09")
    }

    @Test
    fun executeERROR10CallBackTest() {
        executePgmCallBackTest("ERROR10", SourceReferenceType.Program, "ERROR10", listOf(5, 6))
    }

    @Test
    fun executeERROR10SourceLineTest() {
        executeSourceLineTest("ERROR10")
    }

    @Test
    fun executeERROR11CallBackTest() {
        executePgmCallBackTest("ERROR11", SourceReferenceType.Program, "ERROR11", listOf(5))
    }

    @Test
    fun executeERROR11SourceLineTest() {
        executeSourceLineTest("ERROR11")
    }

    @Test
    fun executeERROR12CallBackTest() {
        executePgmCallBackTest("ERROR12", SourceReferenceType.Program, "ERROR12", listOf(7))
    }

    @Test
    fun executeERROR13ShouldNotThrowStackOverflowError() {
        executeSourceLineTest(pgm = "ERROR13", throwableConsumer = {
            it.printStackTrace()
            assertTrue(
                message = "java.lang.StackOverflowError should not be present",
                actual = it.message!!.indexOf("java.lang.StackOverflowError") == -1
            )
        })
    }

    @Test
    fun executeERROR13CallBackTest() {
        executePgmCallBackTest("ERROR13", SourceReferenceType.Program, "ERROR13", listOf(9, 10))
    }

    /**
     * This test simulates what a precompiler might do throws the use of the beforeParsing callback
     * In ERROR01.rpgle I will comment C specification to avoid a division by zero errors
     * */
    @Test
    fun beforeParsingTest() {
        val programName = "ERROR01"
        // the first execution will go wrong
        kotlin.runCatching {
            executePgm(programName = programName)
        }.onSuccess {
            Assert.fail("Program must exit with error")
        }
        // Now implement logic where before parsing I will comment C spec
        val configuration = Configuration().apply {
            jarikoCallback.beforeParsing = { it.replace("     C ", "     C*") }
        }
        kotlin.runCatching {
            executePgm(programName = programName, configuration = configuration)
        }.onFailure {
            Assert.fail("Program must not exit with error")
        }
    }

    @Test
    fun beforeCopyInclusionTest() {
        val expectedIncludedCopies = listOf(CopyId(file = "QILEGEN", member = "TSTCPY01"))
        val includedCopies = mutableListOf<CopyId>()
        val configuration = Configuration().apply {
            jarikoCallback = JarikoCallback().apply {
                beforeCopyInclusion = { copyId, source ->
                    includedCopies.add(copyId)
                    source
                }
            }
        }
        executePgm(programName = "TSTCPY01", configuration = configuration)
        assertEquals(expectedIncludedCopies, includedCopies)
    }

    @Test
    fun afterCopiesInclusionTest() {
        val program = "TSTCPY01"
        val expectedIncludedCopies = listOf(CopyId(file = "QILEGEN", member = "TSTCPY01"))
        lateinit var includedCopies: List<CopyId>
        val configuration = Configuration().apply {
            options.debuggingInformation = true
            jarikoCallback = JarikoCallback().apply {
                afterCopiesInclusion = { copyBlocks ->
                    if (MainExecutionContext.getParsingProgramStack().peek().name == program) {
                        includedCopies = copyBlocks.map { copyBlock -> copyBlock.copyId }
                    }
                }
            }
        }
        executePgm(programName = program, configuration = configuration)
        assertEquals(expectedIncludedCopies, includedCopies)
    }

    @Test
    fun onCallPgmError() {
        var catchedError: ErrorEvent? = null
        val configuration = Configuration().apply {
            jarikoCallback.onCallPgmError = { errorEvent ->
                catchedError = errorEvent
            }
        }
        executePgm(programName = "ERRCALLER", configuration = configuration)
        assertNotNull(catchedError)
    }

    /**
     * This function is used to test the execution of a program and validate the error handling mechanism.
     * It expects the program to fail and checks if the error events are correctly captured.
     *
     * @param pgm The name of the program to be executed.
     * @param sourceReferenceType The expected type of the source reference (Program or Copy) where the error is expected to occur.
     * @param sourceId The expected identifier of the source where the error is expected to occur.
     * @param lines The list of line numbers where the errors are expected.
     */
    private fun executePgmCallBackTest(pgm: String, sourceReferenceType: SourceReferenceType, sourceId: String, lines: List<Int>) {
        val errorEvents = mutableListOf<ErrorEvent>()
        runCatching {
            val configuration = Configuration().apply {
                jarikoCallback.onError = { errorEvent ->
                    println(errorEvent)
                    errorEvents.add(errorEvent)
                }
                options = Options(debuggingInformation = true)
            }
            executePgm(pgm, configuration = configuration)
        }.onSuccess {
            Assert.fail("Program must exit with error")
        }.onFailure {
            println(it.stackTraceToString())
            Assert.assertEquals(sourceReferenceType, errorEvents[0].sourceReference!!.sourceReferenceType)
            Assert.assertEquals(sourceId, errorEvents[0].sourceReference!!.sourceId)
            Assert.assertEquals(lines.sorted(), errorEvents.map { errorEvent -> errorEvent.sourceReference!!.relativeLine }.sorted())
        }
    }

    /**
     * Verify that the sourceLine is properly set in case of error.
     * ErrorEvent must contain a reference of an absolute line of the source code
     * */
    private fun executeSourceLineTest(pgm: String, throwableConsumer: (Throwable) -> Unit = {}) {
        lateinit var lines: List<String>
        val errorEvents = mutableListOf<ErrorEvent>()
        val configuration = Configuration().apply {
            jarikoCallback.beforeParsing = { it ->
                lines = StringReader(it).readLines()
                it
            }
            jarikoCallback.onError = { errorEvents.add(it) }
            // I set dumpSourceOnExecutionError because I want test also the sourceLine presence in case
            // of runtime error
            options = Options(debuggingInformation = true, dumpSourceOnExecutionError = true)
        }
        kotlin.runCatching {
            executePgm(pgm, configuration = configuration)
        }.onSuccess {
            Assert.fail("$pgm must exit with error")
        }.onFailure {
            throwableConsumer(it)
            errorEvents.forEach {
                Assert.assertEquals(lines[it.absoluteLine!! - 1], it.fragment)
            }
        }
    }
}