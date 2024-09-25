package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.db.utilities.DBServer
import com.smeup.rpgparser.smeup.dbmock.MULANGTLDbMock
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
        MULANGTLDbMock().usePopulated {
            val expected = listOf("1.000000000")
            assertEquals(expected, "smeup/MUDRNRAPU00254".outputOf(configuration = smeupConfig))
        }
    }

    @Test
    fun executeMUTE13_41() {
        val expected = listOf("20230930", "113123", "20230930", "113233", "20230930", "113312")
        assertEquals(expected, "mute/MUTE13_41".outputOf(configuration = smeupConfig))
    }
}
