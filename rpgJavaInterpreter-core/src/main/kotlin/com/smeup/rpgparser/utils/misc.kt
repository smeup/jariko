package com.smeup.rpgparser.utils

import java.math.BigDecimal
import kotlin.system.measureTimeMillis

fun <T> enrichPossibleExceptionWith(v: Any?, block: () -> T): T {
    try {
        return block()
    } catch (t: Throwable) {
        throw RuntimeException("Error $t - $v", t)
    }
}

fun measureAndPrint(block: () -> Unit) {
    val timeElapsed = measureAndCatch(block)
    println("Function executed in $timeElapsed milliseconds")
}

fun measureAndCatch(block: () -> Unit): Long {
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
        BigDecimal(this)
    } catch (e: Exception) {
        null
    }

fun BigDecimal?.isZero() = this != null && BigDecimal.ZERO.compareTo(this) == 0
fun BigDecimal?.isZeroOrNull() = this == null || BigDecimal.ZERO.compareTo(this) == 0

fun Any?.asNonNullString(): String = this?.toString() ?: ""

fun String.moveEndingString(s: String): String =
        if (this.endsWith(s)) {
            s + this.substringBefore(s)
        } else {
            this
        }
