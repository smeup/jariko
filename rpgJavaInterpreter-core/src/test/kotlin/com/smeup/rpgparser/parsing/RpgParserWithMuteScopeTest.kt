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
import com.smeup.rpgparser.assertCanBeParsedResult
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RpgParserWithMuteScopeTest : AbstractTest() {
    var printResults: Boolean = true

    // Useful display function for debugging
    private fun showResults(resolved: List<MuteAnnotationResolved>) {
        if (this.printResults) {
            val sorted = resolved.sortedWith(compareBy({ it.muteLine }))
            sorted.forEach {
                println("Mute at line ${it.muteLine} attached to statement ${it.statementLine}")
            }
        }
    }

    private fun getResolvedAnnotation(
        line: Int,
        annotations: List<MuteAnnotationResolved>,
    ): MuteAnnotationResolved? {
        annotations.forEach {
            if (it.muteLine == line) {
                return it
            }
        }
        return null
    }

    @Test
    fun parseMUTE01_scope() {
        val resolved: List<MuteAnnotationResolved>
        val result = assertCanBeParsedResult("mute/MUTE01_SCOPE", withMuteSupport = true)

        result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)
        }

        showResults(resolved)

        assertEquals(resolved.size, 5)

        // Data definitions
        var annotation = getResolvedAnnotation(3, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 4)

        annotation = getResolvedAnnotation(10, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 9)

        annotation = getResolvedAnnotation(15, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 17)

        annotation = getResolvedAnnotation(16, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 17)

        annotation = getResolvedAnnotation(23, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 24)
    }
}
