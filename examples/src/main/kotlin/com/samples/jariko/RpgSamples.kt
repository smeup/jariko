package com.samples.jariko

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.executePgmWithStringArgs
import com.smeup.rpgparser.interpreter.ISymbolTable
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

fun main(args: Array<String>) {
   if(args.isEmpty()) {
        println("Usages:")
        println("1. java -jar RpgJavaInterpreter.jar 15  -> to calculate fibonacci of #15 ")
        println("2. java -jar RpgJavaInterpreter.jar [/rpg/path/] [rpglefile.rpgle] [programParams]  -> to execute generic rpg program")
        return
    }

    if (args.size == 1) {
        val programName = "fibonacci.rpgle"
        val programParm = listOf(args[0])
        val jarikoCallback = JarikoCallback(
                exitInRT = { false },
                onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                    println(symbolTable["FINAL_VAL"].asInt().value)
                }
        )
        execWithCallback(programName, programParm, jarikoCallback)
    } else {
        val programPath = args[0]
        val programName = args[1]
        val programParm = args.toList()
        val jarikoCallback = JarikoCallback()
        execWithCallback(programName, programPath, programParm, jarikoCallback)
    }
}

private fun execWithCallback(programName: String, programParms: List<String>, jarikoCallback: JarikoCallback ) {
    val rpgProgramFinders = listOf(DirRpgProgramFinder(File("examples/src/main/kotlin/com/jariko/samples")))
    val configuration = Configuration(
            jarikoCallback = jarikoCallback
    )

    println("Running $programName ...")
    executePgmWithStringArgs(
            programName = programName,
            programFinders = rpgProgramFinders,
            programArgs = programParms,
            configuration = configuration
    )
    println("... done.")
}

private fun execWithCallback(programName: String, programPath: String, programParms: List<String>, jarikoCallback: JarikoCallback ){
    val rpgProgramFinders = listOf(DirRpgProgramFinder(File(programPath)))
    val configuration = Configuration(
            jarikoCallback = jarikoCallback
    )

    println("Running $programName ...")
    executePgmWithStringArgs(
            programName = programName,
            programFinders = rpgProgramFinders,
            programArgs = programParms,
            configuration = configuration
    )
    println("... done.")
}








