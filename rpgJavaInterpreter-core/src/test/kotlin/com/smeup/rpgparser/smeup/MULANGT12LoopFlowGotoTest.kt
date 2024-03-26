package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT12LoopFlowGotoTest : MULANGTTest() {
    /**
     * If followed by Do
     * @see #263
     */
    @Test
    fun executeT12_A04_P18() {
        val expected = listOf("A04_N50_CNT(10) A04_STR(1) A04_END(10) A04_CNT(11)")
        assertEquals(expected, "smeup/T12_A04_P18".outputOf())
    }
}