package com.smeup.rpgparser

import org.junit.Test as test

class RpgParserParseTreeShapeTest {

    @test fun parseJD_001_justdirectives() {
        val root = assertCanBeParsed("JD_001_justdirectives")
    }

    @test fun parseJD_001_onedatadecl() {
        val root = assertCanBeParsed("JD_001_onedatadecl")
        println(root)
    }

    @test fun parseJD_001_onedatadecl_simple() {
        val root = assertCanBeParsed("JD_001_onedatadecl_simple")
        println(root)
    }

    @test fun parseJD_001_onedatadecl_simple_withspace() {
        val root = assertCanBeParsed("JD_001_onedatadecl_simple_withspace")
        println(root)
    }

}
