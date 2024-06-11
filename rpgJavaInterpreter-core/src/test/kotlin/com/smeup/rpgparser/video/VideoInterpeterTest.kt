package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VideoInterpeterTest : AbstractTest() {

    lateinit var configuration: Configuration

    @BeforeTest
    fun setUp() {
        configuration = Configuration()
        val path = javaClass.getResource("/video/metadata")!!.path
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        configuration.dspfConfig = DspfConfig(
            metadataProducer = { displayFile: String -> dspfConfig.getMetadata(displayFile = displayFile) },
            dspfProducer = { displayFile: String -> dspfConfig.dspfProducer(displayFile = displayFile) }
        )
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
        val expected = listOf("")
        configuration.jarikoCallback.afterAstCreation = {
            assertNotNull(it.displayFiles?.get("EXFMT1V"))
        }
        assertEquals(expected = expected, actual = "video/EXFMT1".outputOf(configuration = configuration))
    }
}