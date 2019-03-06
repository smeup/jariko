package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.*
import me.tomassetti.kolasu.mapping.toPosition

fun RContext.toAst(considerPosition : Boolean = true) = CompilationUnit(
        this.statement().mapNotNull { it.dspec() }.map { it.toAst(considerPosition) },
        position = this.toPosition(considerPosition))

private fun DspecContext.arrayLength(considerPosition : Boolean = true) : Expression {
    val dims = this.keyword().filter { it.keyword_dim() != null }.map { it.keyword_dim() }
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

private fun DspecContext.toAst(considerPosition : Boolean = true) = DataDefinition(
        this.ds_name().text,
        DataType.SINGLE,
        this.TO_POSITION().text.trim().toInt(),
        decimals = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() },
        arrayLength = this.arrayLength(considerPosition),
        position = this.toPosition(true))
