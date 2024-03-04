/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.RpgParser.BlockContext
import com.smeup.rpgparser.interpreter.Evaluator
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.utils.ComparisonOperator
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable

fun RpgParser.StatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed() != null -> this.cspec_fixed().toAst(conf)
        this.block() != null -> this.block().toAst(conf)
        else -> todo(conf = conf)
    }
}

internal fun BlockContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.ifstatement() != null -> this.ifstatement().toAst(conf)
        this.selectstatement() != null -> this.selectstatement()
            .let {
                it.beginselect().csSELECT().cspec_fixed_standard_parts().validate(
                    stmt = it.toAst(conf = conf),
                    conf = conf
                )
            }
        this.casestatement() != null -> this.casestatement().toAst(conf)
        this.begindo() != null -> this.begindo()
            .let {
                it.csDO().cspec_fixed_standard_parts().validate(
                    stmt = it.toAst(blockContext = this, conf = conf),
                    conf = conf
                )
            }
        this.begindow() != null -> this.begindow().toAst(blockContext = this, conf = conf)
        this.csDOWxx() != null -> this.csDOWxx().toAst(blockContext = this, conf = conf)
        this.forstatement() != null -> this.forstatement().toAst(conf)
        this.begindou() != null -> this.begindou().toAst(blockContext = this, conf = conf)
        else -> todo(message = "Missing composite statement implementation for this block: ${this.text}", conf = conf)
    }
}

internal fun RpgParser.CsDOWxxContext.toAst(blockContext: BlockContext, conf: ToAstConfiguration = ToAstConfiguration()): DOWxxStmt {
    val comparison = when {
        this.csDOWEQ() != null -> ComparisonOperator.EQ
        this.csDOWNE() != null -> ComparisonOperator.NE
        this.csDOWGT() != null -> ComparisonOperator.GT
        this.csDOWGE() != null -> ComparisonOperator.GE
        this.csDOWLT() != null -> ComparisonOperator.LT
        this.csDOWLE() != null -> ComparisonOperator.LE
        else -> todo(conf = conf)
    }
    val factor2 = when {
        this.csDOWEQ() != null -> this.csDOWEQ().cspec_fixed_standard_parts().factor2.content.toAst(conf = conf)
        this.csDOWNE() != null -> this.csDOWNE().cspec_fixed_standard_parts().factor2.content.toAst(conf = conf)
        this.csDOWGT() != null -> this.csDOWGT().cspec_fixed_standard_parts().factor2.content.toAst(conf = conf)
        this.csDOWGE() != null -> this.csDOWGE().cspec_fixed_standard_parts().factor2.content.toAst(conf = conf)
        this.csDOWLT() != null -> this.csDOWLT().cspec_fixed_standard_parts().factor2.content.toAst(conf = conf)
        this.csDOWLE() != null -> this.csDOWLE().cspec_fixed_standard_parts().factor2.content.toAst(conf = conf)
        else -> todo(conf = conf)
    }

    return DOWxxStmt(
        comparisonOperator = comparison,
        factor1 = this.factor1.content.toAst(conf = conf),
        factor2 = factor2,
        position = toPosition(conf.considerPosition),
        body = blockContext.statement().map { it.toAst(conf) }
    )
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
    val result = beginselect().csSELECT().cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    val dataDefinition = beginselect().csSELECT().cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return SelectStmt(
        cases = whenClauses,
        other = other,
        dataDefinition = dataDefinition,
        position = toPosition(conf.considerPosition)
    )
}

internal fun RpgParser.BegindoContext.toAst(
    blockContext: BlockContext,
    conf: ToAstConfiguration = ToAstConfiguration()
): DoStmt {
    val result = csDO().cspec_fixed_standard_parts().result
    val iter = if (result.text.isBlank()) null else result.toAst(conf)
    val factor = factor()
    val start = if (factor.text.isBlank()) IntLiteral(1) else factor.content.toAst(conf)
    val factor2 = csDO().cspec_fixed_standard_parts().factor2 ?: null
    val endLimit =
        when {
            factor2 == null || factor2.text.trim().isEmpty() -> IntLiteral(1)
            factor2.symbolicConstants() != null -> factor2.symbolicConstants().toAst()
            else -> factor2.runParserRuleContext(conf = conf) { f2 -> f2.content.toAst(conf) }
        }
    val position = toPosition(conf.considerPosition)
    val dataDefinition = csDO().cspec_fixed_standard_parts().toDataDefinition(result.text, position, conf)
    return DoStmt(
        endLimit = endLimit,
        index = iter,
        body = blockContext.statement().map { it.toAst(conf) },
        startLimit = start,
        dataDefinition = dataDefinition,
        position = toPosition(conf.considerPosition)
    )
}

internal fun RpgParser.BegindowContext.toAst(
    blockContext: BlockContext,
    conf: ToAstConfiguration = ToAstConfiguration()
): DowStmt {
    val endExpression = csDOW().fixedexpression.expression().toAst(conf)
    return DowStmt(endExpression,
        blockContext.statement().map { it.toAst(conf) },
        position = toPosition(conf.considerPosition)
    )
}

internal fun RpgParser.BegindouContext.toAst(
    blockContext: BlockContext,
    conf: ToAstConfiguration = ToAstConfiguration()
): DouStmt {
    val endExpression = csDOU().fixedexpression.expression().toAst(conf)
    return DouStmt(endExpression,
        blockContext.statement().map { it.toAst(conf) },
        position = toPosition(conf.considerPosition))
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
    return if (this.`when`() != null) {
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
        val condition = LogicalCondition(comparison.asExpression(this.csWHENxx().factor1, factor2, conf))
        condition.and(ands)
        condition.or(ors)
        SelectCase(
            condition,
            statementsToConsider,
            position
        )
    }
}

internal fun RpgParser.CasestatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CaseStmt {
    val casClauses = this.csCASxx().map { it.toAst(conf) }
    val otherClause = this.csCASother().toAst()
    return CaseStmt(
        cases = casClauses,
        other = otherClause,
        position = toPosition(conf.considerPosition)
    )
}

internal fun RpgParser.CsCASxxContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CaseClause {
    val (comparison, factor2) = this.getCondition()
    val function = this.getFunction()
    val condition = LogicalCondition(comparison.asExpression(this.factor1, factor2, conf))
    val position = toPosition(conf.considerPosition)
    return CaseClause(
        condition,
        position,
        function
    )
}

internal fun RpgParser.CsCASotherContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CaseOtherClause {
    val function = this.csCAS().cspec_fixed_standard_parts().resultType().text
    val position = toPosition(conf.considerPosition)
    return CaseOtherClause(
        position,
        function
    )
}

@Serializable
data class LogicalCondition(val expression: Expression) : Expression() {
    val ands = mutableListOf<LogicalCondition>()
    fun and(conditions: List<LogicalCondition>) {
        ands.addAll(conditions)
    }

    val ors = mutableListOf<LogicalCondition>()
    fun or(conditions: List<LogicalCondition>) {
        ors.addAll(conditions)
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

internal fun RpgParser.CsANDxxContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): LogicalCondition {
    val (comparison, factor2) = this.getCondition()
    return LogicalCondition(comparison.asExpression(this.factor1, factor2, conf))
}

internal fun RpgParser.CsORxxContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): LogicalCondition {
    val (comparison, factor2) = this.getCondition()
    val ands = this.csANDxx().map { it.toAst(conf) }
    val result = LogicalCondition(comparison.asExpression(this.factor1, factor2, conf))
    result.and(ands)
    return result
}

internal fun ComparisonOperator.asExpression(factor1: RpgParser.FactorContext, factor2: RpgParser.FactorContext, conf: ToAstConfiguration): Expression {
    val left = factor1.toAstIfSymbolicConstant() ?: factor1.content?.toAst(conf) ?: factor1.error("Factor 1 cannot be null", conf = conf)
    val right = factor2.toAstIfSymbolicConstant() ?: factor2.content?.toAst(conf) ?: factor2.error("Factor 2 cannot be null", conf = conf)
    return when (this) {
        ComparisonOperator.EQ -> EqualityExpr(left, right)
        ComparisonOperator.NE -> DifferentThanExpr(left, right)
        ComparisonOperator.GE -> GreaterEqualThanExpr(left, right)
        ComparisonOperator.GT -> GreaterThanExpr(left, right)
        ComparisonOperator.LE -> LessEqualThanExpr(left, right)
        ComparisonOperator.LT -> LessThanExpr(left, right)
    }
}

internal fun RpgParser.CsORxxContext.getCondition() =
    when {
        this.csOREQ() != null -> ComparisonOperator.EQ to this.csOREQ().cspec_fixed_standard_parts().factor2
        this.csORNE() != null -> ComparisonOperator.NE to this.csORNE().cspec_fixed_standard_parts().factor2
        this.csORGE() != null -> ComparisonOperator.GE to this.csORGE().cspec_fixed_standard_parts().factor2
        this.csORGT() != null -> ComparisonOperator.GT to this.csORGT().cspec_fixed_standard_parts().factor2
        this.csORLE() != null -> ComparisonOperator.LE to this.csORLE().cspec_fixed_standard_parts().factor2
        this.csORLT() != null -> ComparisonOperator.LT to this.csORLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No valid ORXX condition")
    }

internal fun RpgParser.CsANDxxContext.getCondition() =
    when {
        this.csANDEQ() != null -> ComparisonOperator.EQ to this.csANDEQ().cspec_fixed_standard_parts().factor2
        this.csANDNE() != null -> ComparisonOperator.NE to this.csANDNE().cspec_fixed_standard_parts().factor2
        this.csANDGE() != null -> ComparisonOperator.GE to this.csANDGE().cspec_fixed_standard_parts().factor2
        this.csANDGT() != null -> ComparisonOperator.GT to this.csANDGT().cspec_fixed_standard_parts().factor2
        this.csANDLE() != null -> ComparisonOperator.LE to this.csANDLE().cspec_fixed_standard_parts().factor2
        this.csANDLT() != null -> ComparisonOperator.LT to this.csANDLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No valid ANDXX condition")
    }

internal fun RpgParser.CsWHENxxContext.getCondition() =
    when {
        this.csWHENEQ() != null -> ComparisonOperator.EQ to this.csWHENEQ().cspec_fixed_standard_parts().factor2
        this.csWHENNE() != null -> ComparisonOperator.NE to this.csWHENNE().cspec_fixed_standard_parts().factor2
        this.csWHENGE() != null -> ComparisonOperator.GE to this.csWHENGE().cspec_fixed_standard_parts().factor2
        this.csWHENGT() != null -> ComparisonOperator.GT to this.csWHENGT().cspec_fixed_standard_parts().factor2
        this.csWHENLE() != null -> ComparisonOperator.LE to this.csWHENLE().cspec_fixed_standard_parts().factor2
        this.csWHENLT() != null -> ComparisonOperator.LT to this.csWHENLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No valid WhenXX condition")
    }

internal fun RpgParser.CsCASxxContext.getCondition() =
    when {
        this.csCASEQ() != null -> ComparisonOperator.EQ to this.csCASEQ().cspec_fixed_standard_parts().factor2
        this.csCASNE() != null -> ComparisonOperator.NE to this.csCASNE().cspec_fixed_standard_parts().factor2
        this.csCASGE() != null -> ComparisonOperator.GE to this.csCASGE().cspec_fixed_standard_parts().factor2
        this.csCASGT() != null -> ComparisonOperator.GT to this.csCASGT().cspec_fixed_standard_parts().factor2
        this.csCASLE() != null -> ComparisonOperator.LE to this.csCASLE().cspec_fixed_standard_parts().factor2
        this.csCASLT() != null -> ComparisonOperator.LT to this.csCASLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No valid WhenXX condition")
    }

internal fun RpgParser.CsCASxxContext.getFunction() =
    when {
        this.csCASEQ() != null -> this.csCASEQ().cspec_fixed_standard_parts().resultType().text
        this.csCASNE() != null -> this.csCASNE().cspec_fixed_standard_parts().resultType().text
        this.csCASGE() != null -> this.csCASGE().cspec_fixed_standard_parts().resultType().text
        this.csCASGT() != null -> this.csCASGT().cspec_fixed_standard_parts().resultType().text
        this.csCASLE() != null -> this.csCASLE().cspec_fixed_standard_parts().resultType().text
        this.csCASLT() != null -> this.csCASLT().cspec_fixed_standard_parts().resultType().text
        else -> throw RuntimeException("No valid WhenXX condition")
    }

internal fun RpgParser.CsIFxxContext.getCondition() =
    when {
        this.csIFEQ() != null -> ComparisonOperator.EQ to this.csIFEQ().cspec_fixed_standard_parts().factor2
        this.csIFNE() != null -> ComparisonOperator.NE to this.csIFNE().cspec_fixed_standard_parts().factor2
        this.csIFGE() != null -> ComparisonOperator.GE to this.csIFGE().cspec_fixed_standard_parts().factor2
        this.csIFGT() != null -> ComparisonOperator.GT to this.csIFGT().cspec_fixed_standard_parts().factor2
        this.csIFLE() != null -> ComparisonOperator.LE to this.csIFLE().cspec_fixed_standard_parts().factor2
        this.csIFLT() != null -> ComparisonOperator.LT to this.csIFLT().cspec_fixed_standard_parts().factor2
        else -> throw RuntimeException("No valid WhenXX condition")
    }

internal fun toAst(conf: ToAstConfiguration = ToAstConfiguration()): SelectOtherClause {
    TODO("OtherContext.toAst with $conf")
}

internal fun RpgParser.IfstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): IfStmt {
    val position = toPosition(conf.considerPosition)
    return if (this.beginif().fixedexpression != null) {
        IfStmt(
            this.beginif().fixedexpression.expression().toAst(conf),
            this.thenBody.mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() },
            this.elseIfClause().mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() },
            this.elseClause()?.toAst(conf),
            position
        )
    } else {
        val (comparison, factor2) = this.beginif().csIFxx().getCondition()
        val csANDxx = this.beginif().csIFxx().csANDxx()
        val ands = csANDxx.mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() }
        val csORxx = this.beginif().csIFxx().csORxx()
        val ors = csORxx.mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() }
        val condition = LogicalCondition(comparison.asExpression(this.beginif().csIFxx().factor1, factor2, conf))
        condition.and(ands)
        condition.or(ors)
        IfStmt(
            condition,
            this.thenBody.mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() },
            this.elseIfClause().mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() },
            this.elseClause()?.toAst(conf),
            position
        )
    }
}

internal fun RpgParser.ElseClauseContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ElseClause {
    return ElseClause(this.statement().mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() }, toPosition(conf.considerPosition))
}

internal fun RpgParser.ElseIfClauseContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ElseIfClause {
    return ElseIfClause(
            this.elseifstmt().fixedexpression.expression().toAst(conf),
            this.statement().mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() }, toPosition(conf.considerPosition))
}
