package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.assertCodeCanBeParsed
import com.smeup.rpgparser.assertFileDefinitionIsPresent
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import org.junit.Test as test

class FileDefinitionTest {

    fun processFileDefinition(
        code: String,
        toAstConfiguration: ToAstConfiguration = ToAstConfiguration(considerPosition = false)
    ): CompilationUnit {
        val completeCode = """
|     H/COPY QILEGEN,£INIZH
|      *---------------------------------------------------------------
|     I/COPY QILEGEN,£TABB£1DS
|     I/COPY QILEGEN,£PDS
|     $code
        """.trimMargin("|")
        val rContext = assertCodeCanBeParsed(completeCode)
        return rContext.toAst(toAstConfiguration)
    }

    @test fun singlFileParsing() {
        val cu = processFileDefinition("FQATOCHOST if   e           k disk")
        cu.assertFileDefinitionIsPresent("QATOCHOST")
    }
}
