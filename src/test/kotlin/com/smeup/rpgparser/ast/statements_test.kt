package com.smeup.rpgparser.ast

import com.smeup.rpgparser.*
import me.tomassetti.kolasu.model.ReferenceByName
import kotlin.test.assertEquals
import org.junit.Test as test

class StatementsTest {

    private fun statement(code: String) : Statement {
        val stmtContext = assertStatementCanBeParsed("     C                   $code                                      ")
        return stmtContext.toAst(considerPosition = false)
    }

    @test fun executeSubroutineParsing() {
        assertEquals(ExecuteSubroutine(ReferenceByName("IMP0")), statement("EXSR      IMP0"))
    }

}
