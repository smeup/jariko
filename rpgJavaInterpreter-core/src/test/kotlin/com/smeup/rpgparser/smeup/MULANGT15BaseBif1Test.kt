package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.parsing.parsetreetoast.AstResolutionError
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

open class MULANGT15BaseBif1Test : MULANGTTest() {
    /**
     * %INTH with half adjustment
     * @see #260
     */
    @Test
    fun executeT15_A30_P03() {
        val expected = listOf("12346")
        assertEquals(expected, "smeup/T15_A30_P03".outputOf())
    }

    /**
     * %INTH with half adjustment (100.000 times)
     * @see #260
     */
    @Test
    fun executeT15_A30_P05() {
        val expected = listOf("12346")
        assertEquals(expected, "smeup/T15_A30_P05".outputOf())
    }

    /**
     * %INTH with seconds half adjustment
     * @see #258
     */
    @Test
    fun executeT15_A30_P06() {
        val expected = listOf("219120.00000")
        assertEquals(expected, "smeup/T15_A30_P06".outputOf())
    }

    /**
     * %INTH with pre-half adjustment
     * @see #259
     */
    @Test
    fun executeT15_A30_P07() {
        val expected = listOf("Min(219120.00000) Cen(219132.00000)")
        assertEquals(expected, "smeup/T15_A30_P07".outputOf())
    }

    /**
     * %LEN near EVAL
     * @see #261
     */
    @Test
    fun executeT15_A80_P09() {
        val expected = listOf("1(10 - North York) 2(5 - North) 3(15 - North          )")
        assertEquals(expected, "smeup/T15_A80_P09".outputOf())
    }

    /**
     * %LOOKUPxx tests
     * @see #LS24002887
     */
    @Test
    fun executeMUDRNRAPU00210() {
        val expected = listOf("3", "0", "0", "3", "3", "0", "0", "3", "2", "2", "3", "3")
        assertEquals(expected, "smeup/MUDRNRAPU00210".outputOf())
    }

    /**
     * %LOOKUPxx fails with arrays that are not sequenced
     * @see #LS24002887
     */
    @Test
    fun executeMUDRNRAPU00211() {
        assertFailsWith(RuntimeException::class) {
            "smeup/MUDRNRAPU00211".outputOf(configuration = smeupConfig)
        }
    }
}