package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import org.junit.Test
import kotlin.test.assertEquals

class ExpressionEvaluationTest {

    @Test
    fun evaluateStringLiteral() {
        assertEquals(StringValue("hello"), interpret(StringLiteral("hello")))
    }

    private fun interpret(expr: Expression) : Value {
        val systemInterface = object : SystemInterface {

        }
        return Interpreter(systemInterface).interpret(expr)
    }

}
