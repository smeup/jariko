package com.smeup.rpgparser.evaluation

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.DummyProgramFinder
import com.smeup.rpgparser.ExtendedCollectorSystemInterface
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.mute.color
import com.smeup.rpgparser.mute.executeMuteAnnotations
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.fail

class MUTEExamplesTest {

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_01() {
        assertMuteOK("MUTE10_01", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04() {
        assertMuteOK("MUTE10_04", withOutput = listOf("Loop 1", "Loop 2", "Loop 3", "Loop 4", "Loop 5", "Loop 6"))
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_04A() {
        assertMuteOK("MUTE10_04A", withOutput = listOf("001.1_d01.1_A01.1_c01.1_B01.1_b01.1_C01.1_901.1_101."))
    }

    // We need to implement SORTA
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_02() {
        assertMuteOK("MUTE10_02", withOutput = emptyList())
    }

    // We need to implement SORTA
    @Test @Category(PerformanceTest::class) @Ignore
    fun executeMUTE10_03() {
        assertMuteOK("MUTE10_03", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_05A() {
        assertMuteOK("MUTE10_05A", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_05B() {
        assertMuteOK("MUTE10_05B", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_05C() {
        assertMuteOK("MUTE10_05C", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_06A() {
        assertMuteOK("MUTE10_06A")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_06B() {
        assertMuteOK("MUTE10_06B")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_07A() {
        assertMuteOK("MUTE10_07A", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_07B() {
        assertMuteOK("MUTE10_07B", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_08A() {
        assertMuteOK("MUTE10_08A", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_08B() {
        assertMuteOK("MUTE10_08B", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_08C() {
        assertMuteOK("MUTE10_08C", withOutput = emptyList())
    }

    private fun siWithProgramFinderInPerformanceFolder(): ExtendedCollectorSystemInterface {
        val si = ExtendedCollectorSystemInterface()
        si.programFinders.add(dummyProgramFinder())
        return si
    }

    private fun dummyProgramFinder() = DummyProgramFinder("/performance/")

    private fun assertMuteOK(programName: String, withOutput: List<String>? = null) {
        val si = siWithProgramFinderInPerformanceFolder()
        val rpgSourceInputStream = dummyProgramFinder().rpgSourceInputStream(programName)

        require(rpgSourceInputStream != null) {
            "$programName cannot be found"
        }
        executeMuteAnnotations(rpgSourceInputStream, si)

        si.assertMutesSucceed(programName)
        withOutput?.let {
            assertEquals(it, si.displayed)
        }
    }
}

fun CollectorSystemInterface.assertMutesSucceed(programName: String) {
    if (this.executedAnnotationInternal.size == 0) {
        println("WARNING: no MUTE assertion ran in $programName".yellow())
    }
    val errors = mutableListOf<String>()
    this.executedAnnotationInternal.forEach {
        val annotation = it.value
        if (annotation.failed()) {
            println("Some assertion failed in $programName".color(false))
            val message = "Mute annotation at line ${it.key} failed - ${annotation.headerDescription()}"
            println(message.color(false))
            errors.add(message)
        }
    }
    if (errors.size > 0) fail(errors.joinToString())
}
