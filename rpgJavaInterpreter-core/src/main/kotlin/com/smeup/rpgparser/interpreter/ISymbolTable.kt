package com.smeup.rpgparser.interpreter

interface ISymbolTable {
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
     * Clear symbol table. It need to set state to not initialized
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