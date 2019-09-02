package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DatabaseInterface
import org.junit.Test

class DbSqlTest {

    @Test
    fun dbSqlSmokeTest() {
        val db: DatabaseInterface = DatabaseSQLInterface(testConnection())
    }

    private fun testConnection() = DBConfiguration("jdbc:h2:mem:regular", "org.h2.Driver")
}