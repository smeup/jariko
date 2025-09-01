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

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CallStmt
import com.smeup.rpgparser.parsing.ast.CompositeStatement
import com.smeup.rpgparser.parsing.ast.ExecuteSubroutine
import com.smeup.rpgparser.parsing.ast.ProfilingAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingAnnotationAttachStrategy
import com.smeup.rpgparser.parsing.ast.ProfilingSpanEndAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingSpanStartAnnotation
import com.smeup.rpgparser.parsing.ast.Statement
import com.smeup.rpgparser.parsing.facade.relative
import com.smeup.rpgparser.utils.peekOrNull

/**
 * Get profiling annotations attached to the following statement.
 */
internal fun Statement.getAttachedBeforeAnnotations() =
    this.profilingAnnotations.filter {
        it.attachStrategy ==
            ProfilingAnnotationAttachStrategy.AttachToNext
    }

/**
 * Get profiling annotations attached to the previous statement.
 */
internal fun Statement.getAttachedAfterAnnotations() =
    this.profilingAnnotations.filter {
        it.attachStrategy ==
            ProfilingAnnotationAttachStrategy.AttachToPrevious
    }

/**
 * Get profiling annotations attached with a given strategy.
 *
 * @param strategy The attachment strategy.
 */
internal fun Statement.getProfilingAnnotations(strategy: ProfilingAnnotationAttachStrategy) =
    when (strategy) {
        ProfilingAnnotationAttachStrategy.AttachToNext -> this.getAttachedBeforeAnnotations()
        ProfilingAnnotationAttachStrategy.AttachToPrevious -> this.getAttachedAfterAnnotations()
    }

/**
 * Execute a set of [ProfilingAnnotation]s.
 *
 * @param annotations The annotations to execute.
 */
internal fun InterpreterCore.executeProfiling(annotations: List<ProfilingAnnotation>) {
    annotations.forEach { executeProfiling(it) }
}

/**
 * Execute a [ProfilingAnnotation].
 *
 * @param annotation The profiling annotation to execute.
 */
internal fun InterpreterCore.executeProfiling(annotation: ProfilingAnnotation) {
    val configuration = getConfiguration()
    val callback = configuration.jarikoCallback
    val programName = getInterpretationContext().currentProgramName
    val program = MainExecutionContext.getProgramStack().peekOrNull()
    val (_, sourceReference) = annotation.position!!.relative(programName, program?.cu?.copyBlocks)
    when (annotation) {
        is ProfilingSpanStartAnnotation -> {
            val description = annotation.description

            val trace = RpgTrace(sourceReference.sourceId, description, sourceReference.relativeLine)
            callback.startRpgTrace(trace)
        }
        is ProfilingSpanEndAnnotation -> {
            val description = annotation.name ?: ""
            val captures = annotation.captures.associate { it to this.get(it).asString().value }

            val trace = RpgTrace(sourceReference.sourceId, description, sourceReference.relativeLine, captures)
            callback.finishRpgTrace(trace)
        }
    }
    renderLog {
        val logSource = { LogSourceData(programName, annotation.startLine()) }
        LazyLogEntry.produceProfiling(annotation, logSource)
    }
}

internal fun InterpreterCore.toTracePoint(statement: Statement): JarikoTrace? =
    when (statement) {
        is CallStmt ->
            JarikoTrace(
                kind = JarikoTraceKind.CallStmt,
                description = eval(statement.expression).asString().value.trim(),
            )
        is ExecuteSubroutine ->
            JarikoTrace(
                kind = JarikoTraceKind.ExecuteSubroutine,
                description = statement.subroutine.name,
            )
        is CompositeStatement ->
            JarikoTrace(
                kind = JarikoTraceKind.CompositeStatement,
                description = statement.loggableEntityName,
            )
        else -> null
    }
