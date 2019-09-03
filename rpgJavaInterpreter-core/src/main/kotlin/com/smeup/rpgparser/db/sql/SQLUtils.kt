package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*

fun FileMetadata.toSQL(): List<String> =
    listOf(
        "CREATE TABLE ${this.tableName} (${this.fields.toSQL()})",
        "COMMENT ON TABLE ${this.tableName} IS '${this.formatName}'")

private fun Collection<AbstractDataDefinition>.toSQL(): String =
    joinToString {
        "${it.name} ${it.sqlType()}"
    }

private fun AbstractDataDefinition.sqlType(): String =
    when (this.type) {
        is StringType -> "CHAR (${this.type.size}) NOT NULL"
        is NumberType -> "DECIMAL (${this.type.size}, ${(this.type as NumberType).decimalDigits}) NOT NULL"
        else -> TODO("Conversion to SQL Type not yet implemented: ${this.type}")
    }

infix fun String.withType(type: Type): AbstractDataDefinition = AbstractDataDefinition(this, type)