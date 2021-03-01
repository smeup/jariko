package com.smeup.rpgparser.utils

import java.math.BigDecimal
import kotlin.system.measureTimeMillis

inline fun <T> enrichPossibleExceptionWith(enrichedMessage: Any?, block: () -> T): T {
    try {
        return block()
    } catch (t: Throwable) {
        throw RuntimeException("Error $t - $enrichedMessage", t)
    }
}

fun measureAndPrint(block: () -> Unit) {
    val timeElapsed = measureAndCatch(block)
    println("Function executed in $timeElapsed milliseconds")
}

inline fun measureAndCatch(block: () -> Unit): Long {
    return measureTimeMillis {
        try {
            block()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}

fun List<String>.chunkAs(elementsPerLine: Int, elmentSize: Int): List<String> {
    if (elementsPerLine == 1) {
        return this
    }
    return this.map {
        it.chunked(elmentSize).take(elementsPerLine)
    }.flatten()
}

fun <T> List<T>.resizeTo(n: Int, defaultValue: T): List<T> {
    if (this.size > n) {
        return this.take(n)
    }
    if (this.size < n) {
        return this + MutableList(n - this.size) { defaultValue }
    }
    return this
}

fun String.divideAtIndex(i: Int): Pair<String, String> {
    if (i + 1 >= this.length) {
        return Pair(this, "")
    }

    if (i < 0) {
        return Pair("", this)
    }

    val intRange = IntRange(i, this.length - 1)
    return Pair(this.take(i), this.slice(intRange))
}

fun String?.isEmptyTrim() = this == null || this.trim().isEmpty()

fun String.substringOfLength(l: Int?): String = if (l != null) take(l) else this

inline fun String?.runIfNotEmpty(code: String.() -> Unit) {
    if (!this.isEmptyTrim()) this?.code()
}

fun String.asLong(): Long = this.trim().toLong()
fun String.asInt(): Int = this.trim().toInt()
fun String?.asIntOrNull(): Int? =
    try {
        this?.trim()?.toInt()
    } catch (e: Exception) {
        null
    }

fun String?.asDouble(): Double {
    if (this == null) {
        return 0.0
    }
    try {
        return java.lang.Double.parseDouble(this)
    } catch (e: Exception) {
        return 0.0
    }
}

fun String.asBigDecimal(): BigDecimal? =
    try {
        BigDecimal(this.trim())
    } catch (e: Exception) {
        null
    }

fun BigDecimal?.isZero() = this != null && BigDecimal.ZERO.compareTo(this) == 0
fun BigDecimal?.isZeroOrNull() = this == null || BigDecimal.ZERO.compareTo(this) == 0

fun Any?.asNonNullString(): String = this?.toString() ?: ""

fun String.moveEndingString(s: String): String =
        if (this.trimEnd().endsWith(s)) {
            s + this.substringBefore(s)
        } else {
            this
        }

fun String.repeatWithMaxSize(l: Int): String {
    val repetitions = (l / this.length) + 1
    return this.repeat(repetitions).take(l)
}

/**
 * Insert line numbers in a string
 * @param padChars Line number will be inserted in a range of source line between 0 and padChars
 * */
fun String.insLineNumber(padChars: Int, filter: (lineNumber: Int) -> Boolean): String {
    return StringBuffer().let { sb ->
        lines().forEachIndexed { index, line ->
            if (filter.invoke(index + 1)) {
                line.padEnd(padChars).let { currentLine ->
                    sb.append("${index + 1}".padEnd(padChars)).append(currentLine.substring(padChars)).append("\n")
                }
            }
        }
        sb.toString()
    }
}