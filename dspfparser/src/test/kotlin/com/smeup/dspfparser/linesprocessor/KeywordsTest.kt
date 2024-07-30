package com.smeup.dspfparser.linesprocessor

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
        val keyword = DSPFKeyword.fromString("SFL")
        assertEquals("SFL", keyword.name)
    }

    @Test
    fun constant() {
        val first = DSPFKeyword.fromString("'* Constant message! *'")
        assertEquals("* Constant message! *", first.name)
        val second = DSPFKeyword.fromString("''* Constant message! *''")
        assertEquals("'* Constant message! *'", second.name)
    }

    @Test
    fun constantGroup() {
        val firsts = DSPFKeywordsGroup.fromString("'* Constant message! *'")
        assertEquals("* Constant message! *", firsts.group[0].name)
        val seconds = DSPFKeywordsGroup.fromString("''* Constant message! *''")
        assertEquals("'* Constant message! *'", seconds.group[0].name)
    }
}
