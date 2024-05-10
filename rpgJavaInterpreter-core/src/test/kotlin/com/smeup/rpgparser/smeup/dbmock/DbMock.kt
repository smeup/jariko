package com.smeup.rpgparser.smeup.dbmock

import com.smeup.rpgparser.interpreter.DbField
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.interpreter.StringType
import kotlin.test.todo

interface DbMock {
    fun createTable(): String
    fun dropTable(): String
    fun populateTable(): String

    fun buildDbColumnsFromDbFields(fields: List<DbField>): String {
        return fields.mapIndexed { i, it ->
            when {
                it.type is StringType -> "${it.fieldName} VARCHAR(${it.type.size}) DEFAULT '' NOT NULL".plus(if (fields.lastIndex != i) ",\n" else "\n")
                it.type is NumberType -> {
                    var columnDeclaration = it.fieldName
                    if ((it.type as NumberType).decimal) {
                        columnDeclaration += " DECIMAL(${(it.type as NumberType).entireDigits}, ${(it.type as NumberType).decimalDigits}) DEFAULT 0.0"
                    } else {
                        columnDeclaration += " BIGINT DEFAULT 0"
                    }

                    columnDeclaration += " NOT NULL".plus(if (fields.lastIndex != i) ",\n" else "\n")

                    columnDeclaration
                }
                else -> todo { "Implements ${it.type} for 'buildDbColumnsFromDbFields'." }
            }
        }
        .joinToString("")
    }
}