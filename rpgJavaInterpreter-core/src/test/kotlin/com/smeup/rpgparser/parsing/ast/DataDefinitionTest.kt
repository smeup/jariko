package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class DataDefinitionTest {

    @test fun singleDataParsing() {
        val cu = parseFragmentToCompilationUnit("D U\$FUNZ          S             10")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", StringType(10))
    }

    @test fun booleanDataParsing() {
        val cu = parseFragmentToCompilationUnit("D OK              S              1N")
        cu.assertDataDefinitionIsPresent("OK", BooleanType)
    }

    @test fun caseInsensitiveBooleanDataParsing() {
        val cu = parseFragmentToCompilationUnit("D OK              S              1n")
        cu.assertDataDefinitionIsPresent("OK", BooleanType)
    }

    @test fun singleDataParsingOther() {
        val cu = parseFragmentToCompilationUnit("D U\$FUNZ          S             99")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", StringType(99))
    }

    @test fun singleDataParsingWithDecimals() {
        val cu = parseFragmentToCompilationUnit("D \$X              S              3  2")
        cu.assertDataDefinitionIsPresent("\$X", NumberType(1, 2))
    }

    @test fun timestampDataParsing() {
        val cu = parseFragmentToCompilationUnit("Dstart            S               z")
        cu.assertDataDefinitionIsPresent("start", TimeStampType)
    }

    @test fun arrayParsing() {
        val cu = parseFragmentToCompilationUnit("D U\$FUNZ          S             10    DIM(200)")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", ArrayType(StringType(10), 200))
    }

    @test fun singleDSParsing() {
        val cu = parseFragmentToCompilationUnit("D £G49SI          DS          1024")
        val dataDefinition = cu.getDataDefinition("£G49SI")
        assert(dataDefinition.type is DataStructureType)
        assertEquals(1024, dataDefinition.type.size)
    }

    @test fun structParsing() {
        val cu = parseFragmentToCompilationUnit(listOf(
                "D                 DS",
                "D \$\$SVAR                      1050    DIM(200)",
                "D  \$\$SVARCD                     50    OVERLAY(\$\$SVAR:1)                    Name",
                "D  \$\$SVARVA                   1000    OVERLAY(\$\$SVAR:*NEXT)                Value"))
        cu.assertDataDefinitionIsPresent("\$\$SVAR", ArrayType(DataStructureType(
                listOf(
                        FieldType("\$\$SVARCD", StringType(50)),
                        FieldType("\$\$SVARVA", StringType(1000))),
                1050), 200),
                fields = listOf(
                        FieldDefinition("\$\$SVARCD", ArrayType(StringType(50), 200),
                                explicitStartOffset = null,
                                calculatedStartOffset = 0,
                                calculatedEndOffset = 50),
                        FieldDefinition("\$\$SVARVA", ArrayType(StringType(1000), 200), explicitStartOffset = null,
                                calculatedStartOffset = 50,
                                calculatedEndOffset = 1050)
                ))
    }

    @test fun likeAndDimClauseParsing() {
        val cu = parseFragmentToCompilationUnit(
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
        val cu = parseFragmentToCompilationUnit("D DSDX3           DS            50       \n" +
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
                        FieldDefinition("\$TIPO", StringType(2), 0, 2),
                        FieldDefinition("\$OBBL", StringType(1), 2, 3),
                        FieldDefinition("\$INDI", StringType(2), 3, 5),
                        FieldDefinition("\$PARA", StringType(10), 20, 30)
                ))
        assertEquals(0, dataDef.fields[0].startOffset)
        assertEquals(2, dataDef.fields[0].endOffset)
        assertEquals(2, dataDef.fields[1].startOffset)
        assertEquals(3, dataDef.fields[1].endOffset)
        assertEquals(3, dataDef.fields[2].startOffset)
        assertEquals(5, dataDef.fields[2].endOffset)
        assertEquals(20, dataDef.fields[3].startOffset)
        assertEquals(30, dataDef.fields[3].endOffset)
    }

    @Test
    fun deriveLengthOfFieldFromOverrideClause() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", true)
        cu.resolve()
        val AR01 = cu.getDataDefinition("ARDS").getFieldByName("AR01")
        val FI01 = cu.getDataDefinition("ARDS").getFieldByName("FI01")
        val FI02 = cu.getDataDefinition("ARDS").getFieldByName("FI02")
        val FI03 = cu.getDataDefinition("ARDS").getFieldByName("FI03")
        val FI04 = cu.getDataDefinition("ARDS").getFieldByName("FI04")
        val FI05 = cu.getDataDefinition("ARDS").getFieldByName("FI05")
        val FI06 = cu.getDataDefinition("ARDS").getFieldByName("FI06")
        val FI07 = cu.getDataDefinition("ARDS").getFieldByName("FI07")
        val FI07A = cu.getDataDefinition("ARDS").getFieldByName("FI07A")
        val FI08 = cu.getDataDefinition("ARDS").getFieldByName("FI08")
        val FI09 = cu.getDataDefinition("ARDS").getFieldByName("FI09")
        val FI10 = cu.getDataDefinition("ARDS").getFieldByName("FI10")
        val FI11 = cu.getDataDefinition("ARDS").getFieldByName("FI11")
        val FI12 = cu.getDataDefinition("ARDS").getFieldByName("FI12")
        val FI13 = cu.getDataDefinition("ARDS").getFieldByName("FI13")
        val FI14 = cu.getDataDefinition("ARDS").getFieldByName("FI14")
        val FI15 = cu.getDataDefinition("ARDS").getFieldByName("FI15")
        val FI16 = cu.getDataDefinition("ARDS").getFieldByName("FI16")
        val FI17 = cu.getDataDefinition("ARDS").getFieldByName("FI17")
        val FI18 = cu.getDataDefinition("ARDS").getFieldByName("FI18")
        val FI19 = cu.getDataDefinition("ARDS").getFieldByName("FI19")
        val FI20 = cu.getDataDefinition("ARDS").getFieldByName("FI20")

        assertEquals(15, FI01.elementSize())
        assertEquals(10, FI02.elementSize())
        assertEquals(35, FI03.elementSize())
        assertEquals(1, FI04.elementSize())
        assertEquals(1, FI05.elementSize())
        assertEquals(1, FI06.elementSize())
        assertEquals(15, FI07.elementSize())
        assertEquals(15, FI07A.elementSize())
        assertEquals(1, FI08.elementSize())
        assertEquals(1, FI09.elementSize())

        I NEED HELP FROM MAURIZIO TO FIGURE THIS ONE OUT

        assertEquals(18, FI10.elementSize())
        assertEquals(2, FI11.elementSize())
        assertEquals(4, FI12.elementSize())
        assertEquals(3, FI13.elementSize())
        assertEquals(5, FI14.elementSize())
        assertEquals(10, FI15.elementSize())
        assertEquals(20, FI16.elementSize())
        assertEquals(3, FI17.elementSize())
        assertEquals(5, FI18.elementSize())
        assertEquals(10, FI19.elementSize())
        assertEquals(20, FI20.elementSize())

        assertEquals(3500, AR01.elementSize())
    }
}
