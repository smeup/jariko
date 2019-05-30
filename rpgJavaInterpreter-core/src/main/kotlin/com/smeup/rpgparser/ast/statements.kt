package com.smeup.rpgparser.ast

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.InStatementDataDefinition
import com.smeup.rpgparser.interpreter.StringType
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Derived
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName

interface StatementThatCanDefineData {
    fun dataDefinition() : List<InStatementDataDefinition>
}

enum class AssignmentOperator(val text: String) {
    DIVIDE_ASSIGNMENT("/="),
    NORMAL_ASSIGNMENT("=");
}


abstract class Statement(override val position: Position? = null) : Node(position)

data class ExecuteSubroutine(var subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) : Statement(position)

data class SelectStmt(var cases: List<SelectCase>,
                      var other: SelectOtherClause? = null,
                      override val position: Position? = null) : Statement(position)

data class SelectOtherClause(val body: List<Statement>, override val position: Position? = null) : Node(position)

data class SelectCase(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)

data class EvalStmt(val target: AssignableExpression,
                    var expression: Expression,
                    val operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT,
                    override val position: Position? = null)
    : Statement(position)

data class CallStmt(val expression: Expression, val params: List<PlistParam>,
                    override val position: Position? = null) : Statement(position) , StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> {
        val result = mutableListOf<InStatementDataDefinition>()
        params.forEach() {
            if (it.len != null) {
                //TODO Type
                result.add(InStatementDataDefinition(it.param.name, StringType(it.len), it.position))
            }
        }
        return result
    }
}

data class IfStmt(val condition: Expression, val body: List<Statement>,
                  val elseIfClauses: List<ElseIfClause> = emptyList(),
                  val elseClause: ElseClause? = null,
                  override val position: Position? = null) : Statement(position)

data class ElseClause(val body: List<Statement>, override val position: Position? = null) : Node(position)

data class ElseIfClause(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)

data class SetOnStmt(val choices: List<DataWrapUpChoice>, override val position: Position? = null) : Statement(position)

data class PlistStmt(val params: List<PlistParam>,
                     val isEntry: Boolean,
                     override val position: Position? = null) : Statement(position)

data class PlistParam(val param: ReferenceByName<AbstractDataDefinition>, val len: Long?, override val position: Position? = null) : Node(position)

data class ClearStmt(val value: Expression,
                     @Derived val dataDefinition: InStatementDataDefinition? = null,
                     override val position: Position? = null) : Statement(position), StatementThatCanDefineData {
    override fun dataDefinition() : List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }
}

data class DisplayStmt(val factor1: Expression?, val response: Expression?, override val position: Position? = null) : Statement(position)

data class DoStmt(
        val endLimit: Expression,
        val index: AssignableExpression?,
        val body: List<Statement>,
        val startLimit: Expression = IntLiteral(1),
        override val position: Position? = null) : Statement(position)

data class LeaveStmt(override val position: Position? = null) : Statement(position)

data class IterStmt(override val position: Position? = null) : Statement(position)

data class OtherStmt(override val position: Position? = null) : Statement(position)

data class ForStmt(
        var init: Expression,
        val endValue: Expression,
        val body: List<Statement>, override val position: Position? = null) : Statement(position) {
    fun iterDataDefinition(): AbstractDataDefinition {
        if (init is AssignmentExpr) {
            if ((init as AssignmentExpr).target is DataRefExpr) {
                return ((init as AssignmentExpr).target as DataRefExpr).variable.referred!!
            } else {
                throw UnsupportedOperationException()
            }
        } else {
            throw UnsupportedOperationException()
        }
    }
}
