package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.utils.isZero
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

import com.smeup.rpgparser.interpreter.DecEdit.*

internal fun DecimalValue.formatAs(format: String, type: Type, decedit: DecEdit, padChar: Char = ' '): StringValue {
    // %EDITC documentation: https://www.ibm.com/docs/en/i/7.3?topic=80-edtcde-edit-code-keyword-display-files

    // The total length of the %EDITC resulting string is calculated as follows:
    //      length of the field (no matter how many decimal there are)
    //    + potential number of separators (thousands and decimal) even if the real length of the decimal doesn't require separators
    //    + if sign is displayed: number of char of the sign on the right even if it's positive
    // For example: if we assign 1234567 in a field defined "7S 0" with edit code 1
    //     the resulting string will be: '1,234,567' where:
    //     - the total length will be 9
    //     - the field length will be 7: 7 chars for the integer part (1234567) + 0 chars for the decimal part
    //     - the separators to include in the calculation of the length in this case will be 2
    //     - the sign length will be 0

    // String representation anomalies: https://www.mcpressonline.com/programming/rpg/converting-numeric-to-character-and-character-to-numeric

    // Notes about anomalies:
    // - If the amount is too big for the defined field it must be trunked on the left must be trunked on the left
    //   for integer part and on the right for the decimal part.
    //     For example: if we assign -123456.239 in a field defined "7S 2" with edit code A,
    //     the resulting string will be: '34,567.23CR' where:
    //     - the total length will be 11
    //     - the field length will be 7: 5 chars for the integer part (34567) + 2 chars for the decimal part (23)
    //     - the separators to include in the calculation of the length in this case will be only 2
    //     - the sign length will be 2
    // - The spaces reserved for the sign, must be padded at the end of the returned string, while the rest
    // (length of the field + potential separators + edit code char) must be padded at the beginning of the returned string
    //
    // As we can see, the same field defined as numeric 7 might have different resulting string


    var cfgCommasDisplayed = false
    var cfgDecimalPointDisplayed = false
    var cfgSign = ""
    var cfgBlankValueOfQDECFMT = ""
    var cfgIValueOfQDECFMT = ""
    var cfgJValueOfQDECFMT = ""
    var cfgLeadingZeroSuppress = false
    var cfgPadChar = padChar
    var cfgSignPosition = true // true = begin, false = end

    val t = (type as NumberType)

    var wrkTotalLength = 0

    var retValue = ""

    fun thousandSeparators(): Int {
        val ts = (t.entireDigits / 3) - 1
        return if (ts < 0) 0 else ts
    }

    fun decimalSeparators() = if (t.decimalDigits > 0) 1 else 0

    fun getWrkTotalLength(): Int {
        var tot = 0
        if (cfgCommasDisplayed) {
            tot += thousandSeparators()
        }
        if (cfgDecimalPointDisplayed) {
            tot += decimalSeparators()
        }
        tot += cfgSign.length
        tot += t.decimalDigits + t.entireDigits
        return tot
    }

    fun setCfg() {
        when (format) {
            "1" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = ""
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "2" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = ""
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "3" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = ""
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "4" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = ""
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "A" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "B" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "C" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "D" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "J" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "K" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "L"-> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "M" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            "N" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = true
            }

            "O" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = true
            }
            "P" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = true
            }
            "Q" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgSignPosition = true
            }
            "W", "Y", "Z" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = false
                cfgSign = ""
                cfgBlankValueOfQDECFMT = ""
                cfgIValueOfQDECFMT = ""
                cfgJValueOfQDECFMT = ""
                cfgLeadingZeroSuppress = true
                cfgSignPosition = false
            }
            else -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = false
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "0"
                cfgIValueOfQDECFMT = "0"
                cfgJValueOfQDECFMT = "0"
                cfgLeadingZeroSuppress = false
                cfgPadChar = '0'
                cfgSignPosition = false
            }

        }
    }

    fun decEditToString(): String {
        return when(decedit) {
            DOT -> "."
            COMMA -> ","
            else -> ""
        }
    }

    fun trunkAmount() {
        var workValue = retValue
        var workIntPart = ""
        var workDecPart = ""

        val splitChar = decEditToString()
        val parts = workValue.split(splitChar)

        workIntPart = parts[0]

        if (parts.size > 1)
            workDecPart = parts[1]

        // trunk int part
        if (workIntPart.length > t.entireDigits && t.entireDigits > 0) {
            val startIndexToRemove = workIntPart.length - t.entireDigits
            workIntPart = workIntPart.substring(startIndexToRemove)
        }

        // trunk dec part
        if (workDecPart.length > t.decimalDigits) {
            val lastIndexToRemove = workDecPart.length - t.decimalDigits
            workDecPart = workDecPart.substring(0, lastIndexToRemove)
        }

        workValue = workIntPart + splitChar + workDecPart

        retValue = workValue
    }

    fun standardDecimalFormat(type: NumberType, locale: Locale) =
        DecimalFormat(decimalPattern(type), DecimalFormatSymbolsRepository.getSymbols(locale)).format(this.value.abs())


    fun getStandardFormat(): String {
        if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITC: $type")
        return when (decedit) {
            COMMA -> {
                standardDecimalFormat(type, Locale.ITALY)
            }
            ZERO_COMMA -> {
                if (this.value.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.ITALY))
                    }
                } else {
                    standardDecimalFormat(type, Locale.ITALY)
                }
            }
            ZERO_DOT -> {
                if (this.value.abs() < BigDecimal.ONE) {
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
        DecimalFormat(buildString { append("#"); append(decimalsFormatString(type)) }, DecimalFormatSymbolsRepository.italianSymbols).format(this.value.abs())

    fun usDecimalFormatWithNoThousandsSeparator(type: NumberType) =
        DecimalFormat(buildString { append("#"); append(decimalsFormatString(type)) }, DecimalFormatSymbolsRepository.usSymbols).format(this.value.abs())

    fun getItalianFormat(): String {
        if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITC: $type")
        return when (decedit) {
            COMMA -> {
                italianDecimalFormatWithNoThousandsSeparator(type)
            }
            ZERO_COMMA -> {
                if (this.value.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.ITALY))
                    }
                } else {
                    italianDecimalFormatWithNoThousandsSeparator(type)
                }
            }
            ZERO_DOT -> {
                if (this.value.abs() < BigDecimal.ONE) {
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
                    .format(this.value.abs())
            }
        }
    }

    // The functions below correspond to the EDITC parameter, one function per value

    fun toBlank(c: Char) = if (c == '0') ' ' else c

    fun fY(): String {
        var stringN = this.value.abs().unscaledValue().toString().trim()
        return if (type.elementSize() <= 6) {
            stringN = stringN.padStart(6, '0')
            "${toBlank(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}".padStart(type.size + 2)
        } else {
            stringN = stringN.padStart(8, '0')
            "${toBlank(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}${stringN[6]}${stringN[7]}".padStart(type.size + 2)
        }
    }

    fun removeLeadingZeros(num: String): String {
        for (i in 0 until num.length) {
            if (num[i] != '0') {
                return num.substring(i)
            }
        }
        return "0"
    }

    fun handleInitialZero(): String {
        return if (this.value.isZero()) {
            ""
        } else {
            retValue.replace(".", "").replace(",", "").trim()
        }
    }

    fun fX(): String {
        return handleInitialZero()
    }

    fun fZ(): String {
        val a = handleInitialZero()
        val b = removeLeadingZeros(a)
        val c = b.padStart(wrkTotalLength, cfgPadChar)
        return c
    }

    // set edit code configuration
    setCfg()

    // parse to local format
    if (cfgCommasDisplayed) {
        retValue = getStandardFormat()
    } else {
        retValue = getItalianFormat()
    }

    // get total length
    wrkTotalLength = getWrkTotalLength()

    // trunk overflow digits
    trunkAmount()

    // append sign
    if (cfgSign.isNotEmpty()) {
        if (this.value < ZERO) {
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

    // padding start
    retValue = retValue.padStart(wrkTotalLength, cfgPadChar)

    // check final size


    return when (format) {
        "1", "2", "3", "4", "A", "B", "C", "D",
        "J", "K", "L", "M", "N", "O", "P", "Q" -> StringValue(retValue)
        "X" -> StringValue(fX())
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
