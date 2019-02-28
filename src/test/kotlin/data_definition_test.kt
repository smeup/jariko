package com.smeup.rpgparser

import org.junit.Test as test

class DataDefinitionTest {

    fun processDataDefinition(code: String) : CompilationUnit {
        val completeCode = """
|     H/COPY QILEGEN,£INIZH
|      *---------------------------------------------------------------
|     I/COPY QILEGEN,£TABB£1DS
|     I/COPY QILEGEN,£PDS
|     $code
        """.trimMargin("|")
        val rContext = assertCodeCanBeParsed(completeCode)
        return rContext.toAst()
    }

    @test fun singleDataParsing() {
        val cu = processDataDefinition("     D U\$FUNZ          S             10")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", DataType.SINGLE, 10)
    }

}
