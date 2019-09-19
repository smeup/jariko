package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DBFile
import com.smeup.rpgparser.interpreter.Value
import java.sql.Connection
import java.sql.ResultSet

class DBSQLFile(private val name: String, private val connection: Connection) : DBFile {
    override fun eof(): Boolean = TODO("not implemented")

    override fun chain(key: Value): List<Pair<String, Value>> {
        val sql = "SELECT * FROM $name ${connection.primaryKeys(name).whereSQL()}"
        connection.prepareStatement(sql).use {
            it.setObject(1, key.toDBValue())
            return toValues(it.executeQuery())
        }
    }

    override fun chain(keys: List<Pair<String, Value>>): List<Pair<String, Value>> {
        val keyNames = keys.map { it.first }
        val sql = "SELECT * FROM $name ${keyNames.whereSQL()}"
        val values = keys.map { it.second }
        connection.prepareStatement(sql).use {
            it.bind(values)
            return toValues(it.executeQuery())
        }
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
}
