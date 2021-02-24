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
data class ActivationGroupDirective(@SerialName("actvgrpType") val type: ActivationGroupType, override val position: Position? = null) :
    Directive(position)

@Serializable
sealed class ActivationGroupType
@Serializable
object CallerActivationGroup : ActivationGroupType()
@Serializable
object NewActivationGroup : ActivationGroupType()
@Serializable
data class NamedActivationGroup(val groupName: String) : ActivationGroupType()

data class CopyDirective(val copyId: CopyId, override val position: Position? = null) : Directive(position)

data class CopyId(val library: String?, val file: String?, val member: String)

/**
 * Get key related receiver, format is as follows:
 * library/file.member.ext
 * Since library and file are not mandatory returned key could be:
 * library/member.ext
 * file.member.ext
 * member.ext
 * */
fun CopyId.key(sourceProgram: SourceProgram): String {
    val fileName = this.file?.let {
        "$it.$member.${sourceProgram.extension}"
    } ?: "$member.${sourceProgram.extension}"
    return library?.let {
        "$library/$fileName"
    } ?: "$fileName"
}