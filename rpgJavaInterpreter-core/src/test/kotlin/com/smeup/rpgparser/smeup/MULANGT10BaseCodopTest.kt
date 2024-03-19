package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT10BaseCodopTest : MULANGTTest() {
    /**
     * DIV e MVR con indicatori
     * @see #245
     */
    @Test
    fun executeT10_A20_P52() {
        val expected = listOf("A20_N73(2.272) A20_N70(0) A20_N112(11.11) A20_N110(0) A20_N309(4.889964788)")
        assertEquals(expected, "smeup/T10_A20_P52".outputOf(configuration = smeupConfig))
    }
}