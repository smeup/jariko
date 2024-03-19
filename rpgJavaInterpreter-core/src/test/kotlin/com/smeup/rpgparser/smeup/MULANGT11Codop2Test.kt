package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT11Codop2Test : MULANGTTest() {
    /**
     * MONITOR and error catching
     * @see #241
     */
    @Test
    fun executeT11_A10_P01() {
        val expected = listOf("BLOCCO; ERR_ZERO_DIV;")
        assertEquals(expected, "smeup/T11_A10_P01".outputOf())
    }

    /**
     * Nested MONITOR statements
     * @see #241
     */
    @Test
    fun executeT11_A10_P02() {
        val expected = listOf("BLOCCO1; BLOCCO2; BLOCCO3; ERR_ZERO_DIV; FINE_BLOCCO3;; ERR_ZERO_DIV; FINE_BLOCCO2;; ERR_ZERO_DIV; FINE_BLOCCO1;")
        assertEquals(expected, "smeup/T11_A10_P02".outputOf())
    }

    /**
     * MONITOR nested in IF, DO and SELECT
     * @see #241
     */
    @Test
    fun executeT11_A10_P03() {
        val expected = listOf("DENTRO_IF(BLOCCO; ERR_ZERO_DIV;) DENTRO_DO(BLOCCO; ERR_ZERO_DIV;BLOCCO; ERR_ZERO_DIV;) DENTRO_WHEN(BLOCCO; ERR_ZERO_DIV;) DENTRO_OTHER(BLOCCO; ERR_ZERO_DIV;)")
        assertEquals(expected, "smeup/T11_A10_P03".outputOf())
    }

    /**
     * IF, DO and SELECT in MONITOR
     * @see #241
     */
    @Test
    fun executeT11_A10_P04() {
        val expected = listOf("DENTRO_IF(BLOCCO; ERR_ZERO_DIV;) DENTRO_DO(BLOCCO; ERR_ZERO_DIV;) DENTRO_WHEN(BLOCCO; ERR_ZERO_DIV;) DENTRO_OTHER(BLOCCO; ERR_ZERO_DIV;)")
        assertEquals(expected, "smeup/T11_A10_P04".outputOf())
    }
}