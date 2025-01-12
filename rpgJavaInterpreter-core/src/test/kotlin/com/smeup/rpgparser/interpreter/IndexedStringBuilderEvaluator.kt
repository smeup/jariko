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

package com.smeup.rpgparser.interpreter

import java.math.RoundingMode
import kotlin.time.DurationUnit
import kotlin.time.measureTime

/**
 * Measures and compares the performance of different string builders.
 *
 * @param printHeader Whether to print the header for the output.
 * @param debugInformation Whether to include debug information in the output.
 * @param stringSize The size of the string to be used in the test.
 * @param fields The number of fields to divide the string into.
 * @param iterations The number of iterations to run the test.
 * @throws IllegalArgumentException if stringSize is less than fields or if stringSize is not a multiple of fields.
 */
private fun stringBuilderVsDataStructStringBuilder(
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

    // Measure the duration for StringBuilder
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

    // Measure the duration for IndexedBuilderBuilder
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

    val ratioSbIndexed = sbDuration.div(indexedSbDuration).toBigDecimal().setScale(2, RoundingMode.HALF_DOWN).toDouble()
    println(", $ratioSbIndexed${if (debugInformation) ", ${sbDuration.plus(indexedSbDuration).toLong(DurationUnit.MILLISECONDS)}" else ""}")
}

/**
 * Creates a dataset for performance comparison by generating various string sizes and field combinations,
 * and then measures the performance of different string builders.
 */
private fun createPerformanceComparisonDataset() {
    val stringSizeTests = mutableListOf<Int>()
    val startStringSizeValue = 1000
    var value = startStringSizeValue

    // Generate string sizes in the pattern: 1000, 2000, ..., 9000, 10000, 20000, ..., 90000, 100000
    while (value <= 100_000) {
        for (i in 1..9) {
            stringSizeTests.add(value * i)
        }
        value *= 10
    }

    // Iterate over each generated string size
    for (stringSize in stringSizeTests) {
        // Test with different numbers of fields
        for (fields in listOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000)) {
            // Skip invalid combinations where stringSize is less than fields or not a multiple of fields
            if (stringSize < fields || ((stringSize % fields) != 0)) {
                break
            }
            // Measure and compare the performance of different string builders
            stringBuilderVsDataStructStringBuilder(
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