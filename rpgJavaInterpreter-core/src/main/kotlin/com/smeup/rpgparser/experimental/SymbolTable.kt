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

package com.smeup.rpgparser.experimental

import com.smeup.rpgparser.fastmaps.IntArrayMap
import com.smeup.rpgparser.interpreter.*

/**
 * Experimental SymbolTable.
 * Data are stored IntArrayMap, which is a more efficient map than standard ones
 * @see IntArrayMap
 * */
@Deprecated("This class is experimental and will be removed in future versions. Use com.smeup.rpgparser.interpreter.SymbolTable instead.")
class SymbolTable : ISymbolTable {
    private val values = IntArrayMap<Pair<AbstractDataDefinition, Value>>(0, 32000)
    private val names = mutableMapOf<String, AbstractDataDefinition>()

    override operator fun contains(dataName: String): Boolean = dataDefinitionByName(dataName) != null
    override operator fun contains(data: AbstractDataDefinition): Boolean = data.key in values.keys

    override var parentSymbolTable: ISymbolTable? = null

    override operator fun get(data: AbstractDataDefinition): Value {
        return when (data.scope.visibility) {
            Visibility.Program -> (programSymbolTable as SymbolTable).getLocal(data)
            Visibility.Local -> getLocal(data)
            Visibility.Static -> TODO()
        }
    }

    private fun getLocal(data: AbstractDataDefinition): Value {
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
        val pair = values[data.key]
        require(pair != null) { "Cannot find searched value for $data" }
        require(pair.first.name == data.name) { "I have retrieved ${pair.first} instead of ${data.name}" }
        return pair.second
    }

    override operator fun get(dataName: String): Value {
        val data = dataDefinitionByName(dataName)
        if (data != null) {
            return get(data)
        }
        // We did not find a top-level declaration with that name,
        // looking among fields
        for (topLevelValue in values.values) {
            if (topLevelValue.first is DataDefinition) {
                val field = (topLevelValue.first as DataDefinition).fields.firstOrNull {
                    it.name.equals(dataName, ignoreCase = true) && it.canBeUsedUnqualified()
                }
                if (field != null) {
                    return if (topLevelValue.first.type is ArrayValue) {
                        TODO("We do not yet handle top level values of array type")
                    } else {
                        (topLevelValue.second as DataStructValue)[field]
                    }
                }
            }
        }
        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }

    override fun dataDefinitionByName(dataName: String): AbstractDataDefinition? {
        return names[dataName.toUpperCase()] ?: parentSymbolTable?.let { (parentSymbolTable as SymbolTable).names[dataName.toUpperCase()] }
    }

    override operator fun set(data: AbstractDataDefinition, value: Value): Value? {
        return when (data.scope.visibility) {
            Visibility.Program -> (programSymbolTable as SymbolTable).setLocal(data, value)
            Visibility.Local -> setLocal(data, value)
            Visibility.Static -> TODO()
        }
    }

    private fun setLocal(data: AbstractDataDefinition, value: Value): Value? {
        require(data.type.canBeAssigned(value)) {
            "Value $value cannot be assigned to data: $data"
        }
        names[data.name.toUpperCase()] = data
        return values.put(data.key, Pair(data, value.forType(data.type)))?.second
    }

    override fun getValues(): Map<AbstractDataDefinition, Value> = values.values.toMap()

    /**
     * Clear symbol table
     * */
    override fun clear() {
        values.clear()
        names.clear()
    }

    /**
     * @return if is empty
     * */
    override fun isEmpty() = values.isEmpty()
}