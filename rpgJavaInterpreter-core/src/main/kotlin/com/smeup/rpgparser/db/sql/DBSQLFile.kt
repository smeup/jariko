package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DBFile
import com.smeup.rpgparser.interpreter.Field
import com.smeup.rpgparser.interpreter.Record
import com.smeup.rpgparser.interpreter.Value
import java.sql.Connection
import java.sql.ResultSet

class DBSQLFile(private val name: String, private val connection: Connection) : DBFile {
    private var resultSet: ResultSet? = null
    private var lastKey: List<Field> = emptyList()

    private val keys: List<String> by lazy {
        val indexes = connection.primaryKeys(name)
        if (indexes.isEmpty()) connection.orderingFields(name) else indexes
    }

    override fun readEqual(): Record {
        require(resultSet != null) {
            "ReadEqual with no previous search"
        }
        return filterRecord(readFromPositionedResultSet())
    }

    private fun readFromPositionedResultSet(): Record {
        return if (!eof()) {
            resultSet.toValues()
        } else {
            resultSet.currentRecordToValues()
        }
    }

    override fun readEqual(key: Value): Record {
        return readEqual(toFields(key))
    }

    private fun filterRecord(result: Record): Record {
        return if (result.matches(lastKey)) {
            result
        } else {
            signalEOF()
            Record()
        }
    }

    private fun signalEOF() {
        resultSet?.last()
    }

    override fun readEqual(keys: List<Field>): Record {
        val result = if (resultSet == null) {
            chain(emptyList())
        } else {
            readFromPositionedResultSet()
        }
        lastKey = keys
        return filterRecord(result)
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
        connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).use {
            it.bind(values)
            resultSet = it.executeQuery()
        }
        return resultSet.toValues()
    }
}