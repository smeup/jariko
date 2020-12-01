package com.smeup.rpgparser

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import java.io.File

abstract class AbstractTestCase {

    fun assertASTCanBeProduced(
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
            compiledProgramsDir = getTestCompilderDir()
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
            compiledProgramsDir = getTestCompilderDir()
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
            compiledProgramsDir = getTestCompilderDir()
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
            compiledProgramsDir = getTestCompilderDir()
        )
    }

    fun getTestCompilderDir(): File? {
        return if (useCompiledVersion()) {
            testCompiledDir
        } else {
            null
        }
    }

    open fun useCompiledVersion() = false
}