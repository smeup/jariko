package com.smeup.rpgparser.parsing.facade

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.PreprocessingLogEnd
import com.smeup.rpgparser.interpreter.PreprocessingLogStart
import com.smeup.rpgparser.parsing.ast.SourceProgram
import java.io.BufferedReader
import java.io.InputStream
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

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
        // Skip commented line
        if (null != matcher.group(2)) continue
        val copyId = matcher.group(1).copyId()
        // println("Processing $copyId")
        kotlin.runCatching {
            val copy = (findCopy.invoke(copyId))?.includesCopy(findCopy)?.surroundWithPreprocessingAnnotations(copyId) ?: let {
                println("Copy ${matcher.group()} not found".yellow())
                matcher.group()
            }
            matcher.appendReplacement(sb, copy.replace("$", "\\$"))
        }.onFailure {
            throw IllegalStateException("Error on inclusion copy: ${matcher.group(0)}\nsource:\n$this", it)
        }
    }
    matcher.appendTail(sb)
    return sb.toString()
}

private val PATTERN = Pattern.compile("(?:.{4}\\s(?:H|I|\\s)/(?:COPY|INCLUDE)\\s+((?:\\w|£|\\$|,)+))|(.{6}\\*.+)", Pattern.CASE_INSENSITIVE)
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
    val programName = getExecutionProgramNameWithNoExtension()
    MainExecutionContext.log(PreprocessingLogStart(programName = programName))
    val preprocessed: String
    measureTimeMillis {
        preprocessed = bufferedReader().use(BufferedReader::readText).includesCopy(findCopy)
    }.apply {
        MainExecutionContext.log(
            PreprocessingLogEnd(
                programName = programName,
                elapsed = this,
                programSouce = preprocessed
            )
        )
    }
    return preprocessed
}

data class CopyId(val library: String?, val file: String?, val member: String) {

    override fun toString(): String {
        return StringBuffer().apply {
            if (library != null) {
                append("$library/")
            }
            if (file != null) {
                append("$file,")
            }
            append(member)
        }.toString()
    }
}

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
    } ?: key
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
