package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile
import com.smeup.dbnative.file.Record
import com.smeup.rpgparser.execution.MainExecutionContext
import java.util.*

class DBFileMap {
    private val byFileName =
        TreeMap<String, EnrichedDBFile>(String.CASE_INSENSITIVE_ORDER)
    private val byFormatName =
        TreeMap<String, EnrichedDBFile>(String.CASE_INSENSITIVE_ORDER)

    /**
     * Register a FileDefinition and create relative DBFile object for access to database with Reload library
     */
    fun add(fileDefinition: FileDefinition) {

        if (!byFileName.containsKey(fileDefinition.name)) {

            val dbFile = MainExecutionContext.getDBFileFactory()?.open(
                fileName = fileDefinition.name,
                fileMetadata = MainExecutionContext.getConfiguration().reloadConfig?.metadataProducer?.invoke(fileDefinition.name)?.toReloadMetadata()
            )

            dbFile?.let {
                val dbFile = EnrichedDBFile(it, fileDefinition)
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
    operator fun get(nameOrFormat: String): EnrichedDBFile? = byFileName[nameOrFormat] ?: byFormatName[nameOrFormat]
}

/**
 * DBFile wrapper needed to add further information to DBFile
 * */
data class EnrichedDBFile(private val dbFile: DBFile, private val fileDefinition: FileDefinition) : DBFile {

    override var fileMetadata = dbFile.fileMetadata

    override var name = dbFile.name

    override fun chain(key: String) = dbFile.chain(key)

    override fun chain(keys: List<String>) = dbFile.chain(keys)

    override fun delete(record: Record) = dbFile.delete(record)

    override fun eof() = dbFile.eof()

    override fun equal() = dbFile.equal()

    override fun read() = dbFile.read()

    override fun readEqual() = dbFile.readEqual()

    override fun readEqual(key: String) = dbFile.readEqual(key)

    override fun readEqual(keys: List<String>) = dbFile.readEqual(keys)

    override fun readPrevious() = dbFile.readPrevious()

    override fun readPreviousEqual() = dbFile.readPreviousEqual()

    override fun readPreviousEqual(key: String) = dbFile.readPreviousEqual(key)

    override fun readPreviousEqual(keys: List<String>) = dbFile.readPreviousEqual(keys)

    override fun setgt(key: String) = dbFile.setgt(key)

    override fun setgt(keys: List<String>) = dbFile.setgt(keys)

    override fun setll(key: String) = dbFile.setll(key)

    override fun setll(keys: List<String>) = dbFile.setll(keys)

    override fun update(record: Record) = dbFile.update(record)

    override fun write(record: Record) = dbFile.write(record)

    fun getDataDefinitionName(dbFieldName: String) = fileDefinition.getDataDefinitionName(dbFieldName)
}
