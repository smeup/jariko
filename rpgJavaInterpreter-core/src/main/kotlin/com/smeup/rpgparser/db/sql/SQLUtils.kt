package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*

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

fun List<String>.whereSQL(): String =
    "WHERE " + this.joinToString(" AND ") { "$it = ?" }