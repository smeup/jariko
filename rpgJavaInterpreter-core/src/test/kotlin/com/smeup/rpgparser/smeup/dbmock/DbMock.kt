package com.smeup.rpgparser.smeup.dbmock

import com.smeup.rpgparser.interpreter.DbField
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.interpreter.StringType
import kotlin.test.todo

interface DbMock : AutoCloseable {
    fun createTable(): String
    fun dropTable(): String
    fun populateTable(): String

    override fun close() {
        dropTable()
    }

    fun buildDbColumnsFromDbFields(fields: List<DbField>): String {
        return fields.mapIndexed { i, it ->
            when (it.type) {
                is StringType -> "\"${it.fieldName}\" VARCHAR(${it.type.size}) DEFAULT '' NOT NULL".plus(if (fields.lastIndex != i) ",\n" else "\n")
                is NumberType -> buildString {
                    append("\"${it.fieldName}\"")
                    val defaultValue = if ((it.type as NumberType).decimal) {
                        " DECIMAL(${(it.type as NumberType).entireDigits}, ${(it.type as NumberType).decimalDigits}) DEFAULT 0.0"
                    } else {
                        " BIGINT DEFAULT 0"
                    }
                    append(defaultValue)
                    append(" NOT NULL".plus(if (fields.lastIndex != i) ",\n" else "\n"))
                }

                else -> todo { "Implements ${it.type} for 'buildDbColumnsFromDbFields'." }
            }
        }
        .joinToString("")
    }
}