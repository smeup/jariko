package com.smeup.rpgparser.video

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import kotlin.test.BeforeTest
import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test

class VideoInterpeterTest : AbstractTest() {

    lateinit var configuration: Configuration

    @BeforeTest
    fun setUp() {
        configuration = Configuration()
        val path = javaClass.getResource("/video/metadata")!!.path
        val reloadConfig = SimpleReloadConfig(metadataPath = path, connectionConfigs = listOf())
        configuration.reloadConfig = ReloadConfig(
            nativeAccessConfig = DBNativeAccessConfig(emptyList()),
            metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) })
    }

    @Test
    fun executeFILEDEF() {
        // I want to check the compilation unit
        configuration.jarikoCallback.afterAstCreation = { ast ->
            assertTrue(message = "file video B£DIR40V should be defined", ast.hasFileDefinition("B£DIR40V"))
            assertTrue(message = "field £RASDI should be defined", ast.hasAnyDataDefinition("£RASDI"))
        }
        "video/FILEDEF".outputOf(configuration = configuration)
    }
}