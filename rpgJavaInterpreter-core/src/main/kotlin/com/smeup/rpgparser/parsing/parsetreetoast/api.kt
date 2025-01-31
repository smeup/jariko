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

package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.AbstractDataStructureType
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node

internal fun RpgParser.Dir_apiContext.toApiId(conf: ToAstConfiguration): ApiId {
    return ApiId(
        position = toPosition(conf.considerPosition),
        library = this.library?.text,
        file = this.file?.text,
        member = this.member.text)
}

internal fun List<RpgParser.StatementContext>.toApiDescriptors(conf: ToAstConfiguration): Map<ApiId, ApiDescriptor>? {
    return mutableMapOf<ApiId, ApiDescriptor>().let { apiDescriptors ->
        forEach { statementContext ->
            statementContext.runParserRuleContext(conf = conf) {
                statementContext.directive()?.dir_api()?.let { dirApicontext ->
                    val apiId = dirApicontext.toApiId(conf)
                    apiDescriptors[apiId] = MainExecutionContext.getSystemInterface()!!.findApiDescriptor(apiId)
                }
            }
        }
        if (apiDescriptors.isEmpty()) {
            null
        } else {
            apiDescriptors
        }
    }
}

private fun CompilationUnit.includeApi(apiId: ApiId): CompilationUnit {
    return apiId.runNode {
        apiId.loadAndUse(this) { api ->
            this.copy(
                fileDefinitions = this.fileDefinitions.include(api.compilationUnit.fileDefinitions),
                dataDefinitions = this.dataDefinitions.include(api.compilationUnit.dataDefinitions).distinct(),
                subroutines = this.subroutines.include(api.compilationUnit.subroutines),
                compileTimeArrays = this.compileTimeArrays.include(api.compilationUnit.compileTimeArrays),
                directives = this.directives.include(api.compilationUnit.directives),
                position = this.position,
                apiDescriptors = api.compilationUnit.apiDescriptors?.let {
                    this.apiDescriptors?.plus(it)
                } ?: this.apiDescriptors,
                procedures = this.procedures.let { it ?: listOf() }.includeProceduresWithoutDuplicates(api.compilationUnit.procedures.let { it ?: listOf() })
            )
        }
    }
}

/**
 * Uses an API by loading it and applying the provided logic function.
 *
 * This function encapsulates the process of setting the current parsing program name to the API's ID,
 * loading the API, invoking a callback to signal the API's inclusion, and then applying a user-defined
 * logic function to the loaded API.
 * After the logic function is applied, the original parsing program name is restored.
 * This ensures that the API processing is isolated and does not affect the global
 * execution context outside of this function's scope.
 *
 * @param T The return type of the logic function applied to the API.
 * @param logic A higher-order function that takes an [Api] instance and returns a value of type [T].
 *              This function is applied to the API after it is loaded and validated.
 * @param parentCu Compilation Unit of its parent, if provided, to resolve duplications. Going to be considered the
 *                 parent definitions if the API has defined the same.
 * @return Returns the result of the logic function applied to the loaded API.
 */
internal fun <T> ApiId.loadAndUse(parentCu: CompilationUnit? = null, logic: (api: Api) -> T): T {
    val parentPgmName = MainExecutionContext.getExecutionProgramName()
    val apiId = this
    MainExecutionContext.setExecutionProgramName(this.toString())
    val api = MainExecutionContext.getSystemInterface()!!.findApi(apiId).also { api ->
        // Have to adjust the definitions by removing duplicates if already provided by its caller program.
        var apiPostProcess: Api = api
        if (parentCu != null) {
            apiPostProcess = apiPostProcess.resolveDuplicates(parentCu)
        }

        MainExecutionContext.getConfiguration().jarikoCallback.onApiInclusion(apiId, apiPostProcess)
    }.validate()
    logic.invoke(api).let { result ->
        MainExecutionContext.setExecutionProgramName(parentPgmName)
        return result
    }
}

/**
 * Resolves duplicate `DataDefinition` objects within an `Api`'s `CompilationUnit` by comparing them
 * against a parent `CompilationUnit`.
 *
 * This function leverages the `findAndReplaceDuplicatesFromParent` function to identify and resolve
 * duplicate `DataDefinition` objects.  It creates a new `Api` instance with the resolved
 * `DataDefinition` list.  Other components of the `CompilationUnit` (fileDefinitions, subroutines, etc.)
 * are copied from the original `Api` instance.
 *
 * **Important:** This function currently has a TODO regarding file logic.  It mentions a future
 * consideration for handling Data Structures with EXTNAME properties that refer to the same file
 * in both the current and parent CompilationUnits.  This logic is not yet implemented and may
 * affect the resolution of duplicates in those specific cases.
 *
 * @param parentCu The parent `CompilationUnit` against which duplicates are resolved.
 * @return A new `Api` instance with the `DataDefinition` list resolved.  All other elements of the
 *         `CompilationUnit` are copied from the original `Api`.
 * @see findAndReplaceDuplicatesFromParent
 */
private fun Api.resolveDuplicates(parentCu: CompilationUnit): Api {
    // TODO: Provide File logic by considering the existence of a DS with EXTNAME to the same File and from parent.

    val dataDefinitionResolved: List<DataDefinition> = this.compilationUnit.dataDefinitions.findAndReplaceDuplicatesFromParent(parentCu.dataDefinitions)

    return Api(
        compilationUnit = CompilationUnit(
            fileDefinitions = this.compilationUnit.fileDefinitions,
            dataDefinitions = dataDefinitionResolved,
            subroutines = this.compilationUnit.subroutines,
            compileTimeArrays = this.compilationUnit.compileTimeArrays,
            directives = this.compilationUnit.directives,
            apiDescriptors = this.compilationUnit.apiDescriptors,
            procedures = this.compilationUnit.procedures,
            position = this.compilationUnit.position,
            main = this.compilationUnit.main
        )
    )
}

/**
 * Finds and replaces duplicate `DataDefinition` objects in a list based on a parent list.
 *
 * This function resolves conflicts between a list of `DataDefinition` objects (presumably from an API)
 * and a parent list of `DataDefinition` objects.  It specifically targets situations where the API
 * defines a field that is also defined within an unqualified Data Structure in the parent.  In these
 * cases, the parent's definition is preferred and used to replace the API's definition.
 *
 * The matching is done based on the `name` property of the `DataDefinition` objects, ignoring case.
 * A check is performed to ensure that the types of the matching DataDefinitions are compatible using `matchType`.
 *
 * @param parent The parent list of `DataDefinition` objects.  This list is searched for definitions
 *               that match those in the receiver list.  It is expected that the parent list contains DataDefinitions
 *               with AbstractDataStructureTypes which are NOT qualified.
 * @return A new list of `DataDefinition` objects containing the resolved definitions.  Definitions from the
 *         API list are used unless a matching definition is found in the parent list (within an unqualified
 *         Data Structure), in which case the parent's definition is used.
 * @throws IllegalArgumentException If a matching `DataDefinition` is found in the parent list, but its type
 *                                  is incompatible with the corresponding `DataDefinition` in the API list
 *                                  (as determined by the `matchType` function).  The exception message will
 *                                  indicate the conflicting definitions.
 */
fun List<DataDefinition>.findAndReplaceDuplicatesFromParent(parent: List<DataDefinition>): List<DataDefinition> {
    val finalDataDefinitions = emptyList<DataDefinition>().toMutableList()

    // Resolves Standalone (in API) with field of unqualified DS (Parent).
    parent
        .filter { dataDefinition -> dataDefinition.type is AbstractDataStructureType && !(dataDefinition.type as AbstractDataStructureType).isQualified }
        .flatMap { dataDefinition -> dataDefinition.fields }
        .also { parentFieldsUnqualifiedDs ->
            this.forEach { dataDefinition ->
                val fromParent = parentFieldsUnqualifiedDs.firstOrNull { field -> field.name.equals(dataDefinition.name, ignoreCase = true) }
                // In this case replaces the one defined on API with already defined on parent.
                if (fromParent != null) {
                    fromParent.require(fromParent.matchType(dataDefinition)) {
                        "Incongruous definitions between API $dataDefinition and its parent $fromParent"
                    }
                    finalDataDefinitions.add(fromParent.parent as DataDefinition)
                } else {
                    finalDataDefinitions.add(dataDefinition)
                }
            }
        }

    return finalDataDefinitions
}

internal fun CompilationUnit.postProcess(): CompilationUnit {
    var compilationUnit = this
    apiDescriptors?.let { apiDescriptors ->
        apiDescriptors.forEach { apiEntry ->
            compilationUnit = when (apiEntry.value.loadApiPolicy) {
                LoadApiPolicy.Static -> compilationUnit.includeApi(apiEntry.key)
                else -> apiEntry.key.todo()
            }
        }
    }
    return compilationUnit
}

private fun <T : Node> List<T>.include(list: List<T>): List<T> {
    return this + list
}

private fun <F, D : Node> Map<F, List<D>>.include(map: Map<F, List<D>>): Map<F, List<D>> {
    return this + map
}

private fun Api.validate(): Api {
    require(compilationUnit.main.stmts.isEmpty()) { "The APIs containing statements are not handled yet" }
    return this
}

private fun List<CompilationUnit>.includeProceduresWithoutDuplicates(from: List<CompilationUnit>): List<CompilationUnit> {
    return this.map { procedure ->
        if (procedure.isProcedurePrototype()) {
            from.firstOrNull { it.procedureName == procedure.procedureName } ?: procedure
        } else {
            procedure
        }
    }
}