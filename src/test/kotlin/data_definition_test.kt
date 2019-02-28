package com.smeup.rpgparser

import me.tomassetti.kolasu.model.children
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
        rContext.children.forEachIndexed { index, parseTree ->
            println(parseTree.text + " [$index]")
        }
        return rContext.toAst()
    }

    @test fun singleDataParsing() {
        val cu = processDataDefinition("     D U\$FUNZ          S             10")
        cu.children
        cu.assertDataDefinitionIsPresent("U\$FUNZ", DataType.SINGLE, 10)
    }

}
