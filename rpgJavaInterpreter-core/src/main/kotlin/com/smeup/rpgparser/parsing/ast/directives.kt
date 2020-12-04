package com.smeup.rpgparser.parsing.ast

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class Directive(@Transient override val position: Position? = null) : Node(position)

@Serializable
data class DeceditDirective(val format: String, override val position: Position? = null) : Directive(position)

@Serializable
data class ActivationGroupDirective(
    @SerialName("actvgrpType") val type: ActivationGroupType,
    override val position: Position? = null
) :
    Directive(position)

@Serializable
sealed class ActivationGroupType

@Serializable
object CallerActivationGroup : ActivationGroupType()

@Serializable
object NewActivationGroup : ActivationGroupType()

@Serializable
data class NamedActivationGroup(val groupName: String) : ActivationGroupType()
