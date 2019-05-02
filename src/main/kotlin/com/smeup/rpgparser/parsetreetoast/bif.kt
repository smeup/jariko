package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.*
import com.strumenta.kolasu.mapping.toPosition

internal fun RpgParser.BifContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Expression {
    return when {
        this.bif_elem() != null -> NumberOfElementsExpr(this.bif_elem().expression().toAst(conf), position = toPosition(conf.considerPosition))
        this.bif_lookup() != null -> this.bif_lookup().toAst(conf)
        this.bif_xlate() != null -> this.bif_xlate().toAst(conf)
        this.bif_scan() != null -> this.bif_scan().toAst(conf)
        this.bif_trim() != null -> this.bif_trim().toAst(conf)
        this.bif_subst() != null -> this.bif_subst().toAst(conf)
        this.bif_len() != null -> this.bif_len().toAst(conf)
        this.bif_dec() != null -> this.bif_dec().toAst(conf)
        this.bif_char() != null -> this.bif_char().toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun RpgParser.Bif_charContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): CharExpr {
    return CharExpr(
            this.expression().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_decContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): DecExpr {
    return DecExpr(
            this.expression(0).toAst(conf),
            this.expression(1).toAst(conf),
            this.expression(2).toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_lenContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): LenExpr {
    return LenExpr(
            this.expression().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_substContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SubstExpr {
    return SubstExpr(
            this.string.toAst(conf),
            this.start.toAst(conf),
            this.length?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_trimContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): TrimExpr {
    return TrimExpr(
            this.string.toAst(conf),
            this.trimcharacters?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_scanContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ScanExpr {
    return ScanExpr(
            this.searcharg.toAst(conf),
            this.source.toAst(conf),
            this.start?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_xlateContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): TranslateExpr {
    return TranslateExpr(
            this.from.toAst(conf),
            this.to.toAst(conf),
            this.string.toAst(conf),
            this.startpos?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_lookupContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): LookupExpr {
    return LookupExpr(
            this.bif_lookupargs().arg.toAst(conf),
            this.bif_lookupargs().array.toAst(conf),
            toPosition(conf.considerPosition))
}
