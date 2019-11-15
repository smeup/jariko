package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test
import kotlin.test.assertEquals

// @Ignore
class RpgParserDataOverlay {

    /**
     * Test Structure Overlay
     */
    @Test
    fun parseOVERLAY_01() {
        assertCanBeParsed("struct/OVERLAY_01", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_01", true)
        cu.resolve(DummyDBInterface)
        execute(cu, mapOf())
    }

    /**
     * Test Field Overlay
     */
    @Test
    fun parseOVERLAY_02() {
        assertCanBeParsed("struct/OVERLAY_02", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_02", true)
        cu.resolve(DummyDBInterface)
        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_03() {
        assertCanBeParsed("struct/OVERLAY_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_03", true)
        cu.resolve(DummyDBInterface)
        val dataDefinition = cu.dataDefinitions[0]
        assertEquals(1, cu.dataDefinitions.size)

        val fieldFamily = dataDefinition.fields.find { it.name == "FAMILY" }!!
        assertEquals(0, fieldFamily.explicitStartOffset)
        assertEquals(0, fieldFamily.startOffset)

        val fieldSequence = dataDefinition.fields.find { it.name == "SEQUENCE" }!!
        assertEquals(3, fieldSequence.explicitStartOffset)
        assertEquals(3, fieldSequence.startOffset)

        val fieldLanguage = dataDefinition.fields.find { it.name == "LANGUAGE" }!!
        assertEquals(9, fieldLanguage.explicitStartOffset)
        assertEquals(9, fieldLanguage.startOffset)

        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_04() {
        assertCanBeParsed("struct/OVERLAY_04", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_04", true)
        cu.resolve(DummyDBInterface)
        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_05() {
        assertCanBeParsed("struct/OVERLAY_05", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_05", true)
        cu.resolve(DummyDBInterface)
        execute(cu, mapOf())
    }
}
