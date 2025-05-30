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

package com.smeup.rpgparser.parsing.ast

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The strategy used to attach a profiling annotation to a sibling statement.
 * @see ProfilingAnnotation
 */
enum class ProfilingAnnotationAttachStrategy {
    /** Attach annotation to the statement following the annotation. */
    AttachToNext,

    /** Attach annotation to the statement preceding the annotation. */
    AttachToPrevious
}

/**
 * A profiling annotation.
 */
@Serializable
abstract class ProfilingAnnotation(
    val attachStrategy: ProfilingAnnotationAttachStrategy,
    @Transient override val position: Position? = null
) : Node(position)

/**
 * A span start annotation.
 */
@Serializable
data class ProfilingSpanStartAnnotation(
    val name: String,
    val comment: String?,
    override val position: Position? = null
) :
    ProfilingAnnotation(ProfilingAnnotationAttachStrategy.AttachToNext, position) {
    val description
        get() = comment?.let {
            if (it.isEmpty()) name else "$name - $comment"
        } ?: name
}

/**
 * A span end annotation.
 */
@Serializable
data class ProfilingSpanEndAnnotation(override val position: Position? = null) : ProfilingAnnotation(
    ProfilingAnnotationAttachStrategy.AttachToPrevious, position
)

/**
 * A profiling annotation associated to a statement.
 */
data class ProfilingAnnotationResolved(val profilingLine: Int, val statementLine: Int)