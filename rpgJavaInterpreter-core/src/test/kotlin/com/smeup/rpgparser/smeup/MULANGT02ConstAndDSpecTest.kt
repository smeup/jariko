package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT02ConstAndDSpecTest : MULANGTTest() {
    /**
     * Data reference - Definizione variabile sia inline che in file
     * @see #253
     */
    @Test
    fun executeT02_A80_P04() {
        val expected = listOf("ABCDEFGHIJ")
        assertEquals(expected, "smeup/T02_A80_P04".outputOf(configuration = smeupConfig))
    }
}