package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT40ArrayAndDSTest : MULANGTTest() {
    /**
     * Reset with inline declaration
     * @see #242
     */
    @Test
    fun executeT40_A10_P10() {
        val expected = listOf("Contenuto Pre-RESET: AAA - Contenuto Post-RESET:")
        assertEquals(expected, "smeup/T40_A10_P10".outputOf(configuration = smeupConfig))
    }

    /**
     * Field of DS with LIKE to field of next DS.
     */
    @Test
    fun executeMU401012() {
        val expected = listOf("HELLOTHERE")
        assertEquals(expected, "smeup/MU401012".outputOf(configuration = smeupConfig))
    }
}