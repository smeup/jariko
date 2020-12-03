package com.smeup.rpgparser

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.execution.defaultProgramFinders
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import java.io.File

/**
 * This class must be extended from all test classes in order to automatically manage tests using both version
 * of the compiled programs and non-compiled programs.
 * For each test case you have to implement a base test case (YourTest : AbstractTest()) and then you'll implement
 * (YourTestCompiled : YourTest()) that simply it will override useCompiledVersion method returning true
 * */
abstract class AbstractTest {

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
        initialSQL: List<String>,
        inputParms: Map<String, StringValue> = mapOf(),
        printTree: Boolean = false
    ): List<String> {
        return com.smeup.rpgparser.db.sql.outputOfDBPgm(
            programName = programName,
            initialSQL = initialSQL,
            inputParms = inputParms,
            printTree = printTree,
            compiledProgramsDir = getTestCompileDir()
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

    fun getTestCompileDir(): File? {
        return if (useCompiledVersion()) {
            testCompiledDir
        } else {
            null
        }
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
