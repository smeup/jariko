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
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.Expression
import java.util.TreeMap

/** [FileDefinition.infdsName] resolves to a [DataDefinition] whose only declared field(s), if
 *  any, must sit at this exact byte range - the well-known IBM i INFDS Relative Record Number
 *  subfield offset (1-based 397-400, so 0-based [INFDS_RRN_START_OFFSET, INFDS_RRN_END_OFFSET)).
 *  This plan wires up only that one subfield, not full INFDS - see [resolveInfdsDataDefinition]. */
internal const val INFDS_RRN_START_OFFSET = 396
internal const val INFDS_RRN_END_OFFSET = 400

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
    fun add(
        fileDefinition: FileDefinition,
        compilationUnit: CompilationUnit,
    ) {
        if (!byFileName.containsKey(fileDefinition.name)) {
            // Resolved per-F-spec (fileDefinition.infdsName), never via the shared byFormatName
            // alias registry below: RENAME lets several F-specs share one record format while
            // each still declares (and needs) its own independent INFDS. Resolved before opening
            // any DB connection, so a bad INFDS(dsName) fails fast without touching reload at all.
            val infdsDataDefinition =
                fileDefinition.infdsName?.let { infdsName ->
                    resolveInfdsDataDefinition(infdsName, fileDefinition, compilationUnit)
                }
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
                val enrichedDBFile = EnrichedDBFile(it, fileDefinition, jarikoMetadata, infdsDataDefinition)
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
 * Resolves [infdsName] (an F-spec's `INFDS(dsName)` argument) to the [DataDefinition] it names,
 * failing fast - at file-open time, not on first read - when: the name doesn't resolve to any
 * declared data definition, it doesn't resolve to a data structure, or that data structure
 * declares any field outside the one subfield this plan supports (the Relative Record Number,
 * at the fixed byte offset [INFDS_RRN_START_OFFSET]-[INFDS_RRN_END_OFFSET]). This plan wires up
 * only that one INFDS subfield, not the full standard layout - see [INFDS_RRN_START_OFFSET]'s kdoc.
 */
private fun resolveInfdsDataDefinition(
    infdsName: String,
    fileDefinition: FileDefinition,
    compilationUnit: CompilationUnit,
): DataDefinition {
    val resolved =
        compilationUnit.allDataDefinitions.firstOrNull { it.name.equals(infdsName, ignoreCase = true) }
            ?: error(
                "File ${fileDefinition.name}: INFDS($infdsName) does not resolve to any declared data definition",
            )
    require(resolved is DataDefinition && resolved.type is DataStructureType) {
        "File ${fileDefinition.name}: INFDS($infdsName) must name a data structure (DS), found $resolved"
    }
    resolved.fields.forEach { field ->
        val start = field.explicitStartOffset ?: field.calculatedStartOffset
        val end = field.explicitEndOffset ?: field.calculatedEndOffset
        require(start == INFDS_RRN_START_OFFSET && end == INFDS_RRN_END_OFFSET) {
            "File ${fileDefinition.name}: INFDS($infdsName) declares field '${field.name}' at byte offset " +
                "${start?.plus(1)}-$end, but only the Relative Record Number subfield (byte offset " +
                "${INFDS_RRN_START_OFFSET + 1}-$INFDS_RRN_END_OFFSET) is supported - this is a partial " +
                "INFDS implementation, not the full standard layout"
        }
    }
    return resolved
}

/**
 * DBFile wrapper needed to add further information to DBFile
 * */
data class EnrichedDBFile(
    private val dbFile: DBFile,
    private val fileDefinition: FileDefinition,
    val jarikoMetadata: FileMetadata,
    /** Resolved target of this F-spec's `INFDS(dsName)` keyword, or null when not declared - see
     *  [resolveInfdsDataDefinition]. Read-execution statements write the current row's Relative
     *  Record Number into this DS after a successful read (see [InterpreterCore.writeInfdsRrn]). */
    val infdsDataDefinition: DataDefinition? = null,
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
