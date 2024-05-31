package com.smeup.dspfparser.linesprocessor

import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

internal class KeywordsTest {
    @Test
    fun withParameters() {
        val keyword = DSPFKeyword.fromString("DATSEP(';')")
        assertEquals("DATSEP", keyword.name)
    }

    @Test
    fun unsigned() {
        val sfl = DSPFKeyword.fromString("SFL")
        assertEquals("SFL", sfl.name)
    }

    @Test
    fun constant() {
        val constant = DSPFKeyword.fromString("'* Constant message! *'")
        assertEquals("* Constant message! *", constant.name)
    }

    @Test @Ignore
    fun group() {
        val keywords = DSPFKeywordsGroup.fromString("'* Constant message! *' SFL DATSEP('-')")
    }
}
