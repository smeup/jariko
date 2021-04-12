package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.LoggingConfiguration
import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.Api
import com.smeup.rpgparser.parsing.ast.ApiDescriptor
import com.smeup.rpgparser.parsing.ast.ApiId
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import org.junit.BeforeClass
import org.junit.experimental.categories.Category
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * These tests are though to evaluate overhead in case of introduction of new features that could have an
 * impact during AST creation.
 * */
class AstOverheadTest : AbstractTest() {

    companion object {

        lateinit var PROGRAM_FINDER: RpgProgramFinder

        val PGM_USING_API_DIRECTIVE = StringBuffer()
        val PGM_USING_COPY_DIRECTIVE = StringBuffer()
        val PGM_USING_API_INLINE = StringBuffer()

        private const val TOTAL_APIS = 1000

        var LOGGING_CONFIGURATION: LoggingConfiguration? = null

        const val REPEAT_TIMES = 1

        @BeforeClass
        @JvmStatic
        fun createSimulationResources() {
            val apiTemplate = """
|    C     nnn           BEGSR
|    C      'A'          DSPLY
|    C      'B'          DSPLY
|    C      'C'          DSPLY
|    C      'D'          DSPLY
|    C      'E'          DSPLY
|    C      'F'          DSPLY
|    C      'G'          DSPLY
|    C      'K'          DSPLY
|    C      'I'          DSPLY
|    C                   ENDSR
        """.trimIndent()

            val templateUsingApiDirective = """
|     /API APInnn
|    C                   EXSR      nnn     
        """.trimIndent()

            val templateUsingCopyDirective = """
|     /COPY APInnn
|    C                   EXSR      nnn     
        """.trimIndent()

            val templateUsingApiInline = """
|    C                   EXSR      nnn     
        """.trimIndent()

            val apis = mutableMapOf<String, String>().apply {
                repeat(TOTAL_APIS) {
                    // create APInnn
                    val subroutineName = it.toString().padStart(3, '0')
                    val apiSource = apiTemplate.replace("nnn", subroutineName)
                    val apiName = "API$subroutineName"
                    this[apiName] = apiSource

                    // Include API directive to programUsingApiDirective
                    PGM_USING_API_DIRECTIVE.append(templateUsingApiDirective.replace("nnn", subroutineName))
                        .append("\n")

                    // Include COPY directive to programUsingCopyDirective
                    PGM_USING_COPY_DIRECTIVE.append(templateUsingCopyDirective.replace("nnn", subroutineName))
                        .append("\n")

                    // Include API inline in programUsingApiInline
                    PGM_USING_API_INLINE.append(templateUsingApiInline.replace("nnn", subroutineName)).append("\n")
                    PGM_USING_API_INLINE.append(apiSource).append("\n")
                }
            }
            PROGRAM_FINDER = MemoryProgramFinder(apis)
            // warming up
//            val file = File.createTempFile("logsperfjariko", ".tsv")
//            LOGGING_CONFIGURATION = fileLoggingConfiguration(file, PARSING_LOGGER)
//            println("Logs will be created in $file")
            testPgm(PGM_USING_API_INLINE.toString(), "WARMUP")
            testPgm(PGM_USING_COPY_DIRECTIVE.toString(), "WARMUP")
            testPgm(PGM_USING_API_DIRECTIVE.toString(), "WARMUP")
            println("done")
        }

        private fun createSystemInterface(): SystemInterface {
            return JavaSystemInterface().apply {
                rpgSystem.addProgramFinder(PROGRAM_FINDER)
                loggingConfiguration = LOGGING_CONFIGURATION
            }
        }

        private fun testPgm(pgmSrc: String, pgmName: String) {
            MainExecutionContext.execute(systemInterface = createSystemInterface()) {
                it.executionProgramName = pgmName
                RpgParserFacade().parseAndProduceAst(pgmSrc.byteInputStream()).apply {
                    assertEquals(TOTAL_APIS, this.subroutines.size)
                }
            }
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiInline1() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_API_INLINE.toString(), pgmName = "INLINE1")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiUsingApi1() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_API_DIRECTIVE.toString(), pgmName = "APIDIR1")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiUsingCopy1() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_COPY_DIRECTIVE.toString(), pgmName = "COPYDIR1")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiInline2() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_API_INLINE.toString(), pgmName = "INLINE2")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiUsingApi2() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_API_DIRECTIVE.toString(), pgmName = "APIDIR2")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiUsingCopy2() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_COPY_DIRECTIVE.toString(), pgmName = "COPYDIR2")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiInline3() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_API_INLINE.toString(), pgmName = "INLINE3")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiUsingApi3() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_API_DIRECTIVE.toString(), pgmName = "APIDIR3")
        }
    }

    @Test(timeout = 4000)
    @Category(PerformanceTest::class)
    fun testPgmApiUsingCopy3() {
        repeat(REPEAT_TIMES) {
            testPgm(pgmSrc = PGM_USING_COPY_DIRECTIVE.toString(), pgmName = "COPYDIR3")
        }
    }
}

class MemoryProgramFinder(private val apis: Map<String, String>) : RpgProgramFinder {

    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        return null
    }

    override fun findCopy(copyId: CopyId): Copy {
        return Copy.fromInputStream(apis[copyId.member]!!.byteInputStream())
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor {
        return ApiDescriptor()
    }

    override fun findApi(apiId: ApiId): Api {
        return Api.loadApi(apis[apiId.member]!!.byteInputStream(), sourceProgram = SourceProgram.RPGLE)
    }
}