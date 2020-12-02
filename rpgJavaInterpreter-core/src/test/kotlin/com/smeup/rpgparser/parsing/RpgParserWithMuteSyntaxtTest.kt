package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.AbstractTestCase
import com.smeup.rpgparser.assertCanBeParsedResult
import org.junit.Test

open class RpgParserWithMuteSyntaxtTest : AbstractTestCase() {

    @Test
    fun parseMUTE01_syntax() {
        assertCanBeParsedResult("mute/MUTE01_SYNTAX", withMuteSupport = true)
    }

    @Test
    fun parseMUTE01_ast() {
        assertASTCanBeProduced("mute/MUTE01_SYNTAX", considerPosition = true, withMuteSupport = true)
    }
}
