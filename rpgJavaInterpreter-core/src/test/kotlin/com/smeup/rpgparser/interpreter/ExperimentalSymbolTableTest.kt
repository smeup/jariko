package com.smeup.rpgparser.interpreter

import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

private const val VARIABLES_NUMBER = 100
private const val ITERATIONS = 100_000

class ExperimentalSymbolTableTest {

    private val keys = AtomicInteger()

    val variables = List<Pair<DataDefinition, Value>>(VARIABLES_NUMBER) {
        Pair(
            DataDefinition(name = "VAR$it", type = StringType(20, false)),
            StringValue("$it")
        )
    }

    @Test
    fun setAndGet() {
        val experimentalSymbolTable: ISymbolTable = SymbolTable()
        for (variable in variables) {
            experimentalSymbolTable[variable.first] = variable.second
        }
        for (variable in variables) {
            assert(experimentalSymbolTable[variable.first] == variable.second)
            assert(experimentalSymbolTable[variable.first.name] == variable.second)
        }
    }

    @Test
    fun evalSymbolTablePerf() {
        println("evalSymbolTablePerf: ${setAndGetAll(SymbolTable())}")
    }

    @Test
    fun evalExperimentalSymbolTablePerf() {
        println("evalExperimentalSymbolTablePerf: ${setAndGetAll(com.smeup.rpgparser.experimental.SymbolTable())}")
    }

    private fun setAndGetAll(iSymbolTable: ISymbolTable): Long {
        return measureTimeMillis {
            for (i in 1..ITERATIONS) {
                callSet(iSymbolTable)
                callGet(iSymbolTable)
                callSet(iSymbolTable)
                callGet(iSymbolTable)
                callSet(iSymbolTable)
                callGet(iSymbolTable)
                callSet(iSymbolTable)
                callGet(iSymbolTable)
            }
        }
    }

    private fun callSet(iSymbolTable: ISymbolTable) {
        for (variable in variables) {
            iSymbolTable[variable.first] = variable.second
        }
    }

    private fun callGet(iSymbolTable: ISymbolTable) {
        for (variable in variables) {
            iSymbolTable[variable.first]
        }
    }
}

fun main() {
    val test = ExperimentalSymbolTableTest()
    for (i in 1..5) {
        test.evalSymbolTablePerf()
        test.evalExperimentalSymbolTablePerf()
    }
}
