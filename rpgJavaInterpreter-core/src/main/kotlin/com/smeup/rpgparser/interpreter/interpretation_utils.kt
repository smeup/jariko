package com.smeup.rpgparser.interpreter

import java.text.SimpleDateFormat

fun Value.stringRepresentation(): String {
    return when (this) {
        is StringValue -> value.trimEnd()
        is BooleanValue -> asString().value // TODO check if it's the best solution
        is NumberValue -> render()
        is ArrayValue -> "[${elements().map { it.render() }.joinToString(", ")}]"
        is TimeStampValue -> SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(value)
        is DataStructValue -> value.trimEnd()
        is ZeroValue -> "0"
        is AllValue -> charsToRepeat
        else -> TODO("Unable to render value $this (${this.javaClass.canonicalName})")
    }
}
