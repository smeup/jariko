package com.smeup.rpgparser.ast

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position

abstract class MuteAnnotation(override val position: Position? = null) : Node(position)
