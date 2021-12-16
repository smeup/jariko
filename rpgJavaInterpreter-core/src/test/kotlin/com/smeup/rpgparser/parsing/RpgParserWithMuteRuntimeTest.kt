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

package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.execution.ResourceProgramFinder
import com.smeup.rpgparser.interpreter.DummySystemInterface
import com.smeup.rpgparser.interpreter.SimpleSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

open class RpgParserWithMuteRuntimeTest : AbstractTest() {

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
        cu.resolveAndValidate()
        DummySystemInterface.executedAnnotationInternal.clear()
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.getSystemInterface().getExecutedAnnotation().size, 8)

        // VAL1(FIELD1) VAL2('AAAA') COMP(EQ)
        var annotation = interpreter.getSystemInterface().getExecutedAnnotation()[3]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(NBR) VAL2(11) COMP(LT)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[7]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(FIELD1) VAL2('A ' + ' B') COMP(NE)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[14]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(GE)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[16]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(LE)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[17]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(GT)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[19]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.succeeded())

        // VAL1(B) VAL2(1) COMP(LT)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[20]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.succeeded())

        // VAL1(COUNT) VAL2(4) COMP(LE)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[28]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())
    }

    @Test
    fun parseMUTE02_runtime() {
        DummySystemInterface.executedAnnotationInternal.clear()
        val cu = assertASTCanBeProduced("mute/MUTE02_RUNTIME", true)
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.getSystemInterface().getExecutedAnnotation().size, 5)

        // VAL1(VAR1) VAL2(%TRIM(' AAAA ')) COMP(EQ)
        var annotation = interpreter.getSystemInterface().getExecutedAnnotation()[3]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        //  VAL1(VALUE1) VAL2('AAA:') COMP(EQ)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[10]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        //  VAL1(VALUE1) VAL2('  AAA:') COMP(EQ)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[12]
        assertTrue(actual = annotation != null)
        // this one fail, as expected
        assertFalse(annotation.succeeded())

        //  VAL1(%TRIMR(VAR1) +':') VAL2('  AAA:') COMP(EQ)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[14]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())

        //  VAL1(VALUE1) VAL2('AAA                         :') COMP(NE)
        annotation = interpreter.getSystemInterface().getExecutedAnnotation()[15]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())
    }

    @Test
    fun parseMUTE02_runtimeWithArray() {
        DummySystemInterface.executedAnnotationInternal.clear()
        val cu = assertASTCanBeProduced("mute/MUTE02_RUNTIME_array", true)
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.getSystemInterface().getExecutedAnnotation().size, 1)

        // VAL1(AR(1)) VAL2(4) COMP(NE)
        val annotation = interpreter.getSystemInterface().getExecutedAnnotation()[2]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.succeeded())
    }

    @Test
    fun executingFIZZBUZZTEST() {
        val cu = assertASTCanBeProduced("mute/FIZZBUZZTEST", true)
        cu.resolveAndValidate()
        val si = SimpleSystemInterface(programFinders = listOf(ResourceProgramFinder("/mute/")))

        val interpreter = execute(cu, mapOf(), systemInterface = si)

        val executedAnnotation = interpreter.getSystemInterface().getExecutedAnnotation()
        assertEquals(executedAnnotation.size, 4)
        assertTrue(executedAnnotation.all { it.value.succeeded() })
    }

    @Test
    fun executingGOTOexecutesMutesToo() {
        val cu = assertASTCanBeProduced("mute/MUTEGOTO", true)
        cu.resolveAndValidate()

        val interpreter = execute(cu, mapOf())

        val executedAnnotation = interpreter.getSystemInterface().getExecutedAnnotation()
        assertEquals(executedAnnotation.size, 2)
        assertTrue(executedAnnotation.all { it.value.succeeded() })
    }
}
