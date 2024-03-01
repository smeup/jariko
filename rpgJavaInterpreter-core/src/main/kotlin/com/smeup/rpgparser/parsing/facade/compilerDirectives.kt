package com.smeup.rpgparser.parsing.facade

val DIRECTIVES_KEYWORD = listOf("      /IF", "      /DEFINE", "      /UNDEFINE", "      /ELSE", "      /ENDIF")

val IF_DEFINED_PATTERN = Regex(""".{6}/IF\sDEFINED\(([\w£$§,]+)\)$""", RegexOption.IGNORE_CASE)
val IF_NOT_DEFINED_PATTERN = Regex(""".{6}/IF\sNOT\sDEFINED\(([\w£$§,]+)\)$""", RegexOption.IGNORE_CASE)
val DEFINE_PATTERN = Regex(""".{6}/DEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val UNDEFINE_PATTERN = Regex(""".{6}/UNDEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val ELSE_PATTERN = Regex(""".{6}/ELSE""", RegexOption.IGNORE_CASE)
val ENDIF_PATTERN = Regex(""".{6}/ENDIF""", RegexOption.IGNORE_CASE)

fun String.resolveCompilerDirectives(): String {

    if (!containDirectives(this)) {
        return this
    }

    var lastIfCode = ""
    var result = ""
    val definitions = mutableListOf<String>()
    var useRow = true
    // Split input text into rows
    val rows = this.lines()

    // Check each row against regex patterns
    for (row in rows) {
        when {
            IF_DEFINED_PATTERN.matches(row) -> {
                val matchResult = IF_DEFINED_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    lastIfCode = code.uppercase()
                    useRow = isDefined(definitions, code)
                }
            }
            IF_NOT_DEFINED_PATTERN.matches(row) -> {
                val matchResult = IF_NOT_DEFINED_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    lastIfCode = code.uppercase()
                    useRow = !isDefined(definitions, code)
                }
            }
            DEFINE_PATTERN.matches(row) -> {
                val matchResult = DEFINE_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    definitions.add(code.uppercase())
                }
            }
            UNDEFINE_PATTERN.matches(row) -> {
                val matchResult = UNDEFINE_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    definitions.remove(code.uppercase())
                }
            }
            ELSE_PATTERN.matches(row) -> {
                useRow = !useRow
            }
            ENDIF_PATTERN.matches(row) -> {
                if (isDefined(definitions, lastIfCode)) {
                    lastIfCode = ""
                }
            }
            else -> {
                if (useRow && row.length > 0) {
                    result += row.plus("\n")
                }
            }
        }
    }
    return result
}

private fun containDirectives(inputString: String): Boolean {
    return DIRECTIVES_KEYWORD.any { keyword -> inputString.contains(keyword, ignoreCase = true) }
}

private fun isDefined(definitions: MutableList<String>, lastIfCode: String) =
    definitions.any { it.equals(lastIfCode.uppercase()) }

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