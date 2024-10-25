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
        val expected = listOf("*IN36: 0;*IN36: 1;*IN36: 0.")
        assertEquals(expected, "smeup/MU101016".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL with Boolean Type
     * @see #LS24003505
     */
    @Test
    fun executeMU101017() {
        val expected = listOf("*IN36: 0;*IN36: 1;*IN36: 0.")
        assertEquals(expected, "smeup/MU101017".outputOf())
    }

    /**
     * MOVE with Boolean Type and factor 2 as decimal.
     * @see #LS24003505
     */
    @Test
    fun executeMU101018() {
        val expected = listOf("*IN36: 0;*IN36: 0;*IN36: 1;*IN36: 0;*IN36: 1;*IN36: 0;*IN36: 0.")
        assertEquals(expected, "smeup/MU101018".outputOf())
    }

    /**
     * MOVEL with Boolean Type and factor 2 as decimal.
     * @see #LS24003505
     */
    @Test
    fun executeMU101019() {
        val expected = listOf("*IN36: 0;*IN36: 0;*IN36: 1;*IN36: 0;*IN36: 1;*IN36: 0;*IN36: 0.")
        assertEquals(expected, "smeup/MU101019".outputOf())
    }

    /**
     * Z-ADD to a DS field defined as array and overlay.
     * @see #LS24004081
     */
    @Test
    fun executeMUDRNRAPU00115() {
        val expected = listOf("99.000000", ".000000")
        assertEquals(expected, "smeup/MUDRNRAPU00115".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of an array, defined as field of DS, to a Standalone variable with MOVEA.
     * @see #LS24004086
     */
    @Test
    fun executeMUDRNRAPU00116() {
        val expected = listOf(
            "AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH", "II", "LL",
            "AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH", "II", "LL",
            "AABBCCDDEEFFGGHHIILL",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", ""
        )
        assertEquals(expected, "smeup/MUDRNRAPU00116".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a scalar to an array, where the digits of scalar are lower than decimal's digits of array type,
     *  with `MOVEA`.
     * @see #LS24004106
     */
    @Test
    fun executeMUDRNRAPU00117() {
        val expected = listOf(
            ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000",
            ".000123", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00117".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a scalar to an array, where the digits of scalar are greater than decimal's digits of array type,
     *  with `MOVEA`.
     * @see #LS24004106
     */
    @Test
    fun executeMUDRNRAPU00118() {
        val expected = listOf(
            ".00", ".00", ".00", ".00", ".00", ".00", ".00", ".00", ".00",
            "123.45", ".00", ".00", ".00", ".00", ".00", ".00", ".00", ".00"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00118".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a scalar to an array, declared as integer type too, with `MOVEA`.
     * @see #LS24004106
     */
    @Test
    fun executeMUDRNRAPU00119() {
        val expected = listOf(
            "0", "0", "0", "0", "0", "0", "0", "0", "0",
            "123", "0", "0", "0", "0", "0", "0", "0", "0"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00119".outputOf(configuration = smeupConfig))
    }

    /**
     * Z-ADD to a Standalone defined as array.
     * @see #LS24004081
     */
    @Test
    fun executeMUDRNRAPU00120() {
        val expected = listOf("99.000000", ".000000")
        assertEquals(expected, "smeup/MUDRNRAPU00120".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a DS containing packed fields to a string
     * @see #LS24004056
     */
    @Test
    fun executeMUDRNRAPU00255() {
        val expected = listOf(
            "A    Dÿ D\u001F 0000771",
            "A    Dÿ D\u001F 0000771",
            "A    Dÿ D\u001F 0000771"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00255".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a string to a DS containing packed fields
     * @see #LS24004056
     */
    @Test
    fun executeMUDRNRAPU00256() {
        val expected = listOf("", "", "")
        assertEquals(expected, "smeup/MUDRNRAPU00256".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a DS to a DS when they contain zoned fields
     * @see #LS24004056
     */
    @Test
    fun executeMUDRNRAPU00258() {
        val expected = List(3) { "A    0044005510000771" }
        assertEquals(expected, "smeup/MUDRNRAPU00258".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a DS with a string when they contain zoned fields
     * @see #LS24004056
     */
    @Test
    fun executeMUDRNRAPU00259() {
        val expected = List(5) { "A    0044005510000771" }
        assertEquals(expected, "smeup/MUDRNRAPU00259".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a DS with a DS when they contain packed fields
     * @see #LS24004056
     */
    @Test
    fun executeMUDRNRAPU00260() {
        val expected = List(3) { "A    Dÿ D\u001F 0000771" }
        assertEquals(expected, "smeup/MUDRNRAPU00260".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of numeric values on standalone fields
     * @see #LS24004056
     */
    @Test
    fun executeMUDRNRAPU00261() {
        val expected = listOf(
            "44.10",
            "108.20",
            "222.30",
            "04410",
            "44.10",
            "10820",
            "108.20",
            "22230",
            "222.30",
            "",
            ".00"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00261".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a scalar to an array, declared as integer type too, with `MOVE`.
     * @see #LS24004106
     */
    @Test
    fun executeMUDRNRAPU00126() {
        val expected = listOf(
            ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000",
            ".000123", ".000123", ".000123", ".000123", ".000123", ".000123", ".000123", ".000123", ".000123"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00126".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a scalar to an array, declared as integer type too, with `MOVEL`.
     * @see #LS24004106
     */
    @Test
    fun executeMUDRNRAPU00127() {
        val expected = listOf(
            ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000",
            "12300000.000000", "12300000.000000", "12300000.000000", "12300000.000000", "12300000.000000",
            "12300000.000000", "12300000.000000", "12300000.000000", "12300000.000000"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00127".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a scalar to an array, declared as integer type too, with `EVAL`.
     * @see #LS24004106
     */
    @Test
    fun executeMUDRNRAPU00128() {
        val expected = listOf(
            ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000", ".000000",
            "123.000000", "123.000000", "123.000000", "123.000000", "123.000000", "123.000000", "123.000000",
            "123.000000", "123.000000"
        )
        assertEquals(expected, "smeup/MUDRNRAPU00128".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a ZeroValue to an IntValue.
     * @see #LS24004319
     */
    @Test
    fun executeMUDRNRAPU00262() {
        val expected = listOf("123")
        assertEquals(expected, "smeup/MUDRNRAPU00262".outputOf(configuration = smeupConfig))
    }

    /**
     * DOWxx with indicator as factor 1
     * @see #LS24004474
     */
    @Test
    fun executeMUDRNRAPU00266() {
        val expected = listOf("1")
        assertEquals(expected, "smeup/MUDRNRAPU00266".outputOf(configuration = smeupConfig))
    }

    /**
     * State of context after a CALL failed setting an error indicator
     * @see #LS24004538
     */
    @Test
    fun executeMUDRNRAPU00268() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00268".outputOf(configuration = smeupConfig))
    }

    /**
     * Comparing number and `*ZEROS` by using `IFxx`.
     * @see #LS24004528
     */
    @Test
    fun executeMUDRNRAPU00134() {
        val expected = listOf("OK")
        assertEquals(expected, "smeup/MUDRNRAPU00134".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL an integer array to another. The size of first is lower than destination.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00135() {
        val expected = listOf("1", "1", "1", "1", "1", "1", "2", "2")
        assertEquals(expected, "smeup/MUDRNRAPU00135".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL an integer array to another. The size of first is greater than destination.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00136() {
        val expected = listOf("1", "1", "1", "1", "1", "1", "1", "1")
        assertEquals(expected, "smeup/MUDRNRAPU00136".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL a decimal array to integer.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00137() {
        val expected = listOf("1.200", "1.200", "1.200", "1200", "1200", "1200", "2", "2")
        assertEquals(expected, "smeup/MUDRNRAPU00137".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL an integer array to decimal.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00138() {
        val expected = listOf("1", "1", "1", ".001", ".001", ".001", "2.200", "2.200")
        assertEquals(expected, "smeup/MUDRNRAPU00138".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL a decimal array to integer. The number of digits of first are greater than second.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00139() {
        val expected = listOf("12.345", "12.345", "12.345", "123", "123", "123", "9", "9")
        assertEquals(expected, "smeup/MUDRNRAPU00139".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL a decimal array to integer. The number of digits of first are greater than second.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00140() {
        val expected = listOf("123", "123", "123", "12.300", "12.300", "12.300", "9.900", "9.900")
        assertEquals(expected, "smeup/MUDRNRAPU00140".outputOf(configuration = smeupConfig))
    }

    /**
     * EVAL an integer array to another. The size of first is lower than destination.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00141() {
        val expected = listOf("1", "1", "1", "1", "1", "1", "2", "2")
        assertEquals(expected, "smeup/MUDRNRAPU00141".outputOf(configuration = smeupConfig))
    }

    /**
     * EVAL an integer array to another. The size of first is greater than destination.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00142() {
        val expected = listOf("1", "1", "1", "1", "1", "1", "1", "1")
        assertEquals(expected, "smeup/MUDRNRAPU00142".outputOf(configuration = smeupConfig))
    }

    /**
     * EVAL a decimal array to integer.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00143() {
        val expected = listOf("1.200", "1.200", "1.200", "1", "1", "1", "2", "2")
        assertEquals(expected, "smeup/MUDRNRAPU00143".outputOf(configuration = smeupConfig))
    }

    /**
     * EVAL an integer array to decimal.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00144() {
        val expected = listOf("1", "1", "1", "1.000", "1.000", "1.000", "2.200", "2.200")
        assertEquals(expected, "smeup/MUDRNRAPU00144".outputOf(configuration = smeupConfig))
    }

    /**
     * EVAL a decimal array to integer. The number of digits of first are greater than second.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00145() {
        val expected = listOf("12.345", "12.345", "12.345", "12", "12", "12", "9", "9")
        assertEquals(expected, "smeup/MUDRNRAPU00145".outputOf(configuration = smeupConfig))
    }

    /**
     * EVAL a decimal array to integer. The number of digits of first are greater than second.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00146() {
        val expected = listOf("123", "123", "123", "123.000", "123.000", "12.300", "9.900", "9.900")
        assertEquals(expected, "smeup/MUDRNRAPU00146".outputOf(configuration = smeupConfig))
    }
}