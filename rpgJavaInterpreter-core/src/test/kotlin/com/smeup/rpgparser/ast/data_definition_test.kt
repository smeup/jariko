package com.smeup.rpgparser.ast

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsetreetoast.resolve
import com.smeup.rpgparser.parsetreetoast.toAst
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class DataDefinitionTest {

    fun processDataDefinition(code: String,
                              toAstConfiguration: ToAstConfiguration = ToAstConfiguration(considerPosition = false)) : CompilationUnit {
        val completeCode = """
|     H/COPY QILEGEN,£INIZH
|      *---------------------------------------------------------------
|     I/COPY QILEGEN,£TABB£1DS
|     I/COPY QILEGEN,£PDS
|     $code
        """.trimMargin("|")
        val rContext = assertCodeCanBeParsed(completeCode)
        return rContext.toAst("<UNNAMED>", toAstConfiguration)
    }

    @test fun singleDataParsing() {
        val cu = processDataDefinition("D U\$FUNZ          S             10")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", StringType(10))
    }

    @test fun booleanDataParsing() {
        val cu = processDataDefinition("D OK              S              1N")
        cu.assertDataDefinitionIsPresent("OK", BooleanType)
    }

    @test fun caseInsensitiveBooleanDataParsing() {
        val cu = processDataDefinition("D OK              S              1n")
        cu.assertDataDefinitionIsPresent("OK", BooleanType)
    }


    @test fun singleDataParsingOther() {
        val cu = processDataDefinition("D U\$FUNZ          S             99")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", StringType(99))
    }

    @test fun singleDataParsingWithDecimals() {
        val cu = processDataDefinition("D \$X              S              3  2")
        cu.assertDataDefinitionIsPresent("\$X", NumberType(1, 2))
    }

    @test fun timestampDataParsing() {
        val cu = processDataDefinition("Dstart            S               z")
        cu.assertDataDefinitionIsPresent("start", TimeStampType)
    }

    @test fun arrayParsing() {
        val cu = processDataDefinition("D U\$FUNZ          S             10    DIM(200)")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", ArrayType(StringType(10), 200))
    }

    @test fun structParsing() {
        val cu = processDataDefinition("D                 DS\n" +
                "     D \$\$SVAR                      1050    DIM(200)\n" +
                "     D  \$\$SVARCD                     50    OVERLAY(\$\$SVAR:1)                    Name\n" +
                "     D  \$\$SVARVA                   1000    OVERLAY(\$\$SVAR:*NEXT)                Value")
        cu.assertDataDefinitionIsPresent("\$\$SVAR", ArrayType(DataStructureType(
                listOf(
                        FieldType("\$\$SVARCD", StringType(50)),
                        FieldType("\$\$SVARVA", StringType(1000))),
                1050), 200),
                fields = listOf(
                        FieldDefinition("\$\$SVARCD", ArrayType(StringType(50), 200)),
                        FieldDefinition("\$\$SVARVA", ArrayType(StringType(1000), 200))
                ))
    }

    @test fun likeAndDimClauseParsing() {
        val cu = processDataDefinition(
                "D U\$SVARSK        S                   LIKE(\$\$SVAR) DIM(%ELEM(\$\$SVAR))",
                toAstConfiguration = ToAstConfiguration(considerPosition = false,
                        compileTimeInterpreter = InjectableCompileTimeInterpreter().apply {
                            this.overrideDecl("\$\$SVAR", ArrayType(StringType(12), 38))
                        }))
        cu.assertDataDefinitionIsPresent("U\$SVARSK", ArrayType(StringType(12), 38))
    }

    @test fun inStatementDataDefinitionInClearIsProcessed() {
        val cu = assertASTCanBeProduced("CALCFIB", true)
        cu.resolve()
        assertTrue(cu.hasAnyDataDefinition("dsp"))
        assertEquals(StringType(50), cu.getAnyDataDefinition("dsp").type)
    }

    @test fun executeJD_useOfLike() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = InternalInterpreter(DummySystemInterface)
        interpreter.simplyInitialize(cu, emptyMap())
        val dataDefinition = cu.getDataDefinition("U\$SVARSK_INI")
        assertEquals(200, dataDefinition.numberOfElements())
    }

    @test fun executeJD_useOfDim() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = InternalInterpreter(DummySystemInterface)
        interpreter.simplyInitialize(cu, emptyMap())
        val dataDefinition = cu.getDataDefinition("U\$SVARSK_INI")
        assertEquals(1050, dataDefinition.elementSize())
    }

    @test fun dsNotArrayWithOffsets() {
        val cu = processDataDefinition("D DSDX3           DS            50       \n" +
                "     D  \$TIPO                  1      2       \n" +
                "     D  \$OBBL                  3      3       \n" +
                "     D  \$INDI                  4      5       \n" +
                "     D  \$PARA                 21     30")
        val dataDef = cu.assertDataDefinitionIsPresent("DSDX3", DataStructureType(
                listOf(
                        FieldType("\$TIPO", StringType(2)),
                        FieldType("\$OBBL", StringType(1)),
                        FieldType("\$INDI", StringType(2)),
                        FieldType("\$PARA", StringType(10))),
                50),
                listOf(
                        FieldDefinition("\$TIPO", StringType(2), 1, 2),
                        FieldDefinition("\$OBBL", StringType(1), 3, 3),
                        FieldDefinition("\$INDI", StringType(2), 4, 5),
                        FieldDefinition("\$PARA", StringType(10), 21, 30)
                ))
        assertEquals(1, dataDef.fields[0].startOffset)
        assertEquals(2, dataDef.fields[0].endOffset)
        assertEquals(3, dataDef.fields[1].startOffset)
        assertEquals(3, dataDef.fields[1].endOffset)
        assertEquals(4, dataDef.fields[2].startOffset)
        assertEquals(5, dataDef.fields[2].endOffset)
        assertEquals(21, dataDef.fields[3].startOffset)
        assertEquals(30, dataDef.fields[3].endOffset)
    }
}
