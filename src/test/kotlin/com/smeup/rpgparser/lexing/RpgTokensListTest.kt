package com.smeup.rpgparser

import org.junit.Test as test

class RpgTokensListTest {

    @test fun lexJD_001() {
        val tokens = assertCanBeLexed("JD_001")
        tokens.forEachIndexed { index, token ->
            println("[$index] ${RpgLexer.VOCABULARY.getDisplayName(token.type)} ${token.text}")
        }
    }

    @test fun lexJD_001_justdirectives() {
        val tokens = assertCanBeLexed("JD_001_justdirectives")
        tokens.forEachIndexed { index, token ->
            println("[$index] ${RpgLexer.VOCABULARY.getDisplayName(token.type)} ${token.text}")
        }
    }

    @test fun lexJD_001_onedatadecl_simple() {
        val tokens = assertCanBeLexed("JD_001_onedatadecl_simple")
        tokens.forEachIndexed { index, token ->
            println("[$index] ${RpgLexer.VOCABULARY.getDisplayName(token.type)} ${token.text}")
        }
    }

}