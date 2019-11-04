package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Ignore
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class RpgParserDataStruct {

    @Test
    fun parseSTRUCT_01_MYDS_isRecognizedCorrectly() {
        val cu = assertASTCanBeProduced("struct/STRUCT_01", true)
        cu.resolve()

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

        cu.resolve()

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
                FieldType("FLD1", NumberType(5, 0)),
                FieldType("FLD2", StringType(10))
        ), 15), MYDS.type)

//        HERE I GET AN ERROR AS THE ELEMENT SIZE IS 10, NOT FIFTEEN
//        THIS IS THE CASE BECAUSE THE EXPLICIT END OFFSET OF FLD2 IS RECOGNIZED AS 10, WHICH I THINK IS WRONG
//        WE SHOULD ASK CLARIFICATIONS ON THIS

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
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun parseSTRUCT_03_recognizeOffsets() {
        assertCanBeParsed("struct/STRUCT_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_03", true)
        cu.resolve()

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
    @Ignore // this is probably failing because of TIMESTAMP()
    fun parseSTRUCT_03() {
        assertCanBeParsed("struct/STRUCT_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_03", true)
        cu.resolve()

        execute(cu, mapOf())
    }

    @Test
    @Ignore // I am not sure we should handle the definition of two consecutive DS
    fun parseSTRUCT_04() {
        assertCanBeParsed("struct/STRUCT_04", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_04", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for TEMPLATE and LIKEDS support
     */
    @Test
    fun parseSTRUCT_05() {
        assertCanBeParsed("struct/STRUCT_05", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_05", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for all data type
     */
    @Test
    fun parseSTRUCT_06_ast() {
        assertCanBeParsed("struct/STRUCT_06", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_06", true)
        cu.resolve()

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
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun compressionIntoDSofPackedValue() {
        val PAC030 = FieldDefinition(name = "PAC030",
                type=NumberType(3, 0, RpgType.PACKED),
                explicitStartOffset = null,
                explicitEndOffset = null,
                calculatedStartOffset = 6,
                calculatedEndOffset = 8,
                overriddenContainer = null,
                position = null)
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
    fun parseSTRUCT_06_runtime() {
        assertCanBeParsed("struct/STRUCT_06", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_06", true)
        cu.resolve()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
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

        val cu = assertASTCanBeProduced("struct/STRUCT_07", true)
        cu.resolve()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}