package com.smeup.rpgparser.lexing

import com.smeup.rpgparser.RpgLexer.*
import com.smeup.rpgparser.assertCodeCanBeLexed
import com.smeup.rpgparser.assertExampleCanBeLexed
import com.smeup.rpgparser.assertToken
import kotlin.test.assertEquals
import org.junit.Test as test

class RpgTokensListTest {

    @test fun lexJD_001() {
        val tokens = assertExampleCanBeLexed("JD_001")
        assertToken(DIRECTIVE, "H/", tokens[0])
        assertToken(DIR_COPY, "COPY", tokens[1])
        assertToken(DIR_OtherText, "QILEGEN", tokens[2])
        assertToken(DIR_OtherText, "£INIZH", tokens[3])
        assertToken(EOL, "", tokens[4])
        assertToken(EOF, "<EOF>", tokens.last())
    }

    @test fun lexJD_001_justdirectives() {
        val tokens = assertExampleCanBeLexed("JD_001_justdirectives")
        assertToken(DIRECTIVE, "H/", tokens[0])
        assertToken(DIR_COPY, "COPY", tokens[1])
        assertToken(DIR_OtherText, "QILEGEN", tokens[2])
        assertToken(DIR_OtherText, "£INIZH", tokens[3])
        assertToken(EOL, "", tokens[4])
        assertToken(DIRECTIVE, "I/", tokens[5])
        assertToken(DIR_COPY, "COPY", tokens[6])
        assertToken(DIR_OtherText, "QILEGEN", tokens[7])
        assertToken(DIR_OtherText, "£TABB£1DS", tokens[8])
        assertToken(EOL, "", tokens[9])
        assertToken(DIRECTIVE, "I/", tokens[10])
        assertToken(DIR_COPY, "COPY", tokens[11])
        assertToken(DIR_OtherText, "QILEGEN", tokens[12])
        assertToken(DIR_OtherText, "£PDS", tokens[13])
        assertToken(EOL, "", tokens[14])
        assertToken(EOF, "<EOF>", tokens[15])
    }

    @test fun lexJD_001_onedatadecl_simple() {
        val tokens = assertExampleCanBeLexed("JD_001_onedatadecl_simple")
        assertToken(DIRECTIVE, "H/", tokens[0])
        assertToken(DIR_COPY, "COPY", tokens[1])
        assertToken(DIR_OtherText, "QILEGEN", tokens[2])
        assertToken(DIR_OtherText, "£INIZH", tokens[3])
        assertToken(EOL, "", tokens[4])
        assertToken(DIRECTIVE, "I/", tokens[5])
        assertToken(DIR_COPY, "COPY", tokens[6])
        assertToken(DIR_OtherText, "QILEGEN", tokens[7])
        assertToken(DIR_OtherText, "£TABB£1DS", tokens[8])
        assertToken(EOL, "", tokens[9])
        assertToken(DIRECTIVE, "I/", tokens[10])
        assertToken(DIR_COPY, "COPY", tokens[11])
        assertToken(DIR_OtherText, "QILEGEN", tokens[12])
        assertToken(DIR_OtherText, "£PDS", tokens[13])
        assertToken(EOL, "", tokens[14])
        assertToken(DS_FIXED, "D", tokens[15])
        assertToken(NAME, "UFUNZ", tokens[16])
        assertToken(EXTERNAL_DESCRIPTION, "", tokens[17])
        assertToken(DATA_STRUCTURE_TYPE, "", tokens[18])
        assertToken(DEF_TYPE_S, "S", tokens[19])
        assertToken(FROM_POSITION, "", tokens[20])
        assertToken(TO_POSITION, "10", tokens[21])
        assertToken(DATA_TYPE, "", tokens[22])
        assertToken(DECIMAL_POSITIONS, "", tokens[23])
        assertToken(RESERVED, "", tokens[24])
        assertToken(EOF, "<EOF>", tokens[25])
    }

    @test fun lexHELLO() {
        val tokens = assertExampleCanBeLexed("HELLO")
        assertToken(LastRecordIndicator, "LR", tokens[48])
        assertToken(EOF, "<EOF>", tokens.last())
    }

    @test fun lexMute12_01_qualifiedAccess() {
        val tokens = assertExampleCanBeLexed("data/ds/MUTE12_01")
        val tokensAtLine102 = tokens.filter { it.line == 102 }
        assertEquals(12, tokensAtLine102.size)
        assertToken(OP_EVAL, "EVAL", tokensAtLine102[5])
        assertToken(ID, "DS1", tokensAtLine102[6])
        assertToken(FREE_DOT, ".", tokensAtLine102[7])
        assertToken(ID, "AR2", tokensAtLine102[8])
    }

    @test fun lexQualifiedDsAccess() {
        val tokens = assertCodeCanBeLexed("     C                   EVAL      DS1.AR2=*ON")
        assertEquals(12, tokens.size)
        assertToken(ID, "DS1", tokens[6])
        assertToken(FREE_DOT, ".", tokens[7])
        assertToken(ID, "AR2", tokens[8])
    }

    @test fun lexMute12_06_indicatorAssignment() {
        val tokens = assertExampleCanBeLexed("data/primitives/MUTE12_06")
        val tokensAtLine = tokens.filter { it.line == 21 }
        assertEquals(10, tokensAtLine.size)
        assertToken(OP_EVAL, "EVAL", tokensAtLine[5])
        assertToken(SPLAT_INDICATOR, "*IN35", tokensAtLine[6])
        assertToken(EQUAL, "=", tokensAtLine[7])
        assertToken(SPLAT_ON, "*ON", tokensAtLine[8])
    }

    @test fun lexMute12_06_globalIndicatorAssignment() {
        val tokens = assertExampleCanBeLexed("data/primitives/MUTE12_06")
        val tokensAtLine = tokens.filter { it.line == 71 }
        assertEquals(10, tokensAtLine.size)
        assertToken(OP_EVAL, "EVAL", tokensAtLine[5])
        assertToken(SPLAT_IN, "*IN", tokensAtLine[6])
        assertToken(EQUAL, "=", tokensAtLine[7])
        assertToken(SPLAT_ON, "*ON", tokensAtLine[8])
    }
}
