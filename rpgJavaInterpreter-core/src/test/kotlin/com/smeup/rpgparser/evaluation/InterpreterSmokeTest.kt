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

package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Before
import org.junit.Test

open class InterpreterSmokeTest : AbstractTest() {

    @Before
    fun resetDefaultConfigAttributes() {
        // It is necessary to fix a problem where if a smoke test fails the errors are propagated to all unit tests
        MainExecutionContext.getAttributes().clear()
    }

    @Test
    fun executeJD_001() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolveAndValidate()
        execute(cu, mapOf())
        // We need JD_URL in Kotlin. We want it to print its third parameter
    }

    @Test
    fun executeJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    @Test
    fun executeJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    @Test
    fun executeMOVEA01() {
        val cu = assertASTCanBeProduced("MOVEA01")
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    @Test
    fun executeINLINESPEC() {
        val cu = assertASTCanBeProduced(exampleName = "INLINESPEC")
        cu.resolveAndValidate()
    }
}
