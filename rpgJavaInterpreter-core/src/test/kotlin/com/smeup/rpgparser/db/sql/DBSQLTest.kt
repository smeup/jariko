package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DBInterface
import org.junit.Test

class DBSQLTest {

    @Test
    fun dbSqlSmokeTest() {
        val db: DBInterface = DBSQLInterface(testConnection())
    }

    private fun testConnection() = DBConfiguration("jdbc:h2:mem:regular", "org.h2.Driver")
}