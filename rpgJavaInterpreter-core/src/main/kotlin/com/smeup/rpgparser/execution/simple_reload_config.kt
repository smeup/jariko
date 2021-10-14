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

package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.FileMetadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.InputStream

private val json = Json {
    prettyPrint = true
}

/**
 * Helper class used to load reload configuration from json file
 * @param metadataPath path where is located jariko metadata
 * @param connectionConfigs List of connection config.
 * */
@Serializable
internal data class SimpleReloadConfig(var metadataPath: String? = null, val connectionConfigs: List<ConnectionConfig>) {

    internal fun getMetadata(dbFile: String): FileMetadata {
        val metadataFile = File(metadataPath, "$dbFile.json")
        require(metadataFile.exists()) {
            "$metadataFile doesn't exist"
        }
        return FileMetadata.createInstance(metadataFile.inputStream())
    }

    internal fun toJson() = json.encodeToString(this)

    companion object {

        /**
         * Create SimpleReloadConfig from json
         * */
        internal fun createInstance(jsonString: String) = json.decodeFromString<SimpleReloadConfig>(jsonString)

        /**
         * Create SimpleReloadConfig from json located in inputStream
         * */
        internal fun createInstance(inputStream: InputStream) =
            json.decodeFromString<SimpleReloadConfig>(inputStream.bufferedReader().use(BufferedReader::readText))
    }
}

/**
 * Connection configuration entry for fileName.
 * This class has the same signature of com.smeup.dbnative.ConnectionConfig but it is necessary
 * for a fast approach to its serialization, without to pass through the contextual annotation.
 * @param fileName File name or pattern (* char is allowed) for which you are settings the connection properties
 * @param url Url to access to file. For example in case of jdbc connection the protocol will be jdbc:
 * @param user User
 * @param password Password
 * @param driver If needed (in case of jdbc connection, yes) the driver implementation
 * @param impl com.smeup.dbnative.DBMManager implementation. If not specified is assumed by url
 * */
@Serializable
internal data class ConnectionConfig(
    val fileName: String,
    val url: String,
    val user: String,
    val password: String,
    val driver: String? = null,
    val impl: String? = null,
    val properties: Map<String, String> = mutableMapOf()
)

/**
 * Create a simple reload config example to use as template
 * @return Simple reload config in json format
 * */
fun createJsonSimpleReloadConfig(): String {
    return SimpleReloadConfig(
        metadataPath = "Path of file metadata",
        connectionConfigs = listOf(
            ConnectionConfig(
                fileName = "*",
                url = "jdbc:as400://defaultserver/RELEASE_LIB",
                user = "releaseUser",
                password = "releasePassword",
                driver = "com.ibm.as400.access.AS400JDBCDriver"
            ),
            ConnectionConfig(
                fileName = "*",
                url = "jdbc:as400://customserver/CUSTOM_LIB",
                user = "customUser",
                password = "customPassword",
                driver = "com.ibm.as400.access.AS400JDBCDriver"
            )
        )
    ).toJson()
}

private fun main() {
    println(createJsonSimpleReloadConfig())
}