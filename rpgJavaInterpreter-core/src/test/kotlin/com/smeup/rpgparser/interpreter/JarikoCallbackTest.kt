/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.parsing.facade.SourceReferenceType
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.smeup.dbmock.TABDS01LDbMock
import com.smeup.rpgparser.utils.Format
import com.smeup.rpgparser.utils.compile
import org.junit.Assert
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.test.*
import kotlin.test.DefaultAsserter.assertTrue

/**
 * Test suite to test Jariko callback features
 * */
class JarikoCallbackTest : AbstractTest() {
    @Test
    fun onEnterStatement() {
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        var enteredTimes = 0
        executePgm(
            systemInterface = systemInterface,
            programName = "HELLO",
            configuration =
                Configuration().apply {
                    jarikoCallback.onEnterStatement = { _: Int, _: SourceReference -> enteredTimes++ }
                    options = Options().apply { debuggingInformation = true }
                },
        )
        Assert.assertEquals(3, enteredTimes)
    }

    @Test
    fun onEnterStatementWithCall() {
        val expectedEnteredTimes = mapOf("CAL01" to 5, "CAL02" to 4)
        val actualEnteredTimes = mutableMapOf("CAL01" to 0, "CAL02" to 0)
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        executePgm(
            systemInterface = systemInterface,
            programName = "CAL01",
            configuration =
                Configuration().apply {
                    jarikoCallback.onEnterStatement = { _: Int, sourceReference: SourceReference ->
                        println(sourceReference)
                        actualEnteredTimes[sourceReference.sourceId] = actualEnteredTimes[sourceReference.sourceId]!! + 1
                    }
                    options = Options().apply { debuggingInformation = true }
                },
        )
        Assert.assertEquals(expectedEnteredTimes, actualEnteredTimes)
    }

    @Test
    fun programLifeCycle() {
        val enteredTimes = mutableMapOf("CAL04" to 0, "CAL02" to 0)
        val exitedTimes = mutableMapOf("CAL04" to 0, "CAL02" to 0)
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        executePgm(
            systemInterface = systemInterface,
            programName = "CAL04",
            configuration =
                Configuration().apply {
                    jarikoCallback.onEnterPgm = { programName: String, symbolTable: ISymbolTable ->
                        println("onEnterPgm - programName: $programName, symbolTable: $symbolTable")
                        enteredTimes[programName] = enteredTimes[programName]!! + 1
                    }
                    jarikoCallback.onExitPgm = { programName: String, symbolTable: ISymbolTable, _: Throwable? ->
                        println("onExitPgm - programName: $programName, symbolTable: $symbolTable")
                        exitedTimes[programName] = exitedTimes[programName]!! + 1
                    }
                },
        )
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
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        debuggingInformation = true
                        dumpSourceOnExecutionError = true
                    }
                jarikoCallback =
                    JarikoCallback().apply {
                        afterAstCreation = { ast -> postProcessed = ast.source!! }
                        onEnterStatement = { lineNumber: Int, sourceReference: SourceReference ->
                            if (sourceReference.sourceReferenceType == SourceReferenceType.Copy) {
                                println(
                                    "Copy - copyId: ${sourceReference.sourceId}, relativeLineNumber: ${sourceReference.relativeLine}, lineNumber: $lineNumber",
                                )
                            } else {
                                println("Program - relativeLineNumber: ${sourceReference.relativeLine}, lineNumber: $lineNumber")
                            }
                            val src =
                                when (sourceReference.sourceReferenceType) {
                                    SourceReferenceType.Copy ->
                                        copyDefinitions[
                                            CopyId(
                                                member = sourceReference.sourceId.uppercase(Locale.getDefault()),
                                            ),
                                        ]
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
        val configuration =
            Configuration().apply {
                options = Options().apply { debuggingInformation = true }
                jarikoCallback =
                    JarikoCallback().apply {
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
        val configuration =
            Configuration().apply {
                options = Options().apply { debuggingInformation = true }
                jarikoCallback =
                    JarikoCallback().apply {
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
        consumeSources: (pgm: String, copyDefinitions: Map<CopyId, String>) -> Unit,
    ) {
        val pgm =
            """
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
        val copyDefinitions =
            mapOf(
                CopyId(member = "COPY1") to copy1,
                CopyId(member = "COPY2") to copy2,
                CopyId(member = "COPY21") to copy21,
                CopyId(member = "COPY22") to copy22,
            )

        consumeSources.invoke(pgm, copyDefinitions)
        val systemInterface =
            object : JavaSystemInterface() {
                override fun findCopy(copyId: CopyId): Copy? =
                    copyDefinitions[copyId]?.let { Copy.fromInputStream(it.byteInputStream(charset("UTF-8"))) }

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
        executePgm(
            systemInterface = systemInterface,
            programName = "PROCEDURE1",
            configuration =
                Configuration().apply {
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
                },
        )
        Assert.assertEquals(enteredTimes, exitedTimes)
    }

    @Test
    fun procedure2CallBackTest() {
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        var enteredTimes = 0
        var exitedTimes = 0
        val functionParams = mutableListOf<FunctionValue>()
        executePgm(
            systemInterface = systemInterface,
            programName = "PROCEDURE2",
            configuration =
                Configuration().apply {
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
                },
        )
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

    /**
     * This test is ignored because there is a mock implementation for SQL statements.
     */
    @Test
    @Ignore
    fun executeERROR07CallBackTest() {
        // Repeated not supported operation code
        executePgmCallBackTest("ERROR07", SourceReferenceType.Program, "ERROR07", listOf(6, 9))
    }

    /**
     * This test is ignored because there is a mock implementation for SQL statements.
     */
    @Test
    @Ignore
    fun executeERROR07SourceLineTest() {
        executeSourceLineTest("ERROR07")
    }

    @Test
    fun executeERROR08CallBackTest() {
        // Errors in block statements
        executePgmCallBackTest("ERROR08", SourceReferenceType.Program, "ERROR08", listOf(7, 8, 8, 9, 9, 14, 15, 15, 16, 16, 21, 22, 22))
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
                actual = it.message!!.indexOf("java.lang.StackOverflowError") == -1,
            )
        })
    }

    @Test
    fun executeERROR13CallBackTest() {
        executePgmCallBackTest("ERROR13", SourceReferenceType.Program, "ERROR13", listOf(9, 10))
    }

    @Test
    fun executeERROR14CallBackTest() {
        executePgmCallBackTest("ERROR14", SourceReferenceType.Program, "ERROR14", listOf(5))
    }

    @Test
    fun executeERROR15CallBackTest() {
        executePgmCallBackTest("ERROR15", SourceReferenceType.Program, "ERROR15", listOf(16))
    }

    @Test
    fun executeERROR16CallBackTest() {
        executePgmCallBackTest("ERROR16", SourceReferenceType.Program, "ERROR16", listOf(12), createMockReloadConfig())
    }

    @Test
    fun executeERROR16SourceLineTest() {
        executeSourceLineTest("ERROR16")
    }

    @Test
    fun executeERROR17CallBackTest() {
        executePgmCallBackTest("ERROR17", SourceReferenceType.Program, "ERROR17", listOf(12))
    }

    @Test
    fun executeERROR17SourceLineTest() {
        executeSourceLineTest("ERROR17")
    }

    @Test
    fun executeERROR18CallBackTest() {
        executePgmCallBackTest("ERROR18", SourceReferenceType.Program, "ERROR18", listOf(10))
    }

    @Test
    fun executeERROR18SourceLineTest() {
        executeSourceLineTest("ERROR18")
    }

    @Test
    fun executeERROR19CallBackTest() {
        // In this case the error occurs inside APIERR1
        executePgmCallBackTest("APIERR1", SourceReferenceType.Program, "APIERR1", listOf(7, 7, 7))
    }

    @Test
    fun executeERROR19SourceLineTest() {
        // In this case the error occurs inside APIERR1
        executeSourceLineTest("APIERR1")
    }

    @Test
    fun executeERROR20SourceLineTest() {
        executeSourceLineTest("ERROR20")
    }

    @Test
    fun executeERROR21CallBackTest() {
        executePgmCallBackTest("ERROR21", SourceReferenceType.Program, "ERROR21", listOf(9, 10))
    }

    @Test
    fun executeERROR21SourceLineTest() {
        executeSourceLineTest("ERROR21")
    }

    @Test
    fun executeERROR20CallBackTest() {
        executePgmCallBackTest("ERROR20", SourceReferenceType.Program, "ERROR20", listOf(7, 8))
    }

    @Test
    fun executeERROR22CallBackTest() {
        executePgmCallBackTest("ERROR22", SourceReferenceType.Program, "ERROR22", listOf(10, 11))
    }

    @Test
    fun executeERROR22SourceLineTest() {
        executeSourceLineTest("ERROR22")
    }

    @Test
    fun executeERROR23CallBackTest() {
        executePgmCallBackTest(
            "ERROR23",
            SourceReferenceType.Program,
            "ERROR23",
            mapOf(
                9 to
                    "Factor 2 cannot be null at: Position(start=Line 9, Column 11, end=Line 9, Column 16) com.smeup.rpgparser.RpgParser\$FactorContext",
                14 to
                    "Factor 1 cannot be null at: Position(start=Line 14, Column 11, end=Line 14, Column 25) com.smeup.rpgparser.RpgParser\$FactorContext",
            ),
        )
    }

    @Test
    fun executeERROR24CallBackTest() {
        executePgmCallBackTest(
            "ERROR24",
            SourceReferenceType.Program,
            "ERROR24",
            mapOf(
                8 to
                    "Initialization value is incorrect. Must be 'YYYY-MM-DD' at: Position(start=Line 8, Column 5, end=Line 8, Column 81) com.smeup.rpgparser.RpgParser\$DspecContext",
                9 to
                    "Initialization value is incorrect. Must be 'YYYY-MM-DD' at: Position(start=Line 9, Column 5, end=Line 9, Column 85) com.smeup.rpgparser.RpgParser\$DspecContext",
            ),
        )
    }

    @Test
    fun executeERROR25CallBackTest() {
        executePgmCallBackTest(
            "ERROR25",
            SourceReferenceType.Program,
            "ERROR25",
            mapOf(
                8 to
                    "For JUL format the date must be between 1940 and 2039 at: Position(start=Line 8, Column 5, end=Line 8, Column 81) com.smeup.rpgparser.RpgParser\$DspecContext",
                9 to
                    "For JUL format the date must be between 1940 and 2039 at: Position(start=Line 9, Column 5, end=Line 9, Column 85) com.smeup.rpgparser.RpgParser\$DspecContext",
            ),
        )
    }

    @Test
    fun executeERROR26CallBackTest() {
        executePgmCallBackTest(
            "ERROR26",
            SourceReferenceType.Program,
            "ERROR26",
            mapOf(
                8 to
                    "For ISO format the date must be between 0001 and 9999 at: Position(start=Line 8, Column 5, end=Line 8, Column 81) com.smeup.rpgparser.RpgParser\$DspecContext",
                9 to
                    "Initialization value is incorrect. Must be 'YYYY-MM-DD' at: Position(start=Line 9, Column 5, end=Line 9, Column 85) com.smeup.rpgparser.RpgParser\$DspecContext",
            ),
        )
    }

    @Test
    fun executeERROR23SourceLineTest() {
        executeSourceLineTest("ERROR23")
    }

    @Test
    fun executeERROR29CallBackTest() {
        executePgmCallBackTest(
            "ERROR29",
            SourceReferenceType.Program,
            "ERROR29",
            mapOf(
                11 to "MOVE/MOVEL for BooleanType have to be 0, 1 or blank",
            ),
        )
    }

    @Test
    fun executeERROR29SourceLineTest() {
        executeSourceLineTest("ERROR29")
    }

    @Test
    fun executeERROR30CallBackTest() {
        executePgmCallBackTest(
            "ERROR30",
            SourceReferenceType.Program,
            "ERROR30",
            mapOf(
                11 to "MOVE/MOVEL for BooleanType have to be 0, 1 or blank",
            ),
        )
    }

    @Test
    fun executeERROR30SourceLineTest() {
        executeSourceLineTest("ERROR30")
    }

    @Test
    fun executeERROR31CallBackTest() {
        executePgmCallBackTest(
            "ERROR31",
            SourceReferenceType.Program,
            "ERROR31",
            mapOf(
                11 to "MOVE/MOVEL for BooleanType have to be 0, 1 or blank",
            ),
        )
    }

    @Test
    fun executeERROR31SourceLineTest() {
        executeSourceLineTest("ERROR31")
    }

    @Test
    fun executeERROR32CallBackTest() {
        executePgmCallBackTest(
            "ERROR32",
            SourceReferenceType.Program,
            "ERROR32",
            mapOf(
                11 to "MOVE/MOVEL for BooleanType have to be 0, 1 or blank",
            ),
        )
    }

    @Test
    fun executeERROR32SourceLineTest() {
        executeSourceLineTest("ERROR32")
    }

    @Test
    fun executeERROR33CallBackTest() {
        executePgmCallBackTest(
            "ERROR33",
            SourceReferenceType.Program,
            "ERROR33",
            mapOf(
                11 to "MOVE/MOVEL for BooleanType have to be 0, 1 or blank",
            ),
        )
    }

    @Test
    fun executeERROR33SourceLineTest() {
        executeSourceLineTest("ERROR33")
    }

    @Test
    fun executeERROR34CallBackTest() {
        executePgmCallBackTest(
            "ERROR34",
            SourceReferenceType.Program,
            "ERROR34",
            mapOf(
                11 to "MOVE/MOVEL for BooleanType have to be 0, 1 or blank",
            ),
        )
    }

    @Test
    fun executeERROR34SourceLineTest() {
        executeSourceLineTest("ERROR34")
    }

    @Test
    fun executeERROR35CallBackTest() {
        executePgmCallBackTest(
            pgm = "ERROR35",
            sourceReferenceType = SourceReferenceType.Program,
            sourceId = "ERROR35",
            lines = listOf(9, 10),
        )
    }

    @Test
    fun executeERROR35CSourceLineTest() {
        executeSourceLineTest(pgm = "ERROR35")
    }

    /**
     * NOTE: This is error is thrown because Reload does not support '*START' and '*END' constants yet.
     * When this feature gets supported please restore it on Jariko side by following these steps:
     * - Remove this test or mark it as ignored
     * - Remove the [@Ignore] decorator from the [MULANGT50FileAccess1Test.executeMUDRNRAPU00248] test
     * - Remove the runtime error (it should be in [Expression.createKList] if not moved)
     */
    @Test
    fun executeERROR36CallBackTest() {
        TABDS01LDbMock().usePopulated({
            executePgmCallBackTest(
                pgm = "ERROR36",
                sourceReferenceType = SourceReferenceType.Program,
                sourceId = "ERROR36",
                lines = listOf(6),
                reloadConfig = it.createReloadConfig(),
            )
        })
    }

    @Test
    fun executeERROR36SourceLineTest() {
        executeSourceLineTest(pgm = "ERROR36")
    }

    /**
     * NOTE: At the current state of implementation dynamic memory allocation and pointers are NOT fully supported
     * If this behaviour changes and [ReallocExpr] is expanded to support every type of pointer, please remove this test
     */
    @Test
    fun executeERROR37CallBackTest() {
        executePgmCallBackTest(
            pgm = "ERROR37",
            sourceReferenceType = SourceReferenceType.Program,
            sourceId = "ERROR37",
            lines = listOf(9),
        )
    }

    @Test
    fun executeERROR37SourceLineTest() {
        executeSourceLineTest(pgm = "ERROR37")
    }

    /**
     * NOTE: At the current state of implementation dynamic memory allocation and pointers are NOT fully supported
     * If this behaviour changes and [ReallocExpr] is expanded to support every type of pointer, please remove this test
     */
    @Test
    fun executeERROR38CallBackTest() {
        executePgmCallBackTest(
            pgm = "ERROR38",
            sourceReferenceType = SourceReferenceType.Program,
            sourceId = "ERROR38",
            lines = listOf(12),
        )
    }

    @Test
    fun executeERROR38SourceLineTest() {
        executeSourceLineTest(pgm = "ERROR38")
    }

    @Test
    fun executeERROR41CallBackTest() {
        executePgmCallBackTest(
            "ERROR41",
            SourceReferenceType.Program,
            "ERROR41",
            mapOf(
                24 to "Cannot coerce sub-string `0052 ` to NumberType(entireDigits=3, decimalDigits=2, rpgType=S).",
            ),
        )
    }

    @Test
    fun executeERROR41SourceLineTest() {
        executeSourceLineTest("ERROR41")
    }

    @Test
    fun executeERROR42CallBackTest() {
        executePgmCallBackTest(
            "ERROR42",
            SourceReferenceType.Program,
            "ERROR42",
            mapOf(
                24 to "Cannot coerce sub-string `0052 ` to NumberType(entireDigits=3, decimalDigits=2, rpgType=S).",
            ),
        )
    }

    @Test
    fun executeERROR42SourceLineTest() {
        executeSourceLineTest("ERROR42")
    }

    @Test
    fun executeERROR43CallBackTest() {
        executePgmCallBackTest(
            "ERROR43",
            SourceReferenceType.Program,
            "ERROR43",
            mapOf(
                23 to "Cannot coerce sub-string `0005 ` to NumberType(entireDigits=5, decimalDigits=0, rpgType=S).",
            ),
        )
    }

    @Test
    fun executeERROR43SourceLineTest() {
        executeSourceLineTest("ERROR43")
    }

    @Test
    fun executeERROR44CallBackTest() {
        executePgmCallBackTest(
            "ERROR44",
            SourceReferenceType.Program,
            "ERROR44",
            mapOf(
                8 to "Error calling program or procedure - Could not find program MISSING",
            ),
        )
    }

    @Test
    fun executeERROR44SourceLineTest() {
        executeSourceLineTest("ERROR44")
    }

    @Test
    fun traceEmittedTest() {
        val startTraces = mutableListOf<JarikoTrace>()
        var finishCount = 0

        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    startTraces.add(trace)
                }
                jarikoCallback.finishJarikoTrace = {
                    finishCount += 1
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)

        assert(startTraces.isNotEmpty())
        assert(finishCount > 0)
    }

    /**
     * Test if trace configurations work as expected.
     */
    @Test
    fun traceConfigurationTest() {
        val targetPgm = "TRACETST1"
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.MainExecutionContext))
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.Parsing))
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.CompositeStatement))
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.RpgProgram))
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.SymbolTable))
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.CallStmt))
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.ExecuteSubroutine))
        testTraceConfiguration(targetPgm, listOf(JarikoTraceKind.FunctionCall))
        testTraceConfiguration(targetPgm, listOf())

        // Test default behaviour -> all enabled
        val traces = mutableListOf<JarikoTrace>()

        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace -> traces.add(trace) }
            }
        executePgm(targetPgm, configuration = configuration, systemInterface = systemInterface)

        // Kinds are reported at least once
        JarikoTraceKind.entries.forEach { kind -> assert(traces.any { it.kind == kind }) }
    }

    @Test
    fun traceOpenedAlsoClosedTest() {
        val startTraces = mutableListOf<JarikoTrace>()
        var finishCount = 0

        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    startTraces.add(trace)
                }
                jarikoCallback.finishJarikoTrace = {
                    finishCount += 1
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        assertEquals(startTraces.size, finishCount)
    }

    @Test
    fun traceSymtblTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val symtblTraces = traces.filter { it.kind == JarikoTraceKind.SymbolTable }

        // (INIT + LOAD) * (n.called programs + n.called procedures = 4)
        assertEquals(symtblTraces.size, 8)

        assertEquals(symtblTraces.filter { it.description == "INIT" }.size, 4)
        assertEquals(symtblTraces.filter { it.description == "LOAD" }.size, 4)
    }

    @Test
    fun traceCompositeStatementTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val compositeTraces = traces.filter { it.kind == JarikoTraceKind.CompositeStatement }

        // 2 for each called program
        assertEquals(compositeTraces.size, 4)

        assertEquals(compositeTraces.filter { it.description == "IF" }.size, 4)
    }

    @Test
    fun traceProgramTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val programTraces = traces.filter { it.kind == JarikoTraceKind.CallStmt }

        // Only one call
        assertEquals(programTraces.size, 1)

        assertEquals(programTraces.filter { it.description == "TRACETST2" }.size, 1)
    }

    @Test
    fun traceSubroutineTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val programTraces = traces.filter { it.kind == JarikoTraceKind.ExecuteSubroutine }

        // 1 subroutine call per program
        assertEquals(programTraces.size, 2)

        assertEquals(programTraces.filter { it.description == "SR" }.size, 2)
    }

    @Test
    fun traceParsingTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val parsingTraces = traces.filter { it.kind == JarikoTraceKind.Parsing }

        // (RPGLOAD + LEXER + PARSER + RCONTEXT + CHKPTREE + AST) * (n.called programs = 2)
        assertEquals(parsingTraces.size, 12)

        assertEquals(parsingTraces.filter { it.description == "RPGLOAD" }.size, 2)
        assertEquals(parsingTraces.filter { it.description == "LEXER" }.size, 2)
        assertEquals(parsingTraces.filter { it.description == "PARSER" }.size, 2)
        assertEquals(parsingTraces.filter { it.description == "RCONTEXT" }.size, 2)
        assertEquals(parsingTraces.filter { it.description == "CHKPTREE" }.size, 2)
        assertEquals(parsingTraces.filter { it.description == "AST" }.size, 2)
    }

    @Test
    fun traceFunctionCallTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val functionCallTraces = traces.filter { it.kind == JarikoTraceKind.FunctionCall }

        // Two in the root program
        assertEquals(functionCallTraces.size, 2)

        assertEquals(functionCallTraces.filter { it.description == "CALL1" }.size, 2)
    }

    @Test
    fun traceMainExecutionContextTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val mainExecutionTraces = traces.filter { it.kind == JarikoTraceKind.MainExecutionContext }

        // Only one main execution context
        assertEquals(mainExecutionTraces.size, 1)
    }

    @Test
    fun traceRpgProgramTest() {
        val traces = mutableListOf<JarikoTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.startJarikoTrace = { trace ->
                    traces.add(trace)
                }
            }
        executePgm("TRACETST1", configuration = configuration, systemInterface = systemInterface)
        val rpgProgramTraces = traces.filter { it.kind == JarikoTraceKind.RpgProgram }

        // TRACETST1, TRACETST2
        assertEquals(rpgProgramTraces.size, 2)

        assertEquals(rpgProgramTraces.filter { it.description == "Execute TRACETST1" }.size, 1)
        assertEquals(rpgProgramTraces.filter { it.description == "Execute TRACETST2" }.size, 1)
    }

    /**
     * Executing a program with profiling annotations when profiling is disabled should produce nothing.
     */
    @Test
    fun executeSimpleSpanWithoutProfilingProducesNothing() {
        var openTraces = 0
        var closedTraces = 0
        val executedTraces = mutableListOf<RpgTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = false
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    openTraces += 1
                    executedTraces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/SIMPLE_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(0, openTraces)
        assertEquals(0, closedTraces)
        assertTrue(executedTraces.isEmpty())
    }

    /**
     * Executing a simple program with span traces correctly reports open and close traces.
     */
    @Test
    fun executeSimpleSpanOpenAlsoCloses() {
        val openTraces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    openTraces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/SIMPLE_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(1, openTraces.size)
        assertEquals(1, closedTraces)

        // assert line of executed traces
        openTraces.assertExecuted(12)
    }

    /**
     * Executing a program with multiple span traces correctly reports open and close traces.
     */
    @Test
    fun executeMultipleSpanOpenAlsoCloses() {
        val openTraces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    openTraces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/MULTIPLE_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(12, openTraces.size)
        assertEquals(12, closedTraces)

        // assert line of executed traces
        openTraces.assertExecuted(13)
        openTraces.assertExecuted(15)
        openTraces.assertExecuted(17, 10)
    }

    /**
     * Test that close spans in MULTIPLE_TELEMETRY_SPAN.rpgle never exceed open ones.
     * See [executeMultipleSpanOpenAlsoCloses] for assertions on their position.
     * @see [executeMultipleSpanOpenAlsoCloses].
     */
    @Test
    fun executeMultipleSpanCloseNeverExceedOpen() {
        val traces = Stack<RpgTrace>()
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.push(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    traces.pop()
                }
            }
        executePgm("profiling/MULTIPLE_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(0, traces.size)
    }

    /**
     * Executing a simple program with multiple spans attached to the same statement works as expected.
     */
    @Test
    fun executeMultipleSpanSameStatementTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/MULTISPAN_SAME_STATEMENT", configuration = configuration, systemInterface = systemInterface)
        assertEquals(4, traces.size)
        assertEquals(4, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(12)
        traces.assertExecuted(13)
        traces.assertExecuted(14)
        traces.assertExecuted(15)
    }

    /**
     * Executing a simple program with multiple spans attached to the same statement works as expected.
     * One of them closes in a different line from the others.
     */
    @Test
    fun executeMultipleSpanSameStatement2Traces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/MULTISPAN_SAME_STATEMENT_2", configuration = configuration, systemInterface = systemInterface)
        assertEquals(4, traces.size)
        assertEquals(4, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(12)
        traces.assertExecuted(13)
        traces.assertExecuted(14)
        traces.assertExecuted(15)
    }

    /**
     * Executing a program with spans attached to a FOR statement behaves as expected.
     */
    @Test
    fun executeForTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/FOR_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(11, traces.size)
        assertEquals(11, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(13)
        traces.assertExecuted(15, 10)
    }

    /**
     * Executing a program with spans attached to a DO statement behaves as expected.
     */
    @Test
    fun executeDoTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/DO_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(101, traces.size)
        assertEquals(101, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(13)
        traces.assertExecuted(15, 100)
    }

    /**
     * Executing a program with spans attached to a MONITOR statement behaves as expected.
     */
    @Test
    fun executeMonitorTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/MONITOR_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(3, traces.size)
        assertEquals(3, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(13)
        traces.assertExecuted(15)
        traces.assertExecuted(19)
    }

    /**
     * Executing a program with spans attached to a MONITOR statement behaves as expected
     * ensuring already cleaned up spans are not reported twice.
     */
    @Test
    fun executeMonitorCleanupTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/MONITOR_TELEMETRY_SPAN_CLEANUP", configuration = configuration, systemInterface = systemInterface)
        assertEquals(4, traces.size)
        assertEquals(4, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(13)
        traces.assertExecuted(15)
        traces.assertExecuted(18)
        traces.assertExecuted(22)
    }

    /**
     * Executing a program with spans attached to an IF statement behaves as expected.
     */
    @Test
    fun executeIfTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/IF_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(2, traces.size)
        assertEquals(2, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(13)
        traces.assertExecuted(15)
    }

    /**
     * Executing a program with spans attached to a SELECT statement behaves as expected.
     */
    @Test
    fun executeSelectTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/SELECT_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(2, traces.size)
        assertEquals(2, closedTraces)

        // assert line of executed traces
        traces.assertExecuted(13)
        traces.assertExecuted(24)
    }

    /**
     * Executing a program with spans in a copy behaves as expected.
     */
    @Test
    fun executeCopySpanTraces() {
        val traces = mutableListOf<RpgTrace>()
        var closedTraces = 0
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        profilingSupport = true
                    }
                jarikoCallback.startRpgTrace = { trace ->
                    traces.add(trace)
                }
                jarikoCallback.finishRpgTrace = {
                    closedTraces += 1
                }
            }
        executePgm("profiling/COPY_TELEMETRY_SPAN", configuration = configuration, systemInterface = systemInterface)
        assertEquals(1, traces.size)
        assertEquals(1, closedTraces)

        // assert line of executed traces
        traces.assertExecutedInSource("TELSPAN", 6)
    }

    @Test
    fun executeERROR45CallBackTest() {
        executePgmCallBackTest(
            "ERROR45",
            SourceReferenceType.Program,
            "ERROR45",
            mapOf(
                18 to "Factor 2 and Result with different type and size.",
            ),
        )
    }

    @Test
    fun executeERROR45SourceLineTest() {
        executeSourceLineTest("ERROR45")
    }

    @Test
    fun executeERROR46CallBackTest() {
        executePgmCallBackTest(
            "ERROR46",
            SourceReferenceType.Program,
            "ERROR46",
            mapOf(
                18 to "Factor 2 and Result with different type and size.",
            ),
        )
    }

    @Test
    fun executeERROR46SourceLineTest() {
        executeSourceLineTest("ERROR46")
    }

    @Test
    fun executeERROR47CallBackTest() {
        executePgmCallBackTest(
            "ERROR47",
            SourceReferenceType.Program,
            "ERROR47",
            mapOf(
                9 to "10 cannot be assigned to I of type NumberType(entireDigits=1, decimalDigits=0, rpgType=)",
            ),
        )
    }

    @Test
    fun executeERROR47SourceLineTest() {
        executeSourceLineTest("ERROR47")
    }

    @Test
    fun executeERROR48CallBackTest() {
        executePgmCallBackTest(
            "ERROR48",
            SourceReferenceType.Program,
            "ERROR48",
            mapOf(
                7 to "241122 cannot be assigned to RES of type NumberType(entireDigits=4, decimalDigits=0, rpgType=)",
            ),
        )
    }

    @Test
    fun executeERROR48SourceLineTest() {
        executeSourceLineTest("ERROR48")
    }

    @Test
    fun executeERROR49CallBackTest() {
        executePgmCallBackTest(
            "ERROR49",
            SourceReferenceType.Program,
            "ERROR49",
            mapOf(
                6 to "Data reference *IN10 not resolved",
            ),
        )
    }

    @Test
    fun executeERROR49SourceLineTest() {
        executeSourceLineTest("ERROR49")
    }

    @Test
    fun executeERROR50CallBackTest() {
        executePgmCallBackTest(
            "ERROR50",
            SourceReferenceType.Program,
            "ERROR50",
            mapOf(
                19 to "Data reference not resolved: DS1_F2 at: Position(start=Line 12, Column 35, end=Line 12, Column 41)",
            ),
        )
    }

    @Test
    fun executeERROR50SourceLineTest() {
        executeSourceLineTest("ERROR50")
    }

    @Test
    fun executeERROR51CallBackTest() {
        executePgmCallBackTest(
            "ERROR51",
            SourceReferenceType.Program,
            "ERROR51",
            mapOf(
                13 to "You cannot move a DS into a numeric array: SCAATTDS (Position(start=Line 13, Column 35, end=Line 13, Column 43))",
            ),
        )
    }

    @Test
    fun executeERROR51SourceLineTest() {
        executeSourceLineTest("ERROR51")
    }

    @Test
    fun executeERROR52CallBackTest() {
        executePgmCallBackTest(
            "ERROR52",
            SourceReferenceType.Program,
            "ERROR52",
            mapOf(
                13 to "You cannot move a numeric array into a DS: SCAATT (Position(start=Line 13, Column 35, end=Line 13, Column 41))",
            ),
        )
    }

    @Test
    fun executeERROR52SourceLineTest() {
        executeSourceLineTest("ERROR52")
    }

    @Test
    fun executeERROR53CallBackTest() {
        executePgmCallBackTest("ERROR53", SourceReferenceType.Copy, "QILEGEN,£PDS", listOf(130))
    }

    @Test
    fun executeERROR53SourceLineTest() {
        executeSourceLineTest("ERROR53")
    }

    @Test
    fun executeERROR54CallBackTest() {
        executePgmCallBackTest(
            "ERROR54",
            SourceReferenceType.Program,
            "ERROR54",
            mapOf(
                5 to "Array index not valid - Indexes should be >=1. Index asked: 0",
            ),
        )
    }

    @Test
    fun executeERROR54SourceLineTest() {
        executeSourceLineTest("ERROR54")
    }

    @Test
    fun executeERROR55CallBackTest() {
        executePgmCallBackTest(
            "ERROR55",
            SourceReferenceType.Program,
            "ERROR55",
            mapOf(
                5 to "Array index not valid - Indexes should be >=1. Index asked: 0",
            ),
        )
    }

    @Test
    fun executeERROR55SourceLineTest() {
        executeSourceLineTest("ERROR55")
    }

    @Test
    fun executeERROR56CallBackTest() {
        val errorLine19 =
            buildString {
                appendLine(
                    "Program FunctionInterpreter.PR2.static - Issue executing EvalStmt at absolute line 32 of SourceReference(sourceReferenceType=Program, sourceId=ERROR56, relativeLine=32, position=Position(start=Line 32, Column 25, end=Line 32, Column 81)).",
                )
                append("Data reference not resolved: PR2_VAR at: Position(start=Line 41, Column 11, end=Line 41, Column 18)")
            }
        executePgmCallBackTest(
            "ERROR56",
            SourceReferenceType.Program,
            "ERROR56",
            mapOf(
                19 to errorLine19,
                32 to "Data reference not resolved: PR2_VAR at: Position(start=Line 41, Column 11, end=Line 41, Column 18)",
                41 to "Data reference not resolved: PR2_VAR at: Position(start=Line 41, Column 11, end=Line 41, Column 18)",
            ),
        )
    }

    @Test
    fun executeERROR56SourceLineTest() {
        executeSourceLineTest("ERROR56")
    }

    @Test
    fun executeERROR57CallBackTest() {
        executePgmCallBackTest(
            "ERROR57",
            SourceReferenceType.Program,
            "ERROR57",
            mapOf(
                14 to "Cannot add an Array to a Standalone at: Position(start=Line 14, Column 35, end=Line 14, Column 41))",
            ),
        )
    }

    @Test
    fun executeERROR57SourceLineTest() {
        executeSourceLineTest("ERROR57")
    }

    /**
     * Test data area read callback without define.
     * We expect it to fail with a meaningful error.
     */
    @Test
    fun executeERROR58CallBackTest() {
        executePgmCallBackTest(
            "ERROR58",
            SourceReferenceType.Program,
            "ERROR58",
            mapOf(
                13 to "Data area for definition SCAATTDS not found",
            ),
        )
    }

    @Test
    fun executeERROR58SourceLineTest() {
        executeSourceLineTest("ERROR58")
    }

    /**
     * Test data area write callback without define.
     * We expect it to fail with a meaningful error.
     */
    @Test
    fun executeERROR59CallBackTest() {
        executePgmCallBackTest(
            "ERROR59",
            SourceReferenceType.Program,
            "ERROR59",
            mapOf(
                13 to "Data area for definition SCAATTDS not found",
            ),
        )
    }

    @Test
    fun executeERROR59SourceLineTest() {
        executeSourceLineTest("ERROR59")
    }

    @Test
    fun bypassSyntaxErrorTest() {
        val configuration =
            Configuration().apply {
                options =
                    Options().apply {
                        toAstConfiguration =
                            ToAstConfiguration().apply {
                                // Consider all errors as not blocking
                                afterPhaseErrorContinue = { true }
                            }
                    }
            }
        var myMessage: String? = null
        val systemInterface =
            JavaSystemInterface().apply {
                onDisplay = { message, _ -> myMessage = message.trim() }
            }
        executePgm("ERROR15", configuration = configuration, systemInterface = systemInterface)
        assertEquals(
            expected = "HELLO WORLD!!!",
            actual = myMessage,
            message = "DSPLY must be called because 1 is always equal to 1",
        )
    }

    /**
     * This test simulates what a precompiler might do throws the use of the beforeParsing callback
     * In ERROR01.rpgle I will comment C specification to avoid a division by zero errors
     * */
    @Test
    fun beforeParsingTest() {
        val programName = "ERROR01"
        // the first execution will go wrong
        kotlin
            .runCatching {
                executePgm(programName = programName)
            }.onSuccess {
                Assert.fail("Program must exit with error")
            }
        // Now implement logic where before parsing I will comment C spec
        val configuration =
            Configuration().apply {
                jarikoCallback.beforeParsing = { it.replace("     C ", "     C*") }
            }
        kotlin
            .runCatching {
                executePgm(programName = programName, configuration = configuration)
            }.onFailure {
                Assert.fail("Program must not exit with error")
            }
    }

    @Test
    fun beforeCopyInclusionTest() {
        val expectedIncludedCopies = listOf(CopyId(file = "QILEGEN", member = "TSTCPY01"))
        val includedCopies = mutableListOf<CopyId>()
        val configuration =
            Configuration().apply {
                jarikoCallback =
                    JarikoCallback().apply {
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
        val configuration =
            Configuration().apply {
                options.debuggingInformation = true
                jarikoCallback =
                    JarikoCallback().apply {
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
        val configuration =
            Configuration().apply {
                jarikoCallback.onCallPgmError = { errorEvent ->
                    catchedError = errorEvent
                }
            }
        executePgm(programName = "ERRCALLER", configuration = configuration)
        assertNotNull(catchedError)
    }

    /**
     * Tests the handling of encoding errors during the compilation unit processing.
     * This test simulates a scenario where an encoding error occurs while compiling a program.
     * It sets up callbacks to capture both encoding errors and general errors, then attempts to compile
     * a program with a known encoding issue. The test asserts that both the encoding error and the general
     * error callbacks are triggered, indicating that the error handling mechanisms are functioning as expected.
     * NB: When the encoding errors in ERROR28 are resolved, this test will fail and should be adapted
     * to mock another kind of encoding error
     */
    @Test
    @Ignore(
        "This test is not working because the encoding error in ERROR28.rpgle was " +
            "due to the fact that we will try to compile also ast with errors",
    )
    fun onCompilationUnitEncodingErrorTest() {
        // Flags to track if callbacks are triggered
        var enteredInOnCompilationUnitEncodingError = false
        var enteredInOnError = false

        // Configuration setup with callbacks for encoding errors and general errors
        val configuration =
            Configuration().apply {
                jarikoCallback.onCompilationUnitEncodingError = { _, _, _ ->
                    enteredInOnCompilationUnitEncodingError = true
                }
                jarikoCallback.onError = { _ ->
                    enteredInOnError = true
                }
            }

        // Path to the resources directory containing the program to compile
        val resourcePath = File({}.javaClass.getResource("/smeup/QILEGEN").file).parentFile

        // Attempt to compile the program, expecting an encoding error
        {}.javaClass.getResource("/smeup/ERROR28.rpgle").openStream().use { inputStream ->
            val programFinders = listOf(DirRpgProgramFinder(resourcePath))
            kotlin
                .runCatching {
                    compile(
                        src = inputStream,
                        out = ByteArrayOutputStream(),
                        format = Format.BIN,
                        programFinders = programFinders,
                        configuration = configuration,
                    )
                }.onSuccess {
                    Assert.fail("Program must exit with error")
                }
        }

        // Assert that both encoding and general error callbacks were triggered
        Assert.assertTrue(enteredInOnCompilationUnitEncodingError)
        Assert.assertTrue(enteredInOnError)
    }

    /**
     * Test data area read callback.
     */
    @Test
    fun testDataAreaRead() {
        var readDataArea = ""
        val configuration =
            Configuration().apply {
                jarikoCallback.readDataArea = { dataArea ->
                    readDataArea = dataArea
                    "READ"
                }
            }

        val expected = listOf("READ")
        val actual = "DTAREAREAD".outputOf(configuration = configuration)
        assertEquals(expected, actual)

        assertEquals("C£C£E00D", readDataArea)
    }

    /**
     * Test data area defined in subroutines read callback.
     */
    @Test
    fun testDataAreaReadSubroutine() {
        var readDataArea = ""
        var writeDataArea = ""
        val configuration =
            Configuration().apply {
                jarikoCallback.readDataArea = { dataArea ->
                    readDataArea = dataArea
                    "READ"
                }
                jarikoCallback.writeDataArea = { dataArea, _ ->
                    writeDataArea = dataArea
                }
            }

        executePgm("DTAREASR", configuration = configuration)
        assertEquals("C£C£E00D", readDataArea)
        assertEquals("C£C£E00D", writeDataArea)
    }

    /**
     * Test data area read and write callback on procedures.
     */
    @Test
    fun testDataAreaProcedures() {
        var readDataArea = ""
        var writeDataArea = ""
        var writeNewValue = ""
        val configuration =
            Configuration().apply {
                jarikoCallback.readDataArea = { dataArea ->
                    readDataArea = dataArea
                    "READ"
                }

                jarikoCallback.writeDataArea = { dataArea, value ->
                    writeDataArea = dataArea
                    writeNewValue = value
                }
            }

        executePgm("DTAREAPROC", configuration = configuration)
        assertEquals(readDataArea, "APU001D1")
        assertEquals(writeDataArea, "APU001D1")
        assert(writeNewValue.startsWith("Bar "))
    }

    /**
     * Test data area define inside procedures.
     * It should fail at runtime time when trying to read
     */
    @Test
    fun testDataAreaWithDefineInProcedures() {
        assertFails {
            executePgm("DTAREAINPROC")
        }
    }

    /**
     * Test data area read callback with indicators.
     */
    @Test
    fun testDataAreaReadIndicator() {
        var readDataArea = ""
        val configuration =
            Configuration().apply {
                jarikoCallback.readDataArea = { dataArea ->
                    readDataArea = dataArea
                    throw RuntimeException("Could not find data area")
                }
            }

        val expected = listOf("CURRENT", "1")
        val actual = "DTAREAREADIND".outputOf(configuration = configuration)
        assertEquals(expected, actual)

        assertEquals("C£C£E00D", readDataArea)
    }

    /**
     * Test data area write callback.
     */
    @Test
    fun testDataAreaWrite() {
        var writeDataArea = ""
        var writeNewValue = ""
        val configuration =
            Configuration().apply {
                jarikoCallback.writeDataArea = { dataArea, value ->
                    writeDataArea = dataArea
                    writeNewValue = value
                }
            }

        val expected = listOf("WRITTEN")
        val actual = "DTAREAWRITE".outputOf(configuration = configuration)
        assertEquals(expected, actual)

        assertEquals("C£C£E00D", writeDataArea)
        assertEquals("WRITTEN", writeNewValue.trim())
    }

    /**
     * Test the injection of telemetry spans in DO statements and capture all variables.
     */
    @Test
    fun executeTelemetryCaptures() {
        var captures = emptyMap<String, String>()
        val configuration =
            Configuration()
                .apply {
                    jarikoCallback.finishRpgTrace = { trace ->
                        captures = trace.captures ?: emptyMap()
                    }
                    options.profilingSupport = true
                }

        executePgm("profiling/DO_TELEMETRY_SPAN_CAPTURES", configuration = configuration)

        assertEquals(3, captures.size)
        assertEquals("69", captures.get("RESULT"))
        assertEquals("5", captures.get("A"))
        assertEquals("8", captures.get("B"))
    }

    private fun createMockReloadConfig(): ReloadConfig {
        val metadata =
            mapOf(
                "FILE01" to
                    FileMetadata(
                        name = "FILE01",
                        tableName = "FILE01",
                        recordFormat = "FILE01",
                        fields =
                            listOf(
                                DbField("FIELD1", StringType(10)),
                                DbField("FIELD1", StringType(10)),
                                DbField("FIELD3", StringType(10)),
                            ),
                        accessFields = listOf("FIELD1"),
                    ),
                "FILE02" to
                    FileMetadata(
                        name = "FILE02",
                        tableName = "FILE02",
                        recordFormat = "FILE02",
                        fields =
                            listOf(
                                DbField("FIELD1", StringType(100)),
                                DbField("FIELD1", StringType(10)),
                                DbField("FIELD3", StringType(10)),
                            ),
                        accessFields = listOf("FIELD1"),
                    ),
            )
        val metadataProducer = { file: String -> metadata[file]!! }
        return ReloadConfig(DBNativeAccessConfig(connectionsConfig = emptyList()), metadataProducer = metadataProducer)
    }

    /**
     * Assert that the trace at specified [line] was executed [executions] times.
     *
     * @param line The line of the trace.
     * @param executions The number of executions of the trace.
     */
    private fun List<RpgTrace>.assertExecuted(
        line: Int,
        executions: Int = 1,
    ) {
        val actual = this.count { it.line == line }
        assertEquals(executions, actual, "expected $executions executions but got $actual instead")
    }

    /**
     * Assert that a trace has been executed in a source.
     *
     * @param sourceId The name of the source.
     * @param line The line in which we expect the trace to be executed.
     */
    private fun List<RpgTrace>.assertExecutedInSource(
        sourceId: String,
        line: Int,
    ) {
        val candidate = this.find { it.program == sourceId && it.line == line }
        assertNotNull(candidate, "could not find a trace at line $line executed in $sourceId")
    }

    /**
     * Utility method to easily test trace configurations based on [JarikoTraceKind].
     */
    private fun testTraceConfiguration(
        program: String,
        kinds: List<JarikoTraceKind>,
    ) {
        val traces = mutableListOf<JarikoTrace>()
        var closedCount = 0

        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        val configuration =
            Configuration().apply {
                jarikoCallback.acceptJarikoTrace = { trace -> kinds.contains(trace.kind) }
                jarikoCallback.startJarikoTrace = { trace -> traces.add(trace) }
                jarikoCallback.finishJarikoTrace = { ++closedCount }
            }
        executePgm(program, configuration = configuration, systemInterface = systemInterface)

        assertEquals(traces.size, closedCount, "open traces do not match closed traces")
        assert(traces.all { trace -> kinds.any { kind -> kind == trace.kind } }) { "got unexpected trace kind" }
    }
}
