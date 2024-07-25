package com.smeup.rpgparser.smeup

import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT10BaseCodopTest : MULANGTTest() {
    /**
     * DIV and MVR with indicators
     * @see #245
     */
    @Test
    fun executeT10_A20_P52() {
        val expected = listOf("A20_N73(2.272) A20_N70(0) A20_N112(11.11) A20_N110(0) A20_N309(4.889964788)")
        assertEquals(expected, "smeup/T10_A20_P52".outputOf(configuration = smeupConfig))
    }

    /**
     * SCAN with array in input
     * @see #218
     */
    @Test
    fun executeT10_A35_P08() {
        val expected = listOf<String>("A35_AR1(2)(123&5) IN45(1)")
        assertEquals(expected, "smeup/T10_A35_P08".outputOf(configuration = smeupConfig))
    }

    /**
     * SCAN with array in result
     * @see #244
     */
    @Test
    fun executeT10_A35_P10() {
        val expected = listOf<String>("A35_AR2(01)(5) A35_AR2(02)(6) A35_AR2(03)(0) IN20(1)")
        assertEquals(expected, "smeup/T10_A35_P10".outputOf(configuration = smeupConfig))
    }

    /**
     * Positive Check with indicator
     * @see #246
     */
    @Test
    fun executeT10_A45_P04() {
        val expected = listOf("IND(0)")
        assertEquals(expected, "smeup/T10_A45_P04".outputOf())
    }

    /**
     * Negative Check with indicator
     * @see #246
     */
    @Test
    fun executeT10_A45_P05() {
        val expected = listOf("IND(1)")
        assertEquals(expected, "smeup/T10_A45_P05".outputOf())
    }

    /**
     * Check with error indicator
     * @see #246
     */
    @Test
    fun executeT10_A45_P06() {
        val expected = listOf("IND(0)")
        assertEquals(expected, "smeup/T10_A45_P06".outputOf())
    }

    /**
     * Definition with PARM and DS
     * @see #251
     */
    @Test
    fun executeT10_A60_P10() {
        val expected = listOf("CALL_1(          , 1, MULANGTB10:  chiamata 1                           )")
        assertEquals(expected, "smeup/T10_A60_P10".outputOf())
    }

    /**
     * SCAN with special char
     * @see LS24002777
     */
    @Test
    fun executeMU103511() {
        val expected = listOf("Found at: 3")
        assertEquals(expected, "smeup/MU103511".outputOf())
    }

    /**
     * Utilization of `LIKEDS` with a `DataDefinition` defined in parent.
     * @see #271
     */
    @Test
    fun executeMU108006() {
        val expected = listOf("ScritturaInProcedura")
        assertEquals(expected, "smeup/MU108006".outputOf())
    }

    /**
     * Inline definition on KFLD
     * @see #276
     */
    @Test
    fun executeMU105501() {
        val expected = listOf("ABCDEFGHIL, 12")
        assertEquals(expected, "smeup/MU105501".outputOf())
    }

    /**
     * CALL with params defined in line and in D-spec
     * @see #278
     */
    @Test
    fun executeMU106011() {
        val expected = listOf("CALL(mod:inp   )")
        assertEquals(expected, "smeup/MU106011".outputOf())
    }

    /**
     * TESTN
     */
    @Test
    fun executeMU102501() {
        val expected = listOf("51=1,52=0,53=0")
        assertEquals(expected, "smeup/MU102501".outputOf())
    }

    /**
     * EXCEPT statement is supported
     * @see #LS24002974
     */
    @Test
    fun executeMUDRNRAPU00216() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00216".outputOf())
    }

    /**
     * MOVE with Boolean Type
     * @see #LS24003505
     */
    @Test
    fun executeMU101016() {
        val expected = listOf("**IN36: 0; *IN36: 1; *IN36: 0; *IN36: 2.")
        assertEquals(expected, "smeup/MU101016".outputOf())
    }

    /**
     * MOVEL with Boolean Type
     * @see #LS24003505
     */
    @Test
    fun executeMU101017() {
        val expected = listOf("*IN36: 0; *IN36: 1; *IN36: 0; *IN36: 2.")
        assertEquals(expected, "smeup/MU101017".outputOf())
    }
}