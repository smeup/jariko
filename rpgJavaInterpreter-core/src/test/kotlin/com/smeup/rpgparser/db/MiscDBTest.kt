package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.CommandLineParms
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.STATEMENT_LOGGER
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

/**
 * This class has to include real pgm test unit related db native access
 * */
open class MiscDBTest : AbstractTest() {

    private val consoleLoggers = arrayOf(STATEMENT_LOGGER)

    @Rule
    @JvmField
    val globalTimeout = Timeout(10, TimeUnit.SECONDS)

    private fun testMute(
        programName: String,
        vararg consoleLoggers: String = this.consoleLoggers
    ) {

        testIfReloadConfig { reloadConfig ->
            val si = JavaSystemInterface().apply {
                loggingConfiguration = consoleLoggingConfiguration(*consoleLoggers)
            }
            executePgm(
                programName = programName,
                configuration = Configuration(reloadConfig = reloadConfig, options = Options(muteSupport = true)),
                systemInterface = si
            )
        }
    }

    @Test
    fun testX1_X21_06N() {
        testIfReloadConfig {
            val config = Configuration(reloadConfig = it)
            var dsDefinition: DataDefinition? = null
            var dsValue: DataStructValue? = null
            executePgm(
                programName = "db/X1_X21_06N",
                configuration = config,
                systemInterface = JavaSystemInterface().apply {
                    // performance logging in console
                    // loggingConfiguration = consoleLoggingConfiguration(PERFORMANCE_LOGGER)
                },
                params = CommandLineParms() { compilationUnit ->
                    // extracting by compilationUnit data structure information needed for assertions
                    val dsName = "£UIBDS"
                    // get dataDefinition
                    dsDefinition = compilationUnit.getDataDefinition(dsName)
                    // get current instance of dsValue
                    dsValue = DataStructValue.createInstance(compilationUnit, dsName,
                        // these are ds fields
                        mapOf(
                            "\$UIBFU" to com.smeup.rpgparser.interpreter.StringValue("X1_X21_06N"),
                            "\$UIBME" to com.smeup.rpgparser.interpreter.StringValue("MAT.CAL"),
                            "\$UIBK2" to com.smeup.rpgparser.interpreter.StringValue("ERB")
                        )
                    )
                    // this map has as key the ds name and as value the ds value returned byDataStructValue.createInstance
                    mapOf(
                        dsName to dsValue!!
                    )
                }
            )
            assertEquals("OK", dsValue!![dsDefinition!!.getFieldByName("\$UIBK6")].asString().value.trim())
        }
    }

    // TODO Waiting for reload issue evaluation. Poor performances
    @Test
    open fun testMUTE16_01() {
        testMute(programName = "db/MUTE16_01")
    }

    // TODO Waiting for reload issue evaluation. Poor performances
    @Test
    fun testMUTE16_02() {
        testMute(programName = "db/MUTE16_02")
    }

    @Test
    fun testMUTE16_03() {
        testMute(programName = "db/MUTE16_03")
    }

    // TODO Waiting for reload issue evaluation. Poor performances
    @Test
    fun testMUTE16_04() {
        // enabled STATEMENT_LOGGER to highlight performance issue
        testMute("db/MUTE16_04")
    }

    @Test
    fun testMUTE16_05() {
        testMute("db/MUTE16_05")
    }

    // TODO Waiting for reload issue evaluation. Poor performances
    @Test
    fun testMUTE16_06() {
        // enabled STATEMENT_LOGGER to highlight performance issue
        testMute("db/MUTE16_06")
    }

    // TODO Waiting for reload issue evaluation. Poor performances
    @Test
    fun testMUTE16_07() {
        testMute("db/MUTE16_07")
    }

    // TODO Waiting for investigation about HIVAL LOVAL
    @Test
    fun testMUTE16_08() {
        testMute("db/MUTE16_08")
    }

    // TODO Waiting for reload issue evaluation. Poor performances
    @Test
    fun testMUTE16_09() {
        testMute("db/MUTE16_09")
    }
}
