package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.ast.*
import com.smeup.rpgparser.ast.AssignmentOperator.DIVIDE_ASSIGNMENT
import com.smeup.rpgparser.ast.AssignmentOperator.NORMAL_ASSIGNMENT
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
    val dataDefinitions = this.statement()
            .mapNotNull {
                when {
                    it.dspec() != null -> it.dspec().toAst(considerPosition)
                    it.dcl_ds() != null -> it.dcl_ds().toAst(considerPosition)
                    else -> null
                }
            }
    val mainStmts = this.statement().mapNotNull {
        when {
            it.cspec_fixed() != null -> it.cspec_fixed().toAst(considerPosition)
            it.block() != null -> it.block().toAst(considerPosition)
            else -> null
        }
    }
    val subroutines = this.subroutine().map { it.toAst(considerPosition) }
    return CompilationUnit(
            dataDefinitions,
            MainBody(mainStmts, if (considerPosition) mainStmts.position() else null),
            subroutines,
            position = this.toPosition(considerPosition))
}

private fun SubroutineContext.toAst(considerPosition: Boolean = true): Subroutine {
    return Subroutine(this.begsr().csBEGSR().factor1.content.text,
            this.statement().map { it.toAst(considerPosition) },
            toPosition(considerPosition))
}

private fun DspecContext.arrayLength(considerPosition : Boolean = true) : Expression? {
    return this.keyword().arrayLength(considerPosition)
}

private fun Dcl_dsContext.arrayLength(considerPosition : Boolean = true) : Expression? {
    return this.keyword().arrayLength(considerPosition)
}

private fun Parm_fixedContext.arrayLength(considerPosition : Boolean = true) : Expression? {
    return this.keyword().arrayLength(considerPosition)
}

private fun List<KeywordContext>.arrayLength(considerPosition : Boolean = true) : Expression? {
    val dims = this.filter { it.keyword_dim() != null }.map { it.keyword_dim() }
    return when (dims.size) {
        0 -> null
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
        this.OR() != null -> LogicalOrExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
        this.AND() != null -> LogicalAndExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
        this.comparisonOperator() != null -> when {
            this.comparisonOperator().GT() != null -> GreaterThanExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
            this.comparisonOperator().GE() != null -> GreaterEqualThanExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
            this.comparisonOperator().LT() != null -> LessThanExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
            this.comparisonOperator().LE() != null -> LessEqualThanExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
            this.comparisonOperator().NE() != null -> DifferentThanExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
            else -> TODO("ComparisonOperator ${this.comparisonOperator().text}")
        }
        this.function() != null -> this.function().toAst(considerPosition)
        this.NOT() != null -> NotExpr(this.expression(0).toAst(considerPosition), toPosition(considerPosition))
        this.PLUS() != null -> PlusExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
        this.MINUS() != null -> MinusExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
        this.MULT() != null -> MultExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
        this.DIV() != null -> DivExpr(this.expression(0).toAst(considerPosition), this.expression(1).toAst(considerPosition))
        // FIXME it is rather ugly that we have to do this: we should get a different parse tree here
        this.children.size == 3 && this.children[0].text == "(" && this.children[2].text == ")"
                && this.children[1] is ExpressionContext -> (this.children[1] as ExpressionContext).toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun FunctionContext.toAst(considerPosition : Boolean = true): FunctionCall {
    return FunctionCall(ReferenceByName(this.functionName().text), this.args().expression().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

private fun LiteralContext.toAst(considerPosition : Boolean = true): Expression {
    return StringLiteral(this.content?.text ?: "", toPosition(considerPosition))
}

private fun BifContext.toAst(considerPosition : Boolean = true): Expression {
    return when {
        this.bif_elem() != null -> NumberOfElementsExpr(this.bif_elem().expression().toAst(considerPosition), position = toPosition(considerPosition))
        this.bif_lookup() != null -> this.bif_lookup().toAst(considerPosition)
        this.bif_xlate() != null -> this.bif_xlate().toAst(considerPosition)
        this.bif_scan() != null -> this.bif_scan().toAst(considerPosition)
        this.bif_trim() != null -> this.bif_trim().toAst(considerPosition)
        this.bif_subst() != null -> this.bif_subst().toAst(considerPosition)
        this.bif_len() != null -> this.bif_len().toAst(considerPosition)
        this.bif_dec() != null -> this.bif_dec().toAst(considerPosition)
        this.bif_char() != null -> this.bif_char().toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun Bif_charContext.toAst(considerPosition: Boolean = true): CharExpr {
    return CharExpr(
            this.expression().toAst(considerPosition),
            toPosition(considerPosition))
}

private fun Bif_decContext.toAst(considerPosition: Boolean = true): DecExpr {
    return DecExpr(
            this.expression(0).toAst(considerPosition),
            this.expression(1).toAst(considerPosition),
            this.expression(2).toAst(considerPosition),
            toPosition(considerPosition))
}

private fun Bif_lenContext.toAst(considerPosition: Boolean = true): LenExpr {
    return LenExpr(
            this.expression().toAst(considerPosition),
            toPosition(considerPosition))
}

private fun Bif_substContext.toAst(considerPosition: Boolean = true): SubstExpr {
    return SubstExpr(
            this.string.toAst(considerPosition),
            this.start.toAst(considerPosition),
            this.length?.toAst(considerPosition),
            toPosition(considerPosition))
}

private fun Bif_trimContext.toAst(considerPosition: Boolean = true): TrimExpr {
    return TrimExpr(
            this.string.toAst(considerPosition),
            this.trimcharacters?.toAst(considerPosition),
            toPosition(considerPosition))
}

private fun Bif_scanContext.toAst(considerPosition: Boolean = true): ScanExpr {
    return ScanExpr(
            this.searcharg.toAst(considerPosition),
            this.source.toAst(considerPosition),
            this.start?.toAst(considerPosition),
            toPosition(considerPosition))
}

private fun Bif_xlateContext.toAst(considerPosition: Boolean = true): TranslateExpr {
    return TranslateExpr(
            this.from.toAst(considerPosition),
            this.to.toAst(considerPosition),
            this.string.toAst(considerPosition),
            this.startpos?.toAst(considerPosition),
            toPosition(considerPosition))
}

private fun Bif_lookupContext.toAst(considerPosition: Boolean = true): LookupExpr {
    return LookupExpr(
            this.bif_lookupargs().arg.toAst(considerPosition),
            this.bif_lookupargs().array.toAst(considerPosition),
            toPosition(considerPosition))
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

private fun IdentifierContext.toAst(considerPosition : Boolean = true) : Expression {
    return when (this.text) {
        "*BLANK", "*BLANKS" -> BlanksRefExpr(toPosition(considerPosition))
        "*ZERO", "*ZEROS" -> TODO()
        "*HIVAL" -> TODO()
        "*LOWVAL" -> TODO()
        "*ON" -> OnRefExpr(toPosition(considerPosition))
        "*OFF" -> OffRefExpr(toPosition(considerPosition))
        else -> when {
            this.text.indicatorIndex() != null -> PredefinedIndicatorExpr(
                    this.text.indicatorIndex()!!,
                    toPosition(considerPosition))
            else -> DataRefExpr(variable = ReferenceByName(this.text), position = toPosition(considerPosition))
        }
    }
}

private fun String.indicatorIndex() : Int? {
    return if (this.startsWith("*IN")) {
        this.substring("*IN".length).toIntOrNull()
    } else {
        null
    }
}

private fun String.isInt() = this.toIntOrNull() != null

private fun DspecContext.toAst(considerPosition : Boolean = true) : DataDefinition {
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

    var like : AssignableExpression? = null
    var dim : Expression? = null
    var initializationValue : Expression? = null
    this.keyword().forEach {
        it.keyword_like()?.let {
            like = it.simpleExpression().toAst(considerPosition) as AssignableExpression
        }
        it.keyword_inz()?.let {
            initializationValue = it.simpleExpression().toAst(considerPosition)
        }
        it.keyword_dim()?.let {
            dim = it.simpleExpression().toAst(considerPosition)
        }
    }
    val elementSize = when {
        else -> this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    }

    val type = when (this.DATA_TYPE()?.text?.trim()) {
        null -> TODO()
        "" -> if (this.DECIMAL_POSITIONS().text.isNotBlank()) {
            val decimalPositions = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() }
            NumberType(elementSize!! - decimalPositions, decimalPositions)
        } else {
            StringType(elementSize!!.toLong())
        }
        "N" -> BooleanType
        else -> throw UnsupportedOperationException("<${this.DATA_TYPE().text}>")
    }
    return DataDefinition(
            this.ds_name().text,
            type,
            this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() },
            decimals = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() },
            arrayLength = this.arrayLength(considerPosition),
            like = like,
            initializationValue = initializationValue,
            position = this.toPosition(true))
}

private fun Dcl_dsContext.toAst(considerPosition : Boolean = true) : DataDefinition {
    require(this.TO_POSITION().text.trim().isEmpty())
    if (this.ds_name().text.trim().isEmpty()) {
        require(this.parm_fixed().isNotEmpty())
        val header = this.parm_fixed().first()
        val others = this.parm_fixed().drop(1)
        TODO()
//        return DataDefinition(
//                header.ds_name().text,
//                //TODO DATA_STRUCTURE,
//                header.TO_POSITION().text.trim().toInt(),
//                decimals = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() },
//                arrayLength = header.arrayLength(considerPosition),
//                fields = others.map { it.toAst(considerPosition) },
//                position = this.toPosition(true))
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
    return FieldDefinition(this.ds_name().text, this.TO_POSITION().text.trim().toInt(), position = this.toPosition(considerPosition))
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
        this.begindo() != null -> DoStmt(this.statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
        this.forstatement() != null -> this.forstatement().toAst(considerPosition)
        else -> TODO(this.text.toString())
    }
}

private fun ForstatementContext.toAst(considerPosition: Boolean = true): ForStmt {
    val assignment = this.beginfor().csFOR().expression(0).toAst(considerPosition)
    val endValue = this.beginfor().csFOR().expression(1).toAst(considerPosition)
    return ForStmt(
            assignment,
            endValue,
            this.statement().map { it.toAst(considerPosition) },
            toPosition(considerPosition))
}

private fun SelectstatementContext.toAst(considerPosition: Boolean = true): SelectStmt {
    val whenClauses = this.whenstatement().map { it.toAst(considerPosition) }
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    val statementsOfLastWhen = if (this.whenstatement().isEmpty())
            emptyList()
        else
            this.whenstatement().last().statement().map { it.toAst(considerPosition) }
    val indexOfOther = statementsOfLastWhen.indexOfFirst { it is OtherStmt }
    var other : SelectOtherClause? = null
    if (indexOfOther != -1) {
        val otherPosition = if (considerPosition) {
            Position(statementsOfLastWhen[indexOfOther].position!!.start, statementsOfLastWhen.last().position!!.end)
        } else {
            null
        }
        other = SelectOtherClause(statementsOfLastWhen.subList(indexOfOther + 1, statementsOfLastWhen.size), position = otherPosition)
    }

    return SelectStmt(whenClauses, other, toPosition(considerPosition))
}

private fun WhenstatementContext.toAst(considerPosition: Boolean = true): SelectCase {
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    var statementsToConsider = this.statement().map { it.toAst(considerPosition) }
    val indexOfOther = statementsToConsider.indexOfFirst { it is OtherStmt }
    if (indexOfOther != -1) {
        statementsToConsider = statementsToConsider.subList(0, indexOfOther)
    }
    return SelectCase(
            this.`when`().csWHEN().fixedexpression.expression().toAst(considerPosition),
            statementsToConsider,
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
        this.csSETON() != null -> this.csSETON().toAst(considerPosition)
        this.csPLIST() != null -> this.csPLIST().toAst(considerPosition)
        this.csCLEAR() != null -> this.csCLEAR().toAst(considerPosition)
        this.csLEAVE() != null -> LeaveStmt(toPosition(considerPosition))
        this.csITER() != null -> IterStmt(toPosition(considerPosition))
        this.csOTHER() != null -> OtherStmt(toPosition(considerPosition))
        this.csDSPLY() != null -> this.csDSPLY().toAst(considerPosition)
        else -> TODO("${this.text.toString()} at ${this.toPosition(true)}")
    }
}

// FIXME: This is very, very, very ugly. It should be fixed by parsing this properly
//        in the grammar
private fun referenceToExpression(text: String, position: Position?) : Expression {
    var expr : Expression = text.indexOf("(").let {
        val varName = if (it == -1) text else text.substring(0, it)
        DataRefExpr(ReferenceByName(varName))
    }
    if (text.contains("(")) {
        // TODO support annidated parenthesis, if necessary
        if (text.substring(text.indexOf("(") + 1).contains("(")) {
            TODO("Support annidated parenthesis")
        }
        val indexText = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"))
        expr = ArrayAccessExpr(expr, IntLiteral(indexText.toLong(),
                if (position == null) null else Position(position.start.plus(text.substring(0, text.indexOf("("))),
                        position.start.plus(text.substring(0, text.lastIndexOf(")"))))))
    }
    return expr
}

private fun CsDSPLYContext.toAst(considerPosition: Boolean = true): DisplayStmt {
    val expression = this.cspec_fixed_standard_parts().result.toAst(considerPosition)
    return DisplayStmt(expression, toPosition(considerPosition))
}

private fun ResultTypeContext.toAst(considerPosition: Boolean = true): Expression {
    // TODO this should have been parsed differently because here we have to figure out
    // what kind of expression is this
    return DataRefExpr(ReferenceByName(this.text), toPosition(considerPosition))
}

private fun CsCLEARContext.toAst(considerPosition: Boolean = true): ClearStmt {
    val name = this.cspec_fixed_standard_parts().result.text
    var dataDeclaration : InStatementDataDefinition? = null
    if (!this.cspec_fixed_standard_parts().len.text.isBlank()) {
        val length = this.cspec_fixed_standard_parts().len.text.trim().toInt()
        dataDeclaration = InStatementDataDefinition(name, length, toPosition(considerPosition))
    }
    return ClearStmt(
            referenceToExpression(name, toPosition(considerPosition)),
            dataDeclaration,
            toPosition(considerPosition))
}



private fun CsPLISTContext.toAst(considerPosition: Boolean = true): PlistStmt {
    return PlistStmt(
            this.csPARM().map { it.toAst(considerPosition) },
            toPosition(considerPosition)
    )
}

private fun CsPARMContext.toAst(considerPosition: Boolean = true): PlistParam {
    val paramName = this.cspec_fixed_standard_parts().result.CS_FactorContent().text
    return PlistParam(paramName, toPosition(considerPosition))
}

private fun CsSETONContext.toAst(considerPosition: Boolean = true): SetOnStmt {
    return SetOnStmt(indicators(this.cspec_fixed_standard_parts()), toPosition(considerPosition))
}

private fun indicators(cspecs: Cspec_fixed_standard_partsContext) : List<DataWrapUpChoice> {
    return listOf(cspecs.hi, cspecs.lo, cspecs.eq)
            .map { it.text }
            .filter { !it.isNullOrBlank() }
            .map (String::toUpperCase)
            .map(DataWrapUpChoice::valueOf)
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
    return EvalStmt(
            this.target().toAst(considerPosition),
            this.fixedexpression.expression().toAst(considerPosition),
            operator=this.operator.toAssignmentOperator(),
            position=toPosition(considerPosition))
}

private fun TargetContext.toAst(considerPosition: Boolean = true): AssignableExpression {
    return when (this) {
        is SimpleTargetContext -> DataRefExpr(ReferenceByName(this.name.text), toPosition(considerPosition))
        is IndexedTargetContext -> ArrayAccessExpr(array=this.base.toAst(considerPosition),
                index = this.index.toAst(considerPosition),
                position = toPosition(considerPosition))
        else -> TODO()
    }
}

private fun AssignmentOperatorIncludingEqualContext.toAssignmentOperator(): AssignmentOperator {
    return when {
        this.CDIV() != null -> DIVIDE_ASSIGNMENT
        this.EQUAL() != null -> NORMAL_ASSIGNMENT
        else -> throw UnsupportedOperationException(this.text)
    }
}

private fun CsCALLContext.toAst(considerPosition: Boolean = true): CallStmt {
    require(this.cspec_fixed_standard_parts().factor().factorContent().size == 1)
    val literal = this.cspec_fixed_standard_parts().factor().factorContent()[0].literal()
    return CallStmt(literal.toAst(considerPosition), toPosition(considerPosition))
}
