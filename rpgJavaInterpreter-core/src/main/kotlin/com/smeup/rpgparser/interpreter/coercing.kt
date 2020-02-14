package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.utils.repeatWithMaxSize
import java.math.BigDecimal
import java.math.RoundingMode

private fun coerceBlanks(type: Type): Value {
    return when (type) {
        is StringType -> {
            blankString(type.length)
        }
        is ArrayType -> {
            createArrayValue(type.element, type.nElements) {
                type.element.blank()
            }
        }
        is NumberType -> {
            if (type.integer) {
                IntValue.ZERO
            } else {
                DecimalValue.ZERO
            }
        }
        is DataStructureType -> {
            type.blank()
        }
        is BooleanType -> {
            BooleanValue.FALSE
        }
        else -> TODO("Converting BlanksValue to $type")
    }
}

private fun coerceString(value: StringValue, type: Type): Value {
    return when (type) {
        is StringType -> {
            if (value.value.length > type.length) {
                return StringValue(value.value.substring(0, type.length))
            }
            return StringValue(value.value, type.varying)
        }
        is ArrayType -> {
            if (type.element is StringType) {
                // We are coercing a String into an array of Strings
                // We split the string in substrings and copy each piece into
                // an element of the array

                val elementSize = type.element.size
                createArrayValue(type.element, type.nElements) {
                    val valueForArray = value.value.substring(0, Math.min(elementSize, value.value.length))
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
                        val intValue = decodeBinary(value.value, type.size.toInt())
                        IntValue(intValue.longValueExact())
                    }
                    type.rpgType == RpgType.INTEGER.rpgType -> {
                        val intValue = decodeInteger(value.value, type.size.toInt())
                        IntValue(intValue.longValueExact())
                    }
                    type.rpgType == RpgType.UNSIGNED.rpgType -> {
                        val intValue = decodeUnsigned(value.value, type.size.toInt())
                        IntValue(intValue.longValueExact())
                    }
                    type.rpgType == RpgType.ZONED.rpgType -> {
                        if (!value.isBlank()) {
                            val intValue = decodeFromZoned(value.value.trim(), type.entireDigits, type.decimalDigits)
                            IntValue(intValue.longValueExact())
                        } else {
                            DecimalValue(BigDecimal.ZERO)
                        }
                    }
                    else -> {
                        if (!value.isBlank()) {
                            val intValue = decodeFromDS(value.value.trim(), type.entireDigits, type.decimalDigits)
                            IntValue(intValue.longValueExact())
                        } else {
                            IntValue(0)
                        }
                    }
                }
            } else {
                if (!value.isBlank()) {
                    if (type.rpgType == RpgType.ZONED.rpgType) {
                        val decimalValue = decodeFromZoned(value.value.trim(), type.entireDigits, type.decimalDigits)
                        DecimalValue(decimalValue)
                    } else {
                        val decimalValue = decodeFromDS(value.value.trim(), type.entireDigits, type.decimalDigits)
                        DecimalValue(decimalValue)
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
                DataStructValue(value.value)
            }
        }
        is CharacterType -> {
            return StringValue(value.value)
        }

        else -> TODO("Converting String to $type")
    }
}

fun coerce(value: Value, type: Type): Value {
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
                    value.asString()
                }
                is ArrayType -> {
                    value
                }
                else -> TODO("Converting ArrayValue to $type")
            }
        }

        is DecimalValue -> {
            when (type) {
                is NumberType -> {
                    // TODO verify the Rounding mode
                    if (type.decimalDigits < value.value.scale()) {
                        return DecimalValue(value.value.setScale(type.decimalDigits, RoundingMode.HALF_EVEN))
                    }
                    return value
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
                else -> TODO("Converting $value to $type")
            }
        }
        else -> value
    }
}

fun Type.lowValue(): Value {
    when (this) {
        is NumberType -> {
            return computeLowValue(this)
        }
        is StringType -> {
            return computeLowValue(this)
        }
        is ArrayType -> {
            return createArrayValue(this.element, this.nElements) { coerce(LowValValue, this.element) }
        }
        else -> TODO("Converting LowValValue to $this")
    }
}

fun Type.hiValue(): Value {
    when (this) {
        is NumberType -> {
            return computeHiValue(this)
        }
        is StringType -> {
            return computeHiValue(this)
        }
        is ArrayType -> {
            return createArrayValue(this.element, this.nElements) { coerce(HiValValue, this.element) }
        }
        else -> TODO("Converting HiValValue to $this")
    }
}

private fun computeHiValue(type: NumberType): Value {
    // Packed and Zone
    if (type.rpgType == RpgType.PACKED.rpgType || type.rpgType == RpgType.ZONED.rpgType || type.rpgType == "") {
        return if (type.decimalDigits == 0) {
            val ed = "9".repeat(type.entireDigits)
            IntValue("$ed".toLong())
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
        return IntValue("$ed".toLong())
    }
    TODO("Type ${type.rpgType} with ${type.entireDigits} digit is not valid")
}

private fun computeLowValue(type: StringType): Value = StringValue(lowValueString(type))

private fun computeHiValue(type: StringType): Value = StringValue(hiValueString(type))

// TODO
fun lowValueString(type: StringType) = " ".repeat(type.size)

// TODO
fun hiValueString(type: StringType) = "\uFFFF".repeat(type.size)

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
