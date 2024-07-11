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
import com.smeup.rpgparser.video.snapshot.StackInfo
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StackTraceTest : AbstractTest() {
    lateinit var configuration: Configuration

    private fun <T> assertFailsWith(program: String, failures: Int) {
        var i = 0
        while (i < failures) {
            assertFailsWith<ExfmtSuspendException> {
                program.outputOf(configuration = configuration)
            }
            i++
        }
    }

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
    fun executeCALL_X_W() {
        val expected = listOf("A:1")
        val info: MutableList<StackInfo> = mutableListOf()
        val programs: MutableList<String> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, _ ->
            val snapshotManager = MainExecutionContext.getSnapshotManager() as SnapshotManager
            info.add(snapshotManager.getStackInfo())
            programs.add(MainExecutionContext.getProgramStack().peek()!!.name)
            null
        }

        this.assertFailsWith<ExfmtSuspendException>("video/CALL_X", 2)
        assertEquals(expected = expected, actual = "video/CALL_X".outputOf(configuration = configuration))
        assertEquals("C_EAE", programs[0])
        assertEquals(StackInfo(listOf(1), 0, "EX_NOVO"), info[0])
        assertEquals("C_EAE", programs[1])
        assertEquals(StackInfo(listOf(3), 0, "CONTINUE"), info[1])
    }

    @Test
    fun executeCALL_X1_W() {
        val expected = listOf("A:0")
        val info: MutableList<StackInfo> = mutableListOf()
        val programs: MutableList<String> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, _ ->
            val snapshotManager = MainExecutionContext.getSnapshotManager() as SnapshotManager
            info.add(snapshotManager.getStackInfo())
            programs.add(MainExecutionContext.getProgramStack().peek()!!.name)
            null
        }

        this.assertFailsWith<ExfmtSuspendException>("video/CALL_X1", 5)
        assertEquals(expected = expected, actual = "video/CALL_X1".outputOf(configuration = configuration))
        assertEquals("CALL_X1", programs[0])
        assertEquals(StackInfo(listOf(0), 0, "EX_NOVO"), info[0])
        assertEquals("CALL_X1", programs[1])
        assertEquals(StackInfo(listOf(1), 0, "CONTINUE"), info[1])

        assertEquals("C_E", programs[2])
        assertEquals(StackInfo(listOf(1), 0, "EX_NOVO"), info[2])

//        assertEquals("CALL_X1", programs[3])
//        assertEquals(StackInfo(listOf(3), 0, "RESUME"), info[3])
//        assertEquals("CALL_X1", programs[4])
//        assertEquals(StackInfo(listOf(4), 0, "CONTINUE"), info[4])
    }

    @Test
    fun executeCALL_X2_W() {
        val expected = listOf("A:1")
        val info: MutableList<StackInfo> = mutableListOf()
        val programs: MutableList<String> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, _ ->
            val snapshotManager = MainExecutionContext.getSnapshotManager() as SnapshotManager
            info.add(snapshotManager.getStackInfo())
            programs.add(MainExecutionContext.getProgramStack().peek()!!.name)
            null
        }

        this.assertFailsWith<ExfmtSuspendException>("video/CALL_X2", 6)
        assertEquals(expected = expected, actual = "video/CALL_X2".outputOf(configuration = configuration))
        assertEquals("CALL_X2", programs[0])
        assertEquals(StackInfo(listOf(0), 0, "EX_NOVO"), info[0])
        assertEquals("CALL_X2", programs[1])
        assertEquals(StackInfo(listOf(1), 0, "CONTINUE"), info[1])

        assertEquals("C_EAE", programs[2])
        assertEquals(StackInfo(listOf(1), 0, "EX_NOVO"), info[2])
        assertEquals("C_EAE", programs[3])
        assertEquals(StackInfo(listOf(3), 0, "CONTINUE"), info[3])

//        assertEquals("CALL_X2", programs[4])
//        assertEquals(StackInfo(listOf(4), 0, "RESUME"), info[4])
//        assertEquals("CALL_X2", programs[5])
//        assertEquals(StackInfo(listOf(4), 0, "CONTINUE"), info[4])
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
    fun executeST_PLAINSEQ_R0() {
        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        (configuration.snapshotManager as SnapshotManager).allowStackReadTest = true
        assertEquals(expected = listOf("A:3", "B:3"), actual = "video/ST_PLAINSEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeST_PLAINSEQ_R1() {
        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        (configuration.snapshotManager as SnapshotManager).allowStackReadTest = true
        (configuration.snapshotManager as SnapshotManager).setStackWithList(listOf(2))
        assertEquals(expected = listOf("A:2", "B:2"), actual = "video/ST_PLAINSEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeST_PLAINSEQ_R2() {
        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        (configuration.snapshotManager as SnapshotManager).allowStackReadTest = true
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

        this.assertFailsWith<ExfmtSuspendException>("video/ST_PLAINSEQ", 2)
        assertEquals(expected = expected, actual = "video/ST_PLAINSEQ".outputOf(configuration = configuration))
        assertEquals(listOf(2), savedStacksAsLists[0])
        assertEquals(listOf(5), savedStacksAsLists[1])
    }
}