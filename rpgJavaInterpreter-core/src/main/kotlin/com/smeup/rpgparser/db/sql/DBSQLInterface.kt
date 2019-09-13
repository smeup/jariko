package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class DBSQLInterface(private val dbConfiguration: DBConfiguration) : DBInterface {

    private val connection: Connection by lazy {
        dbConfiguration.getConnection()
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

    override fun chain(name: String, key: Value): List<Pair<String, Value>> {
        val sql = "SELECT * FROM $name ${connection.primaryKeys(name).whereSQL()}"
        connection.prepareStatement(sql).use {
            it.setObject(1, key.toDBValue())
            return toValues(it.executeQuery())
        }
    }

    override fun chain(name: String, keys: List<Pair<String, Value>>): List<Pair<String, Value>> {
        TODO("Many keys chain not implemented")
    }

    private fun toValues(rs: ResultSet): List<Pair<String, Value>> {
        val result = mutableListOf<Pair<String, Value>>()
        rs.use {
            if (it.next()) {
                val metadata = it.metaData
                for (i in 1..metadata.columnCount) {
                    val type = typeFor(metadata.getColumnTypeName(i), metadata.getScale(i), metadata.getPrecision(i))
                    val value = type.toValue(it, i)
                    result.add(Pair(metadata.getColumnName(i), value))
                }
            }
        }
        return result
    }

    fun create(tables: List<FileMetadata>) {
        val sqlStatements = tables.flatMap { it.toSQL() }
        execute(sqlStatements)
    }

    fun insertRow(tableName: String, values: List<Pair<String, Value>>) {
        val sql = tableName.insertSQL(values)
        connection.prepareStatement(sql).use {
            it.bind(values.map { it.second })
            it.execute()
        }
    }

    fun execute(sqlStatements: List<String>) {
        val statement = connection.createStatement()
        statement.use {
            sqlStatements.forEach { statement.addBatch(it) }
            statement.executeBatch()
        }
    }
}

data class DBConfiguration(var url: String, val user: String = "", val password: String = "") {
    fun getConnection(): Connection = DriverManager.getConnection(url, user, password)
}