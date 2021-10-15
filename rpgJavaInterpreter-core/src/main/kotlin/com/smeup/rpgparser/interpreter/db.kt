/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.model.Field
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
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
    val name: String,
    val tableName: String,
    val recordFormat: String,
    val fields: List<DbField>,
    val accessFields: List<String>
) {

    @Transient
    private val fieldsByName: Map<String, DbField> = fields.map { it.fieldName to it }.toMap()

    @Transient
    val accessFieldsType: List<Type> = accessFields.map { fieldName ->
        fieldsByName[fieldName]!!.type
    }

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
        fun createInstance(inputStream: InputStream) =
            json.decodeFromString<FileMetadata>(inputStream.bufferedReader().use(BufferedReader::readText))

        /**
         * Create a json example including all jariko types
         * */
        @JvmStatic
        fun createJsonExample(): String {
            return FileMetadata(
                "MYTABLE",
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
        name = this.name,
        tableName = this.tableName,
        fields = fields.map {
            Field(it.fieldName)
        },
        fileKeys = accessFields
    )
    return fileMetadata
}