package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.video.snapshot.ExfmtSuspendInterrupt
import com.smeup.rpgparser.video.snapshot.MemorySliceStorageMock
import com.smeup.rpgparser.video.snapshot.SnapshotManager
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExfmtStateManagementTest : AbstractTest() {
    lateinit var configuration: Configuration

    @BeforeTest
    fun setUp() {
        val memorySliceStorage = MemorySliceStorageMock()
        configuration = Configuration(
            memorySliceStorage = memorySliceStorage,
            snapshotManager = SnapshotManager(memorySliceStorage)
        )
        val path = javaClass.getResource("/video/metadata")!!.path
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        configuration.dspfConfig = DspfConfig(
            metadataProducer = { displayFile: String -> dspfConfig.getMetadata(displayFile = displayFile) },
            dspfProducer = { displayFile: String -> dspfConfig.dspfProducer(displayFile = displayFile) }
        )
    }

    @Test
    fun executeSM01() {
        val expected = listOf("A:3", "B:3")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        try {
            "video/SM01".outputOf(configuration = configuration)
        } catch (e: Exception) {
            assertTrue { e is ExfmtSuspendInterrupt }
            assertEquals(expected = expected, actual = "video/SM01".outputOf(configuration = configuration))
        }
    }

    @Test
    fun executeSM02() {
        val expected = listOf("A:2", "B:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        try {
            "video/SM02".outputOf(configuration = configuration)
        } catch (e: Exception) {
            assertTrue { e is ExfmtSuspendInterrupt }
            assertEquals(expected = expected, actual = "video/SM02".outputOf(configuration = configuration))
        }
    }

    @AfterTest
    fun clean() {
        (configuration.snapshotManager as SnapshotManager).resetMemory()
        (configuration.snapshotManager as SnapshotManager).resetStack()
    }
}