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

package com.smeup.rpgparser.lexing

import com.smeup.rpgparser.AcceptanceTest
import com.smeup.rpgparser.assertCanBeLexed
import com.smeup.rpgparser.utils.processFilesInDirectory
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.assertEquals

class RpgLexingAcceptanceTest {

    @Test
    @Category(AcceptanceTest::class)
    fun lexAllDataExamples() {
        var failures = 0
        processFilesInDirectory("src/test/resources/data", 19) { rpgFile ->
            try {
                assertCanBeLexed(rpgFile)
            } catch (e: AssertionError) {
                System.err.println("Failed to lex ${rpgFile.absolutePath}: ${e.message}")
                failures++
            }
            assertEquals(0, failures, "Cannot lex some files")
        }
    }
}
