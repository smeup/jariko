package com.smeup.rpgparser

import me.tomassetti.kolasu.model.Node
import me.tomassetti.kolasu.model.Position

class CompilationUnit(override val position: Position?) : Node(position)

abstract class Expression