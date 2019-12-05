package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DBFile
import com.smeup.rpgparser.interpreter.RecordField
import com.smeup.rpgparser.interpreter.Record
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.utils.Comparison
import java.sql.Connection
import java.sql.ResultSet

class DBSQLFile(private val name: String, private val connection: Connection) : DBFile {
    private var lastSllSql: String? = null
    private var resultSet: ResultSet? = null
    private var lastKey: List<RecordField> = emptyList()

    private val thisFileKeys: List<String> by lazy {
        val indexes = connection.primaryKeys(name)
        if (indexes.isEmpty()) connection.orderingFields(name) else indexes
    }

    override fun read(): Record {
        if (resultSet == null) {
            setll(emptyList())
        }
        require(resultSet != null) {
            "Read with empty result set"
        }
        return readFromPositionedResultSet()
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
        resultSet?.next()
    }

    override fun readEqual(keys: List<RecordField>): Record {
        val result = if (resultSet == null) {
            chain(emptyList())
        } else {
            readFromPositionedResultSet()
        }
        if (!keys.isEmpty()) {
            lastKey = keys
        }
        return filterRecord(result)
    }

    override fun eof(): Boolean = resultSet?.isAfterLast ?: true

    override fun chain(key: Value): Record = chain(toFields(key))

    override fun setll(key: Value) = setll(toFields(key))

    private fun toFields(keyValue: Value): List<RecordField> {
        val keyName = thisFileKeys.first()
        return listOf(RecordField(keyName, keyValue))
    }

    override fun chain(keys: List<RecordField>): Record {
        val keyNames = keys.map { it.name }
        // TODO Using thisFileKeys: TESTS NEEDED!!!
        val sql = "SELECT * FROM $name ${keyNames.whereSQL()} ${thisFileKeys.orderBySQL()}"
        val values = keys.map { it.value }
        executeQuery(sql, values)
        return resultSet.toValues()
    }

    override fun setll(keys: List<RecordField>): Boolean {
        val keyNames = keys.map { it.name }
        // TODO Using thisFileKeys: TESTS NEEDED!!!
        val sql = "SELECT * FROM $name ${keyNames.whereSQL(Comparison.GE)} ${thisFileKeys.orderBySQL()}"
        lastSllSql = "SELECT * FROM $name ${keyNames.whereSQL(Comparison.EQ)} ${thisFileKeys.orderBySQL()}"
        val values = keys.map { it.value }
        lastKey = keys
        executeQuery(sql, values)
        return resultSet.hasRecords()
    }

    override fun equal(): Boolean =
        if (lastSllSql == null) {
            false
        } else {
            val values = lastKey.map { it.value }
            connection.prepareStatement(lastSllSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).use {
                it.bind(values)
                it.executeQuery()
            }.hasRecords()
        }

    private fun executeQuery(sql: String, values: List<Value>) {
        resultSet.closeIfOpen()
        connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).use {
            it.bind(values)
            resultSet = it.executeQuery()
        }
    }
}
