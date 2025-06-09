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

import com.smeup.rpgparser.parsing.ast.CallStmt
import com.smeup.rpgparser.parsing.ast.CompositeStatement
import com.smeup.rpgparser.parsing.ast.ExecuteSubroutine
import com.smeup.rpgparser.parsing.ast.ProfilingAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingAnnotationAttachStrategy
import com.smeup.rpgparser.parsing.ast.ProfilingSpanEndAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingSpanStartAnnotation
import com.smeup.rpgparser.parsing.ast.Statement

/**
 * Get profiling annotations attached to the following statement.
 */
internal fun Statement.getAttachedBeforeAnnotations() = this.profilingAnnotations.filter { it.attachStrategy == ProfilingAnnotationAttachStrategy.AttachToNext }

/**
 * Get profiling annotations attached to the previous statement.
 */
internal fun Statement.getAttachedAfterAnnotations() = this.profilingAnnotations.filter { it.attachStrategy == ProfilingAnnotationAttachStrategy.AttachToPrevious }

/**
 * Get profiling annotations attached with a given strategy.
 *
 * @param strategy The attachment strategy.
 */
internal fun Statement.getProfilingAnnotations(strategy: ProfilingAnnotationAttachStrategy) = when (strategy) {
    ProfilingAnnotationAttachStrategy.AttachToNext -> this.getAttachedBeforeAnnotations()
    ProfilingAnnotationAttachStrategy.AttachToPrevious -> this.getAttachedAfterAnnotations()
}

/**
 * Execute a set of [ProfilingAnnotation]s attached to a statement.
 *
 * @param statement The statement.
 * @param annotations The annotations to execute.
 */
internal fun InterpreterCore.executeProfiling(statement: Statement, annotations: List<ProfilingAnnotation>) {
    annotations.forEach { executeProfiling(it, statement.position?.start?.line ?: 0) }
}

/**
 * Execute a [ProfilingAnnotation].
 *
 * @param annotation The profiling annotation to execute.
 */
internal fun InterpreterCore.executeProfiling(annotation: ProfilingAnnotation, line: Int) {
    val configuration = getConfiguration()
    val programName = getInterpretationContext().currentProgramName
    when (annotation) {
        is ProfilingSpanStartAnnotation -> {
            val callback = configuration.jarikoCallback
            val description = annotation.description
            val trace = RpgTrace(programName, description, line)
            callback.startRpgTrace(trace)

            renderLog {
                val logSource = { LogSourceData(programName, annotation.startLine()) }
                LazyLogEntry.produceProfiling(annotation, logSource)
            }
        }
        is ProfilingSpanEndAnnotation -> {
            val callback = configuration.jarikoCallback
            callback.finishRpgTrace()

            renderLog {
                val logSource = { LogSourceData(programName, annotation.startLine()) }
                LazyLogEntry.produceProfiling(annotation, logSource)
            }
        }
    }
}

internal fun InterpreterCore.toTracePoint(statement: Statement): JarikoTrace? {
    return when (statement) {
        is CallStmt -> JarikoTrace(
            kind = JarikoTraceKind.CallStmt,
            description = eval(statement.expression).asString().value.trim()
        )
        is ExecuteSubroutine -> JarikoTrace(
            kind = JarikoTraceKind.ExecuteSubroutine,
            description = statement.subroutine.name
        )
        is CompositeStatement -> JarikoTrace(
            kind = JarikoTraceKind.CompositeStatement,
            description = statement.loggableEntityName
        )
        else -> null
    }
}
