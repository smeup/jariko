package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT10BaseCodopTest : MULANGTTest() {
    /**
     * Check con indicatore con verifica a buon fine
     * @see #246
     */
    @Test
    fun executeT10_A45_P04() {
        val expected = listOf("IND(0)")
        assertEquals(expected, "smeup/T10_A45_P04".outputOf())
    }

    /**
     * Check con indicatore con verifica non ok
     * @see #246
     */
    @Test
    fun executeT10_A45_P05() {
        val expected = listOf("IND(1)")
        assertEquals(expected, "smeup/T10_A45_P05".outputOf())
    }

    /**
     * Check con indicatore di errore
     * @see #246
     */
    @Test
    fun executeT10_A45_P06() {
        val expected = listOf("IND(0)")
        assertEquals(expected, "smeup/T10_A45_P06".outputOf())
    }
}