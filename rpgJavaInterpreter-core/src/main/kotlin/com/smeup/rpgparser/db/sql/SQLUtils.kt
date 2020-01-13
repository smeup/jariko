package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DBField
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.Type
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.utils.Comparison

fun FileMetadata.toSQL(): List<String> =
    listOf(
        "CREATE TABLE ${this.tableName} (${this.fields.toSQL()})",
        "COMMENT ON TABLE ${this.tableName} IS '${this.formatName}'")

private fun Collection<DBField>.toSQL(): String {
    val primaryKeys = filter(DBField::primaryKey).joinToString { "${it.name}" }
    return joinToString { "${it.name} ${it.sqlType()}" } + (if (primaryKeys.isEmpty()) "" else ", PRIMARY KEY($primaryKeys)")
}

infix fun String.withType(type: Type): DBField = DBField(this, type)
infix fun String.primaryKeyWithType(type: Type): DBField = DBField(this, type, true)

fun String.insertSQL(values: List<Pair<String, Value>>): String {
    val names = values.joinToString { it.first }
    val questionMarks = values.joinToString { "?" }
    return "INSERT INTO $this ($names) VALUES($questionMarks)"
}

fun List<String>.whereSQL(comparation: Comparison = Comparison.EQ): String =
    if (this.isEmpty()) {
        ""
    } else {
        "WHERE " + this.joinToString(" AND ") { "$it ${comparation.symbol} ?" }
    }

fun List<String>.orderBySQL(reverse: Boolean = false): String =
    if (this.isEmpty()) {
        ""
    } else {
        if (reverse) {
            "ORDER BY " + this.joinToString(separator = " DESC, ", postfix = " DESC")
        } else {
            "ORDER BY " + this.joinToString()
        }
    }
