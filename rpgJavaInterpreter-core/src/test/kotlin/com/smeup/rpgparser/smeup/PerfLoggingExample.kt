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

    @Test @Category(PerformanceTest::class)
    fun testT10_A60_P02() {
        val si = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.PERFORMANCE, LogChannel.ANALYTICS)
        }
        executePgm(programName = "smeup/T10_A60_P02", systemInterface = si)
    }

    @Test @Category(PerformanceTest::class)
    fun testT10_A60_P03() {
        val si = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ANALYTICS)
        }
        executePgm(programName = "smeup/T10_A60_P03", systemInterface = si)
    }

    @Test @Category(PerformanceTest::class)
    fun testT40_A20_P17() {
        val si = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ANALYTICS)
        }
        executePgm(programName = "smeup/T40_A20_P17", systemInterface = si)
    }

    @Test @Category(PerformanceTest::class)
    fun testT10_A80_P02() {
        val si = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ANALYTICS)
        }
        executePgm(programName = "smeup/T10_A80_P02", systemInterface = si)
    }
}