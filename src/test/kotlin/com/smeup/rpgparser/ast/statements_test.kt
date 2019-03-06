package com.smeup.rpgparser.ast

import com.smeup.rpgparser.*
import kotlin.test.assertEquals
import org.junit.Test as test

class StatementsTest {

    private fun statement(code: String) : Statement {
        val stmtContext = assertStatementCanBeParsed("                                   $code")
        return stmtContext.toAst(considerPosition = false)
    }

    @test fun intLiteralParsing() {
        assertEquals(ExecuteSubroutine(SubroutineRef("EXSR")), statement("EXSR      IMP0"))
    }
//
//    @test fun stringLiteralParsing() {
//        assertEquals(StringLiteral("INZ"), expression("'INZ'"))
//    }
//
//    @test fun dataRefParsing() {
//        assertEquals(DataRefExpr(ReferenceByName("\$\$SVAR")), expression("\$\$SVAR"))
//    }
//
//    @test fun numberOfElementsParsing() {
//        assertEquals(NumberOfElementsExpr(DataRefExpr(ReferenceByName("\$\$SVAR"))), expression("%ELEM(\$\$SVAR)"))
//    }
//
//    @test fun equalityParsing() {
//        assertEquals(EqualityExpr(DataRefExpr(ReferenceByName("U\$FUNZ")), StringLiteral("INZ")), expression("U\$FUNZ='INZ'"))
//    }
//
//    @test fun greaterThanParsing() {
//        assertEquals(GreaterThanExpr(DataRefExpr(ReferenceByName("\$X")), IntLiteral(0)), expression("\$X>0"))
//    }
}
