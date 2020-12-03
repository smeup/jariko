package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertCanBeParsedResult
import org.junit.Test

open class RpgParserWithMuteSyntaxtTest : AbstractTest() {

    @Test
    fun parseMUTE01_syntax() {
        assertCanBeParsedResult("mute/MUTE01_SYNTAX", withMuteSupport = true)
    }

    @Test
    fun parseMUTE01_ast() {
        assertASTCanBeProduced("mute/MUTE01_SYNTAX", considerPosition = true, withMuteSupport = true)
    }
}
