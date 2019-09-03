package com.smeup.rpgparser.db.sql

import java.lang.StringBuilder
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