package com.smeup.rpgparser.performance

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
    Kute10_53.kt is the "hybrid" way to perform the similar MUTE10_53.rpgle DO loop code.

    - (MUTE10_53.rpgle)
        C                   DO        10000000
        C                   ENDDO

    - (Kute10_53.kt)
        while (myIterValue <= endLimit()) {
            try {
                // nothing to do here
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

class Kute10_53 : Kute() {

    private val expectedElapsedTimeInMillisec = 68L
    private var loopCounter = 0L

    public fun performanceComparing(): Array<String> {
        val intLiteral = IntLiteral(1)
        val endLimitExpression = IntLiteral(10000000)
        val endLimit: () -> Long = optimizedIntExpression(endLimitExpression)

        val actualElapsedTimeInMillisec = measureTimeMillis {
            // 'DO' implementation
            exec_DO(intLiteral, endLimit)
        }

        // Results
        var message1 = "Expected execution of ${endLimitExpression.value} iterations ($loopCounter done) takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message1)
        var message2 = "Expected execution of ${endLimitExpression.value} iterations, actual is $loopCounter iterations."
        assertEquals(endLimitExpression.value, loopCounter, message2)

        return arrayOf(message1, message2)
    }

    private fun exec_DO(intLiteral: IntLiteral, endLimit: () -> Long) {
        var myIterValue = eval(intLiteral).asInt().value
        while (myIterValue <= endLimit()) {
            try {
                // nothing to do here
            } catch (e: IterException) {
                // nothing to do here
            }
            loopCounter++
            myIterValue++
        }
    }
}

fun main(args: Array<String>) {
    Kute10_53().performanceComparing().forEach { println(it) }
}