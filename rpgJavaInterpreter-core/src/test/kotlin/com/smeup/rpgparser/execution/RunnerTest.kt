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

package com.smeup.rpgparser.execution

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.SingletonRpgSystem
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.junit.Assert
import org.junit.Test
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.smeup.rpgparser.execution.main as runnerMain

class RunnerTest : AbstractTest() {
    private val folder by lazy {
        val dir = File(System.getProperty("java.io.tmpdir"), "rpg-test")
        if (!dir.exists()) {
            dir.mkdir()
        }
        dir
    }

    private fun createLogFile(): File {
        return newFile(folder, "log.log").apply {
            // if exists clean
            if (exists()) {
                FileUtils.writeStringToFile(this, "", Charset.defaultCharset())
            }
        }
    }

    private fun createConfigurationFile(logFile: File): File {
        val configFile = newFile(folder, "logging.config")
        val configuration = """
            logger.data.separator =  \t
            logger.date.pattern = HH:mm:ss.SSS
            logger.file.path = ${FilenameUtils.separatorsToUnix(logFile.parentFile.absolutePath)}
            logger.file.name = ${logFile.name}
            data.level = all
            data.output = file
            loop.level = all
            loop.output = file
            expression.level = all
            expression.output = file
            statement.level = all
            statement.output = file
            performance.level = all
            performance.output = file
            resolution.level = all
            resolution.output = file
        """.trimIndent()
        FileUtils.writeStringToFile(configFile, configuration, Charset.defaultCharset())
        return configFile
    }

    private fun newFile(folder: File, name: String): File {
        return File(folder, name)
    }

    @Test
    fun executeExample() {
        val logFile = createLogFile()
        val configurationFile = createConfigurationFile(logFile)
        SingletonRpgSystem.addProgramFinder(ResourceProgramFinder("/logging/"))
        runnerMain(arrayOf("--log-configuration", configurationFile.absolutePath, "TEST_06", "AA", "'ABCD'", "1**"))
        val logs = FileUtils.readLines(logFile, Charset.defaultCharset())
        assertContain(logs, "PERF\tTEST_06\t39\tFOR")
        assertContain(logs, "PERF\tTEST_06\t31\tFOR")
        assertContain(logs, "PERF\tTEST_06\t67\tEXSR")
        assertContain(logs, "PERF\tTEST_06\t\tINTERPRETATION")
    }

    private fun assertContain(logs: List<String>, expected: String) {
        assertNotNull(logs.find { it.contains(expected) }, "Expected: $expected")
    }

    @Test
    fun executeExampleWithCall() {
        val logFile = createLogFile()
        val configurationFile = createConfigurationFile(logFile)
        SingletonRpgSystem.addProgramFinder(ResourceProgramFinder("/"))
        runnerMain(arrayOf("--log-configuration", configurationFile.absolutePath, "CALCFIBCA5", "AA", "'ABCD'", "1**"))
        val logs = FileUtils.readLines(logFile, Charset.defaultCharset())

        // assertContain(logs, "CALCFIBCA5\t\tDATA\tppdat = N/D\t10")
        assertContain(logs, "DATA\tCALCFIBCA5\t3\tASSIGN\tppdat = 10      \twas:         ")
        assertContain(logs, "DATA\tCALCFIB\t2\tASSIGN\tppdat = 10      \twas: N/D")
    }

    @Test
    fun testCallProgramHandler() {
        /*
         * This test check the 'dual CallStmt behaviour':
         *
         * 1 - Normal 'CallStmt behaviour'
         * 'CALL_TRSLT.rpgle' execute the CALL to 'TRANSLATE.rpgle', passing "Hi" string as input parameter.
         * Called 'TRANSLATE.rpgle' will append "!!!" string to input parameter, then return "Hi!!!" to caller.
         *
         * 2 - Extended 'CallStmt behaviour'
         * 'CALL_TRSLT.rpgle' execute the CALL to 'TRANSLATE.rpgle'.
         * Called program is not the previously known 'TRANSLATE.rpgle' but is a custom implementation of it,
         * for example a call to an 'http service' responding with a "Ciao!" plain-text response.
         * N.B. program with name "TRANSLATE" MUST exist, cause is needed to create implementation of Program
         *
         */
        val systemInterface: SystemInterface = JavaSystemInterface()
        val programFinders: List<RpgProgramFinder> = listOf(DirRpgProgramFinder(File("src/test/resources/")))
        val configuration = Configuration()

        val jariko = getProgram("CALL_TRSLT.rpgle", systemInterface, programFinders)
        var result = jariko.singleCall(listOf("Hi"), configuration)
        require(result != null)
        assertEquals("Hi!!!", result.parmsList[0].trim())

        val callProgramHandler = CallProgramHandler(
            handleCall = { programName: String, _: SystemInterface, _: LinkedHashMap<String, Value> ->
                if (programName == "TRANSLATE") {
                    listOf(
                        StringValue(value = "Ciao!!!", varying = false)
                    )
                } else {
                    null
                }
            }
        )

        configuration.options.callProgramHandler = callProgramHandler
        result = jariko.singleCall(listOf(""), configuration)
        require(result != null)
        assertEquals("Ciao!!!", result.parmsList[0].trim())
    }

    @Test
    fun testCallProgramHandler_2() {
        /*
         * This test check the 'dual CallStmt behaviour' as follows:
         *
         * The main rpgle program 'CALL_STMT.rpgle' execute a loop of 4 iterations calling 'ECHO_PGM' program.
         *
         * Behaviour 1: If loop counter is even, the 'CallStmt' works as the 'classic rpg CALL mode', so
         * the ECHO_PGM.rpgle program is called.
         *
         * Behaviour 2: If loop counter is odd, the 'CallStmt' works as the 'extended implementation of CALL', so
         * a 'custom implementation handleCall' is executed, ad simply return "CUSTOM_PGM" string.
         *
         */
        val systemInterface: SystemInterface = JavaSystemInterface()
        val programFinders: List<RpgProgramFinder> = listOf(DirRpgProgramFinder(File("src/test/resources/")))
        val configuration = Configuration()

        var counter = 0
        val callProgramHandler = CallProgramHandler(
            handleCall = { _: String, _: SystemInterface, _: LinkedHashMap<String, Value> ->
                if (counter++ % 2 == 0) {
                    listOf(
                        StringValue(
                            "CUSTOM_PGM",
                            false
                        )
                    )
                } else {
                    null
                }
            }
        )

        val jariko = getProgram("CALL_STMT.rpgle", systemInterface, programFinders)
        configuration.options.callProgramHandler = callProgramHandler
        val result = jariko.singleCall(listOf(""), configuration)
        require(result != null)
    }

    @Test
    fun testCallProgramHandler_3() {
        /*
         * This test check the 'dual CallStmt behaviour' as follows:
         *
         * Behaviour 1: The main rpgle program 'TST_001.rpgle' execute a call to 'ECHO_PGM.rpgle'.
         * This first call is a normal rpg CALL.
         *
         * Behaviour 2: The main rpgle program, execute a call to 'TST_001_2.rpgle'.
         * This call is implemented by 'CallProgramHandler' that execute a POST request
         * to 'https://jariko.smeup.cloud'.
         *
         * The post will invoke the jariko interpreter through a lambda function, passing "JARIKO" string
         * as input parameter and response with a json similar to:
         * {"program-name":"TST_001_2","execution-time":"235 ms","program-params":["HELLO JARIKO        ]}
         *
         * N.B.: set environment variable JARIKO_X_API_KEY
         */
        if (null == System.getenv("JARIKO_X_API_KEY")) {
            return
        }
        val systemInterface: SystemInterface = JavaSystemInterface()
        val programFinders: List<RpgProgramFinder> = listOf(DirRpgProgramFinder(File("src/test/resources/")))
        val configuration = Configuration()

        val callProgramHandler = CallProgramHandler(

            handleCall = { programName: String, _: SystemInterface, _: LinkedHashMap<String, Value> ->
                if (programName == "TST_001_2") {
                    listOf(
                        StringValue(
                            doPost(programName, "JARIKO"),
                            false
                        )
                    )
                } else {
                    null
                }
            }
        )

        val jariko = getProgram("TST_001.rpgle", systemInterface, programFinders)
        configuration.options.callProgramHandler = callProgramHandler
        val result = jariko.singleCall(listOf(""), configuration)
        require(result != null)
        assertTrue { result.parmsList[0].trim().contains("HELLO JARIKO") }
    }

    private fun doPost(theProgram: String, inputParams: String): String {
        val url = URL("https://jariko.smeup.cloud")
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.requestMethod = "POST"
        val xApiKey = System.getenv("JARIKO_X_API_KEY")
        con.setRequestProperty("x-api-key", xApiKey)
        con.setRequestProperty("Content-Type", "application/json; utf-8")
        con.setRequestProperty("Accept", "application/json")
        con.doOutput = true
        val jsonInputString = "{\n" +
                " \"name\": \"$theProgram\",\n" +
                " \"parameters\": [\n" +
                " \"$inputParams                                                                                           \"\n" +
                " ]\n" +
                "}"
        con.outputStream.use { os ->
            val input = jsonInputString.toByteArray(charset("utf-8"))
            os.write(input, 0, input.size)
        }

        val response = StringBuilder()
        BufferedReader(
            InputStreamReader(con.inputStream, "utf-8")).use { br ->
            var responseLine: String?
            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }
        }
        println(response.toString())
        return response.toString()
    }

    /**
     * If a doped program raises an error, this one must be propagated to the caller
     * */
    @Test(expected = java.lang.RuntimeException::class)
    fun raisedErrorMustBePropagated() {
        // just to clear warnings
        DOPEDPGM()
        val pgm = """
     C                   CALL      'DOPEDPGM'            
        """
        val systemInterface = JavaSystemInterface().apply {
            addJavaInteropPackage("com.smeup.rpgparser.execution")
        }
        getProgram(nameOrSource = pgm, systemInterface).singleCall(parms = emptyList())
    }

    /**
     * Tests the right execution of program even although the first program is doped
     * */
    @Test
    fun firstPgmAsDoped() {
        MYDOPED()
        val systemInterface = JavaSystemInterface().apply {
            addJavaInteropPackage("com.smeup.rpgparser.execution")
        }
        val expected = listOf("HELLO", "20.24")
        val actual = getProgram(nameOrSource = "MYDOPED", systemInterface = systemInterface)
            .singleCall(parms = listOf("hello", "10.12"))!!.parmsList
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun rpgCallDopedCallRpg() {
        val pgm = """
     C                   CALL      'DOPEDCALLRPG'            
        """
        getProgram(
            nameOrSource = pgm,
            systemInterface = DOPEDCALLRPG.systemInterface
        ).singleCall(parms = emptyList(), configuration = DOPEDCALLRPG.configuration)
    }
}

class DOPEDPGM : Program {

    override fun params(): List<ProgramParam> = emptyList()

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
        error("Forced error")
    }
}

class MYDOPED : Program {

    override fun params() = listOf(
        ProgramParam("s1", StringType(10, false)),
        ProgramParam("n1", NumberType(5, 2))
    )

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
        systemInterface.display(params.toString())
        return params.values.mapIndexed { index, value ->
            when (index) {
                0 -> StringValue(value.asString().value.toUpperCase(), true)
                1 -> DecimalValue(coerce(value, NumberType(5, 2))
                    .asDecimal().value.multiply(BigDecimal.valueOf(2)))
                else -> throw IllegalArgumentException("index not handled")
            }
        }
    }
}

class DOPEDCALLRPG : Program {

    companion object {
        val systemInterface = JavaSystemInterface().apply {
            addJavaInteropPackage("com.smeup.rpgparser.execution")
        }
        val configuration = Configuration()
    }

    override fun params() = emptyList<ProgramParam>()

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
        val pgm = """
     D Msg             S             10
     C                   EVAL      Msg = 'Test OK'
     C     Msg           DSPLY                      
        """
        getProgram(nameOrSource = pgm, systemInterface = systemInterface)
            .singleCall(parms = emptyList(), configuration = configuration)
        return emptyList()
    }
}