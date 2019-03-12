package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import org.junit.Test
import kotlin.test.assertEquals

class RpgParserSmokeTest {

    @Test
    fun parseJD_001() {
        assertCanBeParsed("JD_001")
    }

    @Test
    fun parseJD_001_alt() {
        assertCanBeParsed("JD_001_alt")
    }

    @Test
    fun parseJD_001_justdirectives() {
        assertCanBeParsed("JD_001_justdirectives")
    }

    @Test
    fun parseJD_001_onedatadecl() {
        assertCanBeParsed("JD_001_onedatadecl")
    }

    @Test
    fun parseJD_001_onedatadecl_simple() {
        assertCanBeParsed("JD_001_onedatadecl_simple")
    }

    @Test
    fun parseJD_002() {
        assertCanBeParsed("JD_002")
    }

    @Test
    fun parseJD_003() {
        assertCanBeParsed("JD_003")
    }

    @Test
    fun buildAstForJD_001() {
        val cu = assertASTCanBeProduced("JD_001")
        assertEquals(10, cu.dataDefinitons.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(7, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        assertEquals(18, cu.dataDefinitons.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(10, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        assertEquals(17, cu.dataDefinitons.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(6, cu.subroutines.size)
    }
}