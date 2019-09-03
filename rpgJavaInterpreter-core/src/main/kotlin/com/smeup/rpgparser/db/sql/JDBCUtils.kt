package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.lang.StringBuilder
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