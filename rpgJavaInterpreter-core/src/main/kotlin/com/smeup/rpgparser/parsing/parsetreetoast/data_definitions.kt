package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.AssignableExpression
import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.IntLiteral
import com.smeup.rpgparser.utils.asInt
import com.strumenta.kolasu.mapping.toPosition
import java.lang.RuntimeException
import kotlin.math.max

enum class RpgType(val rpgType: String) {
    PACKED("P"),
    ZONED("S"),
    INTEGER("I"),
    UNSIGNED("U"),
    BINARY("B")
}
private fun RpgParser.Parm_fixedContext.startOffset(): Int {
    val explicitStartOffset = this.explicitStartOffset()
    if (explicitStartOffset != null) {
        return explicitStartOffset
    }
    // TODO consider overlay
    // If it is the first field than it should start at position 0
    return this.prevField()?.endOffset() ?: 0
}

private fun RpgParser.Parm_fixedContext.prevField(): RpgParser.Parm_fixedContext? {
    val fields = (this.parent as RpgParser.Dcl_dsContext).fieldLines()
    val index = fields.indexOf(this)
    if (index == 0) {
        return null
    }
    return fields[index - 1]
}

private fun RpgParser.Parm_fixedContext.endOffset(): Int {
    val explicitEndOffset = if (this.explicitStartOffset() != null) this.explicitEndOffset() else null
    if (explicitEndOffset != null) {
        return explicitEndOffset
    }
    return startOffset() + this.toType().size.toInt()
}

private fun inferDsSizeFromFieldLines(fieldLines: List<RpgParser.Parm_fixedContext>): Int {
    require(fieldLines.isNotEmpty())
    var maxEnd = 0
    fieldLines.forEach {
        val end = it.endOffset()
        maxEnd = max(maxEnd, end)
    }
    return maxEnd
}

fun RpgParser.Dcl_dsContext.elementSizeOf(): Int {
    var toPosition = ""
    toPosition = if (this.nameIsInFirstLine) {
        this.TO_POSITION().text
    } else {
        val header = this.parm_fixed().first()
        header.TO_POSITION().text
    }
    return if (toPosition.isBlank()) {
        // The element size has to be calculated, as it is not explicitly specified
        val fieldLines = this.fieldLines()

        // return fieldTypes.map { it.type. }
        inferDsSizeFromFieldLines(fieldLines)
    } else {
        toPosition.trim().toInt()
    }
}

internal fun RpgParser.Fspec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): FileDefinition {
    val fileDefinition = FileDefinition(
            this.FS_RecordName().text.trim(),
            position = this.toPosition(true))
    val rename = this.fs_keyword().mapNotNull { it.keyword_rename() }
    if (rename.isNotEmpty()) {
        // TODO Should we evaluate rename[0].int_format ???
        val internalRecordFormatName = rename[0].int_format.text
        fileDefinition.internalFormatName = internalRecordFormatName
    }
    return fileDefinition
}

internal fun RpgParser.DspecContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DataDefinition {
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

    this.keyword().forEach {
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
    }
    val elementSize = when {
        like != null -> conf.compileTimeInterpreter.evaluateElementSizeOf(this.rContext(), like!!)
        else -> this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    }

    val baseType = when (this.DATA_TYPE()?.text?.trim()?.toUpperCase()) {
        null -> TODO()
        "" -> if (this.DECIMAL_POSITIONS().text.isNotBlank()) {
            /* TODO should be packed? */
            NumberType(elementSize!! - decimalPositions, decimalPositions)
        } else {
            StringType(elementSize!!.toLong())
        }
        "A" -> StringType(elementSize!!.toLong())
        "N" -> BooleanType
        "Z" -> TimeStampType
        /* TODO should be zoned? */
        RpgType.ZONED.rpgType -> {
            // StringType(elementSize!!.toLong())
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
                compileTimeRecordsPerLine = conf.compileTimeInterpreter.evaluate(this.rContext(), elementsPerLineExpression!!).asInt().value.toInt()
            } else {
                compileTimeRecordsPerLine = 1
            }
            require(compileTimeRecordsPerLine > 0)
        }
        ArrayType(baseType, conf.compileTimeInterpreter.evaluate(this.rContext(), dim!!).asInt().value.toInt(), compileTimeRecordsPerLine)
    } else {
        baseType
    }
    return DataDefinition(
            this.ds_name().text,
            type,
            initializationValue = initializationValue,
            position = this.toPosition(true))
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
            require(this.parm_fixed().isNotEmpty())
            val header = this.parm_fixed().first()
            header.ds_name().text
        }
    }

val RpgParser.Dcl_dsContext.hasHeader: Boolean
    get() {
        return this.ds_name().text.trim().isEmpty()
    }

fun RpgParser.Dcl_dsContext.fieldLines(): List<RpgParser.Parm_fixedContext> {
    return this.parm_fixed().drop(if (nameIsInFirstLine) 0 else 1)
}

fun RpgParser.Dcl_dsContext.type(size: Int? = null, conf: ToAstConfiguration = ToAstConfiguration()): Type {
    val keywords = if (this.parm_fixed().isEmpty()) {
        this.keyword()
    } else {
        this.parm_fixed().first().keyword()
    }
    val dim: Expression? = keywords.asSequence().mapNotNull { it.keyword_dim()?.simpleExpression()?.toAst(conf) }.firstOrNull()
    val nElements = if (dim != null) conf.compileTimeInterpreter.evaluate(this.rContext(), dim).asInt().value.toInt() else null
    val others = this.fieldLines()
    val fieldTypes: List<FieldType> = others.map { it.toFieldType() }
    val elementSize = this.elementSizeOf()
    val baseType = DataStructureType(fieldTypes, size ?: elementSize)
    return if (nElements == null) {
        baseType
    } else {
        ArrayType(baseType, nElements)
    }
}

internal fun RpgParser.Dcl_dsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DataDefinition {
    val size = if (this.TO_POSITION().text.trim().isNotEmpty()) {
        this.TO_POSITION().text.asInt()
    } else {
        null
    }

    val others = this.parm_fixed().drop(if (this.hasHeader) 1 else 0)
    val type: Type = this.type(size)
    val nElements = if (type is ArrayType) {
        type.nElements
    } else {
        null
    }

    val dataDefinition = DataDefinition(
            this.name,
            type,
            fields = others.map { it.toAst(nElements, conf) },
            position = this.toPosition(true))
    considerOverlays(dataDefinition, others)
    return dataDefinition
}

internal fun RpgParser.Dcl_dsContext.toAstWithLikeDs(conf: ToAstConfiguration = ToAstConfiguration(),
                                                     dataDefinitionProviders: List<DataDefinitionProvider>):
        () -> DataDefinition {
    return {
        val size = if (this.TO_POSITION().text.trim().isNotEmpty()) {
            this.TO_POSITION().text.asInt()
        } else {
            null
        }

        val others = this.parm_fixed().drop(if (this.hasHeader) 1 else 0)

        val referrableDataDefinitions = dataDefinitionProviders.filter { it.isReady() }.map { it.toDataDefinition() }

        val likeDsName = (this.keyword().mapNotNull { it.keyword_likeds() }).first().data_structure_name.identifier().free_identifier().idOrKeyword().ID().text
        val referredDataDefinition = referrableDataDefinitions.find { it.name == likeDsName } ?: throw RuntimeException("Data definition $likeDsName not found")

        val dataDefinition = DataDefinition(
                this.name,
                referredDataDefinition.type,
                referredDataDefinition.fields,
                position = this.toPosition(true))
        dataDefinition.fields = dataDefinition.fields.map { it.copy(overriddenContainer = dataDefinition) }
        dataDefinition
    }
}

// This should hopefully works because overlays should refer only to previous fields
private fun considerOverlays(dataDefinition: DataDefinition, fieldsParseTrees: List<RpgParser.Parm_fixedContext>) {
    var nextOffset : Long = 0L
    dataDefinition.fields.forEach { it.parent = dataDefinition }
    fieldsParseTrees.forEach {

        // Size of the data struct field
        val elementSize = it.toAst(0).type.elementSize()

        // Detects if the OVERALY keyword is present
        val overlay = it.keyword().find { it.keyword_overlay() != null }
        if (overlay != null) {
            val fieldName = it.ds_name().text
            val pos = overlay.keyword_overlay().pos
            val nameExpr = overlay.keyword_overlay().name
            val targetFieldName = nameExpr.identifier().text


            // The overlay refers to the data definition, for example:
            //D SSFLD                        600
            //D  APPNAME                      02    OVERLAY(SSFLD:1)
            if( targetFieldName == dataDefinition.name) {
                val extraOffset = if (pos == null) {
                    // consider *NEXT
                    if( overlay.keyword_overlay().SPLAT_NEXT() != null && overlay.keyword_overlay().SPLAT_NEXT().toString() == "*NEXT" ) {
                        nextOffset
                    } else {
                        0
                    }

                } else {
                    // pos
                    val posValue = (pos.number().toAst() as IntLiteral).value
                    posValue - 1
                }
                val thisFieldDefinition = dataDefinition.fields.find { it.name == fieldName }
                    ?: throw RuntimeException("User of overlay not found: $fieldName")
                thisFieldDefinition.explicitStartOffset =   extraOffset.toInt()

            } else {
                // The overlay refers to the a data structure field, the offset is relative
                //D SSFLD                        600
                //D  OBJTYPE                      30    OVERLAY(SSFLD:*NEXT)
                //D  OBJTP                        02    OVERLAY(OBJTYPE:1)
                val targetFieldDefinition = dataDefinition.fields.find { it.name == targetFieldName }
                        ?: throw RuntimeException("Target of overlay not found: $targetFieldName")
                val thisFieldDefinition = dataDefinition.fields.find { it.name == fieldName }
                        ?: throw RuntimeException("User of overlay not found: $fieldName")


                val extraOffset: Int = if (pos == null) {
                    if( overlay.keyword_overlay().SPLAT_NEXT() != null && overlay.keyword_overlay().SPLAT_NEXT().toString() == "*NEXT" ) {
                        targetFieldDefinition.nextOffset
                    } else {
                        0
                    }

                } else {
                    val posValue = (pos.number().toAst() as IntLiteral).value
                    (posValue - 1).toInt()
                }
                    // refers to a non overlayed field
                    if( thisFieldDefinition.explicitStartOffset == null) {
                        thisFieldDefinition.explicitStartOffset = targetFieldDefinition.startOffset + extraOffset
                    } else {
                        thisFieldDefinition.explicitStartOffset = targetFieldDefinition.explicitStartOffset!! + extraOffset
                    }
                    targetFieldDefinition.nextOffset += elementSize.toInt()
                }

        }
        // updates the *NEXT offset
        nextOffset += elementSize

    }
}

fun RpgParser.Parm_fixedContext.explicitStartOffset(): Int? {
    val text = this.FROM_POSITION().text.trim()
    return if (text.isBlank()) {
        null
    } else {
        text.toInt()
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

internal fun RpgParser.Parm_fixedContext.toAst(
    nElements: Int?,
    conf: ToAstConfiguration = ToAstConfiguration()
): FieldDefinition {
    var baseType = this.toType()
    if (nElements != null) {
        baseType = ArrayType(baseType, nElements)
    }
    return FieldDefinition(this.ds_name().text,
            baseType,
            explicitStartOffset = this.explicitStartOffset(),
            explicitEndOffset = if (explicitStartOffset() != null) this.explicitEndOffset() else null,
            position = this.toPosition(conf.considerPosition))
}

internal fun RpgParser.Parm_fixedContext.toType(): Type {
    val startPosition = this.explicitStartOffset()
    val endPosition = this.explicitEndOffset()
    val elementSize = when {
        startPosition == null -> endPosition
        endPosition == null -> endPosition
        else -> endPosition - startPosition.toInt() + 1
    }

    return when (DATA_TYPE()?.text?.trim()) {
        null -> TODO()
        "", RpgType.PACKED.rpgType,RpgType.ZONED.rpgType,RpgType.INTEGER.rpgType,RpgType.UNSIGNED.rpgType,RpgType.BINARY.rpgType -> if (DECIMAL_POSITIONS().text.isNotBlank()) {
            val rpgType = DATA_TYPE()?.text?.trim()
            val decimalPositions = with(DECIMAL_POSITIONS().text.trim()) { if (isEmpty()) 0 else toInt() }
            NumberType(elementSize!! - decimalPositions, decimalPositions,rpgType)
        } else {
            StringType(elementSize?.toLong()
                    ?: throw RuntimeException("The string has no specified length"))
        }
        "N" -> BooleanType
        "A" -> CharacterType(elementSize!!)
        else -> throw UnsupportedOperationException("<${DATA_TYPE().text}>")
    }
}

internal fun RpgParser.Parm_fixedContext.toFieldType(): FieldType {
    return FieldType(this.ds_name().text, this.toType())
}
