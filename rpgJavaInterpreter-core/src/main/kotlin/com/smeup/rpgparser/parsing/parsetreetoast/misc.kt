package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.parsing.facade.findAllDescendants
import com.smeup.rpgparser.utils.asInt
import com.smeup.rpgparser.utils.asIntOrNull
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.*
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
    var dataDefinitionProviders: MutableList<DataDefinitionProvider> = LinkedList()
    val knownDataDefinitions = LinkedList<DataDefinition>()

    // First pass ignore exception and all the know definitions
    dataDefinitionProviders.addAll(this.statement()
        .mapNotNull {
        when {
            it.dcl_ds() != null -> {
                try {
                    val dataDefinition = it.dcl_ds().toAst(conf)
                    knownDataDefinitions.add(dataDefinition)
                    DataDefinitionHolder(dataDefinition)
                } catch (e: Exception) {
                    null
                }
            }
            else -> null
        }
    })

    // Second pass, everything, I mean everything
    dataDefinitionProviders.addAll(this.statement()
            .mapNotNull {
                when {
                    it.dspec() != null -> {
                        val dataDefinition = it.dspec().toAst(conf, knownDataDefinitions)
                        knownDataDefinitions.add(dataDefinition)
                        DataDefinitionHolder(dataDefinition)
                    }
                    it.dcl_ds() != null -> if (it.dcl_ds().useLikeDs()) {
                        DataDefinitionCalculator(it.dcl_ds().toAstWithLikeDs(conf, dataDefinitionProviders))
                    } else {
                        null
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
    val regexp = Regex("([+\\-])?(\\d|,|\\.)+([+\\-])?")
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
    // fix minus at right
    val value = if (text.endsWith('-')) {
        "-" + text.replaceFirst("-", "")
    } else {
        text
    }
    return when {
        // When assigning a value to a numeric field we could either use
        // a comma or a dot as decimal separators
        value.contains('.') -> {
            value.toRealLiteral(position, Locale.US)
        }
        value.contains(',') -> {
            value.toRealLiteral(position, Locale.ITALIAN)
        }
        else -> {
            IntLiteral(value.toLong(), position)
        }
    }
}

internal fun SymbolicConstantsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    val position = toPosition(conf.considerPosition)
    return when {
        this.SPLAT_HIVAL() != null -> HiValExpr(position)
        this.SPLAT_LOVAL() != null -> LowValExpr(position)
        this.SPLAT_BLANKS() != null -> BlanksRefExpr(position)
        this.SPLAT_ALL() != null -> {
            val content: LiteralContext = this.parent.getChild(1) as LiteralContext
            AllExpr(content.toAst(conf), position)
        }
        else -> TODO("$this - ${position?.line()}")
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
        this.csSETOFF() != null -> this.csSETOFF().toAst(conf)
        this.csPLIST() != null -> this.csPLIST().toAst(conf)
        this.csCLEAR() != null -> this.csCLEAR().toAst(conf)
        this.csLEAVE() != null -> LeaveStmt(toPosition(conf.considerPosition))
        this.csITER() != null -> IterStmt(toPosition(conf.considerPosition))
        this.csOTHER() != null -> OtherStmt(toPosition(conf.considerPosition))
        this.csDSPLY() != null -> this.csDSPLY().toAst(conf)
        this.csMOVE() != null -> this.csMOVE().toAst(conf)
        this.csMOVEA() != null -> this.csMOVEA().toAst(conf)
        this.csMOVEL() != null -> this.csMOVEL().toAst(conf)
        this.csTIME() != null -> this.csTIME().toAst(conf)
        this.csSUBDUR() != null -> this.csSUBDUR().toAst(conf)
        this.csZ_ADD() != null -> this.csZ_ADD().toAst(conf)
        this.csADD() != null -> this.csADD().toAst(conf)
        this.csZ_SUB() != null -> this.csZ_SUB().toAst(conf)
        this.csSUB() != null -> this.csSUB().toAst(conf)
        this.csCHAIN() != null -> this.csCHAIN().toAst(conf)
        this.csCHECK() != null -> this.csCHECK().toAst(conf)
        this.csKLIST() != null -> this.csKLIST().toAst(conf)
        this.csSETLL() != null -> this.csSETLL().toAst(conf)
        this.csREADE() != null -> this.csREADE().toAst(conf)
        this.csREAD() != null -> this.csREAD().toAst(conf)
        this.csCOMP() != null -> this.csCOMP().toAst(conf)
        this.csMULT() != null -> this.csMULT().toAst(conf)
        this.csDIV() != null -> this.csDIV().toAst(conf)
        this.csRETURN() != null -> this.csRETURN().toAst(conf)
        this.csTAG() != null -> this.csTAG().toAst(conf)
        this.csGOTO() != null -> this.csGOTO().toAst(conf)
        this.csSORTA() != null -> this.csSORTA().toAst(conf)
        else -> TODO("${this.text} at ${this.toPosition(true)}")
    }
}

// FIXME: This is very, very, very ugly. It should be fixed by parsing this properly
//        in the grammar
internal fun referenceToExpression(text: String, position: Position?): Expression {
    if (text.toUpperCase() == "*IN") {
        return PredefinedGlobalIndicatorExpr(position)
    }
    if (text.toUpperCase().startsWith("*IN(") && text.endsWith(")")) {
        val index = text.toUpperCase().removePrefix("*IN(").removeSuffix(")").toInt()
        return PredefinedIndicatorExpr(index, position)
    }
    if (text.toUpperCase().startsWith("*IN")) {
        val index = text.toUpperCase().removePrefix("*IN").toInt()
        return PredefinedIndicatorExpr(index, position)
    }
    return annidatedReferenceExpression(text, position)
}

private fun annidatedReferenceExpression(
    text: String,
    position: Position?
): AssignableExpression {
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
    return expr as AssignableExpression
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
    val left = leftExpr(conf)
    val right = if (this.cspec_fixed_standard_parts()?.result?.text?.isNotBlank() ?: false) {
        this.cspec_fixed_standard_parts().result.toAst(conf)
    } else {
        null
    }
    require(left != null || right != null)
    return DisplayStmt(left, right, toPosition(conf.considerPosition))
}

internal fun ResultTypeContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): AssignableExpression {
    // this should have been parsed differently because here we have to figure out
    // what kind of expression is this
    val position = toPosition(conf.considerPosition)

    if (text.contains('.')) {
        return handleParsingOfTargets(text, position)
    } else {
        return annidatedReferenceExpression(text, position)
    }
}

private fun handleParsingOfTargets(code: String, position: Position?): AssignableExpression {
    require(!code.contains("(") && !code.contains(")"))
    val parts = code.split(".")
    require(parts.isNotEmpty())
    return if (parts.size == 1) {
        DataRefExpr(ReferenceByName(parts[0]), position)
    } else {
        val containerCode = parts.dropLast(1).joinToString(separator = ".")
        QualifiedAccessExpr(
                container = handleParsingOfTargets(containerCode, position),
                field = ReferenceByName(parts.last()),
                position = position)
    }
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
    if (factor2?.symbolicConstants() != null) {
        return factor2.symbolicConstants().toAst()
    }
    return factor2?.content?.toAst(conf)
}

fun Cspec_fixed_standard_partsContext.resultExpression(conf: ToAstConfiguration): Expression? {
    if (result?.symbolicConstants() != null) {
        return result.symbolicConstants().toAst()
    }
    return result.toAst()
}

internal fun Cspec_fixed_standard_partsContext.toDataDefinition(name: String, position: Position?, conf: ToAstConfiguration): InStatementDataDefinition? {
    val len = this.len.asInt()
    if (len == null) {
        return null
    }
    val decimals = this.decimalPositions.asInt()
    val initialValue = this.factor2Expression(conf)
    return InStatementDataDefinition(name, dataType(len, decimals), position, initializationValue = initialValue)
}

private fun dataType(len: Int, decimals: Int?): Type =
    if (decimals == null) {
        StringType(len, false)
    } else {
        NumberType(len - decimals, decimals)
    }

internal fun Token.asLong(): Long? {
    val tokenString = this.text.trim()
    return if (tokenString.isNotBlank()) {
        tokenString.toLongOrNull()
    } else {
        null
    }
}

internal fun Token.asInt(): Int? {
    val tokenString = this.text.trim()
    return if (tokenString.isNotBlank()) {
        tokenString.toIntOrNull()
    } else {
        null
    }
}

internal fun CsSETONContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SetStmt {
    try {
        return SetStmt(SetStmt.ValueSet.ON, indicators(this.cspec_fixed_standard_parts(), conf.considerPosition), toPosition(conf.considerPosition))
    } catch (e: Exception) {
        throw RuntimeException("Problem translating ${this.text} at ${this.toPosition(true)}", e)
    }
}

internal fun CsSETOFFContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SetStmt {
    try {
        return SetStmt(SetStmt.ValueSet.OFF, indicators(this.cspec_fixed_standard_parts(), conf.considerPosition), toPosition(conf.considerPosition))
    } catch (e: Exception) {
        throw RuntimeException("Problem translating ${this.text} at ${this.toPosition(true)}", e)
    }
}

internal fun indicators(cspecs: Cspec_fixed_standard_partsContext, considerPosition: Boolean = true): List<AssignableExpression> {
    return listOf(cspecs.hi, cspecs.lo, cspecs.eq)
            .asSequence()
            .map { it.text }
            .filter { !it.isNullOrBlank() }
            .map(String::toUpperCase)
            .map {
                if (it.isInt()) {
                    PredefinedIndicatorExpr(it.toInt(), cspecs.toPosition(considerPosition))
                } else {
                    DataWrapUpIndicatorExpr(DataWrapUpChoice.valueOf(it.toUpperCase()), cspecs.toPosition(considerPosition))
                }
            }
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

internal fun CsRETURNContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ReturnStmt {
    return ReturnStmt(this.fixedexpression?.expression()?.toAst(conf), toPosition(conf.considerPosition))
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
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUBDUR operation requires factor 2: ${this.text} - ${position.atLine()}")
    // TODO handle duration code after the :
    val target = this.cspec_fixed_standard_parts().result.text.split(":")
    return SubDurStmt(left, DataRefExpr(ReferenceByName(target[0]), position), factor2, position)
}

internal fun CsCHAINContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHAIN operation requires factor 1: ${this.text} - ${position.atLine()}")
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("CHAIN operation requires factor 2: ${this.text} - ${position.atLine()}")
    return ChainStmt(factor1, factor2, position)
}

internal fun CsREADEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor1 = this.factor1Context()?.content?.toAst(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READE operation requires factor 2: ${this.text} - ${position.atLine()}")
    return ReadEqualStmt(factor1, factor2, position)
}

internal fun CsREADContext.toAst(conf: ToAstConfiguration): Statement {
    // TODO implement DS in result field
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READE operation requires factor 2: ${this.text}")
    return ReadStmt(
        factor2,
        toPosition(conf.considerPosition))
}

internal fun CsSETLLContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement indicators handling
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("SETLL operation requires factor 1: ${this.text} - ${position.atLine()}")
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READE operation requires factor 2: ${this.text} - ${position.atLine()}")
    return SetllStmt(factor1, factor2, position)
}

internal fun CsCHECKContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHECK operation requires factor 1: ${this.text} - ${position.atLine()}")
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

internal fun CsMOVEAContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveAStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVEA operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    return MoveAStmt(resultExpression, expression, position)
}

internal fun CsMOVEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    return MoveStmt(resultExpression, expression, position)
}

internal fun CsMOVELContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveLStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    return MoveLStmt(this.operationExtender?.text, resultExpression, expression, position)
}

internal fun CsZ_ADDContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZAddStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("Z-ADD operation requires factor 2: ${this.text} - ${position.atLine()}")
    val name = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZAddStmt(DataRefExpr(ReferenceByName(name), position), dataDefinition, expression, position)
}

internal fun CsMULTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MultStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val factor1 = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val extenders = this.operationExtender?.extender?.text?.toUpperCase()?.toCharArray() ?: CharArray(0)
    return MultStmt(DataRefExpr(ReferenceByName(result), position), 'H' in extenders, factor1, factor2, position)
}

internal fun CsDIVContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DivStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val factor1 = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val extenders = this.operationExtender?.extender?.text?.toUpperCase()?.toCharArray() ?: CharArray(0)
    return DivStmt(DataRefExpr(ReferenceByName(result), position), 'H' in extenders, factor1, factor2, position)
}

internal fun CsTAGContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TagStmt {
    return TagStmt(this.factor1Context()?.content?.text!!, toPosition(conf.considerPosition))
}

private fun ParserRuleContext.leftExpr(conf: ToAstConfiguration): Expression? {
    return if (this.factor1Context()?.content?.text?.isNotBlank() == true) {
        this.factor1Context().content.toAst(conf)
    } else {
        null
    }
}

internal fun CsGOTOContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): GotoStmt {
    var cspec_context = this.parent.parent as Cspec_fixedContext
    var offFlag = cspec_context.onOffIndicatorsFlag().NoFlag() != null
    var indicator = cspec_context.indicators.GeneralIndicator()?.text?.asInt()
    return GotoStmt(this.cspec_fixed_standard_parts().factor2.text, indicator, offFlag, toPosition(conf.considerPosition))
}

internal fun CsADDContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): AddStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val left = leftExpr(conf)
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("ADD operation requires factor 2: ${this.text} - ${position.atLine()}")
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return AddStmt(left, DataRefExpr(ReferenceByName(result), position), dataDefinition, right, position)
}

internal fun CsZ_SUBContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZSubStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("Z-SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    val name = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZSubStmt(DataRefExpr(ReferenceByName(name), position), dataDefinition, expression, position)
}

internal fun CsSUBContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val left = leftExpr(conf)
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return SubStmt(left, DataRefExpr(ReferenceByName(result), position), dataDefinition, right, position)
}

internal fun ResultIndicatorContext?.asIntOrNull(): Int? = this?.text?.asIntOrNull()

internal fun CsCOMPContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompStmt {
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf) ?: throw UnsupportedOperationException("COMP operation requires factor 1: ${this.text}")
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("COMP operation requires factor 2: ${this.text} - ${position.atLine()}")
    return CompStmt(
        left,
        right,
        this.cspec_fixed_standard_parts().hi.asIntOrNull(),
        this.cspec_fixed_standard_parts().lo.asIntOrNull(),
        this.cspec_fixed_standard_parts().eq.asIntOrNull(),
        position)
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
    val position = this.toPosition(true)
    require(this.cspec_fixed_standard_parts().factor().factorContent().size == 1) {
        "Missing factor 1 in call statement at line ${position.line()}"
    }
    var literal = this.cspec_fixed_standard_parts().factor().factorContent()[0].literal()
    var functionCalled: Expression?
    functionCalled = literal?.toAst(conf) ?: this.cspec_fixed_standard_parts().factor2.content.toAst(conf)
    return CallStmt(functionCalled,
            this.csPARM().map { it.toAst(conf) },
            this.cspec_fixed_standard_parts().lo.asIndex(),
            toPosition(conf.considerPosition))
}

internal fun CsSORTAContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SortAStmt {
    val expr = this.fixedexpression.expression().toAst(conf)
    return SortAStmt(expr, toPosition(conf.considerPosition))
}
internal fun ResultIndicatorContext.asIndex(): Int? {
    // TODO: verify if we should cover other cases (e.g. external indicators)
    return this.GeneralIndicator()?.text?.toIntOrNull()
}
