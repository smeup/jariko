package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.ArrayAccessExpr
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.LookupStmt
import com.smeup.rpgparser.parsing.ast.WithRightIndicators
import java.nio.charset.Charset

sealed class LookupSearchResult {
    abstract val oneBasedIndex: Int
    abstract fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt)
}

object NotFound : LookupSearchResult() {
    override val oneBasedIndex = 1
    override fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt) =
        interpreterCore.setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.FALSE)
}

class FoundAtIndex(override val oneBasedIndex: Int, val hi: BooleanValue, val lo: BooleanValue, val eq: BooleanValue) :
    LookupSearchResult() {
    override fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt) =
        interpreterCore.setPredefinedIndicators(statement, hi, lo, eq)
}

class ArraySearchingParameters(
    private val arrayValue: ArrayValue,
    private val oneBasedIndex: Int,
    private val withRightIndicators: WithRightIndicators,
    private val charset: Charset,
    val indexVar: DataRefExpr?
) {
    fun lookup(valueToSearch: Value): LookupSearchResult {
        val searchedArray = arrayValue.elements()
        val zeroBasedStartingIndex = oneBasedIndex - 1
        val lastIndex = searchedArray.size - 1
        val foundIndexes = FoundIndexes(-1, -1, -1)

        val (indices, remainingIndices) = if (withRightIndicators.lo != null) {
            Pair(zeroBasedStartingIndex downTo 0, lastIndex downTo zeroBasedStartingIndex + 1)
        } else {
            Pair(zeroBasedStartingIndex..lastIndex, 0..zeroBasedStartingIndex - 1)
        }
        searchIn(indices, searchedArray, valueToSearch, foundIndexes)
        if (withRightIndicators.eq != null && foundIndexes.eq === -1 && (zeroBasedStartingIndex == 1 || zeroBasedStartingIndex == lastIndex)) {
            searchIn(remainingIndices, searchedArray, valueToSearch, foundIndexes)
        }
        return if (!foundIndexes.found()) NotFound else FoundAtIndex(
            foundIndexes.foundIndex() + 1,
            foundIndexes.hiBooleanValue(),
            foundIndexes.loBooleanValue(),
            foundIndexes.eqBooleanValue()
        )
    }

    private fun searchIn(
        idx: IntProgression,
        searchedArray: List<Value>,
        valueToSearch: Value,
        foundIndexes: FoundIndexes
    ) {
        for (i in idx) {
            val comparison = compare(searchedArray[i], valueToSearch, charset)
            if (withRightIndicators.eq != null) {
                if (comparison == Comparison.EQUAL) {
                    foundIndexes.eq = i
                    break
                }
            }
            if (withRightIndicators.lo != null) {
                if (comparison == Comparison.SMALLER) {
                    foundIndexes.lo = i
                    break
                }
            }
            if (withRightIndicators.hi != null) {
                if (comparison == Comparison.GREATER) {
                    foundIndexes.hi = i
                    break
                }
            }
        }
    }
}

fun LookupStmt.arraySearchingParameters(interpreterCore: InterpreterCore, charset: Charset) =
    // ArrayValue...
    if (right is DataRefExpr) {
        ArraySearchingParameters(interpreterCore.eval(right).asArray(), 1, rightIndicators, charset, null)
    } else {
        // ... or ArrayAccessExpr (ie. factor2(index) )
        val arrayAccessExpr = (right as ArrayAccessExpr)
        val indexExpression = arrayAccessExpr.index
        val oneBasedIndex = interpreterCore.eval(indexExpression).asInt().value.toInt()
        val dataRef = if (indexExpression is DataRefExpr) indexExpression else null
        ArraySearchingParameters(
            interpreterCore.eval(right.array) as ArrayValue,
            oneBasedIndex,
            rightIndicators,
            charset,
            dataRef
        )
    }

fun lookUp(statement: LookupStmt, interpreterCore: InterpreterCore, charset: Charset) {
    // TODO
    // - add more MUTE tests;
    // - check/handle searches due to to ascend/descend array declaration;
    // - test performance
    val factor1 = interpreterCore.eval(statement.left)

    val arraySearchingParameters = statement.arraySearchingParameters(interpreterCore, charset)
    val searchResult = arraySearchingParameters.lookup(factor1)
    searchResult.setIndicators(interpreterCore, statement)
    arraySearchingParameters.indexVar?.let {
        interpreterCore.assign(it, searchResult.oneBasedIndex.asValue())
    }
}

class FoundIndexes(var hi: Int, var lo: Int, var eq: Int) {
    fun found(): Boolean = hi >= 0 || lo >= 0 || eq >= 0
    fun hiBooleanValue() = if (hi >= 0) BooleanValue.TRUE else BooleanValue.FALSE
    fun loBooleanValue() = if (lo >= 0) BooleanValue.TRUE else BooleanValue.FALSE
    fun eqBooleanValue() = if (eq >= 0) BooleanValue.TRUE else BooleanValue.FALSE
    fun foundIndex(): Int = when {
        eq >= 0 -> eq
        hi >= 0 -> hi
        lo >= 0 -> lo
        else -> -1
    }
}
