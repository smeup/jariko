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

package com.strumenta.kolasu.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The Abstract Syntax Tree will be constituted by instances of Node.
 */
@Serializable
open class Node(@Transient open val position: Position? = null) {
    var parent: Node? = null
}

/**
 * This should be used for all relations which are secondary, i.e., they are calculated from other relations.
 * This annotation it needs to avoid that the [Node.process] executes multiple operations on the node.
 * For example suppose to have a data class `MyNode` that extends `Node` and which has two property `thenBody` and `elseBody`, both
 * collection of nodes.
 * Suppose that you introduce a third computed property `body` which joins thenBody` and `elseBody`.
 * If you call `myNode.process({yourOperation})` yourOperation will be called foreach node two times, first af all, for all nodes in
 * `thenBody`, than for all nodes in `elseBody`, and finally for all nodes in `body`.
 * To face this issue, you will annotate `body`
 */
annotation class Derived

/**
 * This will be used to mark all the properties that returns a Node or a list of Node which are not
 * contained by the Node having the properties, they are just references
 */
annotation class Link

/**
 * An entity which has a name.
 */
interface Named {
    val name: String?
}

/**
 * A reference associated by using a name.
 */
@Serializable
data class ReferenceByName<N>(val name: String, var referred: N? = null) where N : Named {
    override fun toString(): String {
        return if (referred == null) {
            "Ref($name)[Unsolved]"
        } else {
            "Ref($name)[Solved]"
        }
    }

    override fun hashCode(): Int {
        return name.hashCode() * (7 + if (referred == null) 1 else 2)
    }

    val resolved: Boolean
        get() = referred != null
}

fun <N> ReferenceByName<N>.tryToResolve(candidates: List<N>, caseInsensitive: Boolean = false): Boolean where N : Named {
    val res = candidates.find { if (it.name == null) false else it.name.equals(this.name, caseInsensitive) }
    this.referred = res
    return res != null
}

fun <N> ReferenceByName<N>.tryToResolve(possibleValue: N?): Boolean where N : Named {
    return if (possibleValue == null) {
        false
    } else {
        this.referred = possibleValue
        true
    }
}
