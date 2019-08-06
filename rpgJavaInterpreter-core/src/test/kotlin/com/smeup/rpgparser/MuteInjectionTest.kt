package com.smeup.rpgparser

import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import org.junit.Test
import kotlin.test.assertEquals

class MuteInjectionTest {

    @Test
    fun mutesAtStartOfMainAreAttached() {
        val result = RpgParserFacade()
                .apply { this.muteSupport = true }
                .parse(inputStreamFor("mute/SIMPLE_MUTE"))
        assert(result.correct)
        val cu = result.root!!.rContext.toAst().apply {
            var resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)
            assertEquals(3, resolved.size)
        }
    }
}