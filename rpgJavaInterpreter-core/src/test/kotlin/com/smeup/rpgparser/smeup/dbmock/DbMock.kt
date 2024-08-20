package com.smeup.rpgparser.smeup.dbmock

import com.smeup.dbnative.ConnectionConfig
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.interpreter.*
import kotlin.test.todo

interface DbMock : AutoCloseable {
    val metadata: FileMetadata

    fun createTable(): String = """
        CREATE TABLE IF NOT EXISTS ${metadata.tableName} (
            ${this.buildDbColumnsFromDbFields(metadata.fields)})
        """.trimIndent()

    fun dropTable(): String = "DROP TABLE IF EXISTS ${metadata.tableName}"
    fun populateTable(): String = buildDefaultPopulationQuery()

    fun createConnectionConfig(): ConnectionConfig = ConnectionConfig(
        fileName = "*",
        url = "jdbc:hsqldb:hsql://127.0.0.1:9001/mainDb",
        user = "SA",
        password = "",
        driver = "org.hsqldb.jdbc.JDBCDriver"
    )

    fun createReloadConfig(): ReloadConfig {
        val metadataMap = mapOf(
            metadata.name to FileMetadata(
                name = metadata.name,
                tableName = metadata.tableName,
                recordFormat = metadata.recordFormat,
                fields = metadata.fields,
                accessFields = metadata.accessFields
            )
        )
        val metadataProducer = { file: String -> metadataMap[file]!! }
        val connectionConfig = createConnectionConfig()
        return ReloadConfig(DBNativeAccessConfig(
            connectionsConfig = listOf(connectionConfig)),
            metadataProducer = metadataProducer
        )
    }

    fun <R> usePopulated(predicate: (DbMock) -> R) = this.use {
        val queries = listOf(it.createTable(), it.populateTable())
        execute(queries)
        predicate(it)
    }

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

    private fun buildDefaultPopulationQuery(): String {
        val names = metadata.fields.joinToString { "\"${it.fieldName}\"" }
        val values = metadata.fields.joinToString { it.type.getDefault() }

        return """
            INSERT INTO ${metadata.tableName}($names) 
            VALUES ($values)
        """.trimIndent()
    }

    private fun Type.getDefault() = when (this) {
        is StringType -> "'${" ".repeat(this.length)}'"
        is NumberType -> if (this.integer) "1" else "1.0"
        else -> TODO("Implement default value for type ${this.javaClass.simpleName}") // Add more defaults
    }
}