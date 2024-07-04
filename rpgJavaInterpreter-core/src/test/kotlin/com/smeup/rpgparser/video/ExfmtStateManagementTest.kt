package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.interpreter.ExfmtSuspendException
import com.smeup.rpgparser.video.snapshot.MemorySliceStorageMock
import com.smeup.rpgparser.video.snapshot.SnapshotManager
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
    fun executeSM_DO() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DO".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DO".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOU() {
        val expected = listOf("A:1", "B:1")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOU".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOU".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOUEQ() {
        val expected = listOf("A:1", "B:1")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOUEQ".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOUEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOUGT() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOUGT".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOUGT".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOULT() {
        val expected = listOf("A:-2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOULT".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOULT".outputOf(configuration = configuration))
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
    fun executeSM_DOWEQ() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOWEQ".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOWEQ".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOWGT() {
        val expected = listOf("A:-2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOWGT".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOWGT".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_DOWLT() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_DOWLT".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_DOWLT".outputOf(configuration = configuration))
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
    fun executeSM_FOR() {
        val expected = listOf("A:3")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_FOR".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_FOR".outputOf(configuration = configuration))
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
    fun executeSM_MONITOR() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_MONITOR".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_MONITOR".outputOf(configuration = configuration))
    }

    @Test
    fun executeSM_MX01() {
        val expected = listOf("A:2")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertFailsWith<ExfmtSuspendException> {
            "video/SM_MX01".outputOf(configuration = configuration)
        }
        assertEquals(expected = expected, actual = "video/SM_MX01".outputOf(configuration = configuration))
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
    fun executeSM_PLAINn() {
        val expected = listOf("A:4")
        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        var i = 0
        while (i < 30) {
            assertFailsWith<ExfmtSuspendException> {
                "video/SM_PLAINn".outputOf(configuration = configuration)
            }
            i++
        }
        assertEquals(expected = expected, actual = "video/SM_PLAINn".outputOf(configuration = configuration))
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

    @AfterTest
    fun clean() {
        (configuration.snapshotManager as SnapshotManager).resetMemory()
        (configuration.snapshotManager as SnapshotManager).resetStack()
    }
}