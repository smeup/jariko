package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
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

class ExfmtStateManagementTest : AbstractTest() {
    lateinit var configuration: Configuration

    private fun clean() {
        (configuration.snapshotManager as SnapshotManager).resetMemory()
        (configuration.snapshotManager as SnapshotManager).resetStack()
    }

    private fun coupledOutputTest(program: String, failures: Int) {
        // should I also test expected output values?
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < failures) {
            assertFailsWith<ExfmtSuspendException> {
                program.outputOf(configuration = configuration)
            }
            i++
        }
        val async = program.outputOf(configuration = configuration)

        this.clean()

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }
        val sync = program.outputOf(configuration = configuration)

        assertEquals(async, sync)
    }

    @BeforeTest
    fun setup() {
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
    fun executeSM_DO() {
        val expected = listOf("A:4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_DO".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_DO".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DO_Binary() {
        this.coupledOutputTest("video/SM_DO", 2)
    }

    @Test
    fun executeSM_DOU() {
        val expected = listOf("A:2", "B:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_DOU".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_DOU".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOU_Binary() {
        this.coupledOutputTest("video/SM_DOU", 2)
    }

    @Test
    fun executeSM_DOUEQ() {
        val expected = listOf("A:2", "B:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_DOUEQ".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_DOUEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOUEQ_Binary() {
        this.coupledOutputTest("video/SM_DOUEQ", 2)
    }

    @Test
    fun executeSM_DOUGT() {
        val expected = listOf("A:4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_DOUGT".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_DOUGT".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOUGT_Binary() {
        this.coupledOutputTest("video/SM_DOUGT", 2)
    }

    @Test
    fun executeSM_DOULT() {
        val expected = listOf("A:-4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_DOULT".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_DOULT".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOULT_Binary() {
        this.coupledOutputTest("video/SM_DOULT", 2)
    }

    @Test
    fun executeSM_DOW() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOW".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOW".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOW_Binary() {
        this.coupledOutputTest("video/SM_DOW", 1)
    }

    @Test
    fun executeSM_DOWEQ() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOWEQ".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOWEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOWEQ_Binary() {
        this.coupledOutputTest("video/SM_DOWEQ", 1)
    }

    @Test
    fun executeSM_DOWGT() {
        val expected = listOf("A:-4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_DOWGT".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_DOWGT".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOWGT_Binary() {
        this.coupledOutputTest("video/SM_DOWGT", 2)
    }

    @Test
    fun executeSM_DOWLT() {
        val expected = listOf("A:4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_DOWLT".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_DOWLT".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOWLT_Binary() {
        this.coupledOutputTest("video/SM_DOWLT", 2)
    }

    @Test
    fun executeSM_EXSR() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_EXSR".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_EXSR".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_EXSR_Binary() {
        this.coupledOutputTest("video/SM_EXSR", 1)
    }

    @Test
    fun executeSM_FOR() {
        val expected = listOf("A:6")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_FOR".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_FOR".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_FOR_Binary() {
        this.coupledOutputTest("video/SM_FOR", 2)
    }

    @Test
    fun executeSM_IF() {
        val expected = listOf("A:2", "B:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_IF".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_IF".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_IF_Binary() {
        this.coupledOutputTest("video/SM_IF", 1)
    }

    @Test
    fun executeSM_MONITOR() {
        val expected = listOf("A:4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_MONITOR".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_MONITOR".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_MONITOR_Binary() {
        this.coupledOutputTest("video/SM_MONITOR", 2)
    }

    @Test
    fun executeSM_PLAIN() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_PLAIN".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_PLAIN".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_PLAIN_Binary() {
        this.coupledOutputTest("video/SM_PLAIN", 1)
    }

    @Test
    fun executeSM_PLAINSEQ() {
        val expected = listOf("A:4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 2) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_PLAINSEQ".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_PLAINSEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_PLAINSEQ_Binary() {
        this.coupledOutputTest("video/SM_PLAINSEQ", 2)
    }

    @Test
    fun executeSM_SELECT() {
        val expected = listOf("A:4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_SELECT".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_SELECT".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_SELECT_Binary() {
        this.coupledOutputTest("video/SM_SELECT", 1)
    }

    @Test
    fun executeSM_M01_Binary() {
        this.coupledOutputTest("video/SM_M01", 7)
    }
}