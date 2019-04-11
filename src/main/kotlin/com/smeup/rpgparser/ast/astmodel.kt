package com.smeup.rpgparser.ast

import com.smeup.rpgparser.AbstractDataDefinition
import com.smeup.rpgparser.DataDefinition
import com.smeup.rpgparser.InStatementDataDefinition
import com.strumenta.kolasu.model.*
import java.util.*

data class CompilationUnit(val dataDefinitions: List<DataDefinition>,
                           val main: MainBody,
                           val subroutines: List<Subroutine>,
                           override val position: Position?) : Node(position) {

    private val inStatementsDataDefinitions = LinkedList<InStatementDataDefinition>()

    fun addInStatementDataDefinition(dataDefinition: InStatementDataDefinition) {
        inStatementsDataDefinitions.add(dataDefinition)
    }

    @Derived
    val allDataDefinitions : List<AbstractDataDefinition>
        get() {
            val res = LinkedList<AbstractDataDefinition>()
            res.addAll(dataDefinitions)
            dataDefinitions.forEach { it.fields?.let { res.addAll(it) } }
            res.addAll(inStatementsDataDefinitions)
            return res
        }

    fun hasDataDefinition(name: String) = dataDefinitions.any { it.name == name }

    fun getDataDefinition(name: String) = dataDefinitions.first { it.name == name }

    fun hasAnyDataDefinition(name: String) = allDataDefinitions.any { it.name == name }

    fun getAnyDataDefinition(name: String) = allDataDefinitions.first { it.name == name }
}

data class MainBody(val stmts: List<Statement>, override val position: Position? = null) : Node(position)

class Subroutine(override val name: String, val stmts: List<Statement>, override val position: Position? = null) : Named, Node(position)
class Function(override val name: String, override val position: Position? = null) : Named, Node(position)

enum class DataWrapUpChoice {
    LR,
    RT
}
