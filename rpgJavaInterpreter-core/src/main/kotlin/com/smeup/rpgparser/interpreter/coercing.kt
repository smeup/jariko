package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import java.math.BigDecimal
import java.math.RoundingMode

private fun coerceBlanks(type: Type): Value {
    return when (type) {
        is StringType -> {
            blankValue(type.length.toInt())
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
        else -> TODO("Converting BlanksValue to $type")
    }
}

private fun coerceString(value: StringValue, type: Type): Value {
    return when (type) {
        is StringType -> {
            var s = value.value.padEnd(type.length.toInt(), PAD_CHAR)
            if (value.value.length > type.length) {
                s = s.substring(0, type.length.toInt())
            }
            return StringValue(s)
        }
        is ArrayType -> {
            if (type.element is StringType) {
                val elementSize = type.element.size.toInt()
                val valueForArray = value.value.padEnd(elementSize).take(elementSize)
                createArrayValue(type.element, type.nElements) {
                    // TODO Since value property of StringValue is a var, we cannot share instances of StringValue
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
                    value.isBlank() -> IntValue.ZERO
                    type.rpgType == "B" -> {
                        val intValue = decodeBinary(value.value.trim(), type.entireDigits)
                        IntValue(intValue.longValueExact())
                    }
                    else -> {
                        val intValue = decodeFromDS(value.value.trim(), type.entireDigits, type.decimalDigits)
                        IntValue(intValue.longValueExact())
                    }
                }
            } else {
                if (!value.isBlank()) {
                    val decimalValue = decodeFromDS(value.value.trim(), type.entireDigits, type.decimalDigits)
                    DecimalValue(decimalValue)
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
            type.blank()
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
            when (type) {
                is StringType -> {
                    return value.asString()
                }
                is ArrayType -> {
                    return value
                }
                else -> TODO("Converting ArrayValue to $type")
            }
        }

        is DecimalValue -> {
            when (type) {
                is NumberType -> {
                    // TODO verifiy the Rounding mode
                    if (type.decimalDigits < value.value.scale()) {
                        return DecimalValue(value.value.setScale(type.decimalDigits, RoundingMode.HALF_EVEN))
                    }
                    return value
                }
                else -> TODO("Converting DecimalValue to $type")
            }
        }

        is HiValValue -> {
            when (type) {
                is NumberType -> {
                    return computeHiValue(type)
                }
                is ArrayType -> {
                    when (type.element) {
                        is NumberType -> {
                            return computeHiValue(type.element )
                        }
                        else -> TODO("Converting HiValValue to $type")
                    }
                }
                else -> TODO("Converting HiValValue to $type")
            }
        }
        is LowValValue -> {
            when (type) {
                is NumberType -> {
                    return computeLowValue(type)
                }
                is ArrayType -> {
                    when (type.element) {
                        is NumberType -> {
                            return computeHiValue(type.element )
                        }
                        else -> TODO("Converting LowValValue to $type")
                    }
                }
                else -> TODO("Converting LowValValue to $type")
            }
        }
        else -> value
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
        when (type.entireDigits) {
            1 -> return IntValue(Byte.MAX_VALUE.toLong())
            3 -> return IntValue(Byte.MAX_VALUE.toLong())
            5 -> return IntValue(Short.MAX_VALUE.toLong())
            10 -> return IntValue(Int.MAX_VALUE.toLong())
            else -> return IntValue(Long.MAX_VALUE)
        }
    }
    // Unsigned
    if (type.rpgType == RpgType.UNSIGNED.rpgType) {
        when (type.entireDigits) {
            1 -> return IntValue(UByte.MAX_VALUE.toLong())
            3 -> return IntValue(UByte.MAX_VALUE.toLong())
            5 -> return IntValue(UShort.MAX_VALUE.toLong())
            10 -> return IntValue(UInt.MAX_VALUE.toLong())
            else -> TODO("Number with ${type.entireDigits} digit is too big for IntValue")
        }
    }
    // Binary
    if (type.rpgType == RpgType.BINARY.rpgType) {
        when (type.entireDigits) {
            2 -> {
                val ed = "9".repeat(4)
                return IntValue("$ed".toLong())
            }
            4 -> {
                val ed = "9".repeat(9)
                return IntValue("$ed".toLong())
            }
        }
    }
    TODO("Type ${type.rpgType} with ${type.entireDigits} digit is not valid")
}

private fun computeLowValue(type: NumberType): Value {
    // Packed and Zone
    if (type.rpgType == RpgType.PACKED.rpgType || type.rpgType == RpgType.ZONED.rpgType) {
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
        when (type.entireDigits) {
            3 -> return IntValue(Byte.MIN_VALUE.toLong())
            5 -> return IntValue(Short.MIN_VALUE.toLong())
            10 -> return IntValue(Int.MIN_VALUE.toLong())
            else -> return IntValue(Long.MIN_VALUE)
        }
    }
    // Unsigned
    if (type.rpgType == RpgType.UNSIGNED.rpgType) {
        return IntValue.ZERO
    }
    // Binary
    if (type.rpgType == RpgType.BINARY.rpgType) {
        when (type.entireDigits) {
            2 -> {
                val ed = "9".repeat(4)
                return IntValue("-$ed".toLong())
            }
            4 -> {
                val ed = "9".repeat(9)
                return IntValue("-$ed".toLong())
            }
        }
    }
    TODO("Type ${type.rpgType} with ${type.entireDigits} digit is not valid")
}
