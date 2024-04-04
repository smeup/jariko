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
    private val names = mutableMapOf<String, AbstractDataDefinition>()

    override operator fun contains(dataName: String): Boolean = dataDefinitionByName(dataName) != null
    override operator fun contains(data: AbstractDataDefinition): Boolean = data in values

    override var parentSymbolTable: ISymbolTable? = null

    override operator fun get(data: AbstractDataDefinition): Value {
        return when (data.scope.visibility) {
            Visibility.Program -> (programSymbolTable as SymbolTable).getLocal(data)
            Visibility.Local -> getLocal(data)
            Visibility.Static -> (getStaticSymbolTable(data.scope.reference!!) as SymbolTable).getLocal(data)
        }
    }

    private fun getLocal(data: AbstractDataDefinition): Value {
        if (data !is FieldDefinition)
            return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")

        val containerValue = get(data.container)
        if (data.container.isArray()) {
            TODO("We do not yet handle an array container")
        }
        if (data.declaredArrayInLine != null) {
            return ProjectedArrayValue.forData(containerValue as DataStructValue, data)
        }

        // Should be always a DataStructValue
        return when (containerValue) {
            is DataStructValue -> coerce(containerValue[data], data.type)
            is OccurableDataStructValue -> coerce(containerValue.value()[data], data.type)
            else -> throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is $containerValue")
        }
    }

    override operator fun get(dataName: String): Value {
        val data = dataDefinitionByName(dataName)
        if (data != null) {
            return get(data)
        }
        // We did not find a top-level declaration with that name,
        // looking among fields
        val dataDefinitions = values
            .entries
            .filter { it.key is DataDefinition }

        for (topLevelValue in dataDefinitions) {
            val currentDefinition = topLevelValue.key as DataDefinition
            val field = currentDefinition.fields.firstOrNull {
                it.name.equals(dataName, ignoreCase = true) && it.canBeUsedUnqualified()
            }

            // If field not found then skip this iteration
            if (field == null) continue

            if (currentDefinition.type is ArrayType) {
                TODO("We do not yet handle top level values of array type")
            }

            // Field found and supported, return value
            val struct = (topLevelValue.value as DataStructValue)
            return struct[field]
        }

        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }

    override fun dataDefinitionByName(dataName: String): AbstractDataDefinition? {
        val normalizedDataName = dataName.uppercase()
        return names[normalizedDataName]
            ?: parentSymbolTable?.let { (parentSymbolTable as SymbolTable).names[normalizedDataName] }
    }

    override operator fun set(data: AbstractDataDefinition, value: Value): Value? {
        return when (data.scope.visibility) {
            Visibility.Program -> (programSymbolTable as SymbolTable).setLocal(data, value)
            Visibility.Local -> setLocal(data, value)
            Visibility.Static -> (getStaticSymbolTable(data.scope.reference!!) as SymbolTable).setLocal(data, value)
        }
    }

    private fun setLocal(data: AbstractDataDefinition, value: Value): Value? {
        // replaced "data.name in this" with "data.name in names" because I must search for local name
        // whereas "data.name in this" search for name also in parent symbol table
        // remember that this function set a local scope variable
        require(!(data !in this && data.name in names)) {
            "This data definition would conflict with an existing data definition with the same name. This data definition: $data. Existing data definition: ${this[data.name]}"
        }
        require(data.type.canBeAssigned(value)) {
            "Value $value cannot be assigned to data: $data"
        }

        if (data !is FieldDefinition) {
            names[data.normalizedName] = data
            return values.put(data, value.forType(data.type))
        }

        if (data.container.isArray()) {
            throw IllegalStateException("We do not yet handle an array container")
        }

        val containerValue = get(data.container)
        if (data.declaredArrayInLine != null) {
            ProjectedArrayValue.forData(containerValue as DataStructValue, data).container.set(
                data,
                value.forType(data.type)
            )
            return ProjectedArrayValue.forData(containerValue, data)
        }

        // Should be always a DataStructValue
        return when (containerValue) {
            is DataStructValue -> {
                containerValue.set(data, value.forType(data.type))
                coerce(containerValue[data], data.type)
            }

            is OccurableDataStructValue -> {
                containerValue.value().set(data, value.forType(data.type))
                coerce(containerValue.value()[data], data.type)
            }

            else -> throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is $containerValue")
        }
    }

    override fun getValues(): Map<AbstractDataDefinition, Value> {
        return values
    }

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
