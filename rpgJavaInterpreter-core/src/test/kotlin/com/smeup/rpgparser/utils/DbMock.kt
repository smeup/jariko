package com.smeup.rpgparser.utils

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

    fun populateTable(values: List<Map<String, Any>>): String = if (values.isEmpty()) buildDefaultPopulationQuery() else buildPopulationQuery(values)

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

    fun <R> usePopulated(
        testExecution: (DbMock) -> R,
        values: List<Map<String, Any>> = emptyList()
    ) = this.use {
        /*
         * Avoid row duplication.
         * This is extremely useful when the same test is executed in same time. For example, `executeMUDRNRAPU001134`
         *  executed both from `MULANGT50FileAccess1Test` and `MULANGT50FileAccess1TestCompiled`.
         */
        val valuesCopy = values.map { value -> value.toMutableMap() }.toMutableList()
        valuesCopy.iterator().let {
            while (it.hasNext()) {
                val value = it.next()
                val where: List<String> = value.map { cell -> "${cell.key} = '${cell.value}'" }

                val result = try {
                    execute("SELECT COUNT(*) AS recordCount FROM ${metadata.name} WHERE ${where.joinToString(" AND ")}")
                } catch (t: Throwable) {
                    null
                }

                if (result != null && result.next()) {
                    if (result.getInt("recordCount") > 0) {
                        it.remove()
                    }
                }
            }
        }

        val queries = listOf(it.createTable(), it.populateTable(valuesCopy))
        execute(queries)
        testExecution(it)
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
                        val type = it.type as NumberType
                        val totalSize = type.entireDigits + type.decimalDigits
                        " DECIMAL($totalSize, ${type.decimalDigits}) DEFAULT 0.0"
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

        return madeInsertStatement(names, values)
    }

    private fun buildPopulationQuery(mockedValues: List<Map<String, Any>>): String {
        val names = metadata.fields.joinToString { "\"${it.fieldName}\"" }
        val values = mockedValues.getFromValues(metadata.fields)

        return madeInsertStatement(names, values)
    }

    private fun madeInsertStatement(names: String, values: String): String {
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

    /**
     * Constructs a string representation of values from a list of maps, formatted as tuples of database fields.
     *
     * This function iterates over a list of maps, where each map represents a row of data with field names as keys.
     * For each map, it constructs a tuple string by matching the fields from the provided list of `DbField` objects.
     * If the value for a field is non-null and the field type is `StringType`, the value is wrapped in single quotes.
     * Otherwise, the field's default value is used.
     *
     * The resulting string is a comma-separated list of tuples, suitable for use in SQL insert statements.
     *
     * @param values a list of `DbField` objects representing the database fields to extract values for
     * @return a string representing the formatted values, each row enclosed in parentheses and separated by commas
     */
    private fun List<Map<String, Any>>.getFromValues(values: List<DbField>): String {
        var result = ""
        this.forEachIndexed { index, value ->
            result += "("
            result += values
                .map { field ->
                    var valueSearched = value.get(field.fieldName)
                    if (valueSearched != null && field.type is StringType) {
                        valueSearched = "'$valueSearched'"
                        valueSearched
                    } else {
                        field.type.getDefault()
                    }
                }
                .joinToString(", ")
            result += ")"

            if (index < this.size - 1) {
                result += ", "
            }
        }
        return result
    }
}