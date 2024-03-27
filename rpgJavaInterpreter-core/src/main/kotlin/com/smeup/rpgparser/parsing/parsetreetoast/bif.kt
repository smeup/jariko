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
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition
import java.util.*

internal fun RpgParser.BifContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    val position = toPosition(conf.considerPosition)
    return when {
        this.bif_elem() != null -> NumberOfElementsExpr(
            this.bif_elem().expression().toAst(conf),
            position = position
        )
        this.bif_lookup() != null -> this.bif_lookup().toAst(conf)
        this.bif_xlate() != null -> this.bif_xlate().toAst(conf)
        this.bif_scan() != null -> this.bif_scan().toAst(conf)
        this.bif_check() != null -> this.bif_check().toAst(conf)
        this.bif_trim() != null -> this.bif_trim().toAst(conf)
        this.bif_trimr() != null -> this.bif_trimr().toAst(conf)
        this.bif_triml() != null -> this.bif_triml().toAst(conf)
        this.bif_subst() != null -> this.bif_subst().toAst(conf)
        this.bif_subarr() != null -> this.bif_subarr().toAst(conf)
        this.bif_len() != null -> this.bif_len().toAst(conf)
        this.bif_dec() != null -> this.bif_dec().toAst(conf)
        this.bif_char() != null -> this.bif_char().toAst(conf)
        this.bif_timestamp() != null -> this.bif_timestamp().toAst(conf)
        this.bif_diff() != null -> this.bif_diff().toAst(conf)
        this.bif_editc() != null -> this.bif_editc().toAst(conf)
        this.bif_found() != null -> this.bif_found().toAst(conf)
        this.bif_eof() != null -> this.bif_eof().toAst(conf)
        this.bif_equal() != null -> this.bif_equal().toAst(conf)
        this.bif_abs() != null -> this.bif_abs().toAst(conf)
        this.bif_int() != null -> this.bif_int().toAst(conf)
        this.bif_inth() != null -> this.bif_inth().toAst(conf)
        this.bif_editw() != null -> this.bif_editw().toAst(conf)
        this.bif_rem() != null -> this.bif_rem().toAst(conf)
        this.bif_replace() != null -> this.bif_replace().toAst(conf)
        this.bif_sqrt() != null -> this.bif_sqrt().toAst(conf)
        this.bif_parms() != null -> this.bif_parms().toAst(conf)
        this.bif_open() != null -> this.bif_open().toAst(conf)
        else -> todo(conf = conf)
    }
}

internal fun RpgParser.Bif_remContext.toAst(conf: ToAstConfiguration): Expression {
    return RemExpr(
        this.expression(0).toAst(conf),
        this.expression(1).toAst(conf),
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_replaceContext.toAst(conf: ToAstConfiguration): Expression {
    return ReplaceExpr(
        this.expression(0).toAst(conf),
        this.expression(1).toAst(conf),
        this.expression(2)?.toAst(conf),
        this.expression(3)?.toAst(conf),
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_absContext.toAst(conf: ToAstConfiguration): Expression {
    return AbsExpr(
            this.expression().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_foundContext.toAst(conf: ToAstConfiguration): Expression {
    return FoundExpr(
            this.filenameident?.text,
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_eofContext.toAst(conf: ToAstConfiguration): Expression {
    return EofExpr(
            this.filenameident?.text,
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_equalContext.toAst(conf: ToAstConfiguration): Expression {
    return EqualExpr(
        this.filenameident?.text,
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_editcContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): EditcExpr {
    return EditcExpr(
            this.expression(0).toAst(conf),
            this.expression(1).toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_editwContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): EditwExpr {
    return EditwExpr(
            this.expression(0).toAst(conf),
            this.expression(1).toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_charContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CharExpr {
    return CharExpr(
            this.expression().toAst(conf),
        this.bif_charformat()?.text?.uppercase(Locale.getDefault()),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_decContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DecExpr {
    return DecExpr(
        this.expression(0).toAst(conf),
        this.expression(1).toAst(conf),
        this.expression(2).toAst(conf),
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_intContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): IntExpr {
    return IntExpr(
        this.expression().toAst(conf),
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_inthContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): InthExpr {
    return InthExpr(
        this.expression().toAst(conf),
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_sqrtContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SqrtExpr {
    return SqrtExpr(
            this.expression().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_lenContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): LenExpr {
    return LenExpr(
            this.expression().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_timestampContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TimeStampExpr {
    return TimeStampExpr(
            this.expression()?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_diffContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DiffExpr {
    // TODO: handle 4th parameter (= frac)
    return DiffExpr(
            this.expression(0).toAst(conf),
            this.expression(1).toAst(conf),
            this.durationCode().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.DurationCodeContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DurationCode =
    when {
        SPLAT_MSECONDS() != null || SPLAT_MS() != null -> DurationInMSecs
        SPLAT_DAYS() != null || SPLAT_D() != null -> DurationInDays
        else -> todo(conf = conf)
    }

internal fun RpgParser.Bif_substContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubstExpr {
    return SubstExpr(
            this.string.toAst(conf),
            this.start.toAst(conf),
            this.length?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_subarrContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubarrExpr {
    return SubarrExpr(
        array = this.array.toAst(conf),
        start = this.start.toAst(conf),
        numberOfElements = this.numberelements?.toAst(conf),
        position = toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_trimContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TrimExpr {
    return TrimExpr(
            this.string.toAst(conf),
            this.trimcharacters?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_trimrContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TrimrExpr {
    return TrimrExpr(
            this.string.toAst(conf),
            this.trimcharacters?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_trimlContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TrimlExpr {
    return TrimlExpr(
        this.string.toAst(conf),
        this.trimcharacters?.toAst(conf),
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_scanContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ScanExpr {
    return ScanExpr(
        value = this.searcharg.toAst(conf),
        source = this.source.toAst(conf),
        start = this.start?.toAst(conf),
        length = this.length?.toAst(conf),
        toPosition(conf.considerPosition)
    )
}

internal fun RpgParser.Bif_checkContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CheckExpr {
    return CheckExpr(
        value = this.comparator.toAst(conf),
        source = this.base.toAst(conf),
        start = this.start?.toAst(conf),
        toPosition(conf.considerPosition)
    )
}

internal fun RpgParser.Bif_xlateContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TranslateExpr {
    return TranslateExpr(
            this.from.toAst(conf),
            this.to.toAst(conf),
            this.string.toAst(conf),
            this.startpos?.toAst(conf) ?: IntLiteral(1),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_lookupContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): LookupExpr {
    return LookupExpr(
            this.bif_lookupargs().arg.toAst(conf),
            this.bif_lookupargs().array.toAst(conf),
            this.bif_lookupargs().startindex?.toAst(conf),
            this.bif_lookupargs().numberelements?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_parmsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ParmsExpr {
    return ParmsExpr(
        this.text,
        toPosition(conf.considerPosition))
}

internal fun RpgParser.Bif_openContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): OpenExpr {
    return OpenExpr(
        this.identifier().text,
        toPosition(conf.considerPosition))
}
