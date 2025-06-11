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

package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.ProfilingParser
import com.smeup.rpgparser.interpreter.lineBounds
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.ProfilingAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingAnnotationResolved
import com.smeup.rpgparser.parsing.ast.ProfilingSpanEndAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingSpanStartAnnotation
import com.smeup.rpgparser.parsing.ast.Statement
import com.smeup.rpgparser.parsing.facade.ProfilingImmutableMap
import com.smeup.rpgparser.parsing.facade.ProfilingMap
import com.strumenta.kolasu.model.Position
import kotlin.collections.forEach

fun ProfilingParser.ProfilingLineContext.toAst(conf: ToAstConfiguration = ToAstConfiguration(), position: Position? = null): ProfilingAnnotation {
    return when (val annotation = this.profilingAnnotation()) {
        is ProfilingParser.ProfilingSpanStartAnnotationContext -> {
            val name = annotation.TELEMETRY_SPAN_ID().text.trim()
            val comment = annotation.TELEMETRY_SPAN_COMMENT()?.text?.trim()
            ProfilingSpanStartAnnotation(name, comment, position)
        }
        is ProfilingParser.ProfilingSpanEndAnnotationContext -> {
            ProfilingSpanEndAnnotation(position)
        }
        else -> TODO(this.text.toString())
    }
}

fun Statement.injectProfilingAnnotations(profiling: ProfilingImmutableMap) {
    // Process the main body statements
    injectProfilingAnnotationsToStatements(
        listOf(this),
        this.position!!.start.line,
        this.position!!.end.line,
        profiling
    )
}

/**
 * Inject profiling annotations in compilation unit.
 *
 * @param profiling The profiling annotations to inject.
 */
fun CompilationUnit.injectProfilingAnnotations(profiling: ProfilingImmutableMap) {
    val toResolve = profiling.map { it.key }.toMutableList()

    // Process the main body statements
    // There is an issue when annotations appear just above the first statement
    // so we want to expand the research area to cover preceding annotations
    // In a mute with no statements, as can happen for program with only
    // D SPEC, the function stmts.position() returns null and then this fragments raises error
    val resolved = this.main.stmts.position()?.let { position ->
        val start = position.start.line.expandStartLineWithAnnotations(profiling)
        injectProfilingAnnotationsToStatements(this.main.stmts, start, position.end.line, profiling)
    } ?: emptyList()

    resolved.forEach {
        toResolve.remove(it.profilingLine)
    }

    if (toResolve.isNotEmpty()) {
        throw RuntimeException("Could not resolve annotations at lines: ${toResolve.joinToString()}")
    }
}

/**
 * Inject profiling annotations to statements considering the range in which they are valid.
 *
 * @param statements The list of statements to which annotations need to be injected.
 * @param start Start line to consider an annotation valid.
 * @param end End line to consider an annotation valid.
 * @param candidates The set of candidate profiling annotations to inject.
 */
fun injectProfilingAnnotationsToStatements(
    statements: List<Statement>,
    start: Int,
    end: Int,
    candidates: ProfilingImmutableMap
): List<ProfilingAnnotationResolved> {
    // Consider only the annotation in the scope
    val filtered: ProfilingImmutableMap = candidates.filterKeys { it in start..end }

    // makes a consumable list of annotation
    val profilingAnnotationsToProcess: ProfilingMap = filtered.toSortedMap()
    val profilingAnnotationsResolved: MutableList<ProfilingAnnotationResolved> = mutableListOf()

    // Visit each statement
    statements.forEach {
        val candidateStartForStatement = it.position!!.start.line.expandStartLineWithAnnotations(profilingAnnotationsToProcess)
        val candidateEndForStatement = it.position!!.end.line.expandEndLineWithAnnotations(profilingAnnotationsToProcess)
        val resolved = it.acceptProfiling(profilingAnnotationsToProcess, candidateStartForStatement, candidateEndForStatement)
        profilingAnnotationsResolved.addAll(resolved)

        resolved.forEach { processed -> profilingAnnotationsToProcess.remove(processed.profilingLine) }
    }

    // at the end the annotationsToProcess collection should be empty
    // otherwise it means the remaining annotations can't be attached
    // to any statement
    profilingAnnotationsToProcess.forEach {
        it.value.error("Could not attach the annotation @line ${it.key}", conf = ToAstConfiguration())
    }

    return profilingAnnotationsResolved
}

/**
 * Expand both the start line bound and the end line bound to include profiling annotations.
 *
 * @param profiling The profiling annotation map.
 */
internal fun Pair<Int, Int>.expandWithAnnotations(profiling: ProfilingImmutableMap): Pair<Int, Int> {
    val (a, b) = this
    val start = a.expandStartLineWithAnnotations(profiling)
    val end = b.expandEndLineWithAnnotations(profiling)
    return start to end
}

/**
 * Expand start line bound to include profiling annotations.
 *
 * @param profiling The profiling annotation map.
 */
internal fun Int.expandStartLineWithAnnotations(profiling: ProfilingImmutableMap): Int {
    var line = this
    while (line - 1 in profiling) --line
    return line
}

/**
 * Expand end line bound to include profiling annotations.
 *
 * @param profiling The profiling annotation map.
 */
internal fun Int.expandEndLineWithAnnotations(profiling: ProfilingImmutableMap): Int {
    var line = this
    while (line + 1 in profiling) ++line
    return line
}

/**
 * Accept profiling annotations in a [com.smeup.rpgparser.parsing.ast.CompositeStatement] body or list of [Statement].
 * Determine the search range automatically.
 *
 * @param body The list of statement to which we should apply profiling annotations.
 * @param profiling The profiling annotation map.
 */
fun acceptProfilingBody(body: List<Statement>, profiling: ProfilingMap): MutableList<ProfilingAnnotationResolved> {
    val (start, end) = body.lineBounds().expandWithAnnotations(profiling)
    return acceptProfilingBody(body, profiling, start, end)
}

/**
 * Accept profiling annotations in a [com.smeup.rpgparser.parsing.ast.CompositeStatement] body or list of [Statement].
 *
 * @param body The list of statement to which we should apply profiling annotations.
 * @param profiling The profiling annotation map.
 * @param start The start line.
 * @param end The end line.
 */
fun acceptProfilingBody(body: List<Statement>, profiling: ProfilingMap, start: Int = 0, end: Int): MutableList<ProfilingAnnotationResolved> {
    val attached: MutableList<ProfilingAnnotationResolved> = mutableListOf()

    // Process the body statements
    body.forEach {
        val (startLine, endLine) = it.boundsIncludingProfiling(profiling)
        if (startLine < start || endLine > end) return@forEach

        val accepted = it.acceptProfiling(profiling, startLine, endLine)
        accepted.forEach { annotation ->
            profiling.remove(annotation.profilingLine)
            attached.add(annotation)
        }
    }

    return attached
}

/**
 * Get the line bounds of the statement considering the profiling annotations surrounding it.
 *
 * @param profiling The profiling annotation map.
 */
internal fun Statement.boundsIncludingProfiling(profiling: ProfilingMap): Pair<Int, Int> {
    val start = this.position!!.start.line.expandStartLineWithAnnotations(profiling)
    val end = this.position!!.end.line.expandEndLineWithAnnotations(profiling)

    return start to end
}