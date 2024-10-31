package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.execution.CommandLineParms
import com.smeup.rpgparser.interpreter.ConcreteArrayValue
import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.interpreter.StringType
import com.smeup.rpgparser.interpreter.StringValue
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
        val expected = listOf("4", "0", "0", "3", "0", "6", "0", "3", "0", "2", "4", "0")
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

    /**
     * Runtime evaluation of `%ELEM` during the evaluation of `PLIST`.
     * @see #LS24003751
     */
    @Test
    fun executeMUDRNRAPU00105() {
        // arrayValue takes into account the following RPG code:
        // D £40A            S             15    DIM(300)
        val arrayValue = ConcreteArrayValue(
            elements = MutableList(300) { _ -> StringValue.blank(15) },
            elementType = StringType(15)
        )

        // dataStructValue takes into account the following RPG code:
        // D £G40DS          DS           500
        // I don't set any field value because the RPG code doesn't use them
        val dataStructValue = DataStructValue.blank(500)

        // This is the entry passed to the program
        val entry = CommandLineParms(namedParams = mapOf("£40A" to arrayValue, "£G40DS" to dataStructValue))
        val expected = listOf("300")
        assertEquals(expected = expected, actual = "smeup/MUDRNRAPU00105".outputOf(configuration = smeupConfig, params = entry))
    }

    /**
     * %REALLOC and %ALLOC for dynamic array resizing
     * @see #LS24003806
     */
    @Test
    fun executeMUDRNRAPU00252() {
        val expected = listOf("10000", "10000", "10000")
        assertEquals(expected, "smeup/MUDRNRAPU00252".outputOf(configuration = smeupConfig))
    }

    /**
     * %REALLOC for dynamic array resizing when multiple arrays based on the same pointer are present
     * @see #LS24003806
     */
    @Test
    fun executeMUDRNRAPU00253() {
        val expected = listOf("10000", "10000", "10000", "10000")
        assertEquals(expected, "smeup/MUDRNRAPU00253".outputOf(configuration = smeupConfig))
    }

    /**
     * DS field declared as Array and CTDATA.
     * @see #LS24004654
     */
    @Test
    fun executeMUDRNRAPU00149() {
        val expected = listOf("1", "*SCP")
        assertEquals(expected, "smeup/MUDRNRAPU00149".outputOf(configuration = smeupConfig))
    }

    /**
     * DS field declared as Array and CTDATA. In this case between CTDATA and its name there is more space.
     * @see #LS24004654
     */
    @Test
    fun executeMUDRNRAPU00150() {
        val expected = listOf("1", "*SCP")
        assertEquals(expected, "smeup/MUDRNRAPU00150".outputOf(configuration = smeupConfig))
    }

    /**
     * DS field declared as Array and CTDATA. In this case there isn't CTDATA but more space between name and stars.
     * @see #LS24004654
     */
    @Test
    fun executeMUDRNRAPU00151() {
        val expected = listOf("1", "*SCP")
        assertEquals(expected, "smeup/MUDRNRAPU00151".outputOf(configuration = smeupConfig))
    }

    /**
     * DS field declared as Array and CTDATA. In this case there is only CTDATA.
     * @see #LS24004654
     */
    @Test
    fun executeMUDRNRAPU00152() {
        val expected = listOf("1", "*SCP")
        assertEquals(expected, "smeup/MUDRNRAPU00152".outputOf(configuration = smeupConfig))
    }
}
