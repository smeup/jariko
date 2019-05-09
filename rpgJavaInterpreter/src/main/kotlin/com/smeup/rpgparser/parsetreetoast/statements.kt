package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position

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

internal fun RpgParser.ForstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ForStmt {
    val assignment = this.beginfor().csFOR().expression(0).toAst(conf)
    val endValue = this.beginfor().csFOR().expression(1).toAst(conf)
    return ForStmt(
            assignment,
            endValue,
            this.statement().map { it.toAst(conf) },
            toPosition(conf.considerPosition))
}

internal fun RpgParser.SelectstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SelectStmt {
    val whenClauses = this.whenstatement().map { it.toAst(conf) }
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    val statementsOfLastWhen = if (this.whenstatement().isEmpty())
        emptyList()
    else
        this.whenstatement().last().statement().map { it.toAst(conf) }
    val indexOfOther = statementsOfLastWhen.indexOfFirst { it is OtherStmt }
    var other : SelectOtherClause? = null
    if (indexOfOther != -1) {
        val otherPosition = if (conf.considerPosition) {
            Position(statementsOfLastWhen[indexOfOther].position!!.start, statementsOfLastWhen.last().position!!.end)
        } else {
            null
        }
        other = SelectOtherClause(statementsOfLastWhen.subList(indexOfOther + 1, statementsOfLastWhen.size), position = otherPosition)
    }

    return SelectStmt(whenClauses, other, toPosition(conf.considerPosition))
}

internal fun RpgParser.WhenstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SelectCase {
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    var statementsToConsider = this.statement().map { it.toAst(conf) }
    val indexOfOther = statementsToConsider.indexOfFirst { it is OtherStmt }
    if (indexOfOther != -1) {
        statementsToConsider = statementsToConsider.subList(0, indexOfOther)
    }
    return SelectCase(
            this.`when`().csWHEN().fixedexpression.expression().toAst(conf),
            statementsToConsider,
            toPosition(conf.considerPosition)
    )
}

internal fun RpgParser.OtherContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SelectOtherClause {
    TODO()
}

internal fun RpgParser.IfstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): IfStmt {
    return IfStmt(this.beginif().fixedexpression.expression().toAst(conf),
            this.thenBody.map { it.toAst(conf) },
            this.elseIfClause().map { it.toAst(conf) },
            this.elseClause()?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.ElseClauseContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ElseClause {
    return ElseClause(this.statement().map { it.toAst(conf) }, toPosition(conf.considerPosition))
}

internal fun RpgParser.ElseIfClauseContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ElseIfClause {
    return ElseIfClause(
            this.elseifstmt().fixedexpression.expression().toAst(conf),
            this.statement().map { it.toAst(conf) }, toPosition(conf.considerPosition))
}
