/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private val byInternalFormatName =
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
                val enrichedDBFile = EnrichedDBFile(it, fileDefinition, jarikoMetadata)
                // dbFile not null
                // I consider fileDefinition.name, fileDefinition.internalFormatName and jarikoMetadata.recordFormat as alias of fileDefinition.name
                byFileName[fileDefinition.name] = enrichedDBFile
                fileDefinition.internalFormatName?.let { internalFormatName ->
                    byInternalFormatName[internalFormatName] = enrichedDBFile
                }
                byFormatName[jarikoMetadata.recordFormat] = enrichedDBFile
            }
        }
    }
    operator fun get(nameOrFormat: String): EnrichedDBFile? = byFileName[nameOrFormat] ?: byFormatName[nameOrFormat] ?: byInternalFormatName[nameOrFormat]
}

/**
 * DBFile wrapper needed to add further information to DBFile
 * */
data class EnrichedDBFile(private val dbFile: DBFile, private val fileDefinition: FileDefinition, val jarikoMetadata: FileMetadata) : DBFile {

    // All files are opened by default when defined in F specs.
    var open = true

    override var fileMetadata = dbFile.fileMetadata

    override var name = dbFile.name

    override var logger = dbFile.logger

    override fun chain(key: String) = checkOpened().chain(key).validate()

    override fun chain(keys: List<String>) = checkOpened().chain(keys).validate()

    override fun delete(record: Record) = checkOpened().delete(record).validate()

    override fun eof() = checkOpened().eof()

    override fun equal() = checkOpened().equal()

    override fun read() = checkOpened().read().validate()

    override fun readEqual() = checkOpened().readEqual().validate()

    override fun readEqual(key: String) = checkOpened().readEqual(key).validate()

    override fun readEqual(keys: List<String>) = checkOpened().readEqual(keys).validate()

    override fun readPrevious() = checkOpened().readPrevious().validate()

    override fun readPreviousEqual() = checkOpened().readPreviousEqual().validate()

    override fun readPreviousEqual(key: String) = checkOpened().readPreviousEqual(key).validate()

    override fun readPreviousEqual(keys: List<String>) = checkOpened().readPreviousEqual(keys).validate()

    override fun setgt(key: String) = checkOpened().setgt(key)

    override fun setgt(keys: List<String>) = checkOpened().setgt(keys)

    override fun setll(key: String) = checkOpened().setll(key)

    override fun setll(keys: List<String>) = checkOpened().setll(keys)

    override fun update(record: Record) = checkOpened().update(record).validate()

    override fun write(record: Record) = checkOpened().write(record).validate()

    fun getDataDefinitionName(dbFieldName: String) = fileDefinition.getDataDefinitionName(dbFieldName)

    private fun checkOpened(): DBFile {
        require(open) {
            "Cannot access to closed file $name"
        }
        return dbFile
    }
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
