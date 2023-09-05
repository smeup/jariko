/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.logging

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.utils.StringOutputStream
import org.apache.logging.log4j.LogManager
import org.junit.After
import org.junit.Assert
import java.io.File
import java.io.PrintStream
import kotlin.test.*

class LoggingTest : AbstractTest() {

    private val programName = "MYPGM"
    private val varName = "MYVAR"
    private val varValue = "MYVALUE"
    private val logFormatRegexWhenStandardLog = Regex(pattern = "\\d+:\\d+:\\d+\\.\\d+\\s+\\t$programName\\t\\tDATA\\t$varName = N/D\\t$varValue")
    // there is no time stamp reference
    private val logFormatRegexWhenLogAsCallback = Regex(pattern = "\\t$programName\\t\\tDATA\\t$varName = N/D\\t$varValue")

    @After
    fun after() {
        //  need to reset the state of LogManager else depending on the unit tests order, some tests can fail
        LogManager.shutdown()
    }

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

    // Logging must work as before
    @Test
    fun mustWorkAsBeforeLogAsCallbackFeature() {
        val systemInterface = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(DATA_LOGGER)
        }
        MainExecutionContext.execute(systemInterface = systemInterface) {
            val defaultOut = System.out
            try {
                val out = StringOutputStream()
                System.setOut(PrintStream(out))
                MainExecutionContext.log(createAssignmentLogEntry())
                out.flush()
                val loggedOnConsole = out.toString().trim()
                assertTrue(
                    actual = logFormatRegexWhenStandardLog.matches(loggedOnConsole),
                    message = "'$out' must match this regexp: ${logFormatRegexWhenStandardLog.pattern}"
                )
                System.setOut(defaultOut)
                println("Logged on console: $loggedOnConsole")
            } finally {
                System.setOut(defaultOut)
            }
        }
    }

    @Test
    fun logAsCallBack() {
        val systemInterface = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(DATA_LOGGER)
        }
        val configuration = Configuration()
        var enteredInLogInfo = false
        var enteredInChannelLoggingEnabled = false
        // callback implementation by setting logInfo function
        configuration.jarikoCallback.logInfo = { channel, message ->
            assertEquals(DATA_LOGGER, channel)
            assertTrue(
                actual = logFormatRegexWhenLogAsCallback.matches(message),
                message = "'$message' must match this regexp: ${logFormatRegexWhenLogAsCallback.pattern}"
            )
            enteredInLogInfo = true
        }
        // callback implementation by setting channelLoggingEnabled function
        // where I say that I want to log only data channel
        configuration.jarikoCallback.channelLoggingEnabled = { channel ->
            enteredInChannelLoggingEnabled = channel == DATA_LOGGER
            channel == DATA_LOGGER
        }
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val defaultOut = System.out
            try {
                val out = StringOutputStream()
                System.setOut(PrintStream(out))
                MainExecutionContext.log(createAssignmentLogEntry())
                out.flush()
                // in console, we must have nothing because I have implemented jarikoCallback.logInfo
                val loggedOnConsole = out.toString().trim()
                assertTrue(
                    actual = loggedOnConsole.isEmpty(),
                    message = "'$loggedOnConsole' must be empty"
                )
                System.setOut(defaultOut)
                assertTrue(enteredInLogInfo)
                assertTrue(enteredInChannelLoggingEnabled)
            } finally {
                System.setOut(defaultOut)
            }
        }
    }

    @Test
    fun logAsCallbackAlwaysBeatsJarikoStandardLog() {
        val systemInterface = JavaSystemInterface().apply {
            // I ask jariko to log all in console
            loggingConfiguration = consoleVerboseConfiguration()
        }
        // I set configuration in order to disable all logs
        var logInfoCalled = false
        val configuration = Configuration()
        configuration.jarikoCallback.logInfo = { _, _ ->
            // it never must enter here
            logInfoCalled = true
        }
        // I say that I don't want to log anything
        configuration.jarikoCallback.channelLoggingEnabled = { _ -> false }
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val defaultOut = System.out
            try {
                val out = StringOutputStream()
                System.setOut(PrintStream(out))
                MainExecutionContext.log(createAssignmentLogEntry())
                out.flush()
                // in console, we must have nothing because I have implemented jarikoCallback.logInfo
                val loggedOnConsole = out.toString().trim()
                assertTrue(
                    actual = loggedOnConsole.isEmpty(),
                    message = "'$loggedOnConsole' must be empty"
                )
                System.setOut(defaultOut)
                assertFalse(logInfoCalled, message = "logInfo callback never must be called")
            } finally {
                System.setOut(defaultOut)
            }
        }
    }

    /**
     * Test if resolution logs are overwritten through the setting of [com.smeup.rpgparser.execution.JarikoCallback.logInfo]
     * */
    @Test
    fun resolutionChannelLogInfo() {
        val configuration = Configuration()
        var logInfCalled = false
        configuration.jarikoCallback.logInfo = { _, _ ->
            logInfCalled = true
        }
        val systemInterface = JavaSystemInterface(configuration = configuration).apply {
            loggingConfiguration = consoleLoggingConfiguration(RESOLUTION_LOGGER)
        }
        executePgm(programName = "HELLO", configuration = configuration, systemInterface = systemInterface)
        assertTrue(logInfCalled, "logInfo never called")
    }

    /**
     * Test if error events are logged through the [ERROR_LOGGER]
     * */
    @Test
    fun errorEventsInErrorChannel() {
        val defaultOut = System.out
        val out = StringOutputStream()
        System.setOut(PrintStream(out))
        val systemInterface = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(ERROR_LOGGER)
        }
        kotlin.runCatching {
            executePgm(programName = "ERROR02", systemInterface = systemInterface)
        }.onSuccess {
            System.setOut(defaultOut)
            fail(message = "Jariko must throws an exception")
        }.onFailure {
            out.flush()
            System.setOut(defaultOut)
            println(out.toString().trim())
            val errorPattern = Regex(pattern = "\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}\\s+ERROR02\\s+\\d+\\s+ERR\\s+ErrorEvent.+")
            val errorLogEntries = out.toString().trim().split(regex = Regex("\\n|\\r\\n"))
            assertEquals(2, errorLogEntries.size)
            assertTrue(errorLogEntries[0].matches(errorPattern), "Error entry: ${errorLogEntries[0]} does not match $errorPattern")
            assertTrue(errorLogEntries[1].matches(errorPattern), "Error entry: ${errorLogEntries[0]} does not match $errorPattern")
        }
    }

    /**
     * Test if I can override the error event handling by rewriting logInfo callback
     * */
    @Test
    fun errorChannelOverride() {
        var logInfoChannelParam = ""
        val configuration = Configuration().apply {
            // logInfo rewriting
            jarikoCallback.logInfo = { channel, message ->
                println(message)
                logInfoChannelParam = channel
            }
        }
        val systemInterface = JavaSystemInterface(configuration = configuration).apply {
            loggingConfiguration = consoleLoggingConfiguration(ERROR_LOGGER)
        }
        kotlin.runCatching {
            executePgm(programName = "ERROR02", configuration = configuration, systemInterface = systemInterface)
        }.onSuccess {
            fail(message = "Jariko must throws an exception")
        }.onFailure {
            assertEquals(ERROR_LOGGER, logInfoChannelParam)
        }
    }

    /**
     * Test if the error events are written in stderr also if there is no logging configuration
     * */
    @Test
    fun errorEventsMustByPrintedAlsoWhenNotConfigured() {
        val defaultErr = System.err
        val err = StringOutputStream()
        try {
            System.setErr(PrintStream(err))
            executePgm(programName = "ERROR02")
            fail(message = "Jariko must throws an exception")
        } catch (e: Exception) {
            err.flush()
            val errorEventsStr = err.toString().trim().split(regex = Regex(pattern = "\\n|\\r\\n"))
            Assert.assertEquals(2, errorEventsStr.size)
            Assert.assertTrue(errorEventsStr[0].startsWith("ErrorEvent("))
            Assert.assertTrue(errorEventsStr[1].startsWith("ErrorEvent("))
        } finally {
            err.flush()
            System.setErr(PrintStream(defaultErr))
        }
    }

    private fun createAssignmentLogEntry(): AssignmentLogEntry {
        return AssignmentLogEntry(
            programName = programName,
            data = DataDefinition(name = varName, type = StringType(7)),
            value = StringValue(varValue),
            previous = null
        )
    }
}