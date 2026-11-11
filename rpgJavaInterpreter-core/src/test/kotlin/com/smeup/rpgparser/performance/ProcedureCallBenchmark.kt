package com.smeup.rpgparser.performance

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test
import org.junit.experimental.categories.Category
import java.io.File
import java.lang.management.ManagementFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Benchmark of the interpreter cost of RPG procedure calls, without database and without a Micro Executor.
 *
 * `performance/PERFPROC01` is modeled on a real test-data generator: for every record it makes about 500 calls to small
 * procedures (local and STATIC variables, packed arithmetic, MOVE/Z-ADD/MULT/DIV, a DOWGT loop, nested calls) with
 * values picked from compile-time arrays; 38,700 calls per execution. The program is
 * parsed once (the [JavaSystemInterface] caches it) and executed `jrkBenchWarmups` + `jrkBenchRuns` times on this
 * thread; every measured execution reports the CPU time and the bytes allocated by this thread, so the numbers
 * are not affected by other processes or by the GC threads.
 *
 * The checksum printed by the program must stay the same: an optimization must not change the results. It was
 * verified on IBM i 7.5, where one execution of the program takes about 40 ms (the reference for the interpreter).
 *
 * Run it with `./gradlew :rpgJavaInterpreter-core:testPerformance --tests "*ProcedureCallBenchmark*"`;
 * the summary is written to `build/reports/benchmark/procedure-calls.txt`.
 *
 * Options (system properties of the test JVM: Gradle does not forward them, they are meant for a JVM launched
 * directly), used to compare two jariko builds running at the same time in two JVMs, so that the machine noise hits
 * both equally:
 * - `jrkBenchProgram`: another program to execute (it must DSPLY the same lines at every execution); its output is
 *   written to `<jrkBenchCsv>.output` so that two builds can be checked to compute the same results;
 * - `jrkBenchReady` / `jrkBenchGo`: after the warm-up, create the ready file and wait for the go file, which contains
 *   the epoch ms at which the measure starts (both JVMs measure the same time window);
 * - `jrkBenchSeconds`: measure for this many seconds instead of `jrkBenchRuns` executions (both JVMs stay busy for the
 *   whole window, so neither runs alone at the end);
 * - `jrkBenchCsv`: file receiving one line per measured execution (start epoch ms, CPU ms, allocated MB, wall ms).
 */
class ProcedureCallBenchmark : AbstractTest() {
    @Test
    @Category(PerformanceTest::class)
    fun procedureCalls() {
        val program = System.getProperty("jrkBenchProgram", PROGRAM)
        val warmups = System.getProperty("jrkBenchWarmups", "5").toInt()
        val runs = System.getProperty("jrkBenchRuns", "10").toInt()
        val seconds = System.getProperty("jrkBenchSeconds", "0").toInt()
        val csv = System.getProperty("jrkBenchCsv")?.let { File(it) }
        val systemInterface = JavaSystemInterface()
        val threadMx = ManagementFactory.getThreadMXBean() as com.sun.management.ThreadMXBean

        repeat(warmups) { executePgm(program, systemInterface = systemInterface) }
        waitForCommonStart()
        val cpuMillis = mutableListOf<Double>()
        val allocatedMegabytes = mutableListOf<Double>()
        val rows = StringBuilder("start_epoch_ms,cpu_ms,allocated_mb,wall_ms\n")
        val windowEnd = System.nanoTime() + seconds * 1_000_000_000L
        while (if (seconds > 0) System.nanoTime() < windowEnd else cpuMillis.size < runs) {
            val startEpoch = System.currentTimeMillis()
            val wallStart = System.nanoTime()
            val cpuStart = threadMx.currentThreadCpuTime
            val allocatedStart = threadMx.currentThreadAllocatedBytes
            executePgm(program, systemInterface = systemInterface)
            cpuMillis += (threadMx.currentThreadCpuTime - cpuStart) / 1_000_000.0
            allocatedMegabytes += (threadMx.currentThreadAllocatedBytes - allocatedStart) / 1_000_000.0
            rows.append("$startEpoch,${cpuMillis.last()},${allocatedMegabytes.last()},${(System.nanoTime() - wallStart) / 1_000_000.0}\n")
        }
        csv?.writeText(rows.toString())

        val outputs = systemInterface.consoleOutput
        val outputPerExecution = outputs.size / (warmups + cpuMillis.size)
        assertTrue(outputPerExecution > 0, "the program must DSPLY something, to check that the results do not change")
        assertEquals(warmups + cpuMillis.size, outputs.size / outputPerExecution, "same output lines at every execution")
        val firstExecutionOutput = outputs.take(outputPerExecution)
        outputs.chunked(outputPerExecution).forEach { assertEquals(firstExecutionOutput, it, "the output changed between executions") }
        csv?.let { File("${it.path}.output").writeText(firstExecutionOutput.joinToString("\n")) }
        if (program == PROGRAM) assertEquals(listOf(EXPECTED_CHECKSUM), firstExecutionOutput, "the checksum of the program changed")

        val summary =
            buildString {
                val calls = if (program == PROGRAM) ", $CALLS_PER_EXECUTION procedure calls per execution" else ""
                appendLine("ProcedureCallBenchmark: $program$calls")
                appendLine("warm-up executions: $warmups, measured executions: ${cpuMillis.size}")
                val cpuRange = "min ${cpuMillis.min().fmt()}  max ${cpuMillis.max().fmt()}"
                appendLine("CPU ms per execution:  median ${cpuMillis.median().fmt()}  $cpuRange")
                appendLine("MB allocated per execution: median ${allocatedMegabytes.median().fmt()}")
                if (program == PROGRAM) {
                    appendLine("CPU us per call:       median ${(cpuMillis.median() * 1000 / CALLS_PER_EXECUTION).fmt()}")
                    val bytesPerCall = allocatedMegabytes.median() * 1_000_000 / CALLS_PER_EXECUTION
                    appendLine("bytes allocated per call:   median ${bytesPerCall.fmt()}")
                }
                appendLine("all CPU ms: ${cpuMillis.joinToString(" ") { it.fmt() }}")
            }
        println(summary)
        File("build/reports/benchmark").apply { mkdirs() }.resolve("procedure-calls.txt").writeText(summary)
    }

    /**
     * With `jrkBenchReady`/`jrkBenchGo`: tell the driver that the warm-up is over, then wait for the start instant
     * it writes in the go file. Without them, start at once.
     */
    private fun waitForCommonStart() {
        val ready = System.getProperty("jrkBenchReady")?.let { File(it) } ?: return
        val go = File(System.getProperty("jrkBenchGo") ?: error("jrkBenchGo is required with jrkBenchReady"))
        ready.writeText(System.currentTimeMillis().toString())
        val deadline = System.currentTimeMillis() + 10 * 60 * 1000
        while (!go.exists()) {
            check(System.currentTimeMillis() < deadline) { "no go file after 10 minutes: ${go.path}" }
            Thread.sleep(10)
        }
        val startAt = go.readText().trim().toLong()
        (startAt - System.currentTimeMillis()).takeIf { it > 0 }?.let { Thread.sleep(it) }
    }

    private fun List<Double>.median(): Double = sorted().let { (it[(it.size - 1) / 2] + it[it.size / 2]) / 2 }

    private fun Double.fmt() = "%.1f".format(this)

    private companion object {
        const val PROGRAM = "performance/PERFPROC01"

        // 75 records x 516 calls: 21 for the record fields + 99 values x (RandDec, RandNext and 3 P_RND)
        const val CALLS_PER_EXECUTION = 75 * 516

        // Same value printed by the program compiled and run on IBM i 7.5 (CRTBNDRPG): an optimization must not change it.
        const val EXPECTED_CHECKSUM = "-3272339.834212"
    }
}
