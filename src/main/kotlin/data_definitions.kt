package com.smeup.rpgparser

import com.smeup.rpgparser.ast.AssignableExpression
import com.smeup.rpgparser.ast.Expression
import com.strumenta.kolasu.model.Derived
import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import java.lang.IllegalArgumentException

open class AbstractDataDefinition(override val name: String,
                                  open val size: Int?,
                                  override val position: Position? = null) : Node(position), Named

class DataDefinition(override val name: String,
                     val dataType: Type,
                     override val size: Int? = null,
                     val decimals: Int = 0,
                     val arrayLength: Expression? = null,
                     val fields: List<FieldDefinition>? = null,
                     val like: AssignableExpression? = null,
                     val initializationValue : Expression? = null,
                     override val position: Position? = null) : AbstractDataDefinition(name, size, position) {
    init {
        require((fields != null) == (dataType is DataStructureType))
        { "Fields should be sent always and only for data structures" }
    }

    override fun toString(): String {
        return "DataDefinition($name, $dataType, $size)"
    }

    fun isArray() = arrayLength != null
    fun startOffset(fieldDefinition: FieldDefinition): Int {
        var start = 0
        for (f in fields ?: emptyList()) {
            if (f == fieldDefinition) {
                require(start >= 0)
                return start
            }
            start += f.size
        }
        throw IllegalArgumentException("Unknown field $fieldDefinition")
    }
    fun endOffset(fieldDefinition: FieldDefinition): Int {
        return startOffset(fieldDefinition) + fieldDefinition.size
    }
}

data class FieldDefinition(override val name: String,
                           override val size: Int,
                           override val position: Position? = null) : AbstractDataDefinition(name, size, position) {

    init {
        require(size > 0)
    }

    @Derived
    val container
        get() = this.parent as DataDefinition
    // TODO consider overlay directive
    val startOffset: Int
        get() = container.startOffset(this)
    // TODO consider overlay directive
    val endOffset: Int
        get() = container.endOffset(this)
}

// Positions 64 through 68 specify the length of the result field. This entry is optional, but can be used to define a
// numeric or character field not defined elsewhere in the program. These definitions of the field entries are allowed
// if the result field contains a field name. Other data types must be defined on the definition specification or on the
// calculation specification using the *LIKE DEFINE operation.

class InStatementDataDefinition(override val name: String,
                                override val size: Int,
                                override val position: Position? = null) : AbstractDataDefinition(name, size, position) {
    init {
        require(size > 0)
    }
}