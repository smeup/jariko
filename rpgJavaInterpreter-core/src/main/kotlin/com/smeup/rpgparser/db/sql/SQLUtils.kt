package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*

fun FileMetadata.toSQL(): String {
    return "CREATE TABLE ${this.tableName} (${this.fields.toSQL()})"
}

private fun Collection<AbstractDataDefinition>.toSQL(): String {
    return joinToString {
        "${it.name} ${it.sqlType()}"
    }
}

private fun AbstractDataDefinition.sqlType(): String =
    when (this.type) {
        is StringType -> "CHAR (${this.type.size})"
        is NumberType -> "DECIMAL (${this.type.size}, ${(this.type as NumberType).decimalDigits})"
        else -> TODO("Conversion to SQL Type not yet implemented: ${this.type}")
    }

infix fun String.withType(type: Type): AbstractDataDefinition = AbstractDataDefinition(this, type)