package com.smeup.rpgparser.lexing

import com.smeup.rpgparser.RpgLexer
import com.smeup.rpgparser.assertExampleCanBeLexed
import kotlin.test.assertEquals
import org.junit.Test

/**
 * We just test that the original code is lexed without errors.
 */
class RpgLexerSmokeTest {

    @Test
    fun lexDSEX01() {
        val tokens = assertExampleCanBeLexed("DSEX01")
        assertEquals(listOf(
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_DS,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_BLANK,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_BLANK,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_BLANK,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_BLANK,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_BLANK,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_BLANK,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_BLANK,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_S,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.DS_FIXED,
            RpgLexer.NAME,
            RpgLexer.EXTERNAL_DESCRIPTION,
            RpgLexer.DATA_STRUCTURE_TYPE,
            RpgLexer.DEF_TYPE_S,
            RpgLexer.FROM_POSITION,
            RpgLexer.TO_POSITION,
            RpgLexer.DATA_TYPE,
            RpgLexer.DECIMAL_POSITIONS,
            RpgLexer.RESERVED,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_TIMESTAMP,
            RpgLexer.OPEN_PAREN,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_CHAR,
            RpgLexer.OPEN_PAREN,
            RpgLexer.ID,
            RpgLexer.COLON,
            RpgLexer.SPLAT_ISO,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_DEC,
            RpgLexer.OPEN_PAREN,
            RpgLexer.BIF_SUBST,
            RpgLexer.OPEN_PAREN,
            RpgLexer.ID,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_DEC,
            RpgLexer.OPEN_PAREN,
            RpgLexer.BIF_SUBST,
            RpgLexer.OPEN_PAREN,
            RpgLexer.ID,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_DEC,
            RpgLexer.OPEN_PAREN,
            RpgLexer.BIF_SUBST,
            RpgLexer.OPEN_PAREN,
            RpgLexer.ID,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_DEC,
            RpgLexer.OPEN_PAREN,
            RpgLexer.BIF_SUBST,
            RpgLexer.OPEN_PAREN,
            RpgLexer.ID,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_DEC,
            RpgLexer.OPEN_PAREN,
            RpgLexer.BIF_SUBST,
            RpgLexer.OPEN_PAREN,
            RpgLexer.ID,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_EVAL,
            RpgLexer.ID,
            RpgLexer.EQUAL,
            RpgLexer.BIF_DEC,
            RpgLexer.OPEN_PAREN,
            RpgLexer.BIF_SUBST,
            RpgLexer.OPEN_PAREN,
            RpgLexer.ID,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.COLON,
            RpgLexer.NUMBER,
            RpgLexer.CLOSE_PAREN,
            RpgLexer.C_FREE_NEWLINE,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_FactorContent,
            RpgLexer.OP_DSPLY,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOL,
            RpgLexer.CS_FIXED,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankFlag,
            RpgLexer.BlankIndicator,
            RpgLexer.CS_BlankFactor,
            RpgLexer.OP_SETON,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_BlankFactor,
            RpgLexer.CS_FieldLength,
            RpgLexer.CS_DecimalPositions,
            RpgLexer.LastRecordIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.BlankIndicator,
            RpgLexer.EOF
        ), tokens.map { it.type })
    }

    @Test
    fun lexJD_000() {
        assertExampleCanBeLexed("JD_000")
    }

    @Test
    fun lexJD_001() {
        assertExampleCanBeLexed("JD_001")
    }

    @Test
    fun lexJD_002() {
        assertExampleCanBeLexed("JD_002")
    }

    @Test
    fun lexJD_003() {
        assertExampleCanBeLexed("JD_003")
    }

    @Test
    fun lexJD_003_alt() {
        assertExampleCanBeLexed("JD_003_alt")
    }

    @Test
    fun lexQILEGEN_POUND_PDS() {
        assertExampleCanBeLexed("QILEGEN.£PDS")
    }

    @Test
    fun lexQILEGEN_POUND_TABA_POUND_1DS() {
        assertExampleCanBeLexed("QILEGEN.£TABA£1DS")
    }

    @Test
    fun lexPROOF() {
        assertExampleCanBeLexed("PROOF")
    }

    @Test
    fun lexJCODFISD() {
        assertExampleCanBeLexed("JCODFISD")
    }

    @Test
    fun lexJCODFISS() {
        assertExampleCanBeLexed("JCODFISS")
    }

    @Test
    fun lexJFTCPR() {
        assertExampleCanBeLexed("JFTCPR")
    }

    @Test
    fun lexJDATWD() {
        assertExampleCanBeLexed("JDATWD")
    }

    @Test
    fun lexJDATWDK() {
        assertExampleCanBeLexed("JDATWDK")
    }

    @Test
    fun lexCALCFIB() {
        assertExampleCanBeLexed("CALCFIB")
    }

    @Test
    fun lexHELLO() {
        assertExampleCanBeLexed("HELLO")
    }

    @Test
    fun lexHELLO1() {
        assertExampleCanBeLexed("HELLO1")
    }

    @Test
    fun lexJRANDOMA() {
        assertExampleCanBeLexed("JRANDOMA")
    }

    @Test
    fun lexJRANDOMCAL() {
        assertExampleCanBeLexed("JRANDOMCAL")
    }

    @Test
    fun lexCHAINHOSTS() {
        assertExampleCanBeLexed("CHAINHOSTS")
    }

    @Test
    fun lexTIMESTDIFF() {
        assertExampleCanBeLexed("TIMESTDIFF")
    }

    @Test
    fun lexABSTEST() {
        assertExampleCanBeLexed("ABSTEST")
    }

    @Test
    fun lexINTTEST() {
        assertExampleCanBeLexed("INTTEST")
    }
}
