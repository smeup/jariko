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
}