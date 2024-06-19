package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.interpreter.StatementCounter
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveAndRestoreStateTest : AbstractTest() {
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
    fun executeSTM01FromStart() {
        val expected = listOf("A:1", "B:1")
        StatementCounter.useAsRestored()
        StatementCounter.set(listOf(0), 0)
        assertEquals(expected = expected, actual = "video/STM01".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM01FromHalf() {
        val expected = listOf("B:1")
        StatementCounter.useAsRestored()
        StatementCounter.set(listOf(3), 0)
        assertEquals(expected = expected, actual = "video/STM01".outputOf(configuration = configuration))
    }

    @AfterTest
    fun clean() {
        StatementCounter.useAsExNovo()
        StatementCounter.set(listOf(), -1)
    }

}