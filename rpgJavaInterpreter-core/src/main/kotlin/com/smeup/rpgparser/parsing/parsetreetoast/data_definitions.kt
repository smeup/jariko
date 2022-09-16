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

package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.utils.asInt
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import java.math.BigDecimal
import kotlin.math.max

enum class RpgType(val rpgType: String) {
    PACKED("P"),
    ZONED("S"),
    INTEGER("I"),
    UNSIGNED("U"),
    BINARY("B")
}

private fun inferDsSizeFromFieldLines(fieldsList: FieldsList): Int {
    require(fieldsList.isNotEmpty())
    var maxEnd = 0
    fieldsList.fields.forEach {
        val end = it.endOffset ?: throw IllegalStateException("No end offset set for field ${it.name}")
        maxEnd = max(maxEnd, end)
    }
    return maxEnd
}

fun RpgParser.Dcl_dsContext.elementSizeOf(fieldsList: FieldsList = this.calculateFieldInfos()): Int {
    val toPosition = if (this.nameIsInFirstLine) {
        this.TO_POSITION().text
    } else {
        val header = this.parm_fixed().first()
        header.TO_POSITION().text
    }
    return if (toPosition.isBlank()) {
        inferDsSizeFromFieldLines(fieldsList)
    } else {
        toPosition.trim().toInt()
    }
}

internal fun RpgParser.Fspec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): FileDefinition {
    val prefixContexts = this.fs_keyword().mapNotNull { it.keyword_prefix() }
    val prefix: Prefix? = if (prefixContexts.isNotEmpty()) {
        Prefix(
            prefix = prefixContexts[0].prefix.text,
            numCharsReplaced = prefixContexts[0].nbr_of_char_replaced?.text?.toInt()
        )
    } else {
        null
    }
    val fileDefinition = FileDefinition(
        name = this.FS_RecordName().text.trim(),
        position = this.toPosition(conf.considerPosition),
        prefix = prefix
    )
    val rename = this.fs_keyword().mapNotNull { it.keyword_rename() }
    if (rename.isNotEmpty()) {
        // TODO Should we evaluate rename[0].int_format ???
        val internalRecordFormatName = rename[0].int_format.text
        fileDefinition.internalFormatName = internalRecordFormatName
    }
    return fileDefinition
}

internal fun RpgParser.Keyword_extnameContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): FileDefinition {
    return FileDefinition(
        name = getExtName(),
        position = toPosition(conf.considerPosition),
        justExtName = true
    )
}

private val RpgParser.Parm_fixedContext.decimalPositions
    get() = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() }

internal fun RpgParser.Parm_fixedContext.toAst(
    conf: ToAstConfiguration = ToAstConfiguration(),
    knownDataDefinitions: List<DataDefinition>
): DataDefinition {
    val compileTimeInterpreter = InjectableCompileTimeInterpreter(knownDataDefinitions, conf.compileTimeInterpreter)

    //    A Character (Fixed or Variable-length format)
    //    B Numeric (Binary format)
    //    C UCS-2 (Fixed or Variable-length format)
    //    D Date
    //    F Numeric (Float format)
    //    G Graphic (Fixed or Variable-length format)
    //    I Numeric (Integer format)
    //    N Character (Indicator format)
    //    O Object
    //    P Numeric (Packed decimal format)
    //    S Numeric (Zoned format)
    //    T Time
    //    U Numeric (Unsigned format)
    //    Z Timestamp
    //    * Basing pointer or procedure pointer

    var like: AssignableExpression? = null
    var dim: Expression? = null
    var initializationValue: Expression? = null
    var elementsPerLineExpression: Expression? = null
    var compileTimeArray = false
    var varying = false
    var ascend: Boolean? = null

    this.keyword().forEach {
        it.keyword_ascend()?.let {
            ascend = true
        }
        it.keyword_descend()?.let {
            ascend = false
        }
        it.keyword_like()?.let {
            like = it.simpleExpression().toAst(conf) as AssignableExpression
        }
        it.keyword_inz()?.let {
            initializationValue = it.simpleExpression()?.toAst(conf)
        }
        it.keyword_dim()?.let {
            dim = it.simpleExpression().toAst(conf)
        }
        it.keyword_perrcd()?.let {
            elementsPerLineExpression = it.simpleExpression()?.toAst(conf)
        }
        it.keyword_ctdata()?.let {
            compileTimeArray = true
        }
        it.keyword_varying()?.let {
            varying = true
        }
    }

    val elementSize = when {
        like != null -> {
            compileTimeInterpreter.evaluateElementSizeOf(this.rContext(), like!!, conf)
        }
        else -> this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    }

    val baseType =
        when (this.DATA_TYPE()?.text?.trim()?.toUpperCase()) {
            null -> todo(conf = conf)
            "" -> if (this.DECIMAL_POSITIONS().text.isNotBlank()) {
                /* TODO should be packed? */
                NumberType(elementSize!! - decimalPositions, decimalPositions)
            } else {
                if (like != null) {
                    compileTimeInterpreter.evaluateTypeOf(this.rContext(), like!!, conf)
                } else {
                    StringType(elementSize!!, varying)
                }
            }
            "A" -> StringType(elementSize!!, varying)
            "N" -> BooleanType
            "Z" -> TimeStampType
            /* TODO should be zoned? */
            RpgType.ZONED.rpgType -> {
                /* Zoned Type */
                NumberType(elementSize!! - decimalPositions, decimalPositions, RpgType.ZONED.rpgType)
            }
            RpgType.PACKED.rpgType -> {
                /* Packed Type */
                NumberType(elementSize!! - decimalPositions, decimalPositions, RpgType.PACKED.rpgType)
            }
            RpgType.BINARY.rpgType -> {
                /* Binary */
                NumberType(elementSize!!, 0, RpgType.BINARY.rpgType)
            }
            RpgType.INTEGER.rpgType -> {
                /* Integer Type */
                NumberType(elementSize!!, 0, RpgType.INTEGER.rpgType)
            }
            RpgType.UNSIGNED.rpgType -> {
                /* Unsigned Type */
                NumberType(elementSize!!, 0, RpgType.UNSIGNED.rpgType)
            }
            else -> throw UnsupportedOperationException("Unknown type: <${this.DATA_TYPE().text}>")
        }

    val type = if (dim != null) {
        var compileTimeRecordsPerLine: Int? = null
        if (compileTimeArray) {
            if (elementsPerLineExpression != null) {
                compileTimeRecordsPerLine = compileTimeInterpreter.evaluate(this.rContext(), elementsPerLineExpression!!).asInt().value.toInt()
            } else {
                compileTimeRecordsPerLine = 1
            }
            require(compileTimeRecordsPerLine > 0)
        }

        if (!baseType.isArray()) {
            ArrayType(baseType, compileTimeInterpreter.evaluate(this.rContext(), dim!!).asInt().value.toInt(), compileTimeRecordsPerLine).also {
                it.ascend = ascend
            }
        } else {
            baseType
        }
    } else {
        baseType
    }
    return DataDefinition(
        this.ds_name().text,
        type,
        initializationValue = initializationValue,
        position = this.toPosition(true))
}

internal fun RpgParser.DspecContext.toAst(
    conf: ToAstConfiguration = ToAstConfiguration(),
    knownDataDefinitions: List<DataDefinition>
): DataDefinition {

    val compileTimeInterpreter = InjectableCompileTimeInterpreter(knownDataDefinitions, conf.compileTimeInterpreter)

    //    A Character (Fixed or Variable-length format)
    //    B Numeric (Binary format)
    //    C UCS-2 (Fixed or Variable-length format)
    //    D Date
    //    F Numeric (Float format)
    //    G Graphic (Fixed or Variable-length format)
    //    I Numeric (Integer format)
    //    N Character (Indicator format)
    //    O Object
    //    P Numeric (Packed decimal format)
    //    S Numeric (Zoned format)
    //    T Time
    //    U Numeric (Unsigned format)
    //    Z Timestamp
    //    * Basing pointer or procedure pointer

    var like: AssignableExpression? = null
    var dim: Expression? = null
    var initializationValue: Expression? = null
    var elementsPerLineExpression: Expression? = null
    var compileTimeArray = false
    var varying = false
    var ascend: Boolean? = null

    this.keyword().forEach {
        it.keyword_ascend()?.let {
            ascend = true
        }
        it.keyword_descend()?.let {
            ascend = false
        }
        it.keyword_like()?.let {
            like = it.simpleExpression().toAst(conf) as AssignableExpression
        }
        it.keyword_inz()?.let {
            initializationValue = it.simpleExpression()?.toAst(conf)
        }
        it.keyword_dim()?.let {
            dim = it.simpleExpression().toAst(conf)
        }
        it.keyword_perrcd()?.let {
            elementsPerLineExpression = it.simpleExpression()?.toAst(conf)
        }
        it.keyword_ctdata()?.let {
            compileTimeArray = true
        }
        it.keyword_varying()?.let {
            varying = true
        }
    }

    val elementSize = when {
        like != null -> {
            compileTimeInterpreter.evaluateElementSizeOf(this.rContext(), like!!, conf)
        }
        else -> this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    }

    val baseType =
        when (this.DATA_TYPE()?.text?.trim()?.toUpperCase()) {
            null -> todo(conf = conf)
            "" -> if (this.DECIMAL_POSITIONS().text.isNotBlank()) {
                /* TODO should be packed? */
                NumberType(elementSize!! - decimalPositions, decimalPositions)
            } else {
                if (like != null) {
                    compileTimeInterpreter.evaluateTypeOf(this.rContext(), like!!, conf)
                } else {
                    StringType(elementSize!!, varying)
                }
            }
            "A" -> StringType(elementSize!!, varying)
            "N" -> BooleanType
            "Z" -> TimeStampType
            /* TODO should be zoned? */
            RpgType.ZONED.rpgType -> {
                /* Zoned Type */
                NumberType(elementSize!! - decimalPositions, decimalPositions, RpgType.ZONED.rpgType)
            }
            RpgType.PACKED.rpgType -> {
                /* Packed Type */
                NumberType(elementSize!! - decimalPositions, decimalPositions, RpgType.PACKED.rpgType)
            }
            RpgType.BINARY.rpgType -> {
                /* Binary */
                NumberType(elementSize!!, 0, RpgType.BINARY.rpgType)
            }
            RpgType.INTEGER.rpgType -> {
                /* Integer Type */
                NumberType(elementSize!!, 0, RpgType.INTEGER.rpgType)
            }
            RpgType.UNSIGNED.rpgType -> {
                /* Unsigned Type */
                NumberType(elementSize!!, 0, RpgType.UNSIGNED.rpgType)
            }
            else -> throw UnsupportedOperationException("Unknown type: <${this.DATA_TYPE().text}>")
    }

    val type = if (dim != null) {
        var compileTimeRecordsPerLine: Int? = null
        if (compileTimeArray) {
            if (elementsPerLineExpression != null) {
                compileTimeRecordsPerLine = compileTimeInterpreter.evaluate(this.rContext(), elementsPerLineExpression!!).asInt().value.toInt()
            } else {
                compileTimeRecordsPerLine = 1
            }
            require(compileTimeRecordsPerLine > 0)
        }

        if (!baseType.isArray()) {
            ArrayType(baseType, compileTimeInterpreter.evaluate(this.rContext(), dim!!).asInt().value.toInt(), compileTimeRecordsPerLine).also {
                it.ascend = ascend
            }
        } else {
            baseType
        }
    } else {
        baseType
    }
    return DataDefinition(
            this.ds_name().text,
            type,
            initializationValue = initializationValue,
            position = this.toPosition(true))
}

internal fun RpgParser.Dcl_cContext.toAst(
    conf: ToAstConfiguration = ToAstConfiguration()
): DataDefinition {
    // TODO: check more examples of const declaration
    val initializationValueExpression: Expression = this.keyword_const().simpleExpression().toAst(conf)
    val type = initializationValueExpression.type()
    return DataDefinition(
            this.ds_name().text,
            type,
            initializationValue = initializationValueExpression,
            position = this.toPosition(true),
            const = true
    )
}

private val RpgParser.DspecContext.decimalPositions
    get() = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() }

val RpgParser.Dcl_dsContext.nameIsInFirstLine: Boolean
    get() {
        return this.ds_name().text.trim().isNotEmpty()
    }

val RpgParser.Dcl_dsContext.name: String
    get() {
        return if (nameIsInFirstLine) {
            this.ds_name().text.trim()
        } else {
            // These DS are not accessible in RPG code, however we give them
            // a name for debugging purposes
            "@UNNAMED_DS_${this.toPosition(true)!!.start.line}"
        }
    }

internal fun RpgParser.Dcl_dsContext.type(
    size: Int? = null,
    fieldsList: FieldsList,
    conf: ToAstConfiguration = ToAstConfiguration()
): Type {
    val explicitSize = this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    val keywords = this.keyword()
    val dim: Expression? = keywords.asSequence().mapNotNull { it.keyword_dim()?.simpleExpression()?.toAst(conf) }.firstOrNull()
    val nElements = if (dim != null) conf.compileTimeInterpreter.evaluate(this.rContext(), dim).asInt().value.toInt() else null
    val fieldTypes: List<FieldType> = fieldsList.fields.map { it.toFieldType() }
    val calculatedElementSize = fieldsList.fields.map {
        if (it.overlayInfo == null) {
            if (it.arraySizeDeclared == null) {
                it.endOffset!!
            } else {
                if (it.explicitStartOffset != null && it.explicitEndOffset != null) {
                    it.endOffset!!
                } else {
                    it.startOffset!! + (it.elementSize!! * it.arraySizeDeclared!!)
                }
            }
        } else {
            0
        }
    }.maxOrNull()
    val elementSize = explicitSize
            ?: calculatedElementSize
            ?: throw IllegalStateException("No explicit size and no fields in DS ${this.name}, so we cannot calculate the element size")
    val baseType = DataStructureType(fieldTypes, size ?: elementSize)
    return if (nElements == null) {
        baseType
    } else {
        ArrayType(baseType, nElements)
    }
}

private val RpgParser.Parm_fixedContext.name: String
    get() = this.ds_name().text

/**
 * This class should contain all information explicitly present in the field definition and it should
 * be enriched as we calculate more information. Some information can be calculated by looking at other
 * fields or the containing data definition and this is way we cannot build FieldDefinitions in one step.
 */
data class FieldInfo(
    val name: String,
    val overlayInfo: OverlayInfo? = null,
    val explicitStartOffset: Int?,
    val explicitEndOffset: Int?,
    var arraySizeDeclared: Int? = null,
    var arraySizeDeclaredOnThisField: Int? = null,
    // This can be set when the type permits to get the element size
    // For example, here it is possible:
    // D  FI07                         15  3 OVERLAY(AR01:*NEXT)
    // While here it is not:
    // D AR01                                DIM(100) ASCEND
    var explicitElementType: Type? = null,
    val initializationValue: Expression? = null,
    val descend: Boolean = false,
    val position: Position?
) {

    var startOffset: Int? = explicitStartOffset // these are mutable as they can be calculated using next

    // In case of an array it indicated the end of the first element
    var endOffset: Int? = explicitEndOffset // these are mutable as they can be calculated using next

    // In case this is not an array this is the same as endOffset, otherwise the end
    // of the array
    val endOffsetIncludingAllElement: Int?
        get() = if (endOffset == null) null else if (this.arraySizeDeclared == null) endOffset!! else {
            (startOffset!! + (elementSize!! * arraySizeDeclared!!))
        }

    var calculatedElementSize: Int? = null
    var calculatedElementType: Type? = null

    val elementSize: Int?
        get() = explicitElementSize ?: calculatedElementSize

    val explicitElementSize: Int?
        get() = explicitElementType?.size

    val elementType: Type
        get() {
            return explicitElementType ?: (calculatedElementType ?: throw IllegalStateException("No element type available for $name"))
        }

    fun type(): Type {
        return if (arraySizeDeclared == null) {
            elementType
        } else {
            ArrayType(elementType, arraySizeDeclared!!)
        }
    }

    data class OverlayInfo(val targetFieldName: String, val isNext: Boolean, val posValue: Long?)

    fun toFieldType(): FieldType {
        return FieldType(name, type())
    }

    fun toAst(conf: ToAstConfiguration = ToAstConfiguration()): FieldDefinition {
        return FieldDefinition(this.name,
                type(),
                explicitStartOffset = this.explicitStartOffset,
                explicitEndOffset = if (explicitStartOffset != null) this.explicitEndOffset else null,
                calculatedStartOffset = if (this.explicitStartOffset != null) null else this.startOffset,
                calculatedEndOffset = if (this.explicitEndOffset != null) null else this.endOffset,
                initializationValue = this.initializationValue,
                descend = this.descend,
                position = if (conf.considerPosition) this.position else null,
                declaredArrayInLineOnThisField = arraySizeDeclaredOnThisField)
    }
}

internal fun RpgParser.Parm_fixedContext.arraySizeDeclared(): Int? {
    if (this.keyword().any { it.keyword_dim() != null }) {
        val dims = this.keyword().mapNotNull { it.keyword_dim() }
        require(dims.size == 1)
        val dim = dims[0]
        return dim.numeric_constant.text.toInt()
    }
    return null
}

data class TypeInfo(
    val stringCode: String?,
    val integerPositions: Int?,
    val decimalPositions: Int?,
    val isPackEven: Boolean
)

fun RpgParser.Parm_fixedContext.toTypeInfo(): TypeInfo {
    this.FROM_POSITION()
    this.TO_POSITION()
    return TypeInfo(
            stringCode = DATA_TYPE()?.text?.trim(),
            integerPositions = if (TO_POSITION().text.isNotBlank()) TO_POSITION().text.trim().toInt() else null,
            decimalPositions = if (DECIMAL_POSITIONS().text.isNotBlank()) with(DECIMAL_POSITIONS().text.trim()) { if (isEmpty()) 0 else toInt() } else null,
            isPackEven = keyword().any { it.keyword_packeven() != null }
    )
}

internal fun RpgParser.Parm_fixedContext.calculateExplicitElementType(arraySizeDeclared: Int?, conf: ToAstConfiguration): Type? {
    val rpgCodeType = DATA_TYPE()?.text?.trim() ?: RpgType.ZONED.rpgType
    val decimalPositions = if (DECIMAL_POSITIONS().text.isNotBlank()) with(DECIMAL_POSITIONS().text.trim()) { if (isEmpty()) 0 else toInt() } else null
    val isPackEven = keyword().any { it.keyword_packeven() != null }
    val isVarying = keyword().any { it.keyword_varying() != null }
    val startPosition = this.explicitStartOffset()
    val endPosition = this.explicitEndOffset()
    val precision = if (startPosition != null && endPosition != null) {
        endPosition - startPosition.toInt()
    } else {
        if (TO_POSITION().text.isNotBlank()) TO_POSITION().text.trim().toInt() else null
    }
    val totalSize = when {
        startPosition == null -> null
        endPosition == null -> endPosition
        else -> endPosition - startPosition.toInt()
    }
    val explicitElementSize = if (arraySizeDeclared != null) {
        totalSize?.let {
            it / arraySizeDeclared()!!
        }
    } else {
        totalSize
    }

    return when (rpgCodeType) {
        "", RpgType.ZONED.rpgType -> {
            if (decimalPositions == null && precision == null) {
                null
            } else if (decimalPositions == null) {
                StringType((explicitElementSize ?: precision)!!, isVarying)
            } else {
                val es = explicitElementSize ?: precision!!
                NumberType(es - decimalPositions, decimalPositions, RpgType.ZONED.rpgType)
            }
        }
        RpgType.PACKED.rpgType -> {
            val elementSize = explicitElementSize ?: (decimalPositions!! + precision!!)
            if (isPackEven) {
                // The PACKEVEN keyword indicates that the packed field or array has an even number of digits.
                // The keyword is only valid for packed program-described data-structure subfields defined using
                // FROM/TO positions.

                // if the PACKEVEN keyword is specified, the numberOfDigits is 2(N-1).
                val numberOfDigits = if (explicitElementSize == null) precision!! else 2 * (elementSize - 1)

                NumberType(numberOfDigits - decimalPositions!!, decimalPositions, rpgCodeType)
            } else {
                // If the PACKEVEN keyword is not specified, the numberOfDigits is 2N - 1;
                val numberOfDigits = if (explicitElementSize == null) precision else 2 * elementSize - 1
                NumberType(numberOfDigits!! - decimalPositions!!, decimalPositions, rpgCodeType)
            }
        }
        RpgType.INTEGER.rpgType, RpgType.UNSIGNED.rpgType -> {
            val elementSize = explicitElementSize ?: (precision!! + decimalPositions!!)
            when (elementSize) {
                1 -> NumberType(3, 0, rpgCodeType)
                2 -> NumberType(5, 0, rpgCodeType)
                4 -> NumberType(10, 0, rpgCodeType)
                8 -> NumberType(19, 0, rpgCodeType)
                else -> NumberType(elementSize - decimalPositions!!, decimalPositions, rpgCodeType)
            }
        }
        RpgType.BINARY.rpgType -> {
            val elementSize = explicitElementSize ?: (precision!! + decimalPositions!!)
            when (elementSize) {
                2, 3, 4 -> NumberType(2, 0, rpgCodeType)
                5, 6, 7, 8 -> NumberType(4, 0, rpgCodeType)
                else -> NumberType(8, 0, rpgCodeType)
            }
        }

        "A" -> {
            CharacterType(precision!!)
        }
        "N" -> BooleanType
        else -> todo("Support RPG code type '$rpgCodeType', field $name", conf = conf)
    }
}

fun RpgParser.Dcl_dsContext.calculateFieldInfos(): FieldsList {
    val caughtErrors = mutableListOf<Throwable>()
    val fieldsList = FieldsList(this.parm_fixed().mapNotNull {
        kotlin.runCatching {
            it.toFieldInfo()
        }.onFailure {
            caughtErrors.add(it)
        }.getOrNull()
    })
    if (caughtErrors.isNotEmpty()) {
        throw caughtErrors[0]
    }
    // The first field, if does not use the overlay directive, starts at offset 0
    if (fieldsList.fields.isNotEmpty()) {
        if (fieldsList.fields.first().overlayInfo == null) {
            fieldsList.fields.first().startOffset = 0
        }
    }
    fieldsList.considerFieldSequence()
    fieldsList.considerOverlays(this.name)
    fieldsList.fields.filter { it.startOffset == null }.let {
        require(it.isEmpty()) { "Start offset not calculated for fields ${it.joinToString(separator = ", ") { it.name }}" }
    }
    fieldsList.fields.filter { it.endOffset == null }.let {
        require(it.isEmpty()) { "End offset not calculated for fields ${it.joinToString(separator = ", ") { it.name }}" }
    }
    return fieldsList
}

private fun RpgParser.Parm_fixedContext.toFieldInfo(conf: ToAstConfiguration = ToAstConfiguration()): FieldInfo {
    var overlayInfo: FieldInfo.OverlayInfo? = null
    val overlay = this.keyword().find { it.keyword_overlay() != null }
    // Set the SORTA order
    val descend = this.keyword().find { it.keyword_descend() != null } != null

    if (overlay != null) {
        this.name
        val pos = overlay.keyword_overlay().pos
        val nameExpr = overlay.keyword_overlay().name
        val targetFieldName = nameExpr.identifier().text
        val isNext = overlay.keyword_overlay().SPLAT_NEXT() != null && overlay.keyword_overlay().SPLAT_NEXT().toString() == "*NEXT"
        val posValue = if (pos != null) (pos.number().toAst() as IntLiteral).value else null
        overlayInfo = FieldInfo.OverlayInfo(targetFieldName, isNext, posValue = posValue)
    }

    var initializationValue: Expression? = null
    val hasInitValue = this.keyword().find { it.keyword_inz() != null }
    if (hasInitValue != null) {
        if (hasInitValue.keyword_inz().simpleExpression() != null) {
            initializationValue = hasInitValue.keyword_inz().simpleExpression()?.toAst(conf) as Expression
        } else {
            // TODO handle initializations for any other variables type (es. 'Z' for timestamp)
            initializationValue = if (null != this.toTypeInfo().decimalPositions) {
                RealLiteral(BigDecimal.ZERO, position = toPosition())
            } else {
                StringLiteral("", position = toPosition())
            }
        }
    }

    val arraySizeDeclared = this.arraySizeDeclared()
    return FieldInfo(this.name, overlayInfo = overlayInfo,
            explicitStartOffset = this.explicitStartOffset(),
            explicitEndOffset = if (explicitStartOffset() != null) this.explicitEndOffset() else null,
            explicitElementType = this.calculateExplicitElementType(arraySizeDeclared, conf),
            arraySizeDeclared = this.arraySizeDeclared(),
            arraySizeDeclaredOnThisField = this.arraySizeDeclared(),
            initializationValue = initializationValue,
            descend = descend,
            position = this.toPosition(conf.considerPosition))
}

fun RpgParser.Dcl_dsContext.declaredSize(): Int? {
    return if (TO_POSITION().text.trim().isNotEmpty()) {
        TO_POSITION().text.asInt()
    } else {
        null
    }
}

class FieldsList(val fields: List<FieldInfo>) {

    fun fieldsOverlayingOn(targetField: FieldInfo): List<FieldInfo> {
        return fields.filter { it.overlayInfo != null && it.overlayInfo.targetFieldName == targetField.name }
    }

    // This should hopefully works because overlays should refer only to previous fields
    private fun considerOverlaysFirstRound(dataDefinitionName: String) {
        val sizeSoFar = HashMap<String, Int>()
        if (fields.isEmpty()) {
            return
        }

        fields.forEach { currFieldInfo ->
            if (currFieldInfo.overlayInfo != null) {

                // The overlay refers to the data definition, for example:
                // D SSFLD                        600
                // D  APPNAME                      02    OVERLAY(SSFLD:1)
                if (currFieldInfo.overlayInfo.targetFieldName != dataDefinitionName) {
                    // The overlay refers to the a data structure field, the offset is relative
                    // D SSFLD                        600
                    // D  OBJTYPE                      30    OVERLAY(SSFLD:*NEXT)
                    // D  OBJTP                        02    OVERLAY(OBJTYPE:1)
                    val targetFieldDefinition: FieldInfo = fields.find { it.name == currFieldInfo.overlayInfo.targetFieldName }
                            ?: throw RuntimeException("Target of overlay not found: ${currFieldInfo.overlayInfo.targetFieldName}")

                    // We should consider that on overlay which does overlay of a field which is an array
                    // become itself an array
                    if (targetFieldDefinition.arraySizeDeclared != null) {
                        if (currFieldInfo.arraySizeDeclared != null) {
                            TODO()
                        } else {
                            currFieldInfo.arraySizeDeclared = targetFieldDefinition.arraySizeDeclared
                            if (currFieldInfo.explicitElementType == null) {
                                TODO()
                            }
                        }
                    }

                    val extraOffset: Int = if (currFieldInfo.overlayInfo.posValue == null) {
                        if (currFieldInfo.overlayInfo.isNext) {
                            sizeSoFar.getOrDefault(targetFieldDefinition.name, 0)
                        } else {
                            0
                        }
                    } else {
                        (currFieldInfo.overlayInfo.posValue - 1).toInt()
                    }
                    // refers to a non overlayed field
                    if (currFieldInfo.startOffset == null) {
                        currFieldInfo.startOffset = (targetFieldDefinition.startOffset ?: throw IllegalStateException("No start offset for ${targetFieldDefinition.name}")) + extraOffset
                    } else {
                        currFieldInfo.startOffset = targetFieldDefinition.startOffset!! + extraOffset
                    }
                    if (currFieldInfo.endOffset == null && currFieldInfo.elementSize != null) {
                        currFieldInfo.endOffset = (currFieldInfo.startOffset!! + currFieldInfo.elementSize!!)
                    }
                    val elementSize = currFieldInfo.toAst().type.elementSize()
                    sizeSoFar[targetFieldDefinition.name] = sizeSoFar.getOrDefault(targetFieldDefinition.name, 0) + elementSize
                }
            }
        }
    }

    private fun considerOverlaysSecondRound(dataDefinitionName: String) {
        // var nextOffset: Long = 0L
        val sizeSoFar = HashMap<String, Int>()
        fields.forEach {

            // Size of the data struct field

            if (it.overlayInfo != null) {

                // The overlay refers to the data definition, for example:
                // D SSFLD                        600
                // D  APPNAME                      02    OVERLAY(SSFLD:1)
                if (it.overlayInfo.targetFieldName == dataDefinitionName) {
                    val extraOffset: Long = if (it.overlayInfo.posValue == null) {
                        // consider *NEXT
                        if (it.overlayInfo.isNext) {
                            sizeSoFar.getOrDefault(it.overlayInfo.targetFieldName, 0).toLong()
                        } else {
                            0
                        }
                    } else {
                        it.overlayInfo.posValue - 1
                    }
                    it.startOffset = extraOffset.toInt()
                    if (it.endOffset == null && it.elementSize != null) {
                        it.endOffset = (it.startOffset!! + it.elementSize!!)
                    }
                    sizeSoFar[it.overlayInfo.targetFieldName] = it.endOffset!!
                }
            }
        }
    }

    private fun determineSizeByOverlayingFields() {
        fields.forEach {
            if (it.explicitElementSize == null) {
                val overlayingFields = this.fieldsOverlayingOn(it)
                check(overlayingFields.isNotEmpty()) { "I cannot calculate the size of ${it.name} from the overlaying fields as there are none" }
                val overlayingFieldsWithoutEndOffset = overlayingFields.filter { it.endOffset == null }
                check(overlayingFieldsWithoutEndOffset.isEmpty()) { "I cannot calculate the size of ${it.name} because it should be determined by the fields overlaying on it, but for some I do not know the end offset. They are: ${overlayingFieldsWithoutEndOffset.joinToString(separator = ", ") { it.name }}" }
                val lastOffset = overlayingFields.map { it.endOffset!! }.maxOrNull()!!
                it.calculatedElementSize = lastOffset

                if (it.explicitElementType == null) {
                    it.calculatedElementType = StringType(it.calculatedElementSize!!, false)
                }
                if (it.endOffset == null) {
                    it.endOffset = (it.startOffset!! + it.elementType.size)
                }
            }
        }
    }

    // This should hopefully works because overlays should refer only to previous fields
    fun considerOverlays(dataDefinitionName: String) {
        this.considerOverlaysFirstRound(dataDefinitionName)
        this.considerOverlaysSecondRound(dataDefinitionName)
        this.determineSizeByOverlayingFields()
    }

    fun considerFieldSequence() {
        fields.forEachIndexed { index, field ->
            if (field.startOffset == null) {
                if (field.overlayInfo == null) {
                    if (index > 0) {
                        val prevField = fields[index - 1]
                        if (prevField.endOffsetIncludingAllElement != null) {
                            field.startOffset = prevField.endOffsetIncludingAllElement
                        }
                    }
                }
            }
            if (field.endOffset == null) {
                if (field.overlayInfo == null) {
                    if (field.startOffset != null && field.elementSize != null) {
                        field.endOffset = (field.startOffset!! + field.elementSize!!)
                    }
                }
            }
        }
    }

    fun isNotEmpty() = fields.isNotEmpty()
}

internal fun RpgParser.Dcl_dsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DataDefinition {
    val initializationValue: Expression? = null
    val size = this.declaredSize()

    // Calculating information about the DS and its fields is full of interdependecies
    // therefore we do that in steps

    val fieldsList = calculateFieldInfos()
    val type: Type = this.type(size, fieldsList, conf)
    val inz = this.keyword().asSequence().firstOrNull { it.keyword_inz() != null }

    val dataDefinition = DataDefinition(
        this.name,
        type,
        fields = fieldsList.fields.map { it.toAst(conf) },
        initializationValue = initializationValue,
        inz = inz != null,
        position = this.toPosition(true))

    // set the "overlayingOn" value for all field definitions
    fieldsList.fields.forEach { fieldInfo ->
        if (fieldInfo.overlayInfo != null) {
            val correspondingFieldDefinition = dataDefinition.fields.find { it.name == fieldInfo.name }!!
            correspondingFieldDefinition.overlayTarget = fieldInfo.overlayInfo.targetFieldName
            dataDefinition.setOverlayOn(correspondingFieldDefinition)
        }
    }
    dataDefinition.fields.forEach { it.parent = dataDefinition }
    return dataDefinition
}

internal fun DataDefinition.setOverlayOn(fieldDefinition: FieldDefinition) {
    fieldDefinition.overlayTarget?.let {
        if (it == this.name) {
            fieldDefinition.overlayingOn = this
        } else {
            fieldDefinition.overlayingOn = this.fields.find { fieldDefinition ->
                fieldDefinition.name == it
            }
        }
    }
}

internal fun RpgParser.Dcl_dsContext.toAstWithLikeDs(
    conf: ToAstConfiguration = ToAstConfiguration(),
    dataDefinitionProviders: List<DataDefinitionProvider>
):
        () -> DataDefinition {
    return {
        if (this.TO_POSITION().text.trim().isNotEmpty()) {
            this.TO_POSITION().text.asInt()
        } else {
            null
        }

        val referrableDataDefinitions = dataDefinitionProviders.filter { it.isReady() }.map { it.toDataDefinition() }

        val likeDsName = (this.keyword().mapNotNull { it.keyword_likeds() }).first().data_structure_name.identifier().free_identifier().idOrKeyword().ID().text
        val referredDataDefinition = referrableDataDefinitions.find { it.name == likeDsName } ?: this.error("Data definition $likeDsName not found", conf = conf)

        val dataDefinition = DataDefinition(
                this.name,
                referredDataDefinition.type,
                referredDataDefinition.fields,
                position = this.toPosition(true))
        dataDefinition.fields = dataDefinition.fields.map { it.copy(overriddenContainer = dataDefinition) }
        dataDefinition
    }
}

internal fun RpgParser.Dcl_dsContext.getKeywordExtName() = this.keyword().first { it.keyword_extname() != null }.keyword_extname()

internal fun RpgParser.Keyword_extnameContext.getExtName() = file_name.text

internal fun RpgParser.Dcl_dsContext.toAstWithExtName(
    conf: ToAstConfiguration = ToAstConfiguration(),
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>
): () -> DataDefinition {
    return {
        val keywordExtName = getKeywordExtName()
        val extName = keywordExtName.getExtName()
        val dataDefinitions = fileDefinitions.filter { it.key.name == extName }.values.flatten()
        if (dataDefinitions.isEmpty()) {
            keywordExtName.error(message = "Datadefinition $extName not found", conf = conf)
        }
        var offset = 0
        val fields = dataDefinitions.map {
            FieldDefinition(
                name = it.name,
                type = it.type,
                explicitStartOffset = offset,
                explicitEndOffset = offset + it.type.size,
                position = toPosition(conf.considerPosition)
            ).apply { offset += type.size }
        }
        val fieldInfos = fields.map {
            FieldInfo(
                name = it.name,
                explicitStartOffset = it.explicitStartOffset,
                explicitEndOffset = it.explicitEndOffset,
                explicitElementType = it.type,
                position = it.position
            )
        }
        val dataDefinition = DataDefinition(
            name = this.name,
            type = type(size = fields.sumBy { it.type.size }, FieldsList(fieldInfos)),
            fields = fields,
            inz = this.keyword().any { it.keyword_inz() != null },
            position = this.toPosition(true)
        )
        dataDefinition
    }
}

fun RpgParser.Parm_fixedContext.explicitStartOffset(): Int? {
    val text = this.FROM_POSITION().text.trim()
    return if (text.isBlank()) {
        null
    } else {
        text.toInt() - 1
    }
}

fun RpgParser.Parm_fixedContext.explicitEndOffset(): Int? {
    val text = this.TO_POSITION().text.trim()
    return if (text.isBlank()) {
        null
    } else {
        text.toInt()
    }
}
