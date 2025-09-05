package com.smeup.dspfparser.utils

internal fun String.extend(
    with: Char,
    until: Int,
): String {
    if (this.isEmpty()) {
        return with.toString().repeat(until)
    }

    return this.replaceAfterLast(this.last().toString(), with.toString().repeat(until - this.length))
}

internal fun String.removeNewLineAndExtend(
    with: Char,
    until: Int,
): String {
    val line = this.replace("\n", "")
    if (until < line.length) throw Exception("Line length (${line.length}) is greater than $until")

    return line.extend(with, until)
}

internal fun String.removeFirst(char: Char): String = this.replaceFirst(char.toString(), "")

internal fun String.removeFirst(string: String): String = this.replaceFirst(string, "")

internal fun String.removeLast(char: Char): String = this.reversed().replaceFirst(char.toString(), "").reversed()

internal fun String.removeLast(string: String): String = this.reversed().replaceFirst(string, "").reversed()
