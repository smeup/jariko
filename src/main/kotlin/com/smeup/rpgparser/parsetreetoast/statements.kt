package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.AssignableExpression
import com.smeup.rpgparser.ast.DoStmt
import com.smeup.rpgparser.ast.IntLiteral
import com.smeup.rpgparser.ast.Statement
import com.strumenta.kolasu.mapping.toPosition

fun RpgParser.StatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed() != null -> this.cspec_fixed().toAst(conf)
        this.block() != null -> this.block().toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun RpgParser.BlockContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.ifstatement() != null -> this.ifstatement().toAst(conf)
        this.selectstatement() != null -> this.selectstatement().toAst(conf)
        this.begindo() != null -> {
            val result = this.begindo().csDO().cspec_fixed_standard_parts().result
            val iter = if (result.text.isBlank()) null else result.toAst(conf) as AssignableExpression
            val factor = this.begindo().factor()
            val start = if (factor.text.isBlank()) IntLiteral(1) else factor.content.toAst(conf)
            DoStmt(
                    this.begindo().csDO().cspec_fixed_standard_parts().factor2.symbolicConstants().toAst(conf),
                    iter,
                    this.statement().map { it.toAst(conf) },
                    start,
                    position = toPosition(conf.considerPosition))
        }
        this.forstatement() != null -> this.forstatement().toAst(conf)
        else -> TODO(this.text.toString())
    }
}