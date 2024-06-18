package com.smeup.rpgparser.interpreter

/**
 * Represents the return state by an EXFMT statement
 */
data class OnExfmtResponse(
    val runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot,
    val values: Map<String, Value>
)