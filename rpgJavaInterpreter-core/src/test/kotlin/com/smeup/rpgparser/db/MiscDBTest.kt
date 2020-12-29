package com.smeup.rpgparser.db

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.adaptForTestCase
import com.smeup.rpgparser.execution.CommandLineParms
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.interpreter.consoleVerboseConfiguration
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test

/**
 * This class has to include real pgm test unit related db native access
 * */
open class MiscDBTest : AbstractTest() {

    @Test
    fun testX1_X21_06N() {
        // if we have a reload configuration
        createReloadConfig()?.let {
            val config = Configuration(reloadConfig = it).adaptForTestCase(this)
            executePgm(
                programName = "db/X1_X21_06N",
                configuration = config,
                systemInterface = JavaSystemInterface().apply {
                    loggingConfiguration = consoleVerboseConfiguration()
                },
                /*
                systemInterface = JavaSystemInterface().apply {
                    loggingConfiguration = fileLoggingConfiguration(
                        File("/home/tron/jariko_rpg", "X1_X21_06N.log"),
                        DATA_LOGGER,
                        LOOP_LOGGER,
                        STATEMENT_LOGGER,
                        EXPRESSION_LOGGER,
                        PERFORMANCE_LOGGER,
                        RESOLUTION_LOGGER,
                        PARSING_LOGGER
                    )
                },*/
                params = CommandLineParms() { compilationUnit ->
                    val dsName = "£UIBDS"
                    // this map have as key the ds name and as value the ds value returned byDataStructValue.createInstance
                    mapOf(
                        dsName to DataStructValue.createInstance(compilationUnit, dsName,
                            // these are ds fields
                            mapOf(
                                "\$UIBFU" to com.smeup.rpgparser.interpreter.StringValue("X1_X21_06N"),
                                "\$UIBME" to com.smeup.rpgparser.interpreter.StringValue("MAT.CAL"),
                                "\$UIBK2" to com.smeup.rpgparser.interpreter.StringValue("ERB")
                            )
                        )
                    )
                }
            )
        } ?: println("ConnectionConfig not available".yellow())
    }
}