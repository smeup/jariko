package com.smeup.rpgparser.parsing.facade

/**
 * Directives that can be used only under certain conditions (for example, an ELSE must always follow an IF
 * at the same nesting level)."
 */
enum class SYN_RELEVANT_DIRECTIVES {
    NONE,
    IF_DEFINED,
    IF_NOT_DEFINED,
    ELSE,
    ENDIF
}

// List used to skip the resolution procedure when the source does not contain compiler directives.
val DIRECTIVES_KEYWORDS = listOf(
                                "      /IF DEFINED",
                                "      /IF NOT DEFINED",
                                "      /DEFINE",
                                "      /UNDEFINE",
                                "      /ELSE",
                                "      /ENDIF"
                                )

val EOF_DIRECTIVE_KEYWORD = "      /EOF"

// Search patterns for identifying compiler directive rows.
val IF_DEFINED_PATTERN = Regex(""".{6}/IF\sDEFINED\(([\w£$§*,]+)\)$""", RegexOption.IGNORE_CASE)
val IF_NOT_DEFINED_PATTERN = Regex(""".{6}/IF\sNOT\sDEFINED\(([\w£$§,]+)\)$""", RegexOption.IGNORE_CASE)
val DEFINE_PATTERN = Regex(""".{6}/DEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val UNDEFINE_PATTERN = Regex(""".{6}/UNDEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val ELSE_PATTERN = Regex(""".{6}/ELSE""", RegexOption.IGNORE_CASE)
val ENDIF_PATTERN = Regex(""".{6}/ENDIF""", RegexOption.IGNORE_CASE)
val EOF_PATTERN = Regex(""".{6}/EOF""", RegexOption.IGNORE_CASE)

/**
 * Resolve the EOF directive: after this directive, all rows are ignored until the end of the file
 * and marked in the result string as comments.
 * The resolution of this directive is separated from other directives because EOF resolution has
 * to be managed during /COPY directive resolution and cannot be done after all /COPY directives
 * are resolved.
 */
internal fun String.resolveEOFDirective(): String {
    if (!this.contains(EOF_DIRECTIVE_KEYWORD, true)) {
        return this
    }
    // Split input text into rows
    val rows = this.lines()
    val result = StringBuffer()
    var useRow = true
    for (row in rows) {
        if (EOF_PATTERN.matches(row)) {
            useRow = false
            result.appendLine(row.transformToComment())
        } else {
            if (useRow) {
                result.appendLine(row)
            } else {
                result.appendLine(row.transformToComment())
            }
        }
    }
    return result.toString()
}

/*
Receives an RPG source as string and resolves all embedded compile directives.
The response is a string where all lines that should not be executed after resolving
the compile directives are transformed into comment lines. The resolution of
compilation directives must be performed after the resolution of /COPY directives.

Not implemented:

- ELSEIF compiler directive
- Condition expressions special values(used in IF DEFINED directive):
    - *ILERPG
    - *CRTBNDRPG
    - *CRTRPGMOD
    - *THREAD_CONCURRENT
*/
internal fun String.resolveCompilerDirectives(): String {

    if (!containDirectives(this)) {
        return this
    }

    var lastCode: SYN_RELEVANT_DIRECTIVES = SYN_RELEVANT_DIRECTIVES.NONE
    val result = StringBuffer()
    val definitions = mutableListOf<String>()
    var useRow = true
    var eofInvoked = false
    var directiveRow = false
    // Split input text into rows
    val rows = this.lines()
    var ifLevel = 0

    // Check each row against regex patterns
    for ((index, row) in rows.withIndex()) {
        // if eof invoked manage all remaining rows as comments
        if (!eofInvoked) {
            when {
                IF_DEFINED_PATTERN.matches(row) -> {
                    directiveRow = true
                    lastCode = SYN_RELEVANT_DIRECTIVES.IF_DEFINED
                    val matchResult = IF_DEFINED_PATTERN.matchEntire(row)
                    val code = matchResult?.groups?.get(1)?.value
                    if (code != null) {
                        useRow = isDefined(definitions, code)
                        ifLevel++
                    } else {
                        throw CompilerDirectivesException("IF_DEFINED directive without code value at line " + (index + 1))
                    }
                }

                IF_NOT_DEFINED_PATTERN.matches(row) -> {
                    directiveRow = true
                    lastCode = SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED
                    val matchResult = IF_NOT_DEFINED_PATTERN.matchEntire(row)
                    val code = matchResult?.groups?.get(1)?.value
                    if (code != null) {
                        if (code.startsWith("*V")) {
                            /*
                            Manage case with OS400 version as always true because jariko RPG version
                            is always the last one for definition
                             */
                            useRow = true
                        } else {
                            useRow = !isDefined(definitions, code)
                        }
                        ifLevel++
                    } else {
                        throw CompilerDirectivesException("IF_NOT_DEFINED directive without code value at line " + (index + 1))
                    }
                }

                DEFINE_PATTERN.matches(row) -> {
                    directiveRow = true
                    // Define directive is always acceptable
                    val matchResult = DEFINE_PATTERN.matchEntire(row)
                    val code = matchResult?.groups?.get(1)?.value
                    if (code != null) {
                        definitions.add(code.uppercase())
                    } else {
                        throw CompilerDirectivesException("DEFINE directive without code value at line " + (index + 1))
                    }
                }

                UNDEFINE_PATTERN.matches(row) -> {
                    directiveRow = true
                    // Undefine directive is always acceptable
                    val matchResult = UNDEFINE_PATTERN.matchEntire(row)
                    val code = matchResult?.groups?.get(1)?.value
                    if (code != null) {
                        definitions.remove(code.uppercase())
                    } else {
                        throw CompilerDirectivesException("UNDEFINE directive without code value at line " + (index + 1))
                    }
                }

                ELSE_PATTERN.matches(row) -> {
                    directiveRow = true
                    // Control if ELSE is acceptable
                    val acceptedLastCodes =
                        listOf(
                            SYN_RELEVANT_DIRECTIVES.IF_DEFINED,
                            SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED,
                            SYN_RELEVANT_DIRECTIVES.ENDIF
                        )
                    if (!acceptedLastCodes.any { it == lastCode }) {
                        throw CompilerDirectivesException("Unexpected ELSE directive at line " + (index + 1))
                    }
                    if (ifLevel == 0) {
                        throw CompilerDirectivesException("ELSE directive at line " + (index + 1) + " not assignable to a previous IF")
                    }

                    lastCode = SYN_RELEVANT_DIRECTIVES.ELSE
                    useRow = !useRow
                }

                ENDIF_PATTERN.matches(row) -> {
                    directiveRow = true
                    // Control if ENDIF is acceptable
                    val acceptedLastCodes = listOf(
                        SYN_RELEVANT_DIRECTIVES.IF_DEFINED,
                        SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED,
                        SYN_RELEVANT_DIRECTIVES.ELSE,
                        SYN_RELEVANT_DIRECTIVES.ENDIF
                    )
                    if (!acceptedLastCodes.any { it == lastCode }) {
                        throw CompilerDirectivesException("Unexpected ENDIF directive at line " + (index + 1))
                    }
                    if (ifLevel == 0) {
                        throw CompilerDirectivesException("ENDIF directive at line " + (index + 1) + " that not close a previous IF")
                    }

                    ifLevel--
                    lastCode = SYN_RELEVANT_DIRECTIVES.ENDIF
                    useRow = true
                }

                EOF_PATTERN.matches(row) -> {
                    directiveRow = true
                    eofInvoked = true
                    useRow = false
                }

                else -> {
                    directiveRow = false
                }
            }
        }

        if (directiveRow || eofInvoked) {
            // Comment directives row and all rows after an EOF directive
            result.appendLine(row.transformToComment())
        } else if (useRow) {
            result.appendLine(row)
        } else {
            // Transform row in comment because declared as unused after directive resolution
            result.appendLine(row.transformToComment())
        }
    }
    return result.toString()
}

private fun String.transformToComment(): String {
    val newString: String
    if (this.length >= 7) {
        newString = this.substring(0, 6) + '*' + this.substring(7)
    } else {
        val padding = "       "
        newString = this + padding.substring(0, 7 - this.length) + '*'
    }
    return newString
}

private fun containDirectives(inputString: String): Boolean {
    return inputString.contains(EOF_DIRECTIVE_KEYWORD, true) || DIRECTIVES_KEYWORDS.any { keyword -> inputString.contains(keyword, ignoreCase = true) }
}

private fun isDefined(definitions: MutableList<String>, code: String) =
    /**
     * Manage also "*VxRxMx" formats (return always true)
     */
    code.startsWith("*V") || definitions.any { it.equals(code.uppercase()) }

class CompilerDirectivesException(message: String) : Exception(message)
