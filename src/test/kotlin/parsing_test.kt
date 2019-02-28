package com.smeup.rpgparser

import org.junit.Test as test

class RpgParserSmokeTest {

    @test fun parseJD_001() {
        assertCanBeParsed("JD_001")
    }

    @test fun parseJD_001_alt() {
        assertCanBeParsed("JD_001_alt")
    }

    @test fun parseJD_001_justdirectives() {
        assertCanBeParsed("JD_001_justdirectives")
    }

    @test fun parseJD_002() {
        assertCanBeParsed("JD_002")
    }

    @test fun parseJD_003() {
        assertCanBeParsed("JD_003")
    }
}
