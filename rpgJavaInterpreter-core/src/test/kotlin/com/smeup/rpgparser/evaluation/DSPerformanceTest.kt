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

package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.interpreter.StringBuilderWrapper
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test
import org.junit.experimental.categories.Category
import java.time.Duration
import java.util.*
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

open class DSPerformanceTest : AbstractTest() {
    @Test
    @Category(PerformanceTest::class)
    fun executeDSPERF01() {
        var performanceRatio = 0.0
        val regex = Regex(pattern = "PERFORMANCE RATIO: ((?:\\d+)?\\.\\d+)")
        val systemInterface =
            JavaSystemInterface().apply {
                onDisplay = { message, _ ->
                    println(message.trim())
                    regex.matchEntire(message.trim())?.let { mathResult ->
                        mathResult.groups[1]?.value?.let { value ->
                            performanceRatio = value.toDouble()
                        }
                    }
                }
            }
        executePgm(programName = "DSPERF01", systemInterface = systemInterface)
        require(performanceRatio != 0.0) { "performanceRatio must be initialized" }
        assertTrue(
            performanceRatio > 10,
            "performanceRatio must be at least 10",
        )
    }

    @Test
    @Category(PerformanceTest::class)
    fun executeDSPERF02() {
        lateinit var start: Date
        lateinit var end: Date
        val configuration =
            Configuration().apply {
                jarikoCallback.onEnterPgm = { _, _ ->
                    start = Date()
                }
                jarikoCallback.onExitPgm = { _, _, _ ->
                    end = Date()
                }
            }
        "DSPERF02".outputOf(configuration = configuration)
        val duration = Duration.between(start.toInstant(), end.toInstant()).toMillis().milliseconds
        println("executeDSPERF02 with default useIndexedStringBuilder: $duration ms")
        // Currently the assertion is really empirical
        assertTrue(duration.toLong(DurationUnit.SECONDS) < 2, "Duration must be less than 2 second")
    }

    @Test
    @Category(PerformanceTest::class)
    fun executeDSPERF02CompareIndexedStringBuilderVsStringBuilder() {
        var useIndexedStringBuilder = false
        val start = mutableMapOf<Boolean, Date>()
        val end = mutableMapOf<Boolean, Date>()
        val createDataStructValueBuilderDefaultImpl = Configuration().jarikoCallback.createDataStructValueBuilder
        val configuration =
            Configuration().apply {
                jarikoCallback.onEnterPgm = { _, _ ->
                    start[useIndexedStringBuilder] = Date()
                }
                jarikoCallback.onExitPgm = { _, _, _ ->
                    end[useIndexedStringBuilder] = Date()
                }
                jarikoCallback.createDataStructValueBuilder = { value, type ->
                    if (useIndexedStringBuilder) {
                        createDataStructValueBuilderDefaultImpl(value, type)
                    } else {
                        StringBuilderWrapper(value)
                    }
                }
            }
        "DSPERF02".outputOf(configuration = configuration)
        val durationNotUseIndexedStringBuilder =
            Duration
                .between(
                    start[false]!!.toInstant(),
                    end[false]!!.toInstant(),
                ).toMillis()
                .milliseconds
        println("executeDSPERF02 with useIndexedStringBuilder=false: $durationNotUseIndexedStringBuilder ms")
        useIndexedStringBuilder = true
        "DSPERF02".outputOf(configuration = configuration)
        val durationUseIndexedStringBuilder = Duration.between(start[true]!!.toInstant(), end[true]!!.toInstant()).toMillis().milliseconds
        println("executeDSPERF02 with useIndexedStringBuilder=true: $durationUseIndexedStringBuilder ms")
        assertTrue(
            durationNotUseIndexedStringBuilder / durationUseIndexedStringBuilder >= 10,
            "Duration with useIndexedStringBuilder=false must be at least 10 times greater than duration with useIndexedStringBuilder=true",
        )
    }
}
