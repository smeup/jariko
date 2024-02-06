/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.MuteAnnotation
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.facade.MutesMap
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.require
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.strumenta.kolasu.model.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.math.BigDecimal

@Serializable
abstract class AbstractDataDefinition(
    @Transient override val name: String = "",
    @Transient open val type: Type = StringType(1, false),
    @Transient override val position: Position? = null,
    var muteAnnotations: MutableList<MuteAnnotation> = mutableListOf(),
    // annotated with "@Transient" because in some circumstances the compiled version behaviour
    // is wrong (fails Symbol Table value lookup by key)
    // it is not clear the reason, to reproduce follow these steps:
    // - remove @Transient
    // - recompile ./gradlew compileAllMutes
    // - launch unit test testMUTE16_01
    @Transient open val key: Int = MainExecutionContext.newId(),
    /**
     * true means this is a constant, default false
     * */
    @Transient open val const: Boolean = false,
    /**
     * This scope. Default: got by current parsing entity
     * */
    val scope: Scope = run {
        val parsingProgramStack = MainExecutionContext.getParsingProgramStack()
        val parsingProgram = if (!parsingProgramStack.isEmpty()) {
            parsingProgramStack.peek()
        } else null
        val parsingFunction = parsingProgram?.let {
            if (!it.parsingFunctionNameStack.isEmpty()) {
                it.parsingFunctionNameStack.peek()
            } else null
        }
        if (parsingFunction != null) {
            Scope.Local
        } else Scope.Program
    }
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

    fun canBeAssigned(value: Value): Boolean {
        return type.canBeAssigned(value)
    }
}

enum class ParamOption(val keyword: String) {
    NoPass("*NOPASS");

    companion object {
        fun getByKeyword(keyword: String): ParamOption {
            return ParamOption.values().first {
                it.keyword == keyword
            }
        }
    }
}

/**
 * PREFIX node
 * */
@Serializable
data class Prefix(internal val prefix: String, private val numCharsReplaced: Int?) {

    /**
     * Apply replacement rules and returns value converted
     * @param value Value to convert
     * */
    fun applyReplacementRules(value: String): String {
        return when (numCharsReplaced) {
            null -> "$prefix$value"
            else -> "$prefix${value.substring(numCharsReplaced)}"
        }
    }
}

@Serializable
data class FileDefinition private constructor(
    override val name: String,
    override val position: Position?,
    val prefix: Prefix?,
    val justExtName: Boolean
) : Node(position), Named {
    companion object {
        operator fun invoke(name: String, position: Position? = null, prefix: Prefix? = null, justExtName: Boolean = false): FileDefinition {
            return FileDefinition(name.toUpperCase(), position, prefix, justExtName)
        }
    }

    var internalFormatName: String? = null
        set(value) {
            field = value?.toUpperCase()
        }

    private var fieldNameToDataDefinitionName = mutableMapOf<String, String>()
    private var dataDefinitionNameToFieldName = mutableMapOf<String, String>()

    fun createDbFieldDataDefinitionRelation(dbFieldName: String, dataDefinitionName: String) {
        fieldNameToDataDefinitionName[dbFieldName] = dataDefinitionName
        dataDefinitionNameToFieldName[dataDefinitionName] = dbFieldName
    }

    fun getDataDefinitionName(dbFieldName: String) = fieldNameToDataDefinitionName[dbFieldName]

    fun getDbFieldName(dataDefinitionName: String) = dataDefinitionNameToFieldName[dataDefinitionName]
}

@Serializable
data class DataDefinition(
    override val name: String,
    @SerialName(value = "dataDefType") override val type: Type,
    var fields: List<FieldDefinition> = emptyList(),
    val initializationValue: Expression? = null,
    val inz: Boolean = false,
    override val position: Position? = null,
    override var const: Boolean = false,
    var paramPassedBy: ParamPassedBy = ParamPassedBy.Reference,
    var paramOptions: List<ParamOption> = mutableListOf(),
    @Transient var defaultValue: Value? = null
) :
    AbstractDataDefinition(
        name = name,
        type = type,
        position = position,
        const = const) {

    override fun isArray() = type is ArrayType
    fun isCompileTimeArray() = type is ArrayType && type.compileTimeArray()

    init {
        this.require(name.trim().isNotEmpty(), { "name cannot be empty" })
    }

    @Deprecated("The start offset should be calculated before defining the FieldDefinition")
    fun startOffset(fieldDefinition: FieldDefinition): Int {
        var start = 0
        for (f in fields) {
            if (f == fieldDefinition) {
                require(start >= 0)
                return start
            }
            start += f.elementSize()
        }
        throw IllegalArgumentException("Unknown field $fieldDefinition")
    }

    @Deprecated("The end offset should be calculated before defining the FieldDefinition")
    fun endOffset(fieldDefinition: FieldDefinition): Int {
        return (startOffset(fieldDefinition) + fieldDefinition.elementSize())
    }

    fun getFieldByName(fieldName: String): FieldDefinition {
        return this.fields.find { it.name == fieldName } ?: throw java.lang.IllegalArgumentException("Field not found $fieldName")
    }

    // I had to reimplement this method because of this error:
    //    java.lang.StackOverflowError
    //       at com.smeup.rpgparser.interpreter.DataStructureType.hashCode(typesystem.kt)
    //       at com.smeup.rpgparser.interpreter.DataDefinition.hashCode(data_definitions.kt)
    //       at com.smeup.rpgparser.interpreter.FieldDefinition.hashCode(data_definitions.kt)
    //       on parseMUTE12_01_runtime, parseMUTE12_02_runtime, parseSTRUCT_05 and parseSTRUCT_07 arose
    // when enabled the standard symbol table
    // TODO("Require investigation")
    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = other?.let { this.name == (other as AbstractDataDefinition).name } ?: false
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
                    val fitted = encoded.padEnd(this.size)
                    StringValue(fitted)
                } else {
                    // Transform the numeric to an encoded string
                    val encoded = encodeToDS(value.asDecimal().value, this.entireDigits, 0)
                    // adjust the size to fit the target field
                    val fitted = encoded.padEnd(this.size)
                    StringValue(fitted)
                }
            }
            if (this.rpgType == RpgType.INTEGER.rpgType) {
                // Transform the integer to an encoded string
                val encoded = encodeInteger(value.asDecimal().value, this.size)
                val fitted = encoded.padEnd(this.size)
                return StringValue(fitted)
            }
            if (this.rpgType == RpgType.UNSIGNED.rpgType) {
                // Transform the unsigned to an encoded string
                val encoded = encodeUnsigned(value.asDecimal().value, this.size)
                val fitted = encoded.padEnd(this.size)
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
                val fitted = encoded.padEnd(this.size)
                return StringValue(fitted)
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
        is BooleanType -> {
            if ((value as BooleanValue).value)
                return StringValue("1")
            return StringValue("0")
        }
        else -> TODO("Conversion to data struct value not implemented for $this")
    }
}

@Serializable
data class FieldDefinition(
    override val name: String,
    @SerialName("fieldDefType") override val type: Type,
    val explicitStartOffset: Int? = null,
    val explicitEndOffset: Int? = null,
    val calculatedStartOffset: Int? = null,
    val calculatedEndOffset: Int? = null,
    // In case of using LIKEDS we reuse a FieldDefinition, but specifying a different
    // container. We basically duplicate it
    @property:Link
    @Transient var overriddenContainer: DataDefinition? = null,
    val initializationValue: Expression? = null,
    val descend: Boolean = false,
    override val position: Position? = null,

    // true when the FieldDefinition contains a DIM keyword on its line
    val declaredArrayInLineOnThisField: Int? = null,
    override val const: Boolean = false
) :
    AbstractDataDefinition(name = name, type = type, position = position, const = const) {

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

    val size: Int = type.size

    @property:Link
    @Transient var overlayingOn: AbstractDataDefinition? = null
    internal var overlayTarget: String? = null

    // when they are arrays, how many bytes should we skip into the DS to find the next element?
    // normally it would be the same size as an element of the DS, however if they are declared
    // as on overlay of a field with a DIM keyword, then we should use the size of an element
    // of such field
    val stepSize: Int
        get() {
            return if (declaredArrayInLineOnThisField != null) {
                elementSize()
            } else {
                (overlayingOn as? FieldDefinition)?.stepSize ?: elementSize()
            }
        }

    override fun elementSize(): Int {
        return when {
            container.type is ArrayType -> {
                super.elementSize()
            }
            this.declaredArrayInLine != null -> {
                super.elementSize()
            }
            else -> {
                size
            }
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
            return container.endOffset(this)
        }

    val offsets: Pair<Int, Int>
        get() = Pair(startOffset, endOffset)
}

// Positions 64 through 68 specify the length of the result field. This entry is optional, but can be used to define a
// numeric or character field not defined elsewhere in the program. These definitions of the field entries are allowed
// if the result field contains a field name. Other data types must be defined on the definition specification or on the
// calculation specification using the *LIKE DEFINE operation.

@Serializable
class InStatementDataDefinition(
    override val name: String,
    override val type: Type,
    override val position: Position? = null,
    val initializationValue: Expression? = null,
    override val const: Boolean = false
) : AbstractDataDefinition(name = name, type = type, position = position, const = const) {
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
        var number = 0x0000000
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
    val buffer = IntArray(inChars.size)

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
    val buffer = IntArray(inChars.size / 2 + 1)

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
        buffer[offset++] = (firstNibble + secondNibble)
    }

    // place last digit and sign nibble
    firstNibble = if (inPosition == inChars.size) {
        0x00F0
    } else {
        (inChars[inChars.size - 1].toInt()) and 0x000F shl 4
    }
    if (sign != -1) {
        buffer[offset] = (firstNibble + 0x000F)
    } else {
        buffer[offset] = (firstNibble + 0x000D)
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

    var sign = ""
    var number = ""
    var nibble = ((buffer[buffer.size - 1]) and 0x0F)
    if (nibble == 0x0B || nibble == 0x0D) {
        sign = "-"
    }

    var offset = 0
    while (offset < (buffer.size - 1)) {
        nibble = (buffer[offset] and 0xFF).ushr(4)
        number += Character.toString((nibble or 0x30).toChar())
        nibble = buffer[offset] and 0x0F or 0x30
        number += Character.toString((nibble or 0x30).toChar())

        offset++
    }

    // read last digit
    nibble = (buffer[offset] and 0xFF).ushr(4)
    if (nibble <= 9) {
        number += Character.toString((nibble or 0x30).toChar())
    }
    // adjust the scale
    if (scale > 0 && number != "0") {
        val len = number.length
        number = buildString {
            append(number.substring(0, len - scale))
            append(".")
            append(number.substring(len - scale, len))
        }
    }
    number = sign + number
    return try {
        value.toBigDecimal()
    } catch (e: Exception) {
        number.toBigDecimal()
    }
}

enum class Scope {
    Program, Static, Local
}