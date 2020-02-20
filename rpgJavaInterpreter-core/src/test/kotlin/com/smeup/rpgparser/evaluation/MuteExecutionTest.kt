@file:Suppress("DEPRECATION")
package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.ExtendedCollectorSystemInterface
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertNrOfMutesAre
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import java.util.concurrent.TimeoutException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.test.Ignore

class MuteExecutionTest {

    @Test
    fun executeSimpleMute() {
        assertMuteExecutionSucceded("mute/SIMPLE_MUTE", 3)
    }

    @Test
    fun executeMuteWithScope() {
        val cu = assertASTCanBeProduced("mute/MUTE01_SCOPE", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        cu.assertNrOfMutesAre(5)
        val interpreter = execute(cu, emptyMap())
        assertEquals(5, interpreter.systemInterface.getExecutedAnnotation().size)
    }

    @Test
    fun parsingSimpleMuteTimeout() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        cu.assertNrOfMutesAre(4)
        assertEquals(2, cu.timeouts.size)
        assertEquals(123, cu.timeouts[0].timeout)
        assertEquals(456, cu.timeouts[1].timeout)
    }

    @Test
    fun executionWithShortTimeoutFails() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT_SHORT", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
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
        } catch (e: TimeoutException) {
            assertTrue(e.toString().contains(cu.timeouts[0].timeout.toString()))
        }
    }

    @Test
    fun executionWithLongTimeoutDoesNotFail() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT_LONG", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        assertEquals(1, cu.timeouts.size)
        assertEquals(12345, cu.timeouts[0].timeout)
        execute(cu, emptyMap())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_STATIC_MESSAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_STATIC_MESSAGE", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        cu.assertNrOfMutesAre(1)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.systemInterface.getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.systemInterface.getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
        assertTrue(muteAnnotationExecuted.headerDescription().startsWith("This code should not be executed"))
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_EVALUATED_MESSAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_EVALUATED_MESSAGE", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        cu.assertNrOfMutesAre(1)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.systemInterface.getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.systemInterface.getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
        assertTrue(muteAnnotationExecuted.headerDescription().startsWith("Failure message"))
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_WRONG_USAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_WRONG_USAGE", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.systemInterface.getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.systemInterface.getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_CORRECT_USAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_CORRECT_USAGE", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        val interpreter = execute(cu, emptyMap())
        assertEquals(0, interpreter.systemInterface.getExecutedAnnotation().size)
    }

    @Test
    fun executeSSIMPLE_MUTE_FAIL_WITH_IF_AND_STATEMENTS_BEFORE_ENDIF() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_AND_STATEMENTS_BEFORE_ENDIF", true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        val interpreter = execute(cu, emptyMap())
        assertEquals(0, interpreter.systemInterface.getExecutedAnnotation().size)
    }

    // FIXME: We need to implement MOVEA
    @Test @Ignore
    fun executeMUTE09_04_operations_on_arrays_of_unequal_size_with_MOVEA() {
        assertMuteExecutionSucceded("mute/MUTE09_04", 115)
    }

    @Test
    fun executeMUTE13_13() {
        assertMuteExecutionSucceded("mute/MUTE13_13", 9)
    }

    @Test
    //e (simplified version of MUTE09_04 without MOVEA)
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

    private fun assertMuteExecutionSucceded(
        exampleName: String,
        nrOfMuteAssertions: Int,
        parameters: Map<String, Value> = emptyMap()
    ) {
        val cu = assertASTCanBeProduced(exampleName, true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)
        cu.assertNrOfMutesAre(nrOfMuteAssertions)
        val interpreter = execute(cu, parameters)
        assertEquals(nrOfMuteAssertions, interpreter.systemInterface.getExecutedAnnotation().size)
        interpreter.systemInterface.getExecutedAnnotation().forEach {
            assertTrue(it.value.succeeded(), "Mute assertion failed: ${it.value.headerDescription()}")
        }
    }
}
