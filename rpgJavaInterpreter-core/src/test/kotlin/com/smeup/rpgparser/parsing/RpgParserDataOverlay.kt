package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test


class RpgParserDataOverlay {

    /**
     * Test Structure Overlay
     */
    @Test
    fun parseOVERLAY_01() {
        val result = assertCanBeParsed("struct/OVERLAY_01", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_01", true)
        cu.resolve()
        execute(cu, mapOf())

    }

    /**
     * Test Field Overlay
     */
    @Test
    fun parseOVERLAY_02() {
        val result = assertCanBeParsed("struct/OVERLAY_02", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_02", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_03() {
        val result = assertCanBeParsed("struct/OVERLAY_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_03", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun parseOVERLAY_04() {
        val result = assertCanBeParsed("struct/OVERLAY_04", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_04", true)
        cu.resolve()
        execute(cu, mapOf())

    }

    @Test
    fun parseOVERLAY_05() {
        val result = assertCanBeParsed("struct/OVERLAY_05", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/OVERLAY_05", true)
        cu.resolve()
        execute(cu, mapOf())
    }




}