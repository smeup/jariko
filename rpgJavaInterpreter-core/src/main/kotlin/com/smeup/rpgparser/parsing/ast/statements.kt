package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.InStatementDataDefinition
import com.smeup.rpgparser.interpreter.KListType
import com.smeup.rpgparser.parsing.parsetreetoast.acceptBody
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.model.*

interface StatementThatCanDefineData {
    fun dataDefinition(): List<InStatementDataDefinition>
}

enum class AssignmentOperator(val text: String) {
    NORMAL_ASSIGNMENT("="),
    PLUS_ASSIGNMENT("+="),
    MINUS_ASSIGNMENT("-="),
    MULT_ASSIGNMENT("*="),
    DIVIDE_ASSIGNMENT("/="),
    EXP_ASSIGNMENT("**=");
}

abstract class Statement(
    override val position: Position? = null,
    var muteAnnotations: MutableList<MuteAnnotation> = mutableListOf()
) : Node(position) {
    open fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int = 0, end: Int): MutableList<MuteAnnotationResolved> {

        // List of mutes successully attached to the statements
        val mutesAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        // Extracts the annotation declared before the statement
        val muteToProcess = mutes.filterKeys {
            it < this.position!!.start.line
        }

        muteToProcess.forEach { (line, mute) ->
            this.muteAnnotations.add(mute.toAst(
                    position = pos(line, this.position!!.start.column, line, this.position!!.end.column))
            )
            mutesAttached.add(MuteAnnotationResolved(line, this.position!!.start.line))
        }

        return mutesAttached
    }
}

data class ExecuteSubroutine(var subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) : Statement(position)

data class SelectStmt(
    var cases: List<SelectCase>,
    var other: SelectOtherClause? = null,
    override val position: Position? = null
) : Statement(position) {
    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {

        val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        cases.forEach {
            muteAttached.addAll(
                    acceptBody(it.body, mutes, it.position!!.start.line, it.position.end.line)
            )
        }

        if (other != null) {
            muteAttached.addAll(
                    acceptBody(other!!.body, mutes, other!!.position!!.start.line, other!!.position!!.end.line)
            )
        }

        return muteAttached
    }
}

data class SelectOtherClause(val body: List<Statement>, override val position: Position? = null) : Node(position)

data class SelectCase(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)

data class EvalStmt(
    val target: AssignableExpression,
    var expression: Expression,
    val operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT,
    override val position: Position? = null
) :
    Statement(position)

data class SubDurStmt(
    val factor1: Expression?,
    val target: AssignableExpression,
    val factor2: Expression,
    override val position: Position? = null
) :
    Statement(position)

data class MoveStmt(
    val target: AssignableExpression,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position)

data class MoveLStmt(
    val target: AssignableExpression,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position)

// TODO add other parameters
data class ChainStmt(
    val searchArg: Expression, // Factor1
    val name: String, // Factor 2
    override val position: Position? = null
) :
    Statement(position)

data class ReadEqualStmt(
    val searchArg: Expression?, // Factor1
    val name: String, // Factor 2
    override val position: Position? = null
) :
    Statement(position)

data class CheckStmt(
    val comparatorString: Expression, // Factor1
    val baseString: Expression,
    val start: Int = 1,
    val wrongCharPosition: AssignableExpression?,
    override val position: Position? = null
) : Statement(position)

data class CallStmt(
    val expression: Expression,
    val params: List<PlistParam>,
    val errorIndicator: Int? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> {
        return params.mapNotNull() {
            it.dataDefinition
        }
    }
}

data class KListStmt
    private constructor(val name: String, val fields: List<String>, override val position: Position?) : Statement(position), StatementThatCanDefineData {
    companion object {
        operator fun invoke(name: String, fields: List<String>, position: Position? = null): KListStmt {
            return KListStmt(name.toUpperCase(), fields, position)
        }
    }
    override fun dataDefinition(): List<InStatementDataDefinition> = listOf(InStatementDataDefinition(name, KListType))
}

data class IfStmt(
    val condition: Expression,
    val body: List<Statement>,
    val elseIfClauses: List<ElseIfClause> = emptyList(),
    val elseClause: ElseClause? = null,
    override val position: Position? = null
) : Statement(position) {

    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
        // check if the annotation is just before the ELSE
        val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        // Process the body statements
        muteAttached.addAll(
                acceptBody(body, mutes, this.position!!.start.line, this.position.end.line)
        )

        // Process the ELSE IF
        elseIfClauses.forEach {
            muteAttached.addAll(
                    acceptBody(it.body, mutes, it.position!!.start.line, it.position.end.line)
            )
        }

        // Process the ELSE
        if (elseClause != null) {
            muteAttached.addAll(
                    acceptBody(elseClause.body, mutes, elseClause.position!!.start.line, elseClause.position.end.line)
            )
        }

        return muteAttached
    }
}

data class ElseClause(val body: List<Statement>, override val position: Position? = null) : Node(position)

data class ElseIfClause(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)

data class SetOnStmt(val choices: List<DataWrapUpChoice>, override val position: Position? = null) : Statement(position)

// A Plist is a list of parameters
data class PlistStmt(
    val params: List<PlistParam>,
    val isEntry: Boolean,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> = params.mapNotNull { it.dataDefinition }
}

data class PlistParam(
    val param: ReferenceByName<AbstractDataDefinition>,
                      // TODO @Derived????
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Node(position)

data class ClearStmt(
    val value: Expression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }
}

// TODO add real implementation
data class CompStmt(override val position: Position? = null) : Statement(position)

data class ZAddStmt(
    val target: AssignableExpression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position), StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }
}

data class TimeStmt(
    val value: Expression,
    override val position: Position? = null
) : Statement(position)

data class DisplayStmt(val factor1: Expression?, val response: Expression?, override val position: Position? = null) : Statement(position)

data class DoStmt(
    val endLimit: Expression,
    val index: AssignableExpression?,
    val body: List<Statement>,
    val startLimit: Expression = IntLiteral(1),
    override val position: Position? = null
) : Statement(position) {
    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
        // TODO check if the annotation is the last statement
        return acceptBody(body, mutes, start, end)
    }
}

data class DowStmt(
    val endExpression: Expression,
    val body: List<Statement>,
    override val position: Position? = null
) : Statement(position)

data class DouStmt(
    val endExpression: Expression,
    val body: List<Statement>,
    override val position: Position? = null
) : Statement(position)

data class LeaveStmt(override val position: Position? = null) : Statement(position)

data class IterStmt(override val position: Position? = null) : Statement(position)

data class OtherStmt(override val position: Position? = null) : Statement(position)

data class ForStmt(
    var init: Expression,
    val endValue: Expression,
    val byValue: Expression,
    val downward: Boolean = false,
    val body: List<Statement>,
    override val position: Position? = null
) : Statement(position) {
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
    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
        // TODO check if the annotation is the last statement
        return acceptBody(body, mutes, start, end)
    }
}
