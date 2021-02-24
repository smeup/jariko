package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile
import com.smeup.dbnative.file.Record
import com.smeup.dbnative.file.Result
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.Expression
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
            val jarikoMetadata = MainExecutionContext.getConfiguration().reloadConfig?.metadataProducer?.invoke(fileDefinition.name)
            require(jarikoMetadata != null)
            val dbFile = MainExecutionContext.getDBFileFactory()?.open(
                fileName = fileDefinition.name,
                fileMetadata = jarikoMetadata.toReloadMetadata()
            )

            dbFile?.let {
                val dbFile = EnrichedDBFile(it, fileDefinition, jarikoMetadata)
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
data class EnrichedDBFile(private val dbFile: DBFile, private val fileDefinition: FileDefinition, val jarikoMetadata: FileMetadata) : DBFile {

    override var fileMetadata = dbFile.fileMetadata

    override var name = dbFile.name

    override var logger = MainExecutionContext.getConfiguration().reloadConfig?.nativeAccessConfig?.logger

    override fun chain(key: String) = dbFile.chain(key).validate()

    override fun chain(keys: List<String>) = dbFile.chain(keys).validate()

    override fun delete(record: Record) = dbFile.delete(record).validate()

    override fun eof() = dbFile.eof()

    override fun equal() = dbFile.equal()

    override fun read() = dbFile.read().validate()

    override fun readEqual() = dbFile.readEqual().validate()

    override fun readEqual(key: String) = dbFile.readEqual(key).validate()

    override fun readEqual(keys: List<String>) = dbFile.readEqual(keys).validate()

    override fun readPrevious() = dbFile.readPrevious().validate()

    override fun readPreviousEqual() = dbFile.readPreviousEqual().validate()

    override fun readPreviousEqual(key: String) = dbFile.readPreviousEqual(key).validate()

    override fun readPreviousEqual(keys: List<String>) = dbFile.readPreviousEqual(keys).validate()

    override fun setgt(key: String) = dbFile.setgt(key)

    override fun setgt(keys: List<String>) = dbFile.setgt(keys)

    override fun setll(key: String) = dbFile.setll(key)

    override fun setll(keys: List<String>) = dbFile.setll(keys)

    override fun update(record: Record) = dbFile.update(record).validate()

    override fun write(record: Record) = dbFile.write(record).validate()

    fun getDataDefinitionName(dbFieldName: String) = fileDefinition.getDataDefinitionName(dbFieldName)
}

/**
 * Converts a value in string as required by reload, type currently is used in HyVal LowVal conversion
 * */
fun Value.asString(type: Type): String {
    return if (this is HiValValue || this is LowValValue) {
        coerce(this, type).asString().value
    } else {
        this.asString().value
    }
}

/**
 * Creates a keyList as required by reload
 * */
fun Expression.createKList(fileMetadata: FileMetadata, interpreter: InterpreterCore): List<String> {
    return if (type() is KListType) {
        interpreter.toSearchValues(this, fileMetadata)
    } else {
        val value = interpreter.eval(this)
        listOf(value.asString(fileMetadata.accessFieldsType[0]))
    }
}

/**
 * Validate the result. For now do nothing
 * */
fun Result.validate(): Result {
    return apply {
//        if (record.isEmpty()) {
//            require(indicatorEQ || indicatorHI || indicatorLO) {
//                "record is empty bot no flag is on"
//            }
//        }
    }
}
