package com.smeup.rpgparser.video

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.StatementCounter
import com.smeup.rpgparser.interpreter.Value
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// Test files categories:
// STKR* stands for STacK Read (does not use symbol table)
// STKW* stands for STacK Write (does not use symbol table)

class StatementCounterTest : AbstractTest() {
    lateinit var configuration: Configuration

    @BeforeTest
    fun setUp() {
        configuration = Configuration()
        val path = javaClass.getResource("/video/metadata")!!.path
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        configuration.dspfConfig = DspfConfig(
            metadataProducer = { displayFile: String -> dspfConfig.getMetadata(displayFile = displayFile) },
            dspfProducer = { displayFile: String -> dspfConfig.dspfProducer(displayFile = displayFile) }
        )
        StatementCounter.reset()
    }

    @Test
    fun executeSTKW01() {
        val expected = listOf("A:1", "B:1")
        val firstEXFMTStack = Stack<Int>()
        firstEXFMTStack.addAll(listOf(0, 1, 1))
        val savedStacks: MutableList<Stack<Int>> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            savedStacks.add(StatementCounter.clone())

            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        assertEquals(expected = expected, actual = "video/STKW01".outputOf(configuration = configuration))
        assertEquals(firstEXFMTStack, savedStacks[0])
        assertEquals(Stack(), StatementCounter)
    }

    @Test
    fun executeSTKW02() {
        val expected = listOf("A:10", "B:10")
        val firstEXFMTStack = Stack<Int>()
        firstEXFMTStack.addAll(listOf(0))
        val secondEXFMTStack = Stack<Int>()
        secondEXFMTStack.addAll(listOf(1, 1, 1))
        val thirdEXFMTStack = Stack<Int>()
        thirdEXFMTStack.addAll(listOf(2))

        val savedStacks: MutableList<Stack<Int>> = mutableListOf()

        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            savedStacks.add(StatementCounter.clone())

            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }

        assertEquals(expected = expected, actual = "video/STKW02".outputOf(configuration = configuration))
        assertEquals(firstEXFMTStack, savedStacks[0])
        assertEquals(secondEXFMTStack, savedStacks[1])
        assertEquals(thirdEXFMTStack, savedStacks[2])
        assertEquals(Stack(), StatementCounter)
    }

    @Test
    fun executeSTKR01FromStart() {
        val expected = listOf("A:1", "B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STKR01".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR01FromHalf() {
        val expected = listOf("B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(3), 0)
        assertEquals(expected = expected, actual = "video/STKR01".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR02FromStart() {
        val expected = listOf("A:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STKR02".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR02FromHalf() {
        val expected = listOf("")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(2), 0)
        assertEquals(expected = expected, actual = "video/STKR02".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR03FromStart() {
        val expected = listOf("A:10", "B:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STKR03".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR03FromHalf() {
        val expected = listOf("B:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(3), 0)
        assertEquals(expected = expected, actual = "video/STKR03".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR04FromStart() {
        val expected = listOf("A:1", "B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STKR04".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR04FromHalf() {
        val expected = listOf("A:2", "B:2")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(0, 2, 0), 0)
        assertEquals(expected = expected, actual = "video/STKR04".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR05FromStart() {
        val expected = listOf("A:1", "B:1")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STKR05".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR05FromHalf() {
        val expected = listOf("A:10", "B:10")
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(0, 2, 2), 0)
        assertEquals(expected = expected, actual = "video/STKR05".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR06FromStart() {
        val expected = listOf("A:1", "B:1")
        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(emptyList(), -1)
        assertEquals(expected = expected, actual = "video/STKR06".outputOf(configuration = configuration))
    }

    @Test
    fun executeSTKR06FromEXFMT() {
        val expected = listOf("A:3", "B:3")
        configuration.jarikoCallback.onExfmt = { _, runtimeInterpreterSnapshot ->
            val map = mutableMapOf<String, Value>()
            map["A"] = IntValue(3)
            map["B"] = IntValue(3)
            OnExfmtResponse(runtimeInterpreterSnapshot, map)
        }
        StatementCounter.prepareForRestore()
        StatementCounter.forceSet(listOf(0, 1, 1), 0)
        assertEquals(expected = expected, actual = "video/STKR06".outputOf(configuration = configuration))
    }

    @AfterTest
    fun clean() {
        StatementCounter.reset()
    }
}