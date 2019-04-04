package com.smeup.rpgparser

interface Type

data class ArrayType(val element: Type) : Type

fun Expression.type() : Type {
    when (this) {
        else -> TODO(this.javaClass.canonicalName)
    }
}

fun AbstractDataDefinition.type(): Type {
    when (this) {
        else -> TODO(this.javaClass.canonicalName)
    }
}

class DataDefinitionType(val dataDefinition: AbstractDataDefinition) : Type