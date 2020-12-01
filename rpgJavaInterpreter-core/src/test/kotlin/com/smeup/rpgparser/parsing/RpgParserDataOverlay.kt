package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.AbstractTestCase
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import kotlin.test.assertEquals

class RpgParserDataOverlay : AbstractTestCase() {

    /**
     * Test Structure Overlay
     */
    @Test
    fun parseOVERLAY_01() {
        assertCanBeParsed("struct/OVERLAY_01", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_01", true)
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
    }

    /**
     * Test Field Overlay
     */
    @Test
    fun parseOVERLAY_02() {
        assertCanBeParsed("struct/OVERLAY_02", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_02", true)
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_03() {
        assertCanBeParsed("struct/OVERLAY_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_03", true)
        cu.resolveAndValidate(DummyDBInterface)
        val dataDefinition = cu.dataDefinitions[0]
        assertEquals(1, cu.dataDefinitions.size)

        val fieldPartnum = dataDefinition.fields.find { it.name == "PARTNUM" }!!
        assertEquals(null, fieldPartnum.explicitStartOffset)
        assertEquals(0, fieldPartnum.startOffset)
        assertEquals(10, fieldPartnum.endOffset)

        val fieldFamily = dataDefinition.fields.find { it.name == "FAMILY" }!!
        assertEquals(null, fieldFamily.explicitStartOffset)
        assertEquals(0, fieldFamily.startOffset)
        assertEquals(3, fieldFamily.endOffset)

        val fieldSequence = dataDefinition.fields.find { it.name == "SEQUENCE" }!!
        assertEquals(null, fieldSequence.explicitStartOffset)
        assertEquals(3, fieldSequence.startOffset)
        assertEquals(9, fieldSequence.endOffset)

        val fieldLanguage = dataDefinition.fields.find { it.name == "LANGUAGE" }!!
        assertEquals(null, fieldLanguage.explicitStartOffset)
        assertEquals(9, fieldLanguage.startOffset)
        assertEquals(10, fieldLanguage.endOffset)

        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_04() {
        assertCanBeParsed("struct/OVERLAY_04", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_04", true)
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_05() {
        assertCanBeParsed("struct/OVERLAY_05", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_05", true)
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
    }
}
