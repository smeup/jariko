package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

internal fun RpgParser.SimpleExpressionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when {
        this.number() != null -> this.number()!!.toAst(conf)
        this.identifier() != null -> this.identifier().toAst(conf)
        this.bif() != null -> this.bif().toAst(conf)
        this.literal() != null -> this.literal().toAst(conf)
        else -> TODO(this.javaClass.canonicalName)
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
            else -> TODO("ComparisonOperator ${this.comparisonOperator().text}")
        }
        this.function() != null -> this.function().toAst(conf)
        this.NOT() != null -> NotExpr(this.expression(0).toAst(conf), toPosition(conf.considerPosition))
        this.PLUS() != null -> PlusExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.MINUS() != null -> MinusExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.MULT() != null || this.MULT_NOSPACE() != null -> MultExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.DIV() != null -> DivExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.EXP() != null -> ExpExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        // FIXME it is rather ugly that we have to do this: we should get a different parse tree here
        this.children.size == 3 && this.children[0].text == "(" && this.children[2].text == ")"
                && this.children[1] is RpgParser.ExpressionContext -> (this.children[1] as RpgParser.ExpressionContext).toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun RpgParser.LiteralContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): StringLiteral {
    return StringLiteral(this.content?.text ?: "", toPosition(conf.considerPosition))
}

internal fun RpgParser.NumberContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): NumberLiteral {
    val position = this.toPosition(conf.considerPosition)
    require(this.NumberPart().isEmpty(), { "Number not empty $position" })
    val text = (this.MINUS()?.text ?: "") + this.NUMBER().text

    // When assigning a value to a numeric field we could either use
    // a comma or a dot as decimal separators

    // TODO Rifattorizzare con literalToNumber(text, position)
    return when {
        text.contains('.') -> {
            text.toRealLiteral(position, Locale.US)
        }
        text.contains(',') -> {
            text.toRealLiteral(position, Locale.ITALIAN)
        }
        else -> IntLiteral(text.toLong(), position)
    }
}

fun String.toRealLiteral(position: Position?, locale: Locale): RealLiteral {
    val nf = NumberFormat.getNumberInstance(locale)
    val formatter = nf as DecimalFormat
    formatter.isParseBigDecimal = true

    val bd = formatter.parse(this) as BigDecimal
    return RealLiteral(bd, position)
}

internal fun RpgParser.IdentifierContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    if (this.text.toUpperCase().startsWith("*ALL")) {
        return AllExpr(this.all().literal().toAst(conf), toPosition(conf.considerPosition))
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
        this.text.indicatorIndex() != null -> PredefinedIndicatorExpr(this.text.indicatorIndex()!!, toPosition(conf.considerPosition))
        this.multipart_identifier() != null -> this.multipart_identifier().toAst(conf)
        else -> DataRefExpr(variable = ReferenceByName(this.text), position = toPosition(conf.considerPosition))
    }
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
        else -> TODO()
    }
}

internal fun String.indicatorIndex(): Int? {
    val uCaseIndicatorString = this.toUpperCase()
    return when {
        uCaseIndicatorString.startsWith("*IN(") && this.endsWith(")") ->
            uCaseIndicatorString.removePrefix("*IN(").removeSuffix(")").toIntOrNull()
        uCaseIndicatorString.startsWith("*IN") ->
            this.substring("*IN".length).toIntOrNull()
        else -> null
    }
}
