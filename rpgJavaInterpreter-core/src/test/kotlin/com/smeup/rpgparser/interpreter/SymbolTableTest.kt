package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.DBServer
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.execution.ConnectionConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import kotlin.test.BeforeTest

/**
 * The purpose of this test suite is to validate the behaviour around Symbol Table, directly or not.
 * The resources about this suite starts with `ST` (Symbol Table), followed by any string which describes the purpose.
 * Could be a Data Struct (DS), Standalone (S), File (F) or Inline (I) with an operation on this.
 */
class SymbolTableTest : AbstractTest() {
    /**
     * The configuration used to execute some kind of tests, for example all test required files.
     * The required files are placed in the resources/symboltable/metadata folder.
     */
    lateinit var smeupConfig: Configuration

    @BeforeTest
    fun setUp() {
        if (!DBServer.isRunning()) {
            DBServer.startDB()
        }

        smeupConfig = Configuration()
        val path = javaClass.getResource("/symboltable/metadata")!!.path
        val connectionConfigs = listOf(
            ConnectionConfig(
               fileName = "*",
                url = "jdbc:hsqldb:hsql://127.0.0.1:9001/mainDb",
                user = "SA",
                password = "",
                driver = "org.hsqldb.jdbc.JDBCDriver"
            )
        )
        val reloadConfig = SimpleReloadConfig(metadataPath = path, connectionConfigs = connectionConfigs)
        smeupConfig.reloadConfig = ReloadConfig(
            nativeAccessConfig = DBNativeAccessConfig(connectionConfigs.map {
                com.smeup.dbnative.ConnectionConfig(
                    fileName = it.fileName,
                    url = it.url,
                    user = it.user,
                    password = it.password,
                    driver = it.driver,
                    impl = it.impl
                )
            }),
            metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) })
    }
}