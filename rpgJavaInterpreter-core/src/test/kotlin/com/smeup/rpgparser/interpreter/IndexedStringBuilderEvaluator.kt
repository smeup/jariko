package com.smeup.rpgparser.interpreter

import java.math.RoundingMode
import kotlin.time.DurationUnit
import kotlin.time.measureTime

private fun stringBuilderVsDataStructStringValue(
    printHeader: Boolean = false,
    debugInformation: Boolean = false,
    stringSize: Int,
    fields: Int,
    iterations: Int
) {
    require(stringSize >= fields) { "stringSize must be greater than or equal to elements" }
    require(stringSize % fields == 0) { "stringSize: $stringSize must a multiple of fields: $fields" }

    val chunksSize = stringSize / fields

    if (printHeader) {
        println("stringSize, fields${if (debugInformation) ", chunksSize" else ""}, ratio(sb/indexed_sb), " +
                "ratio(sb/ds_string_value)${if (debugInformation) ", duration(ms)" else ""}")
    }

    print("$stringSize, $fields${if (debugInformation) ", $chunksSize" else ""}")

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

    val dataStructStringValueDuration = measureTime {
        val dataStructStringValue = DataStructStringValue.create("a".repeat(stringSize), fields)
        for (i in 0 until iterations) {
            val replacingChars = stringSize / fields
            for (j in 0 until fields) {
                val start: Int = j * replacingChars
                val end: Int = start + replacingChars
                dataStructStringValue.replace(start, end, replacingString)
            }
        }
    }

    val ratioSbIndexed = sbDuration.div(indexedSbDuration).toBigDecimal().setScale(2, RoundingMode.HALF_DOWN).toDouble()
    val ratioSbDataStructStringValue = sbDuration.div(dataStructStringValueDuration).toBigDecimal().setScale(2, RoundingMode.HALF_DOWN).toDouble()
    println(", $ratioSbIndexed, $ratioSbDataStructStringValue${if (debugInformation) ", ${sbDuration.plus(indexedSbDuration).toLong(DurationUnit.MILLISECONDS)}" else ""}")
}

private fun createPerformanceComparisonDataset() {
    val stringSizeTests = mutableListOf<Int>()
    val startStringSizeValue = 1000
    var value = startStringSizeValue
    while (value <= 100_000) {
        for (i in 1..9) {
            stringSizeTests.add(value * i)
        }
        value *= 10
    }

    for (stringSize in stringSizeTests) {
        for (fields in listOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000)) {
            if (stringSize < fields || ((stringSize % fields) != 0)) {
                break
            }
            stringBuilderVsDataStructStringValue(
                printHeader = stringSize == startStringSizeValue && fields == 1,
                stringSize = stringSize,
                fields = fields,
                iterations = if (stringSize * fields < 100_000) 1_000_000 else if (stringSize * fields < 1_000_000) 100_000 else 100
            )
        }
    }
}

fun main() {
    createPerformanceComparisonDataset()
}