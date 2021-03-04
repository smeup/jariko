package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.Record
import com.smeup.rpgparser.parsing.ast.*

/**
 * Expose interpreter core method that could be useful in statements logic implementation
 **/
interface InterpreterCore {
    val status: InterpreterStatus
    val interpretationContext: InterpretationContext
    val systemInterface: SystemInterface
    val indicators: HashMap<IndicatorKey, BooleanValue>
    val klists: HashMap<String, List<String>>
    val globalSymbolTable: ISymbolTable
    val localizationContext: LocalizationContext
    fun log(logEntry: () -> LogEntry)
    fun assign(target: AssignableExpression, value: Value): Value
    fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value
    fun assign(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT
    ): Value

    fun assignEachElement(target: AssignableExpression, value: Value): Value
    fun assignEachElement(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT
    ): Value

    operator fun get(data: AbstractDataDefinition): Value
    operator fun get(dataName: String): Value
    fun setIndicators(statement: WithRightIndicators, hi: BooleanValue, lo: BooleanValue, eq: BooleanValue)
    fun eval(expression: Expression): Value
    fun execute(statements: List<Statement>)
    fun dbFile(name: String, statement: Statement): EnrichedDBFile
    fun toSearchValues(searchArgExpression: Expression, fileMetadata: FileMetadata): List<String>
    fun fillDataFrom(dbFile: EnrichedDBFile, record: Record)
    fun exists(dataName: String): Boolean
    fun dataDefinitionByName(name: String): AbstractDataDefinition?
    fun mult(statement: MultStmt): Value
    fun div(statement: DivStmt): Value
    fun add(statement: AddStmt): Value
    fun sub(statement: SubStmt): Value
    fun rawRender(values: List<Value>): String
    fun optimizedIntExpression(expression: Expression): () -> Long
    fun enterCondition(index: Value, end: Value, downward: Boolean): Boolean
    fun increment(dataDefinition: AbstractDataDefinition, amount: Long): Value
}
