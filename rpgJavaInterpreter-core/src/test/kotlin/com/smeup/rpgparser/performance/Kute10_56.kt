package com.smeup.rpgparser.performance

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.strumenta.kolasu.model.ReferenceByName
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
    Kute10_56.kt is the "hybrid" way to perform the similar MUTE10_56.rpgle DOU (do-until) loop code.

    - (MUTE10_56.rpgle)
        C                   DOU       $X=10000001
        C                   EVAL      $X=$X+1
        C                   ENDDO

    - (Kute10_56.kt)
        do {
            execute(statement.body)
            loopCounter++
        } while (!eval(statement.endExpression).asBoolean().value)

    "Hybrid" means it uses both Kotlin technology and the Jariko intepreter implementation, so, for example
    the increment of the variable checked for each iteration is done using "EvalStmt" (not simply the plus "+" operator)
    and the value of the variable is stored (read/write) in globalSymbolTable (SymbolTable) Map.
    This approach should be useful for developer to be more efficient to develop/debug code due to
    performance tuning, in a more concise developing environment.

 */

class Kute10_56 : Kute() {

    private val expectedElapsedTimeInMillisec = 81L
    private var loopCounter = 0L

    fun performanceComparing(): Array<String> {
        return performanceComparing(false)
    }

    fun performanceComparing(doAsserts: Boolean): Array<String> {

        // The variable name, 'X' instead of '$X' due to Kotlin syntax
        val name = "X"
        val type = NumberType(10, 0, "I")
        var referred: DataDefinition = DataDefinition(name, type)
        var variable: ReferenceByName<AbstractDataDefinition> = ReferenceByName("X", referred)
        var dataRefExpr = DataRefExpr(variable)

        // Store the variable
        globalSymbolTable[referred] = IntValue.ZERO.asInt()

        // Create the EvalStmt (similar to 'toAst' does)
        val rightInt = IntLiteral(10000000)
        val equalityExpr = EqualityExpr(dataRefExpr, rightInt)
        val intLiteral = IntLiteral(1)
        val plusExpr = PlusExpr(dataRefExpr, intLiteral)
        var statement = evalStmt(dataRefExpr, plusExpr, NORMAL_ASSIGNMENT)

        val actualElapsedTimeInMillisec = measureTimeMillis {
            // 'DOU' implementation
            exec_DOU(equalityExpr, statement)
        }

        // Results
        var message1 = "Expected execution of ${rightInt.value} iterations, actual is $loopCounter iterations."
        var message2 = "Expected execution of ${rightInt.value} iterations ($loopCounter done) takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        if (doAsserts) {
            assertEquals(rightInt.value, loopCounter, message1)
            assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message2)
        }

        return arrayOf(message1, message2)
    }

    private fun exec_DOU(equalityExpr: Expression, evalStatement: EvalStmt) {
        do {
            execute(evalStatement)
            loopCounter++
        } while (!eval(equalityExpr).asBoolean().value)
    }

    private fun evalStmt(dataRefExpr: DataRefExpr, plusExpr: Expression, plusAssignmentOperator: AssignmentOperator): EvalStmt {
        val extenders = charArrayOf('H', 'M', 'R')
        val flags = EvalFlags(
                halfAdjust = 'H' in extenders,
                maximumNumberOfDigitsRule = 'M' in extenders,
                resultDecimalPositionRule = 'R' in extenders
        )
        return EvalStmt(
                dataRefExpr,
                plusExpr,
                plusAssignmentOperator,
                flags = flags,
                position = null)
    }
}

fun main(args: Array<String>) {
    Kute10_56().performanceComparing().forEach { println(it) }
}