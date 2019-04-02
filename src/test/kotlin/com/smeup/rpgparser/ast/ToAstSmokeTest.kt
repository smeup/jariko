package com.smeup.rpgparser.ast

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import org.junit.Test
import kotlin.test.assertEquals

class ToAstSmokeTest {

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
        assertEquals(17, cu.dataDefinitions.size)
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
        assertEquals("\$\$SVAR", root.dataDefinitions[0].name)
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
}
