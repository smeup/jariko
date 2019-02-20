package com.smeup.rpgparser

import org.junit.Test as test

class RpgParserTest {

    @test fun parseJD_001() {
        assertCanBeParsed("JD_001")
    }

    @test fun parseJD_002() {
        assertCanBeParsed("JD_002")
    }

    @test fun parseJD_003() {
        assertCanBeParsed("JD_003")
    }
}
