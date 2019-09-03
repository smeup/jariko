package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

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
        val formatName = formatName(name) ?: return null
        return FileMetadata(name, formatName, fields(name))
    }

    private fun fields(name: String): Collection<DBField> {
        val result = mutableListOf<DBField>()
        connection.metaData.getColumns(null, null, name, null).use {
            while (it.next()) {
                result.add(it.getString("COLUMN_NAME") withType typeFor(it))
            }
        }
        return result
    }

    private fun typeFor(rs: ResultSet): Type =
        when (val sqlType = rs.getString("TYPE_NAME")) {
            "CHARACTER" -> StringType(rs.getLong("COLUMN_SIZE"))
            "DECIMAL" -> {
                val decimalDigits = rs.getInt("DECIMAL_DIGITS")
                NumberType(rs.getInt("COLUMN_SIZE") - decimalDigits, decimalDigits)
            }
            else -> TODO("Conversion from SQL Type not yet implemented: $sqlType")
        }

    private fun formatName(name: String): String? =
        connection.metaData.getTables(null, null, name, null).use {
            if (it.next()) {
                return@use it.getString("REMARKS").ifBlank { name }
            }
            return@use null
        }

    override fun chain(name: String, key: Value): Collection<Pair<DBField, Value>>? {
        TODO("not implemented")
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