package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT15BaseBif1Test : MULANGTTest() {
    /**
     * %LEN near EVAL
     * @see #261
     */
    @Test
    fun executeT15_A80_P09() {
        val expected = listOf("1(10 - North York) 2(5 - North) 3(15 - North          )")
        assertEquals(expected, "smeup/T15_A80_P09".outputOf())
    }
}