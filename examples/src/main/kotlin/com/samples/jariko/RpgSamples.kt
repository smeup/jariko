package com.samples.jariko

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.executePgmWithStringArgs
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.ISymbolTable
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

/**
 * This function execute fibonacciOf number passed as argument
 * The rpgle program in retrieve from local sources from programPath
 * @param fibonacciOf integer to calculate fibonacci series
 */
fun fibonacciOf(fibonacciOf: Int) {
    val programPath = "examples/src/main/kotlin/com/samples/jariko"
    val programName = "fibonacci.rpgle"
    val programArgs = listOf(fibonacciOf.toString())
    var output: Long = 0
    val jarikoCallback = JarikoCallback(
            exitInRT = { false },
            onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                output = symbolTable["FINAL_VAL"].asInt().value
            }
    )
    execWithCallback(programPath, programName, programArgs, jarikoCallback)
    print(output)
}

/**
 * This function is used to execute rpgle from programPath, named filename,
 * with programArgs as parameters showing var values by JarikoCallback
 * @param programPath path to rpgle programs
 * @param programName rpgle program filename
 * @param programArgs parm to pass to rpgle
 * @param jarikoCallback instance of JarikoCallback
 */
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

/**
 * This function is used to execute rpgle source as programSource,
 * with programArgs as parameters showing var values by JarikoCallback
 * @param programSource source of rpgle program
 * @param programArgs parm to pass to rpgle
 * @param jarikoCallback instance of JarikoCallback
 */
fun execWithCallBack(programSource: String, programArgs: List<String>, jarikoCallback: JarikoCallback) {
    println("Running source: $programSource ...")
    val configuration = Configuration(
            jarikoCallback = jarikoCallback
    )
    val systemInterface = JavaSystemInterface()
    val commandLineProgram = getProgram(programSource, systemInterface)
    commandLineProgram.singleCall(programArgs, configuration = configuration)
    println("... done.")
}