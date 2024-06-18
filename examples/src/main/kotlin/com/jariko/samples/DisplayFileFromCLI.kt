package com.jariko.samples

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.dspfparser.linesclassifier.DSPFFieldType
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.Options
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

private fun parseInput(input: String): Map<String, String> {
    val assignments = input.split(';')
    val variablesAndValues = mutableMapOf<String, String>()

    assignments.forEach {
        val tokens = it.split('=')
        variablesAndValues[tokens[0]] = tokens[1]
    }

    return variablesAndValues
}

private fun printOutputFields(fields: List<DSPFField>) {
    print("OUTPUT fields:\t")
    fields.filter { it.type == DSPFFieldType.OUTPUT }.forEach {
        print("${it.name} = ${(it.value as Value).asString().value}")
    }
    print("\t")
}

private fun askInputFor(fields: List<DSPFField>): Map<String, Value> {
    val variablesAndValues = mutableMapOf<String, Value>()
    val line = readln()
    val updatedVariables = parseInput(line)

    fields.filter { it.type == DSPFFieldType.INPUT && updatedVariables[it.name] != null }.forEach {
        if (it.isNumeric && it.precision!! == 0)
            variablesAndValues[it.name] = IntValue(updatedVariables[it.name]!!.toLong())
        if (it.isNumeric && it.precision!! > 0)
            variablesAndValues[it.name] = DecimalValue(updatedVariables[it.name]!!.toBigDecimal())
        else if (!it.isNumeric)
            variablesAndValues[it.name] = StringValue(updatedVariables[it.name]!!)
    }

    return variablesAndValues
}

private fun onExfmt(fields: List<DSPFField>, runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
    printOutputFields(fields)
    while (true) {
        try {
            val variablesAndValues = askInputFor(fields)
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
    val programFinders = listOf(DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg")!!.path)),)
    val program = getProgram(nameOrSource = programSource, programFinders = programFinders)

    program.singleCall(emptyList(), configuration = createConfig())
}