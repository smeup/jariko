package com.smeup.rpgparser.execution

import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.dspfparser.domain.DisplayFileParser
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.getDbFields
import kotlinx.serialization.Serializable
import java.io.File

/**
 * Helper class used to load dspf configuration from a display file.
 * @param displayFilePath path where display file is located
 * */
@Serializable
data class SimpleDspfConfig(var displayFilePath: String? = null) {

    fun dspfProducer(displayFile: String): DSPF {
        val videoFile = File(displayFilePath, "$displayFile.dspf")
        require(videoFile.exists()) { "$videoFile doesn't exist" }

        return videoFile.bufferedReader().use { reader ->
            DisplayFileParser.parse(reader)
        }
    }

    fun getMetadata(displayFile: String): FileMetadata {
        val videoFile = File(displayFilePath, "$displayFile.dspf")
        require(videoFile.exists()) { "$videoFile doesn't exist" }

        return videoFile.bufferedReader().use { reader ->
            val dspf = DisplayFileParser.parse(reader)
            val fields = dspf.getDbFields()

            FileMetadata(
                name = displayFile,
                tableName = "",
                recordFormat = "",
                fields = fields,
                accessFields = emptyList()
            )
        }
    }
}