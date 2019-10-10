package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import java.lang.RuntimeException

fun RpgParser.StatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed() != null -> this.cspec_fixed().toAst(conf)
        this.block() != null -> this.block().toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun RpgParser.BlockContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.ifstatement() != null -> this.ifstatement().toAst(conf)
        this.selectstatement() != null -> this.selectstatement().toAst(conf)
        this.begindo() != null -> {
            val result = this.begindo().csDO().cspec_fixed_standard_parts().result
            val iter = if (result.text.isBlank()) null else result.toAst(conf)
            val factor = this.begindo().factor()
            val start = if (factor.text.isBlank()) IntLiteral(1) else factor.content.toAst(conf)
            val factor2 = this.begindo().csDO().cspec_fixed_standard_parts().factor2 ?: null
            val endLimit =
                    when {
                        factor2 == null -> IntLiteral(1)
                        factor2.symbolicConstants() != null -> factor2.symbolicConstants().toAst()
                        else -> factor2.content.toAst(conf)
                    }
            DoStmt(endLimit,
                iter,
                this.statement().map { it.toAst(conf) },
                start,
                position = toPosition(conf.considerPosition))
        }
        this.begindow() != null -> {
            val endExpression = this.begindow().csDOW().fixedexpression.expression().toAst(conf)
            DowStmt(endExpression,
                    this.statement().map { it.toAst(conf) },
                    position = toPosition(conf.considerPosition))
        }
        this.forstatement() != null -> this.forstatement().toAst(conf)
        this.begindou() != null -> {
            val endExpression = this.begindou().csDOU().fixedexpression.expression().toAst(conf)
            DouStmt(endExpression,
                    this.statement().map { it.toAst(conf) },
                    position = toPosition(conf.considerPosition))
        }
        else -> TODO(this.text.toString() + " " + toPosition(conf.considerPosition))
    }
}

internal fun RpgParser.ForstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ForStmt {
    val csFOR = this.beginfor().csFOR()
    val assignment = csFOR.expression(0).toAst(conf)
    val endValue = csFOR.stopExpression()?.expression()?.toAst() ?: IntLiteral(1)
    val downward = csFOR.FREE_DOWNTO() != null
    val byValue = csFOR.byExpression()?.expression()?.toAst() ?: IntLiteral(1)
    return ForStmt(
            assignment,
            endValue,
            byValue,
            downward,
            this.statement().map { it.toAst(conf) },
            toPosition(conf.considerPosition))
}

internal fun RpgParser.SelectstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SelectStmt {
    val whenClauses = this.whenstatement().map { it.toAst(conf) }
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    val statementsOfLastWhen = if (this.whenstatement().isEmpty())
        emptyList()
    else
        this.whenstatement().last().statement().map { it.toAst(conf) }
    val indexOfOther = statementsOfLastWhen.indexOfFirst { it is OtherStmt }
    var other: SelectOtherClause? = null
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

internal fun RpgParser.WhenstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SelectCase {
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    var statementsToConsider = this.statement().map { it.toAst(conf) }
    val indexOfOther = statementsToConsider.indexOfFirst { it is OtherStmt }
    if (indexOfOther != -1) {
        statementsToConsider = statementsToConsider.subList(0, indexOfOther)
    }
    val position = toPosition(conf.considerPosition)
    return try {
        if (this.`when`() != null) {
            SelectCase(
                this.`when`().csWHEN().fixedexpression.expression().toAst(conf),
                statementsToConsider,
                position
            )
        } else {
            val (comparison, factor2) = this.csWHENxx().getCondition()
            val csANDxx = this.csWHENxx().csANDxx()
            val ands = csANDxx.map { it.toAst(conf) }
            val csORxx = this.csWHENxx().csORxx()
            val ors = csORxx.map { it.toAst(conf) }
            val condition = LogicalCondition(comparison, this.csWHENxx().factor1, factor2)
            condition.and(ands)
            condition.or(ors)
            SelectCase(
                condition,
                statementsToConsider,
                position
            )
        }
    } catch (e: Exception) {
        throw RuntimeException("Exception at $position", e)
    }
}

class LogicalCondition(first: Comparison, factor1Context: RpgParser.FactorContext?, second: RpgParser.FactorContext?) : Expression() {
    private val ands = mutableListOf<LogicalCondition>()
    fun and(conditions: List<LogicalCondition>) {
        ands.addAll(conditions)
    }

    private val ors = mutableListOf<LogicalCondition>()
    fun or(conditions: List<LogicalCondition>) {
        ors.addAll(conditions)
    }
}

internal fun RpgParser.CsANDxxContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): LogicalCondition {
    val (comparison, factor2) = this.getCondition()
    return LogicalCondition(comparison, this.factor1, factor2)
}

internal fun RpgParser.CsORxxContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): LogicalCondition {
    val (comparison, factor2) = this.getCondition()
    val ands = this.csANDxx().map { it.toAst(conf) }
    val result = LogicalCondition(comparison, this.factor1, factor2)
    result.and(ands)
    return result
}

internal fun RpgParser.CsORxxContext.getCondition() =
    when {
        this.csOREQ() != null -> Comparison.EQ to this.csOREQ().cspec_fixed_standard_parts().factor2
        this.csORNE() != null -> Comparison.EQ to this.csORNE().cspec_fixed_standard_parts().factor2
        this.csORGE() != null -> Comparison.EQ to this.csORGE().cspec_fixed_standard_parts().factor2
        this.csORGT() != null -> Comparison.EQ to this.csORGT().cspec_fixed_standard_parts().factor2
        this.csORLE() != null -> Comparison.EQ to this.csORLE().cspec_fixed_standard_parts().factor2
        this.csORLT() != null -> Comparison.EQ to this.csORLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No walid ORXX condition")
    }

internal fun RpgParser.CsANDxxContext.getCondition() =
    when {
        this.csANDEQ() != null -> Comparison.EQ to this.csANDEQ().cspec_fixed_standard_parts().factor2
        this.csANDNE() != null -> Comparison.EQ to this.csANDNE().cspec_fixed_standard_parts().factor2
        this.csANDGE() != null -> Comparison.EQ to this.csANDGE().cspec_fixed_standard_parts().factor2
        this.csANDGT() != null -> Comparison.EQ to this.csANDGT().cspec_fixed_standard_parts().factor2
        this.csANDLE() != null -> Comparison.EQ to this.csANDLE().cspec_fixed_standard_parts().factor2
        this.csANDLT() != null -> Comparison.EQ to this.csANDLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No walid ANDXX condition")
    }

internal fun RpgParser.CsWHENxxContext.getCondition() =
    when {
        this.csWHENEQ() != null -> Comparison.EQ to this.csWHENEQ().cspec_fixed_standard_parts().factor2
        this.csWHENNE() != null -> Comparison.NE to this.csWHENNE().cspec_fixed_standard_parts().factor2
        this.csWHENGE() != null -> Comparison.GE to this.csWHENGE().cspec_fixed_standard_parts().factor2
        this.csWHENGT() != null -> Comparison.GT to this.csWHENGT().cspec_fixed_standard_parts().factor2
        this.csWHENLE() != null -> Comparison.LE to this.csWHENLE().cspec_fixed_standard_parts().factor2
        this.csWHENLT() != null -> Comparison.LT to this.csWHENLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No walid WhenXX condition")
    }

internal fun RpgParser.OtherContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SelectOtherClause {
    TODO("OtherContext.toAst with $conf")
}

internal fun RpgParser.IfstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): IfStmt {
    val position = toPosition(conf.considerPosition)
    return try {
        IfStmt(
            this.beginif().fixedexpression.expression().toAst(conf),
            this.thenBody.map { it.toAst(conf) },
            this.elseIfClause().map { it.toAst(conf) },
            this.elseClause()?.toAst(conf),
            position
        )
    } catch (e: Exception) {
        throw RuntimeException("Error parsing IF statement at $position", e)
    }
}

internal fun RpgParser.ElseClauseContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ElseClause {
    return ElseClause(this.statement().map { it.toAst(conf) }, toPosition(conf.considerPosition))
}

internal fun RpgParser.ElseIfClauseContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ElseIfClause {
    return ElseIfClause(
            this.elseifstmt().fixedexpression.expression().toAst(conf),
            this.statement().map { it.toAst(conf) }, toPosition(conf.considerPosition))
}
