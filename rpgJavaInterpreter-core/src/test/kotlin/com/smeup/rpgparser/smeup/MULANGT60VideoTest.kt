package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

open class MULANGT60VideoTest : MULANGTTest() {

    @Test
    fun executeT60_A10_P01() {
        val expected = listOf<String>("KA(0)KB(0)KC(0)KD(0)KE(0)KF(0)KG(0)KH(0)KI(0)KJ(0)KK(0)KL(0)KM(0)KN(0)KP(0)KQ(0)KR(0)KS(0)KT(0)KU(0)KV(0)KW(0)KX(0)KY(0)")
        assertEquals(expected, "smeup/T60_A10_P01".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeT60_A10_P01_02() {
        val expected = listOf<String>()
        assertEquals(expected, "smeup/T60_A10_P01-02".outputOf(configuration = smeupConfig))
    }

    /**
     * Data reference - Inline definition and in video file
     * @see #252
     */
    @Test
    fun executeT60_A40_P01() {
        val expected = listOf("5")
        assertEquals(expected, "smeup/T60_A40_P01".outputOf(configuration = smeupConfig))
    }
}