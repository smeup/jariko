package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable

@Serializable
sealed class InputSpecification

@Serializable
data class FileNameInputSpecification(
    val name: String,
    val position: Position?
) : InputSpecification()

@Serializable
data class ExternalFieldInputSpecification(
    val originalName: String,
    val newName: String,
    val controlLevelIndicator: String?,
    val matchingFieldsIndicator: String?,
    val position: Position?
) : InputSpecification()

/**
 * Links the external definition to its specifications.
 */
data class InputSpecificationGroup(
    val fileName: FileNameInputSpecification,
    val specifications: List<InputSpecification>
)

fun RpgParser.ControlLevelIndicatorContext.toIndicator(): String? {
    return when {
        this.ControlLevelIndicator() != null -> this.ControlLevelIndicator().text
        else -> null
    }
}

fun RpgParser.MatchingFieldsIndicatorContext.toIndicator(): String? {
    return when {
        this.MatchingRecordIndicator() != null -> this.MatchingRecordIndicator().text
        else -> null
    }
}

fun RpgParser.Is_external_fieldContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): InputSpecification {
    val originalName = this.IF_Name().text.trim()
    val newName = this.IF_FieldName().text.trim()
    val controlIndicator = this.controlLevelIndicator().toIndicator()
    val matchField = this.matchingFieldsIndicator().toIndicator()

    // TODO: Add result indicators
    // val resultIndicators = this.resultIndicator()

    return ExternalFieldInputSpecification(
        originalName,
        newName,
        controlIndicator,
        matchField,
        toPosition(conf.considerPosition)
    )
}

fun RpgParser.Ispec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): InputSpecification {
    return when {
        this.is_external_field() != null -> this.is_external_field().toAst(conf)
        this.IS_FileName() != null -> {
            val fileName = this.IS_FileName().text.trim()

            // TODO: Add support for indicators
            FileNameInputSpecification(
                fileName,
                toPosition(conf.considerPosition)
            )
        }
        else -> todo(conf = conf)
    }
}