package com.smeup.rpgparser

import com.smeup.rpgparser.DataType.*
import com.smeup.rpgparser.RpgParser.*
import me.tomassetti.kolasu.mapping.toPosition
import sun.java2d.cmm.lcms.LCMS

fun RContext.toAst(considerPosition : Boolean = true) = CompilationUnit(
        this.statement().mapNotNull { it.dspec() }.map { it.toAst(considerPosition) }
        + this.statement().mapNotNull { it.dcl_ds() }.map { it.toAst(considerPosition) },
        position = this.toPosition(considerPosition))

private fun DspecContext.arrayLength(considerPosition : Boolean = true) : Expression {
    return this.keyword().arrayLength(considerPosition)
}

private fun Dcl_dsContext.arrayLength(considerPosition : Boolean = true) : Expression {
    return this.keyword().arrayLength(considerPosition)
}

private fun Parm_fixedContext.arrayLength(considerPosition : Boolean = true) : Expression {
    return this.keyword().arrayLength(considerPosition)
}

private fun List<KeywordContext>.arrayLength(considerPosition : Boolean = true) : Expression {
    val dims = this.filter { it.keyword_dim() != null }.map { it.keyword_dim() }
    return when (dims.size) {
        0 -> IntLiteral(1)
        1 -> dims[0].numeric_constant.toAst(considerPosition)
        else -> throw UnsupportedOperationException("Ambiguous array dimensions")
    }
}

private fun SimpleExpressionContext.toAst(considerPosition : Boolean = true): Expression {
    return when {
        this.number() != null -> this.number()!!.toAst(considerPosition)
        else -> TODO(this.javaClass.canonicalName)
    }
}

private fun NumberContext.toAst(considerPosition : Boolean = true) : NumberLiteral {
    require(this.NumberPart().isEmpty())
    require(this.MINUS() == null)
    val text = this.NUMBER().text
    return if (text.contains('.')) {
        RealLiteral(text.toDouble(), this.toPosition(considerPosition))
    } else {
        IntLiteral(text.toLong(), this.toPosition(considerPosition))
    }
}

private fun DspecContext.toAst(considerPosition : Boolean = true) : DataDefinition {
    val type = when (this.DATA_TYPE()?.text?.trim()) {
        null -> SINGLE
        "" -> SINGLE
        "N" -> BOOLEAN
        else -> throw UnsupportedOperationException("<${this.DATA_TYPE().text}>")
    }
    return DataDefinition(
            this.ds_name().text,
            type,
            this.TO_POSITION().text.trim().toInt(),
            decimals = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() },
            arrayLength = this.arrayLength(considerPosition),
            position = this.toPosition(true))
}

private fun Dcl_dsContext.toAst(considerPosition : Boolean = true) : DataDefinition {
    require(this.TO_POSITION().text.trim().isEmpty())
    if (this.ds_name().text.trim().isEmpty()) {
        require(this.parm_fixed().isNotEmpty())
        val header = this.parm_fixed().first()
        val others = this.parm_fixed().drop(1)
        return DataDefinition(
                header.ds_name().text,
                DATA_STRUCTURE,
                header.TO_POSITION().text.trim().toInt(),
                decimals = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() },
                arrayLength = header.arrayLength(considerPosition),
                fields = others.map { it.toAst(considerPosition) },
                position = this.toPosition(true))
    } else {
        TODO()
//        return DataDefinition(
//                this.ds_name().text,
//                DATA_STRUCTURE,
//                0,
//                decimals = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() },
//                arrayLength = this.arrayLength(considerPosition),
//                fields = listOf(),
//                position = this.toPosition(true))
    }
}

private fun Parm_fixedContext.toAst(considerPosition: Boolean = true): FieldDefinition {
    return FieldDefinition(this.ds_name().text, this.TO_POSITION().text.trim().toInt(), this.toPosition(considerPosition))
}
