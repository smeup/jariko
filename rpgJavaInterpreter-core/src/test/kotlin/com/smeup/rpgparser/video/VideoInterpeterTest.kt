package com.smeup.rpgparser.video

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class VideoInterpeterTest : AbstractTest() {

    lateinit var configuration: Configuration
    lateinit var configurationForRetroCompatibilityTest: Configuration

    @BeforeTest
    fun setUp() {
        configuration = Configuration()
        val path = javaClass.getResource("/video/metadata")!!.path
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        configuration.dspfConfig = DspfConfig(
            metadataProducer = { displayFile: String -> dspfConfig.getMetadata(displayFile = displayFile) }
        )
        // If dspfConfig is null metadata must be loaded from reloadConfig, as previously
        configurationForRetroCompatibilityTest = Configuration(dspfConfig = null)
            .apply {
                val reloadConfig = SimpleReloadConfig(metadataPath = path, connectionConfigs = listOf())
                this.reloadConfig = ReloadConfig(
                    nativeAccessConfig = DBNativeAccessConfig(emptyList()),
                    metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) }
                )
            }
    }

    @Test
    fun executeFILEDEF() {
        val expected = listOf("W\$PERI:12", "£RASDI:HELLO_WORLD")
        assertEquals(expected = expected, actual = "video/FILEDEF".outputOf(configuration = configuration))
    }

    @Test
    fun executeFILEDEF1() {
        val expected = listOf("W\$PERI:12", "£RASDI:HELLO_WORLD")
        assertEquals(expected = expected, actual = "video/FILEDEF1".outputOf(configuration = configurationForRetroCompatibilityTest))
    }

    @Test
    fun executeEXFMT_MOCK() {
        val expected = listOf("")
        assertEquals(expected = expected, actual = "video/EXFMT_MOCK".outputOf(configuration = configuration))
    }

    @Test
    fun executeREADC_MOCK() {
        val expected = listOf("")
        assertEquals(expected = expected, actual = "video/READC_MOCK".outputOf(configuration = configuration))
    }

    @Test
    fun executeUNLOCK_MOCK() {
        val expected = listOf("")
        assertEquals(expected = expected, actual = "video/UNLOCK_MOCK".outputOf(configuration = configuration))
    }
}