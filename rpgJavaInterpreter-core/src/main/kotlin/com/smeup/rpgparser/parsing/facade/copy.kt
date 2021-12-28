package com.smeup.rpgparser.parsing.facade

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.PreprocessingLogEnd
import com.smeup.rpgparser.interpreter.PreprocessingLogStart
import com.smeup.rpgparser.parsing.ast.SourceProgram
import kotlinx.serialization.Serializable
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
    },
    includedCopy: (copyBlock: CopyBlock) -> Unit = { _: CopyBlock -> },
    currentLine: Int = 0
): String {
    val matcher = PATTERN.matcher(this)
    val sb = StringBuffer()
    var addedLines = 0
    while (matcher.find()) {
        // Skip commented line
        if (null != matcher.group(2)) continue
        val copyId = matcher.group(1).copyId()
        // println("Processing $copyId")
        kotlin.runCatching {
            val copyStartLine = this.substring(0, matcher.start(1)).lines().size + currentLine + addedLines
            val copy = (findCopy.invoke(copyId))?.includesCopy(
                findCopy = findCopy,
                includedCopy = includedCopy,
                currentLine = copyStartLine
            )?.surroundWithPreprocessingAnnotations(copyId)?.apply {
            } ?: let {
                println("Copy ${matcher.group()} not found".yellow())
                matcher.group()
            }
            val copyContent = copy.replace("$", "\\$")
            val copyEndLine = copyStartLine + copyContent.lines().size
            includedCopy.invoke(CopyBlock(copyId, copyStartLine, copyEndLine))
            addedLines += copyContent.lines().size - 1
            matcher.appendReplacement(sb, copyContent)
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

internal fun InputStream.preprocess(
    findCopy: (copyId: CopyId) -> String?,
    includedCopy: (copyBlock: CopyBlock) -> Unit = { _: CopyBlock -> }
): String {
    val programName = getExecutionProgramNameWithNoExtension()
    MainExecutionContext.log(PreprocessingLogStart(programName = programName))
    val preprocessed: String
    measureTimeMillis {
        preprocessed = bufferedReader().use(BufferedReader::readText).includesCopy(findCopy = findCopy, includedCopy = includedCopy)
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

@Serializable
data class CopyId(val library: String? = null, val file: String? = null, val member: String) {

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
 * Copy block. This object is used to identify a copy inside the main program.
 * @param copyId The copy identifier
 * @param start The first line of the copy (inclusive)
 * @param end The end line of the copy (exclusive)
 * */
@Serializable
data class CopyBlock(val copyId: CopyId, val start: Int, val end: Int) {

    /**
     * @return true if copyBlock passed by parameter is inside this
     * */
    fun contains(copyBlock: CopyBlock) = copyBlock.start > this.start && copyBlock.end < this.end
}

@Serializable
class CopyBlocks {
    private val copyBlocks = mutableListOf<CopyBlock>()

    fun add(copyBlock: CopyBlock) {
        copyBlocks.add(copyBlock)
    }

    internal fun getCopyBlockContaining(line: Int): CopyBlock? {
        return copyBlocks?.filter {
                copyBlock -> line >= copyBlock.start && line < copyBlock.end
        }?.minByOrNull { copyBlock -> copyBlock.end - copyBlock.start }
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
