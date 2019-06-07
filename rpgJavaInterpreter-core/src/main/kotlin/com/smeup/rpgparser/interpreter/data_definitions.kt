package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.ast.Expression
import com.strumenta.kolasu.model.Derived
import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position

open class AbstractDataDefinition(override val name: String,
                                  open val type: Type,
                                  override val position: Position? = null) : Node(position), Named {
    fun numberOfElements() = type.numberOfElements()
    fun elementSize() = type.elementSize()
}

data class DataDefinition(override val name: String,
                          override val type: Type,
                          val fields: List<FieldDefinition> = emptyList(),
                          val initializationValue : Expression? = null,
                          override val position: Position? = null)
            : AbstractDataDefinition(name, type, position) {

    fun isArray() = type is ArrayType
    fun startOffset(fieldDefinition: FieldDefinition): Int {
        var start = 0
        for (f in fields) {
            if (f == fieldDefinition) {
                require(start >= 0)
                return start
            }
            start += f.elementSize().toInt()
        }
        throw IllegalArgumentException("Unknown field $fieldDefinition")
    }
    fun endOffset(fieldDefinition: FieldDefinition): Int {
        return (startOffset(fieldDefinition) + fieldDefinition.elementSize()).toInt()
    }
}

data class FieldDefinition(override val name: String,
                           override val type: Type,
                           val explicitStartOffset: Int? = null,
                           val explicitEndOffset: Int? = null,
                           override val position: Position? = null)
            : AbstractDataDefinition(name, type, position) {
    val size : Long = type.size

    @Derived
    val container
        get() = this.parent as DataDefinition
    // TODO consider overlay directive
    val startOffset: Int
        get() = explicitStartOffset ?: container.startOffset(this)
    // TODO consider overlay directive
    val endOffset: Int
        get() = explicitEndOffset ?: container.endOffset(this)
}

// Positions 64 through 68 specify the length of the result field. This entry is optional, but can be used to define a
// numeric or character field not defined elsewhere in the program. These definitions of the field entries are allowed
// if the result field contains a field name. Other data types must be defined on the definition specification or on the
// calculation specification using the *LIKE DEFINE operation.

class InStatementDataDefinition(override val name: String,
                                override val type: Type,
                                override val position: Position? = null,
                                val initializationValue : Expression? = null)
            : AbstractDataDefinition(name, type, position)