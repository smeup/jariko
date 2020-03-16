package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.ArrayAccessExpr
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.LookupStmt
import com.smeup.rpgparser.parsing.ast.WithRightIndicators
import com.smeup.rpgparser.utils.fromIndex
import com.smeup.rpgparser.utils.toIndex
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

class FoundAtIndex(override val oneBasedIndex: Int, val hi: BooleanValue, val lo: BooleanValue, val eq: BooleanValue) : LookupSearchResult() {
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
        val zeroBasedStartingIndex = oneBasedIndex - 1
        val searchedArray = arrayValue.elements().fromIndex(zeroBasedStartingIndex)
        val foundIndexes = FoundIndexes(-1, -1, -1)

        val (indices, goingForward) = if (withRightIndicators.lo != null) Pair(searchedArray.indices.reversed(), false) else Pair(searchedArray.indices, true)
//        val indices = searchedArray.indices
        for (i in indices) {
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
//            if (goingForward && comparison == Comparison.GREATER) break
//            if (!goingForward && comparison == Comparison.SMALLER) break
        }
        return if (!foundIndexes.found()) NotFound else FoundAtIndex(foundIndexes.foundIndex() + zeroBasedStartingIndex + 1, foundIndexes.hiBooleanValue(), foundIndexes.loBooleanValue(), foundIndexes.eqBooleanValue())
    }
}

fun LookupStmt.arraySearchingParameters(interpreterCore: InterpreterCore, charset: Charset) =
    // ArrayValue...
    if (right is DataRefExpr) {
        ArraySearchingParameters(interpreterCore.interpret(right).asArray(), 1, rightIndicators, charset, null)
    } else {
        // ... or ArrayAccessExpr (ie. factor2(index) )
        val arrayAccessExpr = (right as ArrayAccessExpr)
        val indexExpression = arrayAccessExpr.index
        val oneBasedIndex = interpreterCore.eval(indexExpression).asInt().value.toInt()
        val dataRef = if (indexExpression is DataRefExpr) indexExpression else null
        ArraySearchingParameters(
            interpreterCore.interpret(right.array) as ArrayValue,
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
    val factor1 = interpreterCore.interpret(statement.left)

    val arraySearchingParameters = statement.arraySearchingParameters(interpreterCore, charset)
    val searchResult = arraySearchingParameters.lookup(factor1)
    searchResult.setIndicators(interpreterCore, statement)
    arraySearchingParameters.indexVar?.let {
            interpreterCore.assign(it, searchResult.oneBasedIndex.asValue())
    }
}

class FoundIndexes(var hi: Int, var lo: Int, var eq: Int) {
    fun found(): Boolean = hi >=0 || lo >=0 || eq >= 0
    fun hiBooleanValue() = if (hi >=0) BooleanValue.TRUE else BooleanValue.FALSE
    fun loBooleanValue() = if (lo >=0) BooleanValue.TRUE else BooleanValue.FALSE
    fun eqBooleanValue() = if (eq >=0) BooleanValue.TRUE else BooleanValue.FALSE
    fun foundIndex(): Int = when {
        eq >=0 -> eq
        hi >=0 -> hi
        lo >=0 -> lo
        else -> -1
    }
}
