package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DBFile
import com.smeup.rpgparser.interpreter.Field
import com.smeup.rpgparser.interpreter.Record
import com.smeup.rpgparser.interpreter.Value
import java.sql.Connection
import java.sql.ResultSet

class DBSQLFile(private val name: String, private val connection: Connection) : DBFile {
    private var resultSet: ResultSet? = null
    private val keys: List<String> by lazy {
        val indexes = connection.primaryKeys(name)
        if (indexes.isEmpty()) connection.orderingFields(name) else indexes
    }

    override fun readEqual(): Record {
        if (resultSet == null) {
            throw RuntimeException("ReadEqual with no previous search")
        }
        return readFromPositionedResultSet()
    }

    private fun readFromPositionedResultSet(): Record {
        return if (!eof()) {
            resultSet.toValues()
        } else {
            resultSet.currentRecordToValues()
        }
    }

    override fun readEqual(key: Value): Record {
        val result = if (resultSet == null) {
            chain(emptyList())
        } else {
            readFromPositionedResultSet()
        }
        return if (result.matches(toFields(key))) {
            result
        } else {
            Record()
        }
    }

    override fun readEqual(keys: List<Field>): Record {
        TODO("not implemented")
    }

    override fun eof(): Boolean = resultSet?.isLast ?: false

    override fun chain(key: Value): Record {
        return chain(toFields(key))
    }

    private fun toFields(keyValue: Value): List<Field> {
        val keyName = keys.first()
        return listOf(Field(keyName, keyValue))
    }

    override fun chain(keys: List<Field>): Record {
        val keyNames = keys.map { it.name }
        val sql = "SELECT * FROM $name ${keyNames.whereSQL()} ${keyNames.orderBySQL()}"
        val values = keys.map { it.value }
        resultSet.closeIfOpen()
        connection.prepareStatement(sql).use {
            it.bind(values)
            resultSet = it.executeQuery()
        }
        return resultSet.toValues()
    }
}