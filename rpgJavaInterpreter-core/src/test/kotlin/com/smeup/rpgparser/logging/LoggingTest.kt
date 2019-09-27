package com.smeup.rpgparser.logging

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoggingTest {

    @Test
    fun consoleLoggingConfigurationTest() {
        val loggingConfiguration = consoleLoggingConfiguration(EXPRESSION_LOGGER, PERFORMANCE_LOGGER)
        assertEquals("all", loggingConfiguration.getProperty("$EXPRESSION_LOGGER.level"))
        assertEquals("console", loggingConfiguration.getProperty("$EXPRESSION_LOGGER.output"))

        assertEquals("all", loggingConfiguration.getProperty("$PERFORMANCE_LOGGER.level"))
        assertEquals("console", loggingConfiguration.getProperty("$PERFORMANCE_LOGGER.output"))

        assertNull(loggingConfiguration.getProperty("logger.file.path"))
        assertNull(loggingConfiguration.getProperty("logger.file.name"))
    }

    @Test
    fun fileLoggingConfigurationTest() {
        val file = File("/usr", "x.log")
        val loggingConfiguration = fileLoggingConfiguration(file, EXPRESSION_LOGGER, PERFORMANCE_LOGGER)
        assertEquals("all", loggingConfiguration.getProperty("$EXPRESSION_LOGGER.level"))
        assertEquals("file", loggingConfiguration.getProperty("$EXPRESSION_LOGGER.output"))

        assertEquals("all", loggingConfiguration.getProperty("$PERFORMANCE_LOGGER.level"))
        assertEquals("file", loggingConfiguration.getProperty("$PERFORMANCE_LOGGER.output"))

        assertEquals(file.parent, loggingConfiguration.getProperty("logger.file.path"))
        assertEquals(file.name, loggingConfiguration.getProperty("logger.file.name"))
    }
}