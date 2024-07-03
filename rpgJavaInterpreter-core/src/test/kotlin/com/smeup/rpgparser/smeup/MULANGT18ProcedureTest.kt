package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT18ProcedureTest : MULANGTTest() {
    /**
     * LIKE to variable defined into a COPY. This one is also declared inner of procedure.
     * @see #LS24003187
     */
    @Test
    fun executeT10_A20_P52() {
        val expected = listOf("O:   HT     -P:HT_P")
        assertEquals(expected, "smeup/MU181003".outputOf(configuration = smeupConfig))
    }
}