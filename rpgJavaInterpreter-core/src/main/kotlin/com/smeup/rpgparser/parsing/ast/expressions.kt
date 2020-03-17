package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.FieldDefinition
import com.smeup.rpgparser.interpreter.atLine
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import java.math.BigDecimal

abstract class Expression(override val position: Position? = null) : Node(position) {
    open fun render(): String = this.javaClass.simpleName
}

// /
// / Literals
// /

abstract class NumberLiteral(override val position: Position? = null) : Expression(position)

data class IntLiteral(val value: Long, override val position: Position? = null) : NumberLiteral(position) {
    override fun render() = value.toString()
}
data class RealLiteral(val value: BigDecimal, override val position: Position? = null) : NumberLiteral(position) {
    override fun render() = value.toString()
}

data class StringLiteral(val value: String, override val position: Position? = null) : Expression(position) {
    override fun render() = "\"$value\""
}

// /
// / Figurative constants
// /

abstract class FigurativeConstantRef(override val position: Position? = null) : Expression(position)

data class BlanksRefExpr(override val position: Position? = null) : FigurativeConstantRef(position)

data class OnRefExpr(override val position: Position? = null) : FigurativeConstantRef(position)

data class OffRefExpr(override val position: Position? = null) : FigurativeConstantRef(position)

data class HiValExpr(override val position: Position? = null) : FigurativeConstantRef(position)

data class LowValExpr(override val position: Position? = null) : FigurativeConstantRef(position)

data class ZeroExpr(override val position: Position? = null) : FigurativeConstantRef(position)

data class AllExpr(val charsToRepeat: StringLiteral, override val position: Position? = null) : FigurativeConstantRef(position)

// /
// / Comparisons
// /

data class EqualityExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} = ${right.render()}"
}

data class AssignmentExpr(var target: AssignableExpression, var value: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${target.render()} = ${value.render()}"
}

data class GreaterThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} > ${right.render()}"
}

data class GreaterEqualThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} >= ${right.render()}"
}

data class LessThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} < ${right.render()}"
}

data class LessEqualThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} <= ${right.render()}"
}

data class DifferentThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} <> ${right.render()}"
}

// /
// / Logical operations
// /

data class NotExpr(val base: Expression, override val position: Position? = null) : Expression(position)

data class LogicalOrExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} || ${right.render()}"
}

data class LogicalAndExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} && ${right.render()}"
}

// /
// / Arithmetic operations
// /

data class PlusExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} + ${right.render()}"
}

data class MinusExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} - ${right.render()}"
}

data class MultExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} * ${right.render()}"
}

data class DivExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} / ${right.render()}"
}

data class ExpExpr(var left: Expression, var right: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "${left.render()} ** ${right.render()}"
}

// /
// / Misc
// /

abstract class AssignableExpression(override val position: Position? = null) : Expression(position) {
    abstract fun size(): Int
}

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
}

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
}

data class ArrayAccessExpr(val array: Expression, val index: Expression, override val position: Position? = null) :
    AssignableExpression(position) {
    override fun render(): String {
        return "${this.array.render()}(${index.render()}))"
    }
    override fun size(): Int {
        TODO("size")
    }
}

// A Function call is not distinguishable from an array access
// TODO replace them in the AST during the resolution phase
data class FunctionCall(
    val function: ReferenceByName<Function>,
    val args: List<Expression>,
    override val position: Position? = null
) : Expression(position)

fun dataRefTo(dataDefinition: AbstractDataDefinition) =
        DataRefExpr(ReferenceByName(dataDefinition.name, dataDefinition))

data class NumberOfElementsExpr(val value: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render() = "%ELEM(${value.render()})"
}
