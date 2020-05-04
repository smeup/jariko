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
        var factor1Ref: DataDefinition = DataDefinition("FACTOR1", StringType(6, false))
        var factor1DataRefExpr = DataRefExpr(ReferenceByName("FACTOR1", factor1Ref))

        var factor2Ref: DataDefinition = DataDefinition("FACTOR2", StringType(10, false))
        var factor2DataRefExpr = DataRefExpr(ReferenceByName("FACTOR2", factor2Ref))

        var resultRef: DataDefinition = DataDefinition("RESULT", StringType(20, false))
        var resultDataRefExpr = DataRefExpr(ReferenceByName("RESULT", resultRef))

        // Variables evaluation-storing
        globalSymbolTable[factor1Ref] = StringValue("HELLO")
        globalSymbolTable[factor2Ref] = StringValue("WORLD!    ")
        globalSymbolTable[resultRef] = StringValue("XXXXXXXXXXXXXXXXXXXX")

        // Statement creation (similar to 'toAst' does)
        var statement = catStmt(factor1DataRefExpr, factor2DataRefExpr, resultDataRefExpr, 0, null)

        // Perform a pure kotlin loop
        var actualElapsedTimeInMillisec = measureTimeMillis {
            while (loopCounter < expectedIterations) {
                exec_CAT(statement)
                loopCounter++
            }
        }
        // Results
        var message1 = "(Kotlin pure loop) Expected execution of 10000000 iterations, actual is $loopCounter iterations."
        var message2 = "(Kotlin pure loop) Expected execution of 10000000 iterations ($loopCounter done) takes less or same to " +
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
        var message3 = "(Jariko DO loop) Expected execution of ${endLimitExpression.value} iterations, actual is $loopCounter iterations."
        var message4 = "(Jariko DO loop) Expected execution of ${endLimitExpression.value} iterations ($loopCounter done) takes less or same to " +
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
        var concatenatedFactors: Value

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
                left,
                right,
                target,
                blanksInBetween,
                position)
    }
}

fun main(args: Array<String>) {
    Kute10_50().performanceComparing().forEach { println(it) }
}