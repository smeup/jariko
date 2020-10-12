package com.samples.jariko

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.executePgmWithStringArgs
import com.smeup.rpgparser.interpreter.ISymbolTable
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

/**
 * This class is used to expose function 'execWithCallback' to jariko rpgle interpreter
 *
 * @param programPath path to rpgle programs
 * @param programName rpgle program filename
 * @param programArgs parm to pass to rpgle
 * @param jarikoCallback instance of JarikoCallback
 */

fun fibonacciOf(fibonacciOf: Int){
    val programPath = "examples/src/main/kotlin/com/samples/jariko"
    val programName = "fibonacci.rpgle"
    val programArgs = listOf(fibonacciOf.toString())
    val jarikoCallback = JarikoCallback(
            exitInRT = { false },
            onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                println(symbolTable["FINAL_VAL"].asInt().value)
            }
    )
    execWithCallback(programPath, programName, programArgs, jarikoCallback)
}

fun execWithCallback(programPath: String, programName: String, programArgs: List<String>, jarikoCallback: JarikoCallback) {
    val rpgProgramFinders = listOf(DirRpgProgramFinder(File(programPath)))
    val configuration = Configuration(
            jarikoCallback = jarikoCallback
    )

    println("Running $programName ...")
    executePgmWithStringArgs(
            programName = programName,
            programFinders = rpgProgramFinders,
            programArgs = programArgs,
            configuration = configuration
    )
    println("... done.")
}

fun main(args: Array<String>) {
    // Sample of fibonacciOf 15
    fibonacciOf(15)
}




