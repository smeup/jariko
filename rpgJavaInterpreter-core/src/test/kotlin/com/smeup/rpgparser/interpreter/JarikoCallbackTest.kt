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
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.parsing.facade.SourceReferenceType
import org.junit.Assert
import kotlin.test.Test

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
            options = Options().apply { debuggingInformation = true }
            jarikoCallback = JarikoCallback().apply {
                afterAstCreation = { ast -> postProcessed = ast.source!! }
                onEnterStatement = { lineNumber: Int, sourceReference: SourceReference ->
                    if (sourceReference.sourceReferenceType == SourceReferenceType.Copy) {
                        println("Copy - copyId: ${sourceReference.sourceId}, relativeLineNumber: ${sourceReference.lineNumber}, lineNumber: $lineNumber")
                    } else {
                        println("Program - relativeLineNumber: ${sourceReference.lineNumber}, lineNumber: $lineNumber")
                    }
                    val src = when (sourceReference.sourceReferenceType) {
                        SourceReferenceType.Copy -> copyDefinitions[CopyId(member = sourceReference.sourceId)]
                        SourceReferenceType.Program -> pgm
                    }
                    require(src != null)
                    val relativeStatement = src.lines()[sourceReference.lineNumber - 1]
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
        executePgmCallBackTest("ERROR01", SourceReferenceType.Program, "ERROR01", listOf(4))
    }

    @Test
    fun executeERROR02CallBackTest() {
        executePgmCallBackTest("ERROR02", SourceReferenceType.Program, "ERROR02", listOf(6, 7))
    }
    @Test
    fun executeERROR03CallBackTest() {
        // listOf(7, 7) is not an error because we have an error event duplicated, but for now is not a problem
        executePgmCallBackTest("ERROR03", SourceReferenceType.Copy, "QILEGEN,ERROR01", listOf(7, 7))
    }

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
            println(it.message)
            Assert.assertEquals(sourceReferenceType, errorEvents[0].sourceReference!!.sourceReferenceType)
            Assert.assertEquals(sourceId, errorEvents[0].sourceReference!!.sourceId)
            Assert.assertEquals(lines, errorEvents.map { errorEvent -> errorEvent.sourceReference!!.position.start.line })
        }
    }
}