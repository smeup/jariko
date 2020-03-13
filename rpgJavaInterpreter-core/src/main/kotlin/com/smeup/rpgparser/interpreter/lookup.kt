package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.ArrayAccessExpr
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.LookupStmt
import com.smeup.rpgparser.utils.fromIndex
import java.nio.charset.Charset

sealed class LookupSearchResult
object NotFound : LookupSearchResult()
class FoundAtIndex(val oneBasedIndex: Int) : LookupSearchResult()

class ArraySearchingParameters(
    private val arrayValue: ArrayValue,
    private val oneBasedIndex: Int,
    val indexVar: DataRefExpr?
) {
    fun lookup(valueToSearch: Value): LookupSearchResult {
        val zeroBasedIndex = lookupZeroBased(valueToSearch, arrayValue, oneBasedIndex - 1)
        return if (zeroBasedIndex == null) {
            NotFound
        } else {
            FoundAtIndex(zeroBasedIndex + 1)
        }
    }

    private fun lookupZeroBased(valueToSearch: Value, factor2: ArrayValue, zeroBasedStartingIndex: Int): Int? {
        val found = factor2.elements().fromIndex(zeroBasedStartingIndex).indexOfFirst {
            areEquals(it, valueToSearch)
        }
        return if (found == -1) null else found + zeroBasedStartingIndex
    }
}

fun LookupStmt.arraySearchingParameters(interpreterCore: InterpreterCore) =
    // ArrayValue...
    if (right is DataRefExpr) {
        ArraySearchingParameters(interpreterCore.interpret(right).asArray(), 1, null)
    } else {
        // ... or ArrayAccessExpr (ie. factor2(index) )
        val arrayAccessExpr = (right as ArrayAccessExpr)
        val indexExpression = arrayAccessExpr.index
        val oneBasedIndex = interpreterCore.eval(indexExpression).asInt().value.toInt()
        val dataRef = if (indexExpression is DataRefExpr) indexExpression else null
        ArraySearchingParameters(
            interpreterCore.interpret(right.array) as ArrayValue,
            oneBasedIndex,
            dataRef
        )
    }

fun lookUp(statement: LookupStmt, interpreterCore: InterpreterCore, charset: Charset) {
    println(charset)
    // TODO
    // - add more MUTE tests;
    // - set var argument (if present) of factor2 with index of found element (ie. FACTOR1  LOOKUP  FACTOR2(var) );
    // - check/handle searches due to to ascend/descend array declaration;
    // - test performance
    val factor1 = interpreterCore.interpret(statement.left)

    val arraySearchingParameters = statement.arraySearchingParameters(interpreterCore)

    val searchResult = arraySearchingParameters.lookup(factor1)
    if (searchResult is FoundAtIndex) {
        interpreterCore.setPredefinedIndicators(
            statement,
            BooleanValue.FALSE,
            BooleanValue.FALSE,
            BooleanValue.TRUE
        )
        arraySearchingParameters.indexVar?.let {
            interpreterCore.assign(it, searchResult.oneBasedIndex.asValue())
        }
    } else {
        interpreterCore.setPredefinedIndicators(
            statement,
            BooleanValue.FALSE,
            BooleanValue.FALSE,
            BooleanValue.FALSE
        )
    }

//    // factor1 not found into factor2 array.
//    if (found.asInt().value == 0L) {
//        // if no index (as array argument) specified
//        if (index <= 0L) {
//            interpreterCore.setPredefinedIndicators(
//                statement,
//                BooleanValue.TRUE,
//                BooleanValue.TRUE,
//                BooleanValue.FALSE
//            )
//        } else {
//            // Search for an element into factor2 GREATER (cause of statement.hi) than factor1,
//            // starting from index given as factor2 argument.
//            // ATTENTION: can't 'statement.hi' and 'statement.lo' be declared both
//            var foundWalkingDown = false
//            var foundWalkingUp = false
//            val idx = index.toInt()
//
//            if (null != statement.hi) {
//                // search direction: DOWN
//                for (x in (idx - 1) downTo 1) {
//                    val comparison = factor2.getElement(x).compare(factor1, charset)
//                    if (comparison > 0) {
//                        interpreterCore.setPredefinedIndicators(
//                            statement,
//                            BooleanValue.TRUE,
//                            BooleanValue.FALSE,
//                            BooleanValue.FALSE
//                        )
//                        foundWalkingDown = true
//                        break
//                    }
//                }
//                // search direction: UP
//                if (!foundWalkingDown) {
//                    val nrElements = factor2.asArray().elements().size
//                    for (x in (idx + 1)..nrElements) {
//                        val comparison = factor2.getElement(x).compare(factor1, charset)
//                        if (comparison > 0) {
//                            interpreterCore.setPredefinedIndicators(
//                                statement,
//                                BooleanValue.TRUE,
//                                BooleanValue.FALSE,
//                                BooleanValue.FALSE
//                            )
//                            foundWalkingUp = true
//                            break
//                        }
//                    }
//                }
//            } else if (null != statement.lo) {
//                // Search for an element into factor2 LOWER (cause of statement.lo) than factor1,
//                // starting from index given as factor2 argument.
//                // search direction: DOWN
//                for (x in (idx - 1) downTo 1) {
//                    val comparison = factor2.getElement(x).compare(factor1, charset)
//                    if (comparison < 0) {
//                        interpreterCore.setPredefinedIndicators(
//                            statement,
//                            BooleanValue.FALSE,
//                            BooleanValue.TRUE,
//                            BooleanValue.FALSE
//                        )
//                        foundWalkingDown = true
//                        break
//                    }
//                }
//                // search direction: UP
//                if (!foundWalkingDown) {
//                    val nrElements = factor2.asArray().elements().size
//                    for (x in (idx + 1)..nrElements) {
//                        val comparison = factor2.getElement(x).compare(factor1, charset)
//                        if (comparison < 0) {
//                            interpreterCore.setPredefinedIndicators(
//                                statement,
//                                BooleanValue.FALSE,
//                                BooleanValue.TRUE,
//                                BooleanValue.FALSE
//                            )
//                            foundWalkingUp = true
//                            break
//                        }
//                    }
//                }
//            }
//
//            // Not smaller and not greater element found
//            if (!foundWalkingDown && !foundWalkingUp) {
//                interpreterCore.setPredefinedIndicators(
//                    statement,
//                    BooleanValue.FALSE,
//                    BooleanValue.FALSE,
//                    BooleanValue.FALSE
//                )
//            }
//        }
//    } else {
//        // Factor1 found
//        // Indicators: HI=*OFF, LO=*OFF, EQ=*ON
//        if ((index > 0L && found.asInt().value == index) ||
//            (found.asInt().value > 0 && index == 0L)
//        ) {
//            interpreterCore.setPredefinedIndicators(
//                statement,
//                BooleanValue.FALSE,
//                BooleanValue.FALSE,
//                BooleanValue.TRUE
//            )
//        }
//
//        // Indicators: HI=*OFF, LO=*ON, EQ=*OFF
//        if (found.asInt().value < index) {
//            interpreterCore.setPredefinedIndicators(
//                statement,
//                BooleanValue.FALSE,
//                BooleanValue.TRUE,
//                BooleanValue.FALSE
//            )
//        }
//
//        // Indicators: HI=*ON, LO=*OFF, EQ=*OFF
//        if (found.asInt().value > index && index > 0L) {
//            interpreterCore.setPredefinedIndicators(
//                statement,
//                BooleanValue.TRUE,
//                BooleanValue.FALSE,
//                BooleanValue.FALSE
//            )
//        }
//    }
}
