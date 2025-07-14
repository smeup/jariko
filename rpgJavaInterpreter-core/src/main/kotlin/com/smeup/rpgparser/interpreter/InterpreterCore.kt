/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.Record
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.utils.peekOrNull
import com.strumenta.kolasu.model.Position
import java.util.*

/**
 * Expose interpreter core method that could be useful in statements logic implementation
 **/
interface InterpreterCore {
    fun getConfiguration(): Configuration
    fun getStatus(): InterpreterStatus
    fun getInterpretationContext(): InterpretationContext
    fun getSystemInterface(): SystemInterface
    fun getIndicators(): HashMap<IndicatorKey, BooleanValue>
    fun getGlobalSymbolTable(): ISymbolTable
    fun getLocalizationContext(): LocalizationContext
    fun renderLog(producer: () -> LazyLogEntry?)
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
    fun executeUnwrappedAt(unwrappedStatements: List<UnwrappedStatementData>, offset: Int)
    fun dbFile(name: String, statement: Statement): EnrichedDBFile
    fun toSearchValues(searchArgExpression: Expression, fileMetadata: FileMetadata): List<String>
    fun fillDataFrom(dbFile: EnrichedDBFile, record: Record)
    fun exists(dataName: String): Boolean
    fun dataDefinitionByName(name: String): AbstractDataDefinition?
    fun div(statement: DivStmt): Value
    fun rawRender(values: List<Value>): String
    fun optimizedIntExpression(expression: Expression): () -> Long
    fun enterCondition(index: Value, end: Value, downward: Boolean): Boolean
    fun increment(dataDefinition: AbstractDataDefinition, amount: Long): Value
    fun fireErrorEvent(throwable: Throwable, position: Position?)
    /***
     * This method is called when the interpretation of the first program of the stack is ended
     * There is no warranty that this method is called unless you use:
     * com.smeup.rpgparser.execution.CommandLineProgram.singleCall
     * methods
     */
    fun onInterpretationEnd()
}

internal fun ErrorEvent.pushRuntimeErrorEvent() {
    getErrorEventStack().push(this)
}

internal fun popRuntimeErrorEvent() = getErrorEventStack().pop()
internal fun popRuntimeErrorIfMatches(throwable: Throwable): ErrorEvent? {
    val eventStack = getErrorEventStack()
    val event = eventStack.peekOrNull() ?: return null
    return event.takeIf { it.error == throwable }.apply { eventStack.pop() }
}

private fun getErrorEventStack(): Stack<ErrorEvent> = MainExecutionContext.getAttributes().computeIfAbsent("errorEventStack") { Stack<ErrorEvent>() } as Stack<ErrorEvent>