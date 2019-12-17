package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.MuteAnnotation
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.facade.MutesMap
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.model.*
import java.math.BigDecimal

abstract class AbstractDataDefinition(
    override val name: String,
    open val type: Type,
    override val position: Position? = null,
    var muteAnnotations: MutableList<MuteAnnotation> = mutableListOf()
) : Node(position), Named {
    fun numberOfElements() = type.numberOfElements()
    open fun elementSize() = type.elementSize()

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

    open fun isArray(): Boolean {
        return type is ArrayType
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
    val inz: Boolean = false,
    override val position: Position? = null
) :
            AbstractDataDefinition(name, type, position) {

    override fun isArray() = type is ArrayType
    fun isCompileTimeArray() = type is ArrayType && type.compileTimeArray()

    @Deprecated("The start offset should be calculated before defining the FieldDefinition")
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

    @Deprecated("The end offset should be calculated before defining the FieldDefinition")
    fun endOffset(fieldDefinition: FieldDefinition): Int {
        return (startOffset(fieldDefinition) + fieldDefinition.elementSize()).toInt()
    }

    fun getFieldByName(fieldName: String): FieldDefinition {
        return this.fields.find { it.name == fieldName } ?: throw java.lang.IllegalArgumentException("Field not found $fieldName")
    }
}

fun Type.toDataStructureValue(value: Value): StringValue {
    when (this) {
        // case numeric
        is NumberType -> {
            if (this.rpgType == RpgType.ZONED.rpgType) {
                val s = encodeToZoned(value.asDecimal().value, this.entireDigits, this.decimalDigits)
                val fitted = s.padStart(this.numberOfDigits, '0')
                return StringValue(fitted)
            }
            // Packed
            if (this.rpgType == RpgType.PACKED.rpgType || this.rpgType == "") {
                return if (this.decimal) {
                    // Transform the numeric to an encoded string
                    val encoded = encodeToDS(value.asDecimal().value, this.entireDigits, this.decimalDigits)
                    // adjust the size to fit the target field
                    val fitted = encoded.padEnd(this.size.toInt())
                    StringValue(fitted)
                } else {
                    // Transform the numeric to an encoded string
                    val encoded = encodeToDS(value.asDecimal().value, this.entireDigits, 0)
                    // adjust the size to fit the target field
                    val fitted = encoded.padEnd(this.size.toInt())
                    StringValue(fitted)
                }
            }
            if (this.rpgType == RpgType.INTEGER.rpgType) {
                // Transform the integer to an encoded string
                val encoded = encodeInteger(value.asDecimal().value, this.size.toInt())
                val fitted = encoded.padEnd(this.size.toInt())
                return StringValue(fitted)
            }
            if (this.rpgType == RpgType.UNSIGNED.rpgType) {
                // Transform the unsigned to an encoded string
                val encoded = encodeUnsigned(value.asDecimal().value, this.size.toInt())
                val fitted = encoded.padEnd(this.size.toInt())
                return StringValue(fitted)
            }
            // To date only 2 and 4 bytes are supported
            if (this.rpgType == RpgType.BINARY.rpgType) {
                // Transform the numeric to an encoded string
                val len = when (this.entireDigits) {
                    in 1..4 -> 2
                    in 5..9 -> 4
                    else -> 8
                }
                val encoded = encodeBinary(value.asDecimal().value, len)
                // adjust the size to fit the target field
                // val fitted = encoded.padEnd(this.size.toInt())
                return StringValue(encoded)
            }
            TODO("Not implemented $this")
        }
        is StringType -> {
            return StringValue(value.asString().value)
        }
        is ArrayType -> {
            val sb = StringBuilder()
            (value as ArrayValue).elements().forEach {
                sb.append(this.element.toDataStructureValue(it).value)
            }
            return StringValue(sb.toString())
        }
        is CharacterType -> {
            val sb = StringBuilder()
            (value as StringValue).value.forEach {
                sb.append(it)
            }
            return StringValue(sb.toString())
        }
        is BooleanType ->  {
            if((value as BooleanValue).value)
                return StringValue("1")
            return  StringValue("0")
        }
        else -> TODO("Conversion to data struct value not implemented for $this")
    }
}

data class FieldDefinition(
    override val name: String,
    override val type: Type,
    val explicitStartOffset: Int? = null,
    val explicitEndOffset: Int? = null,
    val calculatedStartOffset: Int? = null,
    val calculatedEndOffset: Int? = null,
        // In case of using LIKEDS we reuse a FieldDefinition, but specifying a different
        // container. We basically duplicate it
    @property:Link
    var overriddenContainer: DataDefinition? = null,
    val initializationValue: Expression? = null,
    override val position: Position? = null,

    // true when the FieldDefinition contains a DIM keyword on its line
    val declaredArrayInLineOnThisField: Int? = null
) :
            AbstractDataDefinition(name, type, position) {

    init {
        require((explicitStartOffset != null) != (calculatedStartOffset != null)) {
            "Field $name should have either an explicit start offset ($explicitStartOffset) or a calculated one ($calculatedStartOffset)"
        }
        require((explicitEndOffset != null) != (calculatedEndOffset != null)) {
            "Field $name should have either an explicit end offset ($explicitEndOffset) or a calculated one ($calculatedEndOffset)"
        }
    }

    // true when the FieldDefinition contains a DIM keyword on its line
    // or when the field is overlaying on an a field which has the DIM keyword
    val declaredArrayInLine: Int?
        get() = declaredArrayInLineOnThisField ?: (overlayingOn as? FieldDefinition)?.declaredArrayInLine

    val size: Long = type.size

    @property:Link
    var overlayingOn: AbstractDataDefinition? = null

    // when they are arrays, how many bytes should we skip into the DS to find the next element?
    // normally it would be the same size as an element of the DS, however if they are declared
    // as on overlay of a field with a DIM keyword, then we should use the size of an element
    // of such field
    val stepSize: Long
        get() {
            return if (declaredArrayInLineOnThisField != null) {
                elementSize()
            } else {
                (overlayingOn as? FieldDefinition)?.stepSize ?: elementSize()
            }
        }

    override fun elementSize(): Long {
        return if (container.type is ArrayType) {
            super.elementSize()
        } else if (this.declaredArrayInLine != null) {
            super.elementSize()
        } else {
            size
        }
    }

    fun toDataStructureValue(value: Value) = type.toDataStructureValue(value)

    /**
     * The fields used through LIKEDS cannot be used unqualified
     */
    fun canBeUsedUnqualified() = this.overriddenContainer == null

    @Derived
    val container
        get() = overriddenContainer
                ?: this.parent as? DataDefinition
                ?: throw IllegalStateException("Parent of field ${this.name} was expected to be a DataDefinition, instead it is ${this.parent} (${this.parent?.javaClass})")

    /**
     * The start offset is zero based, while in RPG code you could find explicit one-based offsets.
     *
     * For example:
     * 0002.00 DCURTIMSTP        DS
     * 0003.00 DCURTIMDATE               1      8S 0
     *
     * In this case CURTIMDATE will have startOffset 0.
     */
    val startOffset: Int
        get() {
            if (explicitStartOffset != null) {
                return explicitStartOffset
            }
            if (calculatedStartOffset != null) {
                return calculatedStartOffset
            }
            return container.startOffset(this)
        }

    /**
     * The end offset is non-inclusive if considered zero based, or inclusive if considered one based.
     *
     * For example:
     * 0002.00 DCURTIMSTP        DS
     * 0003.00 DCURTIMDATE               1      8S 0
     *
     * In this case CURTIMDATE will have endOffset 8.
     *
     * In the case of an array endOffset indicates the end of the first element.
     */
    val endOffset: Int
        get() {
            if (explicitEndOffset != null) {
                return explicitEndOffset
            }
            if (calculatedEndOffset != null) {
                return calculatedEndOffset
            }
            return (container as DataDefinition).endOffset(this)
        }

    val offsets: Pair<Int, Int>
        get() = Pair(startOffset, endOffset)

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
) : AbstractDataDefinition(name, type, position) {
    override fun toString(): String {
        return "InStatementDataDefinition name=$name, type=$type, position=$position"
    }
}

/**
 * Encoding/Decoding a binary value for a data structure
 */

fun encodeBinary(inValue: BigDecimal, size: Int): String {
    val buffer = ByteArray(size)
    val lsb = inValue.toInt()

    if (size == 1) {

        buffer[0] = (lsb and 0x0000FFFF).toByte()

        return buffer[0].toChar().toString()
    }

    if (size == 2) {

        buffer[0] = ((lsb shr 8) and 0x000000FF).toByte()
        buffer[1] = (lsb and 0x000000FF).toByte()

        return buffer[1].toChar().toString() + buffer[0].toChar().toString()
    }
    if (size == 4) {

        buffer[0] = ((lsb shr 24) and 0x0000FFFF).toByte()
        buffer[1] = ((lsb shr 16) and 0x0000FFFF).toByte()
        buffer[2] = ((lsb shr 8) and 0x0000FFFF).toByte()
        buffer[3] = (lsb and 0x0000FFFF).toByte()

        return buffer[3].toChar().toString() + buffer[2].toChar().toString() + buffer[1].toChar().toString() + buffer[0].toChar().toString()
    }
    if (size == 8) {
        val llsb = inValue.toLong()
        buffer[0] = ((llsb shr 56) and 0x0000FFFF).toByte()
        buffer[1] = ((llsb shr 48) and 0x0000FFFF).toByte()
        buffer[2] = ((llsb shr 40) and 0x0000FFFF).toByte()
        buffer[3] = ((llsb shr 32) and 0x0000FFFF).toByte()
        buffer[4] = ((llsb shr 24) and 0x0000FFFF).toByte()
        buffer[5] = ((llsb shr 16) and 0x0000FFFF).toByte()
        buffer[6] = ((llsb shr 8) and 0x0000FFFF).toByte()
        buffer[7] = (llsb and 0x0000FFFF).toByte()

        return buffer[7].toChar().toString() + buffer[6].toChar().toString() + buffer[5].toChar().toString() + buffer[4].toChar().toString() +
        buffer[3].toChar().toString() + buffer[2].toChar().toString() + buffer[1].toChar().toString() + buffer[0].toChar().toString()
    }
    TODO("encode binary for $size not implemented")
}

fun encodeInteger(inValue: BigDecimal, size: Int): String {
    return encodeBinary(inValue, size)
}

fun encodeUnsigned(inValue: BigDecimal, size: Int): String {
    return encodeBinary(inValue, size)
}

fun decodeBinary(value: String, size: Int): BigDecimal {

    if (size == 1) {
        var number: Long = 0x0000000
        if (value[0].toInt() and 0x0010 != 0) {
            number = 0x00000000
        }
        number += (value[0].toInt() and 0x00FF)
        return BigDecimal(number.toInt().toString())
    }

    if (size == 2) {
        var number: Long = 0x0000000
        if (value[1].toInt() and 0x8000 != 0) {
            number = 0xFFFF0000
        }
        number += (value[0].toInt() and 0x00FF) + ((value[1].toInt() and 0x00FF) shl 8)
        return BigDecimal(number.toInt().toString())
    }

    if (size == 4) {
        val number = (value[0].toLong() and 0x00FF) +
                ((value[1].toLong() and 0x00FF) shl 8) +
                ((value[2].toLong() and 0x00FF) shl 16) +
                ((value[3].toLong() and 0x00FF) shl 24)

        return BigDecimal(number.toInt().toString())
    }
    if (size == 8) {
        val number = (value[0].toLong() and 0x00FF) +
                ((value[1].toLong() and 0x00FF) shl 8) +
                ((value[2].toLong() and 0x00FF) shl 16) +
                ((value[3].toLong() and 0x00FF) shl 24) +
                ((value[4].toLong() and 0x00FF) shl 32) +
                ((value[5].toLong() and 0x00FF) shl 40) +
                ((value[6].toLong() and 0x00FF) shl 48) +
                ((value[7].toLong() and 0x00FF) shl 56)

        return BigDecimal(number.toInt().toString())
    }
    TODO("decode binary for $size not implemented")
}

fun decodeInteger(value: String, size: Int): BigDecimal {
    if (size == 1) {
        var number: Int = 0x0000000
        number += (value[0].toByte())
        return BigDecimal(number.toString())
    }

    if (size == 2) {
        var number: Long = 0x0000000
        if (value[1].toInt() and 0x8000 != 0) {
            number = 0xFFFF0000
        }
        number += (value[0].toInt() and 0x00FF) + ((value[1].toInt() and 0x00FF) shl 8)
        return BigDecimal(number.toInt().toString())
    }
    if (size == 4) {
        val number = (value[0].toLong() and 0x00FF) +
                ((value[1].toLong() and 0x00FF) shl 8) +
                ((value[2].toLong() and 0x00FF) shl 16) +
                ((value[3].toLong() and 0x00FF) shl 24)

        return BigDecimal(number.toInt().toString())
    }
    if (size == 8) {
        val number = (value[0].toLong() and 0x00FF) +
                ((value[1].toLong() and 0x00FF) shl 8) +
                ((value[2].toLong() and 0x00FF) shl 16) +
                ((value[3].toLong() and 0x00FF) shl 24) +
                ((value[4].toLong() and 0x00FF) shl 32) +
                ((value[5].toLong() and 0x00FF) shl 40) +
                ((value[6].toLong() and 0x00FF) shl 48) +
                ((value[7].toLong() and 0x00FF) shl 56)

        return BigDecimal(number.toString())
    }
    TODO("decode binary for $size not implemented")
}

fun decodeUnsigned(value: String, size: Int): BigDecimal {

    if (size == 1) {
        var number: Long = 0x0000000
        if (value[0].toInt() and 0x0010 != 0) {
            number = 0x00000000
        }
        number += (value[0].toInt() and 0x00FF)
        return BigDecimal(number.toInt().toString())
    }

    if (size == 2) {
        var number: Long = 0x0000000
        if (value[1].toInt() and 0x1000 != 0) {
            number = 0xFFFF0000
        }
        number += (value[0].toInt() and 0x00FF) + ((value[1].toInt() and 0x00FF) shl 8)
        // make sure you count onlu 16 bits
        number = number and 0x0000FFFF
        return BigDecimal(number.toString())
    }
    if (size == 4) {
        val number = (value[0].toLong() and 0x00FF) +
                ((value[1].toLong() and 0x00FF) shl 8) +
                ((value[2].toLong() and 0x00FF) shl 16) +
                ((value[3].toLong() and 0x00FF) shl 24)

        return BigDecimal(number.toString())
    }
    if (size == 8) {
        val number = (value[0].toLong() and 0x00FF) +
                ((value[1].toLong() and 0x00FF) shl 8) +
                ((value[2].toLong() and 0x00FF) shl 16) +
                ((value[3].toLong() and 0x00FF) shl 24) +
                ((value[4].toLong() and 0x00FF) shl 32) +
                ((value[5].toLong() and 0x00FF) shl 40) +
                ((value[6].toLong() and 0x00FF) shl 48) +
                ((value[7].toLong() and 0x00FF) shl 56)

        return BigDecimal(number.toInt().toString())
    }
    TODO("decode binary for $size not implemented")
}

/**
 * Encode a zoned value for a data structure
 */
fun encodeToZoned(inValue: BigDecimal, digits: Int, scale: Int): String {
    // get just the digits from BigDecimal, "normalize" away sign, decimal place etc.
    val inChars = inValue.abs().movePointRight(scale).toBigInteger().toString().toCharArray()
    var buffer = IntArray(inChars.size)

    // read the sign
    val sign = inValue.signum()

    inChars.forEachIndexed { index, char ->
        val digit = char.toInt()
        buffer[index] = digit
    }
    if (sign < 0) {
        buffer[inChars.size - 1] = (buffer[inChars.size - 1] - 0x030) + 0x0049
    }

    var s = ""
    buffer.forEach { byte ->
        s += byte.toChar()
    }

    s = s.padStart(digits, '0')
    return s
}

fun decodeFromZoned(value: String, digits: Int, scale: Int): BigDecimal {
    val builder = StringBuilder()

    value.forEach {
        when {
            it.isDigit() -> builder.append(it)
            else -> {
                if (it.toInt() == 0) {
                    builder.append('0')
                } else {
                    builder.insert(0, '-')
                    builder.append((it.toInt() - 0x0049 + 0x0030).toChar())
                }
            }
        }
    }
    if (scale != 0) {
        builder.insert(builder.length - scale, ".")
    }
    return BigDecimal(builder.toString())
}

/**
 * Encoding/Decoding a numeric value for a data structure
 */
fun encodeToDS(inValue: BigDecimal, digits: Int, scale: Int): String {
    // get just the digits from BigDecimal, "normalize" away sign, decimal place etc.
    val inChars = inValue.abs().movePointRight(scale).toBigInteger().toString().toCharArray()
    var buffer = IntArray(inChars.size / 2 + 1)

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
        buffer[offset++] = (firstNibble + secondNibble).toInt()
    }

    // place last digit and sign nibble
    firstNibble = if (inPosition == inChars.size) {
        0x00F0
    } else {
        (inChars[inChars.size - 1].toInt()) and 0x000F shl 4
    }
    if (sign != -1) {
        buffer[offset] = (firstNibble + 0x000F).toInt()
    } else {
        buffer[offset] = (firstNibble + 0x000D).toInt()
    }

    var s = ""
    buffer.forEach { byte ->
        s += byte.toChar()
    }

    return s
}

fun decodeFromDS(value: String, digits: Int, scale: Int): BigDecimal {
    val buffer = IntArray(value.length)
    for (i in value.indices) {
        buffer[i] = value[i].toInt()
    }

    var sign: String = ""
    var number: String = ""

    var nibble = ((buffer[buffer.size - 1]).toInt() and 0x0F)
    if (nibble == 0x0B || nibble == 0x0D) {
        sign = "-"
    }

    var offset = 0
    while (offset < (buffer.size - 1)) {
        nibble = (buffer[offset].toInt() and 0xFF).ushr(4)
        number += Character.toString((nibble or 0x30).toChar())
        nibble = buffer[offset].toInt() and 0x0F or 0x30
        number += Character.toString((nibble or 0x30).toChar())

        offset++
    }

    // read last digit
    nibble = (buffer[offset].toInt() and 0xFF).ushr(4)
    if (nibble <= 9) {
        number += Character.toString((nibble or 0x30).toChar())
    }
    // adjust the scale
    if (scale > 0 && number != "0") {
        val len = number.length
        number = number.substring(0, len - scale) + "." + number.substring(len - scale, len)
    }
    number = sign + number
    return try {
        value.toBigDecimal()
    } catch (e: Exception) {
        number.toBigDecimal()
    }
}
