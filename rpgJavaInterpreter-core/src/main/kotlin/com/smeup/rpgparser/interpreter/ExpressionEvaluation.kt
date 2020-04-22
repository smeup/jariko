package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition
import com.smeup.rpgparser.utils.asBigDecimal
import com.smeup.rpgparser.utils.asLong
import com.smeup.rpgparser.utils.divideAtIndex
import com.smeup.rpgparser.utils.moveEndingString
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class ExpressionEvaluation(
    private val systemInterface: SystemInterface,
    private val localizationContext: LocalizationContext,
    private val interpreterStatus: InterpreterStatus
) {
    fun eval(expression: Expression): Value {
        return when (expression) {
            is StringLiteral -> StringValue(expression.value)
            is IntLiteral -> IntValue(expression.value)
            is RealLiteral -> DecimalValue(expression.value)
            is NumberOfElementsExpr -> {
                val value = eval(expression.value)
                when (value) {
                    is ArrayValue -> value.arrayLength().asValue()
                    else -> throw IllegalStateException("Cannot ask number of elements of $value")
                }
            }
            is DataRefExpr -> interpreterStatus.getVar(
                expression.variable.referred
                    ?: throw IllegalStateException("Unsolved reference ${expression.variable.name} at ${expression.position}")
            )
            is EqualityExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                return areEquals(left, right).asValue()
            }
            is DifferentThanExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                return (!areEquals(left, right)).asValue()
            }
            is GreaterThanExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                return isGreaterThan(left, right, localizationContext.charset).asValue()
            }
            is GreaterEqualThanExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                return (isGreaterThan(left, right, localizationContext.charset) || areEquals(left, right)).asValue()
            }
            is LessEqualThanExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                return (isEqualOrSmaller(left, right, localizationContext.charset)).asValue()
            }
            is LessThanExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                return isSmallerThan(left, right, localizationContext.charset).asValue()
            }
            is BlanksRefExpr -> {
                return BlanksValue
            }
            is DecExpr -> {
                val decDigits = eval(expression.decDigits).asInt().value
                val valueAsString = eval(expression.value).asString().value
                val valueAsBigDecimal = valueAsString.asBigDecimal()
                require(valueAsBigDecimal != null) {
                    "Line ${expression.position?.line()} - %DEC can't understand '$valueAsString'"
                }
                return if (decDigits == 0L) {
                    IntValue(valueAsBigDecimal.toLong())
                } else {
                    DecimalValue(valueAsBigDecimal)
                }
            }
            is PlusExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                when {
                    left is StringValue && right is StringValue -> {
                        if (left.varying) {
                            val s = left.value.trimEnd() + right.value
                            StringValue(s)
                        } else {
                            val s = left.value + right.value
                            StringValue(s)
                        }
                    }
                    left is IntValue && right is IntValue -> IntValue(left.value + right.value)
                    left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal + right.bigDecimal)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is MinusExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                when {
                    left is IntValue && right is IntValue -> IntValue(left.value - right.value)
                    left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal - right.bigDecimal)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is MultExpr -> {
                val left = eval(expression.left)
                val right = eval(expression.right)
                when {
                    left is IntValue && right is IntValue -> IntValue(left.value * right.value)
                    left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal * right.bigDecimal)
                    else -> throw UnsupportedOperationException("I do not know how to multiply $left and $right at ${expression.position}")
                }
            }
            is CharExpr -> {
                val value = eval(expression.value)
                return StringValue(value.stringRepresentation().trim())
            }
            is LookupExpr -> {
                val searchValued = eval(expression.searchedValued)
                val array = eval(expression.array) as ArrayValue
                val index = array.elements().indexOfFirst {
                    areEquals(it, searchValued)
                }
                return if (index == -1) 0.asValue() else (index + 1).asValue()
            }
            is ArrayAccessExpr -> {
                val arrayValueRaw = eval(expression.array)
                val arrayValue = arrayValueRaw as? ArrayValue
                    ?: throw IllegalStateException("Array access to something that does not look like an array: ${expression.render()} (${expression.position})")
                val indexValue = eval(expression.index)
                return arrayValue.getElement(indexValue.asInt().value.toInt())
            }
            is HiValExpr -> return HiValValue
            is LowValExpr -> return LowValValue
            is ZeroExpr -> return ZeroValue
            is AllExpr -> return AllValue(eval(expression.charsToRepeat).asString().value)
            is TranslateExpr -> {
                val originalChars = eval(expression.from).asString().value
                val newChars = eval(expression.to).asString().value
                val start = eval(expression.startPos).asInt().value.toInt()
                val s = eval(expression.string).asString().value
                val pair = s.divideAtIndex(start - 1)
                var right = pair.second
                val substitutionMap = mutableMapOf<Char, Char>()
                originalChars.forEachIndexed { i, c ->
                    if (newChars.length > i) {
                        substitutionMap[c] = newChars[i]
                    }
                }
                substitutionMap.forEach {
                    right = right.replace(it.key, it.value)
                }
                return StringValue(pair.first + right)
            }
            is LogicalAndExpr -> {
                val left = eval(expression.left).asBoolean().value
                return if (left) {
                    eval(expression.right)
                } else {
                    BooleanValue(false)
                }
            }
            is LogicalOrExpr -> {
                val left = eval(expression.left).asBoolean().value
                return if (left) {
                    BooleanValue(true)
                } else {
                    eval(expression.right)
                }
            }
            is LogicalCondition -> {
                if (expression.ands.any { !evalAsBoolean(it) }) {
                    return BooleanValue.FALSE
                }

                if (evalAsBoolean(expression.expression)) {
                    return BooleanValue.TRUE
                }

                return BooleanValue(expression.ors.any { evalAsBoolean(it) })
            }
            is OnRefExpr -> {
                return BooleanValue.TRUE
            }
            is NotExpr -> {
                return BooleanValue(!evalAsBoolean(expression.base))
            }
            is ScanExpr -> {
                var startIndex = 0
                if (expression.start != null) {
                    startIndex = eval(expression.start).asInt().value.toInt()
                }
                val value = eval(expression.value).asString().value
                val source = eval(expression.source).asString().value
                val result = source.indexOf(value, startIndex)
                return IntValue(if (result == -1) 0 else result.toLong() + 1)
            }
            is SubstExpr -> {
                val length = if (expression.length != null) eval(expression.length).asInt().value.toInt() else 0
                val start = eval(expression.start).asInt().value.toInt() - 1
                val originalString = eval(expression.string).asString().value
                return if (length == 0) {
                    StringValue(originalString.padEnd(start + 1).substring(start))
                } else {
                    StringValue(originalString.padEnd(start + length + 1).substring(start, start + length))
                }
            }
            is LenExpr -> {
                val value = eval(expression.value)
                return when (value) {
                    is StringValue -> {
                        when (expression.value) {
                            is DataRefExpr -> {
                                val type = expression.value.type()
                                when (type) {
                                    is StringType -> {
                                        value.length(type.varying).asValue()
                                    }
                                    else -> {
                                        value.value.length.asValue()
                                    }
                                }
                            }
                            is ArrayAccessExpr -> {
                                value.value.length.asValue()
                                // value.elementSize().asValue()
                            }
                            else -> {
                                return value.length().asValue()
                            }
                        }
                    }
                    is DataStructValue -> {
                        value.value.length.asValue() }
                    is ArrayValue -> {
                        // Incorrect data structure size calculation #28
                        when (expression.value) {
                            is DataRefExpr -> value.totalSize().asValue()
                            is ArrayAccessExpr -> value.elementSize().asValue()
                            else -> {
                                TODO("Invalid LEN parameter $value")
                            }
                        }
                    }
                    else -> {
                        TODO("Invalid LEN parameter $value")
                    }
                }
            }
            is OffRefExpr -> {
                return BooleanValue(false)
            }
            is PredefinedIndicatorExpr -> {
                return interpreterStatus.indicator(expression.index)
            }
            is FunctionCall -> {
                val functionToCall = expression.function.name
                val function = systemInterface.findFunction(interpreterStatus.symbolTable, functionToCall)
                    ?: throw RuntimeException("Function $functionToCall cannot be found (${expression.position.line()})")
                // TODO check number and types of params
                val paramsValues = expression.args.map { eval(it) }
                return function.execute(systemInterface, paramsValues, interpreterStatus.symbolTable)
            }
            is TimeStampExpr -> {
                if (expression.value == null) {
                    return TimeStampValue(Date())
                } else {
                    val evaluated = eval(expression.value)
                    if (evaluated is StringValue) {
                        return TimeStampValue(evaluated.value.asIsoDate())
                    }
                    TODO("TimeStamp parsing: " + evaluated)
                }
            }
            is EditcExpr -> {
                val n = eval(expression.value)
                val format = eval(expression.format)
                if (format !is StringValue) throw UnsupportedOperationException("Required string value, but got $format at ${expression.position}")
                return n.asDecimal().formatAs(format.value, expression.value.type(), localizationContext.decedit)
            }
            is DiffExpr -> {
                // TODO expression.durationCode
                val v1 = eval(expression.value1)
                val v2 = eval(expression.value2)
                return DecimalValue(BigDecimal(v1.asTimeStamp().value.time - v2.asTimeStamp().value.time))
            }
            is DivExpr -> {
                val v1 = eval(expression.left)
                val v2 = eval(expression.right)
                // Check the type and select the correct operation
                if (v1 is IntValue && v2 is IntValue) {
                    return DecimalValue(BigDecimal(v1.asInt().value / v2.asInt().value))
                }
                require(v1 is NumberValue && v2 is NumberValue)
                val res = v1.bigDecimal.toDouble() / v2.bigDecimal.toDouble()
                // Detects what kind of eval must be evaluated
                if (expression.parent is EvalStmt) {
                    val parent = expression.parent as EvalStmt
                    val targetType = parent.target.type() as NumberType
                    // EVAL(H)
                    if (parent.flags.halfAdjust) {
                        // perform the calculation, adjust the operand scale to the target
                        return DecimalValue(v1.bigDecimal.setScale(targetType.decimalDigits).divide(v2.bigDecimal.setScale(targetType.decimalDigits), RoundingMode.HALF_UP))
                    }
                    // Eval(M)
                    if (parent.flags.maximumNumberOfDigitsRule) {
                        TODO("EVAL(M) not supported yet")
                    }
                    // Eval(R)
                    if (parent.flags.resultDecimalPositionRule) {
                        TODO("EVAL(R) not supported yet")
                    }
                    return DecimalValue(BigDecimal(res).setScale(targetType.decimalDigits, RoundingMode.DOWN))
                }
                // TODO rounding and scale???
                return DecimalValue(BigDecimal(res))
            }
            is ExpExpr -> {
                val v1 = eval(expression.left)
                val v2 = eval(expression.right)
                return DecimalValue(BigDecimal(Math.pow(v1.asInt().value.toDouble(), v2.asInt().value.toDouble())))
            }
            is TrimrExpr -> {
                return if (expression.charactersToTrim == null) {
                    StringValue(eval(expression.value).asString().value.trimEnd())
                } else {
                    val suffix = eval(expression.charactersToTrim).asString().value
                    var result = eval(expression.value).asString().value
                    while (result.endsWith(suffix)) {
                        result = result.substringBefore(suffix)
                    }
                    StringValue(result)
                }
            }
            is TrimlExpr -> {
                if (expression.charactersToTrim == null) {
                    return StringValue(eval(expression.value).asString().value.trimStart())
                } else {
                    val prefix = eval(expression.charactersToTrim).asString().value
                    var result = eval(expression.value).asString().value
                    while (result.startsWith(prefix)) {
                        result = result.substringAfter(prefix)
                    }
                    return StringValue(result)
                }
            }
            is TrimExpr -> {
                return StringValue(eval(expression.value).asString().value.trim())
            }
            is FoundExpr -> {
                // TODO fix this bad implementation
                if (expression.name == null) {
                    return BooleanValue(interpreterStatus.lastFound)
                }
                TODO("Line ${expression.position?.line()} - %FOUND expression with file names is not implemented yet")
            }
            is EofExpr -> {
                // TODO fix this bad implementation
                if (expression.name == null) {
                    return BooleanValue(interpreterStatus.lastDBFile?.eof() ?: false)
                }
                TODO("Line ${expression.position?.line()} - %EOF expression with file names is not implemented yet")
            }
            is EqualExpr -> {
                // TODO fix this bad implementation
                if (expression.name == null) {
                    return BooleanValue(interpreterStatus.lastDBFile?.equal() ?: false)
                }
                TODO("Line ${expression.position?.line()} - %EQUAL expression with file names is not implemented yet")
            }
            is AbsExpr -> {
                val value = eval(expression.value)
                return DecimalValue(BigDecimal.valueOf(Math.abs(value.asDecimal().value.toDouble())))
            }
            is EditwExpr -> {
                val n = eval(expression.value)
                val format = eval(expression.format)
                require(format is StringValue) { "Required string value, but got $format at ${expression.position}" }
                return n.asDecimal().formatAsWord(format.value, expression.value.type())
            }
            is IntExpr -> {
                return when (val value = eval(expression.value)) {
                    is StringValue ->
                        IntValue(cleanNumericString(value.value).asLong())
                    is DecimalValue ->
                        value.asInt()
                    else -> throw UnsupportedOperationException("I do not know how to handle $value with %INT")
                }
            }
            is RemExpr -> {
                val n = eval(expression.dividend).asInt().value
                val m = eval(expression.divisor).asInt().value
                return IntValue(n % m)
            }
            is QualifiedAccessExpr -> {
                val containerValue = eval(expression.container)
                val dataStringValue = containerValue as DataStructValue
                return dataStringValue[expression.field.referred ?: throw IllegalStateException("Referenced to field not resolved: ${expression.field.name}")]
            }
            is ReplaceExpr -> {
                val replString = eval(expression.replacement).asString().value
                val sourceString = eval(expression.source).asString().value
                val replStringLength: Int = replString.length
                // case of %REPLACE(stringToReplaceWith:stringSource)
                if (expression.start == null) {
                    return StringValue(sourceString.replaceRange(0..replStringLength - 1, replString))
                }
                // case of %REPLACE(stringToReplaceWith:stringSource:startIndex)
                if (expression.length == null) {
                    val startNr = eval(expression.start).asInt().value.toInt()
                    return StringValue(sourceString.replaceRange((startNr - 1)..(startNr + replStringLength - 2), replString))
                } else {
                    // case of %REPLACE(stringToReplaceWith:stringSource:startIndex:nrOfCharsToReplace)
                    val startNr = eval(expression.start).asInt().value.toInt() - 1
                    val nrOfCharsToReplace = eval(expression.length).asInt().value.toInt()
                    return StringValue(sourceString.replaceRange(startNr, (startNr + nrOfCharsToReplace), replString))
                }
            }
            is SqrtExpr -> {
                val value = eval(expression.value)
                return DecimalValue(BigDecimal.valueOf(Math.sqrt(value.asDecimal().value.toDouble())))
            }
            else -> TODO(expression.toString())
        }
    }

    private fun evalAsBoolean(expression: Expression): Boolean = eval(expression).asBoolean().value

    private fun cleanNumericString(s: String): String {
        val result = s.moveEndingString("-")
        return when {
            result.contains(".") -> result.substringBefore(".")
            result.contains(",") -> result.substringBefore(",")
            else -> result
        }
    }
}
