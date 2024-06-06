package com.smeup.rpgparser.execution

import com.smeup.dspfparser.domain.DisplayFileParser
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.getDbFields
import kotlinx.serialization.Serializable
import java.io.File

/**
 * Helper class used to load dspf configuration from a display file
 * @param displayFilePath path where display file is located
 * */
@Serializable
internal data class SimpleDspfConfig(var displayFilePath: String? = null) {

    fun getMetadata(displayFile: String): FileMetadata {
        val videoFile = File(displayFilePath, "$displayFile.dspf")
        require(videoFile.exists()) { "$videoFile doesn't exist" }
        val dspf = DisplayFileParser.parse(videoFile.bufferedReader())
        val fields = dspf.getDbFields()

        return FileMetadata(
            name = displayFile,
            tableName = "",
            recordFormat = "",
            fields = fields,
            accessFields = emptyList()
        )
    }
}