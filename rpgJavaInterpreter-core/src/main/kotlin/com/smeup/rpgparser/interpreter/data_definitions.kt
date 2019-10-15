package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.MuteAnnotation
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.facade.MutesMap
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.model.*
import java.math.BigDecimal
import java.util.*

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

    var internalFormatName: String? = null
        set(value) {
            field = value?.toUpperCase()
        }
}

data class DataDefinition(
        override val name: String,
        override val type: Type,
        var fields: List<FieldDefinition> = emptyList(),
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
        var explicitStartOffset: Int? = null,
        var explicitEndOffset: Int? = null,
        var nextOffset : Int = 0,
        // In case of using LIKEDS we reuse a FieldDefinition, but specifying a different
        // container. We basically duplicate it
        @property:Link
        var overriddenContainer: DataDefinition? = null,
        override val position: Position? = null
) :
            AbstractDataDefinition(name, type, position) {
    val size: Long = type.size

    fun toDataStructureValue(value: Value): StringValue {
        when(type) {
            // case numeric
            is  NumberType -> {
                // Packed or Zoned
                if (type.rpgType == RpgType.PACKED.rpgType || type.rpgType == RpgType.ZONED.rpgType || type.rpgType == "") {
                    return if (type.decimal) {
                        // Transform the numeric to an encoded string
                        val encoded = encodeToDS(value.asDecimal().value, type.entireDigits, type.decimalDigits)
                        // adjust the size to fit the target field
                        val fitted = encoded.padEnd(type.entireDigits + type.decimalDigits)
                        StringValue(fitted)

                    } else {
                        // Transform the numeric to an encoded string
                        val encoded = encodeToDS(value.asDecimal().value, type.entireDigits, 0)
                        // adjust the size to fit the target field
                        val fitted = encoded.padEnd(type.entireDigits, ' ')
                        StringValue(fitted)
                    }
                }
                if (type.rpgType == RpgType.INTEGER.rpgType || type.rpgType == RpgType.UNSIGNED.rpgType) {
                    // Transform the numeric to an encoded string
                    val encoded = encodeToDS(value.asDecimal().value, type.entireDigits, 0)
                    // adjust the size to fit the target field
                    val fitted = encoded.padEnd(type.entireDigits, ' ')
                    return StringValue(fitted)
                }
                // To date only 2 and 4 bytes are supported
                if (type.rpgType == RpgType.BINARY.rpgType) {
                    // Transform the numeric to an encoded string
                    if (type.entireDigits == 2 || type.entireDigits == 4) {
                        val encoded = encodeBinary(value.asDecimal().value, type.entireDigits)
                        // adjust the size to fit the target field
                        return StringValue(encoded)

                    }
                }
                TODO("Not implmented ${type}")
            }
            is StringType -> {
                return StringValue( value.asString().value )
            }
            else -> TODO("Not implmented ${type}")
        }

    }

    /**
     * The fields used through LIKEDS cannot be used unqualified
     */
    fun canBeUsedUnqualified() = this.overriddenContainer == null

    @Derived
    val container
        get() = overriddenContainer ?: this.parent as DataDefinition
    // TODO consider overlay directive
    val startOffset: Int
        get() = explicitStartOffset ?: container.startOffset(this)
    // TODO consider overlay directive
    val endOffset: Int
        get() = explicitEndOffset ?: container.endOffset(this)

    override fun hashCode(): Int {
        return name.hashCode() * 31 + type.hashCode() * 7
    }
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

//
// Encode a numeric value for a data structure
// In oroder to fit
// Returns a String with a len < of the number of digits declared
//
fun encodeToDS(inValue: BigDecimal, digits: Int, scale:Int) : String {
    // get just the digits from BigDecimal, "normalize" away sign, decimal place etc.
    val inChars = inValue.abs().movePointRight(scale).toBigInteger().toString().toCharArray()
    var buffer = ByteArray(inChars.size / 2 + 1)

    // read the sign
    val sign = inValue.signum()

    var offset = 0
    var inPosition = 0
    var firstNibble: Int
    var secondNibble: Int

    // place all the digits except last one
    while (inPosition < inChars.size - 1) {
        firstNibble = ((inChars[inPosition++].toInt()) and 0x000F) shl 4
        secondNibble = (inChars[inPosition++].toInt()) and 0x000F
        buffer[offset++] = (firstNibble + secondNibble).toByte()
    }

    // place last digit and sign nibble
    if (inPosition == inChars.size) {
        firstNibble = 0x00F0
    } else {
        firstNibble = (inChars[inChars.size - 1].toInt()) and 0x000F shl 4

    }
    if (sign != -1) {
        buffer[offset] = (firstNibble + 0x000F).toByte()
    } else {
        buffer[offset] = (firstNibble + 0x000D).toByte()
    }

    return Base64.getEncoder().withoutPadding().encodeToString(buffer)
}

fun decodeFromDS(value :String, digits: Int, scale:Int) : BigDecimal {
    val buffer = Base64.getDecoder().decode(value)
    var sign : String = ""
    var number : String = ""
    var nibble = ((buffer[buffer.size-1]).toInt() and 0x0F)
    if(nibble == 0x0B || nibble == 0x0D) {
        sign = "-"
    }

    var offset = 0
    while (offset < (buffer.size-1)) {
        nibble = (buffer[offset].toInt() and 0xFF).ushr(4)
        number += Character.toString((nibble or 0x30).toChar())
        nibble = buffer[offset].toInt() and 0x0F or 0x30
        number+= Character.toString((nibble or 0x30).toChar())

        offset++
    }

    // read last digit
    nibble = (buffer[offset].toInt() and 0xFF).ushr(4)
    if(nibble <= 9) {
        number+= Character.toString((nibble or 0x30).toChar())
    }
    // adjust the scale
    if(scale > 0) {
        val len = number.length
        number = number.substring(0,len -scale) + "." + number.substring(len - scale, len);
    }
    number  = sign + number
    return number.toBigDecimal()

}

fun encodeBinary(inValue: BigDecimal, digits: Int) : String {
    val buffer = ByteArray(digits)
    val lsb = inValue.toInt()
    if( digits == 2) {

        buffer[0] = ((lsb shr 8) and 0x0000FFFF).toByte()
        buffer[1] = (lsb and 0x0000FFFF).toByte()

        return buffer[1].toChar().toString() + buffer[0].toChar().toString()
    }
    if( digits == 4) {

        buffer[0] = ((lsb shr 24) and 0x0000FFFF).toByte()
        buffer[1] = ((lsb shr 16) and 0x0000FFFF).toByte()
        buffer[2] = ((lsb shr 8) and 0x0000FFFF).toByte()
        buffer[3] = (lsb and 0x0000FFFF).toByte()

        return buffer[3].toChar().toString() + buffer[2].toChar().toString() +  buffer[1].toChar().toString() +  buffer[0].toChar().toString()

    }
    TODO("encode binary for $digits not implemented")
}

fun decodeBinary(value :String, digits: Int) : BigDecimal {
    if (digits == 2) {
        var number: Long = 0x0000000
        if (value[1].toInt() and 0x1000 != 0) {
            number = 0xFFFF0000
        }
        number += (value[0].toInt() and 0x00FF) + ((value[1].toInt() and 0x00FF) shl 8)
        return BigDecimal(number.toInt().toString())

    }
    if (digits == 4) {
        val number= (value[0].toLong() and 0x00FF) +
                ((value[1].toLong() and 0x00FF) shl 8 ) +
                ((value[2].toLong() and 0x00FF) shl 16 ) +
                ((value[3].toLong() and 0x00FF) shl 24)

        return BigDecimal(number.toInt().toString())

    }
    TODO("encode binary for $digits not implemented")
}