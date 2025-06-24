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
     * This program calls the procedure `PR2` which calls `PR1`. Both have the same parameter name and are defined in the main.
     * So, the parent Symbol Table must be the main (where the procedure is defined) and not from the caller.
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
     * This program is like `MUDRNRAPU001129` with a 3rd procedure called from the 2nd. Tests the scope of main `MAIN_CONST`,
     *  used from last `PR0` called. The stack of call is Main -> PR2 -> PR1 -> PR0; all procedures have the main
     *  as the parent Symbol Table.
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
     * This program is like `MUDRNRAPU001131` but `PR2` and `PR1` define `MAIN_VAL` already defined in main.
     * As for the other languages, RPGLE shadows the main definition too. `PR0` consider that defined in main.
     * Finally, the main tests its `MAIN_VAL` that must have been untouched from procedures.
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