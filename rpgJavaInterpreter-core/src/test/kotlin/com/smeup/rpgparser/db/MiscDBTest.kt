package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.adaptForTestCase
import com.smeup.rpgparser.execution.CommandLineParms
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.PERFORMANCE_LOGGER
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import org.junit.Test
import kotlin.test.assertEquals

/**
 * This class has to include real pgm test unit related db native access
 * */
open class MiscDBTest : AbstractTest() {

    @Test
    fun testX1_X21_06N() {
        testIfReloadConfig {
            val config = Configuration(reloadConfig = it).adaptForTestCase(this)
            var dsDefinition: DataDefinition? = null
            var dsValue: DataStructValue? = null
            executePgm(
                programName = "db/X1_X21_06N",
                configuration = config,
                systemInterface = JavaSystemInterface().apply {
                    // statement logging in console
                    loggingConfiguration = consoleLoggingConfiguration(PERFORMANCE_LOGGER)
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
}