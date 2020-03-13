package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.AssignableExpression
import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.WithRightIndicators

/**
 * Expose interpreter core method that could be useful in statements logic implementation
 **/
interface InterpreterCore {
    fun log(logEntry: LogEntry)
    fun assign(target: AssignableExpression, value: Value): Value
    fun interpret(expression: Expression): Value
    operator fun get(data: AbstractDataDefinition): Value
    fun setPredefinedIndicators(statement: WithRightIndicators, hi: BooleanValue, lo: BooleanValue, eq: BooleanValue)
    fun eval(expression: Expression): Value
}
