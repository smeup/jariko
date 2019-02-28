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
                     val decimal: Int = 0,
                     val arrayLength: Int = 1,
                     override val position: Position?) : Node(position), Named {

}

class CompilationUnit(val dataDefinitons: List<DataDefinition>, override val position: Position?) : Node(position) {
    fun hasDataDefinition(name: String) = dataDefinitons.any { it.name == name }

    fun getDataDefinition(name: String) = dataDefinitons.first { it.name == name }
}

abstract class Expression(override val position: Position?) : Node(position)
