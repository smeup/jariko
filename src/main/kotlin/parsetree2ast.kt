package com.smeup.rpgparser

import com.smeup.rpgparser.DataType.*
import com.smeup.rpgparser.RpgParser.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName

fun List<Node>.position() : Position? {
    val start = this.map { it.position?.start }.filterNotNull().sorted()
    val end = this.map { it.position?.end }.filterNotNull().sorted()
    return if (start.isEmpty() || end.isEmpty()) {
        null
    } else {
        Position(start.first(), end.last())
    }
}

fun RContext.toAst(considerPosition : Boolean = true) : CompilationUnit {
    val dataDefinitions = this.statement().mapNotNull { it.dspec() }.map { it.toAst(considerPosition) } +
            this.statement().mapNotNull { it.dcl_ds() }.map { it.toAst(considerPosition) }
    val mainStmts = emptyList<Statement>()
    val subroutines = emptyList<Subroutine>()
    return CompilationUnit(
            dataDefinitions,
            MainBody(mainStmts, if (considerPosition) mainStmts.position() else null),
            subroutines,
            position = this.toPosition(considerPosition))
}

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
        this.identifier() != null -> this.identifier().toAst(considerPosition)
        this.bif() != null -> this.bif().toAst(considerPosition)
        this.literal() != null -> this.literal().toAst(considerPosition)
        else -> TODO(this.javaClass.canonicalName)
    }
}

fun ExpressionContext.toAst(considerPosition : Boolean = true): Expression {
    return when {
        this.number() != null -> this.number()!!.toAst(considerPosition)
        this.identifier() != null -> this.identifier().toAst(considerPosition)
        this.bif() != null -> this.bif().toAst(considerPosition)
        this.literal() != null -> this.literal().toAst(considerPosition)
        this.EQUAL() != null -> EqualityExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
        this.comparisonOperator() != null -> when {
            this.comparisonOperator().GT() != null -> GreaterThanExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
            else -> TODO("ComparisonOperator ${this.comparisonOperator().text}")
        }
        this.function() != null -> this.function().toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun FunctionContext.toAst(considerPosition : Boolean = true): FunctionCall {
    return FunctionCall(ReferenceByName(this.functionName().text), this.args().expression().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

private fun LiteralContext.toAst(considerPosition : Boolean = true): Expression {
    return StringLiteral(this.content.text, toPosition(considerPosition))
}

private fun BifContext.toAst(considerPosition : Boolean = true): Expression {
    return when {
        this.bif_elem() != null -> NumberOfElementsExpr(this.bif_elem().expression().toAst(considerPosition), position = toPosition(considerPosition))
        else -> TODO(this.text.toString())
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

private fun IdentifierContext.toAst(considerPosition : Boolean = true) : DataRefExpr {
    return DataRefExpr(variable = ReferenceByName(this.text), position = toPosition(considerPosition))
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
            this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() },
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

fun StatementContext.toAst(considerPosition : Boolean = true): Statement {
    return when {
        this.cspec_fixed() != null -> this.cspec_fixed().toAst(considerPosition)
        this.block() != null -> this.block().toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun BlockContext.toAst(considerPosition: Boolean = true): Statement {
    return when {
        this.ifstatement() != null -> this.ifstatement().toAst(considerPosition)
        this.selectstatement() != null -> this.selectstatement().toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun SelectstatementContext.toAst(considerPosition: Boolean = true): SelectStmt {
    return SelectStmt(
            this.whenstatement().map { it.toAst(considerPosition) },
            this.other()?.toAst(considerPosition),
            toPosition(considerPosition))
}

private fun WhenstatementContext.toAst(considerPosition: Boolean = true): SelectCase {
    return SelectCase(
            this.`when`().csWHEN().fixedexpression.expression().toAst(considerPosition),
            this.statement().map { it.toAst(considerPosition) },
            toPosition(considerPosition)
    )
}

private fun OtherContext.toAst(considerPosition: Boolean = true): SelectOtherClause {
    TODO()
}

private fun IfstatementContext.toAst(considerPosition: Boolean = true): IfStmt {
    return IfStmt(this.beginif().fixedexpression.expression().toAst(considerPosition),
            this.thenBody.map { it.toAst(considerPosition) },
            this.elseIfClause().map { it.toAst(considerPosition) },
            this.elseClause()?.toAst(considerPosition),
            toPosition(considerPosition))
}

private fun ElseClauseContext.toAst(considerPosition: Boolean = true): ElseClause {
    return ElseClause(this.statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

private fun ElseIfClauseContext.toAst(considerPosition: Boolean = true): ElseIfClause {
    return ElseIfClause(
            this.elseifstmt().fixedexpression.expression().toAst(considerPosition),
            this.statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

private fun Cspec_fixedContext.toAst(considerPosition: Boolean = true): Statement {
    return when {
        this.cspec_fixed_standard() != null -> this.cspec_fixed_standard().toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun Cspec_fixed_standardContext.toAst(considerPosition: Boolean = true): Statement {
    return when {
        this.csEXSR() != null -> this.csEXSR().toAst(considerPosition)
        this.csEVAL() != null -> this.csEVAL().toAst(considerPosition)
        this.csCALL() != null -> this.csCALL().toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun CsEXSRContext.toAst(considerPosition: Boolean = true): ExecuteSubroutine {
    val subroutineName = this.cspec_fixed_standard_parts().factor2.text
    require(this.cspec_fixed_standard_parts().decimalPositions.text.isBlank())
    require(this.cspec_fixed_standard_parts().eq.text.isBlank())
    require(this.cspec_fixed_standard_parts().hi.text.isBlank())
    require(this.cspec_fixed_standard_parts().len.text.isBlank())
    require(this.cspec_fixed_standard_parts().lo.text.isBlank())
    require(this.cspec_fixed_standard_parts().result.text.isBlank())
    return ExecuteSubroutine(ReferenceByName(subroutineName), toPosition(considerPosition))
}

private fun CsEVALContext.toAst(considerPosition: Boolean = true): EvalStmt {
    return EvalStmt(this.fixedexpression.expression().toAst(considerPosition), toPosition(considerPosition))
}

private fun CsCALLContext.toAst(considerPosition: Boolean = true): CallStmt {
    require(this.cspec_fixed_standard_parts().factor().factorContent().size == 1)
    val literal = this.cspec_fixed_standard_parts().factor().factorContent()[0].literal()
    return CallStmt(literal.toAst(considerPosition), toPosition(considerPosition))
}
