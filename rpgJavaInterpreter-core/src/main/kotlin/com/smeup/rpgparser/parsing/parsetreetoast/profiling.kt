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

fun Statement.injectProfilingAnnotation(profiling: ProfilingImmutableMap): List<ProfilingAnnotationResolved> {
    // Process the main body statements
    val statements = listOf(this)
    val resolved = injectProfilingAnnotationToStatements(
        statements,
        this.position!!.start.line,
        this.position!!.end.line,
        profiling
    )

    return resolved
}

fun CompilationUnit.injectProfilingAnnotation(profiling: ProfilingImmutableMap): List<ProfilingAnnotationResolved> {
    val resolved: MutableList<ProfilingAnnotationResolved> = mutableListOf()
    // Process the main body statements
    // There is an issue when annotations appear just above the first statement
    // so we want to expand the research area to cover preceding annotations
    // In a mute with no statements, as can happens for program with only
    // D SPEC, the function stmts.position() returns null and than this fragments raises error
    this.main.stmts.position()?.let { position ->
        val start = expandStartLineWhenNeeded(position.start.line, profiling)
        val injected = injectProfilingAnnotationToStatements(this.main.stmts, start, position.end.line, profiling)
        resolved.addAll(injected)
    }

    // Process subroutines body statements
    // TODO: Check if this is needed
    /*this.subroutines.forEach {
        resolved.addAll(injectProfilingAnnotationToStatements(it.stmts,
            it.position!!.start.line,
            it.position.end.line,
            profiling))
    }*/

    return resolved
}

fun injectProfilingAnnotationToStatements(
    statements: List<Statement>,
    start: Int,
    end: Int,
    map: ProfilingImmutableMap
): List<ProfilingAnnotationResolved> {
    // Consider only the annotation in the scope
    val filtered: ProfilingImmutableMap = map.filterKeys {
        it in start..end
    }

    // makes a consumable list of annotation
    val profilingAnnotationsToProcess: ProfilingMap = filtered.toSortedMap()
    val profilingAnnotationsResolved: MutableList<ProfilingAnnotationResolved> = mutableListOf()

    // Visit each statement
    statements.forEach {
        var candidateStartForStatement = it.position!!.start.line
        while (candidateStartForStatement - 1 in profilingAnnotationsToProcess) {
            --candidateStartForStatement
        }

        var candidateEndForStatement = it.position!!.end.line
        while (candidateEndForStatement + 1 in profilingAnnotationsToProcess) {
            ++candidateEndForStatement
        }
        val resolved = it.acceptProfiling(profilingAnnotationsToProcess, candidateStartForStatement, candidateEndForStatement)
        profilingAnnotationsResolved.addAll(resolved)

        resolved.forEach {
            profilingAnnotationsToProcess.remove(it.profilingLine)
        }
    }
    // at the end the annotationsToProcess collection should be empty
    // otherwise it means the remaining annotations can't be attached
    // to any statement

    profilingAnnotationsToProcess.forEach {
        print("Could not attach the annotation @line ${it.key}")
    }

    return profilingAnnotationsResolved
}

private fun expandStartLineWhenNeeded(startLine: Int, profiling: ProfilingImmutableMap): Int {
    var line = startLine
    while (line - 1 in profiling.keys) {
        --line
    }
    return line
}

fun acceptProfilingBody(body: List<Statement>, profiling: ProfilingMap, start: Int = 0, end: Int): MutableList<ProfilingAnnotationResolved> {
    val attached: MutableList<ProfilingAnnotationResolved> = mutableListOf()

    // Process the body statements
    body.forEach {
        val toRemove = it.acceptProfiling(profiling, start, end)
        toRemove.forEach {
            profiling.remove(it.profilingLine)
            attached.add(it)
        }
    }

    return attached
}
