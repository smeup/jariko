package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.AssignableExpression
import com.smeup.rpgparser.ast.Expression
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.utils.asInt
import com.strumenta.kolasu.mapping.toPosition

fun RpgParser.Dcl_dsContext.elementSizeOf(): Int {
    val header = this.parm_fixed().first()
    return header.TO_POSITION().text.asInt()
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
            val decimalPositions = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() }
            NumberType(elementSize!! - decimalPositions, decimalPositions)
        } else {
            StringType(elementSize!!.toLong())
        }
        "A" -> StringType(elementSize!!.toLong())
        "N" -> BooleanType
        "Z" -> TimeStampType
        "S" -> StringType(elementSize!!.toLong())
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

fun RpgParser.Dcl_dsContext.type(size: Int? = null, conf: ToAstConfiguration = ToAstConfiguration()): Type {
    val header = this.parm_fixed().first()
    val dim: Expression? = header.keyword().asSequence().mapNotNull { it.keyword_dim()?.simpleExpression()?.toAst(conf) }.firstOrNull()
    val nElements = if (dim != null) conf.compileTimeInterpreter.evaluate(this.rContext(), dim).asInt().value.toInt() else null
    val others = this.parm_fixed().drop(if (nameIsInFirstLine) 0 else 1)
    val elementSize = this.elementSizeOf()
    val baseType = DataStructureType(others.map { it.toFieldType() }, size ?: elementSize)
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
    require(this.parm_fixed().isNotEmpty())
    val others = this.parm_fixed().drop(if (this.hasHeader) 1 else 0)
    val type: Type = this.type(size)
    val nElements = if (type is ArrayType) {
        type.nElements
    } else {
        null
    }
    return DataDefinition(
            this.name,
            type,
            fields = others.map { it.toAst(nElements, conf) },
            position = this.toPosition(true))
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
        "" -> if (DECIMAL_POSITIONS().text.isNotBlank()) {
            val decimalPositions = with(DECIMAL_POSITIONS().text.trim()) { if (isEmpty()) 0 else toInt() }
            NumberType(elementSize!! - decimalPositions, decimalPositions)
        } else {
            StringType(elementSize!!.toLong())
        }
        "N" -> BooleanType
        else -> throw UnsupportedOperationException("<${DATA_TYPE().text}>")
    }
}

internal fun RpgParser.Parm_fixedContext.toFieldType(): FieldType {
    return FieldType(this.ds_name().text, this.toType())
}
