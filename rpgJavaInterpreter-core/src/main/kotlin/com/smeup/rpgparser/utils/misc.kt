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