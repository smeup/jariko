package com.smeup.rpgparser.utils

import kotlin.system.measureTimeMillis

fun enrichExceptionWith(v: Any?, block: () -> Unit) {
    try {
        block()
    } catch (t: Throwable) {
        throw RuntimeException("Error ${t} - ${v}", t)
    }
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
        return  this
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
        return this + MutableList(n - this.size) {defaultValue}
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