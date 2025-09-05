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

package com.smeup.rpgparser.execution

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.SingletonRpgSystem
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.math.BigDecimal
import java.nio.charset.Charset
import java.util.*
import kotlin.test.assertNotNull
import com.smeup.rpgparser.execution.main as runnerMain

class RunnerTest : AbstractTest() {
    private val folder by lazy {
        val dir = File(System.getProperty("java.io.tmpdir"), "rpg-test")
        if (!dir.exists()) {
            dir.mkdir()
        }
        dir
    }

    private fun createLogFile(): File =
        newFile(folder, "log.log").apply {
            // if exists clean
            if (exists()) {
                FileUtils.writeStringToFile(this, "", Charset.defaultCharset())
            }
        }

    private fun createConfigurationFile(logFile: File): File {
        val configFile = newFile(folder, "logging.config")
        val configuration =
            """
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

    private fun newFile(
        folder: File,
        name: String,
    ): File = File(folder, name)

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

    private fun assertContain(
        logs: List<String>,
        expected: String,
    ) {
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
        val systemInterface =
            JavaSystemInterface().apply {
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
        val systemInterface =
            JavaSystemInterface().apply {
                addJavaInteropPackage("com.smeup.rpgparser.execution")
            }
        val expected = listOf("HELLO", "20.24")
        val actual =
            getProgram(nameOrSource = "MYDOPED", systemInterface = systemInterface)
                .singleCall(parms = listOf("hello", "10.12"))!!
                .parmsList
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun rpgCallDopedCallRpg() {
        val pgm = """
     C                   CALL      'DOPEDCALLRPG'            
        """
        getProgram(
            nameOrSource = pgm,
            systemInterface = DOPEDCALLRPG.systemInterface,
        ).singleCall(parms = emptyList(), configuration = DOPEDCALLRPG.configuration)
    }
}

class DOPEDPGM : Program {
    override fun params(): List<ProgramParam> = emptyList()

    override fun execute(
        systemInterface: SystemInterface,
        params: LinkedHashMap<String, Value>,
    ): List<Value> {
        error("Forced error")
    }
}

class MYDOPED : Program {
    override fun params() =
        listOf(
            ProgramParam("s1", StringType(10, false)),
            ProgramParam("n1", NumberType(5, 2)),
        )

    override fun execute(
        systemInterface: SystemInterface,
        params: LinkedHashMap<String, Value>,
    ): List<Value> {
        systemInterface.display(params.toString())
        return params.values.mapIndexed { index, value ->
            when (index) {
                0 -> StringValue(value.asString().value.uppercase(Locale.getDefault()), true)
                1 ->
                    DecimalValue(
                        coerce(value, NumberType(5, 2))
                            .asDecimal()
                            .value
                            .multiply(BigDecimal.valueOf(2)),
                    )
                else -> throw IllegalArgumentException("index not handled")
            }
        }
    }
}

class DOPEDCALLRPG : Program {
    companion object {
        val systemInterface =
            JavaSystemInterface().apply {
                addJavaInteropPackage("com.smeup.rpgparser.execution")
            }
        val configuration = Configuration()
    }

    override fun params() = emptyList<ProgramParam>()

    override fun execute(
        systemInterface: SystemInterface,
        params: LinkedHashMap<String, Value>,
    ): List<Value> {
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
