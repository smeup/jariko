package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile
import com.smeup.rpgparser.execution.MainExecutionContext
import java.util.*

class DBFileMap {
    private val byFileName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)
    private val byFormatName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)

    /**
     * Register a FileDefinition and create relative DBFile object for access to database with Reload library
     */
    fun add(fileDefinition: FileDefinition) {

        if (byFileName.containsKey(fileDefinition.name) == false) {

            //  Get DBFile from Reload using DBFileFactory registered in Context
            val dbFile = MainExecutionContext.getDBFileFactory()?.open(
                fileName = fileDefinition.name,
                fileMetadata = MainExecutionContext.getConfiguration().reloadConfig?.metadataProducer?.invoke(fileDefinition.name))

            dbFile?.let {
                // dbFile not null
                byFileName[fileDefinition.name] = dbFile
                var formatName = fileDefinition.internalFormatName
                if (formatName != null && !fileDefinition.name.equals(formatName, ignoreCase = true)) {
                    byFormatName[formatName] = dbFile
                } else {
                    formatName = dbFile.fileMetadata.recordFormat
                    byFormatName[formatName] = dbFile
                }
            }
        }
    }
    operator fun get(nameOrFormat: String): DBFile? = byFileName[nameOrFormat] ?: byFormatName[nameOrFormat]

    /**
     * Close all opened file
     */
    fun closeAll() {
        byFileName.forEach() {
            it.value.close()
        }
    }
}
