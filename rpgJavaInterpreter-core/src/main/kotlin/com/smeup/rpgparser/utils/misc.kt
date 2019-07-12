package com.smeup.rpgparser.utils

fun enrichExceptionWith(v: Any?, block: () -> Unit) {
    try {
        block()
    } catch (t: Throwable) {
        throw RuntimeException("Error ${t} - ${v}", t)
    }
}