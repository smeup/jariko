package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.ArrayAccessExpr
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.LookupStmt
import java.nio.charset.Charset

fun lookUp(statement: LookupStmt, interpreterCore: InterpreterCore, charset: Charset) {
    // TODO
    // - add more MUTE tests;
    // - set var argument (if present) of factor2 with index of found element (ie. FACTOR1  LOOKUP  FACTOR2(var) );
    // - check/handle searches due to to ascend/descend array declaration;
    // - test performance
    val factor1 = interpreterCore.interpret(statement.left)

    // If ArrayValue or ArrayExpression (ie. factor2(index) )
    var factor2 = if (statement.right is DataRefExpr) {
        interpreterCore.interpret(statement.right).asArray()
    } else {
        interpreterCore.interpret((statement.right as ArrayAccessExpr).array) as ArrayValue
    }

    // If index as ArrayExpression argument (ie. factor2(index) )
    var index = if (statement.right is DataRefExpr) {
        IntValue.ZERO.value
    } else {
        interpreterCore.eval((statement.right as ArrayAccessExpr).index).asInt().value
    }

    // execute search
    val found = lookup(factor1, factor2)

    // factor1 not found into factor2 array.
    if (found.asInt().value == 0L) {
        // if no index (as array argument) specified
        if (index <= 0L) {
            interpreterCore.setPredefinedIndicators(
                statement,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.FALSE
            )
        } else {
            // Search for an element into factor2 GREATER (cause of statement.hi) than factor1,
            // starting from index given as factor2 argument.
            // ATTENTION: can't 'statement.hi' and 'statement.lo' be declared both
            var foundWalkingDown = false
            var foundWalkingUp = false
            val idx = index.toInt()

            if (null != statement.hi) {
                // search direction: DOWN
                for (x in (idx - 1) downTo 1) {
                    val comparison = factor2.getElement(x).compare(factor1, charset)
                    if (comparison > 0) {
                        interpreterCore.setPredefinedIndicators(
                            statement,
                            BooleanValue.TRUE,
                            BooleanValue.FALSE,
                            BooleanValue.FALSE
                        )
                        foundWalkingDown = true
                        break
                    }
                }
                // search direction: UP
                if (!foundWalkingDown) {
                    val nrElements = factor2.asArray().elements().size
                    for (x in (idx + 1)..nrElements) {
                        val comparison = factor2.getElement(x).compare(factor1, charset)
                        if (comparison > 0) {
                            interpreterCore.setPredefinedIndicators(
                                statement,
                                BooleanValue.TRUE,
                                BooleanValue.FALSE,
                                BooleanValue.FALSE
                            )
                            foundWalkingUp = true
                            break
                        }
                    }
                }
            } else if (null != statement.lo) {
                // Search for an element into factor2 LOWER (cause of statement.lo) than factor1,
                // starting from index given as factor2 argument.
                // search direction: DOWN
                for (x in (idx - 1) downTo 1) {
                    val comparison = factor2.getElement(x).compare(factor1, charset)
                    if (comparison < 0) {
                        interpreterCore.setPredefinedIndicators(
                            statement,
                            BooleanValue.FALSE,
                            BooleanValue.TRUE,
                            BooleanValue.FALSE
                        )
                        foundWalkingDown = true
                        break
                    }
                }
                // search direction: UP
                if (!foundWalkingDown) {
                    val nrElements = factor2.asArray().elements().size
                    for (x in (idx + 1)..nrElements) {
                        val comparison = factor2.getElement(x).compare(factor1, charset)
                        if (comparison < 0) {
                            interpreterCore.setPredefinedIndicators(
                                statement,
                                BooleanValue.FALSE,
                                BooleanValue.TRUE,
                                BooleanValue.FALSE
                            )
                            foundWalkingUp = true
                            break
                        }
                    }
                }
            }

            // Not smaller and not greater element found
            if (!foundWalkingDown && !foundWalkingUp) {
                interpreterCore.setPredefinedIndicators(
                    statement,
                    BooleanValue.FALSE,
                    BooleanValue.FALSE,
                    BooleanValue.FALSE
                )
            }
        }
    } else {
        // Factor1 found
        // Indicators: HI=*OFF, LO=*OFF, EQ=*ON
        if ((index > 0L && found.asInt().value == index) ||
            (found.asInt().value > 0 && index == 0L)
        ) {
            interpreterCore.setPredefinedIndicators(
                statement,
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE
            )
        }

        // Indicators: HI=*OFF, LO=*ON, EQ=*OFF
        if (found.asInt().value < index) {
            interpreterCore.setPredefinedIndicators(
                statement,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.FALSE
            )
        }

        // Indicators: HI=*ON, LO=*OFF, EQ=*OFF
        if (found.asInt().value > index && index > 0L) {
            interpreterCore.setPredefinedIndicators(
                statement,
                BooleanValue.TRUE,
                BooleanValue.FALSE,
                BooleanValue.FALSE
            )
        }
    }
}

fun lookup(factor1: Value, factor2: ArrayValue): Value {
    val found = factor2.elements().indexOfFirst {
        areEquals(it, factor1)
    }
    return if (found == -1) 0.asValue() else (found + 1).asValue()
}
