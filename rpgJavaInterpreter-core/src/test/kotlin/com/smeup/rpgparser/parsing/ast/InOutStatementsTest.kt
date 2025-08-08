package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertStatementCanBeParsed
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class InOutStatementsTest : AbstractTest() {

    private fun statement(code: String): Statement {
        val stmtContext = assertStatementCanBeParsed("     C                   $code                                                          ")
        return stmtContext.toAst(ToAstConfiguration(considerPosition = false))
    }

    @test 
    fun testInStatementParsing() {
        val stmt: InStmt = statement("IN                    DATAARRAY") as InStmt
        assertEquals(null, stmt.factor1)
        assertTrue(stmt.factor2 is DataRefExpr)
        assertEquals("DATAARRAY", (stmt.factor2 as DataRefExpr).variable.name)
    }

    @test 
    fun testInStatementWithFactor1Parsing() {
        val stmt: InStmt = statement("IN        DEVICE      DATAARRAY") as InStmt
        assertTrue(stmt.factor1 is DataRefExpr)
        assertEquals("DEVICE", (stmt.factor1 as DataRefExpr).variable.name)
        assertTrue(stmt.factor2 is DataRefExpr)
        assertEquals("DATAARRAY", (stmt.factor2 as DataRefExpr).variable.name)
    }

    @test 
    fun testOutStatementParsing() {
        val stmt: OutStmt = statement("OUT                   DATAARRAY") as OutStmt
        assertEquals(null, stmt.factor1)
        assertTrue(stmt.factor2 is DataRefExpr)
        assertEquals("DATAARRAY", (stmt.factor2 as DataRefExpr).variable.name)
    }

    @test 
    fun testOutStatementWithFactor1Parsing() {
        val stmt: OutStmt = statement("OUT       DEVICE      DATAARRAY") as OutStmt
        assertTrue(stmt.factor1 is DataRefExpr)
        assertEquals("DEVICE", (stmt.factor1 as DataRefExpr).variable.name)
        assertTrue(stmt.factor2 is DataRefExpr)
        assertEquals("DATAARRAY", (stmt.factor2 as DataRefExpr).variable.name)
    }
}