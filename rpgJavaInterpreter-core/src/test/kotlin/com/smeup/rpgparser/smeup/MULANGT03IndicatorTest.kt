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

    /**
     * Executes DO statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00124() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00124".outputOf())
    }

    /**
     * Executes FOR statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00125() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00125".outputOf())
    }

    /**
     * Executes DOU statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00129() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00129".outputOf())
    }

    /**
     * Executes DOW statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00130() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00130".outputOf())
    }

    /**
     * Execute a CALL with error state bound to an indicator where the error is originated externally
     * @see #LS25002734
     */
    @Test
    fun executeMUDRNRAPU00292() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00292".outputOf())
    }
}