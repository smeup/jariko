package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test


public class RpgParserOverlayTest09 {

    @Test
    fun parseMUTE09_02_syntax() {
        val result = assertCanBeParsed("overlay/MUTE09_02", withMuteSupport = true)
    }

    @Test
    fun parseMUTE09_02_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE09_02_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }


}
