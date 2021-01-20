package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgSystem

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.junit.Test
import java.io.File
import java.net.URL
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.smeup.rpgparser.execution.main as runnerMain

class RunnerTest {

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
        RpgSystem.addProgramFinder(ResourceProgramFinder("/logging/"))
        runnerMain(arrayOf("--log-configuration", configurationFile.absolutePath, "TEST_06", "AA", "'ABCD'", "1**"))
        val logs = FileUtils.readLines(logFile, Charset.defaultCharset())
        assertContain(logs, "TEST_06\t44\tPERF\tENDFOR J")
        assertContain(logs, "TEST_06\t44\tPERF\tENDFOR J")
        assertContain(logs, "TEST_06\t61\tPERF\tENDFOR I")
        assertContain(logs, "TEST_06\t80\tPERF\tSUBROUTINE END\tPRINT")
        assertContain(logs, "TEST_06\t\tPERF\tINTERPRETATION END TEST_06")
    }

    private fun assertContain(logs: List<String>, expected: String) {
        assertNotNull(logs.find { it.contains(expected) }, "Expected: $expected")
    }

    @Test
    fun executeExampleWithCall() {
        val logFile = createLogFile()
        val configurationFile = createConfigurationFile(logFile)
        RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
        runnerMain(arrayOf("--log-configuration", configurationFile.absolutePath, "CALCFIBCA5", "AA", "'ABCD'", "1**"))
        val logs = FileUtils.readLines(logFile, Charset.defaultCharset())

        assertContain(logs, "CALCFIBCA5\t\tDATA\tppdat = N/D\t10")
        assertContain(logs, "CALCFIB\t\tDATA\tppdat = N/D\t10")
    }

    @Test
    fun testCallProgramHandler() {
        /*
         * This test check the 'dual CallStmt behaviour':
         * 1 - Normal 'CallStmt behaviour'
         * 'CALL_TRSLT.rpgle' execute the CALL to 'TRANSLATE.rpgle', passing "Hi" string as input parameter.
         * Called 'TRANSLATE.rpgle' will append "!!!" string to input parameter, then return "Hi!!!" to caller.
         *
         * 2 - Extended 'CallStmt behaviour'
         * 'CALL_TRSLT.rpgle' execute the CALL to 'TRANSLATE.rpgle'.
         * Called program is not the previously known 'TRANSLATE.rpgle' but is a custom implementation of it,
         * for example a call to an 'http service' responding with a "Ciao!" plain-text response.
         *
         */
        var systemInterface: SystemInterface = JavaSystemInterface()
        val programFinders: List<RpgProgramFinder> = listOf(DirRpgProgramFinder(File("src/test/resources/")))
        val configuration = Configuration()

        val jariko = getProgram("CALL_TRSLT.rpgle", systemInterface, programFinders)
        var result = jariko.singleCall(listOf("Hi"), configuration)
        require(result != null)
        assertEquals("Hi!!!", result.parmsList[0].trim())

        var programName = "TRANSLATE"

        val callProgramHandler = CallProgramHandler(
            mayCall = { programName == "TRANSLATE" },
            handleCall = { _: String, _: SystemInterface, _: LinkedHashMap<String, Value> ->
                listOf(
                    StringValue(
                        URL("https://run.mocky.io/v3/c4e203a5-9511-49f0-bc00-78dff4c4ebc7").readText(),
                        false
                    )
                )
            }
        )

        configuration.options?.callProgramHandler = callProgramHandler
        result = jariko.singleCall(listOf(""), configuration)
        require(result != null)
        assertEquals("Ciao!", result.parmsList[0].trim())
    }
}
