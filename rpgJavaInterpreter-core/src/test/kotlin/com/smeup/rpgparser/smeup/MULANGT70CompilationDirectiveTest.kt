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
        assertEquals(expected, "smeup/MU711003".outputOf(configuration = smeupConfig))
    }

    /**
     * Data definition under dependencies.
     * @see #265
     */
    @Test
    fun executeMU711004() {
        val expected = listOf("A71_01(0) A71_02(3) A71_03(F)")
        assertEquals(expected, "smeup/MU711004".outputOf(configuration = smeupConfig))
    }
}