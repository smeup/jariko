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

package com.smeup.rpgparser.parsing.ast

import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.FileDefinition
import com.smeup.rpgparser.interpreter.InStatementDataDefinition
import com.smeup.rpgparser.interpreter.AbstractDataStructureType
import com.smeup.rpgparser.interpreter.startLine
import com.smeup.rpgparser.parsing.facade.CopyBlocks
import com.smeup.rpgparser.parsing.parsetreetoast.removeDuplicatedDataDefinition
import com.strumenta.kolasu.model.*
import kotlinx.serialization.Serializable

// This file contains the AST nodes at the highest level:
// from the CompilationUnit (which represents the whole file)
// to its main components
@Serializable
data class CompilationUnit(
    val fileDefinitions: List<FileDefinition>,
    val dataDefinitions: List<DataDefinition>,
    val main: MainBody,
    val subroutines: List<Subroutine>,
    val compileTimeArrays: List<CompileTimeArray>,
    val directives: List<Directive>,
    override val position: Position?,
    val apiDescriptors: Map<ApiId, ApiDescriptor>? = null,
    // This way we say to not consider these nodes as part of compilation unit, this annotation is
    // necessary to avoid that during data references resolving, are considered expression declared within procedures as well.
    @property:Link val procedures: List<CompilationUnit>? = null,
    val procedureName: String? = null,
    // TODO: Related to 'ProceduresParamsDataDefinitions' a refactor is required, but now:
    // - if 'CompilationUnit' is an 'RpgProgram' this list is null.
    // - if 'CompilationUnit' is an 'RpgFunction', this list contains procedure parameters (if any)
    val proceduresParamsDataDefinitions: List<DataDefinition>? = null,
    val source: String? = null,
    val copyBlocks: CopyBlocks? = null,
    val displayFiles: Map<String, DSPF>? = null
) : Node(position) {

    val resolvedProfilingAnnotations: List<ProfilingAnnotationResolved> by lazy {
        val statements = main.stmts.explode(true)
        statements.map { stmt ->
            val statementLine = stmt.position?.start?.line ?: stmt.startLine().toIntOrNull() ?: 0
            stmt.profilingAnnotations.map { annotation ->
                ProfilingAnnotationResolved(annotation, annotation.position!!.start.line, statementLine)
            }
        }.flatten()
    }

    var timeouts = emptyList<MuteTimeoutAnnotation>()

    val minTimeOut by lazy {
        timeouts.minByOrNull { it.timeout }?.timeout
    }

    companion object {
        fun empty() = CompilationUnit(
            fileDefinitions = emptyList(),
            dataDefinitions = emptyList(),
            main = MainBody(stmts = emptyList(), position = null),
            subroutines = emptyList(),
            compileTimeArrays = emptyList(),
            directives = emptyList(),
            position = null,
            procedures = emptyList()
        )
    }

    val entryPlist: PlistStmt?
        get() = main.stmts.plist()
                ?: subroutines.mapNotNull { it.stmts.plist() }.firstOrNull()

    private val inStatementsDataDefinitions = mutableListOf<InStatementDataDefinition>()

    fun getInStatementDataDefinitions() = inStatementsDataDefinitions

    fun addInStatementDataDefinitions(dataDefinitions: List<InStatementDataDefinition>) {
        inStatementsDataDefinitions.addAll(dataDefinitions)
    }

    @Derived
    val allDataDefinitions: List<AbstractDataDefinition> by lazy {
        // Unqualified DS sub-fields
        val unqualified = dataDefinitions
            .filter { it.type is AbstractDataStructureType && !(it.type as AbstractDataStructureType).isQualified }
            .flatMap { it.fields }

        val bucket = dataDefinitions + unqualified + inStatementsDataDefinitions
        bucket.removeDuplicatedDataDefinition()
    }

    internal val allDataDefinitionsByName: Map<String, AbstractDataDefinition> by lazy {
        allDataDefinitions.associateBy { it.name.uppercase() }
    }

    internal val subroutinesByName: Map<String, Subroutine> by lazy {
        subroutines.associateBy { it.name.uppercase() }
    }

    internal val dataDefinitionsByName: Map<String, DataDefinition> by lazy {
        dataDefinitions.associateBy { it.name.uppercase() }
    }

    internal val compileTimeArraysByName: Map<String, CompileTimeArray> by lazy {
        compileTimeArrays.associateBy { it.name.uppercase() }
    }

    internal val fileDefinitionsByName: Map<String, FileDefinition> by lazy {
        fileDefinitions.associateBy { it.name.uppercase() }
    }

    /**
     * This returns `true` if this procedure is a prototype by its empty lists for file definition,
     *  data definition, subroutines, compile time arrays and directive.
     */
    fun isProcedurePrototype(): Boolean {
        return !this.procedureName.isNullOrBlank() &&
                this.fileDefinitions.isEmpty() &&
                this.dataDefinitions.isEmpty() &&
                this.subroutines.isEmpty() &&
                this.compileTimeArrays.isEmpty() &&
                this.directives.isEmpty()
    }

    fun hasDataDefinition(name: String) = dataDefinitionsByName.containsKey(name.uppercase())

    fun getDataDefinition(name: String, errorMessage: () -> String = { "Data definition $name was not found" }) = dataDefinitionsByName[name.uppercase()]
            ?: throw IllegalArgumentException(errorMessage.invoke())

    fun getDataOrFieldDefinition(name: String) = dataDefinitions.firstOrNull { it.name.equals(name, ignoreCase = true) }
            ?: dataDefinitions.mapNotNull { it -> it.fields.find { it.name.equals(name, ignoreCase = true) } }.firstOrNull()
            ?: throw IllegalArgumentException("Data or field definition $name was not found")

    fun hasAnyDataDefinition(name: String) = allDataDefinitionsByName.containsKey(name.uppercase())

    fun getAnyDataDefinition(name: String) = allDataDefinitionsByName[name.uppercase()]
        ?: throw IllegalArgumentException("Data definition $name was not found")

    fun getAnyDataDefinition(name: String, errorMessage: () -> String = { "Data definition $name was not found" }) = allDataDefinitionsByName[name.uppercase()]
        ?: throw IllegalArgumentException(errorMessage.invoke())

    fun compileTimeArray(name: String): CompileTimeArray {
        fun firstCompileTimeArray() = if (compileTimeArrays.isNotEmpty()) {
            compileTimeArrays[0]
        } else {
            CompileTimeArray("", emptyList())
        }
        return compileTimeArraysByName[name.uppercase()] ?: firstCompileTimeArray()
    }

    fun compileTimeArray(index: Int): CompileTimeArray {
        require(compileTimeArrays.isNotEmpty()) {
            "CompileTimeArrays is empty"
        }
        return compileTimeArrays[index]
    }

    fun hasFileDefinition(name: String) = fileDefinitionsByName.containsKey(name.uppercase())

    fun getFileDefinition(name: String) = fileDefinitionsByName[name.uppercase()]
        ?: throw IllegalArgumentException("File definition $name was not found")
}

@Serializable
data class MainBody(val stmts: List<Statement>, override val position: Position? = null) : Node(position)

@Serializable
data class Subroutine(override val name: String, val stmts: List<Statement>, val tag: String? = null, override val position: Position? = null) : Named, Node(position)

@Serializable
data class Function(override val name: String, override val position: Position? = null) : Named, Node(position)

@Serializable
data class CompileTimeArray(override val name: String, val lines: List<String>, override val position: Position? = null) : Named, Node(position)

enum class DataWrapUpChoice {
    LR,
    RT
}

// A PList is a list of parameters
fun List<Statement>.plist(): PlistStmt? = this.asSequence().mapNotNull { it as? PlistStmt }.firstOrNull { it.isEntry }

internal fun CompilationUnit.format(): String {
    var indent = 0
    var foundComma = false
    val sb = StringBuilder()
    var index = 0
    val cuString = this.toString()
    cuString.iterator().forEach {
        when (it) {
            '{', '[', '(' -> {
                sb.append("$it\n")
                indent += 2
                sb.append(" ".repeat(indent))
            }
            '}', ']', ')' -> {
                foundComma = false
                sb.append("\n")
                indent -= 2
                sb.append(" ".repeat(indent))
                sb.append(it)
            }
            ',' -> {
                sb.append("$it\n")
                sb.append(" ".repeat(indent))
                foundComma = true
            }
            ' ' -> {
                if (!foundComma) {
                    sb.append(it)
                }
                foundComma = false
            }
            else -> sb.append(it)
        }
        index++
    }
    return sb.toString()
}
