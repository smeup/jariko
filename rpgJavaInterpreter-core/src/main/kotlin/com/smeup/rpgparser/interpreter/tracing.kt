/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.JarikoCallback

/**
 * Kind of a trace
 */
enum class JarikoTraceKind {
    Parsing,
    SymbolTable,
    CompositeStatement,
    CallStmt,
    ExecuteSubroutine,
    FunctionCall,
    MainExecutionContext,
    RpgProgram
}

/**
 * A trace emitted by Jariko for telemetry purposes
 */
data class JarikoTrace(
    val kind: JarikoTraceKind,
    val description: String = ""
)

/**
 * A trace emitted by an RPG program for telemetry purposes
 */
data class RpgTrace(
    val program: String,
    val description: String? = null
)

internal fun <T> JarikoCallback.traceBlock(trace: JarikoTrace, block: () -> T): T {
    startJarikoTrace(trace)
    try {
        return block()
    } catch (e: Exception) {
        throw e
    } finally {
        finishJarikoTrace()
    }
}
