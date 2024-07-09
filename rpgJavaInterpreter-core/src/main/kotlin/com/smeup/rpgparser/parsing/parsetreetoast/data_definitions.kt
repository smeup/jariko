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
import java.util.Date
import kotlin.collections.HashMap
import kotlin.math.max

enum class RpgType(val rpgType: String) {
    CHARACTER("A"),
    BOOLEAN("N"),
    DATE("D"),
    TIMESTAMP("Z"),
    PACKED("P"),
    ZONED("S"),
    INTEGER("I"),
    UNSIGNED("U"),
    BINARY("B"),
    UNLIMITED_STRING("0"),
    POINTER("*")
}

/**
 * See https://www.ibm.com/docs/en/i/7.5?topic=formats-date-data-type.
 */
enum class DateFormat(val dateFormat: String) {
    JUL("*JUL"),
    ISO("*ISO")
    // TODO: Add more
}

internal enum class DSFieldInitKeywordType(val keyword: String, val type: Type) {
    STATUS("*STATUS", NumberType(entireDigits = 5, decimalDigits = 0, rpgType = RpgType.ZONED)),
    PARMS("*PARMS", NumberType(entireDigits = 3, decimalDigits = 0, rpgType = RpgType.ZONED));
}

internal data class DSFieldInitKeyword(val position: Position?, val dsFieldInitKeywordType: DSFieldInitKeywordType) {

    internal fun toAst(): Expression {
        return when (dsFieldInitKeywordType) {
            DSFieldInitKeywordType.PARMS -> ParmsExpr(name = DSFieldInitKeywordType.PARMS.keyword, position = position)
            DSFieldInitKeywordType.STATUS -> StatusExpr(position = position)
        }
    }
}

private fun RpgParser.Parm_fixedContext.toDSFieldInitKeyword(conf: ToAstConfiguration): DSFieldInitKeyword? {
    val fromPositionTest = FROM_POSITION().text.trim()
    val position = toPosition(conf.considerPosition)
    return DSFieldInitKeywordType.values()
        .firstOrNull { dsFieldInitKeyword -> dsFieldInitKeyword.keyword.equals(fromPositionTest, ignoreCase = true) }
        ?.let { DSFieldInitKeyword(position = position, dsFieldInitKeywordType = it) }
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

internal fun RpgParser.Dcl_dsContext.elementSizeOf(
    knownDataDefinitions: Collection<DataDefinition>,
    fields: List<FieldInfo> = emptyList()
): Int {
    val fieldsList = this.calculateFieldInfos(knownDataDefinitions, fields)
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
        prefix = prefix,
        fileType = FileType.getByKeyword(this.FS_Type().text.trim())
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

internal fun RpgParser.Dcl_dsContext.toAstWithParameters(conf: ToAstConfiguration = ToAstConfiguration()): FileDefinition {
    val prefixContexts = this.keyword().mapNotNull { it.keyword_prefix() }
    val prefix: Prefix? = if (prefixContexts.isNotEmpty()) {
        Prefix(
            prefix = prefixContexts[0].prefix.text,
            numCharsReplaced = prefixContexts[0].nbr_of_char_replaced?.text?.toInt()
        )
    } else {
        null
    }
    val extNameContexts = this.keyword().mapNotNull { it.keyword_extname() }.firstOrNull()
    val fileDefinition = FileDefinition(
        name = extNameContexts?.getExtName() ?: "",
        position = toPosition(conf.considerPosition),
        justExtName = true,
        prefix = prefix
    )
    return fileDefinition
}

private val RpgParser.Parm_fixedContext.decimalPositions
    get() = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() }

internal fun RpgParser.Parm_fixedContext.toAst(
    conf: ToAstConfiguration = ToAstConfiguration(),
    knownDataDefinitions: List<DataDefinition>
): DataDefinition {
    val compileTimeInterpreter = InjectableCompileTimeInterpreter(
        knownDataDefinitions = knownDataDefinitions,
        delegatedCompileTimeInterpreter = conf.compileTimeInterpreter
    )

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
            compileTimeInterpreter.evaluateElementSizeOf(this.rContext(), like!!, conf, null)
        }
        else -> this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    }

    val baseType =
        when (this.DATA_TYPE()?.text?.trim()?.uppercase()) {
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
            RpgType.CHARACTER.rpgType -> StringType(elementSize!!, varying)
            RpgType.BOOLEAN.rpgType -> BooleanType
            RpgType.TIMESTAMP.rpgType -> TimeStampType
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
    knownDataDefinitions: List<DataDefinition>,
    parentDataDefinitions: List<DataDefinition>? = null,
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>? = null,
    procedureName: String? = null
): DataDefinition {
    if (dspecConstant() != null) return dspecConstant().toAst(conf = conf)
    val compileTimeInterpreter = InjectableCompileTimeInterpreter(
        knownDataDefinitions = knownDataDefinitions,
        // If we have a parent data definition we can use it to resolve the variable through the delegate
        delegatedCompileTimeInterpreter = parentDataDefinitions?.let {
            InjectableCompileTimeInterpreter(
                knownDataDefinitions = it,
                fileDefinitions = fileDefinitions,
                delegatedCompileTimeInterpreter = conf.compileTimeInterpreter
            )
        } ?: conf.compileTimeInterpreter,
        fileDefinitions = fileDefinitions
    )
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
    //    0 UnlimitedString (smeup reserved)

    var like: AssignableExpression? = null
    var dim: Expression? = null
    var initializationValue: Expression? = null
    var elementsPerLineExpression: Expression? = null
    var compileTimeArray = false
    var varying = false
    var ascend: Boolean? = null
    var static = false

    /* Default value is ISO. */
    var dateFormat: DateFormat = DateFormat.ISO

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
        it.keyword_static()?.let {
            static = true
        }
        it.keyword_datfmt()?.let {
            dateFormat = when (it.simpleExpression()?.toAst(conf)) {
                is IsoFormatExpr -> DateFormat.ISO
                is JulFormatExpr -> DateFormat.JUL
                else -> this.todo(message = "${it.simpleExpression().text} like Date format", conf = conf)
            }
        }
    }

    val elementSize = when {
        like != null -> {
            compileTimeInterpreter.evaluateElementSizeOf(this.rContext(), like!!, conf, procedureName)
        }
        else -> this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    }

    val baseType =
        when (this.DATA_TYPE()?.text?.trim()?.uppercase()) {
            null -> todo(conf = conf)
            "" -> if (this.DECIMAL_POSITIONS().text.isNotBlank()) {
                /* TODO should be packed? */
                NumberType(elementSize!! - decimalPositions, decimalPositions)
            } else {
                if (like != null) {
                    compileTimeInterpreter.evaluateTypeOf(this.rContext(), like!!, conf, procedureName)
                } else {
                    StringType.createInstance(elementSize!!, varying)
                }
            }
            RpgType.CHARACTER.rpgType -> StringType(elementSize!!, varying)
            RpgType.BOOLEAN.rpgType -> BooleanType
            RpgType.TIMESTAMP.rpgType -> TimeStampType
            RpgType.DATE.rpgType -> {
                val type = DateType(dateFormat)

                if (initializationValue != null) {
                    if (!(initializationValue as StringLiteral).value.matches(Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}"))) error(message = "Initialization value is incorrect. Must be 'YYYY-MM-DD'", conf = conf)
                    val dateInzSplit = (initializationValue as StringLiteral).value.split("-").map { it.toInt() }
                    val dateInz = Date(dateInzSplit[0] - 1900, dateInzSplit[1] - 1, dateInzSplit[2])
                    initializationValue = IntLiteral(
                        value = dateInz.time,
                        position = initializationValue?.position
                    )

                    /*
                     * Every date format has a valid range:
                     *  - JUL, between 1940 and 2039;
                     *  - ISO, between 0001 and 9999.
                     * For more information, or if you want to add another format, see: https://www.ibm.com/docs/en/i/7.5?topic=formats-date-data-type
                     */
                    when (type.format) {
                        DateFormat.JUL -> if (
                            !dateInz.after(Date(1939 - 1900, 11, 31)) || !dateInz.before(Date(2040 - 1900, 0, 1))
                        ) error(message = "For JUL format the date must be between 1940 and 2039", conf = conf)
                        DateFormat.ISO -> if (
                            !dateInz.after(Date(-1900, 11, 31)) || !dateInz.before(Date(9999 - 1900, 0, 1))
                        ) error(message = "For ISO format the date must be between 0001 and 9999", conf = conf)
                    }
                }

                DateType(dateFormat)
            }
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
            RpgType.UNLIMITED_STRING.rpgType -> {
                UnlimitedStringType
            }
            RpgType.POINTER.rpgType -> {
                NumberType(NumberType.MAX_INTEGER_DIGITS, NumberType.INTEGER_DECIMAL_DIGITS, RpgType.POINTER.rpgType)
            }
            else -> todo("Unknown type: <${this.DATA_TYPE().text}>", conf)
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
            val el = compileTimeInterpreter.evaluate(this.rContext(), dim!!).asInt().value.toInt()
            (baseType as ArrayType).copy(nElements = el)
        }
    } else {
        baseType
    }
    return DataDefinition(
        name = this.ds_name().text,
        type = type,
        initializationValue = initializationValue,
        position = this.toPosition(true),
        static = static
    )
}

internal fun RpgParser.DspecConstantContext.toAst(
    conf: ToAstConfiguration = ToAstConfiguration()
): DataDefinition {
    val initializationValue = this.number().toAst(conf)
    val type = initializationValue.type()

    return DataDefinition(
            this.ds_name().text,
            type,
            initializationValue = initializationValue,
            position = this.toPosition(true))
}

internal fun RpgParser.Dcl_cContext.toAst(
    conf: ToAstConfiguration = ToAstConfiguration()
): DataDefinition {
    val initializationValueExpression = this.keyword_const()?.simpleExpression()?.toAst(conf) ?: this.literal().toAst(conf)
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
    val occurs: Int? = keywords.asSequence().mapNotNull { it.keyword_occurs()?.evaluate(conf) }.firstOrNull()
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
            ?: throw CannotRetrieveDataStructureElementSizeException("No explicit size and no fields in DS ${this.name}, so we cannot calculate the element size")
    val dataStructureType = DataStructureType(fields = fieldTypes, elementSize = size ?: elementSize)
    val baseType = occurs?.let {
        OccurableDataStructureType(dataStructureType = dataStructureType, occurs = occurs)
    } ?: dataStructureType
    return if (nElements == null) {
        baseType
    } else {
        ArrayType(baseType, nElements)
    }
}

/**
 * Evaluates the OCCURS keyword between a number or an expression to evaluate at runtime.
 */
internal fun RpgParser.Keyword_occursContext.evaluate(conf: ToAstConfiguration = ToAstConfiguration()): Int? {
    return when {
        this.numeric_constant != null -> numeric_constant?.getChild(0)?.text?.toInt()
        this.expr != null -> {
            val injectableCompileTimeInterpreter = InjectableCompileTimeInterpreter(
                knownDataDefinitions = KnownDataDefinition.getInstance().values.toList(),
                delegatedCompileTimeInterpreter = conf.compileTimeInterpreter
            )
            injectableCompileTimeInterpreter.evaluate(rContext(), this.expr.toAst(conf)).asInt().value.toInt()
        }
        else -> null
    }
}

internal class CannotRetrieveDataStructureElementSizeException(override val message: String) : IllegalStateException(message)

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

internal fun RpgParser.Parm_fixedContext.arraySizeDeclared(conf: ToAstConfiguration): Int? {
    if (this.keyword().any { it.keyword_dim() != null }) {
        val compileTimeInterpreter = InjectableCompileTimeInterpreter(
            knownDataDefinitions = KnownDataDefinition.getInstance().values.toList(),
            delegatedCompileTimeInterpreter = conf.compileTimeInterpreter
        )
        val dims = this.keyword().mapNotNull { it.keyword_dim() }
        require(dims.size == 1)
        val dim = dims[0]
        return compileTimeInterpreter.evaluate(this.rContext(), dim.simpleExpression().toAst(conf))
            .asInt().value.toInt()
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
            it / arraySizeDeclared(conf)!!
        }
    } else {
        totalSize
    }

    val dsFieldInitKeyword = toDSFieldInitKeyword(conf)

    return when (rpgCodeType) {
        "", RpgType.ZONED.rpgType -> {
            if (dsFieldInitKeyword != null) {
                return dsFieldInitKeyword.dsFieldInitKeywordType.type
            }
            if (decimalPositions == null && precision == null) {
                null
            } else if (decimalPositions == null) {
                StringType.createInstance((explicitElementSize ?: precision)!!, isVarying)
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
        RpgType.CHARACTER.rpgType -> {
            CharacterType(precision!!)
        }
        RpgType.BOOLEAN.rpgType -> BooleanType
        RpgType.UNLIMITED_STRING.rpgType -> UnlimitedStringType
        RpgType.TIMESTAMP.rpgType -> TimeStampType
        else -> todo("Support RPG code type '$rpgCodeType', field $name", conf = conf)
    }
}

/**
 * Prepares an object FieldsList with all calculated fields, that means
 *  sequences and offsets.
 * @param knownDataDefinitions pre calculated data definitions necessary, in example, to resolve LIKE.
 * @param fieldsExtname got previously if the DS uses EXTNAME.
 */
internal fun RpgParser.Dcl_dsContext.calculateFieldInfos(
    knownDataDefinitions: Collection<DataDefinition>,
    fieldsExtname: List<FieldInfo> = listOf()
): FieldsList {
    val caughtErrors = mutableListOf<Throwable>()
    val fieldsList = FieldsList(fieldsExtname + this.parm_fixed().mapNotNull {
        kotlin.runCatching {
            it.toFieldInfo(knownDataDefinitions = knownDataDefinitions)
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
    fieldsList.considerNotInOverlayFields()
    fieldsList.fields.filter { it.startOffset == null }.let {
        require(it.isEmpty()) { "Start offset not calculated for fields ${it.joinToString(separator = ", ") { it.name }}" }
    }
    fieldsList.fields.filter { it.endOffset == null }.let {
        require(it.isEmpty()) { "End offset not calculated for fields ${it.joinToString(separator = ", ") { it.name }}" }
    }
    return fieldsList
}

private fun RpgParser.Parm_fixedContext.toFieldInfo(conf: ToAstConfiguration = ToAstConfiguration(), knownDataDefinitions: Collection<DataDefinition>): FieldInfo {
    var overlayInfo: FieldInfo.OverlayInfo? = null
    val overlay = this.keyword().find { it.keyword_overlay() != null }
    val like = this.keyword()
        .firstOrNull { it.keyword_like() != null }
        .let { if (it != null) it.keyword_like()?.simpleExpression()?.toAst(conf) as DataRefExpr else null }

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
    } else {
        this.toDSFieldInitKeyword(conf = conf)?.apply {
            initializationValue = this.toAst()
        }
    }

    // compileTimeInterpreter.evaluate(this.rContext(), dim!!).asInt().value.toInt(),
    val arraySizeDeclared = this.arraySizeDeclared(conf)
    val varName = like?.variable?.name ?: this.name
    val explicitElementType: Type? = this.calculateExplicitElementType(arraySizeDeclared, conf)
        ?: knownDataDefinitions.firstOrNull { it.name.equals(varName, ignoreCase = true) }?.type
        ?: knownDataDefinitions.flatMap { it.fields }.firstOrNull { fe -> fe.name.equals(varName, ignoreCase = true) }?.type
        ?: like?.let {
            InjectableCompileTimeInterpreter(
                knownDataDefinitions = knownDataDefinitions.toList(),
                delegatedCompileTimeInterpreter = conf.compileTimeInterpreter
            ).evaluateTypeOf(this.rContext(), it, conf)
        }

    return FieldInfo(this.name, overlayInfo = overlayInfo,
            explicitStartOffset = this.explicitStartOffset(),
            explicitEndOffset = if (explicitStartOffset() != null) this.explicitEndOffset() else null,
            explicitElementType = explicitElementType,
            arraySizeDeclared = this.arraySizeDeclared(conf),
            arraySizeDeclaredOnThisField = this.arraySizeDeclared(conf),
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
                        var step = index
                        while (step > 0) {
                            val prevField = fields[--step]
                            if (prevField.endOffsetIncludingAllElement != null) {
                                field.startOffset = prevField.endOffsetIncludingAllElement
                                break
                            }
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

    fun considerNotInOverlayFields() {
        val declareDimElement = fields.filter { it.arraySizeDeclared != null }.firstOrNull()
        if (declareDimElement != null) {
            val totalArraySize = declareDimElement.arraySizeDeclared!! * declareDimElement.endOffset!!
            var startOffsetIndex = totalArraySize
            fields.forEachIndexed { index, field ->

                if (field.startOffset == null) {
                    if (field.overlayInfo == null) {
                        field.startOffset = startOffsetIndex
                    }
                }
                if (field.endOffset == null) {
                    if (field.overlayInfo == null) {
                        field.endOffset = (field.startOffset!! + field.elementSize!!)
                        startOffsetIndex = field.endOffset!!
                    }
                }
            }
        }
    }

    fun isNotEmpty() = fields.isNotEmpty()
}

/**
 * Generates AST for a DS.
 * This implementation considers if the DS uses `LIKEDS` or `EXTNAME`. In first case returns the DS with its name and the fields
 *  from the other DS. In second case returns a DS like this:
 *   - firstly, all fields from file;
 *   - then, fields if declared in source code.
 * In additions, out from `LIKEDS` case, this method calculates sequences, offsets and overlays for its fields.
 * @param conf Ast' configuration
 * @param knownDataDefinitions Collection of other data definitions previously declared. Is necessary to resolve `LIKE` and `LIKEDS`.
 * @param parentDataDefinitions Like to `knownDataDefinitions`, but to find out from actual scope.
 * @param fileDefinitions Necessary for `EXTNAME` case to load its fields.
 * @return DataDefinition for DS or null: for `LIKEDS` if there is not the DS from getting the fields; for `EXTNAME` if there is not the file from getting the fields.
 */
internal fun RpgParser.Dcl_dsContext.toAst(
    conf: ToAstConfiguration = ToAstConfiguration(),
    knownDataDefinitions: Collection<DataDefinition>,
    parentDataDefinitions: List<DataDefinition>?,
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>?
): DataDefinition? {
    // Using `LIKEDS`
    if (this.keyword().any { it.keyword_likeds() != null }) {
        val referredDs = this.findDs(knownDataDefinitions, parentDataDefinitions, conf)
        val dataDefinition = DataDefinition(
            this.name,
            referredDs.type,
            referredDs.fields,
            position = this.toPosition(true)
        )
        dataDefinition.fields = dataDefinition.fields.map { it.copy(overriddenContainer = dataDefinition) }
        return dataDefinition
    }

    val initializationValue: Expression? = null
    val size = this.declaredSize()

    // Using `EXTNAME`
    val fieldsFile = fileDefinitions?.let { getExtnameFields(fileDefinitions, conf) } ?: emptyList()

    // Calculating information about the DS and its fields is full of interdependecies, therefore we do that in steps.
    val fieldsList = calculateFieldInfos(knownDataDefinitions, fieldsFile)
    val type: Type = this.type(size, fieldsList, conf)
    val inz = this.keyword().firstOrNull { it.keyword_inz() != null }

    val dataDefinition = DataDefinition(
        this.name,
        type,
        fields = fieldsList.fields.map { it.toAst(conf) },
        initializationValue = initializationValue,
        inz = inz != null,
        position = this.toPosition(true))

    // Set the "overlayingOn" value for all field definitions.
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

/**
 * Finds the DS and prepare the new DataDefinition for a DS that uses `LIKEDS`.
 * @param knownDataDefinitions Collection of other data definitions previously declared. Is necessary to resolve `LIKE`.
 * @param parentDataDefinitions Like to `knownDataDefinitions`, but to find out from actual scope.
 * @param conf Ast' configuration
 * @return DataDefinition found.
 * @see RpgParser.Dcl_dsContext.toAst for its utilization.
 */
private fun RpgParser.Dcl_dsContext.findDs(
    knownDataDefinitions: Collection<DataDefinition>,
    parentDataDefinitions: List<DataDefinition>?,
    conf: ToAstConfiguration
): DataDefinition {
    val likeDsName = (this.keyword().mapNotNull { it.keyword_likeds() })
        .first().data_structure_name.identifier().free_identifier()
        .idOrKeyword().ID().text
    val referredDataDefinition = knownDataDefinitions.find { it.name == likeDsName }
        ?: parentDataDefinitions?.find { it.name == likeDsName }
        ?: this.error("Data definition $likeDsName not found", conf = conf)

    return referredDataDefinition
}

/**
 * Finds the file and prepare extracts the fields for a DS that uses `EXTNAME`.
 * @param dataDefinitions Collection of other data definitions previously declared.
 * @param conf Ast' configuration
 * @return List of fields.
 * @see RpgParser.Dcl_dsContext.toAst for its utilization.
 */
private fun RpgParser.Dcl_dsContext.extractFieldsFromFile(
    dataDefinitions: List<DataDefinition>,
    conf: ToAstConfiguration
): List<FieldInfo> {
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

    return fields.map {
        FieldInfo(
            name = it.name,
            explicitStartOffset = it.explicitStartOffset,
            explicitEndOffset = it.explicitEndOffset,
            explicitElementType = it.type,
            position = it.position
        )
    }
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

internal fun RpgParser.Dcl_dsContext.getKeywordExtName() = this.keyword().first { it.keyword_extname() != null }.keyword_extname()

internal fun RpgParser.Keyword_extnameContext.getExtName() = file_name.text

internal fun RpgParser.Dcl_dsContext.getKeywordPrefix() = this.keyword().firstOrNull { it.keyword_prefix() != null }?.keyword_prefix()

internal fun RpgParser.Keyword_prefixContext.getPrefixName() = prefix.text

internal fun RpgParser.Dcl_dsContext.hasExtname() = this.keyword().any { it.keyword_extname() != null }

internal fun RpgParser.Dcl_dsContext.getExtnameFields(
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>,
    conf: ToAstConfiguration
): List<FieldInfo> {
    if (!hasExtname()) return emptyList()
    val keywordExtName = getKeywordExtName()
    val keywordPrefix = getKeywordPrefix()

    val extName = keywordExtName.getExtName()
    val prefixName = keywordPrefix?.getPrefixName()

    val fileDataDefinitions =
        fileDefinitions.filter {
            val nameMatches = it.key.name.uppercase() == extName.uppercase()
            val prefixIsNull = keywordPrefix == null && it.key.prefix == null
            val prefixIsValid = keywordPrefix != null && it.key.prefix != null && it.key.prefix is Prefix
            val prefixMatches = prefixIsValid && it.key.prefix?.prefix == prefixName

            nameMatches && (prefixIsNull || prefixMatches)
        }.values.flatten()

    if (fileDataDefinitions.isEmpty()) return emptyList()
    return extractFieldsFromFile(fileDataDefinitions, conf)
}

fun RpgParser.Parm_fixedContext.explicitStartOffset(): Int? {
    val text = this.FROM_POSITION().text.trim()
    return if (text.isBlank()) {
        null
    } else {
        // from position could contain one of keywords defined in DSFieldInitKeyword
        // for this reason not int value is allowed
        text.toIntOrNull()?.let { it - 1 }
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
