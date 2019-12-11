package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.parsing.ast.failed
import kotlin.test.assertEquals
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.Ignore
import kotlin.test.fail

class MUTEExamplesTest {

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_01_perf_calls() {
        assertMuteOK("performance/MUTE10_01", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04_perf_strings() {
        assertMuteOK("performance/MUTE10_04", withOutput = listOf("Loop 1", "Loop 2", "Loop 3", "Loop 4", "Loop 5", "Loop 6"))
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04A_perf_strings() {
        assertMuteOK("performance/MUTE10_04A", withOutput = listOf("001.1_d01.1_A01.1_c01.1_B01.1_b01.1_C01.1_901.1_101."))
    }

    // We need to implement SORTA
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_02() {
        assertMuteOK("performance/MUTE10_02", withOutput = emptyList())
    }

    // We need to implement SORTA
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_03() {
        assertMuteOK("performance/MUTE10_03", withOutput = emptyList())
    }

    // Problem at line 58: MOVEL with arrays
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_05A() {
        assertMuteOK("performance/MUTE10_05A", withOutput = emptyList())
    }

    // Problem at line 58: MOVEL with arrays
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_05B() {
        assertMuteOK("performance/MUTE10_05B", withOutput = emptyList())
    }

    // Problem at line 58: MOVEL with arrays
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_05C() {
        assertMuteOK("performance/MUTE10_05C", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_06A() {
        assertMuteOK("performance/MUTE10_06A")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_06B() {
        assertMuteOK("performance/MUTE10_06B")
    }

    // An operation is not implemented: *ZERO
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_07A_Zoned() {
        assertMuteOK("performance/MUTE10_07A", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_07B_Packed() {
        assertMuteOK("performance/MUTE10_07B", withOutput = emptyList())
    }

    private fun siWithProgramFinderInPerformanceFolder(): ExtendedCollectorSystemInterface {
        val si = ExtendedCollectorSystemInterface()
        si.programFinders.add(DummyProgramFinder("/performance/"))
        return si
    }

    private fun assertMuteOK(programName: String, withOutput: List<String>? = null) {
        val si = siWithProgramFinderInPerformanceFolder()
        execute(programName, mapOf(), si)
        si.assertMutesSucceded()
        withOutput?.let {
            assertEquals(it, si.displayed)
        }
    }
}

fun CollectorSystemInterface.assertMutesSucceded() {
    val failedMutes = this.executedAnnotationInternal.failed()
    if (!failedMutes.isEmpty()) {
        fail("Mutes failed: $failedMutes")
    }
}
