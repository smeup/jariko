package com.smeup.rpgparser

import org.junit.Test as test

class RpgLexerSmokeTest {

    @test fun lexJD_001() {
        assertCanBeLexed("JD_001")
    }

    @test fun lexJD_002() {
        assertCanBeLexed("JD_002")
    }

    @test fun lexJD_003() {
        assertCanBeLexed("JD_003")
    }

    @test fun lexJD_003_alt() {
        assertCanBeLexed("JD_003_alt")
    }
}

class RpgTokensListTest {

    @test fun lexJD_001() {
        val tokens = assertCanBeLexed("JD_001")
        tokens.forEachIndexed { index, token ->
            println("[$index] ${RpgLexer.VOCABULARY.getDisplayName(token.type)} ${token.text}")
        }
    }

}