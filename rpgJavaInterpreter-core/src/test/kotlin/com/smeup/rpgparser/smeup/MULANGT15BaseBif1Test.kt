package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT15BaseBif1Test : MULANGTTest() {
    /**
     * %INTH with half adjustment
     * @see #260
     */
    @Test
    fun executeT15_A30_P03() {
        val expected = listOf("12346")
        assertEquals(expected, "smeup/T15_A30_P03".outputOf())
    }

    /**
     * %INTH with half adjustment (100.000 times)
     * @see #260
     */
    @Test
    fun executeT15_A30_P05() {
        val expected = listOf("12346")
        assertEquals(expected, "smeup/T15_A30_P05".outputOf())
    }

    /**
     * %INTH with seconds half adjustment
     * @see #258
     */
    @Test
    fun executeT15_A30_P06() {
        val expected = listOf("219120.00000")
        assertEquals(expected, "smeup/T15_A30_P06".outputOf())
    }

    /**
     * %INTH with pre-half adjustment
     * @see #259
     */
    @Test
    fun executeT15_A30_P07() {
        val expected = listOf("Min(219120.00000) Cen(219132.00000)")
        assertEquals(expected, "smeup/T15_A30_P07".outputOf())
    }
}