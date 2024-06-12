package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.db.utilities.DBServer
import org.junit.Test
import kotlin.test.BeforeTest
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
     * TODO
     * @see TODO
     */
    @Test
    fun executeMU500901() {
        val expected = listOf("TSSYST(IBMI) TSLIBR() TSFILE() TATIPO(3) TBPROG(MULANGT12) AAAAAA(A03) BBBBBB(P01) CCCCCC()")
        assertEquals(expected, "smeup/MU500901".outputOf(configuration = smeupConfig))
    }
}