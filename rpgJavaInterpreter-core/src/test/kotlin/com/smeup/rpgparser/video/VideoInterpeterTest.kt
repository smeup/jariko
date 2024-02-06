package com.smeup.rpgparser.video

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import kotlin.test.BeforeTest
import kotlin.test.DefaultAsserter
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
        configuration.jarikoCallback.onExitPgm = { _, symbolTable, _ ->
            DefaultAsserter.assertNotNull(message = "field £RASDI should be defined", actual = symbolTable["£RASDI"])
        }
        "video/FILEDEF".outputOf(configuration = configuration)
    }
}