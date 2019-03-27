package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import org.junit.Test
import kotlin.test.assertEquals

class ExpressionEvaluationTest {

    @Test
    fun evaluateStringLiteral() {
        assertEquals(StringValue("hello"), interpret(StringLiteral("hello")))
    }

    @Test
    fun evaluateIntLiteral() {
        assertEquals(IntValue(0), interpret(IntLiteral(0)))
        assertEquals(IntValue(20), interpret(IntLiteral(20)))
    }

    private fun interpret(expr: Expression) : Value {
        val systemInterface = object : SystemInterface {

        }
        return Interpreter(systemInterface).interpret(expr)
    }

}
