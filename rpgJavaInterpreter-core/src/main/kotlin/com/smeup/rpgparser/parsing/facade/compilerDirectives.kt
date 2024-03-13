package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.parsing.parsetreetoast.fireErrorEvent

/**
 * Directives that can be used only under certain conditions (for example, an ELSE must always follow an IF)."
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
val IF_DEFINED_PATTERN = Regex(""".{6}/IF\sDEFINED\(([\w£$§,]+)\)$""", RegexOption.IGNORE_CASE)
val IF_NOT_DEFINED_PATTERN = Regex(""".{6}/IF\sNOT\sDEFINED\(([\w£$§,]+)\)$""", RegexOption.IGNORE_CASE)
val DEFINE_PATTERN = Regex(""".{6}/DEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val UNDEFINE_PATTERN = Regex(""".{6}/UNDEFINE\s+([^\s]+)""", RegexOption.IGNORE_CASE)
val ELSE_PATTERN = Regex(""".{6}/ELSE""", RegexOption.IGNORE_CASE)
val ENDIF_PATTERN = Regex(""".{6}/ENDIF""", RegexOption.IGNORE_CASE)
val EOF_PATTERN = Regex(""".{6}/EOF""", RegexOption.IGNORE_CASE)

/**
 * Resolve the EOF directive: after this directive, all rows are ignored until the end of the file.
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
    var result = ""
    var useRow = true
    for (row in rows) {
        if (EOF_PATTERN.matches(row)) {
            useRow = false
            result += row.transformToComment().plus("\n")
        } else {
            if (useRow) {
                result += row.plus("\n")
            } else {
                result += row.transformToComment().plus("\n")
            }
        }
    }
    return result
}

internal fun String.resolveCompilerDirectives(): String {

    if (!containDirectives(this)) {
        return this
    }

    var lastCode: SYN_RELEVANT_DIRECTIVES = SYN_RELEVANT_DIRECTIVES.NONE
    var result = ""
    val definitions = mutableListOf<String>()
    var useRow = true
    var eofInvoked = false
    var directiveRow = false
    // Split input text into rows
    val rows = this.lines()

    // Check each row against regex patterns
    for ((index, row) in rows.withIndex()) {
        // if eof invoked manage all remaining rows as comments
        if (!eofInvoked) {
            when {
                IF_DEFINED_PATTERN.matches(row) -> {
                    directiveRow = true
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
                    } else {
                        val exc =
                            CompilerDirectivesException("IF_DEFINED directive without code value at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
                    }
                }

                IF_NOT_DEFINED_PATTERN.matches(row) -> {
                    directiveRow = true
                    // Control if IF_NOT_DEFINED is acceptable
                    val acceptedLastCodes = listOf(SYN_RELEVANT_DIRECTIVES.NONE, SYN_RELEVANT_DIRECTIVES.ENDIF)
                    if (!acceptedLastCodes.any { it == lastCode }) {
                        val exc =
                            CompilerDirectivesException("Unexpected IF_NOT_DEFINED directive at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
                    }

                    lastCode = SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED
                    val matchResult = IF_NOT_DEFINED_PATTERN.matchEntire(row)
                    val code = matchResult?.groups?.get(1)?.value
                    if (code != null) {
                        useRow = !isDefined(definitions, code)
                    } else {
                        val exc =
                            CompilerDirectivesException("IF_NOT_DEFINED directive without code value at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
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
                        val exc =
                            CompilerDirectivesException("DEFINE directive without code value at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
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
                        val exc =
                            CompilerDirectivesException("UNDEFINE directive without code value at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
                    }
                }

                ELSE_PATTERN.matches(row) -> {
                    directiveRow = true
                    // Control if ELSE is acceptable
                    val acceptedLastCodes =
                        listOf(SYN_RELEVANT_DIRECTIVES.IF_DEFINED, SYN_RELEVANT_DIRECTIVES.IF_NOT_DEFINED)
                    if (!acceptedLastCodes.any { it == lastCode }) {
                        val exc = CompilerDirectivesException("Unexpected ELSE directive at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
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
                        SYN_RELEVANT_DIRECTIVES.ELSE
                    )
                    if (!acceptedLastCodes.any { it == lastCode }) {
                        val exc = CompilerDirectivesException("Unexpected ENDIF directive at line " + (index + 1))
                        throw AstCreatingException(this, exc).fireErrorEvent(null)
                    }

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
            result += row.transformToComment().plus("\n")
        } else if (useRow) {
            result += row.plus("\n")
        } else {
            // Transform row in comment because declared as unused after directive resolution
            result += row.transformToComment().plus("\n")
        }
    }
    return result
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
