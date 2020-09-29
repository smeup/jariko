package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import java.text.SimpleDateFormat
import java.util.*

fun Value.stringRepresentation(format: String? = null): String {
    return when (this) {
        is StringValue -> value.trimEnd()
        is BooleanValue -> asString().value // TODO check if it's the best solution
        is NumberValue -> render()
        is ArrayValue -> "[${elements().map { it.render() }.joinToString(", ")}]"
        is TimeStampValue -> timestampFormatting(format)
        is DataStructValue -> value.trimEnd()
        is ZeroValue -> "0"
        is AllValue -> charsToRepeat
        else -> TODO("Unable to render value $this (${this.javaClass.canonicalName})")
    }
}

private fun TimeStampValue.timestampFormatting(format: String?): String =
    // TODO this is a simple stub for what the full implementation will be
    if ("*ISO0" == format) {
        SimpleDateFormat("yyyyMMddHHmmssSSS000").format(value)
    } else {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(value)
    }

fun CompilationUnit.activationGroupType(): ActivationGroupType? {
    val activationGroupTypes = directives.asSequence().filterIsInstance<ActivationGroupDirective>().map { it.type }
        .toList()
    return when {
        activationGroupTypes.isEmpty() -> null
        activationGroupTypes.size == 1 -> activationGroupTypes[0]
        else -> error("More than one activation group directive is not admitted")
    }
}

fun ActivationGroupType.assignedName(current: RpgProgram, caller: RpgProgram?): String {
    return when (this) {
        is CallerActivationGroup -> {
            require(caller != null) { "caller is mandatory" }
            caller.activationGroup!!.assignedName
        }
        is NewActivationGroup -> UUID.randomUUID().toString()
        is NamedActivationGroup -> groupName
        else -> error("$this ActivationGroupType is not yet handled")
    }
}
