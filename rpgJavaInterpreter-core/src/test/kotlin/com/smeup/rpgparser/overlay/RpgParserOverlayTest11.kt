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

package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

open class RpgParserOverlayTest11 : AbstractTest() {

    private fun createSystemInterface(): SystemInterface {
        return JavaSystemInterface().apply {
            rpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/overlay")))
        }
    }

    @Test
    fun parseMUTE11_11C_syntax() {
        assertCanBeParsed("overlay/MUTE11_11C", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_ast() {
        assertASTCanBeProduced("overlay/MUTE11_11C", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_11C", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(createSystemInterface())
        interpreter.getStatus().displayFiles = cu.displayFiles
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE11_15_syntax() {
        assertCanBeParsed("overlay/MUTE11_15", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_15_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_15", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val FUND1 = cu.getDataDefinition("£FUND1")
        val FUNQT = FUND1.getFieldByName("£FUNQT")
        assertEquals(Pair(442, 457), FUNQT.offsets)
        assertEquals(NumberType(entireDigits = 10, decimalDigits = 5, rpgType = "S"), FUNQT.type)
        assertEquals(15, FUNQT.size)
    }

    @Test
    fun parseMUTE11_15_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_15", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(createSystemInterface())
        interpreter.getStatus().displayFiles = cu.displayFiles
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE11_16_syntax() {
        assertCanBeParsed("overlay/MUTE11O16", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_16_ast() {
        assertASTCanBeProduced("overlay/MUTE11O16", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_16_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11O16", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(createSystemInterface())
        interpreter.getStatus().displayFiles = cu.displayFiles
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
