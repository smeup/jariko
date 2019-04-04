package com.smeup.rpgparser

import com.smeup.rpgparser.DataType.DATA_STRUCTURE
import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import java.util.*

enum class DataType {
    SINGLE,
    BOOLEAN,
    DATA_STRUCTURE
}

open class AbstractDataDefinition(override val name: String,
    open val size: Int?,
    override val position: Position? = null) : Node(position), Named

class DataDefinition(override val name: String,
                     val dataType: DataType,
                     override val size: Int?,
                     val decimals: Int = 0,
                     val arrayLength: Expression? = null,
                     val fields: List<FieldDefinition>? = null,
                     val like: Expression? = null,
                     override val position: Position? = null) : AbstractDataDefinition(name, size, position) {
    init {
        require((fields != null) == (dataType == DATA_STRUCTURE))
                { "Fields should be sent always and only for data structures" }
    }

    override fun toString(): String {
        return "DataDefinition($name, $dataType, $size)"
    }

}

data class FieldDefinition(override val name: String,
                      override val size: Int,
                      override val position: Position? = null) : AbstractDataDefinition(name, size, position)

data class CompilationUnit(val dataDefinitions: List<DataDefinition>,
                           val main: MainBody,
                           val subroutines: List<Subroutine>,
                           override val position: Position?) : Node(position) {

    val dataDefinitonsAndFields : List<AbstractDataDefinition>
        get() {
            val res = LinkedList<AbstractDataDefinition>()
            res.addAll(dataDefinitions)
            dataDefinitions.forEach { it.fields?.let { res.addAll(it) } }
            return res
        }

    fun hasDataDefinition(name: String) = dataDefinitions.any { it.name == name }

    fun getDataDefinition(name: String) = dataDefinitions.first { it.name == name }
}

data class MainBody(val stmts: List<Statement>, override val position: Position? = null) : Node(position)

class Subroutine(override val name: String, val stmts: List<Statement>, override val position: Position? = null) : Named, Node(position)
class Function(override val name: String, override val position: Position? = null) : Named, Node(position)

//
// Expressions
//

abstract class Expression(override val position: Position? = null) : Node(position) {
    open fun render() : String {
        TODO(this.javaClass.canonicalName)
    }
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
data class DataRefExpr(val variable: ReferenceByName<AbstractDataDefinition>, override val position: Position? = null) : Expression(position) {
    override fun render() = variable.name
}

data class EqualityExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} = ${right.render()}"
}
data class GreaterThanExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} > ${right.render()}"
}
data class GreaterEqualThanExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} >= ${right.render()}"
}
data class LessThanExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} < ${right.render()}"
}
data class LessEqualThanExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} <= ${right.render()}"
}
data class DifferentThanExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} <> ${right.render()}"
}

data class ArrayAccessExpr(val array: Expression, val index: Expression, override val position: Position? = null) : Expression(position)
data class FunctionCall(val function: ReferenceByName<Function>, val args: List<Expression>, override val position: Position? = null) : Expression(position)
data class NotExpr(val base: Expression, override val position: Position? = null) : Expression(position)
data class LogicalOrExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} || ${right.render()}"
}
data class LogicalAndExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} && ${right.render()}"
}

data class PlusExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} + ${right.render()}"
}
data class MinusExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} - ${right.render()}"
}
data class MultExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} * ${right.render()}"
}
data class DivExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position) {
    override fun render() = "${left.render()} / ${right.render()}"
}


//
// Built-in functions
//

data class LookupExpr(val value: Expression, val array: Expression, override val position: Position? = null) : Expression(position)
data class ScanExpr(val value: Expression, val source: Expression, val start: Expression? = null, override val position: Position? = null) : Expression(position)
data class TranslateExpr(val from: Expression, val to: Expression, val string: Expression,
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

//
// Statements
//

abstract class Statement(override val position: Position? = null) : Node(position)
data class ExecuteSubroutine(val subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) : Statement(position)
data class SelectStmt(val cases: List<SelectCase>,
                      val other: SelectOtherClause? = null,
                      override val position: Position? = null) : Statement(position)
data class SelectOtherClause(val body: List<Statement>, override val position: Position? = null) : Node(position)
data class SelectCase(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)
data class EvalStmt(val expression: Expression, override val position: Position? = null) : Statement(position)
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
data class ClearStmt(val value: Expression, override val position: Position? = null) : Statement(position)
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
