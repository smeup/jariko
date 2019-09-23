package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.DBFile
import com.smeup.rpgparser.interpreter.Value
import java.sql.Connection
import java.sql.ResultSet

class DBSQLFile(private val name: String, private val connection: Connection) : DBFile {
    private var resultSet: ResultSet? = null
    private val keys: List<String> by lazy {
        val indexes = connection.primaryKeys(name)
        if (indexes.isEmpty()) connection.orderingFields(name) else indexes
    }

    override fun readEqual(): List<Pair<String, Value>> {
        if (resultSet == null) {
            // TODO read file from first record in key order
            TODO("ReadEqual with no previous search")
        }
        return if (!eof()) {
            resultSet.toValues()
        } else {
            resultSet.currentRecordToValues()
        }
    }

    override fun readEqual(key: Value): List<Pair<String, Value>> {
        TODO("not implemented")
    }

    override fun readEqual(keys: List<Pair<String, Value>>): List<Pair<String, Value>> {
        TODO("not implemented")
    }

    override fun eof(): Boolean = resultSet?.isLast ?: false

    override fun chain(key: Value): List<Pair<String, Value>> {
        val keyName = keys.first()
        return chain(listOf(keyName to key))
    }

    override fun chain(keys: List<Pair<String, Value>>): List<Pair<String, Value>> {
        val keyNames = keys.map { it.first }
        val sql = "SELECT * FROM $name ${keyNames.whereSQL()} ${keyNames.orderBySQL()}"
        val values = keys.map { it.second }
        resultSet.closeIfOpen()
        connection.prepareStatement(sql).use {
            it.bind(values)
            resultSet = it.executeQuery()
        }
        return resultSet.toValues()
    }
}