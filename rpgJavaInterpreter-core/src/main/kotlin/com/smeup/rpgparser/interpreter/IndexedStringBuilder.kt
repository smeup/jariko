package com.smeup.rpgparser.interpreter

/**
 * Creates a string builder that allows to replace substrings in a more efficient way.
 * The string is divided into chunks of a fixed size.
 * When replacing a substring, only the affected chunks are modified.
 * This is useful when the string is very large and only a small part of it is modified.
 * @param value The initial value of the string builder
 * @param chunksSize The size of the chunks
 */
class IndexedStringBuilder(value: String, private val chunksSize: Int) {

    // The string is divided into chunks of a fixed size
    private val chunks: List<StringBuilder> = List((value.length + chunksSize - 1) / chunksSize) { index ->
        StringBuilder(value.substring(index * chunksSize, minOf((index + 1) * chunksSize, value.length)))
    }

    /***
     * Replace the substring from start to end with the replacing string.
     * The length of the replacing string must be equal to the length of the replaced string.
     * @param start The start index of the substring to replace
     * @param end The end index of the substring to replace
     */
    fun replace(start: Int, end: Int, replacingString: String) {
        require(end > start) { "End index must be greater than start index." }
        require(replacingString.length == end - start) { "Replacing string length must match the length of the substring being replaced." }

        val firstChunkIndex = start / chunksSize
        val lastChunkIndex = (end / chunksSize).let { if (it >= chunks.size) it - 1 else it }
        val subChunks = chunks.subList(firstChunkIndex, lastChunkIndex + 1)
        var remaining = replacingString
        var currentIndex = firstChunkIndex * chunksSize

        for (chunk in subChunks) {
            val chunkStart = currentIndex
            val chunkEnd = currentIndex + chunk.length
            if (chunkEnd > end) {
                break
            }
            if (start < chunkEnd && end > chunkStart) {
                val relativeStart = maxOf(0, start - chunkStart)
                val relativeEnd = minOf(chunk.length, end - chunkStart)
                val replaceStart = maxOf(0, start - chunkStart - relativeStart)
                val replaceEnd = replaceStart + (relativeEnd - relativeStart)

                chunk.replace(relativeStart, relativeEnd, remaining.substring(replaceStart, replaceEnd))

                remaining = remaining.substring(replaceEnd - replaceStart)
            }

            currentIndex += chunk.length
        }
    }

    /***
     * Returns the substring from start to end.
     * @param start The start index of the substring
     * @param end The end index of the substring
     * @return The substring from start to end
     */
    fun substring(start: Int, end: Int): String {
        require(end >= start) { "End index must be greater than or equal to start index." }

        if (start == end) return ""

        val result = StringBuilder()
        var currentIndex = 0
        for (chunk in chunks) {
            val chunkStart = currentIndex
            val chunkEnd = currentIndex + chunk.length

            if (start < chunkEnd && end > chunkStart) {
                val relativeStart = maxOf(0, start - chunkStart)
                val relativeEnd = minOf(chunk.length, end - chunkStart)
                result.append(chunk.substring(relativeStart, relativeEnd))
            }

            currentIndex += chunk.length
        }

        return result.toString()
    }

    override fun toString(): String {
        return chunks.joinToString(separator = "") { it.toString() }
    }
}