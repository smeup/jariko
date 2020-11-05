package com.smeup.rpgparser.interpreter

import org.junit.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object SimpleTableStoragingTestMultithread {

    @JvmStatic
    fun main(args: Array<String>) {

        val testNames = listOf("execPgmAndEvaluateStorage","initPgmByStorageAndEvaluateResult",
        "execLRPgmAndEvaluateStorage", "execRTPgmTwiceAndPreserveValues", "initPreExistingVariablesPgmByStorageAndEvaluateResult",
        "sameVariablesButDifferentACTGRP", "sameVariablesAndSameACTGRP")

        val fixedThreadPool = 100
        val repeatTests = 1000

        val executor = Executors.newFixedThreadPool(fixedThreadPool)
        repeat(repeatTests){
            for (testName in testNames) {
                var simbolTableStoragingTest = SymbolTableStoragingTest()
                val workerThread: Runnable = WorkerThread(simbolTableStoragingTest, testName)
                executor.execute(workerThread)
            }
        }
        executor.shutdown()

        println("Waiting for all thread to finish...")
        while (!executor.isTerminated) {
        }
        println("...done")
    }
}

class WorkerThread(var symbolTableStoragingTest: SymbolTableStoragingTest, var testName: String) : Runnable {

    override fun run() {
        println(Thread.currentThread().name + " Start test $testName")
        when (testName) {
            "execPgmAndEvaluateStorage" -> symbolTableStoragingTest.execPgmAndEvaluateStorage()
            "initPgmByStorageAndEvaluateResult" -> symbolTableStoragingTest.initPgmByStorageAndEvaluateResult()
            "execLRPgmAndEvaluateStorage" -> symbolTableStoragingTest.execLRPgmAndEvaluateStorage()
            "execRTPgmTwiceAndPreserveValues" -> symbolTableStoragingTest.execRTPgmTwiceAndPreserveValues()
            "initPreExistingVariablesPgmByStorageAndEvaluateResult" -> symbolTableStoragingTest.initPreExistingVariablesPgmByStorageAndEvaluateResult()
            "sameVariablesButDifferentACTGRP" -> symbolTableStoragingTest.sameVariablesButDifferentACTGRP()
            "sameVariablesAndSameACTGRP" -> symbolTableStoragingTest.sameVariablesAndSameACTGRP()
            else -> {
                print("Test $testName not exists" )
            }
        }
        println(Thread.currentThread().name + " End test $testName")
    }

}