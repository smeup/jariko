package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.utils.removeLast

internal fun merge(linesSubstrings: MutableList<DSPFLineSubstrings>): DSPFLineSubstrings? {
    if (linesSubstrings.isEmpty()) return null

    var continuesAtAnyColumn: Boolean? = null
    var count = -1
    var line = StringBuilder()
    var conditions = StringBuilder()

    // line itself should not be trimmed because positions before 45 should be preserved
    linesSubstrings.forEach {
        if (it.continuationChar != null) {
            if (continuesAtAnyColumn == null) {
                count = it.count
                line.append(it.line.removeLast(it.continuationChar.toString()))
                conditions.append(it.condition)
            } else {
                if (continuesAtAnyColumn == true) {
                    line.append(it.line.trim().removeLast(it.continuationChar.toString()))
                }
                if (continuesAtAnyColumn == false) {
                    line.append(it.keywords.trim().removeLast(it.continuationChar.toString()))
                    conditions.append(it.condition)
                }
            }
            if (it.continuesAtAnyColumn()) continuesAtAnyColumn = true
            if (it.continuesAtColumn45()) continuesAtAnyColumn = false
        } else {
            if (continuesAtAnyColumn == true) line.append(it.line.trim())
            if (continuesAtAnyColumn == false) {
                line.append(it.keywords.trim())
                conditions.append(it.condition)
            }
        }
    }

    val lineSubstrings = DSPFLineSubstrings.from(count, line.toString())
    lineSubstrings.condition = conditions.toString()

    return lineSubstrings
}
