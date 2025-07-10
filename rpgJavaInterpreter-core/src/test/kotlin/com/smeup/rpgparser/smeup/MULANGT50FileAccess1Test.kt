package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.db.utilities.DBServer
import com.smeup.rpgparser.smeup.dbmock.C5RREG1LDbMock
import com.smeup.rpgparser.smeup.dbmock.MULANGTLDbMock
import com.smeup.rpgparser.smeup.dbmock.ST02DbMock
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.assertEquals

open class MULANGT50FileAccess1Test : MULANGTTest() {
    @BeforeTest
    override fun setUp() {
        if (!DBServer.isRunning()) {
            DBServer.startDB()
        }

        super.setUp()
    }

    /**
     * Data Reference to DS
     * @see #280
     */
    @Test
    fun executeMU500802() {
        val expected = listOf("Test")
        assertEquals(expected, "smeup/MU500802".outputOf(configuration = smeupConfig))
    }

    /**
     * I-Spec definitions on a renamed file definition
     * @see #LS24002977
     */
    @Test
    fun executeMU500901() {
        val expected = listOf("TSSYST(IBMI) TSLIBR() TSFILE() TATIPO(3) TBPROG(MULANGT12) AAAAAA(A03) BBBBBB(P01) CCCCCC()")
        assertEquals(expected, "smeup/MU500901".outputOf(configuration = smeupConfig))
    }

    /**
     * Printer file with O-specs
     * @see #LS24002987
     */
    @Test
    fun executeMUDRNRAPU00220() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00220".outputOf(configuration = smeupConfig))
    }

    /**
     * Printer file with O-specs with no DSPF config
     * @see #LS24002987
     */
    @Test
    fun executeMUDRNRAPU00220WithoutDSPF() {
        val expected = listOf("ok")
        val mockSmeupConfig = smeupConfig.copy()
        mockSmeupConfig.dspfConfig = null
        assertEquals(expected, "smeup/MUDRNRAPU00220".outputOf(configuration = mockSmeupConfig))
    }

    /**
     * SetLL with '*START' and '*END' symbolic constants
     * @see #LS24003777
     */
    @Test @Ignore
    fun executeMUDRNRAPU00248() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00248".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU00254() {
        MULANGTLDbMock().usePopulated({
            val expected = listOf("1.000000000")
            assertEquals(expected, "smeup/MUDRNRAPU00254".outputOf(configuration = smeupConfig))
        })
    }

    /**
     * Writing on a field of DS which use `EXTNAME` of a file. In this case the file in `EXTNAME` is different
     *  from `F` spec but shares same fields.
     * @see #LS25000910
     */
    @Test
    fun executeMUDRNRAPU001102() {
        ST02DbMock().usePopulated({
                val expected = listOf(
                    "01", "", "", "", "",
                    "01", "2009", "", "", "1234007"
                )
                assertEquals(expected, "smeup/MUDRNRAPU001102".outputOf(configuration = smeupConfig))
            },
            listOf(
                mapOf("ST02F1" to "01", "ST02F2" to "2009", "ST02F3" to "", "ST02F4" to "", "ST02F5" to "1234007")
            )
        )
    }

    /**
     * Using `SETGT` and `READ` in a cycle.
     * @see #LS25000910
     */
    @Test
    fun executeMUDRNRAPU001103() {
        ST02DbMock().usePopulated({
                val expected = listOf("A003", "A547", "A634")
                assertEquals(expected, "smeup/MUDRNRAPU001103".outputOf(configuration = smeupConfig))
            },
            listOf(
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A003"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A547"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A634")
            )
        )
    }

    /**
     * Using `SETGT` and `READ` in a cycle. The cycle is interrupted when the EOF had been reached.
     * @see #LS25000910
     */
    @Test
    fun executeMUDRNRAPU001120() {
        ST02DbMock().usePopulated({
                val expected = listOf("A003", "A547", "A634", "LEAVE")
                assertEquals(expected, "smeup/MUDRNRAPU001120".outputOf(configuration = smeupConfig))
            },
            listOf(
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A003"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A547"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A634")
            )
        )
    }

    /**
     * Packed numbers in floating point notation from DB
     * @see #LS25003001
     */
    @Test
    fun executeMUDRNRAPU00294() {
        MULANGTLDbMock().usePopulated({
            val expected = listOf(".000000000")
            assertEquals(expected, "smeup/MUDRNRAPU00294".outputOf(configuration = smeupConfig))
        }, listOf(
            mapOf<String, Any>("MLNNAT" to "0E-09")
        ))
    }

    /**
     * This program reads a file through a procedure.
     * @see #LS25002732
     */
    @Test
    fun executeMUDRNRAPU001132() {
        ST02DbMock().usePopulated({
                val expected = listOf("A003", "A003")
                assertEquals(expected, "smeup/MUDRNRAPU001132".outputOf(configuration = smeupConfig))
            },
            listOf(
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A003"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A547"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A634")
            )
        )
    }

    /**
     * This program reads a file through a procedure, by avoiding the use of `SETLL` from procedure.
     * @see #LS25002732
     */
    @Test
    fun executeMUDRNRAPU001133() {
        ST02DbMock().usePopulated({
                val expected = listOf("A003", "A547", "A634")
                assertEquals(expected, "smeup/MUDRNRAPU001133".outputOf(configuration = smeupConfig))
            },
            listOf(
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A003"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A547"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A634")
            )
        )
    }

    /**
     * This program reads a file by using `SETLL` and `READ` from `ST02` for two times.
     * @see #LS25002732
     */
    @Test
    fun executeMUDRNRAPU001134() {
        ST02DbMock().usePopulated({
            val expected = listOf("A003", "A547", "A634")
            assertEquals(expected, "smeup/MUDRNRAPU001134".outputOf(configuration = smeupConfig))
        },
            listOf(
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A003"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A547"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A634")
            )
        )
    }

    /**
     * This program reads a file through a nested procedure call.
     * @see #LS25002732
     */
    @Test
    fun executeMUDRNRAPU001135() {
        ST02DbMock().usePopulated({
                val expected = listOf("A003", "A003")
                assertEquals(expected, "smeup/MUDRNRAPU001135".outputOf(configuration = smeupConfig))
            },
            listOf(
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A003"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A547"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A634")
            )
        )
    }

    /**
     * This program reads a file through a nested procedure call, by avoiding the use of `SETLL` from it.
     * @see #LS25002732
     */
    @Test
    fun executeMUDRNRAPU001136() {
        ST02DbMock().usePopulated({
            val expected = listOf("A003", "A547", "A634")
            assertEquals(expected, "smeup/MUDRNRAPU001136".outputOf(configuration = smeupConfig))
        },
            listOf(
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A003"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A547"),
                mapOf("ST02F1" to "CNFOR", "ST02F2" to "A634")
            )
        )
    }

    /**
     * Executes DB operation from procedure by using a file with same Record Format name of another file imported with
     *   `EXTNAME` for a DS.
     * @see #LS25002732
     */
    @Test
    fun executeMUDRNRAPU001138() {
        C5RREG1LDbMock().usePopulated({
            val expected = listOf("4")
            assertEquals(expected, "smeup/MUDRNRAPU001138".outputOf(configuration = smeupConfig))
        })
    }
}
