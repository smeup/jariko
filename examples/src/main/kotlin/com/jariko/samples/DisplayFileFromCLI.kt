package com.jariko.samples

import com.smeup.dspfparser.linesclassifier.DSPFFieldType
import com.smeup.dspfparser.linesclassifier.DSPFRecord
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

// UI control functions

class UnknownVariable(name: String) : Exception("`$name`")
class WrongInputSyntax : Exception("Should be: `VAR1=VALUE;VAR2=23`")

private fun parseInput(input: String): Map<String, String> {
    try {
        val assignments = input.split(';')
        val variablesAndValues = mutableMapOf<String, String>()

        assignments.forEach {
            val tokens = it.split('=')
            variablesAndValues[tokens[0]] = tokens[1]
        }

        return variablesAndValues
    } catch (e: Exception) {
        throw WrongInputSyntax()
    }
}

private fun printRecord(record: DSPFRecord) {
    println()
    print("OUTPUT fields:\n")
    record.mutables.filter { it.type == DSPFFieldType.OUTPUT }.forEach {
        println("\t|${it.name}=${(it.value as Value).asString().value}|")
    }
    println()
    print("INPUT fields:\n")
    record.mutables.filter { it.type == DSPFFieldType.INPUT }.forEach {
        println("\t|${it.name}=${(it.value as Value).asString().value}|")
    }
    println()
    println("Insert input: ")
}

private fun askInputFor(record: DSPFRecord): Map<String, Value> {
    val variablesAndValues = mutableMapOf<String, Value>()
    val line = readln()
    val updatedVariables = parseInput(line)

    updatedVariables.keys.forEach { variable ->
        record.mutables.find { field -> field.name == variable } ?: throw UnknownVariable(variable)
    }

    record.mutables.filter { it.type == DSPFFieldType.INPUT && updatedVariables[it.name] != null }.forEach {
        if (it.isNumeric && it.precision!! == 0)
            variablesAndValues[it.name] = IntValue(updatedVariables[it.name]!!.toLong())
        if (it.isNumeric && it.precision!! > 0)
            variablesAndValues[it.name] = DecimalValue(updatedVariables[it.name]!!.toBigDecimal())
        else if (!it.isNumeric)
            variablesAndValues[it.name] = StringValue(updatedVariables[it.name]!!)
    }

    return variablesAndValues
}

private fun onExfmt(record: DSPFRecord, runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
    printRecord(record)
    while (true) {
        try {
            val variablesAndValues = askInputFor(record)
            return OnExfmtResponse(runtimeInterpreterSnapshot, variablesAndValues)
        } catch (e: Exception) {
            e.printStackTrace()
            continue
        }
    }
}

// Jariko call setup functions

private fun createDspfConfig(): DspfConfig {
    val simpleDspfConfig = SimpleDspfConfig({ }.javaClass.getResource("/metadata")!!.path)
    return DspfConfig(
        metadataProducer = simpleDspfConfig::getMetadata,
        dspfProducer = simpleDspfConfig::dspfProducer
    )
}

private fun createJarikoCallback(): JarikoCallback {
    val jarikoCallback = JarikoCallback()
    jarikoCallback.onExfmt = ::onExfmt
    return jarikoCallback
}

private fun createConfig(): Configuration {
    return Configuration(
        dspfConfig = createDspfConfig(),
        jarikoCallback = createJarikoCallback()
    )
}

fun main() {
    val programSource = "ADD01.rpgle"
    val programFinders = listOf(DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg")!!.path)))
    val program = getProgram(nameOrSource = programSource, programFinders = programFinders)

    program.singleCall(emptyList(), configuration = createConfig())
}