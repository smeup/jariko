package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT03IndicatorTest : MULANGTTest() {
    /**
     * Executes IF statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00121() {
        val expected = listOf("Hello there")
        assertEquals(expected, "smeup/MUDRNRAPU00121".outputOf())
    }

    /**
     * Executes DOWxx (in this case, DOWNE) statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00122() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00122".outputOf())
    }

    /**
     * Executes DOUxx (in this case, DOUEQ) statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00123() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00123".outputOf())
    }
}