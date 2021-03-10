package com.smeup.rpgparser.parsing.facade

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.SourceProgram
import java.io.BufferedReader
import java.io.InputStream
import java.util.regex.Pattern

class Copy(val source: String) {

    companion object {
        fun fromInputStream(inputStream: InputStream): Copy {
            inputStream.use {
                return Copy(it.bufferedReader().use(BufferedReader::readText))
            }
        }
    }
}

private fun String.includesCopy(
    findCopy: (copyId: CopyId) -> String? = {
        MainExecutionContext.getSystemInterface()!!.findCopy(it)!!.source
    }
): String {
    val matcher = PATTERN.matcher(this)
    val sb = StringBuffer()
    while (matcher.find()) {
        val copyId = matcher.group(1).copyId()
        // println("Processing $copyId")
        val copy = (findCopy.invoke(copyId))?.includesCopy(findCopy)?.let { it.surroundWithPreprocessingAnnotations(copyId) } ?: let {
            println("Copy ${matcher.group()} not found".yellow())
            matcher.group()
        }
        kotlin.runCatching {
            matcher.appendReplacement(sb, copy.replace("$", "\\$"))
        }.onFailure {
            println("Error on inclusion copy: $copy")
            throw it
        }
    }
    matcher.appendTail(sb)
    return sb.toString()
}

private val PATTERN = Pattern.compile("\\s{5}(?:H|I|\\s)/(?:COPY|INCLUDE)\\s+((?:\\w|£|\\$|,)+)", Pattern.CASE_INSENSITIVE)

private fun String.copyId(): CopyId {
    return when {
        this.contains('/') -> {
            this.split("/,").let {
                CopyId(it[0], it[1], it[2])
            }
        }
        this.contains(",") -> {
            this.split(",").let {
                CopyId(null, it[0], it[1])
            }
        }
        else -> CopyId(null, null, this)
    }
}

fun InputStream.preprocess(findCopy: (copyId: CopyId) -> String?): String {
    return bufferedReader().use(BufferedReader::readText).includesCopy(findCopy)
}

data class CopyId(val library: String?, val file: String?, val member: String)

/**
 * Get key related receiver, format is as follows:
 * library/file/member.ext
 * Since library and file are not mandatory returned key could be:
 * library/member.ext
 * file/member.ext
 * member.ext
 * */
fun CopyId.key(sourceProgram: SourceProgram): String {
    val key = this.file?.let {
        "$it/$member.${sourceProgram.extension}"
    } ?: "$member.${sourceProgram.extension}"
    return library?.let {
        "$library/$key"
    } ?: "$key"
}

private fun String.surroundWithPreprocessingAnnotations(copyId: CopyId): String {
    val pref = "*".repeat(10) + " PREPROCESSOR COPYSTART ${copyId}\n"
    val body = if (this.endsWith("\n")) {
        this
    } else {
        "${this}\n"
    }
    val suff = "*".repeat(10) + " PREPROCESSOR COPYEND $copyId"
    return "$pref$body$suff"
}
