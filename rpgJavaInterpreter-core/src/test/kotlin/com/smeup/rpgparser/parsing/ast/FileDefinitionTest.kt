package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.assertFileDefinitionIsPresent
import com.smeup.rpgparser.parseFragmentToCompilationUnit
import org.junit.Test as test

class FileDefinitionTest {

    @test fun singleFileParsing() {
        val cu = parseFragmentToCompilationUnit("FQATOCHOST if   e           k disk")
        cu.assertFileDefinitionIsPresent("QATOCHOST")
    }

    @test fun multipleFileParsing() {
        val cu = parseFragmentToCompilationUnit(listOf(
                "FFirst     if   e           k disk ",
                "FSecond    if   e           k disk    rename(TSTREC:TSTREC2)"))
        cu.assertFileDefinitionIsPresent("First")
        cu.assertFileDefinitionIsPresent("Second")
    }
}
