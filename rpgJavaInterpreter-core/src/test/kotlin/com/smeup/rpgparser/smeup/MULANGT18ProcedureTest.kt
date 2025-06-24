package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.LogChannel
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import org.junit.Test
import kotlin.test.assertEquals

open class MULANGT18ProcedureTest : MULANGTTest() {
    /**
     * LIKE to variable defined into a COPY. This one is also declared inner of procedure.
     * @see #LS24003187
     */
    @Test
    fun executeMU181003() {
        val expected = listOf("O:   HT     -P:HT_P")
        assertEquals(expected, "smeup/MU181003".outputOf(configuration = smeupConfig))
    }

    /**
     * This program calls the procedure `PR2` which calls `PR1`. `PR1` is defined in main. So, the parent Symbol Table
     *  must be that of main.
     * Last consideration, both procedures define the parameter with the same name.
     */
    @Test
    fun executeMUDRNRAPU001129() {
        val messages = mutableListOf<String>()
        val systemInterface = JavaSystemInterface().apply {
            onDisplay = { message, _ -> messages.add(message) }
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ERROR)
        }
        executePgm(programName = "smeup/MUDRNRAPU001129", systemInterface = systemInterface)

        val expected = listOf("3")
        assertEquals(expected, messages.map { it.trimEnd() })
    }

    /**
     * This program calls the procedure `PR2` which calls `PR1`; this one calls `PR0`. All procedures are defined in main.
     */
    @Test
    fun executeMUDRNRAPU001130() {
        val messages = mutableListOf<String>()
        val systemInterface = JavaSystemInterface().apply {
            onDisplay = { message, _ -> messages.add(message) }
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ERROR)
        }
        executePgm(programName = "smeup/MUDRNRAPU001130", systemInterface = systemInterface)

        val expected = listOf("11")
        assertEquals(expected, messages.map { it.trimEnd() })
    }

    /**
     * This program calls the procedure `PR2` which calls `PR1`; this one calls `PR0`. All procedures are defined in main.
     * In this case a variable of main is defined into the first two called procedures.
     */
    @Test
    fun executeMUDRNRAPU001131() {
        val messages = mutableListOf<String>()
        val systemInterface = JavaSystemInterface().apply {
            onDisplay = { message, _ -> messages.add(message) }
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ERROR)
        }
        executePgm(programName = "smeup/MUDRNRAPU001131", systemInterface = systemInterface)

        val expected = listOf("2", "73")
        assertEquals(expected, messages.map { it.trimEnd() })
    }
}