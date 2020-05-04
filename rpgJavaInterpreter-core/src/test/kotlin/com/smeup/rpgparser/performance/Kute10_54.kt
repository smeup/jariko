package com.smeup.rpgparser.performance

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
    Kute10_54.kt is the "hybrid" way to perform the similar MUTE10_54.rpgle FOR loop code.

    - (MUTE10_54.rpgle)
        C                   FOR       $X=1 TO 10000000
        C                   ENDFOR

    - (Kute10_54.kt)
        while (enterCondition(this[statement.iterDataDefinition()],
                                        eval(statement.endValue),
                                        statement.downward))
        {
            try {
            // nothing to do here
            } catch (e: IterException) {
            // nothing to do here
            }
            increment(statement.iterDataDefinition(), step(statement.byValue, statement.downward))
            loopCounter++
        }

    "Hybrid" means it uses both Kotlin technology and the Jariko interpreter implementation, so, for example
    the increment of the variable checked for each iteration is done using "EvalStmt" (not simply the plus "+" operator)
    and the value of the variable is stored (read/write) in globalSymbolTable (SymbolTable) Map.
    This approach should be useful for developer to be more efficient to develop/debug code due to
    performance tuning, in a more concise developing environment.
 */

class Kute10_54 : Kute() {

    private val expectedElapsedTimeInMillisec = 306L
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
        var initDataRefExpr = DataRefExpr(variable)
        val intLiteral = IntLiteral(1)
        var init = AssignmentExpr(initDataRefExpr, intLiteral, null)

        // Store the variable
        globalSymbolTable[referred] = IntValue.ONE.asInt()

        // Create the ForStmt (similar to 'toAst' does)
        val endValue = IntLiteral(10000000)
        val byValue = IntLiteral(1)
        val downward: Boolean = false
        val body: List<Statement> = ArrayList()
        val position: Position? = null
        var statement = forStmt(init, endValue, byValue, downward, body, position)

        val actualElapsedTimeInMillisec = measureTimeMillis {
            // 'FOR' implementation
            exec_FOR(statement)
        }

        // Results
        var message1 = "Expected execution of ${eval(statement.endValue).asInt()} iterations, actual is $loopCounter iterations."
        var message2 = "Expected execution of ${eval(statement.endValue).asInt()} iterations ($loopCounter done) takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        if (doAsserts) {
            assertEquals(eval(statement.endValue).asInt(), loopCounter.asValue().asInt(), message1)
            assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message2)
        }

        return arrayOf(message1, message2)
    }

    private fun exec_FOR(statement: ForStmt) {
        while (enterCondition(this[statement.iterDataDefinition()],
                        eval(statement.endValue),
                        statement.downward)) {
            try {
                // nothing to do here
            } catch (e: IterException) {
                // nothing to do here
            }
            increment(statement.iterDataDefinition(), step(statement.byValue, statement.downward))
            loopCounter++
        }
    }

    private fun forStmt(init: Expression, endValue: Expression, byValue: Expression, downward: Boolean, body: List<Statement>, position: Position?): ForStmt {
        return ForStmt(
                init,
                endValue,
                byValue,
                downward,
                body,
                position)
    }
}

fun main(args: Array<String>) {
    Kute10_54().performanceComparing().forEach { println(it) }
}