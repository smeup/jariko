package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.AssignableExpression
import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.IntLiteral
import com.smeup.rpgparser.utils.asInt
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import java.lang.Exception
import java.lang.IllegalStateException
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

private fun RpgParser.Parm_fixedContext.endOffset(): Int? {
    // The end offset is really an enf offset only when the value is specified
    // also for the start offset, otherwise it has another meaning
    return if (explicitStartOffset() != null) explicitEndOffset() else null
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
    var toPosition = if (this.nameIsInFirstLine) {
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

internal fun RpgParser.Dcl_dsContext.type(
    size: Int? = null,
    fieldsList: FieldsList,
    conf: ToAstConfiguration = ToAstConfiguration()
): Type {
    val keywords = if (this.parm_fixed().isEmpty()) {
        this.keyword()
    } else {
        this.parm_fixed().first().keyword()
    }
    val dim: Expression? = keywords.asSequence().mapNotNull { it.keyword_dim()?.simpleExpression()?.toAst(conf) }.firstOrNull()
    val nElements = if (dim != null) conf.compileTimeInterpreter.evaluate(this.rContext(), dim).asInt().value.toInt() else null
    val fieldTypes: List<FieldType> = fieldsList.fields.map { it.toFieldType(fieldsList) }
    val elementSize = this.elementSizeOf(fieldsList)
    val baseType = DataStructureType(fieldTypes, size ?: elementSize)
    return if (nElements == null) {
        baseType
    } else {
        ArrayType(baseType, nElements)
    }
}

private val RpgParser.Parm_fixedContext.name: String
    get() = this.ds_name().text

private fun RpgParser.Parm_fixedContext.isOverlayingOn(name: String): Boolean {
    if (this.name == name) {
        return false
    }
    val keywordOverlay = this.keyword().mapNotNull { it.keyword_overlay() }.firstOrNull()
    return keywordOverlay?.name?.text?.equals(name, ignoreCase = true) ?: false
}

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
    val arraySizeDeclared: Int? = null,
    // This can be set when the type permits to get the element size
    // For example, here it is possible:
    // D  FI07                         15  3 OVERLAY(AR01:*NEXT)
    // While here it is not:
    // D AR01                                DIM(100) ASCEND
    val explicitElementType: Type? = null,
    val initializationValue: Expression? = null,
    val position: Position?
) {

    var startOffset: Int? = explicitStartOffset // these are mutable as they can be calculated using next
    var endOffset: Int? = explicitEndOffset // these are mutable as they can be calculated using next

    var calculatedElementSize: Long? = null
    var calculatedElementType: Type? = null

    val elementSize: Long?
        get() = explicitElementSize ?: calculatedElementSize

    val explicitElementSize: Long?
        get() = explicitElementType?.size

    val elementType: Type
        get() {
            return explicitElementType ?: (calculatedElementType ?: throw IllegalStateException("No element type available for $name"))
        }

    fun type(): Type {
        return if (arraySizeDeclared == null) {
            elementType
        } else {
            ArrayType(elementType, arraySizeDeclared)
        }
    }

    data class OverlayInfo(val targetFieldName: String, val isNext: Boolean, val posValue: Long?)

    fun toFieldType(fieldsList: FieldsList): FieldType {
        return FieldType(name, type())
    }

    fun toAst(nElements: Int?, fieldsList: FieldsList, conf: ToAstConfiguration = ToAstConfiguration()): FieldDefinition {
        val baseType = type()
        val type = if (nElements != null) {
            ArrayType(baseType, nElements)
        } else {
            baseType
        }

        return FieldDefinition(this.name,
                type,
                explicitStartOffset = this.explicitStartOffset,
                explicitEndOffset = if (explicitStartOffset != null) this.explicitEndOffset else null,
                calculatedStartOffset = if (this.explicitStartOffset != null) null else this.startOffset,
                calculatedEndOffset = if (this.explicitEndOffset != null) null else this.endOffset,
                initializationValue = this.initializationValue,
                position = if (conf.considerPosition) this.position else null)
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
    val fp = this.FROM_POSITION()
    val foo = this.TO_POSITION()
    return TypeInfo(
            stringCode = DATA_TYPE()?.text?.trim(),
            integerPositions = if (TO_POSITION().text.isNotBlank()) TO_POSITION().text.trim().toInt() else null,
            decimalPositions = if (DECIMAL_POSITIONS().text.isNotBlank()) with(DECIMAL_POSITIONS().text.trim()) { if (isEmpty()) 0 else toInt() } else null,
            isPackEven = keyword().any { it.keyword_packeven() != null }
    )
}

internal fun RpgParser.Parm_fixedContext.calculateExplicitElementType(): Type? {
    val rpgCodeType = DATA_TYPE()?.text?.trim() ?: RpgType.ZONED.rpgType
    val precision = if (TO_POSITION().text.isNotBlank()) TO_POSITION().text.trim().toInt() else null
    val decimalPositions = if (DECIMAL_POSITIONS().text.isNotBlank()) with(DECIMAL_POSITIONS().text.trim()) { if (isEmpty()) 0 else toInt() } else null
    val isPackEven = keyword().any { it.keyword_packeven() != null }
    val startPosition = this.explicitStartOffset()
    val endPosition = this.explicitEndOffset()
    val explicitElementSize = when {
        startPosition == null -> null
        endPosition == null -> endPosition
        else -> endPosition - startPosition.toInt()
    }

    return when (rpgCodeType) {
        "", RpgType.ZONED.rpgType -> {
            if (decimalPositions == null && precision == null) {
                null
            } else if (decimalPositions == null) {
                StringType((explicitElementSize ?: precision)!!.toLong())
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
                val numberOfDigits = if (explicitElementSize == null) precision!! else 2 * (elementSize!! - 1)

                NumberType(numberOfDigits!! - decimalPositions!!, decimalPositions, rpgCodeType)
            } else {
                // If the PACKEVEN keyword is not specified, the numberOfDigits is 2N - 1;
                val numberOfDigits = if (explicitElementSize == null) precision else 2 * elementSize!! - 1
                NumberType(numberOfDigits!! - decimalPositions!!, decimalPositions, rpgCodeType)
            }
        }
        RpgType.INTEGER.rpgType, RpgType.UNSIGNED.rpgType, RpgType.BINARY.rpgType -> {
            val elementSize = explicitElementSize ?: (precision!! + decimalPositions!!)
            NumberType(elementSize - decimalPositions!!, decimalPositions!!, rpgCodeType)
        }

        "A" -> {
            CharacterType(precision!!)
        }
        "N" -> BooleanType
        else -> TODO("Support RPG code type '$rpgCodeType', field $name")
    }
}

fun RpgParser.Dcl_dsContext.calculateFieldInfos(): FieldsList {
    val others = this.parm_fixed().drop(if (this.hasHeader) 1 else 0)
    val fieldsList = FieldsList(others.map { it.toFieldInfo() })

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
    try {

        var overlayInfo: FieldInfo.OverlayInfo? = null
        val overlay = this.keyword().find { it.keyword_overlay() != null }
        if (overlay != null) {
            val fieldName = this.name
            val pos = overlay.keyword_overlay().pos
            val nameExpr = overlay.keyword_overlay().name
            val targetFieldName = nameExpr.identifier().text
            val isNext = overlay.keyword_overlay().SPLAT_NEXT() != null && overlay.keyword_overlay().SPLAT_NEXT().toString() == "*NEXT"
            val posValue = if (pos != null) (pos.number().toAst() as IntLiteral).value else null
            overlayInfo = FieldInfo.OverlayInfo(targetFieldName, isNext, posValue = posValue)
        }

        var initializationValue: Expression? = null
        var hasInitValue = this.keyword().find { it.keyword_inz() != null }
        if(hasInitValue != null) {
            initializationValue = hasInitValue.keyword_inz().simpleExpression()?.toAst(conf)  as Expression
        }

        return FieldInfo(this.name, overlayInfo = overlayInfo,
                explicitStartOffset = this.explicitStartOffset(),
                explicitEndOffset = if (explicitStartOffset() != null) this.explicitEndOffset() else null,
                explicitElementType = this.calculateExplicitElementType(),
                arraySizeDeclared = this.arraySizeDeclared(),
                initializationValue = initializationValue,
                position = this.toPosition(conf.considerPosition))
    } catch (e: Exception) {
        throw RuntimeException("Problem arose converting to AST field ${this.name}", e)
    }
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
                    val targetFieldDefinition = fields.find { it.name == currFieldInfo.overlayInfo!!.targetFieldName }
                            ?: throw RuntimeException("Target of overlay not found: ${currFieldInfo.overlayInfo.targetFieldName}")

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
                        currFieldInfo.endOffset = (currFieldInfo.startOffset!! + currFieldInfo.elementSize!!).toInt()
                    }
                    // TODO this toAst causes issues in case of overlays
                    val elementSize = currFieldInfo.toAst(0, this).type.elementSize()
                    sizeSoFar[targetFieldDefinition.name] = sizeSoFar.getOrDefault(targetFieldDefinition.name, 0) + elementSize.toInt()
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
                        it.endOffset = (it.startOffset!! + it.elementSize!!).toInt()
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
                val lastOffset = overlayingFields.map { it.endOffset!! }.max()!!.toLong()
                it.calculatedElementSize = lastOffset

                if (it.explicitElementType == null) {
                    it.calculatedElementType = StringType(it.calculatedElementSize!!)
                }
                if (it.endOffset == null) {
                    it.endOffset = (it.startOffset!! + it.elementType.size).toInt()
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
                        if (prevField.endOffset != null) {
                            field.startOffset = prevField.endOffset
                        }
                    }
                }
            }
            if (field.endOffset == null) {
                if (field.overlayInfo == null) {
                    if (field.startOffset != null && field.elementSize != null) {
                        field.endOffset = (field.startOffset!! + field.elementSize!!).toInt()
                    }
                }
            }
        }
    }

    fun isNotEmpty() = fields.isNotEmpty()
}

internal fun RpgParser.Dcl_dsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DataDefinition {
    var initializationValue : Expression? = null
    val size = this.declaredSize()

    // Calculating information about the DS and its fields is full of interdependecies
    // therefore we do that in steps

    val fieldsList = calculateFieldInfos()
    val type: Type = this.type(size, fieldsList, conf)
    val nElements = if (type is ArrayType) {
        type.nElements
    } else {
        null
    }
    // If the name of the DS is not provided, it takes the first field name
    if(this.hasHeader) {
        var hasInitValue = this.parm_fixed().first().keyword().find { it.keyword_inz() != null }
        if(hasInitValue != null) {
            initializationValue = hasInitValue.keyword_inz().simpleExpression()?.toAst(conf)  as Expression
        }
    }
    val dataDefinition = DataDefinition(
            this.name,
            type,
            fields = fieldsList.fields.map { it.toAst(nElements, fieldsList, conf) },
            initializationValue = initializationValue,
            position = this.toPosition(true))
    dataDefinition.fields.forEach { it.parent = dataDefinition }
    return dataDefinition
}

internal fun RpgParser.Dcl_dsContext.toAstWithLikeDs(
    conf: ToAstConfiguration = ToAstConfiguration(),
    dataDefinitionProviders: List<DataDefinitionProvider>
):
        () -> DataDefinition {
    return {
        val size = if (this.TO_POSITION().text.trim().isNotEmpty()) {
            this.TO_POSITION().text.asInt()
        } else {
            null
        }


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
