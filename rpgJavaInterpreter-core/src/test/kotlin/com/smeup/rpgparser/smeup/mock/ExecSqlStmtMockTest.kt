package com.smeup.rpgparser.smeup.mock

import com.smeup.rpgparser.smeup.MockTest
import org.junit.Test
import kotlin.test.assertEquals

class ExecSqlStmtMockTest : MockTest() {
    /**
     * This program simulates a simple SQL FETCH operation, using SQLCOD to
     * display the SQL status before and after execution. SQLCOD is explicitly
     * defined here for Jariko, emulating AS400 behavior. Suitable for testing
     * basic SQL handling in a mock setup.
     */
    @Test
    fun executeEXECSQL_SQLCOD100() {
        val expected = listOf("0", "100")
        assertEquals(expected, "mock/EXECSQL_SQLCOD100".outputOf())
    }
}