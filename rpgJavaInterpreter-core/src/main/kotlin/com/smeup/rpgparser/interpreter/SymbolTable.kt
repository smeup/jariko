package com.smeup.rpgparser.interpreter

import java.lang.IllegalStateException

class SymbolTable {
    private val values = LinkedHashMap<AbstractDataDefinition, Value>()

    operator fun contains(dataName: String): Boolean = dataDefinitionByName(dataName) != null
    operator fun contains(data: AbstractDataDefinition): Boolean = data in values

    operator fun get(data: AbstractDataDefinition): Value {
        if (data is FieldDefinition) {
            val containerValue = get(data.container)
            return if (data.container.isArray()) {
                ProjectedArrayValue(containerValue as ArrayValue, data)
            } else {
                // Should be always a DataStructValue
                if (containerValue is DataStructValue) {
                    return coerce(containerValue.get(data), data.type)
                } else {
                    val structValue = (containerValue as? StructValue)
                            ?: throw IllegalStateException("Container expected to be a struct value: $containerValue")
                    structValue.elements[data]!!
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
        for (e in values) {
            if (e.key is DataDefinition) {
                val field = (e.key as DataDefinition).fields.firstOrNull {
                    it.name.equals(dataName, ignoreCase = true) && it.canBeUsedUnqualified()
                }
                if (field != null) {
                    return ProjectedArrayValue(e.value as ArrayValue, field)
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
        values[data] = value.forType(data.type)
    }
}

private fun Value.forType(type: Type): Value {
    if (type is StringType && this is StringValue) {
        if (type.varying) {
            this.trimEnd()
        } else {
            if (this.value.length < type.length) this.pad(type.length)
        }
    }
    return this
}
