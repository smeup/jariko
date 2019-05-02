package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.Expression

fun RpgParser.Dcl_dsContext.elementSizeOf() : Int {
    val header = this.parm_fixed().first()
    val elementSize = header.TO_POSITION().text.trim().toInt()
    return elementSize
}

private fun RpgParser.DspecContext.arrayLength(conf : ToAstConfiguration = ToAstConfiguration()) : Expression? {
    return this.keyword().arrayLength(conf)
}

private fun RpgParser.Dcl_dsContext.arrayLength(conf : ToAstConfiguration = ToAstConfiguration()) : Expression? {
    return this.keyword().arrayLength(conf)
}

private fun RpgParser.Parm_fixedContext.arrayLength(conf : ToAstConfiguration = ToAstConfiguration()) : Expression? {
    return this.keyword().arrayLength(conf)
}

private fun List<RpgParser.KeywordContext>.arrayLength(conf : ToAstConfiguration = ToAstConfiguration()) : Expression? {
    val dims = this.filter { it.keyword_dim() != null }.map { it.keyword_dim() }
    return when (dims.size) {
        0 -> null
        1 -> dims[0].numeric_constant.toAst(conf)
        else -> throw UnsupportedOperationException("Ambiguous array dimensions")
    }
}