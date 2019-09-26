package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import java.sql.Connection
import java.sql.DriverManager

class DBSQLInterface(private val dbConfiguration: DBConfiguration) : DBInterface {
    override fun open(name: String): DBFile? = DBSQLFile(name, connection)

    private val connection: Connection by lazy {
        dbConfiguration.getConnection()
    }

    fun setSQLLog(on: Boolean) {
        val level = if (on) 3 else 0
        // TODO This statement is specific for hsqldb: we have to generalize it
        connection.createStatement().use {
            it.execute("SET DATABASE EVENT LOG SQL LEVEL $level")
        }
    }

    override fun metadataOf(name: String): FileMetadata? {
        val formatName = connection.recordFormatName(name) ?: return null
        return FileMetadata(name, formatName, connection.fields(name))
    }

    fun create(tables: List<FileMetadata>) {
        val sqlStatements = tables.flatMap { it.toSQL() }
        execute(sqlStatements)
    }

    fun insertRow(tableName: String, values: List<Pair<String, Value>>) {
        val sql = tableName.insertSQL(values)
        connection.prepareStatement(sql).use {
            it.bind(values.map { it.second })
            it.execute()
        }
    }

    fun execute(sqlStatements: List<String>) {
        val statement = connection.createStatement()
        statement.use {
            sqlStatements.forEach { statement.addBatch(it) }
            statement.executeBatch()
        }
    }
}

data class DBConfiguration(var url: String, val user: String = "", val password: String = "") {
    fun getConnection(): Connection = DriverManager.getConnection(url, user, password)
}