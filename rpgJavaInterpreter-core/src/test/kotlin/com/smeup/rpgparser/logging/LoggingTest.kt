/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
    private val logFormatRegexWhenStandardLog = Regex(pattern = "\\d+:\\d+:\\d+\\.\\d+\\s+DATA\\t$programName\\t\\tASSIGN\\t$varName = $varValue\\twas: N/D\\s*")
    // there is no time stamp reference
    private val logFormatRegexWhenLogAsCallback = Regex(pattern = "\\s*DATA\\t$programName\\t\\tASSIGN\\t$varName = $varValue\\twas: N/D\\s*")

    @After
    fun after() {
        //  need to reset the state of LogManager else depending on the unit tests order, some tests can fail
        LogManager.shutdown()
    }

    @Test
    fun consoleLoggingConfigurationTest() {
        val loggingConfiguration = consoleLoggingConfiguration(LogChannel.EXPRESSION, LogChannel.PERFORMANCE)
        assertEquals("all", loggingConfiguration.getProperty("${LogChannel.EXPRESSION.getPropertyName()}.level"))
        assertEquals("console", loggingConfiguration.getProperty("${LogChannel.EXPRESSION.getPropertyName()}.output"))

        assertEquals("all", loggingConfiguration.getProperty("${LogChannel.PERFORMANCE.getPropertyName()}.level"))
        assertEquals("console", loggingConfiguration.getProperty("${LogChannel.PERFORMANCE.getPropertyName()}.output"))

        assertNull(loggingConfiguration.getProperty("logger.file.path"))
        assertNull(loggingConfiguration.getProperty("logger.file.name"))
    }

    @Test
    fun fileLoggingConfigurationTest() {
        val file = File("/usr", "x.log")
        val loggingConfiguration = fileLoggingConfiguration(file, LogChannel.EXPRESSION, LogChannel.PERFORMANCE)
        assertEquals("all", loggingConfiguration.getProperty("${LogChannel.EXPRESSION.getPropertyName()}.level"))
        assertEquals("file", loggingConfiguration.getProperty("${LogChannel.EXPRESSION.getPropertyName()}.output"))

        assertEquals("all", loggingConfiguration.getProperty("${LogChannel.PERFORMANCE.getPropertyName()}.level"))
        assertEquals("file", loggingConfiguration.getProperty("${LogChannel.PERFORMANCE.getPropertyName()}.output"))

        assertEquals(file.parent, loggingConfiguration.getProperty("logger.file.path"))
        assertEquals(file.name, loggingConfiguration.getProperty("logger.file.name"))
    }

    // Logging must work as before
    @Test
    fun mustWorkAsBeforeLogAsCallbackFeature() {
        val systemInterface = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.DATA)
        }
        MainExecutionContext.execute(systemInterface = systemInterface) {
            val defaultOut = System.out
            try {
                val out = StringOutputStream()
                System.setOut(PrintStream(out))
                MainExecutionContext.log(createAssignmentLogEntry())
                MainExecutionContext.log(createDataLogEntry())
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
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.DATA)
        }
        val configuration = Configuration()
        var enteredInLogInfo = false
        var enteredInChannelLoggingEnabled = false
        // callback implementation by setting logInfo function
        configuration.jarikoCallback.logInfo = { channel, message ->
            assertEquals(LogChannel.DATA.getPropertyName(), channel)
            assertTrue(
                actual = logFormatRegexWhenLogAsCallback.matches(message),
                message = "'$message' must match this regexp: ${logFormatRegexWhenLogAsCallback.pattern}"
            )
            enteredInLogInfo = true
        }
        // callback implementation by setting channelLoggingEnabled function
        // where I say that I want to log only data channel
        configuration.jarikoCallback.channelLoggingEnabled = { channel ->
            enteredInChannelLoggingEnabled = channel == LogChannel.DATA.getPropertyName()
            channel == LogChannel.DATA.getPropertyName()
        }
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val defaultOut = System.out
            try {
                val out = StringOutputStream()
                System.setOut(PrintStream(out))
                MainExecutionContext.log(createAssignmentLogEntry())
                MainExecutionContext.log(createDataLogEntry())
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
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.RESOLUTION)
        }
        executePgm(programName = "HELLO", configuration = configuration, systemInterface = systemInterface)
        assertTrue(logInfCalled, "logInfo never called")
    }

    /**
     * Test if error events are logged through the [ERROR_LOGGER]
     * */
    @Test
    @Ignore(value = "I have given up because for some reason in stdout when this test run after check in stdout we have nothing")
    fun errorEventsInErrorChannel() {
        val defaultOut = System.out
        val out = StringOutputStream()
        System.setOut(PrintStream(out))
        val systemInterface = JavaSystemInterface().apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ERROR)
        }
        kotlin.runCatching {
            executePgm(programName = "ERROR02", systemInterface = systemInterface)
        }.onSuccess {
            System.setOut(defaultOut)
            fail(message = "Jariko must throws an exception")
        }.onFailure {
            out.flush()
            val errorPattern = Regex(pattern = "\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}\\s+ERROR02\\s+\\d+\\s+ERR\\s+ErrorEvent.+")
            val errorLogEntries = out.toString().trim().split(regex = Regex("\\n|\\r\\n"))
            // Files.writeString(Paths.get("c:\\temp\\errorEventsInErrorChannel.txt"), out.toString().trim())
            assertEquals(2, errorLogEntries.size)
            assertTrue(errorLogEntries[0].matches(errorPattern), "Error entry: ${errorLogEntries[0]} does not match $errorPattern")
            assertTrue(errorLogEntries[1].matches(errorPattern), "Error entry: ${errorLogEntries[1]} does not match $errorPattern")
            System.setOut(defaultOut)
            println("errorEventsInErrorChannel: ${out.toString().trim()}")
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
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ERROR)
        }
        kotlin.runCatching {
            executePgm(programName = "ERROR02", configuration = configuration, systemInterface = systemInterface)
        }.onSuccess {
            fail(message = "Jariko must throws an exception")
        }.onFailure {
            assertEquals(LogChannel.ERROR.getPropertyName(), logInfoChannelParam)
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

    private fun createAssignmentLogEntry(): LazyLogEntry {
        val logSource = { LogSourceData(programName, "") }
        return LazyLogEntry.produceAssignment(
            logSource,
            DataDefinition(name = varName, type = StringType(7)),
            StringValue(varValue)
        )
    }

    private fun createDataLogEntry(): LazyLogEntry {
    val logSource = { LogSourceData(programName, "") }
        return LazyLogEntry.produceData(
            logSource,
            DataDefinition(name = varName, type = StringType(7)),
            StringValue(varValue),
            null
        )
    }

    /**
     * Test if analytics logs are overwritten through the setting of [com.smeup.rpgparser.execution.JarikoCallback.logInfo]
     * */
    @Test
    fun analyticsChannelLogInfo() {
        val configuration = Configuration()
        var logInfCalled = false
        configuration.jarikoCallback.logInfo = { _, _ ->
            logInfCalled = true
        }
        val systemInterface = JavaSystemInterface(configuration = configuration).apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.ANALYTICS)
        }
        executePgm(programName = "HELLO", configuration = configuration, systemInterface = systemInterface)
        assertTrue(logInfCalled, "logInfo never called")
    }

    /**
     * Test if mock statements are printed out in the appropriate format
     * @see #LS24004983
     */
    @Test
    fun mockStatementFormat() {
        val defaultErr = System.err
        val virtualErr = StringOutputStream()
        System.setErr(PrintStream(virtualErr))
        val systemInterface = JavaSystemInterface()
        executePgm(programName = "MOCK01", systemInterface = systemInterface)
        virtualErr.flush()
        val logEntries = virtualErr.toString().trim().split(regex = Regex("\\n|\\r\\n"))
        val stmtLogEntries = logEntries.filter { it.contains("MOCKSTMT") }
        assertEquals(3, stmtLogEntries.size)

        val logFormatRegexMockStatement = Regex(pattern = "\\d+:\\d+:\\d+\\.\\d+\\s*\\tMOCKSTMT\\tMOCK01.*")
        assertTrue(stmtLogEntries[0].matches(logFormatRegexMockStatement), "Error entry: ${stmtLogEntries[0]} does not match $logFormatRegexMockStatement")
        assertTrue(stmtLogEntries[1].matches(logFormatRegexMockStatement), "Error entry: ${stmtLogEntries[1]} does not match $logFormatRegexMockStatement")
        assertTrue(stmtLogEntries[2].matches(logFormatRegexMockStatement), "Error entry: ${stmtLogEntries[2]} does not match $logFormatRegexMockStatement")

        System.setErr(defaultErr)
    }

    /**
     * Test if mock expression are printed out in the appropriate format
     * @see #LS24004983
     */
    @Test
    fun mockExpressionFormat() {
        val defaultErr = System.err
        val virtualErr = StringOutputStream()
        System.setErr(PrintStream(virtualErr))
        val systemInterface = JavaSystemInterface()
        executePgm(programName = "MOCK01", systemInterface = systemInterface)
        virtualErr.flush()
        val logEntries = virtualErr.toString().trim().split(regex = Regex("\\n|\\r\\n"))
        val exprLogEntries = logEntries.filter { it.contains("MOCKEXPR") }
        assertEquals(2, exprLogEntries.size)

        val logFormatRegexMockExpr = Regex(pattern = "\\d+:\\d+:\\d+\\.\\d+\\s*\\tMOCKEXPR\\tMOCK01.*")
        assertTrue(exprLogEntries[0].matches(logFormatRegexMockExpr), "Error entry: ${exprLogEntries[0]} does not match $logFormatRegexMockExpr")
        assertTrue(exprLogEntries[1].matches(logFormatRegexMockExpr), "Error entry: ${exprLogEntries[1]} does not match $logFormatRegexMockExpr")

        System.setErr(defaultErr)
    }

    /**
     * Test if function resolution logs are correctly printed out
     */
    @Test
    fun functionResolution() {
        val defaultOut = System.out
        val virtualOut = StringOutputStream()
        System.setOut(PrintStream(virtualOut))

        val configuration = Configuration()
        val systemInterface = JavaSystemInterface(configuration = configuration).apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.RESOLUTION)
        }
        executePgm(programName = "FUNCLOG", configuration = configuration, systemInterface = systemInterface)
        virtualOut.flush()

        val logEntries = virtualOut.toString().trim().split(regex = Regex("\\n|\\r\\n"))
        val functionEntries = logEntries.filter { it.contains("FUNCTION", ignoreCase = true) }
        assertEquals(1, functionEntries.size)
        assertTrue { functionEntries.first().contains("CALL1") }

        System.setOut(defaultOut)
    }

    /**
     * Test if function statement logs are correctly printed out
     */
    @Test
    fun functionStatement() {
        val defaultOut = System.out
        val virtualOut = StringOutputStream()
        System.setOut(PrintStream(virtualOut))

        val configuration = Configuration()
        val systemInterface = JavaSystemInterface(configuration = configuration).apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.STATEMENT)
        }
        executePgm(programName = "FUNCLOG", configuration = configuration, systemInterface = systemInterface)
        virtualOut.flush()

        val logEntries = virtualOut.toString().trim().split(regex = Regex("\\n|\\r\\n"))
        val functionEntries = logEntries.filter { it.contains("FunctionInterpreter.CALL1", ignoreCase = true) }
        // 2 * SYMTBLINI + 2 SYMTBLLOAD + 1 Func body + 1 Func return
        assertEquals(6, functionEntries.size)

        System.setOut(defaultOut)
    }

    /**
     * Test if function statement logs are correctly printed out
     */
    @Test
    fun callScope() {
        val defaultOut = System.out
        val virtualOut = StringOutputStream()
        System.setOut(PrintStream(virtualOut))

        val configuration = Configuration()
        val systemInterface = JavaSystemInterface(configuration = configuration).apply {
            loggingConfiguration = consoleLoggingConfiguration(LogChannel.STATEMENT)
        }
        executePgm(programName = "CALLSCOPE", configuration = configuration, systemInterface = systemInterface)
        virtualOut.flush()

        val logEntries = virtualOut.toString().trim().split(regex = Regex("\\n|\\r\\n"))

        // Program CALL logs in its context
        assertTrue { logEntries.any { it.contains("STMT\tCALLSCOPE\t1\tEXEC\tCALL\t\"CALLDEFV2\"") } }
        // We start called program interpretation
        assertTrue { logEntries.any { it.contains("STMT\tCALLDEFV2\t\tSTART\tINTERPRETATION") } }
        // We execute statements inside the called program
        assertTrue { logEntries.any { it.contains("STMT\tCALLDEFV2\t6\tEXEC\tEVAL\tP1 = \"R\"\t") } }
        // We finish called program interpretation
        assertTrue { logEntries.any { it.contains("STMT\tCALLDEFV2\t\tEND\tINTERPRETATION") } }
        // We immediately restore the correct scope
        assertTrue { logEntries.any { it.contains("STMT\tCALLSCOPE\t3\tEXEC\tEVAL\t\$A = \"T\"\t") } }

        System.setOut(defaultOut)
    }
}
