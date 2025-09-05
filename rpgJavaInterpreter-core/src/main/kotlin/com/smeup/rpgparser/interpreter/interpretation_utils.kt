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

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.parsing.facade.relative
import com.strumenta.kolasu.model.Position
import java.time.format.DateTimeFormatter
import java.util.*

private const val MEMORY_SLICE_ATTRIBUTE = "com.smeup.rpgparser.interpreter.memorySlice"

typealias StatementReference = Pair<Int, SourceReference>

fun Value.stringRepresentation(format: String? = null): String =
    when (this) {
        is StringValue -> value
        is BooleanValue -> asString().value // TODO check if it's the best solution
        is NumberValue -> render()
        is ArrayValue -> "[${elements().joinToString(", ") { it.render() }}]"
        is TimeStampValue -> timestampFormatting(format)
        is DataStructValue -> value.toString().trimEnd()
        is ZeroValue -> STRING_REPRESENTATION
        is AllValue -> charsToRepeat
        is OccurableDataStructValue -> value().value.toString().trimEnd()
        is UnlimitedStringValue -> value.trimEnd()
        else -> TODO("Unable to render value $this (${this.javaClass.canonicalName})")
    }

private fun TimeStampValue.timestampFormatting(format: String?): String =
    // TODO this is a simple stub for what the full implementation will be
    if ("*ISO0" == format) {
        DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS000").format(value)
    } else {
        DateTimeFormatter.ofPattern(TimeStampValue.DEFAULT_FORMAT).format(value)
    }

fun CompilationUnit.activationGroupType(): ActivationGroupType? {
    val activationGroupTypes =
        directives
            .asSequence()
            .filterIsInstance<ActivationGroupDirective>()
            .map { it.type }
            .toList()
    return when {
        activationGroupTypes.isEmpty() -> null
        activationGroupTypes.size == 1 -> activationGroupTypes[0]
        else -> error("More than one activation group directive is not admitted")
    }
}

fun ActivationGroupType.assignedName(caller: RpgProgram?): String =
    when (this) {
        is CallerActivationGroup -> {
            require(caller != null) { "caller is mandatory" }
            caller.activationGroup.assignedName
        }
        is NewActivationGroup -> UUID.randomUUID().toString()
        is NamedActivationGroup -> groupName
    }

/**
 * This function provides the resizing of a variable defined like String, changing the varying to not varying.
 * In addition, truncates the previous string if the target size is shorter, or add spaces if is longer.
 * Throws an `IllegalArgumentException` if the `initializationValue` isn't `StringLiteral` or `defaultValue` isn't `StringValue`.
 *
 * For example: `%LEN(<variable's name>) in `EVAL` like this:
 *     C                   EVAL      %LEN(VAR) = 5
 * changes the previous size declaration of `VAR` to a new size.
 */
internal fun DataDefinition.resizeStringSize(newSize: Int) {
    require(this.initializationValue is StringLiteral)
    require(this.defaultValue is StringValue)

    val initializationValue: StringLiteral = this.initializationValue
    val defaultValue: StringValue = this.defaultValue as StringValue
    this.type = StringType(newSize, false)
    if (initializationValue.value.length >= newSize) {
        initializationValue.value = initializationValue.value.substring(0, newSize)
        defaultValue.value = defaultValue.value.substring(0, newSize)
    } else {
        initializationValue.value = initializationValue.value.padEnd(newSize)
        defaultValue.value = defaultValue.value.padEnd(newSize)
    }
    defaultValue.varying = false
}

/**
 * Get the smallest line bounds containing all the provided statements.
 */
internal fun List<Statement>.lineBounds(): Pair<Int, Int> {
    val positions = this.mapNotNull { it.position }
    val start = positions.minOfOrNull { it.start.line } ?: 0
    val end = positions.maxOfOrNull { it.end.line } ?: 0

    return start to end
}

fun MutableMap<IndicatorKey, BooleanValue>.clearStatelessIndicators() {
    IndicatorType.STATELESS_INDICATORS.forEach {
        this.remove(it)
    }
}

/**
 * @return An instance of StatementReference related to position.
 * */
internal fun Position.relative(): StatementReference {
    val programName =
        if (MainExecutionContext.getProgramStack().empty()) null else MainExecutionContext.getProgramStack().peek().name
    val copyBlocks =
        programName?.let {
            MainExecutionContext
                .getProgramStack()
                .peek()
                .cu.copyBlocks
        }
    return this.relative(programName, copyBlocks)
}

/**
 * Memory slice context attribute name must to be also string representation of MemorySliceId
 * */
internal fun MemorySliceId.getAttributeKey() = "${MEMORY_SLICE_ATTRIBUTE}_$this"

/**
 * Restores the symbol table from a memory slice.
 *
 * This function is used to restore the state of the symbol table from a previously saved memory slice.
 * This is useful in scenarios where the state of the symbol table needs to be preserved across different
 * executions of the same program, for example in case of stateful programs.
 *
 * @param memorySliceId The ID of the memory slice to restore from. This ID is used to look up the memory slice in the memory slice manager.
 * @param memorySliceMgr The memory slice manager that is used to manage memory slices. It provides functions to create, retrieve and delete memory slices.
 * @param initialValues A map of initial values to be set in the symbol table. These values will not be overwritten by the values from the memory slice.
 */
internal fun ISymbolTable.restoreFromMemorySlice(
    memorySliceId: MemorySliceId?,
    memorySliceMgr: MemorySliceMgr?,
    initialValues: Map<String, Value> = emptyMap(),
) {
    memorySliceId?.let { myMemorySliceId ->
        memorySliceMgr?.let {
            MainExecutionContext.getAttributes()[myMemorySliceId.getAttributeKey()] =
                it.associate(
                    memorySliceId = memorySliceId,
                    symbolTable = this,
                    initSymbolTableEntry = { dataDefinition, storedValue ->
                        // initial values have not to be overwritten
                        if (!initialValues.containsKey(dataDefinition.name)) {
                            this[dataDefinition] = storedValue
                        }
                    },
                )
        }
    }
}
