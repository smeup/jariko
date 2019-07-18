package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.parsetreetoast.resolve
import org.junit.Ignore
import org.junit.Test
import org.junit.experimental.categories.Category
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class MUTEExamplesTest {

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_01_perf_calls() {
        assertEquals(LinkedList<String>(), outputOf("MUTE10_01"))
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04_perf_strings() {
        assertEquals(listOf<String>("Loop 1", "Loop 2", "Loop 3", "Loop 4", "Loop 5", "Loop 6"), outputOf("MUTE10_04"))
    }

}
