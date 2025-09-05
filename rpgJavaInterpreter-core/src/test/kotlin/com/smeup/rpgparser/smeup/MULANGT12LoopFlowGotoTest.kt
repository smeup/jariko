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

    /**
     * If followed by Do
     * @see #264
     */
    @Test
    fun executeT12_A04_P19() {
        val expected = listOf("DO_1(A04_N50_CNT(10) A04_STR(1) A04_END(10) A04_CNT(11)) DO_2(A04_N50_CNT(15) A04_CNT(16))")
        assertEquals(expected, "smeup/T12_A04_P19".outputOf())
    }

    /**
     * SELECT-WHEN with complex boolean expression
     * @see #236
     */
    @Test
    fun executeT12_A05_P12() {
        val expected = listOf("PrimoWhen")
        assertEquals(expected, "smeup/T12_A05_P12".outputOf())
    }

    /**
     * IF with complex condition until column 80, followed by comment without space
     */
    @Test
    fun executeMU120214() {
        val expected = listOf("OK")
        assertEquals(expected, "smeup/MU120214".outputOf())
    }

    /**
     * DOUEQ
     */
    @Test
    fun executeMU120901() {
        val expected = listOf("A09_N2: 1")
        assertEquals(expected, "smeup/MU120901".outputOf())
    }

    /**
     * DOUNE
     */
    @Test
    fun executeMU120902() {
        val expected = listOf("A09_N2: 1")
        assertEquals(expected, "smeup/MU120902".outputOf())
    }

    /**
     * DOUGT
     */
    @Test
    fun executeMU120903() {
        val expected = listOf("A09_N1: 11")
        assertEquals(expected, "smeup/MU120903".outputOf())
    }

    /**
     * DOUGE
     */
    @Test
    fun executeMU120904() {
        val expected = listOf("A09_N1: 10")
        assertEquals(expected, "smeup/MU120904".outputOf())
    }

    /**
     * DOULT
     */
    @Test
    fun executeMU120905() {
        val expected = listOf("A09_N2: 11")
        assertEquals(expected, "smeup/MU120905".outputOf())
    }

    /**
     * DOULE
     */
    @Test
    fun executeMU120906() {
        val expected = listOf("A09_N2: 10")
        assertEquals(expected, "smeup/MU120906".outputOf())
    }

    /**
     * DOWEQ with indicator into SR
     */
    @Test
    fun executeMU120907() {
        val expected = listOf("HELLO THERE")
        assertEquals(expected, "smeup/MU120907".outputOf())
    }
}
