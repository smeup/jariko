package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.adaptForTestCase
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.PARSING_LOGGER
import com.smeup.rpgparser.logging.PERFORMANCE_LOGGER
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import org.junit.Test
import org.junit.experimental.categories.Category
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.Callable
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
        return storage.getOrDefault(memorySliceId, mutableMapOf())
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

open class SymbolTableStoragingTest : AbstractTest() {

    @Test
    fun execPgmAndEvaluateStorage() {
        execPgmAndEvaluateStorage("1")
    }

    fun execPgmAndEvaluateStorage(varValue: String) {
        // Program RT-closing: test to check SymbolTable preserves variables values
        val myProgram = """
     H ACTGRP('MyAct')
     D X               S             50  
     D Msg             S             12
     C                   EVAL      X = '$varValue'
     C                   EVAL      Msg = X
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
            expected = varValue,
            actual = (variables.get("X") as StringValue).value.trim() ?: error("Not found X")
        )
    }

    @Test
    fun initPgmByStorageAndEvaluateResult() {
        initPgmByStorageAndEvaluateResult("A")
    }

    fun initPgmByStorageAndEvaluateResult(varValue: String) {
        val myProgram = """
     H ACTGRP('MyAct')
     D X               S             50
     D Y               S              1
     D Msg             S             12
     C     *ENTRY        PLIST
     C                   PARM                    X
     C                   EVAL      X = Y + '$varValue'
     C                   EVAL      Msg = X
     C     msg           dsply
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memorySliceId = MemorySliceId("MyAct".toUpperCase(), programName = myProgram)
        val memoryStorage = MemoryStorage()
        memoryStorage.storage[memorySliceId] = mutableMapOf("Y" to StringValue(value = "$varValue", varying = true))
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        // pass PLIST  in order to achieve in output the current X value and to evaluate it at the end
        val result = commandLineProgram.singleCall(listOf("B"), configuration)
        require(result != null)
        assertEquals("$varValue$varValue", result.parmsList[0].trim())
    }

    @Test
    fun execLRPgmAndEvaluateStorage() {
        execLRPgmAndEvaluateStorage("1")
    }

    fun execLRPgmAndEvaluateStorage(varValue: String) {
        // Program LR-closing: test to check SymbolTable does not preserve variables values
        val myProgram = """
     H ACTGRP('MyAct')
     D X               S             50  
     D Msg             S             12
     C                   EVAL      X = X + '$varValue'
     C                   EVAL      Msg = X
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
        execRTPgmTwiceAndPreserveValues("ABC")
    }

    fun execRTPgmTwiceAndPreserveValues(varValue: String) {
        // Program RT-closing: test to check SymbolTable preserve variables values
        val myProgram = """
     H ACTGRP('MyAct')
     D X               S             50  
     D Msg             S             12
     C                   EVAL      X = %TRIM(X) + '$varValue'
     C                   EVAL      Msg = X
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
            expected = varValue.trim(),
            actual = (variables.get("X") as StringValue).value.trim() ?: error("Not found X")
        )

        // Second program execution, X variable must exist with value 1, than it will increase to value 2.
        commandLineProgram.singleCall(emptyList(), configuration)
        variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
            expected = varValue.trim() + varValue.trim(),
            actual = (variables.get("X") as StringValue).value.trim() ?: error("Not found X")
        )
    }

    @Test
    fun initPreExistingVariablesPgmByStorageAndEvaluateResult() {
        initPreExistingVariablesPgmByStorageAndEvaluateResult("B")
    }

    fun initPreExistingVariablesPgmByStorageAndEvaluateResult(varValue: String) {
        // Program RT-closing: test to check per existing variable will be re-assigned
        val myProgram = """
     H ACTGRP('MyAct')
     D Z               S             50
     D Msg             S             12
     C                   EVAL      Z = '$varValue'
     C                   EVAL      Msg = Z
     C     msg           dsply
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memorySliceId = MemorySliceId("MyAct".toUpperCase(), programName = myProgram)
        val memoryStorage = MemoryStorage()
        memoryStorage.storage[memorySliceId] = mutableMapOf("Z" to StringValue(value = "$varValue", varying = true))
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
            expected = varValue,
            actual = (variables.get("Z") as StringValue).value.trim() ?: error("Not found Z")
        )
    }

    @Test
    fun sameVariablesButDifferentACTGRP() {
        sameVariablesButDifferentACTGRP("xyz")
    }

    fun sameVariablesButDifferentACTGRP(varValue: String) {
        // Program A called, program B called, both with 'SETON RT', both have same var Z but with different two values.
        // Memory must have separated scopes cause two programs have two different ACTGRP (MyActA and MyActB).
        val myProgramA = """
     H ACTGRP('MyActA')
     D Z               S             50
     D Msg             S             12
     C                   EVAL      Z = '$varValue'+'A'
     C                   EVAL      Msg = Z
     C     msg           dsply
     C                   SETON                                          RT
     """
        val myProgramB = """
     H ACTGRP('MyActB')
     D Z               S             50
     D Msg             S             12
     C                   EVAL      Z = '$varValue'+'B'
     C                   EVAL      Msg = Z
     C     msg           dsply
     C                   SETON                                          RT
     """

        val commandLineProgramA = getProgram(nameOrSource = myProgramA)
        val commandLineProgramB = getProgram(nameOrSource = myProgramB)

        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)

        // First call to program A, instatiate and initialize Z variable to value ending with 'A' character.
        commandLineProgramA.singleCall(emptyList(), configuration)
        var variables = memoryStorage.storage[MemorySliceId("MyActA".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
            expected = "${varValue}A",
            actual = (variables.get("Z") as StringValue).value.trim() ?: error("Not found Z")
        )

        // First call to program B, instatiate and initialize Z variable to value ending with 'B' character.
        commandLineProgramB.singleCall(emptyList(), configuration)
        variables = memoryStorage.storage[MemorySliceId("MyActB".toUpperCase(), programName = myProgramB)]
        require(variables != null)
        assertEquals(
            expected = "${varValue}B",
            actual = (variables.get("Z") as StringValue).value.trim() ?: error("Not found Z")
        )

        // Z variable on MyActA must have previous value ending with 'A' character.
        variables = memoryStorage.storage[MemorySliceId("MyActA".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
            expected = "${varValue}A",
            actual = (variables.get("Z") as StringValue).value.trim() ?: error("Not found Z")
        )
    }

    @Test
    fun sameVariablesAndSameACTGRP() {
        sameVariablesAndSameACTGRP("zyx")
    }

    fun sameVariablesAndSameACTGRP(varValue: String) {
        // Program A called, program B called, both with 'SETON RT', both have same var 'Z' but with different values.
        // Memory have separated scopes cause two programs with same ACTGRP (MyActSAME and MyActSAME) have different names.
        val myProgramA = """
     H ACTGRP('MyActSAME')
     D Z               S             50
     D Msg             S             12
     C                   EVAL      Z = '$varValue'+'A'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """
        val myProgramB = """
     H ACTGRP('MyActSAME')
     D Z               S             50
     D Msg             S             12
     C                   EVAL      Z = '$varValue'+'B'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """

        val commandLineProgramA = getProgram(nameOrSource = myProgramA)
        val commandLineProgramB = getProgram(nameOrSource = myProgramB)

        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)

        // First call to program A, instatiate and initialize Z variable to value ending with 'A' character.
        commandLineProgramA.singleCall(emptyList(), configuration)
        var variables = memoryStorage.storage[MemorySliceId("MyActSAME".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
            expected = "${varValue}A",
            actual = (variables.get("Z") as StringValue).value.trim() ?: error("Not found Z")
        )

        // First call to program B, instatiate and initialize Z variable to value ending with 'B' character.
        commandLineProgramB.singleCall(emptyList(), configuration)
        variables = memoryStorage.storage[MemorySliceId("MyActSAME".toUpperCase(), programName = myProgramB)]
        require(variables != null)
        assertEquals(
            expected = "${varValue}B",
            actual = (variables.get("Z") as StringValue).value.trim() ?: error("Not found Z")
        )

        // Variable 'Z myProgramA scoped' have not changed his initial values, no interference between two programs
        variables = memoryStorage.storage[MemorySliceId("MyActSAME".toUpperCase(), programName = myProgramA)]
        require(variables != null)
        assertEquals(
            expected = "${varValue}A",
            actual = (variables.get("Z") as StringValue).value.trim() ?: error("Not found Z")
        )
    }

    @Test
    @Ignore
    fun execRTPgmTwiceAndNotPreserveValues() {
        // TODO Handle ACTGRP(*NEW)
        // Program RT-closing: test to check SymbolTable does NOT preserve variables values due to *NEW activation group
//        """
//     H ACTGRP(*NEW)
//     D X               S              1  0
//     D Msg             S             12
//     C                   EVAL      X = X + 1
//     C                   EVAL      Msg = %CHAR(X)
//     C     msg           dsply
//     C                   SETON                                          RT
//     """
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

        val testNames = listOf(
            "execPgmAndEvaluateStorage",
            "initPgmByStorageAndEvaluateResult",
            "execLRPgmAndEvaluateStorage",
            "execRTPgmTwiceAndPreserveValues",
            "initPreExistingVariablesPgmByStorageAndEvaluateResult",
            "sameVariablesButDifferentACTGRP",
            "sameVariablesAndSameACTGRP"
        )

        val fixedThreadPool = 10
        val repeatTests = 100
        val timeOutExecutorTermination = 60L

        val executor = Executors.newFixedThreadPool(fixedThreadPool)
        val callables = mutableListOf<Callable<Throwable?>>()
        repeat(repeatTests) {
            for (testName in testNames) {
                val simbolTableStoragingTest = SymbolTableStoragingTest()
                callables.add(WorkerThread(simbolTableStoragingTest, testName))
            }
        }
        val futures = executor.invokeAll(callables)
        executor.shutdown()
        println("Waiting 60s. for all thread to finish...")
        executor.awaitTermination(timeOutExecutorTermination, TimeUnit.SECONDS)
        println("...done")
        if (futures.any { it.get() != null }) {
            throw (futures.first { it.get() != null }.get())!!
        }
    }

    @Test
    fun testExecPgmLoadedFromSource() {
        val executor = Executors.newFixedThreadPool(10)
        var failed: Throwable? = null
        mutableListOf<Runnable>()
        repeat(100) {
            executor.execute {
                runCatching {
                    var varValue = Thread.currentThread().name
                    println("Run interpreter: $varValue")
                    val systemInterface: SystemInterface = JavaSystemInterface()
                    (systemInterface as JavaSystemInterface).addJavaInteropPackage("com.smeup.api")
                    val rpgDir = File("src/test/resources/")
                    val programFinders: List<RpgProgramFinder> = listOf(DirRpgProgramFinder(rpgDir))
                    val jariko = getProgram("XTHREAD.rpgle", systemInterface, programFinders)
                    jariko.singleCall(listOf("FUNZ", "METO", "$varValue"), Configuration().adaptForTestCase(this))
                    println(it)
                }.onFailure {
                    println(it)
                    failed = it
                }
            }
        }
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
        assert(failed == null)
    }

    // Performance test related to symbol table initialization with huge number of variables
    @Test
    @Category(PerformanceTest::class)
    open fun initWithHugeDefinedVariables() {
        val programFinders = listOf(DirRpgProgramFinder(File(javaClass.getResource("/performance-ast").path)))
        val si = JavaSystemInterface().also {
            it.loggingConfiguration = consoleLoggingConfiguration(PARSING_LOGGER, PERFORMANCE_LOGGER)
        }
        val configuration = Configuration(memorySliceStorage = IMemorySliceStorage.createMemoryStorage(mutableMapOf())).adaptForTestCase(this)
        val caller = getProgram("MUTE10_69", systemInterface = si, programFinders = programFinders)
        caller.singleCall(emptyList(), configuration)
    }

    // Performance test related to symbol table initialization with huge number of variables followed by evaluation of them
    @Test
    @Category(PerformanceTest::class)
    open fun initAndEvalWithHugeDefinedVariables() {
        val programFinders = listOf(DirRpgProgramFinder(File(javaClass.getResource("/performance-ast").path)))
        val si = JavaSystemInterface().also {
            it.loggingConfiguration = consoleLoggingConfiguration(PARSING_LOGGER, PERFORMANCE_LOGGER)
        }
        val configuration = Configuration(memorySliceStorage = IMemorySliceStorage.createMemoryStorage(mutableMapOf())).adaptForTestCase(this)
        val caller = getProgram("MUTE10_70", systemInterface = si, programFinders = programFinders)
        caller.singleCall(emptyList(), configuration)
    }
}

class WorkerThread(private val symbolTableStoragingTest: SymbolTableStoragingTest, private val testName: String) :
    Callable<Throwable?> {

    override fun call(): Throwable? {
        kotlin.runCatching {
            println(Thread.currentThread().name + " Start test $testName " + DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
            var varValue = Thread.currentThread().name
            when (testName) {
                "execPgmAndEvaluateStorage" -> symbolTableStoragingTest.execPgmAndEvaluateStorage(varValue)
                "initPgmByStorageAndEvaluateResult" -> symbolTableStoragingTest.initPgmByStorageAndEvaluateResult(varValue)
                "execLRPgmAndEvaluateStorage" -> symbolTableStoragingTest.execLRPgmAndEvaluateStorage(varValue)
                "execRTPgmTwiceAndPreserveValues" -> symbolTableStoragingTest.execRTPgmTwiceAndPreserveValues(varValue)
                "initPreExistingVariablesPgmByStorageAndEvaluateResult" -> symbolTableStoragingTest.initPreExistingVariablesPgmByStorageAndEvaluateResult(varValue)
                "sameVariablesButDifferentACTGRP" -> symbolTableStoragingTest.sameVariablesButDifferentACTGRP(varValue)
                "sameVariablesAndSameACTGRP" -> symbolTableStoragingTest.sameVariablesAndSameACTGRP(varValue)
                else -> {
                    error("Test $testName not exists")
                }
            }
            println(Thread.currentThread().name + " End test $testName " + DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
        }.onFailure {
            println(it)
            return it
        }.onSuccess {
            return null
        }
        return Throwable("Why am i here?")
    }
}