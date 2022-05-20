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

package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.ErrorEventSource
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.facade.adaptInFunctionOf
import com.smeup.rpgparser.parsing.facade.relative
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import org.antlr.v4.runtime.ParserRuleContext

open class ParseTreeToAstError(message: String, cause: Throwable? = null) : IllegalStateException(message, cause) {
    constructor(message: String) : this(message = message, cause = null)
}

internal fun Throwable.fireErrorEvent(position: Position?): Throwable {
    getParseTreeToAstErrors().add(this)
    val programNameToCopyBlocks = getProgramNameToCopyBlocks()
    val sourceReference = position?.relative(programNameToCopyBlocks.first, programNameToCopyBlocks.second)?.second
    val errorEvent = ErrorEvent(
        error = this,
        errorEventSource = ErrorEventSource.Parser,
        absoluteLine = position?.start?.line,
        sourceReference = sourceReference
    )
    MainExecutionContext.getConfiguration().jarikoCallback.onError(errorEvent)
    return this
}

internal fun notImplementOperationException(message: String): IllegalStateException {
    return ParseTreeToAstError("An operation is not implemented: $message")
}

internal fun getParseTreeToAstErrors() = MainExecutionContext.getAttributes()
    .getOrPut("com.smeup.rpgparser.parsing.parsetreetoast.parseTreeToAstErrors") { mutableListOf<Throwable>() } as MutableList<Throwable>

internal fun Node.error(message: String? = null, cause: Throwable? = null): Nothing {
    val position = this.position?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    if (cause != null && cause is ParseTreeToAstError) {
        cause
    } else {
        IllegalStateException(
            message?.let { "$message at: $position" } ?: "Error at: $position",
            cause?.let { cause }
        )
    }.let { error ->
        if (this is CompilationUnit) {
            throw error
        } else {
            throw error.fireErrorEvent(this.position)
        }
    }
}

internal fun Node.todo(message: String? = null): Nothing {
    val pref = message?.let {
        "$message at "
    } ?: "Error at "
    val position = this.position?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    notImplementOperationException("${pref}Position: $position").let { error ->
        if (this is CompilationUnit) {
            throw error
        } else {
            throw error.fireErrorEvent(this.position)
        }
    }
}

internal fun ParserRuleContext.todo(message: String? = null, conf: ToAstConfiguration): Nothing {
    val pref = message?.let {
        "$message at"
    } ?: "Error at"
    val position = toPosition(conf.considerPosition)?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    val myMessage = "$pref $position ${this.javaClass.name}"
    throw notImplementOperationException(myMessage).fireErrorEvent(toPosition(conf.considerPosition))
}

internal fun ParserRuleContext.error(message: String? = null, cause: Throwable? = null, conf: ToAstConfiguration): Nothing {
    val pref = message?.let {
        "$message at: "
    } ?: "Error at: "
    val position = toPosition(conf.considerPosition)?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    throw if (cause != null && cause is ParseTreeToAstError) {
        cause
    } else {
        ParseTreeToAstError(
            "$pref$position ${this.javaClass.name}",
            cause
        )
    }.fireErrorEvent(toPosition(conf.considerPosition))
}
