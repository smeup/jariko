package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import com.smeup.rpgparser.logging.ProgramUsageType
import java.io.BufferedReader
import java.io.InputStream
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

fun InputStream.preprocess(
    findCopy: (copyId: CopyId) -> String?,
    onStartInclusion: (copyId: CopyId, start: Int) -> Unit = { _: CopyId, _: Int -> },
    onEndInclusion: (end: Int) -> Unit = { _: Int -> },
    beforeInclusion: (copyId: CopyId) -> Boolean = { true }
): String {
    val programName = getExecutionProgramNameWithNoExtension()
    MainExecutionContext.log(LazyLogEntry.produceStatement({ LogSourceData(programName, "") }, "PREPROP", "START"))
    var preprocessed: String
    measureNanoTime {
        preprocessed = bufferedReader().use(BufferedReader::readText).includesCopy(
            findCopy = findCopy,
            onStartInclusion = onStartInclusion,
            onEndInclusion = onEndInclusion,
            beforeInclusion = beforeInclusion
        ).resolveCompilerDirectives()
    }.apply {
        val endLogSource = { LogSourceData(programName, preprocessed.lines().size.toString()) }
        MainExecutionContext.log(LazyLogEntry.produceStatement(endLogSource, "PREPROP", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformanceAndUpdateAnalytics(endLogSource, ProgramUsageType.Parsing, "PREPROP", this.nanoseconds))
    }
    return preprocessed
}