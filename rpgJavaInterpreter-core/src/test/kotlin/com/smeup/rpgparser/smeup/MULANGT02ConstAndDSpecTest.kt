package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT02ConstAndDSpecTest : MULANGTTest() {
    /**
     * Like define di una variabile precedentemente dichiara tramite un altro like define
     * @see #160
     */
    @Test
    fun executeT02_A50_P02() {
        val expected = listOf("A50_A3(       ) A50_A4(       )")
        assertEquals(expected, "smeup/T02_A50_P02".outputOf())
    }
}