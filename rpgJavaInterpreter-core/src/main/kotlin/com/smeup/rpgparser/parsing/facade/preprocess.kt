package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import java.io.BufferedReader
import java.io.InputStream
import kotlin.system.measureTimeMillis

fun InputStream.preprocess(
    findCopy: (copyId: CopyId) -> String?,
    onStartInclusion: (copyId: CopyId, start: Int) -> Unit = { _: CopyId, _: Int -> },
    onEndInclusion: (end: Int) -> Unit = { _: Int -> },
    beforeInclusion: (copyId: CopyId) -> Boolean = { true }
): String {
    val programName = getExecutionProgramNameWithNoExtension()
    val logSource = LogSourceData(programName, "")
    MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "PREPROP", "START"))
    var preprocessed: String
    measureTimeMillis {
        preprocessed = bufferedReader().use(BufferedReader::readText).includesCopy(
            findCopy = findCopy,
            onStartInclusion = onStartInclusion,
            onEndInclusion = onEndInclusion,
            beforeInclusion = beforeInclusion
        ).resolveCompilerDirectives()
    }.apply {
        val endLogSource = logSource.projectLine(preprocessed.lines().size.toString())
        MainExecutionContext.log(LazyLogEntry.produceStatement(endLogSource, "PREPROP", "END"))
        MainExecutionContext.log(LazyLogEntry.producePerformance(endLogSource, "PREPROP", this))
    }
    return preprocessed
}