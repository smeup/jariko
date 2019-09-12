package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test


public class RpgParserOverlayTest12 {

    @Test
    fun parseMUTE12_01_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_01", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_01_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_01", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_01_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_01", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }



    @Test
    fun parseMUTE12_02_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_02", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_02_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_02", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_02_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_02", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }

    @Test
    fun parseMUTE12_03_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_03", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_03_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_03_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }


    @Test
    fun parseMUTE12_04_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_04", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_04_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_04", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_04_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_04", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }

    @Test
    fun parseMUTE12_05_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_05", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_05_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_05", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_05_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_05", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }
    @Test
    fun parseMUTE12_06_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_06", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_06_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_06_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }

}
