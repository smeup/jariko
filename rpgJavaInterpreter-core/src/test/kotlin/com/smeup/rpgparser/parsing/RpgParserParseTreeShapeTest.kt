package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.assertExpressionCanBeParsed
import com.smeup.rpgparser.parsing.ast.GreaterThanExpr
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class RpgParserParseTreeShapeTest {

    @test fun parseJD_001_justdirectives() {
        val root = assertCanBeParsed("JD_001_justdirectives")
        assertEquals(3, root.children.size)

        assertTrue(root.children[0] is StatementContext)
        val stmt0 = root.children[0] as StatementContext
        assertEquals(1, stmt0.children.size)
        assertTrue(stmt0.children[0] is DirectiveContext)
        val dir0 = stmt0.children[0] as DirectiveContext
        assertEquals("H/", dir0.DIRECTIVE().text)

        assertTrue(root.children[1] is StatementContext)
        val stmt1 = root.children[1] as StatementContext
        assertEquals(1, stmt1.children.size)
        assertTrue(stmt1.children[0] is DirectiveContext)
        val dir1 = stmt1.children[0] as DirectiveContext
        assertEquals("I/", dir1.DIRECTIVE().text)

        assertTrue(root.children[2] is StatementContext)
        val stmt2 = root.children[2] as StatementContext
        assertEquals(1, stmt2.children.size)
        assertTrue(stmt2.children[0] is DirectiveContext)
        val dir2 = stmt2.children[0] as DirectiveContext
        assertEquals("I/", dir2.DIRECTIVE().text)
    }

    @test fun parseJD_001_onedatadecl() {
        val root = assertCanBeParsed("JD_001_onedatadecl")

        assertTrue(root.children[0] is StatementContext)
        val stmt0 = root.children[0] as StatementContext
        assertEquals(1, stmt0.children.size)
        assertTrue(stmt0.children[0] is DirectiveContext)
        val dir0 = stmt0.children[0] as DirectiveContext
        assertEquals("H/", dir0.DIRECTIVE().text)

        assertTrue(root.children[1] is StatementContext)
        val stmt1 = root.children[1] as StatementContext
        assertEquals(1, stmt1.children.size)
        assertTrue(stmt1.children[0] is DirectiveContext)
        val dir1 = stmt1.children[0] as DirectiveContext
        assertEquals("I/", dir1.DIRECTIVE().text)

        assertTrue(root.children[2] is StatementContext)
        val stmt2 = root.children[2] as StatementContext
        assertEquals(1, stmt2.children.size)
        assertTrue(stmt2.children[0] is DirectiveContext)
        val dir2 = stmt2.children[0] as DirectiveContext
        assertEquals("I/", dir2.DIRECTIVE().text)
    }

    @test fun parseJD_001_onedatadecl_simple() {
        val root = assertCanBeParsed("JD_001_onedatadecl_simple")

        assertTrue(root.children[0] is StatementContext)
        val stmt0 = root.children[0] as StatementContext
        assertEquals(1, stmt0.children.size)
        assertTrue(stmt0.children[0] is DirectiveContext)
        val dir0 = stmt0.children[0] as DirectiveContext
        assertEquals("H/", dir0.DIRECTIVE().text)

        assertTrue(root.children[1] is StatementContext)
        val stmt1 = root.children[1] as StatementContext
        assertEquals(1, stmt1.children.size)
        assertTrue(stmt1.children[0] is DirectiveContext)
        val dir1 = stmt1.children[0] as DirectiveContext
        assertEquals("I/", dir1.DIRECTIVE().text)

        assertTrue(root.children[2] is StatementContext)
        val stmt2 = root.children[2] as StatementContext
        assertEquals(1, stmt2.children.size)
        assertTrue(stmt2.children[0] is DirectiveContext)
        val dir2 = stmt2.children[0] as DirectiveContext
        assertEquals("I/", dir2.DIRECTIVE().text)
    }

    @test fun parseJD_001_onedatadecl_simple_withspace() {
        val root = assertCanBeParsed("JD_001_onedatadecl_simple_withspace")

        assertTrue(root.children[0] is StatementContext)
        val stmt0 = root.children[0] as StatementContext
        assertEquals(1, stmt0.children.size)
        assertTrue(stmt0.children[0] is DirectiveContext)
        val dir0 = stmt0.children[0] as DirectiveContext
        assertEquals("H/", dir0.DIRECTIVE().text)

        assertTrue(root.children[1] is StatementContext)
        val stmt1 = root.children[1] as StatementContext
        assertEquals(1, stmt1.children.size)
        assertTrue(stmt1.children[0] is DirectiveContext)
        val dir1 = stmt1.children[0] as DirectiveContext
        assertEquals("I/", dir1.DIRECTIVE().text)

        assertTrue(root.children[2] is StatementContext)
        val stmt2 = root.children[2] as StatementContext
        assertEquals(1, stmt2.children.size)
        assertTrue(stmt2.children[0] is DirectiveContext)
        val dir2 = stmt2.children[0] as DirectiveContext
        assertEquals("I/", dir2.DIRECTIVE().text)
    }

    @test fun parseGreaterThanAndPlus() {
        val exp = assertExpressionCanBeParsed("                                   %LEN(%TRIM(§§NAM))>%LEN(%TRIM(\$\$EST_FLT))+1")
        val ast = exp.toAst(ToAstConfiguration(considerPosition = false))
        assertTrue(ast is GreaterThanExpr)
    }
}
