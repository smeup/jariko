package com.smeup.rpgparser.smeup.mock

import com.smeup.rpgparser.smeup.MockTest
import org.junit.Test
import kotlin.test.assertEquals

class ExecSqlStmtMockTest : MockTest() {
    @Test
    fun executeEXECSQL_SQLCOD100() {
        val expected = listOf("0", "100")
        assertEquals(expected, "mock/EXECSQL_SQLCOD100".outputOf())
    }
}