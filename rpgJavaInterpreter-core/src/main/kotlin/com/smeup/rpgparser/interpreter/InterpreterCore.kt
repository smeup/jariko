/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.Record
import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.*
import java.util.*

/**
 * Expose interpreter core method that could be useful in statements logic implementation
 **/
interface InterpreterCore {
    fun getStatus(): InterpreterStatus
    fun getInterpretationContext(): InterpretationContext
    fun getSystemInterface(): SystemInterface
    fun getIndicators(): HashMap<IndicatorKey, BooleanValue>
    fun getKlists(): HashMap<String, List<String>>
    fun getGlobalSymbolTable(): ISymbolTable
    fun getLocalizationContext(): LocalizationContext
    fun renderLog(renderer: LazyLogEntry)
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

internal fun ErrorEvent.pushRuntimeErrorEvent() {
    getErrorEventStack().push(this)
}

internal fun popRuntimeErrorEvent() = getErrorEventStack().pop()

private fun getErrorEventStack() = MainExecutionContext.getAttributes().computeIfAbsent("errorEventStack") { Stack<ErrorEvent>() } as Stack<ErrorEvent>