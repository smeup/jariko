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
    fun parsePROOF() {
        assertCanBeParsed("PROOF")
    }

    @Test
    fun parseJCODFISD() {
        assertCanBeParsed("JCODFISD")
    }

    @Test
    fun parseJCODFISS() {
        assertCanBeParsed("JCODFISS")
    }

    @Test
    fun parseJFTCPR() {
        assertCanBeParsed("JFTCPR")
    }

    @Test
    fun parseJDATWD() {
        assertCanBeParsed("JDATWD")
    }

    @Test
    fun parseJDATWDK() {
        assertCanBeParsed("JDATWDK")
    }


}