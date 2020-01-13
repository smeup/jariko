@file:Suppress("DEPRECATION")
package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.ExtendedCollectorSystemInterface
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertNrOfMutesAre
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test
import java.util.concurrent.TimeoutException
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class MuteExecutionTest {

    @Test
    fun executeSimpleMute() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        cu.assertNrOfMutesAre(3)
        val interpreter = execute(cu, emptyMap())
        assertEquals(3, interpreter.systemInterface.getExecutedAnnotation().size)
        interpreter.systemInterface.getExecutedAnnotation().forEach {
            assertEquals(BooleanValue(true), it.value.result)
        }
    }

    @Test
    fun executeMuteWithScope() {
        val cu = assertASTCanBeProduced("mute/MUTE01_SCOPE", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        cu.assertNrOfMutesAre(5)
        val interpreter = execute(cu, emptyMap())
        assertEquals(5, interpreter.systemInterface.getExecutedAnnotation().size)
    }

    @Test
    fun parsingSimpleMuteTimeout() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        cu.assertNrOfMutesAre(4)
        assertEquals(2, cu.timeouts.size)
        assertEquals(123, cu.timeouts[0].timeout)
        assertEquals(456, cu.timeouts[1].timeout)
    }

    @Test
    fun executionWithShortTimeoutFails() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_TIMEOUT_SHORT", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
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
        cu.resolve(DummyDBInterface)
        assertEquals(1, cu.timeouts.size)
        assertEquals(12345, cu.timeouts[0].timeout)
        execute(cu, emptyMap())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_STATIC_MESSAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_STATIC_MESSAGE", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        cu.assertNrOfMutesAre(1)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.systemInterface.getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.systemInterface.getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
        assertEquals("This code should not be executed", muteAnnotationExecuted.headerDescription())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_EVALUATED_MESSAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_EVALUATED_MESSAGE", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        cu.assertNrOfMutesAre(1)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.systemInterface.getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.systemInterface.getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
        assertEquals("Failure message", muteAnnotationExecuted.headerDescription())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_WRONG_USAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_WRONG_USAGE", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        val interpreter = execute(cu, emptyMap())
        assertEquals(1, interpreter.systemInterface.getExecutedAnnotation().size)
        val muteAnnotationExecuted = interpreter.systemInterface.getExecutedAnnotation().values.first()
        assertFalse(muteAnnotationExecuted.succeeded())
    }

    @Test
    fun executeSIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_CORRECT_USAGE() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_NO_STATEMENTS_BEFORE_ENDIF_CORRECT_USAGE", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        val interpreter = execute(cu, emptyMap())
        assertEquals(0, interpreter.systemInterface.getExecutedAnnotation().size)
    }

    @Test
    fun executeSSIMPLE_MUTE_FAIL_WITH_IF_AND_STATEMENTS_BEFORE_ENDIF() {
        val cu = assertASTCanBeProduced("mute/SIMPLE_MUTE_FAIL_WITH_IF_AND_STATEMENTS_BEFORE_ENDIF", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        val interpreter = execute(cu, emptyMap())
        assertEquals(0, interpreter.systemInterface.getExecutedAnnotation().size)
    }

    // FIXME: We need to implement MOVEA
    @Test @kotlin.test.Ignore
    // this program test operations on arrays of unequal size
    fun executeMUTE09_04() {
        val cu = assertASTCanBeProduced("mute/MUTE09_04", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        cu.assertNrOfMutesAre(115)
        val interpreter = execute(cu, emptyMap())
        assertEquals(115, interpreter.systemInterface.getExecutedAnnotation().size)
        interpreter.systemInterface.getExecutedAnnotation().forEach {
            assertEquals(BooleanValue(true), it.value.result)
        }
    }
    @Test @kotlin.test.Ignore
    // this program test operations on arrays of unequal size (simplified version of MUTE09_04 without MOVEA)
    fun executeMUTE09_05() {
        val cu = assertASTCanBeProduced("mute/MUTE09_05", true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        cu.assertNrOfMutesAre(115)
        val interpreter = execute(cu, emptyMap())
        assertEquals(115, interpreter.systemInterface.getExecutedAnnotation().size)
        interpreter.systemInterface.getExecutedAnnotation().forEach {
            assertEquals(BooleanValue(true), it.value.result)
        }
    }
}
