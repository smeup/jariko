package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertStatementCanBeParsed
import com.smeup.rpgparser.dataRef
import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice.LR
import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice.RT
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.model.ReferenceByName
import com.strumenta.kolasu.model.collectByType
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.Test as test

open class StatementsTest : AbstractTest() {
    private fun statement(code: String): Statement {
        val stmtContext =
            assertStatementCanBeParsed("     C                   $code                                                          ")
        return stmtContext.toAst(ToAstConfiguration(considerPosition = false))
    }

    private fun multiLineStatement(code: String): Statement {
        val stmtContext = assertStatementCanBeParsed(code)
        return stmtContext.toAst(ToAstConfiguration(considerPosition = false))
    }

    @test fun lookupParsingWith3RightIndicatorShouldNotBeAllowed() {
        assertFailsWith(Throwable::class) {
            multiLineStatement(
                """
     C     'Z'           LOOKUP    ARRAY(N)                           706869                 
                    """,
            )
        }
    }

    @test fun gotoParsingWithIndicator() {
        val stmt: GotoStmt =
            multiLineStatement(
                """
     C  N50              GOTO      START            
                    """,
            ) as GotoStmt
        assertEquals("START", stmt.tag)
        assertEquals(50, stmt.indicatorCondition?.key)
        assertEquals(true, stmt.indicatorCondition?.negate)
    }

    @test fun gotoParsingWithoutIndicator() {
        val stmt: GotoStmt =
            multiLineStatement(
                """
     C                   GOTO      START            
                    """,
            ) as GotoStmt
        assertEquals("START", stmt.tag)
        assertEquals(null, stmt.indicatorCondition?.key)
    }

    @test fun compParsing() {
        val stmt: CompStmt =
            multiLineStatement(
                """
     C     A2            COMP      '01'                               50  51                    """,
            )
                as CompStmt
        assertEquals("A2", (stmt.left as DataRefExpr).variable.name)
        assertEquals("01", (stmt.right as StringLiteral).value)
        assertEquals(50, stmt.hi)
        assertEquals(null, stmt.lo)
        assertEquals(51, stmt.eq)
    }

    @test fun kListParsing() {
        assertEquals(
            KListStmt("Key", listOf("KY1TST", "KY2TST"), emptyList(), null),
            multiLineStatement(
                """
     C     Key           KLIST
     C                   KFLD                    KY1TST
     C                   KFLD                    KY2TST                        
                    """,
            ),
        )
    }

    @test fun executeSubroutineParsing() {
        assertEquals(ExecuteSubroutine(ReferenceByName("IMP0")), statement("EXSR      IMP0"))
    }

    @test fun evalParsing() {
        assertEquals(
            EvalStmt(
                dataRef("\$\$SVAR"),
                dataRef("U\$SVARSK"),
            ),
            statement("EVAL      \$\$SVAR=U\$SVARSK"),
        )
    }

    @test fun callParsing() {
        assertEquals(CallStmt(StringLiteral("JD_URL"), emptyList()), statement("CALL      'JD_URL'"))
    }

    @test fun ifParsingSimple() {
        assertEquals(
            IfStmt(
                GreaterThanExpr(
                    dataRef("\$X"),
                    IntLiteral(0),
                ),
                listOf(
                    EvalStmt(
                        dataRef("U\$IN35"),
                        StringLiteral("1"),
                    ),
                ),
            ),
            statement(
                "IF        \$X>0\n" +
                    "     C                   EVAL      U\$IN35='1'\n" +
                    "     C                   ENDIF",
            ),
        )
    }

    @test fun ifParsingEmpty() {
        assertEquals(
            IfStmt(
                GreaterThanExpr(
                    dataRef("\$X"),
                    IntLiteral(0),
                ),
                emptyList(),
            ),
            statement(
                "IF        \$X>0\n" +
                    "     C                   ENDIF",
            ),
        )
    }

    @test fun ifParsingWithElseSimple() {
        assertEquals(
            IfStmt(
                GreaterThanExpr(
                    dataRef("\$X"),
                    IntLiteral(0),
                ),
                listOf(
                    EvalStmt(
                        dataRef("U\$IN35"),
                        StringLiteral("1"),
                    ),
                ),
                emptyList(),
                ElseClause(
                    listOf(
                        EvalStmt(
                            dataRef("\$\$URL"),
                            FunctionCall(ReferenceByName("\$\$SVARVA"), listOf(dataRef("\$R"))),
                        ),
                    ),
                ),
            ),
            statement(
                "IF        \$X>0\n" +
                    "     C                   EVAL      U\$IN35='1'\n" +
                    "5    C                   ELSE\n" +
                    "     C                   EVAL      \$\$URL=\$\$SVARVA(\$R)\n" +
                    "     C                   ENDIF",
            ),
        )
    }

    @test fun selectEmptyParsing() {
        assertEquals(
            SelectStmt(emptyList()),
            statement(
                "SELECT\n" +
                    "1e   C                   ENDSL",
            ),
        )
    }

    @test fun selectSingleCaseParsing() {
        assertEquals(
            SelectStmt(
                listOf(
                    SelectCase(
                        EqualityExpr(dataRef("U\$FUNZ"), StringLiteral("INZ")),
                        listOf(ExecuteSubroutine(ReferenceByName("FINZ"))),
                    ),
                ),
            ),
            statement(
                "SELECT\n" +
                    "      * Init\n" +
                    "1x   C                   WHEN      U\$FUNZ='INZ'\n" +
                    "     C                   EXSR      FINZ\n" +
                    "1e   C                   ENDSL",
            ),
        )
    }

    @test fun selectParsingComplex() {
        val selectStmt =
            statement(
                "SELECT\n" +
                    "      * Init\n" +
                    "1x   C                   WHEN      U\$FUNZ='INZ'\n" +
                    "     C                   EXSR      FINZ\n" +
                    "      * Invoke URL\n" +
                    "1x   C                   WHEN      U\$FUNZ='EXE'\n" +
                    "     C                   EXSR      FEXE\n" +
                    "      * Detach (empty subroutine in this case)\n" +
                    "1x   C                   WHEN      U\$FUNZ='CLO'\n" +
                    "     C                   EXSR      FCLO\n" +
                    "1e   C                   ENDSL",
            ) as SelectStmt
        assertEquals(3, selectStmt.cases.size)
    }

    // TODO select with other

    @test fun setOnParsing() {
        assertEquals(SetStmt(SetStmt.ValueSet.ON, listOf(IndicatorExpr(LR))), statement("SETON                                        LR"))
        assertEquals(SetStmt(SetStmt.ValueSet.ON, listOf(IndicatorExpr(RT))), statement("SETON                                        RT"))
    }

    @test fun setOnParsingSecondPlace() {
        assertEquals(
            SetStmt(SetStmt.ValueSet.ON, listOf(IndicatorExpr(LR))),
            statement("SETON                                          LR"),
        )
        assertEquals(
            SetStmt(SetStmt.ValueSet.ON, listOf(IndicatorExpr(RT))),
            statement("SETON                                          RT"),
        )
    }

    @test fun clearParsing() {
        assertEquals(ClearStmt(dataRef("\$\$SVAR")), statement("CLEAR                   \$\$SVAR"))
    }

    @test fun clearArrayElementParsing() {
        assertEquals(ClearStmt(ArrayAccessExpr(dataRef("ARR1"), IntLiteral(1))), statement("CLEAR                   ARR1(1)"))
    }

    @test fun doParsing() {
        val stmt =
            statement(
                "DO        *HIVAL        \$X\n" +
                    "     C                   IF        \$\$SVARCD(\$X)=*BLANKS\n" +
                    "     C                   LEAVE\n" +
                    "     C                   ENDIF\n" +
                    "     C                   EVAL      \$\$URL=%XLATE(\$\$SVARCD(\$X):\n" +
                    "     C                             \$\$SVARVA(\$X):\n" +
                    "     C                             \$\$URL)\n" +
                    "     C                   ENDDO",
            )
        assertEquals(true, stmt is DoStmt)
        assertEquals(2, (stmt as DoStmt).body.size)
    }

    @test fun iterParsing() {
        assertEquals(IterStmt(), statement("ITER"))
    }

    @test fun parseEvalWithPlusExpression() {
        assertEquals(
            EvalStmt(
                dataRef("RESULT"),
                PlusExpr(dataRef("A"), dataRef("B")),
            ),
            statement("EVAL      RESULT = A + B"),
        )
    }

    @test fun parseEvalWithUnqualifiedDsAccessAndAssignmentOfLiteral() {
        assertStatementCanBeParsed("EVAL      DS1=1", addPrefix = true)
    }

    @test fun parseEvalWithUnqualifiedDsAccess() {
        assertStatementCanBeParsed("EVAL      DS1=*ON", addPrefix = true)
    }

    @test fun parseEvalWithoutFlags() {
        val stmtCtx = assertStatementCanBeParsed("EVAL      A=1", addPrefix = true)
        val stmt = stmtCtx.toAst()
        assert(stmt is EvalStmt)
        val evalStmt = stmt as EvalStmt
        assertEquals(false, evalStmt.flags.halfAdjust)
        assertEquals(false, evalStmt.flags.maximumNumberOfDigitsRule)
        assertEquals(false, evalStmt.flags.resultDecimalPositionRule)
    }

    @test fun parseEvalH() {
        val stmtCtx = assertStatementCanBeParsed("EVAL(H)   A=1", addPrefix = true)
        val stmt = stmtCtx.toAst()
        assert(stmt is EvalStmt)
        val evalStmt = stmt as EvalStmt
        assertEquals(true, evalStmt.flags.halfAdjust)
        assertEquals(false, evalStmt.flags.maximumNumberOfDigitsRule)
        assertEquals(false, evalStmt.flags.resultDecimalPositionRule)
    }

    @test fun parseEvalM() {
        val stmtCtx = assertStatementCanBeParsed("EVAL(M)   A=1", addPrefix = true)
        val stmt = stmtCtx.toAst()
        assert(stmt is EvalStmt)
        val evalStmt = stmt as EvalStmt
        assertEquals(false, evalStmt.flags.halfAdjust)
        assertEquals(true, evalStmt.flags.maximumNumberOfDigitsRule)
        assertEquals(false, evalStmt.flags.resultDecimalPositionRule)
    }

    @test fun parseEvalR() {
        val stmtCtx = assertStatementCanBeParsed("EVAL(R)   A=1", addPrefix = true)
        val stmt = stmtCtx.toAst()
        assert(stmt is EvalStmt)
        val evalStmt = stmt as EvalStmt
        assertEquals(false, evalStmt.flags.halfAdjust)
        assertEquals(false, evalStmt.flags.maximumNumberOfDigitsRule)
        assertEquals(true, evalStmt.flags.resultDecimalPositionRule)
    }

    @test fun parseEvalRH() {
        val stmtCtx = assertStatementCanBeParsed("EVAL(RH)   A=1", addPrefix = true)
        val stmt = stmtCtx.toAst()
        assert(stmt is EvalStmt)
        val evalStmt = stmt as EvalStmt
        assertEquals(true, evalStmt.flags.halfAdjust)
        assertEquals(false, evalStmt.flags.maximumNumberOfDigitsRule)
        assertEquals(true, evalStmt.flags.resultDecimalPositionRule)
    }

    @test fun parseEvalWithQualifiedDsAccess() {
        assertStatementCanBeParsed("EVAL      DS1.AR2=*ON", addPrefix = true)
    }

    @test fun parseEvalWithIndicatorTarget() {
        assertStatementCanBeParsed("EVAL      *IN35=*ON", addPrefix = true)
    }

    @test fun parseEvalWithGlobalIndicatorTarget() {
        assertStatementCanBeParsed("EVAL      *IN=*ON", addPrefix = true)
    }

    @test fun parseArrayAccessInFactor2() {
        assertStatementCanBeParsed("MOVE      TXT(1)        X", addPrefix = true)
    }

    @test
    fun parseArrayAccessInResult() {
        assertStatementCanBeParsed("MOVE      X             TXT(1)", addPrefix = true)
    }

    @test fun plistDeclareVariable() {
        val cu = assertASTCanBeProduced("ECHO")
        cu.resolveAndValidate()
        val plists = cu.collectByType(PlistStmt::class.java).distinct()
        assertEquals(1, plists.size)
        assertEquals(1, plists.first().dataDefinition().size)
    }

    @test fun plistDoesNotDeclareVariable() {
        val cu = assertASTCanBeProduced("ECHO2")
        cu.resolveAndValidate()
        val plists = cu.collectByType(PlistStmt::class.java).distinct()
        assertEquals(1, plists.size)
        assertEquals(0, plists.first().dataDefinition().size)
    }
}
