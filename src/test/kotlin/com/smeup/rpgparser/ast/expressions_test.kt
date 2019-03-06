package com.smeup.rpgparser.ast

import com.smeup.rpgparser.*
import me.tomassetti.kolasu.model.ReferenceByName
import kotlin.test.assertEquals
import org.junit.Test as test

class ExpressionsTest {

    private fun expression(code: String) : Expression {
        val exprContext = assertExpressionCodeCanBeParsed("                                   $code")
        return exprContext.toAst(considerPosition = false)
    }

    @test fun intLiteralParsing() {
        assertEquals(IntLiteral(200), expression("200"))
    }

    @test fun dataRefParsing() {
        assertEquals(DataRefExpr(ReferenceByName("\$\$SVAR")), expression("\$\$SVAR"))
    }

    @test fun numberOfElementsParsing() {
        assertEquals(NumberOfElementsExpr(DataRefExpr(ReferenceByName("\$\$SVAR"))), expression("%ELEM(\$\$SVAR)"))
    }

}
