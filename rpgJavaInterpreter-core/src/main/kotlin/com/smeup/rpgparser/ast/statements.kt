package com.smeup.rpgparser.ast

import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.InStatementDataDefinition
import com.smeup.rpgparser.parsetreetoast.toAst
import com.strumenta.kolasu.model.*

interface StatementThatCanDefineData {
    fun dataDefinition() : List<InStatementDataDefinition>
}

enum class AssignmentOperator(val text: String) {
    NORMAL_ASSIGNMENT("="),
    PLUS_ASSIGNMENT("+="),
    MINUS_ASSIGNMENT("-="),
    MULT_ASSIGNMENT("*="),
    DIVIDE_ASSIGNMENT("/="),
    EXP_ASSIGNMENT("**=");
}


abstract class Statement(override val position: Position? = null,
                         var muteAnnotations: MutableList<MuteAnnotation> = mutableListOf()) : Node(position) {
    open fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int = 0) : MutableList<Int> {

        // List of mutes successully attached
        val muteAttached : MutableList<Int> = mutableListOf()

        // Extract the available Mutes
        val muteToProcess = mutes.filterKeys{
            it > start && it < this.position!!.start.line
        }
        if(muteToProcess != null) {
            muteToProcess.forEach { (line, mute) ->
                this.muteAnnotations.add( mute!!.toAst(
                        position = pos( line,this.position!!.start.column,line,
                                this.position!!.end.column))
                )
                muteAttached.add(line)
                // println("MuteComparisonAnnotation @line:${line} attached to statement @line:${this.position!!.start.line}")

            }
        }
        return muteAttached
    }
}

data class ExecuteSubroutine(var subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) : Statement(position)

data class SelectStmt(var cases: List<SelectCase>,
                      var other: SelectOtherClause? = null,
                      override val position: Position? = null) : Statement(position) {
    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int) : MutableList<Int> {

        val muteAttached : MutableList<Int> = mutableListOf()
        cases.forEach {
            val muteToRemove = it.accept( mutes , start )
            muteToRemove.forEach {
                mutes.remove(it)
            }
        }
        if( other != null ) {
            muteAttached.addAll( other!!.accept( mutes , start ) )
        }
        return muteAttached
    }
}


data class SelectOtherClause(val body: List<Statement>, override val position: Position? = null) : Node(position) {
    open fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext> ,start: Int = -1) : MutableList<Int> {

        val muteAttached : MutableList<Int> = mutableListOf()

        body.forEach {
            muteAttached.addAll( it.accept(mutes, start))
        }
        return muteAttached
    }
}

data class SelectCase(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position) {
    open fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int = -1) : MutableList<Int> {

        val muteAttached : MutableList<Int> = mutableListOf()

        body.forEach {
            muteAttached.addAll( it.accept(mutes, start))
        }
        return muteAttached
    }
}

data class EvalStmt(val target: AssignableExpression,
                    var expression: Expression,
                    val operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT,
                    override val position: Position? = null)
    : Statement(position)

data class SubDurStmt(val factor1: Expression?,
                      val target: AssignableExpression,
                      val factor2: Expression,
                      override val position: Position? = null)
    : Statement(position)


data class MoveStmt(val target: AssignableExpression,
                    var expression: Expression,
                    override val position: Position? = null)
    : Statement(position)


data class CallStmt(val expression: Expression, val params: List<PlistParam>, val errorIndicator: Int? = null,
                    override val position: Position? = null) : Statement(position) , StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> {
        return params.mapNotNull() {
            it.dataDefinition
        }
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

data class PlistParam(val param: ReferenceByName<AbstractDataDefinition>,
                      //TODO @Derived????
                      @Derived val dataDefinition: InStatementDataDefinition? = null,
                      override val position: Position? = null) : Node(position)

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

data class TimeStmt(val value: Expression,
                     override val position: Position? = null) : Statement(position)


data class DisplayStmt(val factor1: Expression?, val response: Expression?, override val position: Position? = null) : Statement(position)

data class DoStmt(
        val endLimit: Expression,
        val index: AssignableExpression?,
        val body: List<Statement>,
        val startLimit: Expression = IntLiteral(1),
        override val position: Position? = null) : Statement(position)

data class DowStmt(
        val endExpression: Expression,
        val body: List<Statement>,
        override val position: Position? = null) : Statement(position)

data class LeaveStmt(override val position: Position? = null) : Statement(position)

data class IterStmt(override val position: Position? = null) : Statement(position)

data class OtherStmt(override val position: Position? = null) : Statement(position)

data class ForStmt(
        var init: Expression,
        val endValue: Expression,
        val byValue: Expression,
        val downward: Boolean = false,
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
    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int) : MutableList<Int> {
        // check if the annotation is just before the ENDFOR
        val muteAttached : MutableList<Int> = mutableListOf()
        val toRemove : MutableList<Int> = mutableListOf()
        // Process the annototation between the first and the last line of the LOOP
        val muteToProcess = mutes.filterKeys{
            it  >= this.position!!.start.line && it  < this.position!!.end.line
        }

        // Attach the annotations to the last statement
        muteToProcess.forEach{ (line,mute) ->
            if(line > body.last().position!!.end.line ) {
                body.last().muteAnnotations.add( mute!!.toAst(
                        position = pos( line,body.last().position!!.end.line,line,
                                body.last().position!!.end.column))
                )
                //println("MuteComparisonAnnotation @line:${line} attached to statement @line:${body.last().position!!.end.line}")
                toRemove.add(line)
            }
        }

        toRemove.forEach {
            mutes.remove(it)
        }

        body.forEach {
            val toRemove = it.accept(mutes, start)
            toRemove.forEach {
                mutes.remove(it)
            }
        }

        return muteAttached

    }
}
