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

import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.isFloatingPointNumber
import com.smeup.rpgparser.parsing.parsetreetoast.isNumber
import com.smeup.rpgparser.utils.repeatWithMaxSize
import java.math.BigDecimal
import java.math.RoundingMode

private fun coerceBlanks(type: Type): Value =
    when (type) {
        is StringType -> {
            blankString(type.length)
        }
        is ArrayType -> {
            createArrayValue(type.element, type.nElements) {
                coerceBlanks(type.element)
            }
        }
        is NumberType -> {
            // I use this method in order to create a BigDecimal with the correct number of digits
            coerce(DecimalValue.ZERO, type)
        }
        is DataStructureType -> {
            type.blank()
        }
        is OccurableDataStructureType -> {
            type.blank()
        }
        is BooleanType -> {
            BooleanValue.FALSE
        }
        is CharacterType -> {
            blankString(type.nChars)
            // TODO Use CharacterValue(Array(this.nChars) { ' ' })
        }
        is UnlimitedStringType -> {
            UnlimitedStringValue("")
        }
        is RecordFormatType -> {
            type.blank()
        }
        is TimeStampType -> {
            TimeStampValue.LOVAL
        }
        else -> TODO("Converting BlanksValue to $type")
    }

// TODO: is it correct?
private fun String.toNumberSize(size: Int): String {
    var result = this
    while (result.length < size) {
        result = "0" + result
    }
    return result
}

private fun coerceString(
    value: StringValue,
    type: Type,
): Value {
    return when (type) {
        is StringType -> {
            if (value.value.length > type.length) {
                return StringValue(value.value.substring(0, type.length))
            }
            return when (type.varying) {
                // If varying, can be empty (length=0)
                true -> StringValue(value.value, true)
                // If not varying, cannot be empty but must be sized ad type.length
                else ->
                    if (value.value.isEmpty()) {
                        StringValue(" ".repeat(type.length), false)
                    } else {
                        StringValue(value.value.padEnd(type.size, ' '), false)
                    }
            }

            // return StringValue(value.value, type.varying)
        }
        is ArrayType -> {
            if (type.element is StringType) {
                // We are coercing a String into an array of Strings
                // We split the string in substrings and copy each piece into
                // an element of the array

                val elementSize = type.element.size
                createArrayValue(type.element, type.nElements) {
                    val valueForArray =
                        value.value
                            .substring(0, Math.min(elementSize, value.value.length))
                            .padEnd(elementSize)
                    StringValue(valueForArray)
                }
            } else {
                createArrayValue(type.element, type.nElements) {
                    type.element.blank()
                }
            }
        }

        is NumberType -> {
            if (type.integer) {
                when {
                    // TODO commented out see #45
                    // value.isBlank() -> IntValue.ZERO
                    type.rpgType == RpgType.BINARY.rpgType -> {
                        val intValue = decodeBinary(value.value.toNumberSize(type.size), type.size)
                        IntValue(intValue.longValueExact())
                    }
                    type.rpgType == RpgType.INTEGER.rpgType -> {
                        val intValue = decodeInteger(value.value.toNumberSize(type.size), type.size)
                        IntValue(intValue.longValueExact())
                    }
                    type.rpgType == RpgType.UNSIGNED.rpgType -> {
                        val intValue = decodeUnsigned(value.value.toNumberSize(type.size), type.size)
                        IntValue(intValue.longValueExact())
                    }
                    type.rpgType == RpgType.ZONED.rpgType -> {
                        if (!value.isBlank()) {
                            val intValue = decodeFromZoned(value.value.trim(), type.numberOfDigits, type.decimalDigits)
                            IntValue(intValue.longValueExact())
                        } else {
                            DecimalValue(BigDecimal.ZERO)
                        }
                    }
                    else -> {
                        val withoutPadding = value.value.trimEnd(' ')
                        if (withoutPadding.isNotEmpty()) {
                            if (withoutPadding.isNumber()) {
                                IntValue(withoutPadding.toLong())
                            } else {
                                // A Packed could end with a char. Consider MUDRNRAPU00115.
                                val packedValue = decodeFromPacked(withoutPadding, type.numberOfDigits, type.decimalDigits)
                                IntValue(packedValue.longValueExact())
                            }
                        } else {
                            IntValue(0)
                        }
                    }
                }
            } else {
                if (!value.isBlank()) {
                    when {
                        type.rpgType == RpgType.ZONED.rpgType -> {
                            val decimalValue = decodeFromZoned(value.value.trim(), type.numberOfDigits, type.decimalDigits)
                            DecimalValue(decimalValue)
                        }
                        else -> {
                            /*
                             * This logic covers:
                             * - a DB number which corresponds to the type;
                             * - a Packed number to decode;
                             * - a Packed number not encoded. In this case could be extracted from DS field declared as array.
                             *
                             *  For example, during the execution of `MUDRNRAPU00254`, we have:
                             *   NumberType(entireDigits=21, decimalDigits=9, rpgType=P)
                             *  and:
                             *   StringValue[11](1.000000000)
                             */
                            val decimalValue = value.value.trim()
                            val isFloatWithNotation = decimalValue.isFloatingPointNumber() && decimalValue.contains("e", ignoreCase = true)
                            if (decimalValue.isNumber() || isFloatWithNotation) {
                                val isDecimal = decimalValue.lastIndexOf('.') != -1
                                if (isDecimal || isFloatWithNotation) {
                                    return DecimalValue(decimalValue.toBigDecimal())
                                }

                                val numberPaddedLeft = decimalValue.padStart(type.entireDigits, '0')
                                val numberWithDot =
                                    StringBuilder(numberPaddedLeft)
                                        .apply {
                                            insert(numberPaddedLeft.length - type.decimalDigits, ".")
                                        }.toString()

                                DecimalValue(numberWithDot.toBigDecimal())
                            } else {
                                // A Packed could end with a char. Consider MUDRNRAPU00115.
                                val packedValue = decodeFromPacked(value.value.trimEnd(), type.numberOfDigits, type.decimalDigits)
                                DecimalValue(packedValue)
                            }
                        }
                    }
                } else {
                    DecimalValue(BigDecimal.ZERO)
                }
            }
        }
        is BooleanType -> {
            if ("1" == value.value.trim()) {
                BooleanValue.TRUE
            } else {
                BooleanValue.FALSE
            }
        }
        is DataStructureType -> {
            if (value.isBlank()) {
                type.blank()
            } else {
                DataStructValue(value.value.padEnd(type.elementSize))
            }
        }
        is CharacterType -> {
            return StringValue(value.value)
        }
        is UnlimitedStringType -> {
            return UnlimitedStringValue(value.value)
        }
        is TimeStampType -> TimeStampValue.of(value.value)
        else -> TODO("Converting String to $type")
    }
}

private fun coerceBoolean(
    value: BooleanValue,
    type: Type,
): Value {
    // TODO: Add more coercion rules
    return when (type) {
        is BooleanType -> value
        is StringType -> value.asString()
        is UnlimitedStringType -> value.asUnlimitedString()
        is ArrayType -> {
            val coercedValue = coerce(value, type.element)
            ConcreteArrayValue(MutableList(type.nElements) { coercedValue }, type.element)
        }
        else -> TODO("Converting BooleanValue to $type")
    }
}

fun coerce(
    value: Value,
    type: Type,
): Value {
    // TODO to be completed
    return when (value) {
        is BlanksValue -> {
            coerceBlanks(type)
        }
        is StringValue -> {
            coerceString(value, type)
        }
        is ArrayValue -> {
            return when (type) {
                is StringType -> {
                    val result = value.asString()
                    if (result.length() > type.length) {
                        result.setSubstring(0, type.length)
                    }
                    StringValue(
                        result.value.padEnd(type.length, ' '),
                        type.varying,
                    )
                }
                is ArrayType -> {
                    if (value.elements().size > type.nElements) {
                        // coerce elements and truncate array
                        val values: MutableList<Value> = mutableListOf()
                        for (i in 1..type.numberOfElements()) {
                            values.add(coerce(value.getElement(i), type.element))
                        }
                        ConcreteArrayValue(values, type.element)
                    } else {
                        if (value.elements().size == type.nElements) {
                            // coerce elements and set new type creating new instance
                            val values: MutableList<Value> =
                                value
                                    .elements()
                                    .map {
                                        coerce(it, type.element)
                                    }.toMutableList()
                            ConcreteArrayValue(values, type.element)
                        } else {
                            // create an array blank and set element
                            val array = coerceBlanks(type) as ConcreteArrayValue
                            value.elements().forEachIndexed { i, v ->
                                array.setElement(i + 1, coerce(v, type.element))
                            }
                            array
                        }
                    }
                }
                else -> TODO("Converting ArrayValue to $type")
            }
        }

        is DecimalValue -> {
            when (type) {
                is NumberType -> {
                    // TODO verify the Rounding mode
                    if (type.decimalDigits < value.value.scale()) {
                        val roundingMode = if (value.isPositive()) RoundingMode.FLOOR else RoundingMode.CEILING
                        return DecimalValue(value.value.setScale(type.decimalDigits, roundingMode))
                    } else {
                        return DecimalValue(value.value.setScale(type.decimalDigits))
                    }
                }
                is ArrayType -> {
                    val coercedValue = coerce(value, type.element)
                    ConcreteArrayValue(MutableList(type.nElements) { coercedValue }, type.element)
                }
                is DataStructureType -> {
                    val coercedValue =
                        value.value
                            .toString()
                            .replace(".", "")
                            .padStart(type.size, '0')
                    DataStructValue(coercedValue, type)
                }
                else -> TODO("Converting DecimalValue to $type")
            }
        }

        is HiValValue -> {
            return type.hiValue()
        }
        is LowValValue -> {
            return type.lowValue()
        }
        is AllValue -> {
            when (type) {
                is NumberType -> {
                    return coerce(StringValue(value.charsToRepeat.repeatWithMaxSize(type.size)), type)
                }
                is StringType -> {
                    return StringValue(value.charsToRepeat.repeatWithMaxSize(type.length))
                }
                is ArrayType -> {
                    return StringValue(value.charsToRepeat.repeatWithMaxSize(type.size))
                }
                is BooleanType -> {
                    if (value.charsToRepeat == "1") BooleanValue.TRUE else BooleanValue.FALSE
                }
                else -> TODO("Converting $value to $type")
            }
        }
        is IntValue -> {
            when (type) {
                is StringType -> StringValue(value.value.toString(), varying = type.varying)
                is DateType -> DateValue(value.value, type.format)
                is NumberType -> if (type.decimalDigits > 0) value.asDecimal() else value
                is ArrayType -> {
                    val coercedValue = coerce(value, type.element)
                    ConcreteArrayValue(MutableList(type.nElements) { coercedValue }, type.element)
                }
                is DataStructureType -> {
                    val coercedValue = value.value.toString().padStart(type.size, '0')
                    DataStructValue(coercedValue, type)
                }
                else -> value
            }
        }
        is BooleanValue -> coerceBoolean(value, type)
        is UnlimitedStringValue -> coerceString(value.value.asValue(), type)
        is DataStructValue -> coerceDataStruct(value, type)
        else -> value
    }
}

/**
 * Coerces a given DataStructValue to a target type.
 *
 * This method handles type conversions for a DataStructValue into its corresponding
 * target type while enforcing rules such as length constraints for string types.
 *
 * @param value the DataStructValue to be coerced
 * @param type the target Type to which the value is coerced
 * @return a Value instance after applying the necessary transformations
 */
private fun coerceDataStruct(
    value: DataStructValue,
    type: Type,
): Value =
    when (type) {
        is StringType -> {
            val valueAsString: StringValue = value.asString().copy()
            if (type.varying && valueAsString.length() < type.length) {
                valueAsString.pad(type.length)
            } else if (valueAsString.length() > type.length) {
                valueAsString.setSubstring(0, type.length)
            }

            valueAsString
        }
        is DataStructureType -> value
        is NumberType -> {
            val valueAsStringValue = value.getSubstring(0, value.len)
            coerceString(valueAsStringValue, type)
        }
        else -> TODO("Converting DataStructValue to $type")
    }

fun Type.lowValue(): Value =
    when (this) {
        is NumberType -> computeLowValue(this)
        is StringType -> computeLowValue(this)
        is ArrayType -> createArrayValue(this.element, this.nElements) { coerce(LowValValue, this.element) }
        is DataStructureType -> {
            val fields = this.fields.associateWith { field -> field.type.lowValue() }
            DataStructValue.fromFields(fields = fields, type = this)
        }
        is BooleanType -> BooleanValue.FALSE
        is RecordFormatType -> BlanksValue
        else -> TODO("Converting LowValValue to $this")
    }

fun Type.hiValue(): Value =
    when (this) {
        is NumberType -> computeHiValue(this)
        is StringType -> computeHiValue(this)
        is ArrayType -> createArrayValue(this.element, this.nElements) { coerce(HiValValue, this.element) }
        is DataStructureType -> {
            val fields = this.fields.associateWith { field -> field.type.hiValue() }
            DataStructValue.fromFields(fields = fields, type = this)
        }
        is BooleanType -> BooleanValue.TRUE
        is RecordFormatType -> BlanksValue
        else -> TODO("Converting HiValValue to $this")
    }

fun Value.hiValue(): Value =
    when (this) {
        is StringValue -> StringValue(hiValueString(this.value.length))
        else -> TODO("Converting HiValValue to $this")
    }

private fun computeHiValue(type: NumberType): Value {
    // Packed and Zone
    if (type.rpgType == RpgType.PACKED.rpgType || type.rpgType == RpgType.ZONED.rpgType || type.rpgType == "") {
        return if (type.decimalDigits == 0) {
            val ed = "9".repeat(type.entireDigits)
            IntValue(ed.toLong())
        } else {
            val ed = "9".repeat(type.entireDigits)
            val dd = "9".repeat(type.decimalDigits)
            DecimalValue(("$ed.$dd".toBigDecimal()))
        }
    }
    // Integer
    if (type.rpgType == RpgType.INTEGER.rpgType) {
        return when (type.entireDigits) {
            1, 3 -> IntValue(Byte.MAX_VALUE.toLong())
            5 -> IntValue(Short.MAX_VALUE.toLong())
            10 -> IntValue(Int.MAX_VALUE.toLong())
            else -> IntValue(Long.MAX_VALUE)
        }
    }
    // Unsigned
    if (type.rpgType == RpgType.UNSIGNED.rpgType) {
        return when (type.entireDigits) {
            1 -> IntValue(UByte.MAX_VALUE.toLong())
            2 -> IntValue(UByte.MAX_VALUE.toLong())
            3 -> IntValue(UByte.MAX_VALUE.toLong())
            4 -> IntValue(UShort.MAX_VALUE.toLong())
            5 -> IntValue(UShort.MAX_VALUE.toLong())
            10 -> IntValue(UInt.MAX_VALUE.toLong())
            else -> TODO("Number with ${type.entireDigits} digit is too big for IntValue")
        }
    }
    // Binary
    if (type.rpgType == RpgType.BINARY.rpgType) {
        val ed = "9".repeat(type.entireDigits)
        return IntValue(ed.toLong())
    }
    TODO("Type ${type.rpgType} with ${type.entireDigits} digit is not valid")
}

private fun computeLowValue(type: NumberType): Value {
    // Packed and Zone
    if (type.rpgType == RpgType.PACKED.rpgType || type.rpgType == RpgType.ZONED.rpgType || type.rpgType.isNullOrBlank()) {
        return if (type.decimalDigits == 0) {
            val ed = "9".repeat(type.entireDigits)
            IntValue("-$ed".toLong())
        } else {
            val ed = "9".repeat(type.entireDigits)
            val dd = "9".repeat(type.decimalDigits)
            DecimalValue(("-$ed.$dd".toBigDecimal()))
        }
    }
    // Integer
    if (type.rpgType == RpgType.INTEGER.rpgType) {
        return when (type.entireDigits) {
            3 -> IntValue(Byte.MIN_VALUE.toLong())
            5 -> IntValue(Short.MIN_VALUE.toLong())
            10 -> IntValue(Int.MIN_VALUE.toLong())
            else -> IntValue(Long.MIN_VALUE)
        }
    }
    // Unsigned
    if (type.rpgType == RpgType.UNSIGNED.rpgType) {
        return IntValue.ZERO
    }
    // Binary
    if (type.rpgType == RpgType.BINARY.rpgType) {
        val ed = "9".repeat(type.entireDigits)
        return IntValue("-$ed".toLong())
    }
    TODO("Type '${type.rpgType}' with ${type.entireDigits} digit is not valid")
}

private fun computeHiValue(type: StringType): Value = StringValue(hiValueString(type.size))

private fun computeLowValue(type: StringType): Value = StringValue(lowValueString(type.size))

// TODO
private fun hiValueString(size: Int) = "\uFFFF".repeat(size)

// TODO
private fun lowValueString(size: Int) = " ".repeat(size)
