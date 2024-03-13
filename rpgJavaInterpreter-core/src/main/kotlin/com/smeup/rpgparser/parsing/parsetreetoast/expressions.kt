package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.RpgParser.Dcl_prContext
import com.smeup.rpgparser.RpgParser.Ds_nameContext
import com.smeup.rpgparser.RpgParser.ExpressionContext
import com.smeup.rpgparser.RpgParser.PrBeginContext
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

internal fun RpgParser.SimpleExpressionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when {
        this.number() != null -> this.number()!!.toAst(conf)
        this.identifier() != null -> this.identifier().toAst(conf)
        this.bif() != null -> this.bif().toAst(conf)
        this.literal() != null -> this.literal().toAst(conf)
        else -> todo(conf = conf)
    }
}

fun RpgParser.ExpressionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when {
        this.number() != null -> this.number()!!.toAst(conf)
        this.identifier() != null -> this.identifier().toAst(conf)
        this.bif() != null -> this.bif().toAst(conf)
        this.literal() != null -> this.literal().toAst(conf)
        this.EQUAL() != null -> EqualityExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.OR() != null -> LogicalOrExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.AND() != null -> LogicalAndExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.comparisonOperator() != null -> when {
            this.comparisonOperator().GT() != null -> GreaterThanExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
            this.comparisonOperator().GE() != null -> GreaterEqualThanExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
            this.comparisonOperator().LT() != null -> LessThanExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
            this.comparisonOperator().LE() != null -> LessEqualThanExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
            this.comparisonOperator().NE() != null -> DifferentThanExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
            else -> todo(conf = conf)
        }
        this.function() != null -> this.function().toAst(conf)
        this.NOT() != null -> NotExpr(this.expression(0).toAst(conf), toPosition(conf.considerPosition))
        this.PLUS() != null -> PlusExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.MINUS() != null -> MinusExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.MULT() != null || this.MULT_NOSPACE() != null -> MultExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.DIV() != null -> DivExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.EXP() != null -> ExpExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.indicator() != null -> this.indicator().toAst(conf)
        this.unaryExpression() != null -> this.unaryExpression().toAst(conf)
        // FIXME it is rather ugly that we have to do this: we should get a different parse tree here
        this.children.size == 3 && this.children[0].text == "(" && this.children[2].text == ")"
                && this.children[1] is RpgParser.ExpressionContext -> (this.children[1] as RpgParser.ExpressionContext).toAst(conf)
        else -> todo(conf = conf)
    }
}

internal fun RpgParser.UnaryExpressionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    if (this.children.size > 0) {
        return when {
            this.children[0].text.equals("-") && this.children[1] is ExpressionContext -> NegationExpr((this.children[1] as ExpressionContext).toAst(conf))
            else -> todo(conf = conf)
        }
    }

    return todo(conf = conf)
}

internal fun RpgParser.IndicatorContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): IndicatorExpr {
    // manage *IN(
    if (this.children[0].text.uppercase() == "*IN" && this.children[1].text == "(") {
        val index: Expression = (this.children[2].getChild(0) as RpgParser.ExpressionContext).toAst(conf = conf)
        return IndicatorExpr(index = index, position = toPosition(conf.considerPosition))
    } else {
        val index = text.uppercase(Locale.getDefault()).removePrefix("*IN").toInt()
        return IndicatorExpr(index = index, position = toPosition(conf.considerPosition))
    }
}

internal fun RpgParser.LiteralContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): StringLiteral {

    /*
     The following line of code allows you to trap and throw the error when defining a hexadecimal constant variable.
     We don't want to handle this type of constant, because jariko using UTF-8 doesn't need to go through
     hexadecimal when resolving constants.
     Consequently, pending revision of /copy £JAX_PC1, which contains several hexadecimal constants,
     we have decided to leave this change on standby.
       if (this.HexLiteralStart() != null) todo(message = "Error: constant definition in hexadecimal not managed", conf = conf)

     The literalContext can be valued in 2 ways:
       - fetching content from multiple lines
       - fetching content from only one line
     To understand in which case we are, the 'children' node comes in handy.
     This is because the 'children' node is an array structured as follows:
       children[0] = "'"
       children[1 to n-1] = text
       children[n] = "'"
     */
    val stringContent = if (this.children.size > 3) {
        if (children[0].text == "'" && children[this.children.size - 1].text == "'") {
            this.children.subList(1, this.children.size - 1).joinToString(separator = "")
        } else {
            this.content?.text ?: ""
        }
    } else {
        this.content?.text ?: ""
    }
    return StringLiteral(stringContent, toPosition(conf.considerPosition))
}

internal fun RpgParser.NumberContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): NumberLiteral {
    val position = this.toPosition(conf.considerPosition)
    require(this.NumberPart().isEmpty()) { "Number not empty $position" }
    return literalToNumber(this.text, position)
}

fun String.toRealLiteral(position: Position?, locale: Locale): RealLiteral {
    val nf = NumberFormat.getNumberInstance(locale)
    val formatter = nf as DecimalFormat
    formatter.isParseBigDecimal = true
    val bd = (formatter.parse(this) as BigDecimal)
    // in case of zero precision returned by big decimal il always 1
    val precision = if (bd.toDouble() == 0.0) {
        this.replace(Regex("[^0-9]"), "").length
    } else {
        bd.precision()
    }
    return RealLiteral(value = bd, position = position, precision = precision)
}

fun String.toIntLiteral(position: Position?): IntLiteral {
    val value = this.toLong()
    val precision = if (value == 0L) {
        this.replace(Regex("[^0-9]"), "").length
    } else {
        BigDecimal(value).precision()
    }
    return IntLiteral(value = value, position = position, precision = precision)
}

internal fun RpgParser.IdentifierContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    if (this.text.toUpperCase().startsWith("*ALL")) {
        return AllExpr(this.all().literal().toAst(conf), toPosition(conf.considerPosition))
    }

    if (isFunctionWithoutParams(this.text)) {
        return FunctionCall(ReferenceByName(this.text), listOf(), toPosition(conf.considerPosition))
    }

    return when (this.text.toUpperCase()) {
        "*BLANK", "*BLANKS" -> BlanksRefExpr(toPosition(conf.considerPosition))
        "*ZERO", "*ZEROS" -> ZeroExpr(toPosition(conf.considerPosition))
        "*HIVAL" -> HiValExpr(toPosition(conf.considerPosition))
        "*LOVAL" -> LowValExpr(toPosition(conf.considerPosition))
        "*ON" -> OnRefExpr(toPosition(conf.considerPosition))
        "*OFF" -> OffRefExpr(toPosition(conf.considerPosition))
        else -> variableExpression(conf)
    }
}

private fun RpgParser.IdentifierContext.variableExpression(conf: ToAstConfiguration): Expression {
    return when {
        this.text.indicatorIndex() != null -> IndicatorExpr(this.text.indicatorIndex()!!, toPosition(conf.considerPosition))
        this.multipart_identifier() != null -> this.multipart_identifier().toAst(conf)
        else -> DataRefExpr(variable = ReferenceByName(this.text), position = toPosition(conf.considerPosition))
    }
}

private fun RpgParser.IdentifierContext.isFunctionWithoutParams(referenceName: String): Boolean {
    return runCatching {
        rContext().children.filterIsInstance<Dcl_prContext>()
                .flatMap { it.children.filterIsInstance<PrBeginContext>() }
                .flatMap { it.children.filterIsInstance<Ds_nameContext>() }
                .firstOrNull { it.text == referenceName }?.text
    }.getOrNull() != null
}

internal fun RpgParser.Multipart_identifierContext.toAst(conf: ToAstConfiguration = ToAstConfiguration(), fieldName: String? = null): Expression {
    require(this.elements.size == 2) { "More than two elements not yet supported" }

    // The parse tree is not constructed well, and that force us to do... complicated things
    // The reason is that we could have this case:
    // A.B(1)
    // Now, the logic thing is to have:
    // ArrayAccessOf(
    //    array=QualifiedAccess(
    //        container=Ref(A)
    //        fieldName=B)
    //    index=1)
    // While we have
    // QualifiedAccess(
    //    container=Ref(A)
    //    fieldName=ArrayAccessOf(Ref(B), 1))
    // Which is wrong, and bad, and unhealthy.
    // So we deal with this here

    if (fieldName == null && this.elements[1].indexed_identifier() != null) {
        val container = this.toAst(conf, fieldName = this.elements[1].indexed_identifier().free_identifier().idOrKeyword().text)
        val index = this.elements[1].indexed_identifier().expression().toAst(conf)
        return ArrayAccessExpr(container, index, position = toPosition(conf.considerPosition))
    }

    require(fieldName != null || this.elements[1].free_identifier() != null)
    return QualifiedAccessExpr(
            container = this.elements[0].toAst(conf),
            field = ReferenceByName(fieldName ?: this.elements[1].free_identifier().text),
            position = toPosition(conf.considerPosition)
    )
}
//
internal fun RpgParser.Multipart_identifier_elementContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when {
        this.free_identifier() != null -> DataRefExpr(
                variable = ReferenceByName(this.free_identifier().text),
                position = toPosition(conf.considerPosition)
        )
        else -> todo(conf = conf)
    }
}

internal fun String.indicatorIndex(): Int? {
    val uCaseIndicatorString = this.uppercase()
    return when {
        uCaseIndicatorString.startsWith("*IN") ->
            this.substring("*IN".length).toIndicatorKey()
        else -> null
    }
}

internal fun String.dataWrapUpChoice(): DataWrapUpChoice? {
    val indicator = when {
        this.uppercase().startsWith("*IN(") && this.endsWith(")") ->
            this.uppercase().removePrefix("*IN(").removeSuffix(")")
        this.uppercase().startsWith("*IN") -> this.substring("*IN".length)
        else -> null
    }
    return when (indicator) {
        "LR" -> DataWrapUpChoice.LR
        "RT" -> DataWrapUpChoice.RT
        else -> null
    }
}
