package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.parsing.parsetreetoast.fireErrorEvent

enum class SYN_RELEVANT_DIRECTIVES {
    NONE,
    IF_DEFINED,
    IF_NOT_DEFINED,
    ELSE,
    ENDIF
}

// List used to skip the resolution procedure when the source does not contain compiler directives.
val DIRECTIVES_KEYWORD = listOf("      /IF",
                                "      /DEFINE",
                                "      /UNDEFINE",
                                "      /ELSE",
                                "      /ENDIF")

// Search patterns for identifying compiler directive rows.
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

    var lastCode: SYN_RELEVANT_DIRECTIVES = SYN_RELEVANT_DIRECTIVES.NONE
    var result = ""
    val definitions = mutableListOf<String>()
    var useRow = true
    // Split input text into rows
    val rows = this.lines()

    // Check each row against regex patterns
    for ((index, row) in rows.withIndex()) {
        when {
            IF_DEFINED_PATTERN.matches(row) -> {
                // Control if IF_DEFINED is acceptable
                val acceptedLastCodes = listOf(SYN_RELEVANT_DIRECTIVES.NONE, SYN_RELEVANT_DIRECTIVES.ENDIF)
                if (!acceptedLastCodes.any { it == lastCode }) {
                        val exc = CompilerDirectivesException("Unexpected IF_DEFINED directive at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
                }

                lastCode = SYN_RELEVANT_DIRECTIVES.IF_DEFINED
                val matchResult = IF_DEFINED_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {

                    useRow = isDefined(definitions, code)
                }
            }
            IF_NOT_DEFINED_PATTERN.matches(row) -> {
                // Control if IF_NOT_DEFINED is acceptable
                val acceptedLastCodes = listOf(SYN_RELEVANT_DIRECTIVES.NONE, SYN_RELEVANT_DIRECTIVES.ENDIF)
                if (!acceptedLastCodes.any { it == lastCode }) {
                    val exc = CompilerDirectivesException("Unexpected IF_NOT_DEFINED directive at line " + (index + 1))
                    throw AstCreatingException(this, exc).fireErrorEvent(null)
                }

                lastCode = SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED
                val matchResult = IF_NOT_DEFINED_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    useRow = !isDefined(definitions, code)
                }
            }
            DEFINE_PATTERN.matches(row) -> {
                // Define is allways acceptable
                val matchResult = DEFINE_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    definitions.add(code.uppercase())
                }
            }
            UNDEFINE_PATTERN.matches(row) -> {
                // Undefine is allways acceptable
                val matchResult = UNDEFINE_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    definitions.remove(code.uppercase())
                }
            }
            ELSE_PATTERN.matches(row) -> {
                // Control if ELSE is acceptable
                val acceptedLastCodes = listOf(SYN_RELEVANT_DIRECTIVES.IF_DEFINED, SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED)
                if (!acceptedLastCodes.any { it == lastCode }) {
                    val exc = CompilerDirectivesException("Unexpected ELSE directive at line " + (index + 1))
                    throw AstCreatingException(this, exc).fireErrorEvent(null)
                }

                lastCode = SYN_RELEVANT_DIRECTIVES.ELSE
                useRow = !useRow
            }
            ENDIF_PATTERN.matches(row) -> {
                // Control if ENDIF is acceptable
                val acceptedLastCodes = listOf(SYN_RELEVANT_DIRECTIVES.IF_DEFINED, SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED, SYN_RELEVANT_DIRECTIVES.ELSE)
                if (!acceptedLastCodes.any { it == lastCode }) {
                    val exc = CompilerDirectivesException("Unexpected ENDIF directive at line " + (index + 1))
                    throw AstCreatingException(this, exc).fireErrorEvent(null)
                }

                lastCode = SYN_RELEVANT_DIRECTIVES.ENDIF
                useRow = true
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

class CompilerDirectivesException(message: String) : Exception(message)
