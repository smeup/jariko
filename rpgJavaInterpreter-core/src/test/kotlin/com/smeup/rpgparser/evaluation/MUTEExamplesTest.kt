package com.smeup.rpgparser.evaluation

import Kute10_53
import Kute10_54
import Kute10_55
import Kute10_56
import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.DummyProgramFinder
import com.smeup.rpgparser.ExtendedCollectorSystemInterface
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.execution.RunnerCLI.programName
import com.smeup.rpgparser.mute.color
import com.smeup.rpgparser.mute.executeMuteAnnotations
import org.junit.Test
import org.junit.experimental.categories.Category
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

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_02() {
        assertMuteOK("MUTE10_02", withOutput = emptyList())
    }

    @Test @Category(PerformanceTest::class)
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

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_09() {
        assertMuteOK("MUTE10_09")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_10() {
        assertMuteOK("MUTE10_10")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_11() {
        assertMuteOK("MUTE10_11")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_12() {
        assertMuteOK("MUTE10_12")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_13() {
        assertMuteOK("MUTE10_13")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_14() {
        assertMuteOK("MUTE10_14")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_15() {
        assertMuteOK("MUTE10_15")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_16() {
        assertMuteOK("MUTE10_16")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_17() {
        assertMuteOK("MUTE10_17")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_18() {
        assertMuteOK("MUTE10_18")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_19() {
        assertMuteOK("MUTE10_19")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_20() {
        assertMuteOK("MUTE10_20")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_21() {
        assertMuteOK("MUTE10_21")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_22() {
        assertMuteOK("MUTE10_22")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_23() {
        assertMuteOK("MUTE10_23")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_24() {
        assertMuteOK("MUTE10_24")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_25() {
        assertMuteOK("MUTE10_25")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_26() {
        assertMuteOK("MUTE10_26")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_27() {
        assertMuteOK("MUTE10_27")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_28() {
        assertMuteOK("MUTE10_28")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_29() {
        assertMuteOK("MUTE10_29")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_30() {
        assertMuteOK("MUTE10_30")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_31() {
        assertMuteOK("MUTE10_31")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_32() {
        assertMuteOK("MUTE10_32")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_33() {
        assertMuteOK("MUTE10_33")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_34() {
        assertMuteOK("MUTE10_34")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_35() {
        assertMuteOK("MUTE10_35")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_36() {
        assertMuteOK("MUTE10_36")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_37() {
        assertMuteOK("MUTE10_37")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_38() {
        assertMuteOK("MUTE10_38")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_39() {
        assertMuteOK("MUTE10_39")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_40() {
        assertMuteOK("MUTE10_40")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_41() {
        assertMuteOK("MUTE10_41")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_42() {
        assertMuteOK("MUTE10_42")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_43() {
        assertMuteOK("MUTE10_43")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_44() {
        assertMuteOK("MUTE10_44")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_45() {
        assertMuteOK("MUTE10_45")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_46() {
        assertMuteOK("MUTE10_46")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_47() {
        assertMuteOK("MUTE10_47")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_48() {
        assertMuteOK("MUTE10_48")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_49() {
        assertMuteOK("MUTE10_49")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_50() {
        assertMuteOK("MUTE10_50")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_51() {
        assertMuteOK("MUTE10_51")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_52() {
        assertMuteOK("MUTE10_52")
    }

    @Test @Category(PerformanceTest::class)
    // DO (empty loop of 10000000 iterations)
    fun executeMUTE10_53() {
        assertMuteOK("MUTE10_53")
    }

    @Test @Category(PerformanceTest::class)
    // DO (empty loop of 10000000 iterations)
    fun executeKUTE10_53() {
        val kute10_53 = Kute10_53()
        kute10_53.performanceComparing()
    }

    @Test @Category(PerformanceTest::class)
    // FOR (empty loop of 10000000 iterations)
    fun executeMUTE10_54() {
        assertMuteOK("MUTE10_54")
    }

    @Test @Category(PerformanceTest::class)
    // FOR (empty loop of 10000000 iterations)
    fun executeKUTE10_54() {
        val kute10_54 = Kute10_54()
        kute10_54.performanceComparing()
    }

    @Test @Category(PerformanceTest::class)
    // DOW (loop of 10000000 iterations with increment of a numeric var)
    fun executeMUTE10_55() {
        assertMuteOK("MUTE10_55")
    }

    @Test @Category(PerformanceTest::class)
    // DOW (loop of 10000000 iterations with increment of a numeric var)
    fun executeKUTE10_55() {
        val kute10_55 = Kute10_55()
        kute10_55.performanceComparing()
    }

    @Test @Category(PerformanceTest::class)
    // DOU (loop of 10000000 iterations with increment of a numeric var)
    fun executeMUTE10_56() {
        assertMuteOK("MUTE10_56")
    }

    @Test @Category(PerformanceTest::class)
    // DOU (loop of 10000000 iterations with increment of a numeric var)
    fun executeKUTE10_56() {
        val kute10_56 = Kute10_56()
        kute10_56.performanceComparing()
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
