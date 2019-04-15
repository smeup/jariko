package com.smeup.rpgparser.lexing

import com.smeup.rpgparser.assertCanBeLexed
import org.junit.Test

/**
 * We just test that the original code is lexed without errors.
 */
class RpgLexerSmokeTest {

    @Test
    fun lexJD_000() {
        assertCanBeLexed("JD_000")
    }

    @Test
    fun lexJD_001() {
        assertCanBeLexed("JD_001")
    }

    @Test
    fun lexJD_002() {
        assertCanBeLexed("JD_002")
    }

    @Test
    fun lexJD_003() {
        assertCanBeLexed("JD_003")
    }

    @Test
    fun lexJD_003_alt() {
        assertCanBeLexed("JD_003_alt")
    }

    @Test
    fun lexQILEGEN_POUND_PDS() {
        assertCanBeLexed("QILEGEN.£PDS")
    }

    @Test
    fun lexQILEGEN_POUND_TABA_POUND_1DS() {
        assertCanBeLexed("QILEGEN.£TABA£1DS")
    }

    @Test
    fun lexPROOF() {
        assertCanBeLexed("PROOF")
    }

    @Test
    fun lexJCODFISD() {
        assertCanBeLexed("JCODFISD")
    }

    @Test
    fun lexJCODFISS() {
        assertCanBeLexed("JCODFISS")
    }

    @Test
    fun lexJFTCPR() {
        assertCanBeLexed("JFTCPR")
    }

    @Test
    fun lexJDATWD() {
        assertCanBeLexed("JDATWD")
    }

    @Test
    fun lexJDATWDK() {
        assertCanBeLexed("JDATWDK")
    }

    @Test
    fun lexCALCFIB() {
        assertCanBeLexed("CALCFIB")
    }

    @Test
    fun lexHELLO() {
        assertCanBeLexed("HELLO")
    }

    @Test
    fun lexHELLO1() {
        assertCanBeLexed("HELLO1")
    }

    @Test
    fun lexJRANDOMA() {
        assertCanBeLexed("JRANDOMA")
    }

    @Test
    fun lexJRANDOMCAL() {
        assertCanBeLexed("JRANDOMCAL")
    }

    @Test
    fun lexCHAINHOSTS() {
        assertCanBeLexed("CHAINHOSTS")
    }

    @Test
    fun lexTIMESTDIFF() {
        assertCanBeLexed("TIMESTDIFF")
    }
}
