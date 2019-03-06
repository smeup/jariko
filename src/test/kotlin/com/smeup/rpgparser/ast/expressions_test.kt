package com.smeup.rpgparser.ast

import com.smeup.rpgparser.*
import kotlin.test.assertEquals
import org.junit.Test as test

class ExpressionsTest {

    private fun expression(code: String) : Expression {
        val exprContext = assertExpressionCodeCanBeParsed(code)
        return exprContext.toAst(considerPosition = false)
    }

    @test fun intLiteralParsing() {
        assertEquals(IntLiteral(200), expression("200"))
    }

}
