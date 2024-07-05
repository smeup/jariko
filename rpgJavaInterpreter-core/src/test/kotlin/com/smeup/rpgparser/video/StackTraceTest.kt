package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.interpreter.ExfmtSuspendException
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.video.snapshot.MemorySliceStorageMock
import com.smeup.rpgparser.video.snapshot.SnapshotManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StackTraceTest : AbstractTest() {
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
    fun executeST_IF_W() {
        val expected = listOf("A:1", "B:1")
        val savedStacksAsLists: MutableList<List<Int>> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val snapshotManager = MainExecutionContext.getSnapshotManager() as SnapshotManager
            savedStacksAsLists.add(snapshotManager.getStackAsList())

            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        assertEquals(expected = expected, actual = "video/ST_IF".outputOf(configuration = configuration))
        assertEquals(listOf(0), savedStacksAsLists[0])
        assertEquals(listOf(1, 1), savedStacksAsLists[1])
        assertEquals(listOf(1, 2, 1), savedStacksAsLists[2])
        assertEquals(listOf(2), savedStacksAsLists[3])
    }

    @Test
    fun executeST_PLAINSEQ_R() {
        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        assertEquals(expected = listOf("A:3", "B:3"), actual = "video/ST_PLAINSEQ".outputOf(configuration = configuration))

        (configuration.snapshotManager as SnapshotManager).setStackWithList(listOf(2))
        assertEquals(expected = listOf("A:2", "B:2"), actual = "video/ST_PLAINSEQ".outputOf(configuration = configuration))

        (configuration.snapshotManager as SnapshotManager).setStackWithList(listOf(5))
        assertEquals(expected = listOf("A:1", "B:1"), actual = "video/ST_PLAINSEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeST_PLAINSEQ_W() {
        val expected = listOf("A:3", "B:3")
        val savedStacksAsLists: MutableList<List<Int>> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, _ ->
            val snapshotManager = MainExecutionContext.getSnapshotManager() as SnapshotManager
            savedStacksAsLists.add(snapshotManager.getStackAsList())
            null
        }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/ST_PLAINSEQ".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/ST_PLAINSEQ".outputOf(configuration = configuration))
        assertEquals(listOf(2), savedStacksAsLists[0])
        assertEquals(listOf(5), savedStacksAsLists[1])
    }
}