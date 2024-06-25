package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.MemorySliceId
import com.smeup.rpgparser.interpreter.StatementCounter
import com.smeup.rpgparser.interpreter.Value
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// SRSR stands for Save and Restore State Read
// SRSW stands for Save and Restore State Write

/**
 * Combined tests for memory slice storaging and statement counter storaging
 */
class SaveAndRestoreStateTest : AbstractTest() {
    lateinit var configuration: Configuration

    @BeforeTest
    fun setUp() {
        configuration = Configuration(
            memorySliceStorage = MemorySliceStorageMock(),
            statementCounterStorage = StatementCounterStorageMock()
        )
        val path = javaClass.getResource("/video/metadata")!!.path
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        configuration.dspfConfig = DspfConfig(
            metadataProducer = { displayFile: String -> dspfConfig.getMetadata(displayFile = displayFile) },
            dspfProducer = { displayFile: String -> dspfConfig.dspfProducer(displayFile = displayFile) }
        )
    }

    @Test
    fun executeSRSR01() {
        val expected = listOf("A:15", "B:15")
        val statementCounter = StatementCounter.restoredFrom(listOf(0, 1 ,1), 0)
        configuration.statementCounterStorage!!.store(statementCounter)
        val memorySliceId = MemorySliceId("*DFTACTGRP", "SRSR01")
        val values = mutableMapOf<String, Value>()
        values["A"] = IntValue(15)
        values["B"] = IntValue(15)
        configuration.memorySliceStorage!!.store(memorySliceId, values)

        assertEquals(expected = expected, actual = "video/SRSR01".outputOf(configuration = configuration))
    }

    @Test
    fun executeSRSR01_() {
        val expected = listOf("A:0", "B:0")
        assertEquals(expected = expected, actual = "video/SRSR01".outputOf(configuration = configuration))
    }

    @AfterTest
    fun clean() {}
}