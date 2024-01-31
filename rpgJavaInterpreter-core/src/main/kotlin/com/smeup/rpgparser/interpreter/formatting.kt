package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.utils.isZero
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

import com.smeup.rpgparser.interpreter.DecEdit.*

internal fun DecimalValue.formatAs(format: String, type: Type, decedit: DecEdit, padChar: Char = ' '): StringValue {

    val t = (type as NumberType)
    var wrkTotalLength = 0
    val decValue = this.value
    var retValue = ""

    fun thousandSeparators(): Int {
        val quotient = t.entireDigits / 3
        val reminder = t.entireDigits % 3
        val ts = if (reminder == 0) quotient - 1 else quotient
        return if (ts < 0) 0 else ts
    }

    fun decimalSeparators() = if (t.decimalDigits > 0) 1 else 0

    fun getWrkTotalLength(): Int {
        return t.decimalDigits + t.entireDigits
    }

    fun getDecimalSeparator(): String {
        return when (decedit) {
            DOT, ZERO_DOT -> "."
            COMMA, ZERO_COMMA -> ","
            else -> ""
        }
    }

    fun standardDecimalFormat(type: NumberType, locale: Locale) =
        DecimalFormat(decimalPattern(type), DecimalFormatSymbolsRepository.getSymbols(locale)).format(decValue.abs())

    fun getStandardFormat(): String {
        return when (decedit) {
            COMMA -> {
                standardDecimalFormat(type, Locale.ITALY)
            }
            ZERO_COMMA -> {
                if (decValue.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.ITALY))
                    }
                } else {
                    standardDecimalFormat(type, Locale.ITALY)
                }
            }
            ZERO_DOT -> {
                if (decValue.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.US))
                    }
                } else {
                    standardDecimalFormat(type, Locale.US)
                }
            }
            DOT -> {
                standardDecimalFormat(type, Locale.US)
            }
        }
    }

    fun italianDecimalFormatWithNoThousandsSeparator(type: NumberType) =
        DecimalFormat(buildString { append("#"); append(decimalsFormatString(type)) }, DecimalFormatSymbolsRepository.italianSymbols).format(decValue.abs())

    fun usDecimalFormatWithNoThousandsSeparator(type: NumberType) =
        DecimalFormat(buildString { append("#"); append(decimalsFormatString(type)) }, DecimalFormatSymbolsRepository.usSymbols).format(decValue.abs())

    fun getItalianFormat(): String {
        return when (decedit) {
            COMMA -> {
                italianDecimalFormatWithNoThousandsSeparator(type)
            }
            ZERO_COMMA -> {
                if (decValue.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.ITALY))
                    }
                } else {
                    italianDecimalFormatWithNoThousandsSeparator(type)
                }
            }
            ZERO_DOT -> {
                if (decValue.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.US))
                    }
                } else {
                    usDecimalFormatWithNoThousandsSeparator(type)
                }
            }
            DOT -> {
                DecimalFormat(buildString {
                    append("#")
                    append(decimalsFormatString(type))
                }, DecimalFormatSymbolsRepository.usSymbols)
                    .format(decValue.abs())
            }
        }
    }

    fun removeLeadingZeros() {
        if (decedit == DOT || decedit == COMMA) {
            val workValue = retValue
                var exec = false
                for (i in 0 until workValue.length) {
                    if (workValue[i] != '0') {
                        retValue = workValue.substring(i)
                        exec = true
                        break
                    }
                }
                if (!exec) {
                    retValue = "0"
                }
        }
    }

    // The functions below correspond to the EDITC parameter, one function per value
    fun toBlank(c: Char) = if (c == '0') ' ' else c

    fun appendSign(cfgSign: String, cfgSignPosition: Boolean, cfgPadChar: Char) {
            if (decValue < ZERO) {
                if (cfgSignPosition) {
                    retValue = cfgSign + retValue
                } else {
                    retValue += cfgSign
                }
            } else {
                if (cfgSignPosition) {
                    retValue = cfgPadChar.toString().repeat(cfgSign.length) + retValue
                } else {
                    retValue += cfgPadChar.toString().repeat(cfgSign.length)
                }
            }
    }

    fun removeDecimalSeparator() {
        val decimalSeparator = getDecimalSeparator()
        retValue = retValue.replace(decimalSeparator, "")
    }

    fun addPadding(cfgPadChar: Char) {
        retValue = retValue.padStart(wrkTotalLength, cfgPadChar)
    }

    fun replaceZeroWithBlank() {
        if (decValue.isZero())
            retValue = " ".repeat(wrkTotalLength)
    }

    fun f1(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun f2(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun f3(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun f4(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun fA(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        val cfgSign = "CR"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun fB(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        val cfgSign = "CR"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun fC(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        val cfgSign = "CR"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun fD(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        val cfgSign = "CR"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun fJ(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun fK(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun fL(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun fM(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, false, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun fN(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, true, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun fO(): String {
        // parse to local format
        retValue = getStandardFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += thousandSeparators()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, true, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun fP(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, true, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        return retValue
    }

    fun fQ(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        val cfgSign = "-"
        wrkTotalLength = getWrkTotalLength()
        wrkTotalLength += decimalSeparators()
        wrkTotalLength += cfgSign.length

        // append sign
        appendSign(cfgSign, true, padChar)

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    fun fX(): String {
        if (decValue < ZERO) {
            throw UnsupportedOperationException("Unsupported format for %EDITC: $format with negative values")
        }

        // parse to local format
        retValue = getItalianFormat()

        // get total length
        wrkTotalLength = getWrkTotalLength()

        // remove the decimal separator
        removeDecimalSeparator()

        // padding start
        addPadding('0')

        return retValue
    }

    fun fY(): String {
        // get total length
        wrkTotalLength = getWrkTotalLength()

        var stringN = decValue.abs().unscaledValue().toString().trim()

        val testLen =
        if (decValue.isZero()) {
            wrkTotalLength
        } else {
            stringN.length
        }

        if (testLen <= 2) {
            throw UnsupportedOperationException("Unsupported format for %EDITC: $format with value length < 3")
        } else if (testLen == 3) {
            // "nn⁄n"
            stringN = stringN.padStart(3, '0')
            stringN = "${toBlank(stringN[0])}${stringN[1]}/${stringN[2]}".padStart(wrkTotalLength + 2)
        } else if (testLen == 4) {
            // "nn⁄nn"
            stringN = stringN.padStart(4, '0')
            stringN = "${toBlank(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}".padStart(wrkTotalLength + 2)
        } else if (testLen == 5) {
            // "nn⁄nn⁄n"
            stringN = stringN.padStart(5, '0')
            stringN = "${toBlank(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}".padStart(wrkTotalLength + 2)
        } else if (testLen == 6) {
            // "nn⁄nn⁄nn"
            stringN = stringN.padStart(6, '0')
            stringN = "${toBlank(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}".padStart(wrkTotalLength + 2)
        } else if (testLen == 7) {
            // "nnn⁄nn⁄nn"
            stringN = stringN.padStart(7, '0')
            stringN = "${toBlank(stringN[0])}${stringN[1]}${stringN[2]}/${stringN[3]}${stringN[4]}/${stringN[5]}${stringN[6]}".padStart(wrkTotalLength + 2)
        } else if (testLen == 8) {
            // "nnn⁄nn⁄nn"
            stringN = stringN.padStart(8, '0')
            stringN = "${toBlank(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}${stringN[6]}${stringN[7]}".padStart(wrkTotalLength + 2)
        } else if (testLen == 9) {
            // "nnn⁄nn⁄nnnn"
            stringN = stringN.padStart(9, '0')
            stringN = "${toBlank(stringN[0])}${stringN[1]}${stringN[2]}/${stringN[3]}${stringN[4]}/${stringN[5]}${stringN[6]}${stringN[7]}${stringN[8]}".padStart(wrkTotalLength + 2)
        } else {
            throw UnsupportedOperationException("Unsupported format for %EDITC: $format with value length > 9")
        }
        return stringN
    }

    fun fZ(): String {
        // parse to local format
        retValue = getItalianFormat()

        // get total length
        wrkTotalLength = getWrkTotalLength()

        // remove the decimal separator
        removeDecimalSeparator()

        // suppress leading zeros
        removeLeadingZeros()

        // padding start
        addPadding(padChar)

        // replace 0 with blank
        replaceZeroWithBlank()

        return retValue
    }

    return when (format) {
        "1" -> StringValue(f1())
        "2" -> StringValue(f2())
        "3" -> StringValue(f3())
        "4" -> StringValue(f4())
        "A" -> StringValue(fA())
        "B" -> StringValue(fB())
        "C" -> StringValue(fC())
        "D" -> StringValue(fD())
        "X" -> StringValue(fX())
        "J" -> StringValue(fJ())
        "K" -> StringValue(fK())
        "L" -> StringValue(fL())
        "M" -> StringValue(fM())
        "N" -> StringValue(fN())
        "O" -> StringValue(fO())
        "P" -> StringValue(fP())
        "Q" -> StringValue(fQ())
        "Y" -> StringValue(fY())
        "Z" -> StringValue(fZ())
        else -> throw UnsupportedOperationException("Unsupported format for %EDITC: $format")
    }
}

internal fun DecimalValue.formatAsWord(format: String, type: Type): StringValue {
    fun isConst(formatChar: Char): Boolean =
        when (formatChar) {
            '0', '*' -> false // TODO
            ' ' -> false // TODO see if it's OK
            else -> true
        }

    fun CharArray.cleanZeroesUntil(lastPosition: Int): CharArray {
        loop@ for (i in 0..lastPosition) {
            if (this[i] == '0') {
                this[i] = ' '
            } else if (this[i] in '1'..'9') {
                break@loop
            }
        }
        return this
    }

    fun String.handleSignum(decimalValue: DecimalValue): String =
        if (!decimalValue.isPositive() || this.count { it == '-' } > 1) {
            this
        } else {
            this.replaceFirst("-", " ")
        }

    if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITW: $type")
    val firstZeroInFormat = format.indexOfFirst { it == '0' }
    val wholeNumberAsString =
        this.significantDigitsAsStringJustDigits(type)
            .padStart(format.length)
            .mapIndexed { i, c -> if ((firstZeroInFormat > -1 && i > firstZeroInFormat) && c == ' ') '0' else c }
            .reversed()
            .iterator()
    val reversedResult = " ".repeat(format.length).toCharArray()
    format.reversed().forEachIndexed {
        i, formatChar ->
        if (isConst(formatChar)) {
            reversedResult[i] = formatChar
        } else {
            if (wholeNumberAsString.hasNext()) {
                reversedResult[i] = wholeNumberAsString.next()
            }
        }
    }
    val result =
        reversedResult
        .reversedArray()
        .cleanZeroesUntil(firstZeroInFormat)
        .joinToString(separator = "")
        .handleSignum(this)

    return StringValue(result)
}

object DecimalFormatSymbolsRepository {
    val italianSymbols = DecimalFormatSymbols(Locale.ITALY)
    val usSymbols = DecimalFormatSymbols(Locale.US)
    fun getSymbols(locale: Locale): DecimalFormatSymbols {
        return when (locale) {
            Locale.ITALY -> italianSymbols
            Locale.US -> usSymbols
            else -> DecimalFormatSymbols(locale)
        }
    }
}

private fun decimalPattern(type: NumberType) = buildString {
    append("#,###")
    append(decimalsFormatString(type))
}

fun decimalsFormatString(t: NumberType) =
    if (t.decimalDigits == 0) {
        ""
    } else buildString {
        append(".")
        append("".padEnd(t.decimalDigits, '0'))
    }

fun DecimalValue.significantDigitsAsStringJustDigits(t: NumberType): String = significantDigitsAsString(t).filter(Char::isDigit)
fun DecimalValue.significantDigitsAsString(t: NumberType): String = DecimalFormat(decimalsFormatString(t)).format(this.value)
