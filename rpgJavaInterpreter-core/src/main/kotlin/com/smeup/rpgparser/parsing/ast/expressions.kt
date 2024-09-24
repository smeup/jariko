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

package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.logging.ILoggableExpression
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.math.BigDecimal

@Serializable
abstract class Expression(@Transient override val position: Position? = null) : Node(position), ILoggableExpression {
    open fun render(): String = this.javaClass.simpleName
    abstract fun evalWith(evaluator: Evaluator): Value
}

// /
// / Literals
// /

@Serializable
abstract class NumberLiteral(@Transient override val position: Position? = null) : Expression(position)

@Serializable
data class IntLiteral(
    val value: Long,
    override val position: Position? = null,
    val precision: Int = BigDecimal(value).precision()
) : NumberLiteral(position) {
    override fun render() = value.toString()
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}
@Serializable
data class RealLiteral(
    @Contextual val value: BigDecimal,
    override val position: Position? = null,
    val precision: Int = value.precision()
) : NumberLiteral(position) {

    override fun render() = value.toString()
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}
@Serializable
data class StringLiteral(var value: String, override val position: Position? = null) : Expression(position) {
    override fun render() = "\"$value\""
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// /
// / Figurative constants
// /

@Serializable
abstract class FigurativeConstantRef(@Transient override val position: Position? = null) : Expression(position)

@Serializable
data class BlanksRefExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class OnRefExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class OffRefExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class HiValExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class LowValExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class StartValExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class EndValExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class ZeroExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class AllExpr(val charsToRepeat: StringLiteral, override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class UDateRefExpr(
    override val position: Position? = null
) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class UYearRefExpr(
    override val position: Position? = null
) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class UMonthRefExpr(
    override val position: Position? = null
) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class UDayRefExpr(
    override val position: Position? = null
) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// /
// / Date formats
// /

@Serializable
data class IsoFormatExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class JulFormatExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class NullValExpr(override val position: Position? = null) : FigurativeConstantRef(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// /
// / Comparisons
// /

@Serializable
data class EqualityExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} = ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class AssignmentExpr(var target: AssignableExpression, var value: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${target.render()} = ${value.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class GreaterThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} > ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class GreaterEqualThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} >= ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class LessThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} < ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class LessEqualThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} <= ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class DifferentThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} <> ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// /
// / Logical operations
// /
@Serializable
data class NotExpr(var base: Expression, override val position: Position? = null) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class LogicalOrExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} || ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}
@Serializable
data class LogicalAndExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} && ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// /
// / Arithmetic operations
// /

@Serializable
data class PlusExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} + ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class MinusExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} - ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class MultExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} * ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class DivExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} / ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class NegationExpr(
    var value1: Expression,
    override val position: Position? = null
) :
    Expression(position) {

    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class ExpExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} ** ${right.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// /
// / Misc
// /

@Serializable
abstract class AssignableExpression(@Transient override val position: Position? = null) : Expression(position) {
    abstract fun size(): Int
}

@Serializable
data class DataRefExpr(val variable: ReferenceByName<AbstractDataDefinition>, override val position: Position? = null) :
    AssignableExpression(position) {

    init {
        require(!variable.name.startsWith("*")) { "This is not a valid variable name: '${variable.name}' - ${position.atLine()}" }
        require(variable.name.isNotBlank()) {
            "The variable name should not blank - ${position.atLine()}"
        }
        require(variable.name.trim() == variable.name) {
            "The variable name should not starts or ends with whitespace: $variable.name - ${position.atLine()}"
        }
        require(!variable.name.contains(".")) {
            "The variable name should not contain any dot: <${variable.name}> - ${position.atLine()}"
        }
        require(!variable.name.contains("(") && !variable.name.contains(")")) {
            "The variable name should not contain any parenthesis: $variable.name - ${position.atLine()}"
        }
    }

    override fun size(): Int {
        return variable.referred!!.type.size
    }

    override fun render() = variable.name
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class QualifiedAccessExpr(val container: Expression, val field: ReferenceByName<FieldDefinition>, override val position: Position? = null) :
        AssignableExpression(position) {

    init {
        require(field.name.isNotBlank()) { "The field name should not blank" }
        require(field.name.trim() == field.name) {
            "The field name should not starts or ends with whitespace"
        }
    }

    override fun size(): Int {
        TODO()
    }

    override fun render() = "${container.render()}.${field.name}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class ArrayAccessExpr(var array: Expression, var index: Expression, override val position: Position? = null) :
    AssignableExpression(position) {
    override fun render(): String {
        return "${this.array.render()}(${index.render()}))"
    }
    override fun size(): Int {
        TODO("size")
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// A Function call is not distinguishable from an array access
// TODO replace them in the AST during the resolution phase
@Serializable
data class FunctionCall(
    val function: ReferenceByName<Function>,
    val args: List<Expression>,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

fun dataRefTo(dataDefinition: AbstractDataDefinition) =
        DataRefExpr(ReferenceByName(dataDefinition.name, dataDefinition))

@Serializable
data class NumberOfElementsExpr(val value: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "%ELEM(${value.render()})"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
internal data class StatusExpr(override val position: Position?) : Expression(position) {
    override fun render() = "*STATUS"
    override fun evalWith(evaluator: Evaluator) = IntValue(0)
}
