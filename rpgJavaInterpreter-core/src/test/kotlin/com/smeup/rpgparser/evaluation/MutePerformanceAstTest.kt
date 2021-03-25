package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.PARSING_LOGGER
import com.smeup.rpgparser.logging.fileLoggingConfiguration
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.junit.Test
import org.junit.experimental.categories.Category
import java.io.File
import java.io.FileInputStream

open class MutePerformanceAstTest : AbstractTest() {

    val si = JavaSystemInterface().apply {
        val dir = File(System.getProperty("user.dir"), "build/test-results/testPerformance")
        if (!dir.exists()) {
            println("Creating $dir")
            dir.mkdirs()
        }
        val file = File(dir, "performance-ast.log")
        println("Performance ast creation logging file will be created in: $file")
        loggingConfiguration = fileLoggingConfiguration(file, PARSING_LOGGER)
        loggingConfiguration?.setProperty("logger.date.pattern", "yyyyMMdd HH:mm:ss")
    }

    private fun createAst(
        fileName: String,
        onParsingError: (error: Throwable) -> Unit = { throw it }
    ) {
        val file = File(javaClass.getResource("/performance-ast").path, fileName)
        println("Creating AST for: $file")
        val configuration = Configuration()
        configuration.options = Options(false, getTestCompileDir())
        // this way i enable copy looking for
        si.rpgSystem.addProgramFinder(DirRpgProgramFinder(file.parentFile))
        MainExecutionContext.execute(systemInterface = si, configuration = configuration) { it ->
            it.executionProgramName = file.name
            val rpgParserFacade = RpgParserFacade()
            runCatching {
                rpgParserFacade.parseAndProduceAst(FileInputStream(file))
            }.onFailure { error ->
                onParsingError.invoke(error)
            }
        }
        println("AST created")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_59() {
        createAst("MUTE10_59.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_60() {
        createAst("MUTE10_60.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_61() {
        createAst("MUTE10_61.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_62() {
        createAst("MUTE10_62.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_63() {
        createAst("MUTE10_63.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_64() {
        createAst("MUTE10_64.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_65() {
        createAst("MUTE10_64.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_66() {
        createAst("MUTE10_66.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_67() {
        createAst("MUTE10_67.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_68() {
        createAst("MUTE10_68.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_69() {
        createAst("MUTE10_69.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_70() {
        createAst("MUTE10_70.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE10_58() {
        createAst("../performance/MUTE10_58.rpgle")
    }

    @Test
    @Category(PerformanceTest::class)
    fun testMUTE_ERROR() {
        var parsingError: Throwable? = null
        createAst("MUTE_ERROR.rpgle") {
            println("Handled forced error: ${it.message}")
            parsingError = it
        }
        requireNotNull(parsingError)
    }
}
