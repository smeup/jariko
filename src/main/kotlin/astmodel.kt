package com.smeup.rpgparser

import me.tomassetti.kolasu.model.Named
import me.tomassetti.kolasu.model.Node
import me.tomassetti.kolasu.model.Position

enum class DataType {
    SINGLE
}

class DataDefinition(override val name: String,
                     val dataType: DataType,
                     val size: Int,
                     val decimals: Int = 0,
                     val arrayLength: Expression = IntLiteral(1),
                     override val position: Position?) : Node(position), Named {

}

class CompilationUnit(val dataDefinitons: List<DataDefinition>, override val position: Position?) : Node(position) {
    fun hasDataDefinition(name: String) = dataDefinitons.any { it.name == name }

    fun getDataDefinition(name: String) = dataDefinitons.first { it.name == name }
}

abstract class Expression(override val position: Position? = null) : Node(position)

open class NumberLiteral(override val position: Position? = null) : Expression(position)
data class IntLiteral(val value: Long, override val position: Position? = null) : NumberLiteral(position)
data class RealLiteral(val value: Double, override val position: Position? = null) : NumberLiteral(position)
