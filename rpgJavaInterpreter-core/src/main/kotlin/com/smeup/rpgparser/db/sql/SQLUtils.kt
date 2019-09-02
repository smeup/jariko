package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.interpreter.StringType

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
