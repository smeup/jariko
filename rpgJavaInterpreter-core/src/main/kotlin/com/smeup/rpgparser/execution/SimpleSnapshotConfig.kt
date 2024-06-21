package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.InterpreterCore
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

private val json = Json {
    prettyPrint = true
}

@Serializable
data class SimpleSnapshotConfig(var snapshotPath: String? = null) {

    fun save(snapshotFile: String, interpreter: InterpreterCore) {
        val file = File(snapshotPath, "$snapshotFile.json")

        file.bufferedWriter().use {
            it.write(json.encodeToString(interpreter.getGlobalSymbolTable()))
        }
    }
}