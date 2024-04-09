package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT70CompilationDirectiveTest : MULANGTTest() {
    /**
     * ###################
     * ATOMIC TEST SECTION
     * ###################
     */

    /**
     * Data reference £DEC £OAV £HEX.
     * @see #265
     */
    @Test
    fun executeMU711001() {
        val expected = listOf("Codice: ATTVTSD8*DMY250324G/10")
        assertEquals(expected, "smeup/MU711001".outputOf(configuration = smeupConfig))
    }
}