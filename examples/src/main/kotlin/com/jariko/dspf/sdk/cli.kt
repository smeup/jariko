package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.dspfparser.linesclassifier.DSPFFieldType
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.Value

// rendering

private const val MAX_COLUMNS = 80
private const val MAX_LINES = 24

private fun renderScreenHorizontalBorders() {
    print("+")
    for (i in 0..MAX_COLUMNS) {
        print("-")
    }
    print("+")
}

private fun renderScreenVerticalBorder() {
    print("|")
}

private fun renderBlankLines(from: Int, to: Int) {
    for (i in from..to) {
        if (i == from) {
            println()
        } else {
            print("|")
            for (j in 0..MAX_COLUMNS) {
                print(" ")
            }
            println("|")
        }
    }
}

private fun renderBlankColumns(from: Int, to: Int) {
    for (i in from..to) {
        print(" ")
    }
}

private fun renderOutputFields(fields: List<DSPFField>) {
    var previousLineNo = 1
    var currentLineNo: Int
    var fieldsOnSameLineNo: List<DSPFField>
    var previousColumnNo: Int
    var currentColumnNo: Int

    renderScreenHorizontalBorders()

    fields.groupBy { it.y }.toList().sortedBy { it.first }.forEach { group ->
        currentLineNo = group.first!!
        fieldsOnSameLineNo = group.second

        renderBlankLines(previousLineNo, currentLineNo)
        renderScreenVerticalBorder()

        previousColumnNo = 0
        fieldsOnSameLineNo.forEach { member ->
            val fakeConstField = "${member.name}: "
            val string = "$fakeConstField${(member.value as Value).asString().value}"
            // - 2 because in terminal x = 0 equals x = 1 in 5250, and the value of x is included (<= sign)
            currentColumnNo = member.x!! - 2 - fakeConstField.length

            renderBlankColumns(previousColumnNo, currentColumnNo)
            print(string)

            previousColumnNo = currentColumnNo + string.length + 1
        }

        renderBlankColumns(previousColumnNo, MAX_COLUMNS)
        renderScreenVerticalBorder()
        previousLineNo = currentLineNo + 1
    }

    renderBlankLines(previousLineNo, MAX_LINES)
    renderScreenHorizontalBorders()
}

private fun render(fields: List<DSPFField>) {
    Runtime.getRuntime().exec("clear")
    println()
    renderOutputFields(fields)
    println()
    print("INPUT fields: ")
    println("${fields.filter { it.type == DSPFFieldType.INPUT }.map { it.name }}")
    println()
    println("Insert input: ")
}

// UI controls

class UnknownVariable(name: String) : Exception("`$name`")
class WrongInputSyntax : Exception("Should be: `VAR1=VALUE;VAR2=23`")

private fun parseInput(input: String): Map<String, String> {
    try {
        if (input.isBlank() || input.isEmpty()) return emptyMap()

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

private fun updateValues(fields: List<DSPFField>): Map<String, Value> {
    val variablesAndValues = mutableMapOf<String, Value>()
    val line = readln()
    val updatedVariables = parseInput(line)

    if (updatedVariables.isEmpty()) return emptyMap()

    updatedVariables.keys.forEach { variable ->
        fields.find { field -> field.name == variable } ?: throw UnknownVariable(variable)
    }

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

fun ask(fields: List<DSPFField>): Map<String, Value> {
    while (true) {
        try {
            val variablesAndValues = updateValues(fields)
            return variablesAndValues
        } catch (e: Exception) {
            e.printStackTrace()
            continue
        }
    }
}

fun startVideoSession(fields: List<DSPFField>): Map<String, Value> {
    render(fields)
    return ask(fields)
}