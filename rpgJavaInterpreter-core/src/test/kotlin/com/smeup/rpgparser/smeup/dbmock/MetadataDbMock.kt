package com.smeup.rpgparser.smeup.dbmock

import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.interpreter.StringType
import com.smeup.rpgparser.interpreter.Type

abstract class MetadataDbMock : DbMock {
    abstract val metadata: FileMetadata

    override fun createTable(): String = """
        CREATE TABLE IF NOT EXISTS ${metadata.tableName} (
            ${this.buildDbColumnsFromDbFields(metadata.fields)})
        """
        .trimIndent()
    override fun dropTable(): String = "DROP TABLE IF EXISTS ${metadata.tableName}"
    override fun populateTable(): String = buildDefaultPopulationQuery()

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