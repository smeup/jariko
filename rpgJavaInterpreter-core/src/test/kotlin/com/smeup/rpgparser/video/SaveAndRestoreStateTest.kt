package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.execution.SimpleSnapshotConfig
import com.smeup.rpgparser.execution.SnapshotConfig
import com.smeup.rpgparser.interpreter.ISymbolTable
import com.smeup.rpgparser.interpreter.InterpreterCore
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.StatementCounter
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// SRS stands for Save and Restore State

class SaveAndRestoreStateTest : AbstractTest() {
    lateinit var configuration: Configuration

    @BeforeTest
    fun setUp() {
        configuration = Configuration()
        val path = javaClass.getResource("/video/metadata")!!.path
        val snapshotPath = javaClass.getResource("/video/snapshot")!!.path
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        val snapshotConfig = SimpleSnapshotConfig(snapshotPath = snapshotPath)
        configuration.dspfConfig = DspfConfig(
            metadataProducer = { displayFile: String -> dspfConfig.getMetadata(displayFile = displayFile) },
            dspfProducer = { displayFile: String -> dspfConfig.dspfProducer(displayFile = displayFile) }
        )
        configuration.snapshotConfig = SnapshotConfig(
            save = { runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot, symbolTable: ISymbolTable ->
                snapshotConfig.save(runtimeInterpreterSnapshot = runtimeInterpreterSnapshot, symbolTable = symbolTable)
            },
            restore = { runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot ->
                snapshotConfig.restore(runtimeInterpreterSnapshot)
            }
        )
        StatementCounter.reset()
    }

    @Test
    fun executeSRS01() {
        val expected = listOf("MSG:")

        configuration.jarikoCallback.onExfmt = { _, _ -> null }

        assertEquals(expected = expected, actual = "video/SRS01".outputOf(configuration = configuration))

    }

    @AfterTest
    fun clean() {
        StatementCounter.reset()
    }
}