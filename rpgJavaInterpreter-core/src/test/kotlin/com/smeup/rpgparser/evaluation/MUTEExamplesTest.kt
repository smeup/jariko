package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import kotlin.test.assertEquals
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.Ignore
import kotlin.test.assertTrue

class MUTEExamplesTest {

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_01_perf_calls() {
        val si = siWithProgramFinderInPerformanceFolder()
        execute("performance/MUTE10_01", mapOf(), si)
        assertTrue(si.displayed.isEmpty())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04_perf_strings() {
        assertEquals(listOf("Loop 1", "Loop 2", "Loop 3", "Loop 4", "Loop 5", "Loop 6"), outputOf("performance/MUTE10_04"))
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04A_perf_strings() {
        assertEquals(listOf("001.1_d01.1_A01.1_c01.1_B01.1_b01.1_C01.1_901.1_101."), outputOf("performance/MUTE10_04A"))
    }

    // We need to implement SORTA
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_02() {
        assertEquals(emptyList(), outputOf("performance/MUTE10_02"))
    }

    // We need to implement SORTA
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_03() {
        assertEquals(emptyList(), outputOf("performance/MUTE10_03"))
    }

    // Problem at line 58: MOVEL with arrays
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_05A() {
        assertEquals(emptyList(), outputOf("performance/MUTE10_05A", si = siWithProgramFinderInPerformanceFolder()))
    }

    // Problem at line 58: MOVEL with arrays
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_05B() {
        assertEquals(emptyList(), outputOf("performance/MUTE10_05B", si = siWithProgramFinderInPerformanceFolder()))
    }

    // Problem at line 58: MOVEL with arrays
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_05C() {
        assertEquals(emptyList(), outputOf("performance/MUTE10_05C", si = siWithProgramFinderInPerformanceFolder()))
    }

    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_06A() {
        val result = outputOf("performance/MUTE10_06A", si = siWithProgramFinderInPerformanceFolder())
        assertTrue(result[0].startsWith("Time spent"))
    }

    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_06B() {
        val result = outputOf("performance/MUTE10_06B", si = siWithProgramFinderInPerformanceFolder())
        assertTrue(result[0].startsWith("Time spent"))
    }

    // An operation is not implemented: *ZERO
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_07A_Zoned() {
        assertEquals(emptyList(), outputOf("performance/MUTE10_07A"))
    }

    // Line 206: *LOVAL
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_07B_Packed() {
        assertEquals(emptyList(), outputOf("performance/MUTE10_07B"))
    }

    private fun siWithProgramFinderInPerformanceFolder(): ExtendedCollectorSystemInterface {
        val si = ExtendedCollectorSystemInterface()
        si.programFinders.add(DummyProgramFinder("/performance/"))
        return si
    }
}
