package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.getProgram
import org.junit.Test
import kotlin.test.assertEquals

class MemoryStorage : IMemorySliceStorage {

    val storage = mutableMapOf<MemorySliceId, Map<String, Value>>()

    /**
     * Open the storage
     * */
    override fun open() {
    }

    /**
     * Load memory associated to memorySliceId
     * */
    override fun load(memorySliceId: MemorySliceId): Map<String, Value> {
        return storage.getOrDefault(memorySliceId, mutableMapOf<String, Value>())
    }

    /**
     * Notify transaction start. Is called before all memory slices storing
     * */
    override fun beginTrans() {
    }

    /**
     * Store map associated to memory slices
     * */
    override fun store(memorySliceId: MemorySliceId, values: Map<String, Value>) {
        storage[memorySliceId] = values
    }

    /**
     * Called if all memory slices storing process is succesfully completed
     * */
    override fun commitTrans() {
    }

    /**
     * Called in case of failure
     * */
    override fun rollbackTrans() {
    }

    /**
     * Close the storage. If do not has been called commitTrans, it could be needed to implement rollback mechanisms
     * */
    override fun close() {
    }
}

class SymbolTableStoragingTest {

    @Test
    fun execPgmAndEvaluateStorage() {
        val myProgram = """
     H ACTGRP('MyAct')
     D X               S              1  0
     D Msg             S             12
     C                   EVAL      X = X + 1
     C                   EVAL      Msg = %CHAR(X)
     C     msg           dsply
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        // verify if serialization is ok
        // get variables for memorysliceid
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
            expected = IntValue(1),
            actual = variables["X"] ?: error("Not found X")
        )
    }

    @Test
    fun initPgmByStorageAndEvaluateResult() {
        val myProgram = """
     H ACTGRP('MyAct')
     D X               S              2
     D Y               S              1
     D Msg             S             12
     C     *ENTRY        PLIST
     C                   PARM                    X
     C                   EVAL      X = Y + 'A'
     C                   EVAL      Msg = %CHAR(X)
     C     msg           dsply
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memorySliceId = MemorySliceId("MyAct".toUpperCase(), programName = myProgram)
        val memoryStorage = MemoryStorage()
        memoryStorage.storage[memorySliceId] = mutableMapOf("Y" to StringValue(value = "A", varying = true))
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        // pass PLIST  in order to achieve in output the current X value and to evaluate it at the end
        val result = commandLineProgram.singleCall(listOf("B"), configuration)
        require(result != null)
        assertEquals("AA", result.parmsList[0])
    }
}
