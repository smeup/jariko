package com.smeup.rpgparser

import com.smeup.rpgparser.DataType.DATA_STRUCTURE
import me.tomassetti.kolasu.model.Named
import me.tomassetti.kolasu.model.Node
import me.tomassetti.kolasu.model.Position
import me.tomassetti.kolasu.model.ReferenceByName
import javax.xml.crypto.Data

enum class DataType {
    SINGLE,
    BOOLEAN,
    DATA_STRUCTURE
}

class DataDefinition(override val name: String,
                     val dataType: DataType,
                     val size: Int,
                     val decimals: Int = 0,
                     val arrayLength: Expression = IntLiteral(1),
                     val fields: List<FieldDefinition>? = null,
                     override val position: Position?) : Node(position), Named {
    init {
        require((fields != null) == (dataType == DATA_STRUCTURE))
                { "Fields should be sent always and only for data structures" }
    }
}

data class FieldDefinition(override val name: String,
                      val size: Int,
                      override val position: Position? = null) : Node(position), Named

class CompilationUnit(val dataDefinitons: List<DataDefinition>, override val position: Position?) : Node(position) {
    fun hasDataDefinition(name: String) = dataDefinitons.any { it.name == name }

    fun getDataDefinition(name: String) = dataDefinitons.first { it.name == name }
}

class Subroutine(override val name: String, override val position: Position? = null) : Named, Node(position)
class Function(override val name: String, override val position: Position? = null) : Named, Node(position)

//
// Expressions
//

abstract class Expression(override val position: Position? = null) : Node(position)

open class NumberLiteral(override val position: Position? = null) : Expression(position)
data class IntLiteral(val value: Long, override val position: Position? = null) : NumberLiteral(position)
data class RealLiteral(val value: Double, override val position: Position? = null) : NumberLiteral(position)

data class StringLiteral(val value: String, override val position: Position? = null) : Expression(position)

data class NumberOfElementsExpr(val value: Expression, override val position: Position? = null) : Expression(position)
data class DataRefExpr(val variable: ReferenceByName<DataDefinition>, override val position: Position? = null) : Expression(position)

data class EqualityExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class GreaterThanExpr(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)

data class ArrayAccessExpr(val array: Expression, val index: Expression, override val position: Position? = null) : Expression(position)
data class FunctionCall(val function: ReferenceByName<Function>, val args: List<Expression>, override val position: Position? = null) : Expression(position)

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

