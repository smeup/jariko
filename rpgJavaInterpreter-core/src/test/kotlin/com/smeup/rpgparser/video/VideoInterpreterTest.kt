package com.smeup.rpgparser.video

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.dspfparser.linesclassifier.ConstantValue
import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.Value
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VideoInterpreterTest : AbstractTest() {

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
    fun executeREADC_MOCK() {
        val expected = listOf("")
        configuration.jarikoCallback.onExfmt = { fields, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            // leave all fields unchanged
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }
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
        // no onExfmt needed, there is no EXFMT spec in this RPGLE file
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("FILEDEFV"))
        }
        assertEquals(expected = expected, actual = "video/FILEDEF".outputOf(configuration = configuration))
    }

    @Test
    fun executeFILEDEF1() {
        val expected = listOf("W\$PERI:12", "£RASDI:HELLO_WORLD")
        // no onExfmt needed, there is no EXFMT spec in this RPGLE file
        // this uses fallback config (reload), so displayFiles attribute should be null
        configuration.jarikoCallback.afterAstCreation = {
            assertEquals(null, it.displayFiles)
        }
        assertEquals(expected = expected, actual = "video/FILEDEF1".outputOf(configuration = configurationForRetroCompatibilityTest))
    }

    @Test
    fun executeEXFMT1() {
        val expected = listOf("FLD01:NEW_VALUE", "STR:uppercase", "INT:124", "DEC:124.45")
        configuration.jarikoCallback.onExfmt = { fields, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()

            // simulates user typing something new in FLD01
            map["FLD01"] = StringValue("NEW_VALUE")

            // user edits existing field value
            val str = fields.find { it.name == "STR" }!!.value as StringValue
            map["STR"] = StringValue(str.asString().value.lowercase())
            val int = fields.find { it.name == "INT" }!!.value as IntValue
            map["INT"] = int.plus(IntValue(1))
            val dec = fields.find { it.name == "DEC" }!!.value as DecimalValue
            map["DEC"] = dec.increment(1)

            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("EXFMT1V"))
        }
        assertEquals(expected = expected, actual = "video/EXFMT1".outputOf(configuration = configuration))
    }

    @Test
    fun executeEXFMT2() {
        val expected = emptyList<String>()
        val constants = mutableListOf<DSPFField>()
        configuration.jarikoCallback.onExfmt = { fields, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()

            fields.forEach {
                if (it.value is ConstantValue) {
                    constants.add(it)
                }
            }

            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("EXFMT2V"))
        }
        assertEquals(expected = expected, actual = "video/EXFMT2".outputOf(configuration = configuration))
        assertEquals(expected = 2, actual = constants.size)
        assertEquals(expected = "Article code", actual = (constants[0].value as ConstantValue).value)
        assertEquals(expected = 12, actual = constants[0].y)
        assertEquals(expected = 2, actual = constants[0].x)
        assertEquals(expected = "Article name", actual = (constants[1].value as ConstantValue).value)
        assertEquals(expected = 2, actual = constants[1].y)
        assertEquals(expected = 8, actual = constants[1].x)
    }
}