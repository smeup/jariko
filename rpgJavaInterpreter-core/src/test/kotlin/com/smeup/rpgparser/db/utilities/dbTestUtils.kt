package com.smeup.rpgparser.db.utilities

import com.smeup.dbnative.ConnectionConfig
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.dbnative.model.FileMetadata
import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.hsqldb.Server
import org.hsqldb.server.ServerConstants
import java.io.File
import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


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
        val statement = connection.createStatement()
        statement.use {
            sqlStatements.forEach { statement.addBatch(it) }
            statement.executeBatch()
        }
    }

    fun outputOfDBPgm(programName: String, metadata: List<FileMetadata>, initialSQL: List<String>, inputParms: Map<String, StringValue> = mapOf(), printTree: Boolean = false): List<String> {

        val si = CollectorSystemInterface()
        try {
            execute(initialSQL)

            // Get source file parh for test
            val path = {}.javaClass.classLoader.getResource("$programName.rpgle")

            // here I set the path from where jariko will search for the rpg sources
            val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile.parentFile))

            // get program and execute

            val commandLineProgram = getProgram(nameOrSource = programName, systemInterface = si, programFinders = rpgProgramFinders)

            val parms = inputParms.values.map { it.value.toString() }

            // Create ReloadConfig for Jariko
            val conf = Configuration(
                    reloadConfig = ReloadConfig(
                            nativeAccessConfig = DBNativeAccessConfig(listOf(getConnectionConfig())),
                            metadata = metadata
                    ),
                    defaultActivationGroupName = "MYGRP"
            )

            commandLineProgram.singleCall(parms, conf)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }

        return si.displayed
    }

    fun startDB(): Server {

        val server = Server()
        server.setDatabaseName(0, "mainDb")
        server.setDatabasePath(0, "mem:mainDb")
        server.setPort(9001)
        server.start()

        /*
        var startThread = Thread {
            server.start()
        }
        val executor = Executors.newFixedThreadPool(1)
        executor.execute(startThread)
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
         */

        return server
    }

    fun stopDB(server: Server) {
        server.signalCloseAllServerConnections();
        server.stop();
        server.shutdown();

    }
