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

/**
 * This test class provides a way to test if a stack can really be used to restore computation
 * from a custom point. Program is restored without using a symbol to set variables values to the state
 * they are at restored point; these tests should be adapted to complain with further project changes.
 */
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
        StatementCounter.reset()
    }

    @Test
    fun executeSTM01FromStart() {
        val expected = listOf("A:1", "B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STM01".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM01FromHalf() {
        val expected = listOf("B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(3), 0)
        assertEquals(expected = expected, actual = "video/STM01".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM02FromStart() {
        val expected = listOf("A:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STM02".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM02FromHalf() {
        val expected = listOf("")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(2), 0)
        assertEquals(expected = expected, actual = "video/STM02".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM03FromStart() {
        val expected = listOf("A:10", "B:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STM03".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM03FromHalf() {
        val expected = listOf("B:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(3), 0)
        assertEquals(expected = expected, actual = "video/STM03".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM04FromStart() {
        val expected = listOf("A:1", "B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STM04".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM04FromHalf() {
        val expected = listOf("A:2", "B:2")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(0, 2, 0), 0)
        assertEquals(expected = expected, actual = "video/STM04".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM05FromStart() {
        val expected = listOf("A:1", "B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STM05".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTM05FromHalf() {
        val expected = listOf("A:10", "B:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(0, 2, 2), 0)
        assertEquals(expected = expected, actual = "video/STM05".outputOf(configuration = configuration))
    }

    @AfterTest
    fun clean() {
        StatementCounter.reset()
    }

}