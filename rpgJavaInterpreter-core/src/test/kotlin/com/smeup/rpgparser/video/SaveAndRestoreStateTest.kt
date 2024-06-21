package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.execution.SimpleSnapshotConfig
import com.smeup.rpgparser.execution.SnapshotConfig
import com.smeup.rpgparser.interpreter.InterpreterCore
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
            save = { snapshotFile: String, interpreter: InterpreterCore ->
                snapshotConfig.save(snapshotFile = snapshotFile, interpreter = interpreter)
            }
        )
        StatementCounter.reset()
    }

    @Test
    fun executeSRS01() {
        val expected = listOf("MSG:New message set to variable!")

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            // val map = mutableMapOf<String, Value>()

            // try to alter symbol table to check if it will be set successfully
            // the variable changed is not in the display file
            // val msg = runtimeInterpreterSnapshot.symbolTable.dataDefinitionByName("MSG")!!
            // runtimeInterpreterSnapshot.symbolTable[msg] = StringValue("New message set to variable!")

            // OnExfmtResponse(runtimeInterpreterSnapshot, map)
            null
        }

        assertEquals(expected = expected, actual = "video/SRS01".outputOf(configuration = configuration))
    }

    @AfterTest
    fun clean() {
        StatementCounter.reset()
    }
}