package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.ast.*
import com.smeup.rpgparser.ast.AssignmentOperator.DIVIDE_ASSIGNMENT
import com.smeup.rpgparser.ast.AssignmentOperator.NORMAL_ASSIGNMENT
import com.smeup.rpgparser.interpreter.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import org.antlr.v4.runtime.ParserRuleContext

data class ToAstConfiguration(val considerPosition: Boolean = true, 
                              val compileTimeInterpreter : CompileTimeInterpreter = CommonCompileTimeInterpreter)

fun List<Node>.position() : Position? {
    val start = this.map { it.position?.start }.filterNotNull().sorted()
    val end = this.map { it.position?.end }.filterNotNull().sorted()
    return if (start.isEmpty() || end.isEmpty()) {
        null
    } else {
        Position(start.first(), end.last())
    }
}

fun RContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()) : CompilationUnit {
    val dataDefinitions = this.statement()
            .mapNotNull {
                when {
                    it.dspec() != null -> it.dspec().toAst(conf)
                    it.dcl_ds() != null -> it.dcl_ds().toAst(conf)
                    else -> null
                }
            }
    val mainStmts = this.statement().mapNotNull {
        when {
            it.cspec_fixed() != null -> it.cspec_fixed().toAst(conf)
            it.block() != null -> it.block().toAst(conf)
            else -> null
        }
    }
    val subroutines = this.subroutine().map { it.toAst(conf) }
    return CompilationUnit(
            dataDefinitions,
            MainBody(mainStmts, if (conf.considerPosition) mainStmts.position() else null),
            subroutines,
            position = this.toPosition(conf.considerPosition))
}

internal fun SubroutineContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Subroutine {
    return Subroutine(this.begsr().csBEGSR().factor1.content.text,
            this.statement().map { it.toAst(conf) },
            toPosition(conf.considerPosition))
}

internal fun SimpleExpressionContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Expression {
    return when {
        this.number() != null -> this.number()!!.toAst(conf)
        this.identifier() != null -> this.identifier().toAst(conf)
        this.bif() != null -> this.bif().toAst(conf)
        this.literal() != null -> this.literal().toAst(conf)
        else -> TODO(this.javaClass.canonicalName)
    }
}

fun ExpressionContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Expression {
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
        this.MULT() != null -> MultExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        this.DIV() != null -> DivExpr(this.expression(0).toAst(conf), this.expression(1).toAst(conf))
        // FIXME it is rather ugly that we have to do this: we should get a different parse tree here
        this.children.size == 3 && this.children[0].text == "(" && this.children[2].text == ")"
                && this.children[1] is ExpressionContext -> (this.children[1] as ExpressionContext).toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun FunctionContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): FunctionCall {
    return FunctionCall(ReferenceByName(this.functionName().text), this.args().expression().map { it.toAst(conf) }, toPosition(conf.considerPosition))
}

internal fun LiteralContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Expression {
    return StringLiteral(this.content?.text ?: "", toPosition(conf.considerPosition))
}

internal fun BifContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Expression {
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

internal fun Bif_charContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): CharExpr {
    return CharExpr(
            this.expression().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun Bif_decContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): DecExpr {
    return DecExpr(
            this.expression(0).toAst(conf),
            this.expression(1).toAst(conf),
            this.expression(2).toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun Bif_lenContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): LenExpr {
    return LenExpr(
            this.expression().toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun Bif_substContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SubstExpr {
    return SubstExpr(
            this.string.toAst(conf),
            this.start.toAst(conf),
            this.length?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun Bif_trimContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): TrimExpr {
    return TrimExpr(
            this.string.toAst(conf),
            this.trimcharacters?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun Bif_scanContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ScanExpr {
    return ScanExpr(
            this.searcharg.toAst(conf),
            this.source.toAst(conf),
            this.start?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun Bif_xlateContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): TranslateExpr {
    return TranslateExpr(
            this.from.toAst(conf),
            this.to.toAst(conf),
            this.string.toAst(conf),
            this.startpos?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun Bif_lookupContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): LookupExpr {
    return LookupExpr(
            this.bif_lookupargs().arg.toAst(conf),
            this.bif_lookupargs().array.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun NumberContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()) : NumberLiteral {
    require(this.NumberPart().isEmpty())
    require(this.MINUS() == null)
    val text = this.NUMBER().text
    return if (text.contains('.')) {
        RealLiteral(text.toDouble(), this.toPosition(conf.considerPosition))
    } else {
        IntLiteral(text.toLong(), this.toPosition(conf.considerPosition))
    }
}

internal fun IdentifierContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()) : Expression {
    return when (this.text.toUpperCase()) {
        "*BLANK", "*BLANKS" -> BlanksRefExpr(toPosition(conf.considerPosition))
        "*ZERO", "*ZEROS" -> TODO()
        "*HIVAL" -> HiValExpr(toPosition(conf.considerPosition))
        "*LOWVAL" -> LowValExpr(toPosition(conf.considerPosition))
        "*ON" -> OnRefExpr(toPosition(conf.considerPosition))
        "*OFF" -> OffRefExpr(toPosition(conf.considerPosition))
        else -> when {
            this.text.indicatorIndex() != null -> PredefinedIndicatorExpr(
                    this.text.indicatorIndex()!!,
                    toPosition(conf.considerPosition))
            else -> DataRefExpr(variable = ReferenceByName(this.text), position = toPosition(conf.considerPosition))
        }
    }
}

internal fun String.indicatorIndex() : Int? {
    return if (this.startsWith("*IN")) {
        this.substring("*IN".length).toIntOrNull()
    } else {
        null
    }
}

internal fun String.isInt() = this.toIntOrNull() != null

internal fun DspecContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()) : DataDefinition {
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
            like = it.simpleExpression().toAst(conf) as AssignableExpression
        }
        it.keyword_inz()?.let {
            initializationValue = it.simpleExpression().toAst(conf)
        }
        it.keyword_dim()?.let {
            dim = it.simpleExpression().toAst(conf)
        }
    }
    val elementSize = when {
        like != null -> conf.compileTimeInterpreter.evaluateElementSizeOf(this.rContext(), like!!)
        else -> this.TO_POSITION().text.trim().let { if (it.isBlank()) null else it.toInt() }
    }

    val baseType = when (this.DATA_TYPE()?.text?.trim()) {
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
    val type = if (dim != null) {
        ArrayType(baseType, conf.compileTimeInterpreter.evaluate(this.rContext(), dim!!).asInt().value.toInt())
    } else {
        baseType
    }
    return DataDefinition(
            this.ds_name().text,
            type,
            initializationValue = initializationValue,
            position = this.toPosition(true))
}

internal fun ParserRuleContext.rContext(): RContext {
    return if (this.parent == null) {
        this as RContext
    } else {
        (this.parent as ParserRuleContext).rContext()
    }
}

val Dcl_dsContext.nameIsInFirstLine : Boolean
    get() {
        return this.ds_name().text.trim().isNotEmpty()
    }

val Dcl_dsContext.name : String
    get() {
        return if (nameIsInFirstLine) {
            this.ds_name().text.trim()
        } else {
            require(this.parm_fixed().isNotEmpty())
            val header = this.parm_fixed().first()
            header.ds_name().text
        }
    }

val Dcl_dsContext.hasHeader : Boolean
    get() {
        return this.ds_name().text.trim().isEmpty()
    }

fun Dcl_dsContext.type(size: Int? = null, conf : ToAstConfiguration = ToAstConfiguration()) : Type {
    val header = this.parm_fixed().first()
    val dim : Expression? = header.keyword().mapNotNull { it.keyword_dim()?.simpleExpression()?.toAst(conf) }.firstOrNull()
    val nElements = if (dim != null) conf.compileTimeInterpreter.evaluate(this.rContext(), dim!!).asInt().value.toInt() else null
    val others = this.parm_fixed().drop(if (nameIsInFirstLine) 0 else 1)
    val elementSize = this.elementSizeOf()
    val baseType = DataStructureType(others.map { it.toFieldType() }, size ?: elementSize)
    return if (nElements == null) {
        baseType
    } else {
        ArrayType(baseType, nElements)
    }
}

internal fun Dcl_dsContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()) : DataDefinition {
    val size = if (this.TO_POSITION().text.trim().isNotEmpty()) {
        this.TO_POSITION().text.trim().toInt()
    } else {
        null
    }
    require(this.parm_fixed().isNotEmpty())
    val others = this.parm_fixed().drop(if (this.hasHeader) 1 else 0)
    val type : Type = this.type(size)
    val nElements = if (type is ArrayType) {
        type.nElements
    } else {
        null
    }
    return DataDefinition(
            this.name,
            type,
            fields = others.map { it.toAst(nElements, conf) },
            position = this.toPosition(true))
}

fun Parm_fixedContext.explicitStartOffset() : Int? {
    val text = this.FROM_POSITION().text.trim()
    return if (text.isBlank()) {
        null
    } else {
        text.toInt()
    }
}

fun Parm_fixedContext.explicitEndOffset() : Int? {
    val text = this.TO_POSITION().text.trim()
    return if (text.isBlank()) {
        null
    } else {
        text.toInt()
    }
}

internal fun Parm_fixedContext.toAst(
        nElements: Int?,
        conf : ToAstConfiguration = ToAstConfiguration()): FieldDefinition {
    var baseType = this.toType()
    if (nElements != null) {
        baseType = ArrayType(baseType, nElements)
    }
    return FieldDefinition(this.ds_name().text,
            baseType,
            explicitStartOffset = this.explicitStartOffset(),
            explicitEndOffset = if (explicitStartOffset() != null) this.explicitEndOffset() else null,
            position = this.toPosition(conf.considerPosition))
}

internal fun Parm_fixedContext.toType(): Type {
    val startPosition = this.explicitStartOffset()
    val endPosition = this.explicitEndOffset()
    val elementSize = when {
        startPosition == null -> endPosition
        endPosition == null -> endPosition
        else -> endPosition - startPosition.toInt() + 1
    }

    var baseType = when (this.DATA_TYPE()?.text?.trim()) {
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
    return baseType
}

internal fun Parm_fixedContext.toFieldType(): FieldType {
    return FieldType(this.ds_name().text, this.toType())
}


fun StatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed() != null -> this.cspec_fixed().toAst(conf)
        this.block() != null -> this.block().toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun BlockContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.ifstatement() != null -> this.ifstatement().toAst(conf)
        this.selectstatement() != null -> this.selectstatement().toAst(conf)
        this.begindo() != null -> {
            val result = this.begindo().csDO().cspec_fixed_standard_parts().result
            val iter = if (result.text.isBlank()) null else result.toAst(conf) as AssignableExpression
            val factor = this.begindo().factor()
            val start = if (factor.text.isBlank()) IntLiteral(1) else factor.content.toAst(conf)
            DoStmt(
                    this.begindo().csDO().cspec_fixed_standard_parts().factor2.symbolicConstants().toAst(conf),
                    iter,
                    this.statement().map { it.toAst(conf) },
                    start,
                    position = toPosition(conf.considerPosition))
        }
        this.forstatement() != null -> this.forstatement().toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun FactorContentContext.toAst(conf: ToAstConfiguration): Expression {
    return IntLiteral(this.CS_FactorContent().text.toLong(), position = toPosition(conf.considerPosition))
}

internal fun SymbolicConstantsContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()) : Expression {
    return when {
        this.SPLAT_HIVAL() != null -> HiValExpr(toPosition(conf.considerPosition))
        this.SPLAT_LOVAL() != null -> LowValExpr(toPosition(conf.considerPosition))
        else -> TODO()
    }
}

internal fun ForstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ForStmt {
    val assignment = this.beginfor().csFOR().expression(0).toAst(conf)
    val endValue = this.beginfor().csFOR().expression(1).toAst(conf)
    return ForStmt(
            assignment,
            endValue,
            this.statement().map { it.toAst(conf) },
            toPosition(conf.considerPosition))
}

internal fun SelectstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SelectStmt {
    val whenClauses = this.whenstatement().map { it.toAst(conf) }
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    val statementsOfLastWhen = if (this.whenstatement().isEmpty())
            emptyList()
        else
            this.whenstatement().last().statement().map { it.toAst(conf) }
    val indexOfOther = statementsOfLastWhen.indexOfFirst { it is OtherStmt }
    var other : SelectOtherClause? = null
    if (indexOfOther != -1) {
        val otherPosition = if (conf.considerPosition) {
            Position(statementsOfLastWhen[indexOfOther].position!!.start, statementsOfLastWhen.last().position!!.end)
        } else {
            null
        }
        other = SelectOtherClause(statementsOfLastWhen.subList(indexOfOther + 1, statementsOfLastWhen.size), position = otherPosition)
    }

    return SelectStmt(whenClauses, other, toPosition(conf.considerPosition))
}

internal fun WhenstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SelectCase {
    // Unfortunately the other clause ends up being part of the when clause so we should
    // unfold it
    // TODO change this in the grammar
    var statementsToConsider = this.statement().map { it.toAst(conf) }
    val indexOfOther = statementsToConsider.indexOfFirst { it is OtherStmt }
    if (indexOfOther != -1) {
        statementsToConsider = statementsToConsider.subList(0, indexOfOther)
    }
    return SelectCase(
            this.`when`().csWHEN().fixedexpression.expression().toAst(conf),
            statementsToConsider,
            toPosition(conf.considerPosition)
    )
}

internal fun OtherContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SelectOtherClause {
    TODO()
}

internal fun IfstatementContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): IfStmt {
    return IfStmt(this.beginif().fixedexpression.expression().toAst(conf),
            this.thenBody.map { it.toAst(conf) },
            this.elseIfClause().map { it.toAst(conf) },
            this.elseClause()?.toAst(conf),
            toPosition(conf.considerPosition))
}

internal fun ElseClauseContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ElseClause {
    return ElseClause(this.statement().map { it.toAst(conf) }, toPosition(conf.considerPosition))
}

internal fun ElseIfClauseContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ElseIfClause {
    return ElseIfClause(
            this.elseifstmt().fixedexpression.expression().toAst(conf),
            this.statement().map { it.toAst(conf) }, toPosition(conf.considerPosition))
}

internal fun Cspec_fixedContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed_standard() != null -> this.cspec_fixed_standard().toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun Cspec_fixed_standardContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.csEXSR() != null -> this.csEXSR().toAst(conf)
        this.csEVAL() != null -> this.csEVAL().toAst(conf)
        this.csCALL() != null -> this.csCALL().toAst(conf)
        this.csSETON() != null -> this.csSETON().toAst(conf)
        this.csPLIST() != null -> this.csPLIST().toAst(conf)
        this.csCLEAR() != null -> this.csCLEAR().toAst(conf)
        this.csLEAVE() != null -> LeaveStmt(toPosition(conf.considerPosition))
        this.csITER() != null -> IterStmt(toPosition(conf.considerPosition))
        this.csOTHER() != null -> OtherStmt(toPosition(conf.considerPosition))
        this.csDSPLY() != null -> this.csDSPLY().toAst(conf)
        else -> TODO("${this.text.toString()} at ${this.toPosition(true)}")
    }
}

// FIXME: This is very, very, very ugly. It should be fixed by parsing this properly
//        in the grammar
internal fun referenceToExpression(text: String, position: Position?) : Expression {
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

internal fun CsDSPLYContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): DisplayStmt {
    val expression = this.cspec_fixed_standard_parts().result.toAst(conf)
    return DisplayStmt(expression, toPosition(conf.considerPosition))
}

internal fun ResultTypeContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Expression {
    // TODO this should have been parsed differently because here we have to figure out
    // what kind of expression is this
    return DataRefExpr(ReferenceByName(this.text), toPosition(conf.considerPosition))
}

internal fun CsCLEARContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ClearStmt {
    val name = this.cspec_fixed_standard_parts().result.text
    var dataDeclaration : InStatementDataDefinition? = null
    if (!this.cspec_fixed_standard_parts().len.text.isBlank()) {
        val length = this.cspec_fixed_standard_parts().len.text.trim().toInt()
        dataDeclaration = InStatementDataDefinition(name, StringType(length.toLong()), toPosition(conf.considerPosition))
    }
    return ClearStmt(
            referenceToExpression(name, toPosition(conf.considerPosition)),
            dataDeclaration,
            toPosition(conf.considerPosition))
}



internal fun CsPLISTContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): PlistStmt {
    val isEntry = ((this.parent as Cspec_fixed_standardContext).parent as Cspec_fixedContext).factor().symbolicConstants().SPLAT_ENTRY() != null
    return PlistStmt(
            this.csPARM().map { it.toAst(conf) },
            isEntry,
            toPosition(conf.considerPosition)
    )
}

internal fun CsPARMContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): PlistParam {
    val paramName = this.cspec_fixed_standard_parts().result.CS_FactorContent().text
    return PlistParam(ReferenceByName(paramName), toPosition(conf.considerPosition))
}

internal fun CsSETONContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): SetOnStmt {
    return SetOnStmt(indicators(this.cspec_fixed_standard_parts()), toPosition(conf.considerPosition))
}

internal fun indicators(cspecs: Cspec_fixed_standard_partsContext) : List<DataWrapUpChoice> {
    return listOf(cspecs.hi, cspecs.lo, cspecs.eq)
            .map { it.text }
            .filter { !it.isNullOrBlank() }
            .map (String::toUpperCase)
            .map(DataWrapUpChoice::valueOf)
}

internal fun CsEXSRContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): ExecuteSubroutine {
    val subroutineName = this.cspec_fixed_standard_parts().factor2.text
    require(this.cspec_fixed_standard_parts().decimalPositions.text.isBlank())
    require(this.cspec_fixed_standard_parts().eq.text.isBlank())
    require(this.cspec_fixed_standard_parts().hi.text.isBlank())
    require(this.cspec_fixed_standard_parts().len.text.isBlank())
    require(this.cspec_fixed_standard_parts().lo.text.isBlank())
    require(this.cspec_fixed_standard_parts().result.text.isBlank())
    return ExecuteSubroutine(ReferenceByName(subroutineName), toPosition(conf.considerPosition))
}

internal fun CsEVALContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): EvalStmt {
    return EvalStmt(
            this.target().toAst(conf),
            this.fixedexpression.expression().toAst(conf),
            operator=this.operator.toAssignmentOperator(),
            position=toPosition(conf.considerPosition))
}

internal fun TargetContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): AssignableExpression {
    return when (this) {
        is SimpleTargetContext -> DataRefExpr(ReferenceByName(this.name.text), toPosition(conf.considerPosition))
        is IndexedTargetContext -> ArrayAccessExpr(array=this.base.toAst(conf),
                index = this.index.toAst(conf),
                position = toPosition(conf.considerPosition))
        else -> TODO()
    }
}

internal fun AssignmentOperatorIncludingEqualContext.toAssignmentOperator(): AssignmentOperator {
    return when {
        this.CDIV() != null -> DIVIDE_ASSIGNMENT
        this.EQUAL() != null -> NORMAL_ASSIGNMENT
        else -> throw UnsupportedOperationException(this.text)
    }
}

internal fun CsCALLContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): CallStmt {
    require(this.cspec_fixed_standard_parts().factor().factorContent().size == 1)
    val literal = this.cspec_fixed_standard_parts().factor().factorContent()[0].literal()
    return CallStmt(literal.toAst(conf),
            this.csPARM().map { it.toAst(conf) },
            toPosition(conf.considerPosition))
}

