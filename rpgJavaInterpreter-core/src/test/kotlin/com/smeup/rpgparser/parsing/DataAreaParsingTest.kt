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
import com.smeup.rpgparser.parsing.ast.InStmt
import com.smeup.rpgparser.parsing.ast.OutStmt
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests that IN and OUT statements can be parsed correctly
 */
class DataAreaParsingTest : AbstractTest() {

    @Test
    fun testInStatementParsing() {
        val cu = assertASTCanBeProduced("DTAREAREAD")
        cu.resolveAndValidate()
        
        // Find IN statements in the AST
        val inStatements = cu.main?.stmts?.filterIsInstance<InStmt>() ?: emptyList()
        assertTrue(inStatements.isNotEmpty(), "Should have found at least one IN statement")
        
        val inStmt = inStatements.first()
        assertNotNull(inStmt.dataAreaName, "IN statement should have data area name")
        assertNotNull(inStmt.target, "IN statement should have target field")
        
        println("Successfully parsed IN statement with data area and target")
    }

    @Test
    fun testOutStatementParsing() {
        val cu = assertASTCanBeProduced("DTAREAWRITE")
        cu.resolveAndValidate()
        
        // Find OUT statements in the AST
        val outStatements = cu.main?.stmts?.filterIsInstance<OutStmt>() ?: emptyList()
        assertTrue(outStatements.isNotEmpty(), "Should have found at least one OUT statement")
        
        val outStmt = outStatements.first()
        assertNotNull(outStmt.dataAreaName, "OUT statement should have data area name")
        assertNotNull(outStmt.source, "OUT statement should have source field")
        
        println("Successfully parsed OUT statement with data area and source")
    }

    @Test
    fun testInStatementWithIndicator() {
        val cu = assertASTCanBeProduced("DTAREAREADIND")
        cu.resolveAndValidate()
        
        // Find IN statements with indicators
        val inStatements = cu.main?.stmts?.filterIsInstance<InStmt>() ?: emptyList()
        assertTrue(inStatements.isNotEmpty(), "Should have found at least one IN statement")
        
        val inStmt = inStatements.first()
        assertNotNull(inStmt.errorIndicator, "IN statement should have error indicator")
        assertEquals("50", inStmt.errorIndicator.toString(), "Error indicator should be 50")
        
        println("Successfully parsed IN statement with error indicator")
    }
    
    @Test
    fun testBasicInOutSyntax() {
        // Test that our implementation can handle the basic RPG syntax
        val testPrograms = listOf("DTAREAREAD", "DTAREAWRITE", "DTAREAREADIND")
        
        testPrograms.forEach { programName ->
            val cu = assertASTCanBeProduced(programName)
            cu.resolveAndValidate()
            
            println("Successfully parsed and validated $programName")
        }
    }
}