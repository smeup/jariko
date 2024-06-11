package com.smeup.rpgparser.video

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VideoInterpeterTest : AbstractTest() {

    lateinit var configuration: Configuration
    lateinit var configurationForRetroCompatibilityTest: Configuration

    @BeforeTest
    fun setUp() {
        configuration = Configuration()
        val path = javaClass.getResource("/video/metadata")!!.path
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        configuration.dspfConfig = DspfConfig(
            metadataProducer = { displayFile: String -> dspfConfig.getMetadata(displayFile = displayFile) },
            dspfProducer = { displayFile: String -> dspfConfig.dspfProducer(displayFile = displayFile) }
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
    fun executeFILEDEF1() {
        val expected = listOf("W\$PERI:12", "£RASDI:HELLO_WORLD")
        assertEquals(expected = expected, actual = "video/FILEDEF1".outputOf(configuration = configurationForRetroCompatibilityTest))
    }

    @Test
    fun executeEXFMT_MOCK() {
        val expected = listOf("")
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("EXFMT_MV"))
        }
        assertEquals(expected = expected, actual = "video/EXFMT_MOCK".outputOf(configuration = configuration))
    }

    @Test
    fun executeREADC_MOCK() {
        val expected = listOf("")
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("READC_MV"))
        }
        assertEquals(expected = expected, actual = "video/READC_MOCK".outputOf(configuration = configuration))
    }

    @Test
    fun executeUNLOCK_MOCK() {
        val expected = listOf("")
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("UNLOCK_MV"))
        }
        assertEquals(expected = expected, actual = "video/UNLOCK_MOCK".outputOf(configuration = configuration))
    }

    @Test
    fun executeFILEDEF() {
        val expected = listOf("W\$PERI:12", "£RASDI:HELLO_WORLD")
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("FILEDEFV"))
        }
        assertEquals(expected = expected, actual = "video/FILEDEF".outputOf(configuration = configuration))
    }

    @Test
    fun executeEXFMT1() {
        val expected = listOf("NEW_VALUE")
        configuration.jarikoCallback.onExfmt = { fields, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, String>()
//            map[fields[0].name] = "NEW_VALUE"
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("EXFMT1V"))
        }
        assertEquals(expected = expected, actual = "video/EXFMT1".outputOf(configuration = configuration))
    }
}