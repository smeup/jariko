package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT10BaseCodopTest : MULANGTTest() {
    /**
     * SCAN with array in input
     * @see #218
     */
    @Test
    fun executeT10_A35_P08() {
        val expected = listOf<String>("A35_AR1(2)(123&5) IN45(1)")
        assertEquals(expected, "smeup/T10_A35_P08".outputOf(configuration = smeupConfig))
    }

    /**
     * SCAN with array in result
     * @see #244
     */
    @Test
    fun executeT10_A35_P10() {
        val expected = listOf<String>("A35_AR2(01)(5) A35_AR2(02)(6) A35_AR2(03)(0) IN20(1)")
        assertEquals(expected, "smeup/T10_A35_P10".outputOf(configuration = smeupConfig))
    }

    /**
     * Positive Check with indicator
     * @see #246
     */
    @Test
    fun executeT10_A45_P04() {
        val expected = listOf("IND(0)")
        assertEquals(expected, "smeup/T10_A45_P04".outputOf())
    }

    /**
     * Negative Check with indicator
     * @see #246
     */
    @Test
    fun executeT10_A45_P05() {
        val expected = listOf("IND(1)")
        assertEquals(expected, "smeup/T10_A45_P05".outputOf())
    }

    /**
     * Check with error indicator
     * @see #246
     */
    @Test
    fun executeT10_A45_P06() {
        val expected = listOf("IND(0)")
        assertEquals(expected, "smeup/T10_A45_P06".outputOf())
    }
}