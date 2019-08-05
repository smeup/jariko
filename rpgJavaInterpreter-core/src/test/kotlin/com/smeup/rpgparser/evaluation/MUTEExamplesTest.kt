package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import java.util.*
import kotlin.test.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.experimental.categories.Category

class MUTEExamplesTest {

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_01_perf_calls() {
        assertEquals(LinkedList<String>(), outputOf("MUTE10_01"))
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04_perf_strings() {
        assertEquals(listOf("Loop 1", "Loop 2", "Loop 3", "Loop 4", "Loop 5", "Loop 6"), outputOf("MUTE10_04"))
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04_A_perf_strings() {
        assertEquals(listOf("001.1_d01.1_A01.1_c01.1_B01.1_b01.1_C01.1_901.1_101."), outputOf("MUTE10_04_A"))
    }

    // TODO understand why this test does not pass
    @Test @Ignore
    fun executeMUTE11_02() {
        // TODO
        assertEquals(listOf("TODO"), outputOf("MUTE11_02"))
    }
}
