package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT02ConstAndDSpecTest : MULANGTTest() {
    /**
     * Data reference - Inline definition
     * @see #250
     */
    @Test
    fun executeT02_A80_P01() {
        val expected = listOf("ABCDEFGHIJ12345")
        assertEquals(expected, "smeup/T02_A80_P01".outputOf())
    }

    /**
     * Data reference - Inline definition and with prefix External DS
     * @see #254
     */
    @Test
    fun executeT02_A80_P05() {
        val expected = listOf("ABCDEFGHIJ123.00000")
        assertEquals(expected, "smeup/T02_A80_P05".outputOf(configuration = smeupConfig))
    }
}