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

package com.smeup.rpgparser

import com.andreapivetta.kolor.yellow
import com.smeup.dbnative.ConnectionConfig
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import java.io.File
import kotlin.test.BeforeTest

/**
 * This class must be extended from all test classes in order to automatically manage tests using both version
 * of the compiled programs and non-compiled programs.
 * For each test case you have to implement a base test case (YourTest : AbstractTest()) and then you'll implement
 * (YourTestCompiled : YourTest()) that simply it will override useCompiledVersion method returning true
 * */
abstract class AbstractTest {

    @BeforeTest
    fun beforeTest() {
        // I don't like but until I won't be able to refactor the test units through
        // the unification of the SytemInterfaces I need to use this workaround
        SingletonRpgSystem.reset()
    }

    /**
     * Create ast for exampleName. Let's assume that: testResourceDir is rpgJavaInterpreter-core/src/test/resources
     * @param exampleName Relative path respect testResourceDir where is located source file-
     * For example if we you have to assert AST of testResourceDir/MYPGM.rpgle, you are going to specify MYPGM,
     * elsewhere if you need to test testResourceDir/QILEGEN/MYPGM.rpgle you are going to specify QILEGEN/MYPGM
     * @param considerPosition If true parsing or ast creation error include also line number, default false
     * @param withMuteSupport If true are parsed also MUTE annotations, default false
     * @param printTree If true is dumped on console parse tree in xml format, default false
     * */
    open fun assertASTCanBeProduced(
        exampleName: String,
        considerPosition: Boolean = false,
        withMuteSupport: Boolean = false,
        printTree: Boolean = false
    ): CompilationUnit {
        return assertASTCanBeProduced(
            exampleName = exampleName,
            considerPosition = considerPosition,
            withMuteSupport = withMuteSupport,
            printTree = printTree,
            compiledProgramsDir = getTestCompileDir()
        )
    }

    fun outputOf(
        programName: String,
        initialValues: Map<String, Value> = mapOf(),
        printTree: Boolean = false,
        si: CollectorSystemInterface = ExtendedCollectorSystemInterface()
    ): List<String> {
        return outputOf(
            programName = programName,
            initialValues = initialValues,
            printTree = printTree,
            si = si,
            compiledProgramsDir = getTestCompileDir()
        )
    }

    fun execute(
        programName: String,
        initialValues: Map<String, Value>,
        si: CollectorSystemInterface = ExtendedCollectorSystemInterface(),
        logHandlers: List<InterpreterLogHandler> = SimpleLogHandler.fromFlag(TRACE),
        printTree: Boolean = false
    ): InternalInterpreter {
        return execute(
            programName = programName,
            initialValues = initialValues,
            si = si,
            logHandlers = logHandlers,
            printTree = printTree,
            compiledProgramsDir = getTestCompileDir()
        )
    }

    fun outputOfDBPgm(
        programName: String,
        metadata: List<FileMetadata> = emptyList(),
        initialSQL: List<String> = emptyList(),
        inputParms: Map<String, Value> = mapOf(),
        configuration: Configuration = Configuration(options = Options(muteSupport = true))
    ): List<String> {
        return com.smeup.rpgparser.db.utilities.outputOfDBPgm(
            programName = programName,
            metadata = metadata,
            initialSQL = initialSQL,
            inputParms = inputParms,
            configuration = configuration.adaptForTestCase(this)
        )
    }

    fun executePgmWithStringArgs(
        programName: String,
        programArgs: List<String>,
        logConfigurationFile: File? = null,
        programFinders: List<RpgProgramFinder> = defaultProgramFinders,
        configuration: Configuration = Configuration()
    ) {
        com.smeup.rpgparser.execution.executePgmWithStringArgs(
            programName = programName,
            programArgs = programArgs,
            logConfigurationFile = logConfigurationFile,
            programFinders = programFinders,
            configuration = configuration.adaptForTestCase(this)
        )
    }

    /**
     * Execute a PGM
     * @param programName Name or relative path followed by name. Example performance/MUTE10_01 to execute a PGM
     * in test/resources/performance/MUTE10_01.rpgle
     * @param params If needed, an instance of params to pass to the program. Default empty params
     * @param configuration If needed, you can pass an instance of configuration. Default empty configuration
     * @param systemInterface If needed, you can pass an instance of SystemInterface. Default JavaSystemInterface
     * */
    fun executePgm(
        programName: String,
        params: CommandLineParms = CommandLineParms(emptyList()),
        configuration: Configuration = Configuration(),
        systemInterface: SystemInterface = JavaSystemInterface()
    ): CommandLineParms? {
        val resourceName = if (programName.endsWith(".rpgle")) {
            programName
        } else {
            "$programName.rpgle"
        }
        val resource = AbstractTest::class.java.getResource("/$resourceName")
        require(resource != null) {
            "Cannot find resource $resourceName"
        }
        val programFinders = listOf(
            DirRpgProgramFinder(directory = File(resource.path).parentFile),
            DirRpgProgramFinder(directory = File("src/test/resources/"))
        )

        val jariko = getProgram(
            nameOrSource = programName.substringAfterLast("/", programName),
            systemInterface = systemInterface,
            programFinders = programFinders
        )
        return jariko.singleCall(params, configuration.adaptForTestCase(this))
    }

    fun getTestCompileDir(): File? {
        return if (useCompiledVersion()) {
            testCompiledDir
        } else {
            null
        }
    }

    private fun createConnectionConfig(): ConnectionConfig? {
        val url: String? = System.getenv("JRK_TEST_DB_URL")
        val user: String? = System.getenv("JRK_TEST_DB_USR")
        val password: String? = System.getenv("JRK_TEST_DB_PWD")
        val driver: String? = System.getenv("JRK_TEST_DB_DRIVER")
        return if (url != null && user != null && password != null && driver != null) {
            ConnectionConfig(
                fileName = "*",
                url = url,
                user = user,
                password = password,
                driver = driver
            )
        } else {
            null
        }
    }

    fun createReloadConfig(): ReloadConfig? {
        return createConnectionConfig()?.let {
            ReloadConfig(
                nativeAccessConfig = DBNativeAccessConfig(listOf(it)),
                metadataProducer = { dbFile ->
                    val resource = javaClass.getResource("/db/metadata/$dbFile.json")
                    require(resource != null) {
                        "Cannot find /db/metadata/$dbFile.json in test resources"
                    }
                    FileMetadata.createInstance(resource.openStream())
                }
            )
        }
    }

    /**
     * Executes unitTest only if ReloadConfig is available.
     * ReloadConfig is available only if all of the following environment variable are set:
     * - JRK_TEST_DB_USR - DB user
     * - JRK_TEST_DB_PWD - DB password
     * - JRK_TEST_DB_URL - DB connection string
     * - JRK_TEST_DB_DRIVER - DB driver
     * */
    fun testIfReloadConfig(unitTest: (reloadConfig: ReloadConfig) -> Unit) {
        createReloadConfig()?.let {
            unitTest.invoke(it)
        } ?: println("ConnectionConfig not available".yellow())
    }

    open fun useCompiledVersion() = false
}

fun Configuration.adaptForTestCase(testCase: AbstractTest): Configuration {
    if (this.options != null) {
        this.options!!.compiledProgramsDir = testCase.getTestCompileDir()
    } else {
        this.options = Options(compiledProgramsDir = testCase.getTestCompileDir())
    }
    return this
}
