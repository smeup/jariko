package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.sql.Connection
import java.sql.DriverManager

class DBSQLInterface(private val dbConfiguration: DBConfiguration) : DBInterface {

    private val connection: Connection by lazy {
        DriverManager.getConnection(dbConfiguration.url, dbConfiguration.user, dbConfiguration.password)
    }

    fun setSQLLog(on: Boolean) {
        val level = if (on) 3 else 0
        // TODO This statement is specific for hsqldb: we have to generalize it
        connection.createStatement().use {
            it.execute("SET DATABASE EVENT LOG SQL LEVEL $level")
        }
    }

    override fun metadataOf(name: String): FileMetadata? {
        val formatName = connection.recordFormatName(name) ?: return null
        return FileMetadata(name, formatName, connection.fields(name))
    }

    override fun chain(name: String, key: Value): Collection<Pair<DBField, Value>> {
        TODO("CHAIN")
//        val sql = "SELECT * FROM $name where ${primaryKey(name)} = ?"
//        connection.prepareStatement(sql).use {
//            it.setObject(1, key.toDBValue())
//            return toValues(it.executeQuery())
//        }
//    }
//
//    private fun primaryKeys(name: String): List<String> {
//
//    }
//
//    private fun toValues(rs: ResultSet): Collection<Pair<DBField, Value>> {
//        val result = mutableListOf<Pair<DBField, Value>>()
//
//        return result
    }

    fun create(tables: List<FileMetadata>) {
        val sqlStatements = tables.flatMap { it.toSQL() }
        val statement = connection.createStatement()
        statement.use {
            sqlStatements.forEach { statement.addBatch(it) }
            statement.executeBatch()
        }
    }

    fun insertRow(tableName: String, values: List<Pair<String, Value>>) {
        val sql = tableName.insertSQL(values)
        connection.prepareStatement(sql).use {
            it.bind(values.map { it.second })
            it.execute()
        }
    }
}

data class DBConfiguration(var url: String, val user: String = "", val password: String = "")