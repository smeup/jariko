package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT02ConstAndDSpecTest : MULANGTTest() {
    /**
     * I cannot calculate the size of DS5_FL1 from the overlaying fields as there are none
     * @see #24
     */
    @Test
    fun executeT02_A40_P05() {
        val expected = listOf("333,zz")
        assertEquals(expected, "smeup/T02_A40_P05".outputOf())
    }
}