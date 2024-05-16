package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.RpgParser.SetActGrpContext
import com.smeup.rpgparser.RpgParser.SetDeceditContext
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition

fun RpgParser.Hspec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): List<Directive> {
    val content = this.hspec_content()
    val deceditDirectives = content.filterIsInstance<SetDeceditContext>().map { it.hs_decedit_set().toAst(conf) }
    val actgrpDirectives = content.filterIsInstance<SetActGrpContext>().map { it.hs_actgrp().toAst(conf) }
    return deceditDirectives + actgrpDirectives
}

fun RpgParser.Hs_actgrpContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ActivationGroupDirective {
    require(this.hs_actgrp_parm() != null)

    return ActivationGroupDirective(
        activationGroupType(this.hs_actgrp_parm()),
        this.toPosition(conf.considerPosition)
    )
}

fun activationGroupType(parm: RpgParser.Hs_actgrp_parmContext): ActivationGroupType =
    when {
        parm.HS_NEW() != null ->
            NewActivationGroup

        parm.HS_CALLER() != null ->
            CallerActivationGroup

        else ->
            NamedActivationGroup(parm.hs_string().text.removeSurrounding("'").uppercase())
    }

fun RpgParser.Hs_decedit_parmContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DeceditDirectiveType {
    return when {
        this.hs_string() != null -> {
            val content = this.hs_string().content.joinToString(separator = "") { it.text }
            val format = StringLiteral(content, position = this.toPosition(conf.considerPosition))
            FormatDeceditDirective(format)
        }
        this.HS_JOBRUN() != null -> JobRunDeceditDirective
        else -> todo(message = "Invalid param in decedit directive", conf)
    }
}

fun RpgParser.Hs_decedit_setContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Directive {
    val type = this.hs_decedit_parm().toAst(conf)
    return DeceditDirective(type, position = this.toPosition(conf.considerPosition))
}
