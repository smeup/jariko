/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.performance

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
    Kute10_50.kt is the "hybrid" way to perform the similar MUTE10_50.rpgle DO loop code.

    - (MUTE10_50.rpgle)
        C                   DO        10000000
        C     FACTOR1       CAT       FACTOR2       RESULT
        C                   ENDDO

    - (Kute10_50.kt)
        while (myIterValue <= endLimit()) {
            try {
                exec_CAT(statement)
            } catch (e: IterException) {
                // nothing to do here
            }
            loopCounter++
            myIterValue++
        }

    "Hybrid" means it uses both Kotlin technology and the Jariko intepreter implementation, so, for example
    the increment of the variable checked for each iteration is done using "EvalStmt" (not simply the plus "+" operator)
    and the value of the variable is stored (read/write) in globalSymbolTable (SymbolTable) Map.
    This approach should be useful for developer to be more efficient to develop/debug code due to
    performance tuning, in a more concise developing environment.

 */

class Kute10_50 : Kute() {

    private val expectedElapsedTimeInMillisec = 150L
    private var loopCounter = 0L
    private var expectedIterations = 10000000L

    fun performanceComparing(): Array<String> {
        return performanceComparing(false)
    }

    fun performanceComparing(doAsserts: Boolean): Array<String> {

        // Variables declaration-instantiation
        val factor1Ref = DataDefinition("FACTOR1", StringType(6, false))
        val factor1DataRefExpr = DataRefExpr(ReferenceByName("FACTOR1", factor1Ref))

        val factor2Ref = DataDefinition("FACTOR2", StringType(10, false))
        val factor2DataRefExpr = DataRefExpr(ReferenceByName("FACTOR2", factor2Ref))

        val resultRef = DataDefinition("RESULT", StringType(20, false))
        val resultDataRefExpr = DataRefExpr(ReferenceByName("RESULT", resultRef))

        // Variables evaluation-storing
        globalSymbolTable[factor1Ref] = StringValue("HELLO")
        globalSymbolTable[factor2Ref] = StringValue("WORLD!    ")
        globalSymbolTable[resultRef] = StringValue("XXXXXXXXXXXXXXXXXXXX")

        // Statement creation (similar to 'toAst' does)
        val statement = catStmt(factor1DataRefExpr, factor2DataRefExpr, resultDataRefExpr, 0, null)

        // Perform a pure kotlin loop
        var actualElapsedTimeInMillisec: Long = measureTimeMillis {
            loopCounter = 0L
            while (loopCounter < expectedIterations) {
                exec_CAT(statement)
                loopCounter++
            }
        }
        // Results
        val message1 = "(Kotlin pure loop) Expected execution of 10000000 iterations, actual is $loopCounter iterations."
        val message2 = "(Kotlin pure loop) Expected execution of 10000000 iterations ($loopCounter done) takes less or same to " +
                "$expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms. (CAT result is ${globalSymbolTable[resultRef].asString()})"
        if (doAsserts) {
            assertEquals(expectedIterations, loopCounter, message1)
            assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message2)
        }

        // Perform a Jariko loop (DO statement)
        val intLiteral = IntLiteral(1)
        val endLimitExpression = IntLiteral(expectedIterations)
        val endLimit: () -> Long = optimizedIntExpression(endLimitExpression)
        actualElapsedTimeInMillisec = measureTimeMillis {
            // The Jariko 'DO' implementation
            exec_DO(intLiteral, endLimit, statement)
        }

        // Results
        val message3 = "(Jariko DO loop) Expected execution of ${endLimitExpression.value} iterations, actual is $loopCounter iterations."
        val message4 = "(Jariko DO loop) Expected execution of ${endLimitExpression.value} iterations ($loopCounter done) takes less or same to " +
                "$expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms. (CAT result is ${globalSymbolTable[resultRef].asString()})"
        if (doAsserts) {
            assertEquals(endLimitExpression.value, loopCounter, message3)
            assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message4)
        }

        return arrayOf(message1, message2, message3, message4)
    }

    private fun exec_DO(intLiteral: IntLiteral, endLimit: () -> Long, statement: CatStmt) {
        var myIterValue = eval(intLiteral).asInt().value
        loopCounter = 0L
        while (myIterValue <= endLimit()) {
            try {
                exec_CAT(statement)
            } catch (e: IterException) {
                // nothing to do here
            }
            loopCounter++
            myIterValue++
        }
    }

    private fun exec_CAT(statement: CatStmt) {
        val blanksInBetween = statement.blanksInBetween
        val blanks = StringValue.blank(blanksInBetween)
        val factor2 = eval(statement.right)
        var result = eval(statement.target)
        val resultLen = result.asString().length()
        val concatenatedFactors: Value

        if (null != statement.left) {
            val factor1 = eval(statement.left!!)
            val f1Trimmed = (factor1 as StringValue).value.trim()
            val factor1Trimmed = StringValue(f1Trimmed)
            concatenatedFactors = if (blanksInBetween > 0) {
                factor1Trimmed.concatenate(blanks).concatenate(factor2)
            } else {
                factor1.concatenate(factor2)
            }
        } else {
            concatenatedFactors = if (!result.asString().isBlank()) {
                result
            } else if (blanksInBetween > 0) {
                if (blanksInBetween >= resultLen) {
                    result
                } else {
                    blanks.concatenate(factor2)
                }
            } else {
                result
            }
        }
        val concatenatedFactorsLen = concatenatedFactors.asString().length()
        result = if (concatenatedFactorsLen >= resultLen) {
            concatenatedFactors.asString().getSubstring(0, resultLen)
        } else {
            concatenatedFactors.concatenate(result.asString().getSubstring(concatenatedFactorsLen, resultLen))
        }
        assign(statement.target, result)
    }

    private fun catStmt(left: Expression?, right: Expression, target: AssignableExpression, blanksInBetween: Int, position: Position? = null): CatStmt {
        return CatStmt(
                left = left,
                right = right,
                target = target,
                blanksInBetween = blanksInBetween,
                position = position)
    }
}

fun main() {
    Kute10_50().performanceComparing().forEach { println(it) }
}