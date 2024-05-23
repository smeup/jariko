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
}