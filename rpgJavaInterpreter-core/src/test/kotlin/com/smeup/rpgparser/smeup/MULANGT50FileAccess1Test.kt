package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT50FileAccess1Test : MULANGTTest() {
    /**
     * Data Reference to DS
     * @see #280
     */
    @Test
    fun executeMU500802() {
        val expected = listOf("")
        assertEquals(expected, "smeup/MU500802".outputOf(configuration = smeupConfig))
    }
}