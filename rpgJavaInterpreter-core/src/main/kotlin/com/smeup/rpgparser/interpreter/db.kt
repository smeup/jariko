package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.model.CharacterType
import com.smeup.dbnative.model.Field
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream

private val json = Json {
    prettyPrint = true
}

/**
 * This class is associated to a db field.
 * It is used by jariko to create instance of DataDefinition
 * @see toDataDefinition
 * */
@Serializable
data class DbField(val fieldName: String, val type: Type) {

    /**
     * Creates a data definition associated to this
     * */
    fun toDataDefinition(prefix: Prefix?) = DataDefinition(prefix?.applyReplacementRules(fieldName) ?: fieldName, type)
}

/**
 * Contains information needed for the native access implementation.
 * */
@Serializable
data class FileMetadata(
    val tableName: String,
    val recordFormat: String,
    val fields: List<DbField>,
    val accessFields: List<String>
) {
    fun toJson(): String = json.encodeToString(this)

    companion object {

        /**
         * Create FileMetadataInstance from json
         * */
        @JvmStatic
        fun createInstance(jsonString: String) = json.decodeFromString<FileMetadata>(jsonString)

        /**
         * Create FileMetadataInstance from json located in inputStream
         * */
        @JvmStatic
        fun createInstance(inputStream: InputStream) = json.decodeFromString<FileMetadata>(String(inputStream.readAllBytes()))

        /**
         * Create a json example including all jariko types
         * */
        @JvmStatic
        fun createJsonExample(): String {
            return FileMetadata(
                "MYTABLE",
                "MYRECORDFORMAT",
                listOf(
                    DbField(fieldName = "ALPHANUM", StringType(10, true)),
                    DbField(fieldName = "BINARY", NumberType(entireDigits = 10, decimalDigits = 2, RpgType.BINARY)),
                    DbField(fieldName = "INTEGER", NumberType(entireDigits = 10, decimalDigits = 0, RpgType.INTEGER)),
                    DbField(fieldName = "PACKED", NumberType(entireDigits = 10, decimalDigits = 2, RpgType.PACKED)),
                    DbField(fieldName = "UNSIGNED", NumberType(entireDigits = 10, decimalDigits = 0, RpgType.UNSIGNED)),
                    DbField(fieldName = "ZONED", NumberType(entireDigits = 10, decimalDigits = 2, RpgType.ZONED)),
                    DbField(fieldName = "STRINGARRAY", ArrayType(element = StringType(10), nElements = 10)),
                    DbField(fieldName = "BOOLEAN", BooleanType)
                ),
                accessFields = listOf("ALPHANUM", "BINARY")
            ).toJson()
        }

        /**
         * Print json example of metadata including all jariko types
         * */
        @JvmStatic
        fun printJsonExample() {
            println(createJsonExample())
        }
    }
}

fun FileMetadata.toReloadMetadata(): com.smeup.dbnative.model.FileMetadata {
    val fileMetadata = com.smeup.dbnative.model.FileMetadata(
        tableName = this.tableName,
        recordFormat = this.recordFormat,
        fields = fields.map {
            Field(it.fieldName, CharacterType(112))
        },
        fileKeys = accessFields
    )
    return fileMetadata
}