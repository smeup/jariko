package com.smeup.rpgparser.interpreter

class SymbolTable {
    private val values = HashMap<AbstractDataDefinition, Value>()

    fun contains(dataName: String) : Boolean = dataDefinitionByName(dataName) != null

    operator fun get(data: AbstractDataDefinition) : Value {
        if (data is FieldDefinition) {
            val containerValue = get(data.container)
            return if (data.container.isArray()) {
                ProjectedArrayValue(containerValue as ArrayValue, data)
            } else {
                (containerValue as StructValue).elements[data]!!
            }
        }
        return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
    }

    operator fun get(dataName: String) : Value {
        val data = dataDefinitionByName(dataName)
        if (data != null) {
            return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
        }
        for (e in values) {
            if (e.key is DataDefinition) {
                val field = (e.key as DataDefinition).fields.firstOrNull { it.name.equals(dataName, ignoreCase = true) }
                if (field != null) {
                    return ProjectedArrayValue(e.value as ArrayValue, field)
                }
            }
        }
        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }

    private fun dataDefinitionByName(dataName: String) =
            values.keys.firstOrNull { it.name.equals(dataName, ignoreCase = true) }

    operator fun set(data: AbstractDataDefinition, value: Value) {
        values[data] = value
    }

}
