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

package com.smeup.rpgparser.db.utilities

import com.smeup.dbnative.ConnectionConfig
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Real PostgreSQL backend for DB-integration tests, alongside [outputOfDBPgm]'s default HSQLDB
 * one. Needed specifically to exercise PostgreSQLDialect's direct-RRN path (reload's `__RNN`
 * identity column) on a KEYED file - something HSQLDB cannot do at all (a keyed HSQLDB read's
 * Result.rrn is always null, by reload's own design - see the companion smeuperp workspace's
 * docs/plans/rrn-output-support-reload.md). Every test using this MUST guard itself with
 * `org.junit.Assume.assumeTrue(isPostgresAvailable())` first, so the suite is skipped - never
 * failed - when no PostgreSQL container is reachable.
 */
private fun postgresConnectionConfig(): ConnectionConfig =
    ConnectionConfig(
        fileName = "*",
        url = "jdbc:postgresql://localhost:5432/postgres",
        user = System.getenv("PG_USER") ?: "root",
        password = System.getenv("PG_PASSWORD") ?: "root",
        driver = "org.postgresql.Driver",
    )

private val postgresConnection: Connection? by lazy {
    try {
        val config = postgresConnectionConfig()
        DriverManager.getConnection(config.url, config.user, config.password)
    } catch (e: SQLException) {
        null
    }
}

fun isPostgresAvailable(): Boolean = postgresConnection != null

private fun executePostgres(sqlStatements: List<String>) {
    val connection =
        requireNotNull(postgresConnection) {
            "PostgreSQL is not available - guard the caller with Assume.assumeTrue(isPostgresAvailable())"
        }
    connection.createStatement().use { statement ->
        sqlStatements.forEach { statement.addBatch(it) }
        statement.executeBatch()
    }
}

/**
 * PostgreSQL counterpart of `com.smeup.rpgparser.db.utilities.outputOfDBPgm` - same contract,
 * connects to a real local PostgreSQL instead of the HSQLDB server that function starts.
 */
fun outputOfDBPgmPostgres(
    programName: String,
    metadata: List<FileMetadata>,
    initialSQL: List<String>,
    inputParms: Map<String, Value> = mapOf(),
    configuration: Configuration,
    trimEnd: Boolean = true,
): List<String> {
    val si = CollectorSystemInterface()

    executePostgres(initialSQL)

    val path = {}.javaClass.classLoader.getResource("$programName.rpgle")
    val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile.parentFile))

    val commandLineProgram = getProgram(nameOrSource = programName, systemInterface = si, programFinders = rpgProgramFinders)

    configuration.reloadConfig = configuration.reloadConfig ?: ReloadConfig(
        nativeAccessConfig = DBNativeAccessConfig(listOf(postgresConnectionConfig())),
        metadataProducer = { dbFile ->
            metadata.first { it.name == dbFile }
        },
    )
    commandLineProgram.singleCall(inputParms, configuration)
    return if (trimEnd) si.displayed.map { it.trimEnd() } else si.displayed
}
