package com.smeup.rpgparser

import com.strumenta.kolasu.model.*
import java.util.*

data class CompilationUnit(val dataDefinitions: List<DataDefinition>,
                           val main: MainBody,
                           val subroutines: List<Subroutine>,
                           override val position: Position?) : Node(position) {

    private val inStatementsDataDefinitions = LinkedList<InStatementDataDefinition>()

    fun addInStatementDataDefinition(dataDefinition: InStatementDataDefinition) {
        inStatementsDataDefinitions.add(dataDefinition)
    }

    @Derived
    val allDataDefinitions : List<AbstractDataDefinition>
        get() {
            val res = LinkedList<AbstractDataDefinition>()
            res.addAll(dataDefinitions)
            dataDefinitions.forEach { it.fields?.let { res.addAll(it) } }
            res.addAll(inStatementsDataDefinitions)
            return res
        }

    fun hasDataDefinition(name: String) = dataDefinitions.any { it.name == name }

    fun getDataDefinition(name: String) = dataDefinitions.first { it.name == name }

    fun hasAnyDataDefinition(name: String) = allDataDefinitions.any { it.name == name }

    fun getAnyDataDefinition(name: String) = allDataDefinitions.first { it.name == name }
}

data class MainBody(val stmts: List<Statement>, override val position: Position? = null) : Node(position)

class Subroutine(override val name: String, val stmts: List<Statement>, override val position: Position? = null) : Named, Node(position)
class Function(override val name: String, override val position: Position? = null) : Named, Node(position)

//
// Expressions
//

abstract class Expression(override val position: Position? = null) : Node(position) {
    open fun render() = this.javaClass.simpleName
}

abstract class NumberLiteral(override val position: Position? = null) : Expression(position)
data class IntLiteral(val value: Long, override val position: Position? = null) : NumberLiteral(position) {
    override fun render() = value.toString()
}
data class RealLiteral(val value: Double, override val position: Position? = null) : NumberLiteral(position) {
    override fun render() = value.toString()
}

data class StringLiteral(val value: String, override val position: Position? = null) : Expression(position) {
    override fun render() = "\"$value\""
}

data class NumberOfElementsExpr(val value: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "%ELEM(${value.render()})"
}
abstract class FigurativeConstantRef(override val position: Position? = null) : Expression(position)
data class BlanksRefExpr(override val position: Position? = null) : FigurativeConstantRef(position)
data class OnRefExpr(override val position: Position? = null) : FigurativeConstantRef(position)
data class OffRefExpr(override val position: Position? = null) : FigurativeConstantRef(position)

abstract class AssignableExpression(override val position: Position? = null) : Expression(position)

data class DataRefExpr(val variable: ReferenceByName<AbstractDataDefinition>, override val position: Position? = null)
    : AssignableExpression(position) {
    override fun render() = variable.name
}

data class EqualityExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} = ${right.render()}"
}
data class AssignmentExpr(var target: AssignableExpression, var value: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${target.render()} = ${value.render()}"
}
data class GreaterThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} > ${right.render()}"
}
data class GreaterEqualThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} >= ${right.render()}"
}
data class LessThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} < ${right.render()}"
}
data class LessEqualThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} <= ${right.render()}"
}
data class DifferentThanExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} <> ${right.render()}"
}

data class ArrayAccessExpr(val array: Expression, val index: Expression, override val position: Position? = null) : AssignableExpression(position)

// A Function call is not distinguishable from an array access
// TODO replace them in the AST during the resolution phase
data class FunctionCall(val function: ReferenceByName<Function>, val args: List<Expression>, override val position: Position? = null) : Expression(position)
data class NotExpr(val base: Expression, override val position: Position? = null) : Expression(position)
data class LogicalOrExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} || ${right.render()}"
}
data class LogicalAndExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} && ${right.render()}"
}

data class PlusExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} + ${right.render()}"
}
data class MinusExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} - ${right.render()}"
}
data class MultExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} * ${right.render()}"
}
data class DivExpr(var left: Expression, var right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} / ${right.render()}"
}


//
// Built-in functions
//

data class LookupExpr(val value: Expression, val array: Expression, override val position: Position? = null) : Expression(position)
data class ScanExpr(val value: Expression, val source: Expression, val start: Expression? = null, override val position: Position? = null) : Expression(position)
data class TranslateExpr(var from: Expression, var to: Expression, var string: Expression,
                         val startPos: Expression? = null, override val position: Position? = null) : Expression(position)
data class TrimExpr(val value: Expression, val charactersToTrim: Expression? = null,
                    override val position: Position? = null) : Expression(position)
data class SubstExpr(val string: Expression, val start: Expression,
                     val length: Expression? = null, override val position: Position? = null)
    : Expression(position)
data class LenExpr(val value: Expression, override val position: Position? = null)
    : Expression(position)
data class PredefinedIndicatorExpr(val index: Int, override val position: Position? = null)
    : Expression(position)
data class DecExpr(val value: Expression, var intDigits : Expression, val decDigits: Expression, override val position: Position? = null)
    : Expression(position)
data class CharExpr(val value: Expression, override val position: Position? = null)
    : Expression(position)

//
// Statements
//

interface StatementThatCanDefineData {
    fun dataDefinition() : InStatementDataDefinition?
}

abstract class Statement(override val position: Position? = null) : Node(position)
data class ExecuteSubroutine(var subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) : Statement(position)
data class SelectStmt(var cases: List<SelectCase>,
                      var other: SelectOtherClause? = null,
                      override val position: Position? = null) : Statement(position)
data class SelectOtherClause(val body: List<Statement>, override val position: Position? = null) : Node(position)
data class SelectCase(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)
data class EvalStmt(var expression: Expression, override val position: Position? = null) : Statement(position)
data class CallStmt(val expression: Expression, override val position: Position? = null) : Statement(position)
data class IfStmt(val condition: Expression, val body: List<Statement>,
                  val elseIfClauses: List<ElseIfClause> = emptyList(),
                  val elseClause: ElseClause? = null,
                  override val position: Position? = null) : Statement(position)

data class ElseClause(val body: List<Statement>, override val position: Position? = null) : Node(position)
data class ElseIfClause(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)
data class SetOnStmt(val choice: DataWrapUpChoice, override val position: Position? = null) : Statement(position)
data class PlistStmt(val params: List<PlistParam>, override val position: Position? = null) : Statement(position)
data class PlistParam(val paramName: String, override val position: Position? = null) : Node(position)
data class ClearStmt(val value: Expression,
                     @Derived val dataDefinition: InStatementDataDefinition? = null,
                     override val position: Position? = null) : Statement(position), StatementThatCanDefineData {
    override fun dataDefinition() = dataDefinition
}
data class DisplayStmt(val value: Expression, override val position: Position? = null) : Statement(position)
data class DoStmt(val body: List<Statement>, override val position: Position? = null) : Statement(position)
data class LeaveStmt(override val position: Position? = null) : Statement(position)
data class IterStmt(override val position: Position? = null) : Statement(position)
data class OtherStmt(override val position: Position? = null) : Statement(position)
data class ForStmt(
        val init: Expression,
        val endValue: Expression,
        val body: List<Statement>, override val position: Position? = null) : Statement(position)

//data class Assignment(val target: Expression, val value: Expression, override val position: Position? = null) : Node(position)

enum class DataWrapUpChoice {
    LR,
    RT
}

fun dataRefTo(dataDefinition: AbstractDataDefinition) = DataRefExpr(ReferenceByName(dataDefinition.name, dataDefinition))
