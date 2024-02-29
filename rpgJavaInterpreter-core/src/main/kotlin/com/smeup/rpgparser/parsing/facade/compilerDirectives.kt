package com.smeup.rpgparser.parsing.facade

import java.util.regex.Pattern

// If true, the last read line from the source will be used in the resulting source.
var useRow: Boolean = true
var definitions = mutableListOf<String>()
var lastIfCode: String = ""

val IF_DEFINED_PATTERN = Regex(""".{6}/IF\sDEFINED\(([\w£$§,]+)\)$""", RegexOption.IGNORE_CASE)
val IF_NOT_DEFINED_PATTERN = Regex(""".{6}/IF\sNOT\sDEFINED\(([\w£$§,]+)\)$""", RegexOption.IGNORE_CASE)
val DEFINE_PATTERN = Regex(""".{6}/DEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val UNDEFINE_PATTERN = Regex(""".{6}/UNDEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val ELSE_PATTERN = Regex(""".{6}/ELSE""", RegexOption.IGNORE_CASE)
val ENDIF_PATTERN = Regex(""".{6}/ENDIF""", RegexOption.IGNORE_CASE)

fun String.resolveCompilerDirectives(): String {
    var result = ""
    // Split input text into rows
    val rows = this.split("\n")

    // Check each row against regex patterns
    for (row in rows) {
        when {
            IF_DEFINED_PATTERN.matches(row) -> manageIfDefined(row)
            IF_NOT_DEFINED_PATTERN.matches(row) -> manageIfNotDefined(row)
            DEFINE_PATTERN.matches(row) -> manageDefine(row)
            UNDEFINE_PATTERN.matches(row) -> manageUndefine(row)
            ELSE_PATTERN.matches(row) -> manageElse()
            ENDIF_PATTERN.matches(row) -> manageEndIf()
            else -> {
                if (useRow) {
                    result += row.plus("\n")
                }
            }
        }
    }
    return result
}

fun manageEndIf() {
    if (isDefined(lastIfCode)) {
        lastIfCode = ""
    }
}

fun manageElse() {
    useRow = !useRow
}

fun manageUndefine(row: String) {
    val matchResult = UNDEFINE_PATTERN.matchEntire(row)
    val code = matchResult?.groups?.get(1)?.value
    if (code != null) {
        definitions.remove(code.uppercase())
    }
}

fun manageDefine(row: String) {
    val matchResult = DEFINE_PATTERN.matchEntire(row)
    val code = matchResult?.groups?.get(1)?.value
    if (code != null) {
        definitions.add(code.uppercase())
    }
}

fun manageIfNotDefined(row: String) {
    val matchResult = IF_NOT_DEFINED_PATTERN.matchEntire(row)
    val code = matchResult?.groups?.get(1)?.value
    if (code != null) {
        lastIfCode = code.uppercase()
        useRow = !isDefined(code)
    }
}

private fun manageIfDefined(row: String) {
    val matchResult = IF_DEFINED_PATTERN.matchEntire(row)
    val code = matchResult?.groups?.get(1)?.value
    if (code != null) {
        lastIfCode = code.uppercase()
        useRow = isDefined(code)
    }
}

private fun isDefined(code: String): Boolean {
    return definitions.any { it.equals(code.uppercase()) }
}

fun getContentInsideParentheses(input: String): String? {
    val regexPattern = Regex(""".{6}/DEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
    val matchResult = regexPattern.matchEntire(input)
    return matchResult?.groups?.get(1)?.value
}

fun main() {
    // Example usage:
    val testString = "      /DEFINE DMSE_INCLUDED"
    val contentInsideParentheses = getContentInsideParentheses(testString)

    if (contentInsideParentheses != null) {
        println("Content inside parentheses: $contentInsideParentheses")
    } else {
        println("The string does not match the specified format.")
    }
}