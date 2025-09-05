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
import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.interpreter.Evaluator
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.utils.ComparisonOperator
import com.smeup.rpgparser.utils.isEmptyTrim
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree

fun RpgParser.StatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement =
    when {
        this.cspec_fixed() != null -> this.cspec_fixed().toAst(conf)
        this.cspec_fixed_sql() != null -> this.cspec_fixed_sql().toAst(conf)
        this.block() != null -> this.block().toAst(conf)
        else -> todo(conf = conf)
    }

internal fun BlockContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement =
    when {
        this.ifstatement() != null ->
            this
                .ifstatement()
                .toAst(conf)
                .buildIndicatorsFlags(this.ifstatement().beginif().csIFxx() ?: this.ifstatement().beginif(), conf)
        this.selectstatement() != null ->
            this
                .selectstatement()
                .let {
                    it.beginselect().csSELECT().cspec_fixed_standard_parts().validate(
                        stmt = it.toAst(conf = conf),
                        conf = conf,
                    )
                }
        this.casestatement() != null -> this.casestatement().toAst(conf)
        this.dostatement() != null -> this.dostatement().toAst(this, conf)
        this.forstatement() != null ->
            this
                .forstatement()
                .toAst(conf)
                .buildIndicatorsFlags(this.forstatement().beginfor(), conf)
        this.monitorstatement() != null ->
            this.monitorstatement().let {
                it.beginmonitor().csMONITOR().cspec_fixed_standard_parts().validate(
                    stmt = it.toAst(conf = conf),
                    conf = conf,
                )
            }
        else -> todo(message = "Missing composite statement implementation for this block: ${this.text}", conf = conf)
    }

/**
 * Builds and assigns indicator conditions and flags for the given statement.
 *
 * This method inspects the context type and generates an indicator condition
 * based on the indicators and configuration provided. It also handles the
 * processing of continued indicators.
 *
 * @param TContext The type of the parser rule context, which must extend [ParserRuleContext].
 * @param context The parsing rule context, which is one of several specific types such as
 *                [CsIFxxContext], [CsDOWxxContext], [CsDOUxxContext], etc.
 * @param conf The configuration used to build the AST (Abstract Syntax Tree).
 * @return The current [Statement] with updated indicator conditions and continued indicators.
 */
internal fun <TContext : ParserRuleContext> Statement.buildIndicatorsFlags(
    context: TContext,
    conf: ToAstConfiguration,
): Statement {
    var continuedIndicators: List<Cspec_continuedIndicatorsContext> = emptyList()
    this.indicatorCondition =
        when (context) {
            is CsIFxxContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is CsDOWxxContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is CsDOUxxContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is BeginifContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is BegindoContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is BegindowContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is BegindouContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is BeginforContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            is Cspec_fixedContext ->
                context
                    .toIndicatorCondition(context.indicators, context.indicatorsOff, conf)
                    .also { continuedIndicators = context.cspec_continuedIndicators() }
            else -> null
        }

    if (this.indicatorCondition != null) {
        this.continuedIndicators.populate(continuedIndicators, context.children.toList())
    }

    return this
}

/**
 * Converts the given indicator context and its associated on/off flag into an `IndicatorCondition` object.
 * This function checks if the provided `Cs_indicatorsContext` contains a valid indicator text. If the text is
 * not empty, it attempts to convert it into an `IndicatorCondition` object. If the conversion fails due to a
 * `NumberFormatException` (non-numeric indicators), an error is reported. If the indicator text is empty,
 * the function returns `null`.
 *
 * @receiver The `ParserRuleContext` that acts as the context for this operation.
 *
 * @param indicators The `Cs_indicatorsContext` containing the indicator data (as text) to be converted.
 * @param indicatorsOff The `OnOffIndicatorsFlagContext` representing the on/off flag for the indicator.
 *                      The condition checks whether the flag is equal to a space (" ") to determine if it's off.
 * @param conf The `ToAstConfiguration` used for configuration in case of an error.
 *
 * @return An `IndicatorCondition` containing the converted `IndicatorKey` and the on/off state (as a boolean)
 *         if the indicator text is valid. Returns `null` if the indicator text is empty.
 *
 * @throws NumberFormatException If the indicator text cannot be parsed into a valid numeric `IndicatorKey`.
 *                               In this case, an error is reported with a message about non-numeric indicators.
 */
private fun ParserRuleContext.toIndicatorCondition(
    indicators: Cs_indicatorsContext,
    indicatorsOff: OnOffIndicatorsFlagContext,
    conf: ToAstConfiguration,
) = if (indicators.text.isEmptyTrim()) {
    null
} else {
    try {
        IndicatorCondition(indicators.text.toIndicatorKey(), " " != indicatorsOff.text)
    } catch (e: NumberFormatException) {
        error("Non numeric indicators", e, conf)
    }
}

/**
 * Populates the current `HashMap` with a set of continued indicators and their related conditions.
 * This function processes a list of `Cspec_continuedIndicatorsContext` objects, extracts the relevant information
 * such as indicator keys, on/off states, and control levels (e.g., "AND", "OR"), and inserts them into the
 * `HashMap`. Additionally, it handles inline indicators by processing a `children` list of `ParseTree` nodes.
 *
 * @receiver A `HashMap` where keys are `IndicatorKey` objects, and values are `ContinuedIndicator` instances
 *           representing indicators with their conditions (e.g., on/off state, control level).
 * @param continuedIndicators A list of `Cspec_continuedIndicatorsContext` objects representing the indicators
 *                            to be processed. These indicators do not include inline indicators.
 * @param children An optional list of `ParseTree` nodes representing additional conditions (such as control levels
 *                 and indicator states) for inline indicators. By default, an empty list is passed if no children are
 *                 provided.
 */
private fun HashMap<IndicatorKey, ContinuedIndicator>.populate(
    continuedIndicators: List<Cspec_continuedIndicatorsContext>,
    children: List<ParseTree> = emptyList(),
) {
    // loop over continued indicators (WARNING: continuedIndicators not contains inline indicator)
    for (i in 0 until continuedIndicators.size) {
        val indicator =
            continuedIndicators[i]
                .indicators.children[0]
                .toString()
                .toIndicatorKey()
        var onOff = false
        if (!continuedIndicators[i]
                .indicatorsOff.children[0]
                .toString()
                .isEmptyTrim()
        ) {
            onOff = true
        }
        val controlLevel =
            when (continuedIndicators[i].start.type) {
                AndIndicator -> "AND"
                OrIndicator -> "OR"
                else -> ""
            }
        val continuedIndicator = ContinuedIndicator(indicator, onOff, controlLevel)
        this.put(indicator, continuedIndicator)
    }

    // Add indicatorCondition (inline indicator) also
    var controlLevel =
        (children[continuedIndicators.size + 1] as Cs_controlLevelContext).children[0].toString()
    if (controlLevel == "AN") {
        controlLevel = "AND"
    }
    var onOff = false
    if (!(children[continuedIndicators.size + 2] as OnOffIndicatorsFlagContext)
            .children[0]
            .toString()
            .isEmptyTrim()
    ) {
        onOff = true
    }
    val indicator =
        (children[continuedIndicators.size + 3] as Cs_indicatorsContext)
            .children[0]
            .toString()
            .toIndicatorKey()
    val continuedIndicator = ContinuedIndicator(indicator, onOff, controlLevel)
    this.put(indicator, continuedIndicator)
}

internal fun RpgParser.CsDOWxxContext.toAst(
    blockContext: BlockContext,
    position: Position? = null,
    conf: ToAstConfiguration = ToAstConfiguration(),
): DOWxxStmt {
    val pos = position ?: toPosition(conf.considerPosition)
    val comparison =
        when {
            this.csDOWEQ() != null -> ComparisonOperator.EQ
            this.csDOWNE() != null -> ComparisonOperator.NE
            this.csDOWGT() != null -> ComparisonOperator.GT
            this.csDOWGE() != null -> ComparisonOperator.GE
            this.csDOWLT() != null -> ComparisonOperator.LT
            this.csDOWLE() != null -> ComparisonOperator.LE
            else -> todo(conf = conf)
        }
    val factor2 =
        when {
            this.csDOWEQ() != null -> this.csDOWEQ().cspec_fixed_standard_parts().factor2
            this.csDOWNE() != null -> this.csDOWNE().cspec_fixed_standard_parts().factor2
            this.csDOWGT() != null -> this.csDOWGT().cspec_fixed_standard_parts().factor2
            this.csDOWGE() != null -> this.csDOWGE().cspec_fixed_standard_parts().factor2
            this.csDOWLT() != null -> this.csDOWLT().cspec_fixed_standard_parts().factor2
            this.csDOWLE() != null -> this.csDOWLE().cspec_fixed_standard_parts().factor2
            else -> todo(conf = conf)
        }

    val factor1Ast = this.factor1.toAstIfSymbolicConstant() ?: this.factor1.content.toAst(conf = conf)
    val factor2Ast = factor2.toAstIfSymbolicConstant() ?: factor2.content.toAst(conf)

    return DOWxxStmt(
        comparisonOperator = comparison,
        factor1 = factor1Ast,
        factor2 = factor2Ast,
        position = pos,
        body = blockContext.dostatement().statement().map { it.toAst(conf) },
    )
}

/**
 * In accord to official documentation, the condition of DOUxx is for to quit from block,
 *  instead for execute it like the new programming languages.
 * @see https://www.ibm.com/docs/en/i/7.5?topic=codes-douxx-do-until
 */
internal fun RpgParser.CsDOUxxContext.toAst(
    blockContext: BlockContext,
    position: Position? = null,
    conf: ToAstConfiguration = ToAstConfiguration(),
): DOUxxStmt {
    val pos = position ?: toPosition(conf.considerPosition)
    val comparison =
        when {
            this.csDOUEQ() != null -> ComparisonOperator.NE
            this.csDOUNE() != null -> ComparisonOperator.EQ
            this.csDOUGT() != null -> ComparisonOperator.LE
            this.csDOUGE() != null -> ComparisonOperator.LT
            this.csDOULT() != null -> ComparisonOperator.GE
            this.csDOULE() != null -> ComparisonOperator.GT
            else -> todo(conf = conf)
        }
    val factor2 =
        when {
            this.csDOUEQ() != null -> this.csDOUEQ().cspec_fixed_standard_parts().factor2
            this.csDOUNE() != null -> this.csDOUNE().cspec_fixed_standard_parts().factor2
            this.csDOUGT() != null -> this.csDOUGT().cspec_fixed_standard_parts().factor2
            this.csDOUGE() != null -> this.csDOUGE().cspec_fixed_standard_parts().factor2
            this.csDOULT() != null -> this.csDOULT().cspec_fixed_standard_parts().factor2
            this.csDOULE() != null -> this.csDOULE().cspec_fixed_standard_parts().factor2
            else -> todo(conf = conf)
        }

    return DOUxxStmt(
        comparisonOperator = comparison,
        factor1 =
            factor1.toAstIfSymbolicConstant() ?: factor1.content?.toAst(
                conf,
            ) ?: factor1.error("Factor 1 cannot be null", conf = conf),
        factor2 =
            factor2.toAstIfSymbolicConstant() ?: factor2.content?.toAst(
                conf,
            ) ?: factor1.error("Factor 2 cannot be null", conf = conf),
        position = pos,
        body = blockContext.dostatement().statement().map { it.toAst(conf) },
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
        toPosition(conf.considerPosition),
    )
}

internal fun RpgParser.SelectstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SelectStmt {
    val whenClauses = this.whenstatement().map { it.toAst(conf) }
    // Unfortunately the other clause might end up being part of the when clause so we should unfold it
    // TODO change this in the grammar
    val statementsOfLastWhen =
        if (this.whenstatement().isEmpty()) {
            emptyList()
        } else {
            this
                .whenstatement()
                .last()
                .statement()
                .map { it.toAst(conf) }
        }
    val indexOfOther = statementsOfLastWhen.indexOfFirst { it is OtherStmt }
    var other: SelectOtherClause? = null
    if (indexOfOther != -1) {
        val otherPosition =
            if (conf.considerPosition) {
                Position(statementsOfLastWhen[indexOfOther].position!!.start, statementsOfLastWhen.last().position!!.end)
            } else {
                null
            }
        other = SelectOtherClause(statementsOfLastWhen.subList(indexOfOther + 1, statementsOfLastWhen.size), position = otherPosition)
    }
    val result =
        beginselect()
            .csSELECT()
            .cspec_fixed_standard_parts()
            .result.text
    val position = toPosition(conf.considerPosition)
    val dataDefinition = beginselect().csSELECT().cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    // If other statement didn't get caught in the last when statement it can be still present as a standalone statement
    val otherClause = other ?: this.otherstatement()?.toAst(conf)
    return SelectStmt(
        cases = whenClauses,
        other = otherClause,
        dataDefinition = dataDefinition,
        position = toPosition(conf.considerPosition),
    )
}

internal fun RpgParser.DostatementContext.toAst(
    blockContext: BlockContext,
    conf: ToAstConfiguration = ToAstConfiguration(),
): Statement {
    val position = toPosition(conf.considerPosition)
    return when {
        this.begindo() != null ->
            this
                .begindo()
                .let {
                    val doStmt = it.toAst(blockContext = blockContext, position = position, conf = conf)
                    it.csDO().cspec_fixed_standard_parts().validate(
                        stmt = doStmt.buildIndicatorsFlags(this.begindo(), conf),
                        conf = conf,
                    )
                }
        this.begindow() != null ->
            this
                .begindow()
                .toAst(blockContext = blockContext, position = position, conf = conf)
                .buildIndicatorsFlags(this.begindow(), conf)
        this.csDOWxx() != null ->
            this
                .csDOWxx()
                .toAst(blockContext = blockContext, position = position, conf = conf)
                .buildIndicatorsFlags(this.csDOWxx(), conf)
        this.begindou() != null ->
            this
                .begindou()
                .toAst(blockContext = blockContext, position = position, conf = conf)
                .buildIndicatorsFlags(this.begindou(), conf)
        this.csDOUxx() != null ->
            this
                .csDOUxx()
                .toAst(blockContext = blockContext, position = position, conf = conf)
                .buildIndicatorsFlags(this.csDOUxx(), conf)
        else -> todo("Missing implementation for DO statement ${this.text}", conf)
    }
}

internal fun RpgParser.BegindoContext.toAst(
    blockContext: BlockContext,
    position: Position? = null,
    conf: ToAstConfiguration = ToAstConfiguration(),
): DoStmt {
    val result = csDO().cspec_fixed_standard_parts().result
    val iter = if (result.text.isBlank()) null else result.toAst(conf)
    val factor = factor()
    val start = if (factor.text.isBlank()) IntLiteral(1) else factor.content.toAst(conf)
    val factor2 = csDO().cspec_fixed_standard_parts().factor2
    val endLimit =
        when {
            factor2 == null || factor2.text.trim().isEmpty() -> IntLiteral(1)
            factor2.symbolicConstants() != null -> factor2.symbolicConstants().toAst()
            else -> factor2.runParserRuleContext(conf = conf) { f2 -> f2.content.toAst(conf) }
        }
    val pos = position ?: toPosition(conf.considerPosition)
    val dataDefinition = csDO().cspec_fixed_standard_parts().toDataDefinition(result.text, pos, conf)
    return DoStmt(
        endLimit = endLimit,
        index = iter,
        body = blockContext.dostatement().statement().map { it.toAst(conf) },
        startLimit = start,
        dataDefinition = dataDefinition,
        position = pos,
    )
}

internal fun RpgParser.BegindowContext.toAst(
    blockContext: BlockContext,
    position: Position? = null,
    conf: ToAstConfiguration = ToAstConfiguration(),
): DowStmt {
    val pos = position ?: toPosition(conf.considerPosition)
    val endExpression = csDOW().fixedexpression.expression().toAst(conf)
    return DowStmt(
        endExpression,
        blockContext.dostatement().statement().map { it.toAst(conf) },
        position = pos,
    )
}

internal fun RpgParser.BegindouContext.toAst(
    blockContext: BlockContext,
    position: Position? = null,
    conf: ToAstConfiguration = ToAstConfiguration(),
): DouStmt {
    val pos = position ?: toPosition(conf.considerPosition)
    val endExpression = csDOU().fixedexpression.expression().toAst(conf)
    return DouStmt(
        endExpression,
        blockContext.dostatement().statement().map { it.toAst(conf) },
        position = pos,
    )
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
            this
                .`when`()
                .csWHEN()
                .fixedexpression
                .expression()
                .toAst(conf),
            statementsToConsider,
            position,
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
            position,
        )
    }
}

internal fun OtherstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SelectOtherClause {
    val body = statement().map { it.toAst(conf) }
    val position = toPosition(conf.considerPosition)
    return SelectOtherClause(body, position)
}

internal fun RpgParser.CasestatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CaseStmt {
    val casClauses = this.csCASxx().map { it.toAst(conf) }
    val otherClause = if (this.csCASother() != null) this.csCASother().toAst() else null
    return CaseStmt(
        cases = casClauses,
        other = otherClause,
        position = toPosition(conf.considerPosition),
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
        function,
    )
}

internal fun RpgParser.CsCASotherContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CaseOtherClause {
    val function =
        this
            .csCAS()
            .cspec_fixed_standard_parts()
            .resultType()
            .text
    val position = toPosition(conf.considerPosition)
    return CaseOtherClause(
        position,
        function,
    )
}

@Serializable
data class LogicalCondition(
    val expression: Expression,
) : Expression() {
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

internal fun ComparisonOperator.asExpression(
    factor1: RpgParser.FactorContext,
    factor2: RpgParser.FactorContext,
    conf: ToAstConfiguration,
): Expression {
    val left = factor1.toAstIfSymbolicConstant() ?: factor1.content?.toAst(conf) ?: factor1.error("Factor 1 cannot be null", conf = conf)
    val right = factor2.toAstIfSymbolicConstant() ?: factor2.content?.toAst(conf) ?: factor2.error("Factor 2 cannot be null", conf = conf)
    return when (this) {
        ComparisonOperator.EQ -> EqualityExpr(left, right, left.position)
        ComparisonOperator.NE -> DifferentThanExpr(left, right, left.position)
        ComparisonOperator.GE -> GreaterEqualThanExpr(left, right, left.position)
        ComparisonOperator.GT -> GreaterThanExpr(left, right, left.position)
        ComparisonOperator.LE -> LessEqualThanExpr(left, right, left.position)
        ComparisonOperator.LT -> LessThanExpr(left, right, left.position)
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
        this.csCASEQ() != null ->
            this
                .csCASEQ()
                .cspec_fixed_standard_parts()
                .resultType()
                .text
        this.csCASNE() != null ->
            this
                .csCASNE()
                .cspec_fixed_standard_parts()
                .resultType()
                .text
        this.csCASGE() != null ->
            this
                .csCASGE()
                .cspec_fixed_standard_parts()
                .resultType()
                .text
        this.csCASGT() != null ->
            this
                .csCASGT()
                .cspec_fixed_standard_parts()
                .resultType()
                .text
        this.csCASLE() != null ->
            this
                .csCASLE()
                .cspec_fixed_standard_parts()
                .resultType()
                .text
        this.csCASLT() != null ->
            this
                .csCASLT()
                .cspec_fixed_standard_parts()
                .resultType()
                .text
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

internal fun RpgParser.MonitorstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MonitorStmt {
    val position = toPosition(conf.considerPosition)
    val statements =
        this.statement().mapNotNull {
            it.toAst(conf)
        }
    val onErrorClauses =
        this.onError().mapNotNull {
            it.toAst(conf)
        }

    return MonitorStmt(statements, onErrorClauses, position)
}

internal fun RpgParser.IfstatementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): IfStmt {
    val position = toPosition(conf.considerPosition)
    return if (this.beginif().fixedexpression != null) {
        IfStmt(
            this
                .beginif()
                .fixedexpression
                .expression()
                .toAst(conf),
            this.thenBody.mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() },
            this.elseIfClause().mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() },
            this.elseClause()?.toAst(conf),
            position,
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
            position,
        )
    }
}

internal fun RpgParser.OnErrorContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): OnErrorClause {
    val body = this.statement().mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() }
    val codes =
        this
            .csON_ERROR()
            .onErrorCode()
            .map { it.text.trim() }
            .ifEmpty { listOf("") }
    val position = toPosition(conf.considerPosition)
    return OnErrorClause(codes, body, position)
}

internal fun RpgParser.ElseClauseContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ElseClause =
    ElseClause(
        this.statement().mapNotNull {
            kotlin.runCatching { it.toAst(conf) }.getOrNull()
        },
        toPosition(conf.considerPosition),
    )

internal fun RpgParser.ElseIfClauseContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ElseIfClause =
    ElseIfClause(
        this
            .elseifstmt()
            .fixedexpression
            .expression()
            .toAst(conf),
        this.statement().mapNotNull { kotlin.runCatching { it.toAst(conf) }.getOrNull() },
        toPosition(conf.considerPosition),
    )
