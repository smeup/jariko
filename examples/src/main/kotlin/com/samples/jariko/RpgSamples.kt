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
 * This class is used to expose function 'execWithCallback' to jariko rpgle interpreter
 * The function can be called with two signature:
 *
 * @param programPath path to rpgle programs
 * @param programName rpgle program filename
 * @param programArgs parm to pass to rpgle
 * @param jarikoCallback instance of JarikoCallback
 *
 * or:
 *
 * @param programSource source of rpgle program
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

fun execWithCallBack(programSource: String, programArgs: List<String>, jarikoCallback: JarikoCallback){
    println("Running source: $programSource ...")
    val configuration = Configuration(
            jarikoCallback = jarikoCallback
    )
    val systemInterface = JavaSystemInterface()
    val commandLineProgram = getProgram(programSource, systemInterface)
    commandLineProgram.singleCall(programArgs, configuration = configuration)
    println("... done.")
}

fun main(args: Array<String>) {
    // Sample of fibonacciOf(15) using local rpgle source file "fibonacci.rpgle"
    fibonacciOf(15)

    // Sample of execWithCallback with programPath and programName
    var jarikoCallback = JarikoCallback(
            exitInRT = { false },
            onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                println(symbolTable["FINAL_VAL"].asInt().value)
            }
    )
    execWithCallback("examples/src/main/kotlin/com/samples/jariko", "fibonacci.rpgle", listOf("12"), jarikoCallback)

    // Sample of execWithCallback with programSource
    val programSource = """
     D PWROF2          S              5
     D PWROF2_N        S              5  0
      * Calculate power of two of received number
     C     *ENTRY        PLIST
     C                   PARM                    PWROF2
     C                   EVAL      PWROF2_N = %DEC(%TRIM(PWROF2):5:0)
     C                   EVAL      PWROF2_N = PWROF2_N * PWROF2_N
     C                   SETON                                          LR
     """
    jarikoCallback = JarikoCallback(
            exitInRT = { false },
            onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                println(symbolTable["PWROF2_N"].asInt().value)
            }
    )
    execWithCallBack(programSource, listOf("12"), jarikoCallback)
}



