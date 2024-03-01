package com.smeup.rpgparser.parsing.facade

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.PreprocessingLogEnd
import com.smeup.rpgparser.interpreter.PreprocessingLogStart
import java.io.BufferedReader
import java.io.InputStream
import kotlin.system.measureTimeMillis

internal fun InputStream.preprocess(
    findCopy: (copyId: CopyId) -> String?,
    onStartInclusion: (copyId: CopyId, start: Int) -> Unit = { _: CopyId, _: Int -> },
    onEndInclusion: (end: Int) -> Unit = { _: Int -> },
    beforeInclusion: (copyId: CopyId) -> Boolean = { true },
    resolveDirectives: Boolean = true
): String {
    val programName = getExecutionProgramNameWithNoExtension()
    MainExecutionContext.log(PreprocessingLogStart(programName = programName))
    var preprocessed: String
    measureTimeMillis {
        preprocessed = bufferedReader().use(BufferedReader::readText).includesCopy(
            findCopy = findCopy,
            onStartInclusion = onStartInclusion,
            onEndInclusion = onEndInclusion,
            beforeInclusion = beforeInclusion
        )
        if (resolveDirectives) {
            preprocessed = preprocessed.resolveCompilerDirectives()
        }
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