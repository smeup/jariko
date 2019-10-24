package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.facade.findAllDescendants
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.*
import java.lang.IllegalStateException
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.util.*

data class ToAstConfiguration(
    val considerPosition: Boolean = true,
    val compileTimeInterpreter: CompileTimeInterpreter = CommonCompileTimeInterpreter
)

fun List<Node>.position(): Position? {
    val start = this.asSequence().map { it.position?.start }.filterNotNull().sorted().toList()
    val end = this.asSequence().map { it.position?.end }.filterNotNull().sorted().toList()
    return if (start.isEmpty() || end.isEmpty()) {
        null
    } else {
        Position(start.first(), end.last())
    }
}

internal interface DataDefinitionProvider {
    fun isReady(): Boolean
    fun toDataDefinition(): DataDefinition
}
private data class DataDefinitionHolder(val dataDefinition: DataDefinition) : DataDefinitionProvider {
    override fun isReady() = true
    override fun toDataDefinition() = dataDefinition
}
private data class DataDefinitionCalculator(val calculator: () -> DataDefinition) : DataDefinitionProvider {
    override fun isReady() = false
    override fun toDataDefinition() = calculator()
}

private fun RContext.getDataDefinitions(conf: ToAstConfiguration = ToAstConfiguration()): List<DataDefinition> {
    // We need to calculate first all the data definitions which do not contain the LIKE DS directives
    // then we calculate the ones with the LIKE DS clause, as they could have references to DS declared
    // after them
    var dataDefinitionProviders: MutableList<DataDefinitionProvider> = LinkedList<DataDefinitionProvider>()
    dataDefinitionProviders.addAll(this.statement()
            .mapNotNull {
                when {
                    it.dspec() != null -> DataDefinitionHolder(it.dspec().toAst(conf))
                    it.dcl_ds() != null -> if (it.dcl_ds().useLikeDs()) {
                        DataDefinitionCalculator(it.dcl_ds().toAstWithLikeDs(conf, dataDefinitionProviders))
                    } else {
                        DataDefinitionHolder(it.dcl_ds().toAst(conf))
                    }
                    else -> null
                }
            })
    return dataDefinitionProviders.map { it.toDataDefinition() }
}

fun RContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompilationUnit {
    val fileDefinitions = this.statement()
            .mapNotNull {
                when {
                    it.fspec_fixed() != null -> it.fspec_fixed().toAst(conf)
                    else -> null
                }
            }
    val dataDefinitions = getDataDefinitions(conf)

    val mainStmts = this.statement().mapNotNull {
        when {
            it.cspec_fixed() != null -> it.cspec_fixed().toAst(conf)
            it.block() != null -> it.block().toAst(conf)
            else -> null
        }
    }
    val subroutines = this.subroutine().map { it.toAst(conf) }
    val compileTimeArrays = this.endSourceBlock()?.endSource()?.map { it.toAst(conf) } ?: emptyList()
    val directives = this.findAllDescendants(Hspec_fixedContext::class).map { it.toAst(conf) }
    return CompilationUnit(
            fileDefinitions,
            dataDefinitions,
            MainBody(mainStmts, if (conf.considerPosition) mainStmts.position() else null),
            subroutines,
            compileTimeArrays,
            directives,
            position = this.toPosition(conf.considerPosition))
}

private fun Dcl_dsContext.useLikeDs(): Boolean {
    val keywordLikeDs = this.keyword_likeds()
    if (keywordLikeDs != null) {
        TODO()
    }
    return (this.keyword().any { it.keyword_likeds() != null })
}

internal fun EndSourceContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompileTimeArray {
    fun cName(s: String) = s.substringAfter("CTDATA ").replace("\\s".toRegex(), "")
    return CompileTimeArray(cName(this.endSourceHead().text), // TODO: change grammar to get **CTDATA name
            this.endSourceLine().map { it.endSourceLineText().text },
            toPosition(conf.considerPosition))
}

internal fun SubroutineContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Subroutine {
    return Subroutine(this.begsr().csBEGSR().factor1.content.text,
            this.statement().map { it.toAst(conf) },
            toPosition(conf.considerPosition))
}

internal fun FunctionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when (this.functionName().text.toUpperCase()) {
        "NOT" -> {
            if (this.args().expression().size != 1) {
                throw IllegalStateException("Not should have just one parameter")
            }
            NotExpr(this.args().expression()[0].toAst(conf), toPosition(conf.considerPosition))
        }
        else -> FunctionCall(ReferenceByName(this.functionName().text), this.args().expression().map { it.toAst(conf) }, toPosition(conf.considerPosition))
    }
}

internal fun String.isInt() = this.toIntOrNull() != null

internal fun ParserRuleContext.rContext(): RContext {
    return if (this.parent == null) {
        this as RContext
    } else {
        (this.parent as ParserRuleContext).rContext()
    }
}

internal fun FactorContentContext.toAst(conf: ToAstConfiguration): Expression {
    val position = this.toPosition(conf.considerPosition)

    val l = this.literal()
    if (l != null) {
        return l.toAst(conf)
    }
    val text = this.CS_FactorContent().text
    val regexp = Regex("(\\+|\\-)?(\\d|,|\\.)+(\\+|\\-)?")
    return if (text.matches(regexp)) {
        literalToNumber(text, position)
    } else if (text.startsWith("\'")) {
        StringLiteral(text, position)
    } else {
        referenceToExpression(text, position)
    }
}

fun literalToNumber(
    text: String,
    position: Position?
): Expression {
    return when {
        // When assigning a value to a numeric field we could either use
        // a comma or a dot as decimal separators
        text.contains('.') -> {
            text.toRealLiteral(position, Locale.US)
        }
        text.contains(',') -> {
            text.toRealLiteral(position, Locale.ITALIAN)
        }
        else -> IntLiteral(text.toLong(), position)
    }
}

internal fun SymbolicConstantsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when {
        this.SPLAT_HIVAL() != null -> HiValExpr(toPosition(conf.considerPosition))
        this.SPLAT_LOVAL() != null -> LowValExpr(toPosition(conf.considerPosition))
        else -> TODO()
    }
}

internal fun Cspec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed_standard() != null -> this.cspec_fixed_standard().toAst(conf)
        else -> TODO(this.text.toString())
    }
}

internal fun Cspec_fixed_standardContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
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
        this.csMOVE() != null -> this.csMOVE().toAst(conf)
        this.csMOVEL() != null -> this.csMOVEL().toAst(conf)
        this.csTIME() != null -> this.csTIME().toAst(conf)
        this.csSUBDUR() != null -> this.csSUBDUR().toAst(conf)
        this.csZ_ADD() != null -> this.csZ_ADD().toAst(conf)
        this.csZ_SUB() != null -> this.csZ_SUB().toAst(conf)
        this.csCHAIN() != null -> this.csCHAIN().toAst(conf)
        this.csCHECK() != null -> this.csCHECK().toAst(conf)
        this.csKLIST() != null -> this.csKLIST().toAst(conf)
        this.csREADE() != null -> this.csREADE().toAst(conf)
        this.csCOMP() != null -> this.csCOMP().toAst(conf)
        else -> TODO("${this.text} at ${this.toPosition(true)}")
    }
}

// FIXME: This is very, very, very ugly. It should be fixed by parsing this properly
//        in the grammar
internal fun referenceToExpression(text: String, position: Position?): Expression {
    var expr: Expression = text.indexOf("(").let {
        val varName = if (it == -1) text else text.substring(0, it)
        DataRefExpr(ReferenceByName(varName), position)
    }
    if (text.contains("(")) {
        // TODO support annidated parenthesis, if necessary
        if (text.substring(text.indexOf("(") + 1).contains("(")) {
            TODO("Support annidated parenthesis")
        }
        val indexText = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"))
        val indexValue = indexText.toLongOrNull()
        val indexExpression =
                if (indexValue == null) {
                    DataRefExpr(ReferenceByName(indexText), computeNewPosition(position, text))
                } else {
                    IntLiteral(indexValue, computeNewPosition(position, text))
                }
        expr = ArrayAccessExpr(expr, indexExpression)
    }
    return expr
}

private fun computeNewPosition(position: Position?, text: String) =
    if (position == null) {
        null
    } else {
        Position(position.start.plus(text.substring(0, text.indexOf("("))),
            position.start.plus(text.substring(0, text.lastIndexOf(")"))))
    }

fun ParserRuleContext.factor1Context() = ((this.parent as Cspec_fixed_standardContext).parent as Cspec_fixedContext).factor()

internal fun CsKLISTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): KListStmt {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.text ?: throw UnsupportedOperationException("Line ${position?.line()}: KLIST operation requires factor 1: ${this.text}")
    val fields = this.csKFLD().map {
        it.cspec_fixed_standard_parts().result.text
    }
    return KListStmt(factor1, fields, position)
}

internal fun CsDSPLYContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DisplayStmt {
    val left = if (this.factor1Context()?.content?.text?.isNotBlank() ?: false) {
        this.factor1Context().content.toAst(conf)
    } else {
        null
    }
    val right = if (this.cspec_fixed_standard_parts()?.result?.text?.isNotBlank() ?: false) {
        this.cspec_fixed_standard_parts().result.toAst(conf)
    } else {
        null
    }
    require(left != null || right != null)
    return DisplayStmt(left, right, toPosition(conf.considerPosition))
}

internal fun ResultTypeContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DataRefExpr {
    // TODO this should have been parsed differently because here we have to figure out
    // what kind of expression is this
    return DataRefExpr(ReferenceByName(this.text), toPosition(conf.considerPosition))
}

internal fun CsPLISTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): PlistStmt {
    val isEntry = this.factor1Context().symbolicConstants().SPLAT_ENTRY() != null
    return PlistStmt(
            this.csPARM().map { it.toAst(conf) },
            isEntry,
            toPosition(conf.considerPosition)
    )
}

internal fun CsPARMContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): PlistParam {
    val paramName = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return PlistParam(ReferenceByName(paramName), this.cspec_fixed_standard_parts().toDataDefinition(paramName, position, conf), position)
}

internal fun CsTIMEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TimeStmt {
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return TimeStmt(referenceToExpression(name, toPosition(conf.considerPosition)), position)
}

fun Cspec_fixed_standard_partsContext.factor2Expression(conf: ToAstConfiguration): Expression? {
    return this.factor2?.content?.toAst(conf)
}

internal fun Cspec_fixed_standard_partsContext.toDataDefinition(name: String, position: Position?, conf: ToAstConfiguration): InStatementDataDefinition? {
    val len = this.len.asLong()
    if (len == null) {
        return null
    }
    val decimals = this.decimalPositions.asLong()
    val initialValue = this.factor2Expression(conf)
    return InStatementDataDefinition(name, dataType(len, decimals), position, initializationValue = initialValue)
}

private fun dataType(len: Long, decimals: Long?): Type =
    if (decimals == null) {
        StringType(len)
    } else {
        NumberType(len.toInt() - decimals.toInt(), decimals.toInt())
    }

internal fun Token.asLong(): Long? {
    val tokenString = this.text.trim()
    return if (tokenString.isNotBlank()) {
        tokenString.toLongOrNull()
    } else {
        null
    }
}

internal fun CsSETONContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SetOnStmt {
    return SetOnStmt(indicators(this.cspec_fixed_standard_parts()), toPosition(conf.considerPosition))
}

internal fun indicators(cspecs: Cspec_fixed_standard_partsContext): List<DataWrapUpChoice> {
    return listOf(cspecs.hi, cspecs.lo, cspecs.eq)
            .asSequence()
            .map { it.text }
            .filter { !it.isNullOrBlank() }
            .map(String::toUpperCase)
            .map(DataWrapUpChoice::valueOf)
            .toList()
}

internal fun CsEXSRContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ExecuteSubroutine {
    val subroutineName = this.cspec_fixed_standard_parts().factor2.text
    require(this.cspec_fixed_standard_parts().decimalPositions.text.isBlank())
    require(this.cspec_fixed_standard_parts().eq.text.isBlank())
    require(this.cspec_fixed_standard_parts().hi.text.isBlank())
    require(this.cspec_fixed_standard_parts().len.text.isBlank())
    require(this.cspec_fixed_standard_parts().lo.text.isBlank())
    require(this.cspec_fixed_standard_parts().result.text.isBlank())
    return ExecuteSubroutine(ReferenceByName(subroutineName), toPosition(conf.considerPosition))
}

internal fun CsEVALContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): EvalStmt {
    val extenders = this.operationExtender?.extender?.text?.toUpperCase()?.toCharArray() ?: CharArray(0)
    val flags = EvalFlags(
            halfAdjust = 'H' in extenders,
            maximumNumberOfDigitsRule = 'M' in extenders,
            resultDecimalPositionRule = 'R' in extenders
    )
    return EvalStmt(
            this.target().toAst(conf),
            this.fixedexpression.expression().toAst(conf),
            operator = this.operator.toAssignmentOperator(),
            flags = flags,
            position = toPosition(conf.considerPosition))
}

internal fun CsSUBDURContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubDurStmt {
    val left = if (this.factor1Context()?.content?.text?.isNotBlank() ?: false) {
        this.factor1Context().content.toAst(conf)
    } else {
        null
    }
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUBDUR operation requires factor 2: ${this.text}")
    // TODO handle duration code after the :
    val target = this.cspec_fixed_standard_parts().result.text.split(":")
    val position = toPosition(conf.considerPosition)
    return SubDurStmt(left, DataRefExpr(ReferenceByName(target[0]), position), factor2, position)
}

internal fun CsCHAINContext.toAst(conf: ToAstConfiguration): Statement {
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHAIN operation requires factor 1: ${this.text}")
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("CHAIN operation requires factor 2: ${this.text}")
    return ChainStmt(
            factor1,
            factor2,
            toPosition(conf.considerPosition))
}

internal fun CsREADEContext.toAst(conf: ToAstConfiguration): Statement {
    // TODO implement DS in result field
    val factor1 = this.factor1Context()?.content?.toAst(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READE operation requires factor 2: ${this.text}")
    return ReadEqualStmt(
        factor1,
        factor2,
        toPosition(conf.considerPosition))
}

internal fun CsCHECKContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHECK operation requires factor 1: ${this.text}")
    val baseStringTokens = this.cspec_fixed_standard_parts().factor2.text.split(":")
    val startPosition =
        when (baseStringTokens.size) {
            !in 1..2 -> throw UnsupportedOperationException("Wrong base string expression for CHECK at line ${position?.line()}: ${this.cspec_fixed_standard_parts().factor2.text}")
            2 -> baseStringTokens[1].toInt()
            else -> 1
        }
    val reference = baseStringTokens[0]
    return CheckStmt(
            factor1,
            DataRefExpr(ReferenceByName(reference), position),
            startPosition,
            this.cspec_fixed_standard_parts()?.result?.toAst(conf),
            position)
}

internal fun CsMOVEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveStmt {
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text}")
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return MoveStmt(DataRefExpr(ReferenceByName(name), position), expression, position)
}

internal fun CsMOVELContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveLStmt {
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text}")
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return MoveLStmt(DataRefExpr(ReferenceByName(name), position), expression, position)
}

internal fun CsZ_ADDContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZAddStmt {
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("Z-ADD operation requires factor 2: ${this.text}")
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZAddStmt(DataRefExpr(ReferenceByName(name), position), dataDefinition, expression, position)
}

internal fun CsZ_SUBContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZSubStmt {
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("Z-SUB operation requires factor 2: ${this.text}")
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZSubStmt(DataRefExpr(ReferenceByName(name), position), dataDefinition, expression, position)
}
// TODO add real implementation
internal fun CsCOMPContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompStmt {
    val position = toPosition(conf.considerPosition)
    return CompStmt(position)
}

internal fun CsCLEARContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ClearStmt {
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return ClearStmt(
            referenceToExpression(name, toPosition(conf.considerPosition)),
            this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf),
            position)
}

private fun QualifiedTargetContext.getFieldName(): String {
    return if (this.fieldName != null) {
        this.fieldName.text
    } else {
        this.field.ID().text
    }
}

internal fun TargetContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): AssignableExpression {
    return when (this) {
        is SimpleTargetContext -> DataRefExpr(ReferenceByName(this.name.text), toPosition(conf.considerPosition))
        is IndexedTargetContext -> ArrayAccessExpr(array = this.base.toAst(conf),
                index = this.index.toAst(conf),
                position = toPosition(conf.considerPosition))
        is SubstTargetContext -> this.bif_subst().toAst(conf)
        is QualifiedTargetContext -> QualifiedAccessExpr(
                DataRefExpr(ReferenceByName(this.container.text), this.container!!.toPosition(conf.considerPosition)),
                ReferenceByName(this.getFieldName()),
                toPosition(conf.considerPosition))
        is IndicatorTargetContext -> PredefinedIndicatorExpr(
                this.indic.text.indicatorIndex()!!,
                toPosition(conf.considerPosition)
        )
        is GlobalIndicatorTargetContext -> PredefinedGlobalIndicatorExpr(
                toPosition(conf.considerPosition)
        )
        else -> TODO("${this.text} - Position: ${toPosition(conf.considerPosition)} ${this.javaClass.name}")
    }
}

fun Token.toPosition(considerPosition: Boolean): Position? {
    return if (considerPosition) {
        Position(this.startPoint, this.endPoint)
    } else {
        null
    }
}

internal fun AssignmentOperatorIncludingEqualContext.toAssignmentOperator(): AssignmentOperator {
    return when {
        this.EQUAL() != null -> NORMAL_ASSIGNMENT
        this.CPLUS() != null -> PLUS_ASSIGNMENT
        this.CMINUS() != null -> MINUS_ASSIGNMENT
        this.CMULT() != null -> MULT_ASSIGNMENT
        this.CDIV() != null -> DIVIDE_ASSIGNMENT
        this.CEXP() != null -> EXP_ASSIGNMENT
        else -> throw UnsupportedOperationException(this.text)
    }
}

internal fun CsCALLContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CallStmt {
    require(this.cspec_fixed_standard_parts().factor().factorContent().size == 1)
    val position = this.toPosition(true)
    var literal = this.cspec_fixed_standard_parts().factor().factorContent()[0].literal()
    var functionCalled : Expression?
    functionCalled = literal?.toAst(conf) ?: this.cspec_fixed_standard_parts().factor2.content.toAst(conf)
    return CallStmt(functionCalled,
            this.csPARM().map { it.toAst(conf) },
            this.cspec_fixed_standard_parts().lo.asIndex(),
            toPosition(conf.considerPosition))
}

internal fun ResultIndicatorContext.asIndex(): Int? {
    // TODO: verify if we should cover other cases (e.g. external indicators)
    return this.GeneralIndicator()?.text?.toIntOrNull()
}
