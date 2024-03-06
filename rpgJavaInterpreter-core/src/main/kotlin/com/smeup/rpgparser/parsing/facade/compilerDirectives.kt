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
fun String.resolveEOFDirective(): String {
    if (!this.contains(EOF_DIRECTIVE_KEYWORD, true)) {
        return this
    }
    // Split input text into rows
    val rows = this.lines()
    var result = ""
    for (row in rows) {
        if (EOF_PATTERN.matches(row)) {
            // Break loop if EOF found
            break
        } else {
            if (row.length > 0) {
                result += row.plus("\n")
            }
        }
    }
    return result
}

fun String.resolveCompilerDirectives(): String {

    if (!containDirectives(this)) {
        return this
    }

    var lastCode: SYN_RELEVANT_DIRECTIVES = SYN_RELEVANT_DIRECTIVES.NONE
    var result = ""
    val definitions = mutableListOf<String>()
    var useRow = true
    var eofInvoked = false
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
                // Define directive is always acceptable
                val matchResult = DEFINE_PATTERN.matchEntire(row)
                val code = matchResult?.groups?.get(1)?.value
                if (code != null) {
                    definitions.add(code.uppercase())
                }
            }
            UNDEFINE_PATTERN.matches(row) -> {
                // Undefine directive is always acceptable
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
            EOF_PATTERN.matches(row) -> {
                eofInvoked = true
                useRow = false
            }
            else -> {
                if (useRow && row.length > 0) {
                    result += row.plus("\n")
                }
            }
        }
        // Exit loop if EOF invoked
        if (eofInvoked) break
    }
    return result
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
