package com.smeup.rpgparser

import java.io.InputStream
import org.junit.Test as test

class RpgLexerTest {

    @test fun lexJD_001() {
        assertCanBeLexed("JD_001")
    }

    @test fun lexJD_002() {
        assertCanBeLexed("JD_002")
    }

    @test fun lexJD_003() {
        assertCanBeLexed("JD_003")
    }
}
