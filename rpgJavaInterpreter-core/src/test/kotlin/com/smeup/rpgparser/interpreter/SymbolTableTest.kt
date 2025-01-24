package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.DBServer
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.execution.ConnectionConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import com.smeup.rpgparser.interpreter.dbmock.ST01DbMock
import junit.framework.TestCase.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test

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

    /**
     * In this test we have a Data Structure declared as not `QUALIFIED` by using `EXTNAME` keyword.
     * When I use a set (by `EVAL`) for a field without dot notation, the parent DS must be find in parent
     *  of Symbol Table.
     */
    @Test
    fun executeSTDSUNQUALIFIED1() {
        val expected = listOf("FOO")
        assertEquals(expected, "symboltable/STDSUNQUALIFIED1".outputOf(configuration = smeupConfig))
    }

    /**
     * In this test we have a Data Structure declared as not `QUALIFIED` by using `EXTNAME` keyword and
     *  a File to the same declared for DS `EXTNAME`. In this case the File fields are removed from parent
     *  of Symbol Table. Like for `CHAIN` of this test, every field must be resolved in DS.
     */
    @Test
    fun executeSTDSCHAIN1() {
        ST01DbMock().usePopulated({
            val expected = listOf("1", "FOO", "BAR")
            assertEquals(expected, "symboltable/STDSCHAIN1".outputOf(configuration = smeupConfig))
        },
            listOf(mapOf("ST01_KEY" to "1", "ST01_COL1" to "FOO", "ST01_COL2" to "BAR"))
        )
    }

    /**
     * In this test we have a Fil and all fields are placed on parent.
     * So, the resolution must be in this place.
     */
    @Test
    fun executeSTFCHAIN1() {
        ST01DbMock().usePopulated({
            val expected = listOf("1", "FOO", "BAR")
            assertEquals(expected, "symboltable/STFCHAIN1".outputOf(configuration = smeupConfig))
        },
            listOf(mapOf("ST01_KEY" to "1", "ST01_COL1" to "FOO", "ST01_COL2" to "BAR"))
        )
    }
}