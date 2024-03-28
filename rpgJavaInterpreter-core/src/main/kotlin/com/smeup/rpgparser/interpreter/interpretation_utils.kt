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

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import java.time.format.DateTimeFormatter
import java.util.*

fun Value.stringRepresentation(format: String? = null): String {
    return when (this) {
        is StringValue -> value
        is BooleanValue -> asString().value // TODO check if it's the best solution
        is NumberValue -> render()
        is ArrayValue -> "[${elements().map { it.render() }.joinToString(", ")}]"
        is TimeStampValue -> timestampFormatting(format)
        is DataStructValue -> value.trimEnd()
        is ZeroValue -> "0"
        is AllValue -> charsToRepeat
        is OccurableDataStructValue -> value().value.trimEnd()
        is UnlimitedStringValue -> value.trimEnd()
        else -> TODO("Unable to render value $this (${this.javaClass.canonicalName})")
    }
}

private fun TimeStampValue.timestampFormatting(format: String?): String =
    // TODO this is a simple stub for what the full implementation will be
    if ("*ISO0" == format) {
        DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS000").format(value)
    } else {
        DateTimeFormatter.ofPattern(TimeStampValue.DEFAULT_FORMAT).format(value)
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
            caller.activationGroup.assignedName
        }
        is NewActivationGroup -> UUID.randomUUID().toString()
        is NamedActivationGroup -> groupName
        else -> error("$this ActivationGroupType is not yet handled")
    }
}

/**
 * This function provides the resizing of a variable defined like String, changing the varying to not varying.
 * In addition, truncates the previous string if the target size is shorter, or add spaces if is longer.
 * Throws an `IllegalArgumentException` if the `initializationValue` isn't `StringLiteral` or `defaultValue` isn't `StringValue`.
 *
 * For example: `%LEN(<variable's name>) in `EVAL` like this:
 *     C                   EVAL      %LEN(VAR) = 5
 * changes the previous size declaration of `VAR` to a new size.
 */
internal fun DataDefinition.resizeStringSize(newSize: Int) {
    require(this.initializationValue is StringLiteral)
    require(this.defaultValue is StringValue)

    val initializationValue: StringLiteral = this.initializationValue
    val defaultValue: StringValue = this.defaultValue as StringValue
    this.type = StringType(newSize, false)
    if (initializationValue.value.length >= newSize) {
        initializationValue.value = initializationValue.value.substring(0, newSize)
        defaultValue.value = defaultValue.value.substring(0, newSize)
    } else {
        initializationValue.value = initializationValue.value.padEnd(newSize)
        defaultValue.value = defaultValue.value.padEnd(newSize)
    }
    defaultValue.varying = false
}
