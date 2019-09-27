package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.utils.isZero
import java.math.BigDecimal.ZERO
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

internal fun DecimalValue.formatAs(format: String, type: Type, decedit: String): StringValue {
    fun signumChar(empty: Boolean) = (if (this.value < ZERO) "-" else if (empty) "" else " ")

    fun commas(t: NumberType) = if (t.entireDigits <= 3) 0 else t.entireDigits / 3
    fun points(t: NumberType) = if (t.decimalDigits > 0) 1 else 0

    fun nrOfPunctuationsIn(t: NumberType): Int {
        return commas(t) + points(t)
    }

    fun decimalsFormatString(t: NumberType) = if (t.decimalDigits == 0) "" else "." + "".padEnd(t.decimalDigits, '0')

    fun f1(decedit: String): String {
        if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITC: $type")

        return when (decedit) {
            "," -> {
                val s = DecimalFormat("#,###" + decimalsFormatString(type), DecimalFormatSymbols(Locale.ITALIAN)).format(this.value.abs())
                s.padStart(type.size.toInt() + nrOfPunctuationsIn(type))
            }
            "0," -> {
                val s = DecimalFormat("#,###" + decimalsFormatString(type), DecimalFormatSymbols(Locale.ITALIAN)).format(this.value.abs())
                s.padStart(type.size.toInt() + nrOfPunctuationsIn(type))
            }
            else -> {
                val s = DecimalFormat("#,###" + decimalsFormatString(type), DecimalFormatSymbols(Locale.US)).format(this.value.abs())
                s.padStart(type.size.toInt() + nrOfPunctuationsIn(type))
            }
        }
    }

    fun f2(decedit: String): String = if (this.value.isZero()) "".padStart(type.size.toInt() + nrOfPunctuationsIn(type as NumberType)) else f1(decedit)

    fun f3(decedit: String): String {
        if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITC: $type")
        return when (decedit) {
            "," -> {
                val s = DecimalFormat("#" + decimalsFormatString(type), DecimalFormatSymbols(Locale.ITALIAN)).format(this.value.abs())
                s.padStart(type.size.toInt() + points(type))
            }
            "0," -> {
                val s = DecimalFormat("#" + decimalsFormatString(type), DecimalFormatSymbols(Locale.ITALIAN)).format(this.value.abs())
                s.padStart(type.size.toInt() + points(type))
            }
            else -> {
                val s = DecimalFormat("#" + decimalsFormatString(type), DecimalFormatSymbols(Locale.US)).format(this.value.abs())
                s.padStart(type.size.toInt() + points(type))
            }
        }
    }

    fun f4(decedit: String): String = if (this.value.isZero()) "".padStart(type.size.toInt() + points(type as NumberType)) else f3(decedit)

    fun fA(decedit: String): String {
        return if (this.value < ZERO) {
            f1(decedit) + "CR"
        } else {
            f1(decedit)
        }
    }

    fun fB(decedit: String): String = fA(decedit)

    fun fC(decedit: String): String {
        return if (this.value < ZERO) {
            f3(decedit) + "CR"
        } else {
            f3(decedit)
        }
    }

    fun fD(decedit: String): String {
        return if (this.value < ZERO) {
            f3(decedit) + "CR"
        } else {
            f3(decedit)
        }
    }

    fun fJ(decedit: String): String = f1(decedit) + signumChar(true)

    fun fK(decedit: String): String = f2(decedit) + signumChar(true)

    fun fL(decedit: String): String = f3(decedit) + signumChar(true)

    fun fM(decedit: String): String = f4(decedit) + signumChar(true)

    fun fN(decedit: String): String = signumChar(false) + f1(decedit)

    fun fO(decedit: String): String = signumChar(false) + f2(decedit)

    fun fP(decedit: String): String = signumChar(false) + f3(decedit)

    fun fQ(decedit: String): String = signumChar(false) + f4(decedit)

    fun toBlnk(c: Char) = if (c == '0') ' ' else c

    fun fY(decedit: String): String {
        var stringN = this.value.abs().unscaledValue().toString().trim()
        return if (stringN.length <= 6) {
            stringN = stringN.padStart(6, '0')
            "${toBlnk(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}".padStart(type.size.toInt() + 2)
        } else {
            stringN = stringN.padStart(8, '0')
            "${toBlnk(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}${stringN[6]}${stringN[7]}".padStart(type.size.toInt() + 2)
        }
    }

    fun handleInitialZero(decedit: String) : String {
        return if (this.value.isZero()) {
            ""
        } else {
            f1(decedit).replace(".", "").replace(",", "").trim()
        }
    }

    fun fX(decedit: String) =handleInitialZero(decedit).padStart(type.size.toInt(), '0')

    fun fZ(decedit: String) = handleInitialZero(decedit).padStart(type.size.toInt())

    return when (format) {
        "1" -> StringValue(f1(decedit))
        "2" -> StringValue(f2(decedit))
        "3" -> StringValue(f3(decedit))
        "4" -> StringValue(f4(decedit))
        "A" -> StringValue(fA(decedit))
        "B" -> StringValue(fB(decedit))
        "C" -> StringValue(fC(decedit))
        "D" -> StringValue(fD(decedit))
        "X" -> StringValue(fX(decedit))
        "J" -> StringValue(fJ(decedit))
        "K" -> StringValue(fK(decedit))
        "L" -> StringValue(fL(decedit))
        "M" -> StringValue(fM(decedit))
        "N" -> StringValue(fN(decedit))
        "O" -> StringValue(fO(decedit))
        "P" -> StringValue(fP(decedit))
        "Q" -> StringValue(fQ(decedit))
        "Y" -> StringValue(fY(decedit))
        "Z" -> StringValue(fZ(decedit))
        else -> throw UnsupportedOperationException("Unsupported format for %EDITC: $format")
    }
}
