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

    /**
     * Alias registry for a file's record-format name(s). A RENAME'd F-spec's internal format
     * name and the record's native format name (jarikoMetadata.recordFormat) are both aliases of
     * the same "format name" concept, and multiple F-specs legitimately share one alias (e.g. an
     * arrival-sequence F-spec plus several RENAME'd keyed F-specs over the same physical format).
     * Resolution is first-registration-wins: whichever F-spec is registered first for a given
     * alias keeps it; later F-specs sharing that alias never steal it. This makes format-name
     * CHAIN/READ/SETLL resolution deterministic and declaration-order-driven, instead of
     * accidental last-write-wins split across two separately-prioritized maps.
     */
    private val byFormatName =
        TreeMap<String, EnrichedDBFile>(String.CASE_INSENSITIVE_ORDER)

    /**
     * Register a FileDefinition and create relative DBFile object for access to database with Reload library
     */
    fun add(fileDefinition: FileDefinition) {
        if (!byFileName.containsKey(fileDefinition.name)) {
            val jarikoMetadata =
                MainExecutionContext
                    .getConfiguration()
                    .reloadConfig
                    ?.metadataProducer
                    ?.invoke(fileDefinition.name)
            require(jarikoMetadata != null)
            val dbFile =
                MainExecutionContext.getDBFileFactory()?.open(
                    fileName = fileDefinition.name,
                    fileMetadata = jarikoMetadata.toReloadMetadata(),
                )

            dbFile?.let {
                val enrichedDBFile = EnrichedDBFile(it, fileDefinition, jarikoMetadata)
                // dbFile not null
                // fileDefinition.name is unique per F-spec (guarded above); fileDefinition.internalFormatName
                // and jarikoMetadata.recordFormat are format-name aliases that MAY be shared across F-specs
                // (RENAME) - first registration wins, see byFormatName kdoc.
                byFileName[fileDefinition.name] = enrichedDBFile
                fileDefinition.internalFormatName?.let { internalFormatName ->
                    byFormatName.putIfAbsent(internalFormatName, enrichedDBFile)
                }
                byFormatName.putIfAbsent(jarikoMetadata.recordFormat, enrichedDBFile)
            }
        }
    }

    operator fun get(nameOrFormat: String): EnrichedDBFile? = byFileName[nameOrFormat] ?: byFormatName[nameOrFormat]
}

/**
 * DBFile wrapper needed to add further information to DBFile
 * */
data class EnrichedDBFile(
    private val dbFile: DBFile,
    private val fileDefinition: FileDefinition,
    val jarikoMetadata: FileMetadata,
) : DBFile {
    // All files are opened by default when defined in F specs.
    var open = true

    private val cacheEnabled = MainExecutionContext.getSystemInterface()?.getFeaturesFactory()?.isChainCacheEnabled() ?: false
    private val cacheKey: MutableMap<String, Result>? = if (cacheEnabled) mutableMapOf() else null
    private val cacheKeys: MutableMap<List<String>, Result>? = if (cacheEnabled) mutableMapOf() else null

    override var fileMetadata = dbFile.fileMetadata

    override var name = dbFile.name

    override var logger = dbFile.logger

    override fun chain(key: String): Result {
        val op = { checkOpened().chain(key).validate() }
        return cacheKey?.computeIfAbsent(key) { op.invoke() } ?: op.invoke()
    }

    override fun chain(keys: List<String>): Result {
        val op = { checkOpened().chain(keys).validate() }
        return cacheKeys?.computeIfAbsent(keys) { op.invoke() } ?: op.invoke()
    }

    override fun delete(record: Record): Result = checkOpened().delete(record).validate().apply { deleteCache() }

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

    override fun update(record: Record): Result = checkOpened().update(record).validate().apply { deleteCache() }

    override fun write(record: Record): Result = checkOpened().write(record).validate().apply { deleteCache() }

    fun getDataDefinitionName(dbFieldName: String) = fileDefinition.getDataDefinitionName(dbFieldName)

    private fun checkOpened(): DBFile {
        require(open) {
            "Cannot access to closed file $name"
        }
        return dbFile
    }

    private fun deleteCache() {
        cacheKey?.clear()
        cacheKeys?.clear()
    }
}

/**
 * Converts a value in string as required by reload, type currently is used in HyVal LowVal conversion
 * */
fun Value.asString(type: Type): String =
    if (this is HiValValue || this is LowValValue) {
        coerce(this, type).asString().value
    } else {
        this.asString().value
    }

/**
 * Creates a keyList as required by reload
 * */
fun Expression.createKList(
    fileMetadata: FileMetadata,
    interpreter: InterpreterCore,
): List<String> =
    if (type() is KListType) {
        interpreter.toSearchValues(this, fileMetadata)
    } else {
        when (val value = interpreter.eval(this)) {
            is StartValValue, is EndValValue -> throw NotImplementedError("$value constant not yet supported.")
            else -> {
                val accessFieldType = fileMetadata.accessFieldsType.firstOrNull()
                listOf(
                    if (accessFieldType != null) {
                        value.asString(accessFieldType)
                    } else {
                        // Unkeyed (arrival-sequence) file: this is a CHAIN/READE/READPE/SETLL/SETGT
                        // by Relative Record Number, not by key - there is no field type to coerce
                        // against, so stringify the raw value directly (RRN is always numeric).
                        value.asString().value
                    },
                )
            }
        }
    }

/**
 * Validate the result. For now do nothing
 * */
fun Result.validate(): Result =
    apply {
//        if (record.isEmpty()) {
//            require(indicatorEQ || indicatorHI || indicatorLO) {
//                "record is empty bot no flag is on"
//            }
//        }
    }
