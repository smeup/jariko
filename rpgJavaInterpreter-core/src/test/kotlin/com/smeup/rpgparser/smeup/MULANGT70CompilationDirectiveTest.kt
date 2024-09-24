package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT70CompilationDirectiveTest : MULANGTTest() {
    /**
     * Data definition under dependencies.
     * @see #265
     */
    @Test
    fun executeMU711001() {
        val expected = listOf("Codice:")
        assertEquals(expected, "smeup/MU711001".outputOf(configuration = smeupConfig))
    }

    /**
     * Data definition under dependencies.
     * @see #265
     */
    @Test
    fun executeMU711002() {
        val expected = listOf("Decodifica base: Decodifica completa:")
        assertEquals(expected, "smeup/MU711002".outputOf(configuration = smeupConfig))
    }

    /**
     * Data definition under dependencies.
     * @see #265
     */
    @Test
    fun executeMU711003() {
        val expected = listOf("A71_01(0) A71_02(3) A71_03(F)")
        // Since QILEGEN,£PRZ is not formally correct API I override the API resolution.
        val myConfig = smeupConfig.copy().apply { jarikoCallback.onApiInclusion = { _, _ -> } }
        assertEquals(expected, "smeup/MU711003".outputOf(configuration = myConfig))
    }

    /**
     * Data definition under dependencies.
     * @see #265
     */
    @Test
    fun executeMU711004() {
        val expected = listOf("A71_01(0) A71_02(3) A71_03(F)")
        // Since QILEGEN,£C5PES is not formally correct API I override the API resolution.
        val myConfig = smeupConfig.copy().apply { jarikoCallback.onApiInclusion = { _, _ -> } }
        assertEquals(expected, "smeup/MU711004".outputOf(configuration = myConfig))
    }

    /**
     * COPY where its name has lower alphabetic character.
     * @see #LS24003436
     */
    @Test
    fun executeMU711006() {
        val expected = listOf("HELLO THERE")
        assertEquals(expected, "smeup/MU711006".outputOf(configuration = smeupConfig))
    }

    /**
     * Data Definition with LIKE of another variable imported with API directive
     * @see #LS24003689
     */
    @Test
    fun executeMUDRNRAPU00103() {
        val expected = listOf("HELLO THERE")
        assertEquals(expected, "smeup/MUDRNRAPU00103".outputOf(configuration = smeupConfig))
    }
}