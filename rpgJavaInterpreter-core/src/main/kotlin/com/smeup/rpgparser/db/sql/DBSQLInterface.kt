package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.sql.Connection
import java.sql.DriverManager

class DBSQLInterface(private val dbConfiguration: DBConfiguration) : DBInterface {

    val connection: Connection by lazy {
        DriverManager.getConnection(dbConfiguration.url, dbConfiguration.user, dbConfiguration.password)
    }

    override fun metadataOf(name: String): FileMetadata? {
        TODO("not implemented")
    }

    override fun chain(name: String, key: Value): Collection<Pair<AbstractDataDefinition, Value>>? {
        TODO("not implemented")
    }

    fun create(tables: List<FileMetadata>) {
        tables.forEach {
            connection.createStatement().execute(it.toSQL())
        }
    }
}

data class DBConfiguration(var url: String, val user: String = "", val password: String = "")