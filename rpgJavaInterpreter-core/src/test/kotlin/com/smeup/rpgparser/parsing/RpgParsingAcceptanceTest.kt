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

import com.smeup.rpgparser.AcceptanceTest
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.utils.processFilesInDirectory
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.assertEquals

class RpgParsingAcceptanceTest {

    @Test
    @Category(AcceptanceTest::class)
    fun parseAllDataExamples() {
        var failures = 0
        processFilesInDirectory("src/test/resources/data", 19) { rpgFile ->
            try {
                assertCanBeParsed(rpgFile)
            } catch (e: AssertionError) {
                System.err.println("Failed to parse ${rpgFile.absolutePath}: ${e.message}")
                failures++
            }
            assertEquals(0, failures, "Cannot parse some files")
        }
    }
}
