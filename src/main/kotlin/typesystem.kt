package com.smeup.rpgparser

interface Type

data class ArrayType(val element: Type) : Type
data class RawType(val size: Int?) : Type

fun Expression.type() : Type {
    return when (this) {
        is DataRefExpr -> this.variable.referred!!.type()
        else -> TODO(this.javaClass.canonicalName)
    }
}

fun AbstractDataDefinition.type(): Type {
    when (this) {
        is FieldDefinition -> {
            // TODO consider data type
            val baseType = RawType(this.size)
            return if (container.isArray()) {
                ArrayType(baseType)
            } else {
                baseType
            }
        }
        is DataDefinition -> {
            return RawType(this.size)
        }
        else -> TODO(this.javaClass.canonicalName)
    }
}

class DataDefinitionType(val dataDefinition: AbstractDataDefinition) : Type