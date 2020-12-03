package com.smeup.rpgparser.evaluation

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.*
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.interpreter.StringType
import com.smeup.rpgparser.interpreter.parm
import com.smeup.rpgparser.jvminterop.JvmMockProgram
import com.smeup.rpgparser.mute.color
import com.smeup.rpgparser.mute.executeMuteAnnotations
import com.smeup.rpgparser.performance.*
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.assertEquals
import kotlin.test.fail

open class MUTEExamplesTest : AbstractTest() {

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_01() {
        assertMuteOK("MUTE10_01")
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

    private fun siWithProgramFinderInPerformanceFolder(jvmMockPrograms: List<JvmMockProgram> = emptyList<JvmMockProgram>()): ExtendedCollectorSystemInterface {
        val si = ExtendedCollectorSystemInterface(jvmMockPrograms = jvmMockPrograms)
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
    // FOR (empty loop of 10000000 iterations)
    fun executeMUTE10_54() {
        assertMuteOK("MUTE10_54")
    }

    @Test @Category(PerformanceTest::class)
    // DOW (loop of 10000000 iterations with increment of a numeric var)
    fun executeMUTE10_55() {
        assertMuteOK("MUTE10_55")
    }

    @Test @Category(PerformanceTest::class)
    // DOU (loop of 10000000 iterations with increment of a numeric var)
    fun executeMUTE10_56() {
        assertMuteOK("MUTE10_56")
    }

    @Test @Category(PerformanceTest::class)
    // DOU (loop of 10000000 iterations with increment of a numeric var)
    fun executeMUTE10_57() {
        assertMuteOK("MUTE10_57")
    }

    @Test @Category(PerformanceTest::class)
    fun executeMUTE10_58() {
        // mock of £JAX_IMP0, £IXA and £JAX_FIN0
        val jvmMockPrograms = listOf(
            "£JAX_IMP0".mockProgram(1),
            "£IXA".mockProgram(7),
            "£JAX_FIN0".mockProgram(1)
        )
        assertMuteOK(programName = "MUTE10_58", jvmMockPrograms = jvmMockPrograms)
    }

    @Test @Category(PerformanceTest::class)
    // DO (expected 68ms. to perform an empty loop of 10000000 iterations, as MUTE10_53.rpgle)
    fun executeKute10_53() {
        val kute = Kute10_53()
        kute.performanceComparing(true)
    }

    @Test @Category(PerformanceTest::class)
    // FOR (expected 306ms. to perform an empty loop of 10000000 iterations, as MUTE10_54.rpgle)
    fun executeKute10_54() {
        val kute = Kute10_54()
        kute.performanceComparing(true)
    }

    @Test @Category(PerformanceTest::class)
    // DOW (expected 83ms. to perform a loop of 10000000 iterations with increment of a numeric var, as MUTE10_55.rpgle)
    fun executeKute10_55() {
        val kute = Kute10_55()
        kute.performanceComparing(true)
    }

    @Test @Category(PerformanceTest::class)
    // DOU (expected 81ms. to perform a loop of 10000000 iterations with increment of a numeric var, as MUTE10_56.rpgle)
    fun executeKute10_56() {
        val kute = Kute10_56()
        kute.performanceComparing(true)
    }

    @Test @Category(PerformanceTest::class)
    // CAT (expected 150ms. to perform a loop of 10000000 iterations with conCATenation of two strings as MUTE10_50.rpgle)
    fun executeKute10_50() {
        val kute = Kute10_50()
        kute.performanceComparing(true)
    }

    /**
     * Use this function to mock a program. Mock program has no logic.
     * @param paramsCount Number of param required by program. View CALL in RPG
     * */
    private fun String.mockProgram(paramsCount: Int = 0): JvmMockProgram {
        val params = Array(paramsCount) { StringType(1).parm(it.toString()) }
        return JvmMockProgram(name = this, params = params.toList())
    }

    private fun dummyProgramFinder() = DummyProgramFinder("/performance/")

    private fun assertMuteOK(
        programName: String,
        withOutput: List<String>? = null,
        configuration: Configuration = Configuration(options = Options(muteSupport = true, compiledProgramsDir = getTestCompileDir(), muteVerbose = true)),
        jvmMockPrograms: List<JvmMockProgram> = emptyList<JvmMockProgram>()
    ) {
        val si = siWithProgramFinderInPerformanceFolder(jvmMockPrograms = jvmMockPrograms)
        val programSrc = dummyProgramFinder().getFile(programName)
        executeMuteAnnotations(
            programSrc = programSrc,
            systemInterface = si,
            configuration = configuration
        )

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
