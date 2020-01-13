package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

const val CONVENTIONAL_INDEX_SUFFIX = "_INDEX"

fun ResultSet.joinToString(separator: String = " - "): String {
    val sb = StringBuilder()
    while (this.next()) {
        for (i in 1..this.metaData.columnCount) {
            sb.append("${this.metaData.getColumnName(i)}: ${this.getObject(i)}")
            if (i != this.metaData.columnCount) sb.append(separator)
        }
        sb.appendln()
    }
    return sb.toString()
}

fun PreparedStatement.bind(values: List<Value>) {
    values.forEachIndexed { i, value ->
        val jdbcIndex = i + 1
        val type =
            typeFor(parameterMetaData.getParameterTypeName(jdbcIndex),
                parameterMetaData.getPrecision(jdbcIndex),
                parameterMetaData.getScale(jdbcIndex)
            )
        this.setObject(jdbcIndex, value.toDBValue(type))
    }
}

fun Connection.recordFormatName(tableName: String): String? =
    this.metaData.getTables(null, null, tableName, null).use {
        if (it.next()) {
            val remarks = it.getString("REMARKS")
            if (!remarks.isNullOrBlank()) {
                return@use remarks
            }
        }
        return@use tableName
    }

fun Connection.fields(name: String): List<DBField> {
    val result = mutableListOf<DBField>()
    this.metaData.getColumns(null, null, name, null).use {
        while (it.next()) {
            result.add(it.getString("COLUMN_NAME") withType typeFor(it))
        }
    }
    return result
}

fun typeFor(metadataResultSet: ResultSet): Type {
    val sqlType = metadataResultSet.getString("TYPE_NAME")
    val columnSize = metadataResultSet.getInt("COLUMN_SIZE")
    val decimalDigits = metadataResultSet.getInt("DECIMAL_DIGITS")
    return typeFor(sqlType, columnSize, decimalDigits)
}

fun Connection.primaryKeys(tableName: String): List<String> {
    val result = mutableListOf<String>()
    this.metaData.getPrimaryKeys(null, null, tableName).use {
        while (it.next()) {
            result.add(it.getString("COLUMN_NAME"))
        }
    }
    return result
}

fun Connection.orderingFields(tableName: String): List<String> {
    val result = mutableListOf<String>()
    this.prepareStatement("SELECT VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME = ?").use {
        it.setString(1, tableName)
        it.executeQuery().use {
            if (it.next()) {
                // TODO handle DESC and ASC keywords
                val fields = it.getString("VIEW_DEFINITION").substringAfter("ORDER BY").split(",")
                result.addAll(fields.map(String::trim))
            }
        }
    }
    return result
}

fun ResultSet?.closeIfOpen() {
    if (this != null) {
        try {
            this.close()
        } catch (t: Throwable) {}
    }
}

fun ResultSet?.hasRecords() = (this?.isBeforeFirst) ?: false

fun ResultSet?.toValues(): Record {
    if (this != null && this.next()) {
        return this.currentRecordToValues()
    }
    return Record()
}

fun ResultSet?.currentRecordToValues(): Record {
    // TODO create a unit test for the isAfterLast condition
    if (this == null || this.isAfterLast) {
        return Record()
    }
    val result = Record()
    val metadata = this.metaData
    for (i in 1..metadata.columnCount) {
        val type = typeFor(metadata.getColumnTypeName(i), metadata.getScale(i), metadata.getPrecision(i))
        val value = type.toValue(this, i)
        result.add(RecordField(metadata.getColumnName(i), value))
    }
    return result
}
