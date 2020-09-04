package com.smeup.rpgparser.interpreter

import java.text.SimpleDateFormat

fun Value.stringRepresentation(format: String? = null): String {
    return when (this) {
        is StringValue -> value.trimEnd()
        is BooleanValue -> asString().value // TODO check if it's the best solution
        is NumberValue -> render()
        is ArrayValue -> "[${elements().map { it.render() }.joinToString(", ")}]"
        is TimeStampValue -> timestampFormatting(format)
        is DataStructValue -> value.trimEnd()
        is ZeroValue -> "0"
        is AllValue -> charsToRepeat
        else -> TODO("Unable to render value $this (${this.javaClass.canonicalName})")
    }
}

private fun TimeStampValue.timestampFormatting(format: String?): String =
    // TODO this is a simple stub for what will be the full implementation
    if ("*ISO0" == format) {
        SimpleDateFormat("yyyyMMddHHmmssSSS000").format(value)
    } else {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(value)
    }
