package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.getProgram
import org.junit.Test
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Ignore
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
        // Program RT-closing: test to check SymbolTable preserves variables values
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

    @Test
    fun execLRPgmAndEvaluateStorage() {
        // Program LR-closing: test to check SymbolTable does not preserve variables values
        val myProgram = """
     H ACTGRP('MyAct')
     D X               S              1  0
     D Msg             S             12
     C                   EVAL      X = X + 1
     C                   EVAL      Msg = %CHAR(X)
     C     msg           dsply
     C                   SETON                                          LR
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        assertEquals(
                expected = null,
                actual = variables,
                "Expected no variables due to 'SETON LR' instruction."
        )
    }

    @Test
    fun execRTPgmTwiceAndPreserveValues() {
        // Program RT-closing: test to check SymbolTable preserve variables values
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
        // First call, instatiate and initialize X variable to value 1
        commandLineProgram.singleCall(emptyList(), configuration)
        var variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
                expected = IntValue(1),
                actual = variables["X"] ?: error("Not found X")
        )

        // Second program execution, X variable must exist with value 1, than it will increase to value 2.
        commandLineProgram.singleCall(emptyList(), configuration)
        variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
                expected = IntValue(2),
                actual = variables["X"] ?: error("Not found X")
        )
    }

    @Test
    fun initPreExistingVariablesPgmByStorageAndEvaluateResult() {
        // Program RT-closing: test to check per existing variable will be re-assigned
        val myProgram = """
     H ACTGRP('MyAct')
     D Z               S              1
     D Msg             S             12
     C                   EVAL      Z = 'B'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memorySliceId = MemorySliceId("MyAct".toUpperCase(), programName = myProgram)
        val memoryStorage = MemoryStorage()
        memoryStorage.storage[memorySliceId] = mutableMapOf("Z" to StringValue(value = "A", varying = true))
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
                expected = StringValue("B"),
                actual = variables["Z"] ?: error("Not found Z")
        )
    }

    @Test
    fun sameVariablesButDifferentACTGRP() {
        // Program A called, program B called, both with 'SETON RT', both have same var Z but with different two values.
        // Memory must have separated scopes cause two programs have two different ACTGRP (MyActA and MyActB).
        val myProgramA = """
     H ACTGRP('MyActA')
     D Z               S              1
     D Msg             S             12
     C                   EVAL      Z = 'A'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """
        val myProgramB = """
     H ACTGRP('MyActB')
     D Z               S              1
     D Msg             S             12
     C                   EVAL      Z = 'B'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """

        val commandLineProgramA = getProgram(nameOrSource = myProgramA)
        val commandLineProgramB = getProgram(nameOrSource = myProgramB)

        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)

        // First call to program A, instatiate and initialize Z variable to value 'A'
        commandLineProgramA.singleCall(emptyList(), configuration)
        var variables = memoryStorage.storage[MemorySliceId("MyActA".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
                expected = StringValue("A"),
                actual = variables["Z"] ?: error("Not found Z")
        )

        // First call to program B, instatiate and initialize Z variable to value 'B'.
        commandLineProgramB.singleCall(emptyList(), configuration)
        variables = memoryStorage.storage[MemorySliceId("MyActB".toUpperCase(), programName = myProgramB)]
        require(variables != null)
        assertEquals(
                expected = StringValue("B"),
                actual = variables["Z"] ?: error("Not found Z")
        )

        // Z variable on MyActA must have previous value of A
        variables = memoryStorage.storage[MemorySliceId("MyActA".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
                expected = StringValue("A"),
                actual = variables["Z"] ?: error("Not found Z")
        )
    }

    @Test
    fun sameVariablesAndSameACTGRP() {
        // Program A called, program B called, both with 'SETON RT', both have same var 'Z' but with different values.
        // Memory have separated scopes cause two programs with same ACTGRP (MyActSAME and MyActSAME) have different names.
        val myProgramA = """
     H ACTGRP('MyActSAME')
     D Z               S              1
     D Msg             S             12
     C                   EVAL      Z = 'A'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """
        val myProgramB = """
     H ACTGRP('MyActSAME')
     D Z               S              1
     D Msg             S             12
     C                   EVAL      Z = 'B'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """

        val commandLineProgramA = getProgram(nameOrSource = myProgramA)
        val commandLineProgramB = getProgram(nameOrSource = myProgramB)

        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)

        // First call to program A, instatiate and initialize Z variable to value 'A'
        commandLineProgramA.singleCall(emptyList(), configuration)
        var variables = memoryStorage.storage[MemorySliceId("MyActSAME".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
                expected = StringValue("A"),
                actual = variables["Z"] ?: error("Not found Z")
        )

        // First call to program B, instatiate and initialize Z variable to value 'B'
        commandLineProgramB.singleCall(emptyList(), configuration)
        variables = memoryStorage.storage[MemorySliceId("MyActSAME".toUpperCase(), programName = myProgramB)]
        require(variables != null)
        assertEquals(
                expected = StringValue("B"),
                actual = variables["Z"] ?: error("Not found Z")
        )

        // Variable 'Z myProgramA scoped' have not changed his initial values, no interference between trwo programs
        variables = memoryStorage.storage[MemorySliceId("MyActSAME".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
                expected = StringValue("A"),
                actual = variables["Z"] ?: error("Not found Z")
        )
    }

    @Test
    @Ignore
    fun execRTPgmTwiceAndNotPreserveValues() {
        // TODO Handle ACTGRP(*NEW)
        // Program RT-closing: test to check SymbolTable does NOT preserve variables values due to *NEW activation group
        val myProgram = """
     H ACTGRP(*NEW)
     D X               S              1  0
     D Msg             S             12
     C                   EVAL      X = X + 1
     C                   EVAL      Msg = %CHAR(X)
     C     msg           dsply
     C                   SETON                                          RT
     """
    }

    @Test
    @Ignore
    fun wrongNumericVariableSize() {
        // Variable 'NUM' is defined as '1' integer digit and '0' decimal digit.
        // Due to its definition, 'NUM' variable can store values from -9 to 9.
        // This test shows how numeric variable declaration doesn't work properly cause,
        // cause the 'NUM' variable can store 9999 value.
        val myProgram = """
     H ACTGRP('MyAct')
     D NUM             S              1  0
     D MSG             S             12
     C                   EVAL      NUM = 9999
     C                   EVAL      MSG = %CHAR(NUM) 
     C     MSG           DSPLY
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
                expected = IntValue(9),
                actual = variables["NUM"] ?: error("Not found NUM")
        )
    }

    @Test
    fun multiThreadTest() {
        // Run all SymbolTableStoragingTest.kt tests in multithread mode

        val testNames = listOf("execPgmAndEvaluateStorage", "initPgmByStorageAndEvaluateResult",
                "execLRPgmAndEvaluateStorage", "execRTPgmTwiceAndPreserveValues", "initPreExistingVariablesPgmByStorageAndEvaluateResult",
                "sameVariablesButDifferentACTGRP", "sameVariablesAndSameACTGRP")

        val fixedThreadPool = 10
        val repeatTests = 100
        val timeOutExecutorTermination = 60L

        val executor = Executors.newFixedThreadPool(fixedThreadPool)
        repeat(repeatTests) {
            for (testName in testNames) {
                var simbolTableStoragingTest = SymbolTableStoragingTest()
                var workerThread: Runnable = WorkerThread(simbolTableStoragingTest, testName)
                executor.execute(workerThread)
            }
        }
        executor.shutdown()

        println("Waiting 60s. for all thread to finish...")
        executor.awaitTermination(timeOutExecutorTermination, TimeUnit.SECONDS)
        println("...done")
    }
}

class WorkerThread(var symbolTableStoragingTest: SymbolTableStoragingTest, var testName: String) : Runnable {

    override fun run() {
        println(Thread.currentThread().name + " Start test $testName " + DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
        when (testName) {
            "execPgmAndEvaluateStorage" -> symbolTableStoragingTest.execPgmAndEvaluateStorage()
            "initPgmByStorageAndEvaluateResult" -> symbolTableStoragingTest.initPgmByStorageAndEvaluateResult()
            "execLRPgmAndEvaluateStorage" -> symbolTableStoragingTest.execLRPgmAndEvaluateStorage()
            "execRTPgmTwiceAndPreserveValues" -> symbolTableStoragingTest.execRTPgmTwiceAndPreserveValues()
            "initPreExistingVariablesPgmByStorageAndEvaluateResult" -> symbolTableStoragingTest.initPreExistingVariablesPgmByStorageAndEvaluateResult()
            "sameVariablesButDifferentACTGRP" -> symbolTableStoragingTest.sameVariablesButDifferentACTGRP()
            "sameVariablesAndSameACTGRP" -> symbolTableStoragingTest.sameVariablesAndSameACTGRP()
            else -> {
                print("Test $testName not exists")
            }
        }
        println(Thread.currentThread().name + " End test $testName " + DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
    }
}