package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.video.snapshot.MemorySliceStorageMock
import com.smeup.rpgparser.video.snapshot.SnapshotManager
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun executeSTKW01() {
        val expected = listOf("A:1", "B:1")
        val savedStacksAsLists: MutableList<List<Int>> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val snapshotManager = MainExecutionContext.getSnapshotManager() as SnapshotManager
            savedStacksAsLists.add(snapshotManager.getStackAsList())

            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        assertEquals(expected = expected, actual = "video/STKW01".outputOf(configuration = configuration))
        assertEquals(listOf(0), savedStacksAsLists[0])
        assertEquals(listOf(1, 1), savedStacksAsLists[1])
        assertEquals(listOf(1, 2, 1), savedStacksAsLists[2])
        assertEquals(listOf(2), savedStacksAsLists[3])
    }

    @Test
    fun executeSTKR01FromStart() {
        val expected = listOf("A:3", "B:3")

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        assertEquals(expected = expected, actual = "video/STKR01".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR01() {
        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        (configuration.snapshotManager as SnapshotManager).setStackWithList(listOf(2))
        assertEquals(expected = listOf("A:2", "B:2"), actual = "video/STKR01".outputOf(configuration = configuration))

        (configuration.snapshotManager as SnapshotManager).setStackWithList(listOf(5))
        assertEquals(expected = listOf("A:1", "B:1"), actual = "video/STKR01".outputOf(configuration = configuration))
    }

    @AfterTest
    fun clean() {
        (configuration.snapshotManager as SnapshotManager).resetStack()
    }
}