package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class RpgSortATest : AbstractTest() {

    @Test
    fun encodeDecodeCp037() {
        val charset = Charset.forName("Cp037")
        val s1 = StringValue("{V5STAT0F")
        val s2 = StringValue("ÂV5STAT0F")

        assertTrue(s1.compare(s2, charset) > 0)
        assertTrue(s1.compare(s2, charset, true) < 0)
    }

    @Test
    fun encodeDecodeCp280() {
        val charset = Charset.forName("Cp280")
        val s1 = StringValue("{V5STAT0F")
        val s2 = StringValue("ÂV5STAT0F")

        assertTrue(s1.compare(s2, charset) < 0)
        assertTrue(s1.compare(s2, charset, true) > 0)
    }

    fun getField(field: FieldDefinition, value: StringValue): String {
        return value.value.substring(field.startOffset, field.endOffset)
    }
    @Test
    fun executeSORTA() {
        val cu = assertASTCanBeProduced("overlay/SORTATEST", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val YFLD_FIL = cu.getDataOrFieldDefinition("YFLD_FIL") as FieldDefinition
        val YFLD_FLD = cu.getDataOrFieldDefinition("YFLD_FLD") as FieldDefinition
        val sorted = (interpreter["YFLD"] as ProjectedArrayValue)

        assertEquals(getField(YFLD_FIL, sorted.getElement(1).asString()), "A5STAT0F  ")
        assertEquals(getField(YFLD_FLD, sorted.getElement(1).asString()), "E6CONT    ")

        assertEquals(getField(YFLD_FIL, sorted.getElement(2).asString()), "BEREFE0F  ")
        assertEquals(getField(YFLD_FLD, sorted.getElement(2).asString()), "D1COEN    ")

        assertEquals(getField(YFLD_FIL, sorted.getElement(3).asString()), "C5TREG0F  ")
        assertEquals(getField(YFLD_FLD, sorted.getElement(3).asString()), "C5NU01    ")

        assertEquals(getField(YFLD_FIL, sorted.getElement(4).asString()), "D5ICEE0F  ")
        assertEquals(getField(YFLD_FLD, sorted.getElement(4).asString()), "B5MTRA    ")

        assertEquals(getField(YFLD_FIL, sorted.getElement(5).asString()), "E5ICEE0F  ")
        assertEquals(getField(YFLD_FLD, sorted.getElement(5).asString()), "A5COCO    ")
    }
}
