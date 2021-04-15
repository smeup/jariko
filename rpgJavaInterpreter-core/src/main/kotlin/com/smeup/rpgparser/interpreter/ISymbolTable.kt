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

interface ISymbolTable {

    var parentSymbolTable: ISymbolTable?

    /**
     * Get the program symbol table, which is the one without parentSymbolTable
     * */
    val programSymbolTable: ISymbolTable
        get() {
            return if (parentSymbolTable == null) {
                this
            } else {
                var currentParent = parentSymbolTable
                var rootSymbolTable = this
                while (currentParent != null) {
                    rootSymbolTable = currentParent
                    currentParent = currentParent.parentSymbolTable
                }
                rootSymbolTable
            }
        }

    /**
     * @return true if SymbolTable contains a variable named dataName
     * */
    operator fun contains(dataName: String): Boolean

    /**
     * @return true if SymbolTable contains the datadefinition
     * */
    operator fun contains(data: AbstractDataDefinition): Boolean

    /**
     * @return value associated to data
     * */
    operator fun get(data: AbstractDataDefinition): Value

    /**
     * @return value associated to variable named dataName
     * */
    operator fun get(dataName: String): Value

    /**
     * @return if exists a datadefinition by variable name
     * */
    fun dataDefinitionByName(dataName: String): AbstractDataDefinition?

    /**
     * Set a value for data.
     * @return old value if presents
     * */
    operator fun set(data: AbstractDataDefinition, value: Value): Value?

    /**
     * @return All symbol table values
     * */
    fun getValues(): Map<AbstractDataDefinition, Value>

    /**
     * Clear symbol table.
     * */
    fun clear()

    /**
     * @return if is empty
     * */
    fun isEmpty(): Boolean
}

fun Value.forType(type: Type): Value {
    if (type is StringType && this is StringValue) {
        if (type.varying) {
            // this.trimEnd()
        } else {
            if (this.value.length < type.length) this.pad(type.length)
        }
    }
    return this
}