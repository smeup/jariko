package com.smeup.rpgparser.parsing.facade

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.PreprocessingLogEnd
import com.smeup.rpgparser.interpreter.PreprocessingLogStart
import com.smeup.rpgparser.parsing.ast.SourceProgram
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.BufferedReader
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

typealias RelativeLine = Pair<Int, CopyBlock?>

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
    onStartInclusion: (copyId: CopyId, startLine: Int) -> Unit = { _: CopyId, _: Int -> },
    onEndInclusion: (endLine: Int) -> Unit = { _: Int -> },
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
            onStartInclusion.invoke(copyId, copyStartLine)
            val copy = (findCopy.invoke(copyId))?.includesCopy(
                findCopy = findCopy,
                onStartInclusion = onStartInclusion,
                onEndInclusion = onEndInclusion,
                currentLine = copyStartLine
            )?.surroundWithPreprocessingAnnotations(copyId)?.apply {
            } ?: let {
                println("Copy ${matcher.group()} not found".yellow())
                matcher.group()
            }
            val copyContent = copy.replace("$", "\\$")
            val copyEndLine = copyStartLine + copyContent.lines().size
            onEndInclusion.invoke(copyEndLine)
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
    onStartInclusion: (copyId: CopyId, start: Int) -> Unit = { _: CopyId, _: Int -> },
    onEndInclusion: (end: Int) -> Unit = { _: Int -> }
): String {
    val programName = getExecutionProgramNameWithNoExtension()
    MainExecutionContext.log(PreprocessingLogStart(programName = programName))
    val preprocessed: String
    measureTimeMillis {
        preprocessed = bufferedReader().use(BufferedReader::readText).includesCopy(
            findCopy = findCopy,
            onStartInclusion = onStartInclusion,
            onEndInclusion = onEndInclusion
        )
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

/**
 * This class models a source fragment of post-processed program that contains a copy.
 * @param copyId The copy identifier
 * @param start Start line of fragment, the value is inclusive, and it is base 1
 * @param end The end line of fragment, value is exclusive
 * */
@Serializable
data class CopyBlock(val copyId: CopyId, val start: Int, var end: Int) {

    var parent: CopyBlock? = null

    internal fun contains(copyBlock: CopyBlock) = copyBlock.start > this.start && copyBlock.end < this.end

    internal fun ascendants(): List<CopyBlock> {
        return mutableListOf<CopyBlock>().let {
            var current: CopyBlock? = this.parent
            while (current != null) {
                it.add(current)
                current = current.parent
            }
            it
        }
    }
}

/**
 * Collection of copy blocks.
 *
 * */
@Serializable
class CopyBlocks {

    private val copyBlocks = mutableListOf<CopyBlock>()

    @Transient
    private var blocksStack = Stack<CopyBlock>()

    private val firstLevelBlocks: List<CopyBlock> by lazy {
        val list = mutableListOf<CopyBlock>()
        copyBlocks.forEach { copyBlock -> if (list.none { it.contains(copyBlock) }) list.add(copyBlock) }
        list
    }

    internal fun onStartCopyBlock(copyId: CopyId, start: Int) {
        val copyBlock = CopyBlock(copyId, start = start, end = 0)
        if (!blocksStack.empty()) {
            copyBlock.parent = blocksStack.peek()
        }
        blocksStack.push(copyBlock)
    }

    internal fun onEndCopyBlock(end: Int) {
        val copyBlock = blocksStack.pop()
        copyBlock.end = end
        copyBlocks.add(copyBlock)
        copyBlocks.sortBy { it.start }
    }

    /**
     * Get a copy block instance that contains a line
     * @param absoluteLine is the line position of post-processed program (base 1)
     * @return An instance of copy block containing line
     * */
    internal fun getCopyBlock(absoluteLine: Int): CopyBlock? {
        return copyBlocks.filter {
                copyBlock -> absoluteLine >= copyBlock.start && absoluteLine < copyBlock.end
            // capture smallest range (inner copy)
        }.minByOrNull { copyBlock ->
            copyBlock.end - copyBlock.start
        }
    }

    /**
     * Transforms absolute line in relative line.
     * Relative line is the line position in the coordinates of source
     * reference while absolute line is the line positions in the coordinates of post-processed program.
     * For example, if we have a program containing one copy, in the post-processed program we have two fragments:
     * one fragment is the main program (it is an approximation but meaningful)
     * and the other one is the copy.
     * @param absoluteLine Line in the post-processed program coordinates
     * @return relative line
     * */
    internal fun relativeLine(absoluteLine: Int): RelativeLine {
        require(absoluteLine >= 0) { "absoluteLine must be greater than 0" }
        val copyBlock = getCopyBlock(absoluteLine)
        val relativeLine = copyBlock?.let { block ->
            getChildren(block).let { children ->
                if (children.isEmpty() || absoluteLine < children.first().start) {
                    absoluteLine - block.start
                } else {
                    val childrenBefore = children.filter { child -> child.end <= absoluteLine }
                    absoluteLine - block.start - childrenBefore.sumBy { childBefore -> childBefore.end - childBefore.start } + 1
                }
            }
            // else if line inside program
        } ?: (absoluteLine - (getCopyBlocksBefore(absoluteLine)
            .sumOf { it.end - it.start - 1 }))
        return RelativeLine(first = relativeLine, second = copyBlock)
    }

    private fun getChildren(copyBlock: CopyBlock) = copyBlocks.filter { it.parent == copyBlock }

    private fun getDescendants(copyBlock: CopyBlock): List<CopyBlock> {
        return mutableListOf<CopyBlock>().let { descendants ->
            getChildren(copyBlock = copyBlock).forEach { child ->
                descendants.add(child)
                descendants.addAll(getDescendants(child))
            }
            descendants
        }
    }

    private fun getCopyBlocksBefore(line: Int) = firstLevelBlocks.filter { copyBlock -> copyBlock.end <= line }
}
