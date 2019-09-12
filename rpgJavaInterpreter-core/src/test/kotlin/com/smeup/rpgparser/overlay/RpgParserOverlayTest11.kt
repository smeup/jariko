package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test


public class RpgParserOverlayTest11 {

    @Test
    fun parseMUTE11_11C_syntax() {
        val result = assertCanBeParsed("overlay/MUTE11_11C", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_11C", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_11C", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }


    @Test
    fun parseMUTE11_15_syntax() {
        val result = assertCanBeParsed("overlay/MUTE11_15", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_15_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_15", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_15_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_15", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }


    @Test
    fun parseMUTE11_16_syntax() {
        val result = assertCanBeParsed("overlay/MUTE11_16", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_16_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_16", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_16_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_16", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }

    fun parseMUTE11_16A_syntax() {
        val result = assertCanBeParsed("overlay/MUTE11_16A", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_16A_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_16A", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_16A_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_16A", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }





}
