package com.smeup.rpgparser.lexing

import com.smeup.rpgparser.RpgLexer
import com.smeup.rpgparser.RpgLexer.*
import com.smeup.rpgparser.assertCanBeLexed
import com.smeup.rpgparser.assertToken
import org.junit.Test as test

class RpgTokensListTest {

    @test fun lexJD_001() {
        val tokens = assertCanBeLexed("JD_001")
        assertToken(DIRECTIVE, "H/", tokens[0])
        assertToken(DIR_COPY, "COPY", tokens[1])
        assertToken(DIR_OtherText, "QILEGEN", tokens[2])
        assertToken(DIR_OtherText, "£INIZH", tokens[3])
        assertToken(EOL, "", tokens[4])
        assertToken(EOF, "<EOF>", tokens[964])
    }

    @test fun lexJD_001_justdirectives() {
        val tokens = assertCanBeLexed("JD_001_justdirectives")
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
        val tokens = assertCanBeLexed("JD_001_onedatadecl_simple")
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

    @test fun lexQILEGEN_PDS_header() {
        val tokens = assertCanBeLexed("QILEGEN.£PDS_header")
        tokens.forEachIndexed { index, token ->
            println("[$index] ${RpgLexer.VOCABULARY.getDisplayName(token.type)} ${token.text}")
        }
        assertToken(DIRECTIVE, "/", tokens[0])
        assertToken(DIR_IF, "IF", tokens[1])
        assertToken(DIR_NOT, "NOT", tokens[2])
        assertToken(DIR_DEFINED, "DEFINED", tokens[3])
        // LPAREN
        assertToken(DIR_OtherText, "JPDS", tokens[5])
        // RPAREN
        assertToken(EOL, "", tokens[7])
        assertToken(DIRECTIVE, "/", tokens[8])
        assertToken(DIR_DEFINE, "DEFINE", tokens[9])
        assertToken(DIR_OtherText, "JPDS", tokens[10])
        assertToken(EOL, "", tokens[11])
        assertToken(EOF, "<EOF>", tokens[12])
    }

}