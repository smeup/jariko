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

package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertFileDefinitionIsPresent
import com.smeup.rpgparser.parseFragmentToCompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import org.junit.Test as test

class FileDefinitionTest : AbstractTest() {

    @test fun singleFileParsing() {
        val cu = parseFragmentToCompilationUnit("FQATOCHOST if   e           k disk")
        cu.assertFileDefinitionIsPresent("QATOCHOST")
    }

    @test fun multipleFileParsing() {
        val cu = parseFragmentToCompilationUnit(listOf(
                "FFirst     if   e           k disk ",
                "FSecond    if   e           k disk    rename(TSTREC:TSTREC2)"))
        cu.assertFileDefinitionIsPresent("First")
        cu.assertFileDefinitionIsPresent("Second")
        val secondFileDefinition = cu.getFileDefinition("Second")
        assertEquals("TSTREC2", secondFileDefinition.internalFormatName)
    }

    @Test @Ignore
    fun resolveEXTNAME01() {
        assertASTCanBeProduced(
            exampleName = "db/EXTNAME01",
            considerPosition = true,
            afterAstCreation = { compilationUnit ->
                assertEquals(listOf(), compilationUnit.resolveAndValidate())
            })
    }

    @Test
    fun resolveLIKEDSPEC01() {
        assertASTCanBeProduced(exampleName = "db/LIKEDSPEC01", considerPosition = true, afterAstCreation = { compilationUnit ->
            assertEquals(listOf(), compilationUnit.resolveAndValidate())
        })
    }

    @Test
    fun resolveLIKEDEFINE01() {
        assertASTCanBeProduced(exampleName = "db/LIKEDEFINE01", considerPosition = true, afterAstCreation = { compilationUnit ->
            assertEquals(listOf(), compilationUnit.resolveAndValidate())
        })
    }
}
