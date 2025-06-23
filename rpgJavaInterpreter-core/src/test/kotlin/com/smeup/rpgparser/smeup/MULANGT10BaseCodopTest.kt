package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.LogChannel
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import com.smeup.rpgparser.smeup.dbmock.C5ADFF9LDbMock
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
     * State of context after a CALL stack failed setting an error indicator
     * @see #LS24004538
     */
    @Test
    fun executeMUDRNRAPU00269() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00269".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU00271() {
        val expected = listOf("OK")
        assertEquals(expected, "smeup/MUDRNRAPU00271".outputOf(configuration = smeupConfig))
    }

    /**
     * READ operations with EQ Indicator
     * @see #LS24005102
     */
    @Test
    fun executeMUDRNRAPU00272() {
        val expected = listOf("ok", "ok", "ok", "ok")
        C5ADFF9LDbMock().usePopulated({
            assertEquals(expected, "smeup/MUDRNRAPU00272".outputOf(configuration = smeupConfig))
        })
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
        val expected = listOf("123", "123", "123", "123.000", "123.000", "123.000", "9.900", "9.900")
        assertEquals(expected, "smeup/MUDRNRAPU00146".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL an integer array to another. The size of first is lower than destination. In this case the target
     *  is a DS array.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00147() {
        val expected = listOf("1", "1", "1", "1", "1", "1", "2", "2")
        assertEquals(expected, "smeup/MUDRNRAPU00147".outputOf(configuration = smeupConfig))
    }

    /**
     * EVAL an integer array to another. The size of first is lower than destination. In this case the target
     *  is a DS array.
     * @see #LS24004606
     */
    @Test
    fun executeMUDRNRAPU00148() {
        val expected = listOf("1", "1", "1", "1", "1", "1", "2", "2")
        assertEquals(expected, "smeup/MUDRNRAPU00148".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEA between a DS field declared as array and a standalone array. Both as integer.
     * @see #LS24004772
     */
    @Test
    fun executeMUDRNRAPU00155() {
        val expected = listOf("2", "2", "2", "2", "2", "1", "1", "1", "1", "1")
        assertEquals(expected, "smeup/MUDRNRAPU00155".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEA between a DS field declared as array and a standalone array. Both as integer.
     * Size of DS field as array is lower than standalone.
     * @see #LS24004772
     */
    @Test
    fun executeMUDRNRAPU00156() {
        val expected = listOf("2", "2", "2", "2", "2", "1", "1", "1", "0", "0")
        assertEquals(expected, "smeup/MUDRNRAPU00156".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEA between a DS field declared as array and a standalone array. Both as integer.
     * Size of DS field as array is greater than standalone.
     * @see #LS24004772
     */
    @Test
    fun executeMUDRNRAPU00157() {
        val expected = listOf("2", "2", "2", "1", "1", "1")
        assertEquals(expected, "smeup/MUDRNRAPU00157".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEA between a DS field declared as array and a standalone array. DS field array is declared as decimal;
     *  standalone array as integer.
     * Size of DS field as array is lower than standalone.
     * @see #LS24004772
     */
    @Test
    fun executeMUDRNRAPU00158() {
        val expected = listOf("2", "2", "2", "2", "2", "1200", "1200", "1200", "0", "0")
        assertEquals(expected, "smeup/MUDRNRAPU00158".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEA between a DS field declared as array and a standalone array. DS field array is declared as integer;
     *  standalone array as decimal.
     * Size of DS field as array is lower than standalone.
     * @see #LS24004772
     */
    @Test
    fun executeMUDRNRAPU00159() {
        val expected = listOf("2.200", "2.200", "2.200", "2.200", "2.200", ".001", ".001", ".001", ".000", ".000")
        assertEquals(expected, "smeup/MUDRNRAPU00159".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEL between a DS field without initialization to an integer variable declared inline.
     * @see #LS24004842
     */
    @Test
    fun executeMUDRNRAPU00161() {
        val expected = listOf("", "0")
        assertEquals(expected, "smeup/MUDRNRAPU00161".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a boolean value (*OFF) to an array by using MOVEA.
     * @see #LS24004909
     */
    @Test
    fun executeMUDRNRAPU00166() {
        val expected = listOf("1", "1", "1", "0", "0", "0")
        assertEquals(expected, "smeup/MUDRNRAPU00166".outputOf(configuration = smeupConfig))
    }

    /**
     * Assignment of a boolean value (*ON) to an array by using MOVEA.
     * @see #LS24004909
     */
    @Test
    fun executeMUDRNRAPU00167() {
        val expected = listOf("0", "0", "0", "1", "1", "1")
        assertEquals(expected, "smeup/MUDRNRAPU00167".outputOf(configuration = smeupConfig))
    }

    /**
     * SELECT statement containing only a OTHER clause
     * @see #LS24005232
     */
    @Test
    fun executeMUDRNRAPU00274() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00274".outputOf(configuration = smeupConfig))
    }

    /**
     * CHECKR with indexed expression based on a Data Reference
     * @see #LS24005243
     */
    @Test
    fun executeMUDRNRAPU00275() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00275".outputOf(configuration = turnOnZAddLegacyFlagConfig))
    }

    /**
     * Unary expression with '+'
     * @see #LS24005278
     */
    @Test
    fun executeMUDRNRAPU00278() {
        val expected = listOf("1")
        assertEquals(expected, "smeup/MUDRNRAPU00278".outputOf(configuration = smeupConfig))
    }

    /**
     * This program tests the moving of value from Factor 2 to Result. This operation is performed at the end of execution of
     *  program called. In this case, MUDRNRAPU00172_P turn on indicator 35. Later, `DO` block ends its execution.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00172() {
        val expected = listOf("Sub program", "1")
        assertEquals(expected, "smeup/MUDRNRAPU00172".outputOf(configuration = smeupConfig))
    }

    /**
     * This program tests the behaviour of `CALL` and `PLIST` when is used the Params for both between caller and called.
     * In accord to documentation:
     * - when `CALL` is processed, the content of Factor 2 is placed in the Result field;
     * - when control transfers to called program, the contents of the Result field is placed in the Factor 1 field.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00173() {
        val expected = listOf("BAR")
        assertEquals(expected, "smeup/MUDRNRAPU00173".outputOf(configuration = smeupConfig))
    }

    /**
     * This program tests the behaviour of `CALL` and `PLIST` when is used the Params for both between caller and called.
     * Also, the called program change the result (`RES`) to another value.
     * In accord to documentation:
     * - when `CALL` is processed, the content of Factor 2 is placed in the Result field;
     * - when control transfers to called program, the contents of the Result field is placed in the Factor 1 field.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00174() {
        val expected = listOf("BAR")
        assertEquals(expected, "smeup/MUDRNRAPU00174".outputOf(configuration = smeupConfig))
    }

    /**
     * This program is more complex. Tests the assignment of value between waterfall calls and by parameters. Each subprogram is
     *  called max 101 times (last is Jariko error), thanks `DO` statement; this main program calls `MUDRNRAPU00175_P1` which
     *  calls recursively `MUDRNRAPU00175_P2`.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00175() {
        val expected = listOf("HELLO")
        assertEquals(expected, "smeup/MUDRNRAPU00175".outputOf(configuration = smeupConfig))
    }

    /**
     * This program is more complex. Tests the assignment of value between waterfall calls and by using `EVAL`.
     * Each subprogram is called max 101 times (last is Jariko error), thanks `DO` statement; this main program calls
     * `MUDRNRAPU00176_P1` which calls recursively `MUDRNRAPU00176_P2`.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00176() {
        val expected = listOf("HELLO")
        assertEquals(expected, "smeup/MUDRNRAPU00176".outputOf(configuration = smeupConfig))
    }

    /**
     * This program call a sub program by using pre-initialized variables as factors and result. The called program
     *  doesn't make any assignment.
     * This tests the full behaviour between a CALLER and CALLED, where:
     * - caller (at the beginning) move Factor 2 to Result;
     * - called (at the beginning) move Result to Factor 1;
     * - called (at the end) move Factor 2 to Result;
     * - caller (at the end) move Result to Factor 1.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00177() {
        val expected = listOf("CALLED", "BAR", "", "BAR", "CALLER", "", "BAR", "")
        assertEquals(expected, "smeup/MUDRNRAPU00177".outputOf(configuration = smeupConfig))
    }

    /**
     * This program call a sub program by using pre-initialized variables as factors and result. The called program
     *  doesn't make any assignment.
     * This tests the full behaviour between a CALLER and CALLED, where:
     * - caller (at the beginning) move Factor 2 to Result;
     * - called (at the beginning) move Result to Factor 1;
     * - caller (at the end) move Result to Factor 1.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00178() {
        val expected = listOf("CALLED", "BAR", "BAR", "CALLER", "BAR", "BAR", "BAR")
        assertEquals(expected, "smeup/MUDRNRAPU00178".outputOf(configuration = smeupConfig))
    }

    /**
     * This program call a sub program by using pre-initialized variables as factors and result. The called program
     *  doesn't make any assignment.
     * This tests the full behaviour between a CALLER and CALLED, where:
     * - called (at the beginning) move Result to Factor 1;
     * - caller (at the end) move Result to Factor 1.
     * @see #LS24005158
     */
    @Test
    fun executeMUDRNRAPU00179() {
        val expected = listOf("CALLED", "BAZ", "BAZ", "CALLER", "BAZ", "BAZ")
        assertEquals(expected, "smeup/MUDRNRAPU00179".outputOf(configuration = smeupConfig))
    }

    /**
     * IF comparison by using *HIVAL, right side.
     * @see #LS24005305
     */
    @Test
    fun executeMUDRNRAPU00180() {
        val expected = listOf("TRUE", "END")
        assertEquals(expected, "smeup/MUDRNRAPU00180".outputOf(configuration = smeupConfig))
    }

    /**
     * IF comparison by using *HIVAL, left side.
     * @see #LS24005305
     */
    @Test
    fun executeMUDRNRAPU00181() {
        val expected = listOf("TRUE", "END")
        assertEquals(expected, "smeup/MUDRNRAPU00181".outputOf(configuration = smeupConfig))
    }

    /**
     * Assign a new value to a DS preserving the original DS size.
     * @see #LS24005314
     */
    @Test
    fun executeMUDRNRAPU00182() {
        val expected = listOf("", "FOO", "BAR", "BARX")
        assertEquals(expected, "smeup/MUDRNRAPU00182".outputOf(configuration = smeupConfig))
    }

    /**
     * Comparison between:
     * - *HIVAL and String,
     * - *HIVAL and *HIVAL
     * by using "not equal" operator.
     * @see #LS24005329
     */
    @Test
    fun executeMUDRNRAPU00183() {
        val expected = listOf("TRUE", "END", "TRUE", "END", "END", "END")
        assertEquals(expected, "smeup/MUDRNRAPU00183".outputOf(configuration = smeupConfig))
    }

    /**
     * Comparison between:
     * - *HIVAL and String,
     * - *HIVAL and *HIVAL
     * by using "greater" operator.
     * @see #LS24005329
     */
    @Test
    fun executeMUDRNRAPU00184() {
        val expected = listOf("TRUE", "END", "TRUE", "END", "END", "END")
        assertEquals(expected, "smeup/MUDRNRAPU00184".outputOf(configuration = smeupConfig))
    }

    /**
     * Comparison between:
     * - *HIVAL and String,
     * - *HIVAL and *HIVAL
     * by using "greater/equal" operator.
     * @see #LS24005329
     */
    @Test
    fun executeMUDRNRAPU00185() {
        val expected = listOf("TRUE", "END", "TRUE", "END", "TRUE", "END", "TRUE", "END")
        assertEquals(expected, "smeup/MUDRNRAPU00185".outputOf(configuration = smeupConfig))
    }

    /**
     * Comparison between:
     * - *HIVAL and String,
     * - *HIVAL and *HIVAL
     * by using "lower" operator.
     * @see #LS24005329
     */
    @Test
    fun executeMUDRNRAPU00186() {
        val expected = listOf("END", "END", "END", "END")
        assertEquals(expected, "smeup/MUDRNRAPU00186".outputOf(configuration = smeupConfig))
    }

    /**
     * Comparison between:
     * - *HIVAL and String,
     * - *HIVAL and *HIVAL
     * by using "lower/equal" operator.
     * @see #LS24005329
     */
    @Test
    fun executeMUDRNRAPU00187() {
        val expected = listOf("END", "END", "TRUE", "END", "TRUE", "END")
        assertEquals(expected, "smeup/MUDRNRAPU00187".outputOf(configuration = smeupConfig))
    }

    /**
     * Comparison between:
     * - *HIVAL as Standalone of 2 chars,
     * - *HIVAL as Standalone of 4 chars,
     * by using "equal" operator.
     * @see #LS24005329
     */
    @Test
    fun executeMUDRNRAPU00188() {
        val expected = listOf("END")
        assertEquals(expected, "smeup/MUDRNRAPU00188".outputOf(configuration = smeupConfig))
    }

    /**
     * SCAN followed by %FOUND
     * @see #LS24005347
     */
    @Test
    fun executeMUDRNRAPU00279() {
        val expected = listOf("3", "FOUND", "0", "NOT FOUND")
        assertEquals(expected, "smeup/MUDRNRAPU00279".outputOf(configuration = smeupConfig))
    }

    /**
     * LOOKUP followed by %FOUND
     * @see #LS24005347
     */
    @Test
    fun executeMUDRNRAPU00280() {
        val expected = listOf("1", "0")
        assertEquals(expected, "smeup/MUDRNRAPU00280".outputOf(configuration = smeupConfig))
    }

    /**
     * MOVEA from DS to S defined as array.
     * @see #LS25000557
     */
    @Test
    fun executeMUDRNRAPU00195() {
        val expected = listOf("0123456789", "", "0123456789", "0123456789")
        assertEquals(expected, "smeup/MUDRNRAPU00195".outputOf())
    }

    /**
     * MOVEA from DS to S defined as array, with size lower than DS.
     * @see #LS25000557
     */
    @Test
    fun executeMUDRNRAPU00196() {
        val expected = listOf("0123456789", "", "0123456789", "01234567")
        assertEquals(expected, "smeup/MUDRNRAPU00196".outputOf())
    }

    /**
     * MOVEA from DS to S defined as array, with size greater than DS.
     * @see #LS25000557
     */
    @Test
    fun executeMUDRNRAPU00197() {
        val expected = listOf("0123456789", "", "0123456789", "0123456789")
        assertEquals(expected, "smeup/MUDRNRAPU00197".outputOf())
    }

    /**
     * MOVEA from S, defined as array, to DS.
     * @see #LS25000567
     */
    @Test
    fun executeMUDRNRAPU00198() {
        val expected = listOf("0123456789", "AAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAA")
        assertEquals(expected, "smeup/MUDRNRAPU00198".outputOf())
    }

    /**
     * MOVEA from S, defined as array, to DS. The array size is lower than DS.
     * @see #LS25000567
     */
    @Test
    fun executeMUDRNRAPU00199() {
        val expected = listOf("0123456789", "AAAAAAAA", "AAAAAAAA89", "AAAAAAAA")
        assertEquals(expected, "smeup/MUDRNRAPU00199".outputOf())
    }

    /**
     * MOVEA from S, defined as array, to DS. The array size is greater than DS.
     * @see #LS25000567
     */
    @Test
    fun executeMUDRNRAPU001100() {
        val expected = listOf("0123456789", "AAAAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAAAA")
        assertEquals(expected, "smeup/MUDRNRAPU001100".outputOf())
    }

    /**
     * This program shows a Packed number of a DS which, in hexadecimal, corresponds to `0   `.
     * @see #LS25000966
     */
    @Test
    fun executeMUDRNRAPU001106() {
        val expected = listOf("300.000000")
        assertEquals(expected, "smeup/MUDRNRAPU001106".outputOf())
    }

    /**
     * Decode a packed encoded with a scale smaller than what its type expects
     * @see #LS25001002
     */
    @Test
    fun executeMUDRNRAPU00284() {
        val expected = listOf(".010000")
        assertEquals(expected, "smeup/MUDRNRAPU00284".outputOf(configuration = smeupConfig))
    }

    /**
     * Z-ADD from a binary field to a binary data definition
     * @see #LS25001162
     */
    @Test
    fun executeMUDRNRAPU00285() {
        // The magic number 8224 correspond to the deserialization of the "  " DS substring
        // code of ' ' = 32
        // 8224 = 8192 + 32 = (32 << 8) + 32
        val expected = listOf("8224", "8224")
        assertEquals(expected, "smeup/MUDRNRAPU00285".outputOf(configuration = smeupConfig))
    }

    /**
     * Perform OPEN on a F-spec marked with PRINTER
     * @see #LS25001512
     */
    @Test
    fun executeMUDRNRAPU00286() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00286".outputOf(configuration = smeupConfig))
    }

    /**
     * Pass an array declared as DS field to a program which declares same program entry as Standalone.
     * @see #LS25001579
     */
    @Test
    fun executeMUDRNRAPU001107() {
        val expected = listOf("1", "2", "3", "000010000200003")
        assertEquals(expected, "smeup/MUDRNRAPU001107".outputOf())
    }

    /**
     * Pass an array declared as DS field to a program which declares same program entry as Standalone.
     * Is similar to `MUDRNRAPU001107` but the DS field is declared as array of decimals instead integers.
     * @see #LS25001579
     */
    @Test
    fun executeMUDRNRAPU001108() {
        val expected = listOf("1.50", "2.50", "3.50", "001500025000350", "1.50", "2.50", "3.50")
        assertEquals(expected, "smeup/MUDRNRAPU001108".outputOf())
    }

    /**
     * Pass an array declared as DS field to a program which declares same program entry as Standalone.
     * Is similar to `MUDRNRAPU001107` but the DS field is declared as array of packed instead integers.
     * @see #LS25001579
     */
    @Test
    fun executeMUDRNRAPU001109() {
        val expected = listOf("1.50", "2.50", "3.50", "1.50", "2.50", "3.50")
        assertEquals(expected, "smeup/MUDRNRAPU001109".outputOf())
    }

    /**
     * Using `OCCURS` by passing a variable as argument.
     */
    @Test
    fun executeMUDRNRAPU001125() {
        val expected = listOf("0", ".0", "1", "1.1", "0", ".0", "2", "2.2")
        assertEquals(expected, "smeup/MUDRNRAPU001125".outputOf())
    }

    /**
     * Using `Z-ADD` with a field of occurable DS declared as array.
     */
    @Test
    fun executeMUDRNRAPU001126() {
        val expected = listOf("0", "0", "0", "1", "1", "1", "0", "0", "0", "2", "2", "2", "1", "1", "1", "2", "2", "2")
        assertEquals(expected, "smeup/MUDRNRAPU001126".outputOf())
    }

    /**
     * Using `Z-ADD` with the fields declared by using offsets.
     */
    @Test
    fun executeMUDRNRAPU001127() {
        val expected = listOf("1.00", "1.00", "1.00", "1.00", "1.00", "1.00", "2.00", "2.00", "2.00", "2.00", "2.00", "2.00", "3.00", "")
        assertEquals(expected, "smeup/MUDRNRAPU001127".outputOf())
    }

    /**
     * Using `Z-ADD` with the fields declared by using offsets. Also, uses `OCCURS` keyword.
     */
    @Test
    fun executeMUDRNRAPU001128() {
        val expected = listOf(
            "1.00", "1.00", "1.00", "1.00", "1.00", "1.00", "2.00", "2.00", "2.00", "2.00", "2.00", "2.00", "3.00", "",
            "4.00", "4.00", "4.00", "4.00", "4.00", "4.00", "5.00", "5.00", "5.00", "5.00", "5.00", "5.00", "6.00", "",
            "1.00", "1.00", "1.00", "1.00", "1.00", "1.00", "2.00", "2.00", "2.00", "2.00", "2.00", "2.00", "3.00", ""
        )
        assertEquals(expected, "smeup/MUDRNRAPU001128".outputOf())
    }
}