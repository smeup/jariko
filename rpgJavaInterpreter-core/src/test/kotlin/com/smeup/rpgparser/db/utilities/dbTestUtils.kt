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
import org.hsqldb.Server
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

object DBServer : Server() {
    var running = false

    fun startDB() {
        setDatabaseName(0, "mainDb")
        setDatabasePath(0, "mem:mainDb")
        port = 9001
        running = true
        start()
    }

    fun stopDB() {
        running = false
        signalCloseAllServerConnections()
        stop()
        shutdown()
    }

    fun isRunning(): Boolean {
        return running
    }
}

private val connection: Connection by lazy {
    DriverManager.getConnection(getConnectionConfig().url, getConnectionConfig().user, getConnectionConfig().password)
}

private fun getConnectionConfig(): ConnectionConfig =
    ConnectionConfig(
        fileName = "*",
        url = "jdbc:hsqldb:hsql://127.0.0.1:9001/mainDb",
        user = "SA",
        password = "",
        driver = "org.hsqldb.jdbc.JDBCDriver"
    )

private fun setSQLLog(on: Boolean) {
    val level = if (on) 3 else 0
    // TODO This statement is specific for hsqldb: we have to generalize it
    connection.createStatement().use {
        it.execute("SET DATABASE EVENT LOG SQL LEVEL $level")
    }
}

fun execute(sqlStatements: List<String>) {
    if (DBServer.isRunning() == false) {
        DBServer.startDB()
    }

    val statement = connection.createStatement()
    statement.use {
        sqlStatements.forEach { statement.addBatch(it) }
        statement.executeBatch()
    }
}

/**
 * Executes a DB program and returns the output.
 *
 * @param programName The name of the program to be executed.
 * @param metadata The metadata of the files used in the program.
 * @param initialSQL The initial SQL statements to be executed before the program.
 * @param inputParms The input parameters for the program.
 * @param configuration The configuration for the execution of the program.
 * @param trimEnd A boolean value indicating whether the output should be trimmed or not. Default value is true.
 *
 * @return A list of strings representing the output of the program. If trimEnd is true, the strings are trimmed.
 */
fun outputOfDBPgm(
    programName: String,
    metadata: List<FileMetadata>,
    initialSQL: List<String>,
    inputParms: Map<String, Value> = mapOf(),
    configuration: Configuration,
    trimEnd: Boolean = true
): List<String> {

    val si = CollectorSystemInterface()

    execute(initialSQL)

    // Get source file parh for test
    val path = {}.javaClass.classLoader.getResource("$programName.rpgle")

    // here I set the path from where jariko will search for the rpg sources
    val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile.parentFile))

    // get program and execute

    val commandLineProgram =
        getProgram(nameOrSource = programName, systemInterface = si, programFinders = rpgProgramFinders)

    val parms = inputParms

    // If needed, create ReloadConfig for Jariko
    configuration.reloadConfig = configuration.reloadConfig ?: ReloadConfig(
        nativeAccessConfig = DBNativeAccessConfig(listOf(getConnectionConfig())),
        metadataProducer = { dbFile ->
            metadata.first { it.name == dbFile }
        }
    )
    commandLineProgram.singleCall(parms, configuration)
    return if (trimEnd) si.displayed.map { it.trimEnd() } else si.displayed
}
