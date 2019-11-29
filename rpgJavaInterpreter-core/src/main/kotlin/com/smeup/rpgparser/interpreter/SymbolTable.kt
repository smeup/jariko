package com.smeup.rpgparser.interpreter

import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException

class SymbolTable {
    private val values = LinkedHashMap<AbstractDataDefinition, Value>()

    operator fun contains(dataName: String): Boolean = dataDefinitionByName(dataName) != null
    operator fun contains(data: AbstractDataDefinition): Boolean = data in values

    operator fun get(data: AbstractDataDefinition): Value {
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
                    throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is ${containerValue}")
                }
            }
        }
        return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
    }

    operator fun get(dataName: String): Value {
        val data = dataDefinitionByName(dataName)
        if (data != null) {
            return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
        }
        // We did not find a top-level declaration with that name,
        // looking among fields
        for (e in values) {
            if (e.key is DataDefinition) {
                val field = (e.key as DataDefinition).fields.firstOrNull {
                    it.name.equals(dataName, ignoreCase = true) && it.canBeUsedUnqualified()
                }
                if (field != null) {
                    return if (e.key.type is ArrayValue) {
                        TODO("We do not yet handle fields of array type")
                    } else {
                        (e.value as DataStructValue)[field]
                    }
                }
            }
        }
        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }

    fun dataDefinitionByName(dataName: String) =
            values.keys.firstOrNull { it.name.equals(dataName, ignoreCase = true) }

    operator fun set(data: AbstractDataDefinition, value: Value) {
        require(!(data !in this && data.name in this)) {
            "This data definition would conflict with an existing data definition with the same name. This data definition: $data. Existing data definition: ${this[data.name]}"
        }
        require(data.type.canBeAssigned(value))
        values[data] = value
    }
}
