package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.*
import com.strumenta.kolasu.model.*
import java.util.*

// This file contains the AST nodes at the highest level:
// from the CompilationUnit (which represents the whole file)
// to its main components

data class CompilationUnit(
    val fileDefinitions: List<FileDefinition>,
    val dataDefinitions: List<DataDefinition>,
    val main: MainBody,
    val subroutines: List<Subroutine>,
    val compileTimeArrays: List<CompileTimeArray>,
    override val position: Position?
) : Node(position) {

    companion object {
        fun empty() = CompilationUnit(emptyList(), emptyList(), MainBody(emptyList(), null), emptyList(), emptyList(), null)
    }

    var databaseInterface: DBInterface = DummyDBInterface

    val entryPlist: PlistStmt?
        get() = main.stmts.plist()
                ?: subroutines.mapNotNull { it.stmts.plist() }.firstOrNull()

    private val inStatementsDataDefinitions = LinkedList<InStatementDataDefinition>()

    fun addInStatementDataDefinitions(dataDefinitions: List<InStatementDataDefinition>) {
        inStatementsDataDefinitions.addAll(dataDefinitions)
    }

    private var _allDataDefinitions = mutableListOf<AbstractDataDefinition>()

    @Derived
    val allDataDefinitions: List<AbstractDataDefinition>
        get() {
            if (_allDataDefinitions.isEmpty()) {
                _allDataDefinitions.addAll(dataDefinitions)
                dataDefinitions.forEach { it.fields.let { _allDataDefinitions.addAll(it) } }
                _allDataDefinitions.addAll(inStatementsDataDefinitions)
                fileDefinitions.forEach {
                    val metadata = databaseInterface.metadataOf(it.name)
                    if (metadata != null) {
                        it.formatName = metadata.formatName
                        _allDataDefinitions.addAll(metadata.fields.map(DBField::toDataDefinition))
                    }
                }
                checkDuplicatedDataDefinition(_allDataDefinitions)
            }
            return _allDataDefinitions
        }

    private fun checkDuplicatedDataDefinition(dataDefinitions: List<AbstractDataDefinition>) {
        val dataDefinitionMap = mutableMapOf<String, AbstractDataDefinition>()
        dataDefinitions.forEach {
            val dataDefinition = dataDefinitionMap[it.name]
            if (dataDefinition == null) {
                dataDefinitionMap[it.name] = it
            } else {
                require(dataDefinition.type == it.type) {
                    "Incongruous definitions of ${it.name}: ${dataDefinition.type} vs ${it.type}"
                }
            }
        }
    }

    fun hasDataDefinition(name: String) = dataDefinitions.any { it.name.equals(name, ignoreCase = true) }

    fun getDataDefinition(name: String) = dataDefinitions.first { it.name.equals(name, ignoreCase = true) }

    fun hasAnyDataDefinition(name: String) = allDataDefinitions.any { it.name.equals(name, ignoreCase = true) }

    fun getAnyDataDefinition(name: String) = allDataDefinitions.first { it.name.equals(name, ignoreCase = true) }

    fun compileTimeArray(name: String): CompileTimeArray {
        return compileTimeArrays.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: compileTimeArrays[0]
    }

    fun hasFileDefinition(name: String) = fileDefinitions.any { it.name.equals(name, ignoreCase = true) }

    fun getFileDefinition(name: String) = fileDefinitions.first { it.name.equals(name, ignoreCase = true) }
}

data class MainBody(val stmts: List<Statement>, override val position: Position? = null) : Node(position)

class Subroutine(override val name: String, val stmts: List<Statement>, override val position: Position? = null) : Named, Node(position)
class Function(override val name: String, override val position: Position? = null) : Named, Node(position)

// TODO describe what a compile time array is
class CompileTimeArray(override val name: String, val lines: List<String>, override val position: Position? = null) : Named, Node(position)

enum class DataWrapUpChoice {
    LR,
    RT
}

// A PList is a list of parameters
fun List<Statement>.plist(): PlistStmt? = this.asSequence().mapNotNull { it as? PlistStmt }.firstOrNull { it.isEntry }
