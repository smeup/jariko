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

package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.*
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.*
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

open class RpgParserDataStruct : AbstractTest() {

    @Test
    fun parseSTRUCT_01_MYDS_isRecognizedCorrectly() {
        val cu = assertASTCanBeProduced("struct/STRUCT_01", true)
        cu.resolveAndValidate()

        val dataDefinition = cu.getDataDefinition("MYDS")
        assertEquals(0, dataDefinition.fields[0].startOffset)
        assertEquals(5, dataDefinition.fields[0].endOffset)
        assertEquals(5, dataDefinition.fields[1].startOffset)
        assertEquals(15, dataDefinition.fields[1].endOffset)
        assertEquals(15, dataDefinition.elementSize())
    }

    @Test
    fun parseSTRUCT_01() {
        assertCanBeParsed("struct/STRUCT_01", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_01", true)

        cu.resolveAndValidate()

        val MYDS = cu.getDataDefinition("MYDS")
        val FLD1 = MYDS.getFieldByName("FLD1")
        val FLD2 = MYDS.getFieldByName("FLD2")
        assertEquals(null, FLD1.explicitStartOffset)
        assertEquals(null, FLD1.explicitEndOffset)
        assertEquals(0, FLD1.startOffset)
        assertEquals(5, FLD1.endOffset)
        assertEquals(null, FLD2.explicitStartOffset)
        assertEquals(null, FLD2.explicitEndOffset)
        assertEquals(5, FLD2.startOffset)
        assertEquals(15, FLD2.endOffset)

        assertEquals(5, FLD1.size)
        assertEquals(5, FLD1.elementSize())

        assertEquals(10, FLD2.size)
        assertEquals(10, FLD2.elementSize())

        assertEquals(DataStructureType(listOf(
                // In datastruct if not specified default to ZONED (S)
                FieldType("FLD1", NumberType(5, 0, "S")),
                FieldType("FLD2", StringType(10))
        ), 15), MYDS.type)

        assertEquals(15, MYDS.elementSize())

        execute(cu, mapOf())
    }

    /**
     * Test for QUALIFIED support
     */
    @Test
    fun parseSTRUCT_02() {
        assertCanBeParsed("struct/STRUCT_02", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_02", true)
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    @Test
    fun parseSTRUCT_03_recognizeOffsets() {
        assertCanBeParsed("struct/STRUCT_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_03", true)
        cu.resolveAndValidate()

        val CURTIMSTP = cu.getDataDefinition("CURTIMSTP")
        val CURTIMDATE = CURTIMSTP.getFieldByName("CURTIMDATE")
        val CURRYEAR = CURTIMSTP.getFieldByName("CURRYEAR")
        val CURRMONTH = CURTIMSTP.getFieldByName("CURRMONTH")
        val CURRDAY = CURTIMSTP.getFieldByName("CURRDAY")
        val CURRHRS = CURTIMSTP.getFieldByName("CURRHRS")
        val CURRMINS = CURTIMSTP.getFieldByName("CURRMINS")
        val CURRSECS = CURTIMSTP.getFieldByName("CURRSECS")

        assertEquals(0, CURTIMDATE.explicitStartOffset)
        assertEquals(0, CURRYEAR.explicitStartOffset)
        assertEquals(4, CURRMONTH.explicitStartOffset)
        assertEquals(6, CURRDAY.explicitStartOffset)
        assertEquals(8, CURRHRS.explicitStartOffset)
        assertEquals(10, CURRMINS.explicitStartOffset)
        assertEquals(12, CURRSECS.explicitStartOffset)

        assertEquals(8, CURTIMDATE.explicitEndOffset)
        assertEquals(4, CURRYEAR.explicitEndOffset)
        assertEquals(6, CURRMONTH.explicitEndOffset)
        assertEquals(8, CURRDAY.explicitEndOffset)
        assertEquals(10, CURRHRS.explicitEndOffset)
        assertEquals(12, CURRMINS.explicitEndOffset)
        assertEquals(16, CURRSECS.explicitEndOffset)

        assertEquals(Pair(0, 8), CURTIMDATE.offsets)
        assertEquals(Pair(0, 4), CURRYEAR.offsets)
        assertEquals(Pair(4, 6), CURRMONTH.offsets)
        assertEquals(Pair(6, 8), CURRDAY.offsets)
        assertEquals(Pair(8, 10), CURRHRS.offsets)
        assertEquals(Pair(10, 12), CURRMINS.offsets)
        assertEquals(Pair(12, 16), CURRSECS.offsets)
    }

    @Test
    fun parseSTRUCT_03() {
        assertCanBeParsed("struct/STRUCT_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_03", true)
        val si = CollectorSystemInterface().apply {
            printOutput = true
        }
        cu.resolveAndValidate()
        execute(cu, mapOf(), si)
    }

    @Test
    @Ignore // I am not sure we should handle the definition of two consecutive DS
    fun parseSTRUCT_04() {
        assertCanBeParsed("struct/STRUCT_04", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_04", true)
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    /**
     * Test for TEMPLATE and LIKEDS support
     */
    @Test
    fun parseSTRUCT_05() {
        assertCanBeParsed("struct/STRUCT_05", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_05", true)
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    /**
     * Test for all data type
     */
    @Test
    fun parseSTRUCT_06_ast() {
        assertCanBeParsed("struct/STRUCT_06", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_06", true)
        cu.resolveAndValidate()

        val MYDS = cu.getDataDefinition("MYDS")

        val FILLER1 = MYDS.getFieldByName("FILLER1")
        assertEquals(Pair(0, 6), FILLER1.offsets)
        assertEquals(StringType(6), FILLER1.type)
        assertEquals(6, FILLER1.size)

        val PAC030 = MYDS.getFieldByName("PAC030")
        assertEquals(Pair(6, 8), PAC030.offsets)
        assertEquals(NumberType(3, 0, "P"), PAC030.type)
        assertEquals(2, PAC030.size)

        val PAC040 = MYDS.getFieldByName("PAC040")
        assertEquals(Pair(14, 17), PAC040.offsets)
        assertEquals(NumberType(4, 0, "P"), PAC040.type)
        assertEquals(3, PAC040.size)

        val PAC113 = MYDS.getFieldByName("PAC113")
        assertEquals(NumberType(8, 3, "P"), PAC113.type)
        assertEquals(6, PAC113.size)

        val INT001 = MYDS.getFieldByName("INT001")
        assertEquals(NumberType(3, 0, "I"), INT001.type)
        assertEquals(1, INT001.size)
    }

    @Test
    fun numberTypeSizeForPackedInteger() {
        assertEquals(1, NumberType(1, 0, "P").size)
        assertEquals(2, NumberType(2, 0, "P").size)
        assertEquals(2, NumberType(3, 0, "P").size)
        assertEquals(3, NumberType(4, 0, "P").size)
        assertEquals(3, NumberType(5, 0, "P").size)
        assertEquals(4, NumberType(6, 0, "P").size)
        assertEquals(4, NumberType(7, 0, "P").size)
        assertEquals(5, NumberType(8, 0, "P").size)
        assertEquals(5, NumberType(9, 0, "P").size)
        assertEquals(6, NumberType(10, 0, "P").size)
    }

    @Test
    fun numberTypeSizeForPackedDecimal() {
        assertEquals(6, NumberType(8, 3, "P").size)
    }

    /**
     * Test for all data type
     */
    @Test
    fun parseSTRUCT_06() {
        assertCanBeParsed("struct/STRUCT_06", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_06", true)
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    @Test
    fun compressionIntoDSofPackedValue() {
        val PAC030 = FieldDefinition(
            name = "PAC030",
            type = NumberType(3, 0, RpgType.PACKED),
            explicitStartOffset = null,
            explicitEndOffset = null,
            calculatedStartOffset = 6,
            calculatedEndOffset = 8,
            overriddenContainer = null,
            position = null,
            declaredArrayInLineOnThisField = null
        )
        val encodedValue = PAC030.toDataStructureValue(IntValue(999))
        assertEquals(2, encodedValue.value.length)
    }

//    private fun stringFromBytes(vararg bytes: Int) : String {
//        return String(bytes.map {
//            if (it > 127) {
//                it - 256
//            } else {
//                it
//            }.toByte()
//        }.toByteArray())
//    }
//
//    @Test
//    fun encodeToZonedTest() {
//        val e = stringFromBytes(0xF5)
//        val a = encodeToZoned(BigDecimal(5), 1, 0)
//        assertEquals(stringFromBytes(0xF5), encodeToZoned(BigDecimal(5), 1, 0))
//        assertEquals(stringFromBytes(0xD5), encodeToZoned(BigDecimal(-5), 1, 0))
//        assertEquals(stringFromBytes(0x03, 0x04, 0xD5), encodeToZoned(BigDecimal(-345), 3, 0))
//    }

    /**
     * Test for all data type
     */
    @Test
    @Ignore
    open fun parseSTRUCT_06_runtime() {
        assertCanBeParsed("struct/STRUCT_06", withMuteSupport = true)

        val cu = assertASTCanBeProduced(
            exampleName = "struct/STRUCT_06",
            considerPosition = true,
            withMuteSupport = true
        )
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        val failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
    /**
     * Test for all data type
     */
    @Test
    fun parseSTRUCT_07_runtime() {
        assertCanBeParsed("struct/STRUCT_07", withMuteSupport = true)

        val cu = assertASTCanBeProduced(
            exampleName = "struct/STRUCT_07",
            considerPosition = true,
            withMuteSupport = true
        )
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        val annotations = interpreter.getSystemInterface().getExecutedAnnotation().toSortedMap()
        val failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseSTRUCT_80TypeTest() {

        val ds1 = OccurableDataStructureType(
            dataStructureType = DataStructureType(
                fields = listOf(
                    FieldType("FLDA", StringType(5, false)),
                    FieldType("FLDB", StringType(75, false))
                ),
                elementSize = 80),
            occurs = 50
        )

        val ds2 = DataStructureType(
            fields = listOf(
                FieldType("FLDC", StringType(6, false)),
                FieldType("FLDD", StringType(5, false))
            ),
            elementSize = 11
        )
        val expectedDSTypes = mapOf(
            "DS1" to ds1,
            "DS2" to ds2
        )
        val actualDSTypes = mutableMapOf<String, Type>()
        val r = assertCanBeParsed("struct/STRUCT_08", withMuteSupport = true)
        for (stat in r.statement()) {
            stat.dcl_ds()?.apply {
                val fieldsList = calculateFieldInfos()
                actualDSTypes[stat.dcl_ds().name] = this.type(this.declaredSize(), fieldsList)
            }
        }
        assertEquals(expectedDSTypes, actualDSTypes)
    }

    @Test
    fun executeSTRUCT_08() {
        val exampleName = "struct/STRUCT_08"
        executePgm(programName = exampleName, configuration = Configuration().apply { options.muteSupport = true })
    }

    // This test must fail with error
    @Test
    fun executeSTRUCT_09() {
        val expectedErrors = listOf(
            "Program STRUCT_09 - Issue executing OccurStmt at line 10. OCCUR not supported. DS2 must be an DS defined with OCCURS keyword"
        )
        testError(exampleName = "struct/STRUCT_09", expectedErrors = expectedErrors)
    }

    private fun testError(exampleName: String, expectedErrors: List<String>) {
        val errorMessages = mutableListOf<String>()
        val configuration = Configuration().apply {
            jarikoCallback.onError = { errorEvent ->
                println(errorEvent)
                errorEvent.error.message?.let {
                    errorMessages.add(it)
                }
            }
        }
        kotlin.runCatching {
            executePgm(programName = exampleName, configuration = configuration)
        }.onSuccess {
            fail("This program must fail")
        }.onFailure {
            if (expectedErrors != errorMessages) {
                it.printStackTrace()
            }
            assertEquals(expectedErrors, errorMessages)
        }
    }
}
