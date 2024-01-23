package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.utils.isZero
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

import com.smeup.rpgparser.interpreter.DecEdit.*

internal fun DecimalValue.formatAs(format: String, type: Type, decedit: DecEdit, padChar: Char = ' '): StringValue {
    // https://www.ibm.com/docs/en/i/7.3?topic=80-edtcde-edit-code-keyword-display-files

    var cfgCommasDisplayed = false
    var cfgDecimalPointDisplayed = false
    var cfgSign = ""
    var cfgBlankValueOfQDECFMT = ""
    var cfgIValueOfQDECFMT = ""
    var cfgJValueOfQDECFMT = ""
    var cfgLeadingZeroSuppress = false
    var cfgPadChar = padChar
    var cfgPositiveSignAtBeginning = false

    var padStartLength = 0
    var padEndLength = 0
    var retValue = ""

    val t = (type as NumberType)

    fun decEditToString(): String {
        return when(decedit) {
            DOT -> "."
            COMMA -> ","
            else -> ""
        }
    }

    fun thousandSeparators(): Int {
        //val valueString = this.value.toString()
        //val integers = valueString.split(decEditToString())[0].length
        //return if (integers <= 3) 0 else integers / 3
        return if (t.entireDigits <= 3) 0 else (t.entireDigits / 3) - 1
    }

    fun decimalSeparators() = if (t.decimalDigits > 0) 1 else 0

    fun getPadStartLength(): Int {
        var tot = 0
        if (cfgCommasDisplayed) {
            tot += thousandSeparators()
        }
        if (cfgDecimalPointDisplayed) {
            tot += decimalSeparators()
        }
        if(cfgSign.isNotEmpty() && cfgPositiveSignAtBeginning) {
            if (this.value >= ZERO) {
                tot += cfgSign.length
            }
        }
        tot += t.decimalDigits + t.entireDigits
        return tot
    }

    fun getPadEndLength(): Int {
        var tot = 0
        if(cfgSign.isNotEmpty()) {
            tot += cfgSign.length
        }
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
                cfgPositiveSignAtBeginning = false
            }
            "2" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = ""
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "3" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = ""
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "4" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = ""
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "A" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "B" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "C" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "D" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "CR"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "J" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "N" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = true
            }
            "K" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "O" -> {
                cfgCommasDisplayed = true
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = true
            }
            "L"-> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "P" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = ".00|0"
                cfgIValueOfQDECFMT = ",00|0"
                cfgJValueOfQDECFMT = ",00|0"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = true
            }
            "M" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
            }
            "Q" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = true
                cfgSign = "-"
                cfgBlankValueOfQDECFMT = "Blanks"
                cfgIValueOfQDECFMT = "Blanks"
                cfgJValueOfQDECFMT = "Blanks"
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = true
            }

            "W", "Y", "Z" -> {
                cfgCommasDisplayed = false
                cfgDecimalPointDisplayed = false
                cfgSign = ""
                cfgBlankValueOfQDECFMT = ""
                cfgIValueOfQDECFMT = ""
                cfgJValueOfQDECFMT = ""
                cfgLeadingZeroSuppress = true
                cfgPositiveSignAtBeginning = false
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
                cfgPositiveSignAtBeginning = true
            }

        }
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

    fun italianDecimalformatWithNoThounsandsSeparator(type: NumberType) =
        DecimalFormat(buildString { append("#"); append(decimalsFormatString(type)) }, DecimalFormatSymbolsRepository.italianSymbols).format(this.value.abs())

    fun usDecimalformatWithNoThounsandsSeparator(type: NumberType) =
        DecimalFormat(buildString { append("#"); append(decimalsFormatString(type)) }, DecimalFormatSymbolsRepository.usSymbols).format(this.value.abs())

    fun getItalianFormat(): String {
        if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITC: $type")
        return when (decedit) {
            COMMA -> {
                italianDecimalformatWithNoThounsandsSeparator(type)
            }
            ZERO_COMMA -> {
                if (this.value.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.ITALY))
                    }
                } else {
                    italianDecimalformatWithNoThounsandsSeparator(type)
                }
            }
            ZERO_DOT -> {
                if (this.value.abs() < BigDecimal.ONE) {
                    buildString {
                        append("0")
                        append(standardDecimalFormat(type, Locale.US))
                    }
                } else {
                    usDecimalformatWithNoThounsandsSeparator(type)
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

    fun toBlnk(c: Char) = if (c == '0') ' ' else c

    fun fY(): String {
        var stringN = this.value.abs().unscaledValue().toString().trim()
        return if (type.elementSize() <= 6) {
            stringN = stringN.padStart(6, '0')
            "${toBlnk(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}".padStart(type.size + 2)
        } else {
            stringN = stringN.padStart(8, '0')
            "${toBlnk(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}${stringN[6]}${stringN[7]}".padStart(type.size + 2)
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
        val c = b.padStart(padStartLength, cfgPadChar)
        return c
    }

    setCfg()
    if (cfgCommasDisplayed) {
        retValue = getStandardFormat()
    } else {
        retValue = getItalianFormat()
    }

    if (cfgSign != "No sign" && this.value < ZERO) {
        retValue += cfgSign
    }

    padStartLength = getPadStartLength()
    retValue = retValue.padStart(padStartLength, cfgPadChar)

    if (!cfgPositiveSignAtBeginning) {
        padEndLength = getPadEndLength()
        retValue = retValue.padEnd(padEndLength + padStartLength, cfgPadChar)
    }

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
