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
import com.smeup.rpgparser.interpreter.DataStructureType
import com.smeup.rpgparser.interpreter.Scope
import com.smeup.rpgparser.parsing.parsetreetoast.DSFieldInitKeywordType
import org.junit.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class ToAstSmokeTest : AbstractTest() {

    @Test
    fun buildAstForJD_001() {
        val cu = assertASTCanBeProduced("JD_001")
        assertEquals(10, cu.dataDefinitions.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(7, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        assertEquals(18, cu.dataDefinitions.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(10, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        assertEquals(16, cu.dataDefinitions.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(6, cu.subroutines.size)
    }

    @Test
    fun buildAstForJCODFISS() {
        val cu = assertASTCanBeProduced("JCODFISS")
        assertEquals(0, cu.dataDefinitions.size)
        assertEquals(2, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_001_dataDefinitions() {
        val root = assertASTCanBeProduced("JD_001")
        assertEquals(10, root.dataDefinitions.size)
        assertEquals("@UNNAMED_DS_16", root.dataDefinitions[0].name)
        assertEquals("U\$FUNZ", root.dataDefinitions[1].name)
        assertEquals("U\$METO", root.dataDefinitions[2].name)
        assertEquals("U\$SVARSK", root.dataDefinitions[3].name)
        assertEquals("U\$IN35", root.dataDefinitions[4].name)
        assertEquals("\$\$URL", root.dataDefinitions[5].name)
        assertEquals("\$X", root.dataDefinitions[6].name)
        assertEquals("U\$SVARSK_INI", root.dataDefinitions[7].name)
        assertEquals("§§FUNZ", root.dataDefinitions[8].name)
        assertEquals("§§METO", root.dataDefinitions[9].name)
    }

    @Test
    fun buildAstForJD_001_subroutines() {
        val root = assertASTCanBeProduced("JD_001")
        assertEquals(7, root.subroutines.size)
        assertEquals("£INIZI", root.subroutines[0].name)
        assertEquals(1, root.subroutines[0].stmts.size)
        assertEquals("IMP0", root.subroutines[1].name)
        assertEquals(1, root.subroutines[1].stmts.size)
        assertEquals("FIN0", root.subroutines[2].name)
        assertEquals(0, root.subroutines[2].stmts.size)
        assertEquals("FINZ", root.subroutines[3].name)
        assertEquals(2, root.subroutines[3].stmts.size)
        assertEquals("FESE", root.subroutines[4].name)
        assertEquals(3, root.subroutines[4].stmts.size)
        assertEquals("REPVAR", root.subroutines[5].name)
        assertEquals(1, root.subroutines[5].stmts.size)
        assertEquals("FCLO", root.subroutines[6].name)
        assertEquals(0, root.subroutines[6].stmts.size)
    }

    @Test
    fun buildAstForHELLO() {
        val cu = assertASTCanBeProduced("HELLO")
        assertEquals(1, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForCALCFIBCAL() {
        val cu = assertASTCanBeProduced("CALCFIBCAL")
        assertEquals(1, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForCALCFIBCA2() {
        val cu = assertASTCanBeProduced("CALCFIBCA2")
        assertEquals(0, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForCALCFIBCA3() {
        val cu = assertASTCanBeProduced("CALCFIBCA3")
        assertEquals(0, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForHELLOTYPE() {
        val cu = assertASTCanBeProduced("HELLOTYPE")
        assertEquals(2, cu.dataDefinitions.size)
        assertEquals(6, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForHELLOERROR() {
        val cu = assertASTCanBeProduced("HELLOERROR")
        assertEquals(1, cu.dataDefinitions.size)
        assertEquals(5, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForTIMESTDIFF() {
        val cu = assertASTCanBeProduced("TIMESTDIFF")
        assertEquals(5, cu.dataDefinitions.size)
        assertEquals(6, cu.main.stmts.size)
        assertEquals(1, cu.subroutines.size)
    }

    @Test
    fun buildAstForMUTE10_01() {
        assertASTCanBeProduced("performance/MUTE10_01")
        assertASTCanBeProduced("performance/MUTE10_01A")
        assertASTCanBeProduced("performance/MUTE10_01B")
        assertASTCanBeProduced("performance/MUTE10_01C")
    }

    @Test
    fun buildAstForREADP() {
        assertASTCanBeProduced("db/READP")
    }

    @Test
    fun buildAstForACTGRP_FIX() {
        val cu = assertASTCanBeProduced("ACTGRP_FIX")
        assertEquals(firstActivationGroupDirective(cu).type, NamedActivationGroup("MYACT"))
    }

    @Test
    fun buildAstForACTGRP_NEW() {
        val cu = assertASTCanBeProduced("ACTGRP_NEW")
        assertEquals(firstActivationGroupDirective(cu).type, NewActivationGroup)
    }

    @Test
    fun buildAstForACTGRP_CALLER() {
        val cu = assertASTCanBeProduced("ACTGRP_CLR")
        assertEquals(firstActivationGroupDirective(cu).type, CallerActivationGroup)
    }

    private fun firstActivationGroupDirective(cu: CompilationUnit): ActivationGroupDirective {
        assertTrue(cu.directives.size >= 1)
        val directive = cu.directives[0]
        assertTrue(directive is ActivationGroupDirective)
        return directive
    }

    /**
     * We need to be able to create AST for sources containing just D specifications
     * */
    @Test
    fun buildAstForMU1DSPEC() {
        assert(assertASTCanBeProduced("£MU1DSPEC").dataDefinitions.size == 2)
    }

    /**
     * We need to be able to create AST for sources containing just C specifications
     * */
    @Test
    fun buildAstForMU1CSPEC() {
        assert(assertASTCanBeProduced("£MU1CSPEC").subroutines.size == 1)
    }

    /**
     * We need to be able to create AST for sources containing /COPY directive and to verify
     * that the resulting AST contains all AST fragments provided by included copies
     * */
    @Test
    fun buildAstForMU1API() {
        val cu = assertASTCanBeProduced("£MU1API")
        assert(cu.dataDefinitions.size == 2)
    }

    /**
     * We need to be able to create AST for sources containing /COPY directive and to verify
     * that the resulting AST contains all AST fragments provided by included copies
     * */
    @Test
    fun buildAstForMU1PGM() {
        val cu = assertASTCanBeProduced("£MU1PGM", considerPosition = true)
        assert(cu.subroutines.size == 4)
        assert(cu.dataDefinitions.size == 2)
    }

    // TODO fix
    // java.lang.IllegalArgumentException: Start offset not calculated for fields £G64P1, £G64P2, £G64TC, £G64CS, £G64DC
    // at com.smeup.rpgparser.parsing.parsetreetoast.Data_definitionsKt.calculateFieldInfos(data_definitions.kt:678)
    /**
     * This error has been already classified earlier as [DS-OVERLAY](https://docs.google.com/spreadsheets/d/1x05ATX9lcJLL7s1sNpZawBKC1Zz7lP--V7xqOZ-wBbk/edit#gid=36284680&range=E25)
     * Earlier this error was hidden and then the ast creating apparently worked properly
     * */
    @Test
    @Ignore
    fun buildAstForLOSER_PR() {
        assertASTCanBeProduced("LOSER_PR", considerPosition = true)
    }

    // TODO fix
    // java.lang.IllegalArgumentException: Start offset not calculated for fields £G64P1, £G64P2, £G64TC, £G64CS, £G64DC
    // at com.smeup.rpgparser.parsing.parsetreetoast.Data_definitionsKt.calculateFieldInfos(data_definitions.kt:678)
    /**
     * This error has been already classified earlier as [DS-OVERLAY](https://docs.google.com/spreadsheets/d/1x05ATX9lcJLL7s1sNpZawBKC1Zz7lP--V7xqOZ-wBbk/edit#gid=36284680&range=E25)
     * Earlier this error was hidden and then the ast creating apparently worked properly
     * */
    @Test
    @Ignore
    fun buildAstForLOSER_PR_FULL() {
        assertASTCanBeProduced("LOSER_PR_FULL", considerPosition = true)
    }

    @Test
    @Ignore
    fun buildAstForAPIPGM1() {
        assertASTCanBeProduced("APIPGM1", considerPosition = true).apply {
            assertEquals(4, this.dataDefinitions.size)
            assertEquals(1, this.subroutines.size)
        }
    }

    @Test
    fun buildAstForProcedure_B_testScope() {
        val mainProgramCu = assertASTCanBeProduced("PROCEDURE_B", considerPosition = true)
        mainProgramCu.apply {
            // we must have none variable with scope: Scope.Local or Scope.Static
            val none = this.allDataDefinitions.filter {
                it.scope == Scope.Local || it.scope == Scope.Static
            }.none()
            assertTrue(none)
        }
        val procedureCu = mainProgramCu.procedures!![0]
        procedureCu.apply {
            // we must have none variable with scope: Scope.program
            assertTrue(this.allDataDefinitions.none { it.scope == Scope.Program })
        }
    }

    @Test
    fun buildAstForJAX_PD1() {
        assertASTCanBeProduced("QILEGEN/£JAX_PD1", considerPosition = true).apply {
            assertEquals(expected = 3, actual = this.dataDefinitions.size)
        }
    }

    @Test
    fun buildAstForJAX_PC1() {
        val procedureNameToParamsSize = mapOf(
            "P_RxVAL" to 2,
            "P_RxATT" to 5,
            "P_RxURL" to 1,
            "P_RxSOS" to 2,
            "P_RxSOC" to 2,
            "P_RxLATE" to 5,
            "P_RxATV" to 2,
            "P_RxATP" to 2,
            "P_RxELE" to 8,
            "P_RxSPL" to 2
        )
        assertASTCanBeProduced("QILEGEN/£JAX_PC1", considerPosition = true).apply {
            assertEquals(expected = 10, actual = procedures!!.size)
            procedures!!.forEach { procedureAst ->
                assertEquals(
                    expected = procedureNameToParamsSize[procedureAst.procedureName],
                    actual = procedureAst.proceduresParamsDataDefinitions!!.size
                )
            }
        }
    }

    @Test
    fun buildAstForPARMS1() {
        assertASTCanBeProduced(exampleName = "PARMS1", printTree = false).apply {
            assertEquals(3, dataDefinitions.size)
            val type = dataDefinitions[0].type
            require(type is DataStructureType)
            val fields = type.fields
            // DS must contain 3 fields
            assertEquals(3, fields.size)
            dataDefinitions[0].fields.first { fieldDefinition -> fieldDefinition.name == "£PDSPR" }.apply {
                // during AST creating the field type must be like this
                assertTrue { this.type == DSFieldInitKeywordType.PARMS.type }
                // during AST creating startOffset and endOffset will be initialized
                assertEquals(10, this.startOffset)
                assertEquals(13, this.endOffset)
                assertTrue { this.initializationValue is ParmsExpr }
            }
            dataDefinitions[0].fields.first { fieldDefinition -> fieldDefinition.name == "£PDSST" }.apply {
                // during AST creating the field type must be like this
                assertTrue { this.type == DSFieldInitKeywordType.STATUS.type }
                // during AST creating startOffset and endOffset will be initialized
                assertEquals(13, this.startOffset)
                assertEquals(18, this.endOffset)
                assertTrue { this.initializationValue is StatusExpr }
            }
        }
    }
}