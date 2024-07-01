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
import java.util.Stack
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
        val firstEXFMTStack = Stack<Int>()
        firstEXFMTStack.addAll(listOf(0, 1, 1))
        val savedStacks: MutableList<Stack<Int>> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val snapshotManager = MainExecutionContext.getSnapshotManager() as SnapshotManager
            savedStacks.add(snapshotManager.getStack())

            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        assertEquals(expected = expected, actual = "video/STKW01".outputOf(configuration = configuration))
        assertEquals(firstEXFMTStack, savedStacks[0])
    }
}