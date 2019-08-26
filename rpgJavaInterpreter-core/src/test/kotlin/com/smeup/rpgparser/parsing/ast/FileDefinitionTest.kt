package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.assertFileDefinitionIsPresent
import com.smeup.rpgparser.parseFragmentToCompilationUnit
import org.junit.Test as test

class FileDefinitionTest {

    @test fun singlFileParsing() {
        val cu = parseFragmentToCompilationUnit("FQATOCHOST if   e           k disk")
        cu.assertFileDefinitionIsPresent("QATOCHOST")
    }
}
