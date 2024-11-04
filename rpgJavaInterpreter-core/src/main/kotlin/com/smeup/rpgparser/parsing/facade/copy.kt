/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing.facade

import com.andreapivetta.kolor.yellow
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.parsetreetoast.fireErrorEvent
import com.smeup.rpgparser.rpginterop.CopyFileExtension
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.BufferedReader
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern

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

internal fun String.includesCopy(
    findCopy: (copyId: CopyId) -> String? = {
        MainExecutionContext.getSystemInterface()!!.findCopy(it)!!.source
    },
    onStartInclusion: (copyId: CopyId, startLine: Int) -> Unit = { _: CopyId, _: Int -> },
    onEndInclusion: (endLine: Int) -> Unit = { _: Int -> },
    beforeInclusion: (copyId: CopyId) -> Boolean = { true },
    currentLine: Int = 0
): String {
    val matcher = PATTERN.matcher(this)
    val sb = StringBuffer()
    var addedLines = 0
    while (matcher.find()) {
        // Skip CDATA section
        if (null != matcher.group(3)) continue
        // Skip commented line
        if (null != matcher.group(2)) continue
        val copyId = matcher.group(1).copyId()
        if (beforeInclusion(copyId)) {
            // println("Processing $copyId")
            kotlin.runCatching {
                val copyStartLine = this.substring(0, matcher.start(1)).lines().size + currentLine + addedLines
                onStartInclusion.invoke(copyId, copyStartLine)
                val copy = (findCopy.invoke(copyId))?.includesCopy(
                    findCopy = findCopy,
                    onStartInclusion = onStartInclusion,
                    onEndInclusion = onEndInclusion,
                    beforeInclusion = beforeInclusion,
                    currentLine = copyStartLine
                )?.resolveEOFDirective()?.surroundWithPreprocessingAnnotations(copyId)?.apply {
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
                // TODO position of copy directive is not easy, for now I pass null
                throw AstCreatingException(this, it).fireErrorEvent(null)
            }
        }
    }
    matcher.appendTail(sb)
    return sb.toString()
}

/**
 *  Group 1: ID of Copy, like <folder>,<file> (without extension)
 *  Group 2: Commented line
 *  Group 3: CDATA section
 */
@JvmField
val PATTERN: Pattern = Pattern.compile("" +
        ".{6}/(?:COPY|INCLUDE)\\s+((?:\\w|£|\\\$|§|,)+)|" +
        "(.{6}\\*.+)|" +
        "(\\*\\*(CTDATA)?\\s?[§£#@a-zA-Z0-9_]*[\\n\\S\\s]*)",
    Pattern.CASE_INSENSITIVE
)

fun String.copyId(): CopyId {
    return when {
        this.contains('/') -> {
            this.split("/", ",").let {
                CopyId(it[0].uppercase(Locale.getDefault()), it[1].uppercase(Locale.getDefault()), it[2].uppercase(Locale.getDefault()))
            }
        }
        this.contains(",") -> {
            this.split(",").let {
                CopyId(null, it[0].uppercase(Locale.getDefault()), it[1].uppercase(Locale.getDefault()))
            }
        }
        else -> CopyId(null, null, this)
    }
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
fun CopyId.key(extension: CopyFileExtension): String {
    val key = this.file?.let {
        "$it/$member.$extension"
    } ?: "$member.$extension"
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
class CopyBlocks : Iterable<CopyBlock> {

    private val copyBlocks = mutableListOf<CopyBlock>()

    private val revertOrderedCopyBlocks = mutableListOf<CopyBlock>()

    @Transient
    private var blocksStack = Stack<CopyBlock>()

    private val firstLevelBlocks: List<CopyBlock> by lazy {
        val list = mutableListOf<CopyBlock>()
        copyBlocks.forEach { copyBlock -> if (list.none { it.contains(copyBlock) }) list.add(copyBlock) }
        list
    }

    /**
     * Returns an iterator over the elements of this object.
     */
    override fun iterator(): Iterator<CopyBlock> {
        return copyBlocks.iterator()
    }

    operator fun get(index: Int) = copyBlocks[index]

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
        revertOrderedCopyBlocks.add(copyBlock)
        revertOrderedCopyBlocks.sortBy { -it.start }
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
    fun relativeLine(absoluteLine: Int): RelativeLine {
        require(absoluteLine >= 0) { "absoluteLine must be greater than 0" }
        val copyBlock = getCopyBlock(absoluteLine)
        val relativeLine = copyBlock?.let { block ->
            getChildren(block).let { children ->
                if (children.isEmpty() || absoluteLine < children.first().start) {
                    absoluteLine - block.start
                } else {
                    val childrenBefore = children.filter { child -> child.end <= absoluteLine }
                    absoluteLine - block.start - childrenBefore.sumOf { childBefore -> childBefore.end - childBefore.start } + 1
                }
            }
            // else if line inside program
        } ?: (absoluteLine - (getCopyBlocksBefore(absoluteLine)
            .sumOf { it.end - it.start - 1 }))
        return RelativeLine(first = relativeLine, second = copyBlock)
    }

    /**
     * Allows observing the copy blocks transitions. Parameters from and to are inclusive.
     * @param from Start from absolute line
     * @param to End to absolute line
     * */
    fun observeTransitions(
        from: Int,
        to: Int,
        onEnter: (copyBlock: CopyBlock) -> Unit,
        onExit: (copyBlock: CopyBlock) -> Unit
    ) {

        if (from <= to) {
            for (line in from..to) {
                copyBlocks.forEach { copyBlock ->
                    if (line == copyBlock.start) {
                        onEnter.invoke(copyBlock)
                    }
                    if (line == copyBlock.end) {
                        onExit.invoke(copyBlock)
                    }
                }
            }
        } else {
            for (line in from downTo to) {
                revertOrderedCopyBlocks.forEach { copyBlock ->
                    if (line == copyBlock.end - 1) {
                        onEnter.invoke(copyBlock)
                    }
                    if (line == copyBlock.start - 1) {
                        onExit.invoke(copyBlock)
                    }
                }
            }
        }
    }

    private fun getChildren(copyBlock: CopyBlock) = copyBlocks.filter { it.parent == copyBlock }

    private fun getCopyBlocksBefore(line: Int) = firstLevelBlocks.filter { copyBlock -> copyBlock.end <= line }
}
