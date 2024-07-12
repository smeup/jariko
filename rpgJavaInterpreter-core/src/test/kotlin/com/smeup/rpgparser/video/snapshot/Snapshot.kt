package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.MemorySliceId
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.Value

internal data class Snapshot(
    val stackTraces: MutableMap<String, StackTrace> = mutableMapOf(),
    val symbolTables: MutableMap<MemorySliceId, Map<String, Value>> = mutableMapOf()
) : RuntimeInterpreterSnapshot