package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.lang.StringBuilder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

fun ResultSet.joinToString(separator: String = " - "): String {
    val sb = StringBuilder()
    if (this.next()) {
        for (i in 1..this.metaData.columnCount) {
            sb.append("${this.metaData.getColumnName(i)}: ${this.getObject(i)}")
            if (i != this.metaData.columnCount) sb.append(separator)
        }
    }
    return sb.toString()
}

fun Value.toDBValue() =
    when (this) {
        is StringValue -> this.valueWithoutPadding
        is IntValue -> this.value
        else -> TODO("Conversion to DB Obejct not yet implemented: $this")
    }

fun PreparedStatement.bind(values: List<Value>) {
    values.forEachIndexed {
        i, value -> this.setObject(i + 1, value.toDBValue())
    }
}

fun Connection.recordFormatName(tableName: String): String? =
    this.metaData.getTables(null, null, tableName, null).use {
        if (it.next()) {
            return@use it.getString("REMARKS").ifBlank { tableName }
        }
        return@use null
    }

fun Connection.fields(name: String): Collection<DBField> {
    val result = mutableListOf<DBField>()
    this.metaData.getColumns(null, null, name, null).use {
        while (it.next()) {
            result.add(it.getString("COLUMN_NAME") withType typeFor(it))
        }
    }
    return result
}

fun Connection.primaryKeys(tableName: String): List<String> {
    val result = mutableListOf<String>()
    this.metaData.getPrimaryKeys(null, null, "survey").use {
        while (it.next()) {
            result.add(it.getString("COLUMN_NAME"))
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