package com.smeup.rpgparser.execution

import com.smeup.rpgparser.rgpinterop.RpgSystem

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.junit.Test
import java.io.File
import java.nio.charset.Charset
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
        return newFile(folder, "log.log")
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
        assertContain(logs, "TEST_06\t\tPERF\tEND TEST_06")
    }

    private fun assertContain(logs: List<String>, s: String) {
        assertNotNull(logs.find { it.contains(s) })
    }

    @Test
    fun executeExampleWithCall() {
        val logFile = createLogFile()
        val configurationFile = createConfigurationFile(logFile)
        RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
        runnerMain(arrayOf("--log-configuration", configurationFile.absolutePath, "CALCFIBCA5", "AA", "'ABCD'", "1**"))
        val logs = FileUtils.readLines(logFile, Charset.defaultCharset())

        assertContain(logs, "CALCFIBCA5\t\tDATA\tppdat = N/D\t10")
        assertContain(logs, "CALCFIB.CALCFIB\t\tDATA\tppdat = N/D\t10")
    }
}