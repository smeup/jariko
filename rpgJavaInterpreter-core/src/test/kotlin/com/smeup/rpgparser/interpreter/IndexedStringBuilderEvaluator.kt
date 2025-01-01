package com.smeup.rpgparser.interpreter

import java.math.RoundingMode
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime

private fun compareStringBuilderAndIndexedStringBuilderPerformance(
    printHeader: Boolean = false,
    debugInformation: Boolean = false,
    stringSize: Int,
    fields: Int,
    chunksSize: Int,
    iterations: Int,
    stringBuilderDone: (duration: Duration) -> Unit,
    indexedStringBuilderDone: (duration: Duration) -> Unit
) {
    require(stringSize >= fields) { "stringSize must be greater than or equal to elements" }
    require(stringSize % fields == 0) { "stringSize: $stringSize must a multiple of fields: $fields" }

    if (printHeader) {
        println("stringSize, ${if (debugInformation) "fields, " else "" }chunksSize, ratio${if (debugInformation) ", duration(ms)" else "" }")
    }

    print("$stringSize, ${if (debugInformation) "$fields, " else ""}$chunksSize")

    val replacingString = "b".repeat(stringSize / fields)
    val sbDuration = measureTime {
        val sb = StringBuilder("a".repeat(stringSize))
        for (i in 0 until iterations) {
            val replacingChars = stringSize / fields
            for (j in 0 until fields) {
                val start: Int = j * replacingChars
                val end: Int = start + replacingChars
                sb.replace(start, end, replacingString)
            }
        }
    }
    stringBuilderDone(sbDuration)

    val indexedSbDuration = measureTime {
        val indexedSb = IndexedStringBuilder("a".repeat(stringSize), chunksSize)
        for (i in 0 until iterations) {
            val replacingChars = stringSize / fields
            for (j in 0 until fields) {
                val start: Int = j * replacingChars
                val end: Int = start + replacingChars
                indexedSb.replace(start, end, replacingString)
            }
        }
    }

    indexedStringBuilderDone(indexedSbDuration)

    val ratio = sbDuration.div(indexedSbDuration).toBigDecimal().setScale(2, RoundingMode.HALF_DOWN).toDouble()

    println(", $ratio${if (debugInformation) ", ${sbDuration.plus(indexedSbDuration).toLong(DurationUnit.MILLISECONDS)}" else ""}")
}

private fun createPerformanceComparisonDataset(chunksSize: (stringSize: Int, fields: Int) -> Int = { stringSize, fields -> stringSize / fields }) {
    for (stringSize in listOf(10, 100, 500, 1000, 2_000, 5_000, 10_000, 50_000, 100_000, 1_000_000, 10_000_000)) {
        for (fields in listOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000)) {
            if (stringSize < fields || ((stringSize % fields) != 0)) {
                break
            }
            compareStringBuilderAndIndexedStringBuilderPerformance(
                printHeader = stringSize == 10 && fields == 1,
                stringSize = stringSize,
                fields = fields,
                chunksSize = chunksSize(stringSize, fields),
                iterations = if (stringSize*fields < 100_000) 1_000_000 else if (stringSize*fields < 1_000_000) 100_000 else 100,
                stringBuilderDone = { },
                indexedStringBuilderDone = { }
            )
        }
    }
}

fun main() {
    createPerformanceComparisonDataset()
}