package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.MuteAnnotation
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.facade.MutesMap
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.model.*

open class AbstractDataDefinition(
    override val name: String,
    open val type: Type,
    override val position: Position? = null,
    var muteAnnotations: MutableList<MuteAnnotation> = mutableListOf()
) : Node(position), Named {
    fun numberOfElements() = type.numberOfElements()
    fun elementSize() = type.elementSize()

    fun accept(mutes: MutesMap, start: Int, end: Int):
            MutableList<MuteAnnotationResolved> {
        // List of mutes successfully attached to the  definition
        val mutesAttached = mutableListOf<MuteAnnotationResolved>()
        // Extracts the annotation declared before the statement
        // Note the second expression evaluate an annotation in the
        // very last line
        val muteToProcess = mutes.filterKeys {
            // TODO CodeReview: we could perhaps refactor this as
            // fun MutesMap.relevantForPosition(position: Position, end: Int) = this.filterKeys {
            //    it < position.start.line || position.start.line == (end - 1)
            // }
            // however the code is not super clear to me: why is not using >= end - 1?
            // why is using end but not start?
            it < this.position!!.start.line || this.position!!.start.line == (end - 1)
        }

        muteToProcess.forEach { (line, mute) ->
            this.muteAnnotations.add(mute.toAst(
                    // TODO CodeReview: we could add unit tests to verify we set the correct position for mutes
                    position = pos(line, this.position!!.start.column, line, this.position!!.end.column))
            )
            mutesAttached.add(MuteAnnotationResolved(line, this.position!!.start.line))
        }

        return mutesAttached
    }
}

data class FileDefinition private constructor(override val name: String, override val position: Position?) : Node(position), Named {
    companion object {
        operator fun invoke(name: String, position: Position? = null): FileDefinition {
            return FileDefinition(name.toUpperCase(), position)
        }
    }

    var formatName: String? = null
        set(value) {
            field = value?.toUpperCase()
        }
}

data class DataDefinition(
    override val name: String,
    override val type: Type,
    val fields: List<FieldDefinition> = emptyList(),
    val initializationValue: Expression? = null,
    override val position: Position? = null
) :
            AbstractDataDefinition(name, type, position) {

    fun isArray() = type is ArrayType
    fun isCompileTimeArray() = type is ArrayType && type.compileTimeArray()
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

data class FieldDefinition(
    override val name: String,
    override val type: Type,
    val explicitStartOffset: Int? = null,
    val explicitEndOffset: Int? = null,
    override val position: Position? = null
) :
            AbstractDataDefinition(name, type, position) {
    val size: Long = type.size

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

class InStatementDataDefinition(
    override val name: String,
    override val type: Type,
    override val position: Position? = null,
    val initializationValue: Expression? = null
) : AbstractDataDefinition(name, type, position)