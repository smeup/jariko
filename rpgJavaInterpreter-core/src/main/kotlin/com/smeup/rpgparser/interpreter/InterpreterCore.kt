package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*

/**
 * Expose interpreter core method that could be useful in statements logic implementation
 **/
interface InterpreterCore {
    val status: InterpreterStatus
    val interpretationContext: InterpretationContext
    val systemInterface: SystemInterface
    val predefinedIndicators: HashMap<IndicatorKey, BooleanValue>
    val klists: HashMap<String, List<String>>
    val globalSymbolTable: SymbolTable
    val localizationContext: LocalizationContext
    fun log(logEntry: () -> LogEntry)
    fun assign(target: AssignableExpression, value: Value): Value
    fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value
    fun assign(target: AssignableExpression, value: Expression, operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT): Value
    fun assignEachElement(target: AssignableExpression, value: Value): Value
    fun assignEachElement(target: AssignableExpression, value: Expression, operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT): Value
    operator fun get(data: AbstractDataDefinition): Value
    operator fun get(dataName: String): Value
    fun setPredefinedIndicators(statement: WithRightIndicators, hi: BooleanValue, lo: BooleanValue, eq: BooleanValue)
    fun eval(expression: Expression): Value
    fun execute(compilationUnit: CompilationUnit, initialValues: Map<String, Value>, reinitialization: Boolean = true)
    fun execute(statements: List<Statement>)
    fun dbFile(name: String, statement: Statement): DBFile
    fun toSearchValues(searchArgExpression: Expression): List<RecordField>
    fun fillDataFrom(record: Record)
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
