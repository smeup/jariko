package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.LogChannel
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import org.junit.Test
import org.junit.experimental.categories.Category

class PerfLoggingExample : MULANGTTest() {

    @Test @Category(PerformanceTest::class)
    fun testMUTE10_10() {
        val si = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.PERFORMANCE)
        }
        executePgm(programName = "performance/MUTE10_10", systemInterface = si)
    }
}