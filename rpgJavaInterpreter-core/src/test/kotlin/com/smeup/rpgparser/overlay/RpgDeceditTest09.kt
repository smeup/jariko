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
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.DecEdit
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.interpreter.LocalizationContext
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test

open class RpgDeceditTest09 : AbstractTest() {

    @Test
    fun parseMUTE09_02() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.getStatus().displayFiles = cu.displayFiles

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE09_02_comma() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02_COMMA", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val localizationContext = LocalizationContext(decedit = DecEdit.COMMA)
        val interpreter = InternalInterpreter(JavaSystemInterface(), localizationContext)
        interpreter.getStatus().displayFiles = cu.displayFiles
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE09_06() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_06", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()
        val localizationContext = LocalizationContext(decedit = DecEdit.ZERO_COMMA)
        val interpreter = InternalInterpreter(JavaSystemInterface(), localizationContext)
        interpreter.getStatus().displayFiles = cu.displayFiles
        // Changes the default decedit
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
