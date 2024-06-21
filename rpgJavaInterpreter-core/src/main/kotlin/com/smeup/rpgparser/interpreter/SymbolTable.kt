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
import com.smeup.rpgparser.logging.ProgramUsageType
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.nanoseconds

@Serializable
class SymbolTable : ISymbolTable {
    private val values = LinkedHashMap<AbstractDataDefinition, Value>()
    private val names = mutableMapOf<String, AbstractDataDefinition>()

    override operator fun contains(dataName: String): Boolean = dataDefinitionByName(dataName) != null
    override operator fun contains(data: AbstractDataDefinition): Boolean = data in values

    override var parentSymbolTable: ISymbolTable? = null

    override operator fun get(data: AbstractDataDefinition) =
        if (MainExecutionContext.isLoggingEnabled) getWithLogging(data) else getInternal(data)

    override operator fun get(dataName: String): Value =
        if (MainExecutionContext.isLoggingEnabled) getWithLogging(dataName) else getInternal(dataName)

    override fun dataDefinitionByName(dataName: String): AbstractDataDefinition? {
        return names[dataName.uppercase()] ?: parentSymbolTable?.let { (parentSymbolTable as SymbolTable).names[dataName.uppercase()] }
    }

    override operator fun set(data: AbstractDataDefinition, value: Value): Value? {
        val start = System.nanoTime()

        val output = when (data.scope.visibility) {
            Visibility.Program -> (programSymbolTable as SymbolTable).setLocal(data, value)
            Visibility.Local -> setLocal(data, value)
            Visibility.Static -> (getStaticSymbolTable(data.scope.reference!!) as SymbolTable).setLocal(data, value)
        }

        val elapsed = System.nanoTime() - start
        if (MainExecutionContext.isLoggingEnabled) {
            val programName = MainExecutionContext.getExecutionProgramName()
            MainExecutionContext.log(
                LazyLogEntry.producePerformanceAndUpdateAnalytics(
                    { LogSourceData.fromProgram(programName) },
                    ProgramUsageType.SymbolTable,
                    SymbolTableAction.SET.name,
                    elapsed.nanoseconds
                )
            )
        }
        return output
    }

    private fun setLocal(data: AbstractDataDefinition, value: Value): Value? {
        // replaced "data.name in this" with "data.name in names" because I must search for local name
        // whereas "data.name in this" search for name also in parent symbol table
        // remember that this function set a local scope variable
        require(!(data !in this && data.name in names)) {
            "This data definition would conflict with an existing data definition with the same name. This data definition: $data. Existing data definition: ${this[data.name]}"
        }
        require(data.type.canBeAssigned(value)) {
            "Value $value cannot be assigned to data: $data"
        }

        if (data is FieldDefinition) {
            val containerValue = get(data.container)
            if (data.container.isArray()) {
                throw IllegalStateException("We do not yet handle an array container")
            } else if (data.declaredArrayInLine != null) {
                ProjectedArrayValue.forData(containerValue as DataStructValue, data).container.set(data, value.forType(data.type))
                return ProjectedArrayValue.forData(containerValue as DataStructValue, data)
            } else {
                // Should be always a DataStructValue
                when (containerValue) {
                    is DataStructValue -> {
                        containerValue.set(data, value.forType(data.type))
                        return coerce(containerValue[data], data.type)
                    }
                    is OccurableDataStructValue -> {
                        containerValue.value().set(data, value.forType(data.type))
                        return coerce(containerValue.value()[data], data.type)
                    }
                    else -> {
                        throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is $containerValue")
                    }
                }
            }
        } else {
            names[data.name.uppercase()] = data
            return values.put(data, value.forType(data.type))
        }
    }

    override fun getValues(): Map<AbstractDataDefinition, Value> {
        return values
    }

    /**
     * Clear symbol table
     * */
    override fun clear() {
        values.clear()
        names.clear()
    }

    /**
     * @return if is empty
     * */
    override fun isEmpty() = values.isEmpty()

    private fun getInternal(data: AbstractDataDefinition): Value = when (data.scope.visibility) {
        Visibility.Program -> (programSymbolTable as SymbolTable).getLocal(data)
        Visibility.Local -> getLocal(data)
        Visibility.Static -> (getStaticSymbolTable(data.scope.reference!!) as SymbolTable).getLocal(data)
    }

    private fun getInternal(dataName: String): Value {
        val data = dataDefinitionByName(dataName)
        if (data != null) {
            return get(data)
        }
        // We did not find a top-level declaration with that name,
        // looking among fields
        return findInFields(dataName)
    }

    private fun getWithLogging(data: AbstractDataDefinition): Value {
        val start = System.nanoTime()
        val value = getInternal(data)
        val elapsed = System.nanoTime() - start

        val programName = MainExecutionContext.getExecutionProgramName()
        MainExecutionContext.log(
            LazyLogEntry.producePerformanceAndUpdateAnalytics(
                { LogSourceData.fromProgram(programName) },
                ProgramUsageType.SymbolTable,
                SymbolTableAction.GET.name,
                elapsed.nanoseconds
            )
        )

        return value
    }

    private fun getWithLogging(dataName: String): Value {
        val start = System.nanoTime()
        val value = getInternal(dataName)
        val elapsed = System.nanoTime() - start

        val programName = MainExecutionContext.getExecutionProgramName()
        MainExecutionContext.log(
            LazyLogEntry.producePerformanceAndUpdateAnalytics(
                { LogSourceData.fromProgram(programName) },
                ProgramUsageType.SymbolTable,
                SymbolTableAction.GET.name,
                elapsed.nanoseconds
            )
        )

        return value
    }

    private fun getLocal(data: AbstractDataDefinition): Value {
        if (data is FieldDefinition) {
            val containerValue = get(data.container)
            return if (data.container.isArray()) {
                TODO("We do not yet handle an array container")
            } else if (data.declaredArrayInLine != null) {
                ProjectedArrayValue.forData(containerValue as DataStructValue, data)
            } else {
                // Should be always a DataStructValue
                when (containerValue) {
                    is DataStructValue -> return coerce(containerValue[data], data.type)
                    is OccurableDataStructValue -> return coerce(containerValue.value()[data], data.type)
                    else -> {
                        throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is $containerValue")
                    }
                }
            }
        }

        return values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
    }

    private fun findInFields(dataName: String): Value {
        values
            .filterKeys { it is DataDefinition }
            .forEach {
                val field = (it.key as DataDefinition).fields.firstOrNull {
                        field -> field.name.equals(dataName, ignoreCase = true) && field.canBeUsedUnqualified()
                }
                if (field != null) {
                    return if (it.key.type is ArrayType) {
                        TODO("We do not yet handle top level values of array type")
                    } else {
                        (it.value as DataStructValue)[field]
                    }
                }
            }

        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }
}
