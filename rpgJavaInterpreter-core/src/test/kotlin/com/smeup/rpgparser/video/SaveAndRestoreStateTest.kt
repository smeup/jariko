package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.interpreter.ExfmtSuspendException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun executeSRS01() {
        val expected = listOf("A:2", "B:2")
        try {
            "video/SRS01".outputOf(configuration = configuration)
        } catch (e: Exception) {
            assertTrue { e is ExfmtSuspendException }
            assertEquals(expected = expected, actual = "video/SRS01".outputOf(configuration = configuration))
        }
    }

    @AfterTest
    fun clean() {}
}