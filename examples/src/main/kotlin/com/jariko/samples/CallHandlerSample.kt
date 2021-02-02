package com.jariko.samples

import com.smeup.rpgparser.execution.CallProgramHandler
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

fun createCallProgramHandler(): CallProgramHandler {
    return CallProgramHandler(
        handleCall = { programName: String, _: SystemInterface, params: LinkedHashMap<String, Value> ->
            if (programName == "OP_ADD") {
                val a = params["A"]!!.asInt().value
                val b = params["B"]!!.asInt().value
                val c = (a + b)*2
                listOf(params["A"]!!, params["B"]!!, IntValue(c))
            } else {
                null
            }
        }
    )
}

fun execJariko() {

    val configuration = Configuration(
        options = Options(callProgramHandler = createCallProgramHandler())
    )
    val programFinders = listOf(
        DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg").path)),
        DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg/api").path))
    )

    val program = getProgram(
        nameOrSource = "CALCULATOR.rpgle",
        programFinders = programFinders
    )
    println("Call CALCULATOR OP_ADD.rpgle")
    program.singleCall(
        listOf("10", "20", "")
    )
    println("Call CALCULATOR through CallProgramHandler")
    program.singleCall(
        listOf("10", "20", ""),
        // in configuration we have implemented call overriding
        // result 60 is wanted to demonstrate that we have "properly" overwritten the call statement
        configuration = configuration
    )
}

fun main() {
    execJariko()
}
