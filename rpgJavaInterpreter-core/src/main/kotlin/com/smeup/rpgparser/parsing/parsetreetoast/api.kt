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
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.specificProcess

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
        apiId.loadAndUse { api ->
            this.copy(
                fileDefinitions = this.fileDefinitions.include(api.compilationUnit.fileDefinitions),
                dataDefinitions = this.dataDefinitions.include(api.compilationUnit.dataDefinitions),
                // subroutines with the same names cannot be included
                subroutines = this.subroutines.merge(api.compilationUnit.subroutines.invalidateResolution()),
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
 * @return Returns the result of the logic function applied to the loaded API.
 */
internal fun <T> ApiId.loadAndUse(logic: (api: Api) -> T): T {
    val parentPgmName = MainExecutionContext.getExecutionProgramName()
    val apiId = this
    MainExecutionContext.setExecutionProgramName(this.toString())
    val api = MainExecutionContext.getSystemInterface()!!.findApi(apiId).apply {
        MainExecutionContext.getConfiguration().jarikoCallback.onApiInclusion(apiId, this)
    }.validate()
    logic.invoke(api).let { result ->
        MainExecutionContext.setExecutionProgramName(parentPgmName)
        return result
    }
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

/**
 * Merges two lists of elements, ensuring no duplicates based on the `name` property.
 *
 * This function combines the current list with another list, filtering out elements
 * from the second list that have a `name` property matching any element in the current list.
 *
 * @param list The list of elements to merge with the current list.
 * @return A new list containing all elements from both lists, excluding duplicates
 *         based on the `name` property.
 */
private fun <T> List<T>.merge(list: List<T>): List<T> where T : Node, T : Named {
    return this + list.filter { elementToMerge -> elementToMerge.name != null && this.none { existingElement -> existingElement.name == elementToMerge.name } }
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

/**
 * Invalidates the resolution of variable references within a list of `Subroutine` objects.
 *
 * This function iterates through each `Subroutine` in the list and clears the `referred` property
 * of any `Variable` referenced by a `DataRefExpr`.  This effectively invalidates the previous
 * resolution of these variable references, potentially forcing them to be re-resolved later.
 * This is often necessary when changes have been made to the data model that might affect
 * the validity of existing variable resolutions.
 *
 * The function uses the `specificProcess` method to traverse the `Subroutine`'s expression tree
 * and target only `DataRefExpr` instances.  For each `DataRefExpr`, it sets the `referred`
 * property of the associated `Variable` to `null`.
 *
 * This function does not modify the original `Subroutine` objects; it returns a *new* list
 * containing modified copies of the `Subroutine` objects.
 *
 * @return A new list of `Subroutine` objects with invalidated variable references.
 */
private fun List<Subroutine>.invalidateResolution(): List<Subroutine> {
    return this.map { subroutine ->
        subroutine.specificProcess(DataRefExpr::class.java) { it.variable.referred = null }.let { subroutine }
    }
}