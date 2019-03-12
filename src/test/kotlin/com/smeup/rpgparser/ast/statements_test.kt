package com.smeup.rpgparser.ast

import com.smeup.rpgparser.*
import me.tomassetti.kolasu.model.ReferenceByName
import kotlin.test.assertEquals
import org.junit.Test as test

class StatementsTest {

    private fun statement(code: String) : Statement {
        val stmtContext = assertStatementCanBeParsed("     C                   $code                                                          ")
        return stmtContext.toAst(considerPosition = false)
    }

    @test fun executeSubroutineParsing() {
        assertEquals(ExecuteSubroutine(ReferenceByName("IMP0")), statement("EXSR      IMP0"))
    }

    @test fun evalParsing() {
        assertEquals(EvalStmt(EqualityExpr(
                DataRefExpr(ReferenceByName("\$\$SVAR")),
                DataRefExpr(ReferenceByName("U\$SVARSK")))),
                statement("EVAL      \$\$SVAR=U\$SVARSK"))
    }

    @test fun callParsing() {
        assertEquals(CallStmt(StringLiteral("JD_URL")), statement("CALL      'JD_URL'"))
    }

    @test fun ifParsingSimple() {
        assertEquals(IfStmt(
                GreaterThanExpr(
                        DataRefExpr(ReferenceByName("\$X")),
                        IntLiteral(0)
                ),
                listOf(
                        EvalStmt(EqualityExpr(
                                DataRefExpr(ReferenceByName("U\$IN35")),
                                StringLiteral("1")))
                )
        ), statement("IF        \$X>0\n" +
                "     C                   EVAL      U\$IN35='1'\n" +
                "     C                   ENDIF"))
    }

    @test fun ifParsingEmpty() {
        assertEquals(IfStmt(
                GreaterThanExpr(
                        DataRefExpr(ReferenceByName("\$X")),
                        IntLiteral(0)
                ),
                emptyList()
        ), statement("IF        \$X>0\n" +
                "     C                   ENDIF"))
    }

    @test fun ifParsingWithElseSimple() {
        assertEquals(IfStmt(
                GreaterThanExpr(
                        DataRefExpr(ReferenceByName("\$X")),
                        IntLiteral(0)
                ),
                listOf(
                        EvalStmt(EqualityExpr(
                                DataRefExpr(ReferenceByName("U\$IN35")),
                                StringLiteral("1")))
                ),
                emptyList(),
                ElseClause(listOf(
                        EvalStmt(EqualityExpr(
                                DataRefExpr(ReferenceByName("\$\$URL")),
                                FunctionCall(ReferenceByName("\$\$SVARVA"), listOf(DataRefExpr(ReferenceByName("\$R")))))))
                )
        ), statement("IF        \$X>0\n" +
                "     C                   EVAL      U\$IN35='1'\n" +
                "5    C                   ELSE\n" +
                "     C                   EVAL      \$\$URL=\$\$SVARVA(\$R)\n" +
                "     C                   ENDIF"))
    }

//    @test fun ifParsingComplex() {
//        assertEquals(ExecuteSubroutine(ReferenceByName("IMP0")), statement("IF        \$X>0\n" +
//                "     C                   EVAL      U\$IN35='1'\n" +
//                "5    C                   ELSE\n" +
//                "     C                   EVAL      \$\$URL=\$\$SVARVA(\$R)\n" +
//                "      * Replace all variables of execution in url\n" +
//                "     C                   EXSR      REPVAR\n" +
//                "      * Replace all variables of initialisation in url\n" +
//                "     C                   EVAL      \$\$SVAR=U\$SVARSK_INI\n" +
//                "     C                   EXSR      REPVAR\n" +
//                "      * Invoke url\n" +
//                "     C                   CLEAR                   \$\$SVAR\n" +
//                "     C                   EVAL      \$\$SVARCD(01)='Url'                           COSTANTE\n" +
//                "     C                   EVAL      \$\$SVARVA(01)=\$\$URL\n" +
//                "     C                   CALL      'JD_URL'\n" +
//                "     C                   PARM                    §§FUNZ\n" +
//                "     C                   PARM                    §§METO\n" +
//                "     C                   PARM                    \$\$SVAR\n" +
//                "     C                   ENDIF"))
//    }

    // TODO if with else if

    @test fun selectEmptyParsing() {
        assertEquals(SelectStmt(emptyList()), statement("SELECT\n" +
                "1e   C                   ENDSL"))
    }
//
//    @test fun selectSingleCaseParsing() {
//        assertEquals(SelectStmt(emptyList()), statement("SELECT\n" +
//                "      * Init\n" +
//                "1x   C                   WHEN      U\$FUNZ='INZ'\n" +
//                "     C                   EXSR      FINZ\n" +
//                "1e   C                   ENDSL"))
//    }
//
//    @test fun selectParsingComplex() {
//        assertEquals(SelectStmt(listOf(
//                /*SelectCase(
//                        EqualityExpr()),
//                SelectCase(),
//                SelectCase()*/)), statement("SELECT\n" +
//                "      * Init\n" +
//                "1x   C                   WHEN      U\$FUNZ='INZ'\n" +
//                "     C                   EXSR      FINZ\n" +
//                "      * Invoke URL\n" +
//                "1x   C                   WHEN      U\$FUNZ='EXE'\n" +
//                "     C                   EXSR      FEXE\n" +
//                "      * Detach (empty subroutine in this case)\n" +
//                "1x   C                   WHEN      U\$FUNZ='CLO'\n" +
//                "     C                   EXSR      FCLO\n" +
//                "1e   C                   ENDSL"))
//    }

}
