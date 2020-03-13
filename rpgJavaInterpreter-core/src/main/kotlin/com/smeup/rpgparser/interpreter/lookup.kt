package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.ArrayAccessExpr
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.LookupStmt
import com.smeup.rpgparser.parsing.ast.WithRightIndicators
import com.smeup.rpgparser.utils.fromIndex
import java.nio.charset.Charset

sealed class LookupSearchResult {
    abstract fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt)
}

object NotFound : LookupSearchResult() {
    override fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt) =
        interpreterCore.setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.FALSE)
}

class FoundAtIndex(val oneBasedIndex: Int) : LookupSearchResult() {
    override fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt) =
        interpreterCore.setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
}

class LowerAtIndex(val oneBasedIndex: Int) : LookupSearchResult() {
    override fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt) =
        interpreterCore.setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
}

class HigherAtIndex(val oneBasedIndex: Int) : LookupSearchResult() {
    override fun setIndicators(interpreterCore: InterpreterCore, statement: LookupStmt) =
        interpreterCore.setPredefinedIndicators(statement, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
}

class ArraySearchingParameters(
    private val arrayValue: ArrayValue,
    private val oneBasedIndex: Int,
    private val withRightIndicators: WithRightIndicators,
    private val charset: Charset,
    val indexVar: DataRefExpr?
) {
    fun lookup(valueToSearch: Value): LookupSearchResult {
        val zeroBasedIndex = lookupZeroBased(valueToSearch, arrayValue, oneBasedIndex - 1)
        if (zeroBasedIndex != null) {
            return FoundAtIndex(zeroBasedIndex + 1)
        }

        withRightIndicators.lo?.let {
            val zeroBasedIndex = lookupLowerZeroBased(valueToSearch, arrayValue, oneBasedIndex - 1)
            if (zeroBasedIndex != null) {
                return LowerAtIndex(zeroBasedIndex + 1)
            }
        }

        withRightIndicators.hi?.let {
            val zeroBasedIndex = lookupHigherZeroBased(valueToSearch, arrayValue, oneBasedIndex - 1)
            if (zeroBasedIndex != null) {
                return HigherAtIndex(zeroBasedIndex + 1)
            }
        }

        return NotFound
    }

    private fun lookupZeroBased(valueToSearch: Value, factor2: ArrayValue, zeroBasedStartingIndex: Int): Int? {
        val found = factor2.elements().fromIndex(zeroBasedStartingIndex).indexOfFirst {
            areEquals(it, valueToSearch)
        }
        return if (found == -1) null else found + zeroBasedStartingIndex
    }

    private fun lookupLowerZeroBased(valueToSearch: Value, factor2: ArrayValue, zeroBasedStartingIndex: Int): Int? {
        val searchedArray = factor2.elements().fromIndex(zeroBasedStartingIndex)
        var found = -1
        for (i in searchedArray.indices) {
            if (compare(valueToSearch, searchedArray[i], charset) == Comparison.SMALLER) {
                found = i
            } else {
                break
            }
        }
        return if (found == -1) null else found + zeroBasedStartingIndex
    }

    private fun lookupHigherZeroBased(valueToSearch: Value, factor2: ArrayValue, zeroBasedStartingIndex: Int): Int? {
        TODO()
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
    // - set var argument (if present) of factor2 with index of found element (ie. FACTOR1  LOOKUP  FACTOR2(var) );
    // - check/handle searches due to to ascend/descend array declaration;
    // - test performance
    val factor1 = interpreterCore.interpret(statement.left)

    val arraySearchingParameters = statement.arraySearchingParameters(interpreterCore, charset)
    val searchResult = arraySearchingParameters.lookup(factor1)
    searchResult.setIndicators(interpreterCore, statement)
    if (searchResult is FoundAtIndex) {
        arraySearchingParameters.indexVar?.let {
            interpreterCore.assign(it, searchResult.oneBasedIndex.asValue())
        }
    }
    if (searchResult is LowerAtIndex) {
        arraySearchingParameters.indexVar?.let {
            interpreterCore.assign(it, searchResult.oneBasedIndex.asValue())
        }
    }
    if (searchResult is HigherAtIndex) {
        arraySearchingParameters.indexVar?.let {
            interpreterCore.assign(it, searchResult.oneBasedIndex.asValue())
        }
    }
}
