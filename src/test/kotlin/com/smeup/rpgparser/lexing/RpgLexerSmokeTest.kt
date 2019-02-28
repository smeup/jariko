package com.smeup.rpgparser

import org.junit.Test

class RpgLexerSmokeTest {

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
}