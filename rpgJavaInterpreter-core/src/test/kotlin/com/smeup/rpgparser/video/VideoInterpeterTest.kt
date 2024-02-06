package com.smeup.rpgparser.video

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import com.smeup.rpgparser.interpreter.InterpreterCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

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
        lateinit var interpreter: InterpreterCore
        configuration.jarikoCallback.onInterpreterCreation = { it ->
            interpreter = it
        }
        "video/FILEDEF".outputOf(configuration = configuration)
        assertNotNull(message = "field £RASDI should be defined", actual = interpreter["£RASDI"])
    }
}