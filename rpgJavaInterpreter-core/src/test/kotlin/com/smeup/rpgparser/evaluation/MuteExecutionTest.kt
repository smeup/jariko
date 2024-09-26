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

package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.ExtendedCollectorSystemInterface
import com.smeup.rpgparser.assertNrOfMutesAre
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File
import java.nio.file.Paths
import kotlin.test.*

open class MuteExecutionTest : AbstractTest() {

    @Test
    fun executeSimpleMute() {
        assertMuteExecutionSucceded("mute/SIMPLE_MUTE", 3)
    }

    @Test
    fun executeMuteWithScope() {
        val cu = assertASTCanBeProduced("mute/MUTE01_SCOPE", true, withMuteSupport = true)
        cu.resolveAndValidate()
        cu.assertNrOfMutesAre(5)
        val interpreter = execute(cu, emptyMap())
        assertEquals(5, interpreter.getSystemInterface().getExecutedAnnotation().size)
    }

    @Test
    fun parsingSimpleMuteTimeout() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT", true, withMuteSupport = true)
        cu.resolveAndValidate()
        cu.assertNrOfMutesAre(4)
        assertEquals(2, cu.timeouts.size)
        assertEquals(123, cu.timeouts[0].timeout)
        assertEquals(456, cu.timeouts[1].timeout)
    }

    @Test
    fun executionWithShortTimeoutFails() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT_SHORT", true, withMuteSupport = true)
        cu.resolveAndValidate()
        assertEquals(2, cu.timeouts.size)
        assertEquals(1, cu.timeouts[0].timeout)
        assertEquals(234, cu.timeouts[1].timeout)
        val si = ExtendedCollectorSystemInterface()
        si.programs["Sleep"] =
            object : JvmProgramRaw("Sleep", NumberType(9, 0) parm "millis") {
                override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                    val millis = params["millis"]!!.asDecimal().value.toLong()
                    Thread.sleep(millis)
                    return emptyList()
                }
            }
        try {
            execute(cu, emptyMap(), si)
            fail("No timeout")
        } catch (e: InterpreterTimeoutException) {
            assertTrue(e.toString().contains(cu.timeouts[0].timeout.toString()))
        }
    }

    @Test
    fun executionWithLongTimeoutDoesNotFail() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT_LONG", true, withMuteSupport = true)
        cu.resolveAndValidate()
        assertEquals(1, cu.timeouts.size)
        assertEquals(12345, cu.timeouts[0].timeout)
        execute(cu, emptyMap())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_STATIC_MESSAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_STATIC_MESSAGE", true, withMuteSupport = true)
        cu.resolveAndValidate()
        cu.assertNrOfMutesAre(1)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.getSystemInterface().getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.getSystemInterface().getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
        assertTrue(muteAnnotationExecuted.headerDescription().startsWith("This code should not be executed"))
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_EVALUATED_MESSAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_EVALUATED_MESSAGE", true, withMuteSupport = true)
        cu.resolveAndValidate()
        cu.assertNrOfMutesAre(1)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.getSystemInterface().getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.getSystemInterface().getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
        assertTrue(muteAnnotationExecuted.headerDescription().startsWith("Failure message"))
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_WRONG_USAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_WRONG_USAGE", true, withMuteSupport = true)
        cu.resolveAndValidate()
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.getSystemInterface().getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.getSystemInterface().getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_CORRECT_USAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_CORRECT_USAGE", true, withMuteSupport = true)
        cu.resolveAndValidate()
        val interpreter = execute(cu, emptyMap())
        assertEquals(0, interpreter.getSystemInterface().getExecutedAnnotation().size)
    }

    @Test
    fun executeSSIMPLE_MUTE_FAIL_WITH_IF_AND_STATEMENTS_BEFORE_ENDIF() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_AND_STATEMENTS_BEFORE_ENDIF", true, withMuteSupport = true)
        cu.resolveAndValidate()
        val interpreter = execute(cu, emptyMap())
        assertEquals(0, interpreter.getSystemInterface().getExecutedAnnotation().size)
    }

    @Test
    fun executeMUTE09_04_operations_on_arrays_of_unequal_size_with_MOVEA() {
        assertMuteExecutionSucceded("mute/MUTE09_04", 116)
    }

    @Test
    fun executeMUTE13_01() {
        assertMuteExecutionSucceded("mute/MUTE13_01", 22)
    }

    @Test
    fun executeMUTE13_02() {
        assertMuteExecutionSucceded("mute/MUTE13_02", 17)
    }

    @Test
    fun executeMUTE13_03_IF() {
        assertMuteExecutionSucceded("mute/MUTE13_03_IF", 14)
    }

    @Test
    fun executeMUTE13_03_WHEN() {
        assertMuteExecutionSucceded("mute/MUTE13_03_WHEN", 9)
    }

    @Test
    fun executeMUTE13_04() {
        assertMuteExecutionSucceded("mute/MUTE13_04", 11)
    }

    @Test
    fun executeMUTE13_05_ZSUB() {
        assertMuteExecutionSucceded("mute/MUTE13_05", 11)
    }

    @Test
    fun executeMUTE13_07() {
        // I don't pass the nrOfMuteAssertions parameter
        // because currently is not properly handled this annotation
        // MU* Type="NOXMI"
        assertMuteExecutionSucceded("mute/MUTE13_07")
    }

    @Test
    fun executeMUTE13_08() {
        // I don't pass the nrOfMuteAssertions parameter
        // because currently is not properly handled this annotation
        // MU* Type="NOXMI"
        assertMuteExecutionSucceded("mute/MUTE13_08")
    }

    @Test
    fun executeMUTE13_09_ADD() {
        assertMuteExecutionSucceded("mute/MUTE13_09", 12)
    }

    @Test
    fun executeMUTE13_06_SUB() {
        assertMuteExecutionSucceded("mute/MUTE13_06", 12)
    }

    @Test
    fun executeMUTE13_10() {
        assertMuteExecutionSucceded("mute/MUTE13_10", 8)
    }

    // TODO evaluate if it is a false positive
    @Test
    @Ignore
    fun executeMUTE13_11() {
        // I don't pass nrOfMuteAssertions because if we have a MU* after D spec the function CompilationUnit.assertNrOfMutesAre
        // does not work properly
        assertMuteExecutionSucceded("mute/MUTE13_11")
    }

    @Test
    fun executeMUTE13_13() {
        assertMuteExecutionSucceded("mute/MUTE13_13", 9)
    }

    @Test
    fun executeMUTE13_14() {
        // I don't pass nrOfMuteAssertions because MU* FAIL is not properly handled
        assertMuteExecutionSucceded("mute/MUTE13_14")
    }

    @Test
    fun executeMUTE13_15() {
        assertMuteExecutionSucceded("mute/MUTE13_15", 1)
    }

    @Test
    fun executeMUTE13_16() {
        assertMuteExecutionSucceded("mute/MUTE13_16", 1)
    }

    @Test
    // Simplified version of MUTE09_04 without MOVEA
    fun executeMUTE09_05_operations_on_arrays_of_unequal_size() {
        assertMuteExecutionSucceded("mute/MUTE09_05", 46)
    }

    @Test
    fun executeMUTE09_05_SIMPLE() {
        assertMuteExecutionSucceded("mute/MUTE09_05_SIMPLE", 2)
    }

    @Test @Ignore
    fun executeMUTE15_01_procedures() {
        assertMuteExecutionSucceded("mute/MUTE15_01", 14)
    }

    @Test
    fun executeMUTE13_22_SetOn_SetOff() {
        assertMuteExecutionSucceded("mute/MUTE13_22", 11)
    }

    /* MUTE13_22B was included into MUTE13_22 */
    /*@Test
    fun executeMUTE13_22B_If_test_does_not_change_indicator_value() {
        assertMuteExecutionSucceded("mute/MUTE13_22B", 2)
    }*/

    @Test
    @Ignore
    fun executeMUTE13_23_Lookup_OpCode_Eq() {
        assertMuteExecutionSucceded("mute/MUTE13_23", 8)
    }

    @Test
    fun executeMUTE13_24_Scan_OpCode() {
        assertMuteExecutionSucceded("mute/MUTE13_24", 4)
    }

    @Test
    fun executeMUTE13_25() {
        assertMuteExecutionSucceded("mute/MUTE13_25", 40)
    }

    @Test
    fun executeMUTE13_37() {
        assertMuteExecutionSucceded("mute/MUTE13_37", 24)
    }

    @Test
    fun executeMUTE13_39() {
        assertMuteExecutionSucceded("mute/MUTE13_39", 24)
    }

    @Test
    fun executeMUTE13_38() {
        assertMuteExecutionSucceded("mute/MUTE13_38", 22)
    }

    @Test
    fun executeMUTE13_35() {
        assertMuteExecutionSucceded("mute/MUTE13_35", 10)
    }

    @Test
    fun executeMUTE13_36() {
        assertMuteExecutionSucceded("mute/MUTE13_36", 4)
    }

    @Test
    fun executeMUTE13_10B2() {
        assertMuteExecutionSucceded("mute/MUTE13_10B2", 4)
    }

    @Test
    fun executeMUTE13_10B3() {
        assertMuteExecutionSucceded("mute/MUTE13_10B3", 5)
    }

    @Test
    fun executeMUTE12_08() {
        assertMuteExecutionSucceded("data/ds/MUTE12_08", 16)
    }

    @Test
    fun executeMUTE12_17() {
        assertMuteExecutionSucceded("data/ds/MUTE12_17", 8)
    }

    @Test
    fun executeMUTE12_16() {
        assertMuteExecutionSucceded("data/ds/MUTE12_16", 14)
    }

    @Test
    open fun executeMUTE12_09() {
        assertMuteExecutionSucceded("overlay/MUTE12_09")
    }

    @Test
    fun executeMUTE12_11() {
        assertMuteExecutionSucceded("data/ds/MUTE12_11", 12)
    }

    @Test
    fun executeMUTE12_12() {
        assertMuteExecutionSucceded("data/ds/MUTE12_12", 4)
    }

    @Test
    fun executeMUTE12_13() {
        assertMuteExecutionSucceded("data/ds/MUTE12_13", 4)
    }

    @Test
    fun executeMUTE12_14() {
        assertMuteExecutionSucceded("data/ds/MUTE12_14", 4)
    }

    @Test
    fun executeMUTE12_15() {
        // I don't pass nrOfMuteAssertions because since MUTE12_15 calls other mute which containing mute assertions
        // this check does not work and fixing it is a mess
        assertMuteExecutionSucceded("mute/MUTE12_15")
    }

    @Test
    fun executeMUTE13_17() {
        assertMuteExecutionSucceded("mute/MUTE13_17")
    }

    @Test @Ignore
    fun executeMUTE13_18() {
        assertMuteExecutionSucceded("mute/MUTE13_18")
    }

    @Test
    fun executeMUTE13_19() {
        assertMuteExecutionSucceded("mute/MUTE13_19", 22)
    }

    @Test
    fun executeMUTE13_20() {
        assertMuteExecutionSucceded("mute/MUTE13_20", 9)
    }

    @Test
    fun executeMUTE13_21() {
        assertMuteExecutionSucceded("mute/MUTE13_21", 1)
    }

    @Test @Ignore
    fun executeMUTE13_26() {
        assertMuteExecutionSucceded("mute/MUTE13_26")
    }

    @Test
    fun executeMUTE13_27() {
        assertMuteExecutionSucceded("mute/MUTE13_27")
    }

    @Test
    fun executeMUTE13_28() {
        assertMuteExecutionSucceded("mute/MUTE13_28")
    }

    @Test
    fun executeMUTE13_30() {
        assertMuteExecutionSucceded("mute/MUTE13_30")
    }

    @Test
    fun executeMUTE13_31() {
        executePgm("mute/MUTE13_31", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE13_32() {
        executePgm("mute/MUTE13_32", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE13_33() {
        executePgm("mute/MUTE13_33", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE13_34() {
        executePgm("mute/MUTE13_34", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE13_41() {
        val expected = listOf("20230930", "113123", "20230930", "113233", "20230930", "113312")
        assertEquals(expected, "mute/MUTE13_41".outputOf())
    }

    @Test
    fun executeMUTE15_01() {
        executePgm("mute/MUTE15_01", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    @Ignore
    // TODO ("Fix: Errors at line: 1 messages: token recognition error at: ''',token recognition error at: ']'")
    // The problem is mutelexer.g4 which is no able to lex this annotation MU* VAL1(RIS1) VAL2('[T1]') COMP(EQ)
    // Maybe it's the presence of square brackets
    fun executeMUTE15_02() {
        executePgm("mute/MUTE15_02", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_03() {
        executePgm("mute/MUTE15_03", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    @Ignore
    // TODO ("Fix: Mute annotation at line 33 failed - ELEM = "5" - Left value 0  - right value 5 - Line 34")
    fun executeMUTE15_04() {
        executePgm("mute/MUTE15_04", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    @Ignore
    // TODO ("Fix: Issue executing ExecuteSubroutine at line 17. Program MUTE15_05 - Issue executing EvalStmt at line 27. Program <UNSPECIFIED> - Issue executing CallStmt at line 48. Data definition XXSTR was not found")
    fun executeMUTE15_05() {
        executePgm("mute/MUTE15_05", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_06() {
        executePgm("mute/MUTE15_06", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_07() {
        executePgm("mute/MUTE15_07", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_08() {
        executePgm("mute/MUTE15_08", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_09() {
        executePgm("mute/MUTE15_09", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_10() {
        val configuration = Configuration().apply {
            options = Options(muteSupport = true, dumpSourceOnExecutionError = false)
        }
        executePgm("mute/MUTE15_10", configuration = configuration, systemInterface = JavaSystemInterface())
    }

    @Test
    fun executeMUTE15_11() {
        executePgm("mute/MUTE15_11", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_12() {
        executePgm("mute/MUTE15_12", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_13() {
        executePgm("mute/MUTE15_13", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_14() {
        executePgm("mute/MUTE15_14", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE15_15() {
        executePgm("mute/MUTE15_15", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE18_01() {
        executePgm("mute/MUTE18_01", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE18_02() {
        executePgm("mute/MUTE18_02", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE18_03() {
        executePgm("mute/MUTE18_03", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE18_04() {
        executePgm("mute/MUTE18_04", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE01_07() {
        executePgm("mute/MUTE01_07", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    @Test
    fun executeMUTE12_06() {
        executePgm("mute/MUTE12_06", configuration = Configuration().apply { options = Options(muteSupport = true) })
    }

    private fun assertMuteExecutionSucceded(
        exampleName: String,
        // if null ignores mutes number assertions check
        nrOfMuteAssertions: Int? = null,
        parameters: Map<String, Value> = emptyMap()
    ) {
        val cu = assertASTCanBeProduced(exampleName, true, withMuteSupport = true)
        cu.resolveAndValidate()
        nrOfMuteAssertions?.let { cu.assertNrOfMutesAre(it) }
        val relativePath = Paths.get(exampleName).parent
        val examplePath = Paths.get("src", "test", "resources").resolve(relativePath)
        val systemInterface = JavaSystemInterface().apply {
            rpgSystem.addProgramFinder(DirRpgProgramFinder(examplePath.toFile()))
            // to include copy
            rpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/")))
        }
        val configuration = Configuration().apply {
            options.muteSupport = true
        }
        val interpreter = MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            it.executionProgramName = exampleName
            execute(cu, parameters, systemInterface = systemInterface, programName = exampleName)
        }
        nrOfMuteAssertions?.let { assertEquals(nrOfMuteAssertions, interpreter.getSystemInterface().getExecutedAnnotation().size) }
        interpreter.getSystemInterface().getExecutedAnnotation().forEach {
            assertTrue(it.value.succeeded(), "Mute assertion failed - ${it.value.programName}: ${it.value.headerDescription()}")
        }
    }
}
