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

import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

private val containmentPropertiesCache = ConcurrentHashMap<Class<*>, Collection<KProperty1<out Node, *>>>()

private val <T : Node> T.containmentProperties: Collection<KProperty1<T, *>>
    @Suppress("UNCHECKED_CAST")
    get() = containmentPropertiesCache.computeIfAbsent(this.javaClass) { clazz ->
        clazz.kotlin.memberProperties
            .filterIsInstance<KProperty1<Node, *>>() // Safely filter properties of Node
            .filter { it.visibility == KVisibility.PUBLIC }
            .filter { it.findAnnotation<Derived>() == null }
            .filter { it.findAnnotation<Link>() == null }
            .filter { it.name != "parent" }
    } as Collection<KProperty1<T, *>>

fun Node.assignParents() {
    this.children.forEach {
        it.parent = this
        it.assignParents()
    }
}

fun Node.process(operation: (Node) -> Unit) {
    operation(this)
    containmentProperties.forEach { p ->
        val v = p.get(this)
        when (v) {
            is Node -> v.process(operation)
            is Collection<*> -> v.forEach { (it as? Node)?.process(operation) }
        }
    }
}

fun Node.find(predicate: (Node) -> Boolean): Node? {
    if (predicate(this)) {
        return this
    }
    containmentProperties.forEach { p ->
        val v = p.get(this)
        when (v) {
            is Node -> {
                val res = v.find(predicate)
                if (res != null) {
                    return res
                }
            }
            is Collection<*> -> v.forEach { (it as? Node)?.let {
                val res = it.find(predicate)
                if (res != null) {
                    return res
                }
            } }
        }
    }
    return null
}

/**
 * Navigate the abstract syntax tree and execute the operation on all the nodes of the given type.
 * */
fun <T : Node> Node.specificProcess(klass: Class<T>, operation: (T) -> Unit) {
    process { if (klass.isInstance(it)) {
        operation(it as T) }
    }
}

fun <T : Node> Node.collectByType(klass: Class<T>): List<T> {
    val res = LinkedList<T>()
    this.specificProcess(klass, { res.add(it) })
    return res
}

fun Node.processConsideringParent(operation: (Node, Node?) -> Unit, parent: Node? = null) {
    operation(this, parent)
    this.containmentProperties.forEach { p ->
        val v = p.get(this)
        when (v) {
            is Node -> v.processConsideringParent(operation, this)
            is Collection<*> -> v.forEach { (it as? Node)?.processConsideringParent(operation, this) }
        }
    }
}

val Node.children: List<Node>
    get() {
        val children = LinkedList<Node>()
        containmentProperties.forEach { p ->
            val v = p.get(this)
            when (v) {
                is Node -> children.add(v)
                is Collection<*> -> v.forEach { if (it is Node) children.add(it) }
            }
        }
        return children.toList()
    }

// TODO reimplement using transformChildren
fun Node.transform(operation: (Node) -> Node, inPlace: Boolean = false): Node {
    if (inPlace) TODO()
    operation(this)
    val changes = HashMap<String, Any>()
    relevantMemberProperties().forEach { p ->
        val v = p.get(this)
        when (v) {
            is Node -> {
                val newValue = v.transform(operation)
                if (newValue != v) changes[p.name] = newValue
            }
            is Collection<*> -> {
                val newValue = v.map { if (it is Node) it.transform(operation) else it }
                if (newValue != v) changes[p.name] = newValue
            }
        }
    }
    var instanceToTransform = this
    if (!changes.isEmpty()) {
        val constructor = this.javaClass.kotlin.primaryConstructor!!
        val params = HashMap<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if (changes.containsKey(param.name)) {
                params[param] = changes[param.name]
            } else {
                params[param] = this.javaClass.kotlin.memberProperties.find { param.name == it.name }!!.get(this)
            }
        }
        instanceToTransform = constructor.callBy(params)
    }
    return operation(instanceToTransform)
}

class ImmutablePropertyException(property: KProperty<*>, node: Node) :
    RuntimeException("Cannot mutate property '${property.name}' of node $node (class: ${node.javaClass.canonicalName})")

fun Node.transformChildren(operation: (Node) -> Node, inPlace: Boolean = false): Node {
    val changes = HashMap<String, Any>()
    relevantMemberProperties().forEach { p ->
        val v = p.get(this)
        when (v) {
            is Node -> {
                val newValue = operation(v)
                if (newValue != v) {
                    if (inPlace) {
                        if (p is KMutableProperty<*>) {
                            p.setter.call(this, newValue)
                        } else {
                            /**
                             * The following exception is raised when the property we are trying to replace is immutable.
                             * This usually happens when it is declared as `val` or it is a getter without a setter.
                             * If for some reason you are sure the property must be replaced please check that it is
                             * declared as `var` field or add a setter to it in the corresponding superclass of Node
                             * that defines the target property.
                             */
                            throw ImmutablePropertyException(p, v)
                        }
                    } else {
                        changes[p.name] = newValue
                    }
                }
            }
            is Collection<*> -> {
                if (inPlace) {
                    if (v is List<*>) {
                        for (i in 0 until v.size) {
                            val element = v[i]
                            if (element is Node) {
                                val newValue = operation(element)
                                if (newValue != element) {
                                    if (v is MutableList<*>) {
                                        (v as MutableList<Node>)[i] = newValue
                                    } else {
                                        throw ImmutablePropertyException(p, element)
                                    }
                                }
                            }
                        }
                    } else {
                        TODO()
                    }
                } else {
                    val newValue = v.map { if (it is Node) operation(it) else it }
                    if (newValue != v) {
                        changes[p.name] = newValue
                    }
                }
            }
        }
    }
    var instanceToTransform = this
    if (changes.isNotEmpty()) {
        val constructor = this.javaClass.kotlin.primaryConstructor!!
        val params = HashMap<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if (changes.containsKey(param.name)) {
                params[param] = changes[param.name]
            } else {
                params[param] = this.javaClass.kotlin.memberProperties.find { param.name == it.name }!!.get(this)
            }
        }
        instanceToTransform = constructor.callBy(params)
    }
    return instanceToTransform
}

fun Node.replace(other: Node): Node {
    return this.parent?.let {
        it.transformChildren(inPlace = true, operation = { node -> if (node == this) other else node })
    } ?: throw IllegalStateException("Parent not set")
}
