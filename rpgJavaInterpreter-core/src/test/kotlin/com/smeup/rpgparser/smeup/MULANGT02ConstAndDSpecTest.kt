package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT02ConstAndDSpecTest : MULANGTTest() {
    /**
     * Data reference - DS with 2 arrays defined with overlay
     * @see #247
     */
    @Test
    fun executeT02_A40_P03() {
        val expected = listOf("CNCLICNCLIAAAABBBBBAAAABBBBBCNFORCNFORCCCCDDDDDCCCCDDDDDCNCOLCNCOLEEEEFFFFFEEEEFFFFF")
        assertEquals(expected, "smeup/T02_A40_P03".outputOf())
    }

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
     * Definition with Like to a variable defined also with like.
     * @see #160
     */
    @Test
    fun executeT02_A50_P02() {
        val expected = listOf("A50_A3(       ) A50_A4(       )")
        assertEquals(expected, "smeup/T02_A50_P02".outputOf())
    }

    /**
     * Data reference - Definition both inline and file
     * @see #253
     */
    @Test
    fun executeT02_A80_P04() {
        val expected = listOf("ABCDEFGHIJ")
        assertEquals(expected, "smeup/T02_A80_P04".outputOf(configuration = smeupConfig))
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