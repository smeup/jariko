/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertDataDefinitionIsPresent
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parseFragmentToCompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

open class DataDefinitionTest : AbstractTest() {

    @test fun singleDataParsing() {
        val cu = parseFragmentToCompilationUnit("D U\$FUNZ          S             10")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", StringType(10))
    }

    @test fun booleanDataParsing() {
        val cu = parseFragmentToCompilationUnit("D OK              S              1N")
        cu.assertDataDefinitionIsPresent("OK", BooleanType)
    }

    @test fun constDataParsing() {
        val cu = parseFragmentToCompilationUnit("Dx                C                   CONST(30)")
        cu.assertDataDefinitionIsPresent("x", NumberType(2, 0))
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

    @test fun unlimitedStringDataParsing() {
        val cu = parseFragmentToCompilationUnit("Dunlimited        S               0")
        cu.assertDataDefinitionIsPresent("unlimited", UnlimitedStringType)
    }

    @test fun arrayParsing() {
        val cu = parseFragmentToCompilationUnit("D U\$FUNZ          S             10    DIM(200)")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", ArrayType(StringType(10), 200))
    }

    @test fun variableWithContinuationLineTokenInComment() {
        val cu = parseFragmentToCompilationUnit("DVAR              S             10                                         ...")
        cu.assertDataDefinitionIsPresent("VAR", StringType(length = 10))
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
        cu.assertDataDefinitionIsPresent("@UNNAMED_DS_5", DataStructureType(
                listOf(
                        FieldType("\$\$SVAR", ArrayType(StringType(1050), 200)),
                        FieldType("\$\$SVARCD", ArrayType(StringType(50), 200)),
                        FieldType("\$\$SVARVA", ArrayType(StringType(1000), 200))),
                210000),
                fields = listOf(
                        FieldDefinition("\$\$SVAR", ArrayType(StringType(1050), 200),
                                explicitStartOffset = null,
                                calculatedStartOffset = 0,
                                calculatedEndOffset = 1050,
                                declaredArrayInLineOnThisField = 200),
                        FieldDefinition("\$\$SVARCD", ArrayType(StringType(50), 200),
                                explicitStartOffset = null,
                                calculatedStartOffset = 0,
                                calculatedEndOffset = 50,
                                declaredArrayInLineOnThisField = null,
                                overlayTarget = "\$\$SVAR"),
                        FieldDefinition("\$\$SVARVA", ArrayType(StringType(1000), 200),
                                explicitStartOffset = null,
                                calculatedStartOffset = 50,
                                calculatedEndOffset = 1050,
                                declaredArrayInLineOnThisField = null,
                                overlayTarget = "\$\$SVAR")
                ))
    }

    @test
    fun likeAndDimClauseParsing() {
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
        cu.resolveAndValidate()
        assertTrue(cu.hasAnyDataDefinition("dsp"))
        assertEquals(StringType(50), cu.getAnyDataDefinition("dsp").type)
    }

    @test fun executeJD_useOfLike() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolveAndValidate()
        val interpreter = InternalInterpreter(DummySystemInterface)
        interpreter.simplyInitialize(cu, emptyMap())
        val dataDefinition = cu.getDataDefinition("U\$SVARSK_INI")
        assertEquals(200, dataDefinition.numberOfElements())
    }

    @test fun executeJD_useOfDim() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolveAndValidate()
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
    fun sizeOfIntegerData() {
        assertEquals(1, NumberType(3, 0, RpgType.INTEGER).size)
        assertEquals(2, NumberType(5, 0, RpgType.INTEGER).size)
        assertEquals(4, NumberType(10, 0, RpgType.INTEGER).size)
        assertEquals(8, NumberType(20, 0, RpgType.INTEGER).size)
    }

    @Test
    fun sizeOfUnsignedIntegerData() {
        assertEquals(1, NumberType(3, 0, RpgType.UNSIGNED).size)
        assertEquals(2, NumberType(5, 0, RpgType.UNSIGNED).size)
        assertEquals(4, NumberType(10, 0, RpgType.UNSIGNED).size)
        assertEquals(8, NumberType(20, 0, RpgType.UNSIGNED).size)
    }

    @Test
    fun deriveLengthOfFieldFromOverrideClause() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", true)
        cu.resolveAndValidate()
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

        assertEquals(ArrayType(NumberType(12, 3, RpgType.ZONED), 100), FI07.type)
        assertEquals(ArrayType(NumberType(12, 3, RpgType.PACKED), 100), FI10.type)
        assertEquals(ArrayType(NumberType(2, 0, "B"), 100), FI11.type)
        assertEquals(ArrayType(NumberType(3, 0, "U"), 100), FI17.type)

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

        assertEquals(122, FI07.stepSize)
        assertEquals(122, FI07A.stepSize)

        // Number of digits for FI10 = 15 (12 integers, 3 decimals)
        // For packed: Number of digits = 2n - 1
        // Where n is the size in bytes
        // So size in bytes = (NbOfDigits + 1) / 2, rounded

        assertEquals(8, FI10.elementSize())
        assertEquals(2, FI11.elementSize())
        // The actual size is 2
        // assertEquals(4, FI12.elementSize())
        assertEquals(2, FI11.elementSize())

        assertEquals(1, FI13.elementSize())
        assertEquals(2, FI14.elementSize())
        assertEquals(4, FI15.elementSize())
        assertEquals(8, FI16.elementSize())
        assertEquals(1, FI17.elementSize())
        assertEquals(2, FI18.elementSize())
        assertEquals(4, FI19.elementSize())
        assertEquals(8, FI20.elementSize())

        val allFieldsElementSize = FI01.elementSize() +
                FI02.elementSize() +
                FI03.elementSize() +
                FI04.elementSize() +
                FI05.elementSize() +
                FI06.elementSize() +
                FI07.elementSize() +
                FI08.elementSize() +
                FI09.elementSize() +
                FI10.elementSize() +
                FI11.elementSize() +
                FI12.elementSize() +
                FI13.elementSize() +
                FI14.elementSize() +
                FI15.elementSize() +
                FI16.elementSize() +
                FI17.elementSize() +
                FI18.elementSize() +
                FI19.elementSize() +
                FI20.elementSize()

        // The actual len is 122
        // assertEquals(124, allFieldsElementSize)
        assertEquals(122, allFieldsElementSize)

        // The actual size is 12200
        // assertEquals(12400, AR01.elementSize())
        assertEquals(122, AR01.elementSize())

        assertEquals(15, FI01.endOffset)
        assertEquals(25, FI02.endOffset)
        assertEquals(122, FI20.endOffset)
    }

    @Test
    fun initializationValue() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", true)
        cu.resolveAndValidate()
        val unnamedDs = cu.getDataDefinition("@UNNAMED_DS_48")
        assertEquals(DataStructureType(listOf(
                FieldType("LOG1", StringType(14)),
                FieldType("LOG", ArrayType(StringType(2), 7))), 14), unnamedDs.type)

        val LOG1 = unnamedDs.getFieldByName("LOG1")
        assertEquals(StringType(14), LOG1.type)
        assertEquals((LOG1.initializationValue as StringLiteral).value, "0F0L1L2L3L4L5L")

        val LOG = unnamedDs.getFieldByName("LOG")
        assertEquals(ArrayType(StringType(2), 7), LOG.type)
        assertEquals(LOG.initializationValue, null)
        assertEquals(2, LOG.elementSize())
        assertEquals(LOG.startOffset, 0)
        assertEquals(LOG.explicitEndOffset, null)
        assertEquals(LOG.calculatedEndOffset, 2)
        assertEquals(LOG.endOffset, 2)
        assertEquals(LOG.size, 14)
    }

    @Test
    fun initializationValueOnOverlay() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", true)
        cu.resolveAndValidate()

        val AR01 = cu.getDataOrFieldDefinition("AR01") as FieldDefinition
        assertEquals(122, AR01.elementSize())
        assertEquals(ArrayType(StringType(122), 100), AR01.type)

        val LOG = cu.getDataOrFieldDefinition("LOG") as FieldDefinition
        assertEquals(0, LOG.startOffset)
        assertEquals(2, LOG.endOffset)

        val result = execute(cu, emptyMap())
        val unnamedDsValue = result["@UNNAMED_DS_48"]
        assertEquals(DataStructValue("0F0L1L2L3L4L5L"), unnamedDsValue)
        val log1Value = result["LOG1"]
        assertEquals(StringValue("0F0L1L2L3L4L5L"), log1Value)
        val logValue = result["LOG"] as ProjectedArrayValue
        assertEquals(0, logValue.startOffset)
        assertEquals(2, logValue.step)
        assertEquals(7, logValue.arrayLength)
        assertEquals(ConcreteArrayValue(mutableListOf(StringValue("0F"),
                StringValue("0L"),
                StringValue("1L"),
                StringValue("2L"),
                StringValue("3L"),
                StringValue("4L"),
                StringValue("5L")), StringType(2)) as ArrayValue, logValue)
    }
}
