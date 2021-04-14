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

class SymbolTable : ISymbolTable {
    private val values = LinkedHashMap<AbstractDataDefinition, Value>()

    override operator fun contains(dataName: String): Boolean = dataDefinitionByName(dataName) != null
    override operator fun contains(data: AbstractDataDefinition): Boolean = data in values

    override var parentSymbolTable: ISymbolTable?
        get() = TODO("Not yet implemented")
        set(value) { parentSymbolTable = value }

    override operator fun get(data: AbstractDataDefinition): Value {
        if (data is FieldDefinition) {
            val containerValue = get(data.container)
            return if (data.container.isArray()) {
                TODO("We do not yet handle an array container")
            } else if (data.declaredArrayInLine != null) {
                ProjectedArrayValue.forData(containerValue as DataStructValue, data)
            } else {
                // Should be always a DataStructValue
                if (containerValue is DataStructValue) {
                    return coerce(containerValue[data], data.type)
                } else {
                    throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is $containerValue")
                }
            }
        }
        return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
    }

    override operator fun get(dataName: String): Value {
        val data = dataDefinitionByName(dataName)
        if (data != null) {
            return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
        }
        // We did not find a top-level declaration with that name,
        // looking among fields
        for (topLevelValue in values) {
            if (topLevelValue.key is DataDefinition) {
                val field = (topLevelValue.key as DataDefinition).fields.firstOrNull {
                    it.name.equals(dataName, ignoreCase = true) && it.canBeUsedUnqualified()
                }
                if (field != null) {
                    return if (topLevelValue.key.type is ArrayValue) {
                        TODO("We do not yet handle top level values of array type")
                    } else {
                        (topLevelValue.value as DataStructValue)[field]
                    }
                }
            }
        }
        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }

    override fun dataDefinitionByName(dataName: String) =
            values.keys.firstOrNull { it.name.equals(dataName, ignoreCase = true) }

    override operator fun set(data: AbstractDataDefinition, value: Value): Value? {
        require(!(data !in this && data.name in this)) {
            "This data definition would conflict with an existing data definition with the same name. This data definition: $data. Existing data definition: ${this[data.name]}"
        }
        require(data.type.canBeAssigned(value))
        return values.put(data, value.forType(data.type))
    }

    override fun getValues(): Map<AbstractDataDefinition, Value> {
        return values
    }

    /**
     * Clear symbol table
     * */
    override fun clear() = values.clear()

    /**
     * @return if is empty
     * */
    override fun isEmpty() = values.isEmpty()
}
