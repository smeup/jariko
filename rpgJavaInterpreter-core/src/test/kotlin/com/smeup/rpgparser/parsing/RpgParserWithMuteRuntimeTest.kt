package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.AbstractTestCase
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.execution.ResourceProgramFinder
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.interpreter.DummySystemInterface
import com.smeup.rpgparser.interpreter.SimpleSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

open class RpgParserWithMuteRuntimeTest : AbstractTestCase() {

    fun assertASTCanBeProduced(
        exampleName: String,
        considerPosition: Boolean
    ): CompilationUnit {
        return super.assertASTCanBeProduced(
            exampleName = exampleName,
            considerPosition = considerPosition,
            withMuteSupport = true,
            printTree = false
        )
    }

    @Test
    fun parseMUTE01_runtime() {
        val cu = assertASTCanBeProduced("mute/MUTE01_RUNTIME", true)
        cu.resolveAndValidate(DummyDBInterface)
        DummySystemInterface.executedAnnotationInternal.clear()
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.systemInterface.getExecutedAnnotation().size, 8)

        // VAL1(FIELD1) VAL2('AAAA') COMP(EQ)
        var annotation = interpreter.systemInterface.getExecutedAnnotation()[3]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(NBR) VAL2(11) COMP(LT)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[7]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(FIELD1) VAL2('A ' + ' B') COMP(NE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[14]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(GE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[16]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(LE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[17]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(GT)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[19]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(LT)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[20]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.succeeded())

        // VAL1(COUNT) VAL2(4) COMP(LE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[28]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())
    }

    @Test
    fun parseMUTE02_runtime() {
        DummySystemInterface.executedAnnotationInternal.clear()
        val cu = assertASTCanBeProduced("mute/MUTE02_RUNTIME", true)
        cu.resolveAndValidate(DummyDBInterface)
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.systemInterface.getExecutedAnnotation().size, 5)

        // VAL1(VAR1) VAL2(%TRIM(' AAAA ')) COMP(EQ)
        var annotation = interpreter.systemInterface.getExecutedAnnotation()[3]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        //  VAL1(VALUE1) VAL2('AAA:') COMP(EQ)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[10]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        //  VAL1(VALUE1) VAL2('  AAA:') COMP(EQ)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[12]
        assertTrue(actual = annotation != null)
        // this one fail, as expected
        assertFalse(annotation.succeeded())

        //  VAL1(%TRIMR(VAR1) +':') VAL2('  AAA:') COMP(EQ)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[14]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        //  VAL1(VALUE1) VAL2('AAA                         :') COMP(NE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[15]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())
    }

    @Test
    fun parseMUTE02_runtimeWithArray() {
        DummySystemInterface.executedAnnotationInternal.clear()
        val cu = assertASTCanBeProduced("mute/MUTE02_RUNTIME_array", true)
        cu.resolveAndValidate(DummyDBInterface)
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.systemInterface.getExecutedAnnotation().size, 1)

        // VAL1(AR(1)) VAL2(4) COMP(NE)
        val annotation = interpreter.systemInterface.getExecutedAnnotation()[2]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())
    }

    @Test
    fun executingFIZZBUZZTEST() {
        val cu = assertASTCanBeProduced("mute/FIZZBUZZTEST", true)
        cu.resolveAndValidate(DummyDBInterface)
        val si = SimpleSystemInterface(programFinders = listOf(ResourceProgramFinder("/mute/")))

        val interpreter = execute(cu, mapOf(), systemInterface = si)

        val executedAnnotation = interpreter.systemInterface.getExecutedAnnotation()
        assertEquals(executedAnnotation.size, 4)
        assertTrue(executedAnnotation.all { it.value.succeeded() })
    }

    @Test
    fun executingGOTOexecutesMutesToo() {
        val cu = assertASTCanBeProduced("mute/MUTEGOTO", true)
        cu.resolveAndValidate(DummyDBInterface)

        val interpreter = execute(cu, mapOf())

        val executedAnnotation = interpreter.systemInterface.getExecutedAnnotation()
        assertEquals(executedAnnotation.size, 2)
        assertTrue(executedAnnotation.all { it.value.succeeded() })
    }
}
