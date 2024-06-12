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
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.junit.Test
import java.io.File

open class RpgParserOverlayTest03 : AbstractTest() {

    @Test
    fun parseMUTE03_09_syntax() {
        assertCanBeParsed("overlay/MUTE03_09", withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_ast() {
        assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09B_ast() {
        assertASTCanBeProduced("overlay/MUTE03_09B", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(JavaSystemInterface().apply {
            rpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/overlay")))
        })
        interpreter.getStatus().displayFiles = cu.displayFiles
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
