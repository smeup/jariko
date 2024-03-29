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

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition
import com.smeup.rpgparser.utils.asBigDecimal
import com.smeup.rpgparser.utils.asLong
import com.smeup.rpgparser.utils.divideAtIndex
import com.smeup.rpgparser.utils.moveEndingString
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.specificProcess
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.roundToLong
import kotlin.math.sqrt

class ExpressionEvaluation(
    val systemInterface: SystemInterface,
    val localizationContext: LocalizationContext,
    val interpreterStatus: InterpreterStatus
) : Evaluator {
    private fun evalAsBoolean(expression: Expression): Boolean = expression.evalWith(this).asBoolean().value
    private fun evalAsString(expression: Expression): String = expression.evalWith(this).asString().value
    private fun evalAsInt(expression: Expression): Int = expression.evalWith(this).asInt().value.toInt()
    private fun evalAsLong(expression: Expression): Long = expression.evalWith(this).asInt().value
    private fun evalAsDecimal(expression: Expression): BigDecimal = expression.evalWith(this).asDecimal().value

    override fun eval(expression: StringLiteral): Value = StringValue(expression.value)
    override fun eval(expression: IntLiteral) = IntValue(expression.value)
    override fun eval(expression: RealLiteral) = DecimalValue(expression.value)
    override fun eval(expression: NumberOfElementsExpr): Value {
        return when (val value = expression.value.evalWith(this)) {
            is ArrayValue -> value.arrayLength().asValue()
            is OccurableDataStructValue -> value.occurs.asValue()
            else -> throw IllegalStateException("Cannot ask number of elements of $value")
        }
    }

    override fun eval(expression: DataRefExpr) = interpreterStatus.getVar(
        expression.variable.referred
            ?: throw IllegalStateException("Unsolved reference ${expression.variable.name} at ${expression.position}")
    )

    override fun eval(expression: EqualityExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return areEquals(left, right).asValue()
    }

    override fun eval(expression: DifferentThanExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return (!areEquals(left, right)).asValue()
    }

    override fun eval(expression: GreaterThanExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return isGreaterThan(left, right, localizationContext.charset).asValue()
    }

    override fun eval(expression: GreaterEqualThanExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return (isGreaterThan(left, right, localizationContext.charset) || areEquals(left, right)).asValue()
    }

    override fun eval(expression: LessEqualThanExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return (isEqualOrSmaller(left, right, localizationContext.charset)).asValue()
    }

    override fun eval(expression: LessThanExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return isSmallerThan(left, right, localizationContext.charset).asValue()
    }

    override fun eval(expression: BlanksRefExpr) = BlanksValue
    override fun eval(expression: DecExpr): Value {
        val decDigits = expression.decDigits.evalWith(this).asInt().value
        val valueAsString = expression.value.evalWith(this).asString().value
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

    override fun eval(expression: PlusExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return when {
            left is StringValue && right is AbstractStringValue -> {
                if (left.varying) {
                    val s = left.value.trimEnd() + right.getWrappedString()
                    StringValue(s)
                } else {
                    val s = left.value + right.getWrappedString()
                    StringValue(s)
                }
            }
            left is AbstractStringValue && right is AbstractStringValue -> {
                UnlimitedStringValue(left.getWrappedString() + right.getWrappedString())
            }
            left is IntValue && right is IntValue -> {
                (left + right)
            }
            left is NumberValue && right is NumberValue -> {
                DecimalValue(left.bigDecimal.plus(right.bigDecimal))
            }
            left is ArrayValue && right is ArrayValue -> {
                sum(left, right, expression.position)
            }
            left is StringValue && right is DataStructValue -> {
                if (left.varying) {
                    val s = left.value.trimEnd() + right.value
                    StringValue(s)
                } else {
                    val s = left.value + right.value
                    StringValue(s)
                }
            }
            else -> {
                throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
            }
        }
    }

    private fun sum(left: ArrayValue, right: ArrayValue, position: Position?): ArrayValue {
        val listValue = when {
            left.elementType is StringType && right.elementType is StringType ->
                left.elements().mapIndexed { i: Int, v: Value ->
                    if (right.elements().size > i) {
                        val rightElement = right.getElement(i + 1)

                        val valueToAdd: String = if (v.asString().varying) {
                            (v.asString().value + rightElement.asString().value)
                        } else {
                            (v.asString().value + " ".repeat(left.elementType.elementSize() - v.asString().value.length) + rightElement.asString().value)
                        }
                        StringValue(valueToAdd, left.elementType.hasVariableSize())
                    } else {
                        v
                    }
                }
            left.elementType is NumberType && right.elementType is NumberType ->
                left.elements().mapIndexed { i: Int, v: Value ->
                    if (right.elements().size > i) {
                        val rightElement = right.getElement(i + 1)
                        when {
                            v is IntValue && rightElement is IntValue -> {
                                (v + rightElement)
                            }

                            v is NumberValue && rightElement is NumberValue -> {
                                DecimalValue(v.bigDecimal.plus(rightElement.bigDecimal))
                            }

                            else -> throw UnsupportedOperationException("Unable to sum ${left.elementType} and ${right.elementType} as Array elements at $position")
                        }
                    } else {
                        v
                    }
                }
            else -> throw UnsupportedOperationException("Unable to sum ${left.elementType} and ${right.elementType} as Array elements at $position")
        } as MutableList<Value>
        return ConcreteArrayValue(listValue, left.elementType)
    }

    override fun eval(expression: MinusExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return when {
            left is IntValue && right is IntValue -> (left - right)
            left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal.minus(right.bigDecimal))
            else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
        }
    }

    override fun eval(expression: MultExpr): Value {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return when {
            left is IntValue && right is IntValue -> (left * right)
            left is NumberValue && right is NumberValue -> {
                DecimalValue(left.bigDecimal.multiply(right.bigDecimal))
            }
            else -> throw UnsupportedOperationException("I do not know how to multiply $left and $right at ${expression.position}")
        }
    }

    override fun eval(expression: CharExpr): Value {
        val value = expression.value.evalWith(this)
        val representation = value.stringRepresentation(expression.format)

        if (expression.value !is DivExpr) {
            return StringValue(representation)
        }

        // Decimals are always returned with 10 decimal digits
        // fill with 0 if necessary
        val numDecimals = value.asDecimal().value.scale()
        return when {
            numDecimals == 0 -> StringValue("$representation.0000000000")
            numDecimals < 10 -> StringValue(representation + "0".repeat(10 - numDecimals))
            else -> StringValue(representation.trim())
        }
    }

    override fun eval(expression: LookupExpr): Value {
        val searchValued = expression.searchedValued.evalWith(this)
        val array = expression.array.evalWith(this) as ArrayValue
        var index = array.elements().indexOfFirst {
            areEquals(it, searchValued)
        }
        // If 'start' and/or 'length' specified (both optional)
        // Start: is the index of array element to start search
        // Length: is the limit number of elements to search forward
        var start = expression.start?.evalWith(this)?.asInt()
        start = start?.minus(1.asValue()) ?: 0.asValue()

        val arrayElements = expression.array.type().numberOfElements().asValue()
        val length = expression.length?.evalWith(this)?.asInt() ?: arrayElements

        val lowerLimit = start.value
        val upperLimit = start.plus(length).value - 1
        if (lowerLimit > index || upperLimit < index) index = -1

        return if (index == -1) 0.asValue() else (index + 1).asValue()
    }

    override fun eval(expression: ArrayAccessExpr): Value {
        val arrayValueRaw = expression.array.evalWith(this)
        val arrayValue = arrayValueRaw as? ArrayValue
            ?: throw IllegalStateException("Array access to something that does not look like an array: ${expression.render()} (${expression.position})")
        val indexValue = expression.index.evalWith(this)
        return arrayValue.getElement(indexValue.asInt().value.toInt())
    }

    override fun eval(expression: HiValExpr) = HiValValue
    override fun eval(expression: LowValExpr) = LowValValue
    override fun eval(expression: ZeroExpr) = ZeroValue
    override fun eval(expression: AllExpr) = AllValue(eval(expression.charsToRepeat).asString().value)
    override fun eval(expression: TranslateExpr): Value {
        val originalChars = expression.from.evalWith(this).asString().value
        val newChars = expression.to.evalWith(this).asString().value
        val start = expression.startPos.evalWith(this).asInt().value.toInt()
        val s = expression.string.evalWith(this).asString().value
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

    override fun eval(expression: LogicalAndExpr): Value {
        val left = evalAsBoolean(expression.left)
        return if (left) {
            expression.right.evalWith(this)
        } else {
            BooleanValue.FALSE
        }
    }

    override fun eval(expression: LogicalOrExpr): Value {
        val left = evalAsBoolean(expression.left)
        return if (left) {
            BooleanValue.TRUE
        } else {
            expression.right.evalWith(this)
        }
    }

    override fun eval(expression: LogicalCondition): Value {
        if (expression.ands.any { !evalAsBoolean(it) } && !expression.ors.any { evalAsBoolean(it) }) {
            return BooleanValue.FALSE
        }

        if (evalAsBoolean(expression.expression)) {
            return BooleanValue.TRUE
        }

        return BooleanValue(expression.ors.any { evalAsBoolean(it) })
    }

    override fun eval(expression: OnRefExpr) = BooleanValue.TRUE
    override fun eval(expression: NotExpr) = BooleanValue(!evalAsBoolean(expression.base))
    override fun eval(expression: ScanExpr): Value {
        var startIndex = 0
        if (expression.start != null) {
            startIndex = expression.start.evalWith(this).asInt().value.toInt()
            if (startIndex > 0) {
                startIndex -= 1
            }
        }
        val value = expression.value.evalWith(this).asString().value
        // if length is specified, I need to scan from start index to startIndex + length
        val source = expression.length?.evalWith(this)?.asInt()?.value?.toInt()?.let { length ->
            expression.source.evalWith(this).asString().value.substring(0, startIndex + length)
        } ?: expression.source.evalWith(this).asString().value
        val result = source.indexOf(value, startIndex)
        return IntValue(if (result == -1) 0 else result.toLong() + 1)
    }
    override fun eval(expression: CheckExpr): Value {
        var startpos = 0
        if (expression.start != null) {
            startpos = expression.start.evalWith(this).asInt().value.toInt()
            if (startpos > 0) {
                startpos -= 1
            }
        }
        val comparator = expression.value.evalWith(this).asString().value
        val base = expression.source.evalWith(this).asString().value.toCharArray()

        var result = 0
        for (i in startpos until base.size) {
            val currChar = base[i]
            if (!comparator.contains(currChar)) {
                result = i + 1
                break
            }
        }

        return IntValue(result.toLong())
    }

    override fun eval(expression: SubstExpr): Value {
        val length = if (expression.length != null) expression.length.evalWith(this).asInt().value.toInt() else 0
        val start = expression.start.evalWith(this).asInt().value.toInt() - 1
        val originalString = expression.string.evalWith(this).asString().value
        return if (length == 0) {
            StringValue(originalString.padEnd(start + 1).substring(start))
        } else {
            StringValue(originalString.padEnd(start + length + 1).substring(start, start + length))
        }
    }

    override fun eval(expression: SubarrExpr): Value {
        val start = expression.start.evalWith(this).asInt().value.toInt() - 1
        val numberOfElement: Int? = if (expression.numberOfElements != null) expression.numberOfElements.evalWith(this).asInt().value.toInt() else null
        val originalArray: ArrayValue = expression.array.evalWith(this).asArray()
        val to: Int = if (numberOfElement == null) {
            originalArray.arrayLength()
        } else {
            (start) + numberOfElement
        }
        return originalArray.take(start, to)
    }

    override fun eval(expression: LenExpr): Value {
        return when (val value = expression.value.evalWith(this)) {
            is StringValue -> {
                when (expression.value) {
                    is DataRefExpr -> {
                        when (val type = expression.value.type()) {
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
                value.value.length.asValue()
            }
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
            is IntValue -> {
                // see https://www.ibm.com/docs/en/i/7.5?topic=length-len-used-its-value
                var totalSize = 0L
                // the len is the sum of variable size
                expression.specificProcess(DataRefExpr::class.java) {
                    totalSize += it.variable.referred!!.type.size.toLong()
                }
                if (totalSize == 0L) {
                    TODO("Invalid LEN parameter $value")
                } else {
                    totalSize.asValue()
                }
            }
            is DecimalValue -> {
                // see https://www.ibm.com/docs/en/i/7.5?topic=length-len-used-its-value
                var totalSize = 0L
                // the len is the sum of variable size
                expression.specificProcess(DataRefExpr::class.java) {
                    totalSize += it.variable.referred!!.type.size.toLong()
                }
                if (totalSize == 0L) {
                    TODO("Invalid LEN parameter $value")
                } else {
                    totalSize.asValue()
                }
            }
            else -> {
                TODO("Invalid LEN parameter $value")
            }
        }
    }

    override fun eval(expression: OffRefExpr) = BooleanValue.FALSE

    override fun eval(expression: NegationExpr): DecimalValue {
        val value = expression.value1.evalWith(this).asDecimal().value
        return DecimalValue(-value)
    }

    override fun eval(expression: IndicatorExpr): BooleanValue {
        // if index is passed through expression, it means that it is a dynamic indicator
        val runtimeIndex = expression.indexExpression?.evalWith(this)?.asInt()?.value?.toInt() ?: expression.index
        return interpreterStatus.indicator(runtimeIndex)
    }
    override fun eval(expression: FunctionCall): Value {
        val functionToCall = expression.function.name
        val function = systemInterface.findFunction(interpreterStatus.symbolTable, functionToCall)
            ?: throw RuntimeException("Function $functionToCall cannot be found (${expression.position.line()})")
        return FunctionWrapper(function = function, functionName = functionToCall, expression).let { functionWrapper ->
            val paramsValues = expression.args.map {
                if (it is DataRefExpr) {
                    FunctionValue(variableName = it.variable.name, value = it.evalWith(this))
                } else {
                    FunctionValue(value = it.evalWith(this))
                }
            }
            functionWrapper.execute(systemInterface, paramsValues, interpreterStatus.symbolTable)
        }
    }

    override fun eval(expression: TimeStampExpr): Value {
        if (expression.value == null) {
            return TimeStampValue.now()
        } else {
            val evaluated = expression.value.evalWith(this)
            if (evaluated is StringValue) {
                return TimeStampValue.of(evaluated.value)
            }
            TODO("TimeStamp parsing: " + evaluated)
        }
    }

    override fun eval(expression: EditcExpr): Value {
        val n = expression.value.evalWith(this)
        val format = expression.format.evalWith(this)
        if (format !is StringValue) throw UnsupportedOperationException("Required string value, but got $format at ${expression.position}")
        return n.asDecimal().formatAs(format.value, expression.value.type(), localizationContext.decedit)
    }

    override fun eval(expression: DiffExpr): Value {
        val v1 = expression.value1.evalWith(this)
        val v2 = expression.value2.evalWith(this)
        return when (expression.durationCode) {
            is DurationInMSecs -> IntValue(
                ChronoUnit.MICROS.between(
                    v2.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant(), v1.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant()
                )
            )
            is DurationInDays -> IntValue(
                ChronoUnit.DAYS.between(
                    v2.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant(), v1.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant()
                )
            )
            is DurationInSecs -> IntValue(
                ChronoUnit.SECONDS.between(
                    v2.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant(), v1.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant()
                )
            )
            is DurationInMinutes -> IntValue(
                ChronoUnit.MINUTES.between(
                    v2.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant(), v1.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant()
                )
            )
            is DurationInHours -> IntValue(
                ChronoUnit.HOURS.between(
                    v2.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant(), v1.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant()
                )
            )
            is DurationInMonths -> IntValue(
                ChronoUnit.MONTHS.between(
                    v2.asTimeStamp().localDate, v1.asTimeStamp().localDate
                )
            )
            is DurationInYears -> IntValue(
                ChronoUnit.YEARS.between(
                    v2.asTimeStamp().localDate, v1.asTimeStamp().localDate
                )
            )
        }
    }

    override fun eval(expression: DivExpr): Value {
        val v1 = expression.left.evalWith(this)
        val v2 = expression.right.evalWith(this)
        require(v1 is NumberValue && v2 is NumberValue)

        // Detects what kind of eval must be evaluated
        val res = if (expression.parent is EvalStmt) {
            val parent = (expression.parent as EvalStmt)
            val decimalDigits = (parent.target.type() as NumberType).decimalDigits
            when {
                // EVAL(H)
                parent.flags.halfAdjust -> {
                    v1.bigDecimal.setScale(decimalDigits)
                        .divide(v2.bigDecimal.setScale(decimalDigits), RoundingMode.HALF_UP)
                }
                // Eval(M)
                parent.flags.maximumNumberOfDigitsRule -> {
                    TODO("EVAL(M) not supported yet")
                }
                // Eval(R)
                parent.flags.resultDecimalPositionRule -> {
                    TODO("EVAL(R) not supported yet")
                }
                else -> {
                    // In rpgle when I move 7.500 / 8.1 = 0,9259259259259259 in a variable with precision 4 and scale 3
                    // it become 0,925 and then, I have to scale and truncate down
                    v1.bigDecimal.divide(v2.bigDecimal, decimalDigits, RoundingMode.DOWN)
                }
            }
        } else {
            v1.bigDecimal.divide(v2.bigDecimal, MathContext.DECIMAL128)
        }
        // TODO rounding and scale???
        return DecimalValue(res)
    }

    override fun eval(expression: ExpExpr): Value {
        val v1 = expression.left.evalWith(this)
        val v2 = expression.right.evalWith(this)
        return DecimalValue(BigDecimal(Math.pow(v1.asInt().value.toDouble(), v2.asInt().value.toDouble())))
    }

    override fun eval(expression: TrimrExpr): Value {
        return if (expression.charactersToTrim == null) {
            StringValue(evalAsString(expression.value).trimEnd())
        } else {
            val suffix = evalAsString(expression.charactersToTrim)
            var result = evalAsString(expression.value)
            while (result.endsWith(suffix)) {
                result = result.substringBefore(suffix)
            }
            StringValue(result)
        }
    }

    override fun eval(expression: TrimlExpr): Value {
        if (expression.charactersToTrim == null) {
            return StringValue(evalAsString(expression.value).trimStart())
        } else {
            val prefix = evalAsString(expression.charactersToTrim)
            var result = evalAsString(expression.value)
            while (result.startsWith(prefix)) {
                result = result.substringAfter(prefix)
            }
            return StringValue(result)
        }
    }

    override fun eval(expression: TrimExpr) = StringValue(evalAsString(expression.value).trim())
    override fun eval(expression: FoundExpr): Value {
        // TODO fix this bad implementation
        if (expression.name == null) {
            return BooleanValue(interpreterStatus.lastFound)
        }
        TODO("Line ${expression.position?.line()} - %FOUND expression with file names is not implemented yet")
    }

    override fun eval(expression: EofExpr): Value {

        if (expression.name == null) {
            return BooleanValue(interpreterStatus.lastDBFile?.eof() ?: false)
        }
        TODO("Line ${expression.position?.line()} - %EOF expression with file names is not implemented yet")
    }

    override fun eval(expression: EqualExpr): Value {
        if (expression.name == null) {
            return BooleanValue(interpreterStatus.lastDBFile?.equal() ?: false)
        }
        TODO("Line ${expression.position?.line()} - %EQUAL expression with file names is not implemented yet")
    }

    override fun eval(expression: AbsExpr): Value {
        val value = evalAsDecimal(expression.value)
        return DecimalValue(BigDecimal.valueOf(abs(value.toDouble())))
    }

    override fun eval(expression: EditwExpr): Value {
        val n = expression.value.evalWith(this)
        val format = evalAsString(expression.format)
        return n.asDecimal().formatAsWord(format, expression.value.type())
    }

    override fun eval(expression: IntExpr): Value =
        when (val value = expression.value.evalWith(this)) {
            is StringValue ->
                IntValue(cleanNumericString(value.value).asLong())
            is DecimalValue ->
                value.asInt()
            is UnlimitedStringValue ->
                IntValue(cleanNumericString(value.value).asLong())
            else -> throw UnsupportedOperationException("I do not know how to handle $value with %INT")
        }

    override fun eval(expression: InthExpr): Value =
        when (val value = expression.value.evalWith(this)) {
            is StringValue ->
                IntValue(value.value.toDouble().roundToLong())
            is DecimalValue ->
                IntValue(value.value.toDouble().roundToLong())
            is UnlimitedStringValue ->
                IntValue(value.value.toDouble().roundToLong())
            else -> throw UnsupportedOperationException("I do not know how to handle $value with %INTH")
        }

    override fun eval(expression: RemExpr): Value {
        val n = evalAsLong(expression.dividend)
        val m = evalAsLong(expression.divisor)
        return IntValue(n % m)
    }

    override fun eval(expression: QualifiedAccessExpr): Value {
        val dataStringValue = when (val value = expression.container.evalWith(this)) {
            is DataStructValue -> {
                value
            }

            is OccurableDataStructValue -> {
                value.value()
            }

            else -> {
                throw ClassCastException(value::class.java.name)
            }
        }
        return dataStringValue[expression.field.referred
            ?: throw IllegalStateException("Referenced to field not resolved: ${expression.field.name}")]
    }

    override fun eval(expression: ReplaceExpr): Value {
        val replacement = evalAsString(expression.replacement)
        val sourceString = evalAsString(expression.source)
        val replacementLength: Int = replacement.length
        val result: String = if (expression.start == null) {
            // case of %REPLACE(stringToReplaceWith:stringSource)
            // replace text at beginning of variable
            sourceString.replaceRange(0 until replacementLength, replacement)
        } else {
            val start = evalAsInt(expression.start) - 1
            if (expression.length == null) {
                // case of %REPLACE(stringToReplaceWith:stringSource:startIndex)
                // replace text at specified position
                val truncatedSource = sourceString.substring(0, start)
                if (truncatedSource.length + replacement.length < sourceString.length) {
                    (truncatedSource + replacement + sourceString.substring(truncatedSource.length + replacementLength))
                } else {
                    truncatedSource + replacement
                }
            } else {
                // case of %REPLACE(stringToReplaceWith:stringSource:startIndex:nrOfCharsToReplace)
                // replace to insert or delete text
                val characterToReplace = evalAsInt(expression.length)
                sourceString.replaceRange(start, (start + characterToReplace), replacement)
            }
        }
        // truncated if length is greater than type.elementSize
        return if (result.length > expression.source.type().elementSize()) {
            StringValue(result.substring(0, expression.source.type().elementSize()), expression.source.type().hasVariableSize())
        } else {
            StringValue(result, expression.source.type().hasVariableSize())
        }
    }

    override fun eval(expression: SqrtExpr): Value {
        val value = evalAsDecimal(expression.value)
        return DecimalValue(BigDecimal.valueOf(sqrt(value.toDouble())))
    }

    override fun eval(expression: AssignmentExpr) =
        throw RuntimeException("AssignmentExpr should be handled by the interpreter: $expression")

    override fun eval(expression: GlobalIndicatorExpr): Value {
        val value = StringBuilder()
        for (i in IndicatorType.Predefined.range) {
            value.append(if (interpreterStatus.indicators[i] == null) "0" else if (interpreterStatus.indicators[i]!!.value) "1" else "0")
        }
        return StringValue(value.toString())
    }

    override fun eval(expression: ParmsExpr): Value {
        return IntValue(interpreterStatus.params.toLong())
    }

    override fun eval(expression: OpenExpr): Value {
        val name = expression.name
        require(name != null) {
            "Line ${expression.position?.line()} - %OPEN require a table name"
        }
        val enrichedDBFile = interpreterStatus.dbFileMap.get(name)
            ?: throw RuntimeException("Table $name cannot be found (${expression.position.line()})")
        return BooleanValue(enrichedDBFile.open)
    }

    private fun cleanNumericString(s: String): String {
        val result = s.moveEndingString("-")
        return when {
            result.contains(".") -> result.substringBefore(".")
            result.contains(",") -> result.substringBefore(",")
            else -> result
        }
    }
}
