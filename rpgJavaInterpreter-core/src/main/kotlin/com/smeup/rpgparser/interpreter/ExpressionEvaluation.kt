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

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.ProgramUsageType
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition
import com.smeup.rpgparser.utils.*
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.specificProcess
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.*
import kotlin.time.Duration.Companion.nanoseconds

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

    private inline fun proxyLogging(expression: Expression, action: () -> Value): Value {
        if (!MainExecutionContext.isLoggingEnabled) return action()

        val programName = MainExecutionContext.getExecutionProgramName()

        val start = System.nanoTime()
        val value = action()
        val elapsed = (System.nanoTime() - start).nanoseconds

        MainExecutionContext.log(
            LazyLogEntry.producePerformanceAndUpdateAnalytics(
                { LogSourceData(programName, expression.startLine()) },
                ProgramUsageType.Expression,
                expression.loggableEntityName,
                elapsed
            )
        )

        return value
    }

    override fun eval(expression: StringLiteral): Value = proxyLogging(expression) { StringValue(expression.value) }
    override fun eval(expression: IntLiteral) = proxyLogging(expression) { IntValue(expression.value) }
    override fun eval(expression: RealLiteral) = proxyLogging(expression) { DecimalValue(expression.value) }

    override fun eval(expression: UDateRefExpr) = proxyLogging(expression) {
        val formatter = DateTimeFormatter.ofPattern("ddMMyy")
        val date = LocalDate.now().format(formatter)
        StringValue(date)
    }

    override fun eval(expression: UYearRefExpr) = proxyLogging(expression) {
        val formatter = DateTimeFormatter.ofPattern("yy")
        val year = LocalDate.now().format(formatter)
        StringValue(year)
    }

    override fun eval(expression: UMonthRefExpr) = proxyLogging(expression) {
        val formatter = DateTimeFormatter.ofPattern("MM")
        val month = LocalDate.now().format(formatter)
        StringValue(month)
    }

    override fun eval(expression: UDayRefExpr) = proxyLogging(expression) {
        val formatter = DateTimeFormatter.ofPattern("dd")
        val day = LocalDate.now().format(formatter)
        StringValue(day)
    }

    override fun eval(expression: NumberOfElementsExpr): Value = proxyLogging(expression) {
        return@proxyLogging when (val value = expression.value.evalWith(this)) {
            is ArrayValue -> value.arrayLength().asValue()
            is OccurableDataStructValue -> value.occurs.asValue()
            else -> throw IllegalStateException("Cannot ask number of elements of $value")
        }
    }

    override fun eval(expression: DataRefExpr) = proxyLogging(expression) {
        interpreterStatus.getVar(
            expression.variable.referred
                ?: throw IllegalStateException("Unsolved reference ${expression.variable.name} at ${expression.position}")
        )
    }

    override fun eval(expression: EqualityExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        areEquals(left, right).asValue()
    }

    override fun eval(expression: DifferentThanExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        (!areEquals(left, right)).asValue()
    }

    override fun eval(expression: GreaterThanExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        isGreaterThan(left, right, localizationContext.charset).asValue()
    }

    override fun eval(expression: GreaterEqualThanExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        (isGreaterThan(left, right, localizationContext.charset) || areEquals(left, right)).asValue()
    }

    override fun eval(expression: LessEqualThanExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        (isEqualOrSmaller(left, right, localizationContext.charset)).asValue()
    }

    override fun eval(expression: LessThanExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        isSmallerThan(left, right, localizationContext.charset).asValue()
    }

    override fun eval(expression: BlanksRefExpr) = proxyLogging(expression) { BlanksValue } as BlanksValue

    override fun eval(expression: DecExpr): Value = proxyLogging(expression) {
        val decDigits = expression.decDigits.evalWith(this).asInt().value
        val valueAsString = expression.value.evalWith(this).asString().value
        val valueAsBigDecimal = valueAsString.asBigDecimal()
        require(valueAsBigDecimal != null) {
            "Line ${expression.position?.line()} - %DEC can't understand '$valueAsString'"
        }

        return@proxyLogging if (decDigits == 0L) {
            IntValue(valueAsBigDecimal.toLong())
        } else {
            DecimalValue(valueAsBigDecimal)
        }
    }

    override fun eval(expression: PlusExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return@proxyLogging when {
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

    override fun eval(expression: MinusExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return@proxyLogging when {
            left is IntValue && right is IntValue -> (left - right)
            left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal.minus(right.bigDecimal))
            else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
        }
    }

    override fun eval(expression: MultExpr): Value = proxyLogging(expression) {
        val left = expression.left.evalWith(this)
        val right = expression.right.evalWith(this)
        return@proxyLogging when {
            left is IntValue && right is IntValue -> (left * right)
            left is NumberValue && right is NumberValue -> {
                DecimalValue(left.bigDecimal.multiply(right.bigDecimal))
            }
            else -> throw UnsupportedOperationException("I do not know how to multiply $left and $right at ${expression.position}")
        }
    }

    override fun eval(expression: CharExpr): Value = proxyLogging(expression) {
        val value = expression.value.evalWith(this)
        val representation = value.stringRepresentation(expression.format)

        if (expression.value !is DivExpr) {
            return@proxyLogging StringValue(representation)
        }

        // Decimals are always returned with 10 decimal digits
        // fill with 0 if necessary
        val numDecimals = value.asDecimal().value.scale()
        return@proxyLogging when {
            numDecimals == 0 -> StringValue("$representation.0000000000")
            numDecimals < 10 -> StringValue(representation + "0".repeat(10 - numDecimals))
            else -> StringValue(representation.trim())
        }
    }

    override fun eval(expression: LookupExpr): Value = proxyLogging(expression) {
        lookup(
            array = expression.array.evalWith(this) as ArrayValue,
            arrayType = expression.array.type() as ArrayType,
            searchedValue = expression.searchedValued.evalWith(this),
            start = expression.start?.evalWith(this)?.asInt(),
            length = expression.length?.evalWith(this)?.asInt(),
            operator = ComparisonOperator.EQ
        )
    }

    override fun eval(expression: LookupGtExpr): Value = proxyLogging(expression) {
        lookup(
            array = expression.array.evalWith(this) as ArrayValue,
            arrayType = expression.array.type() as ArrayType,
            searchedValue = expression.searchedValue.evalWith(this),
            start = expression.start?.evalWith(this)?.asInt(),
            length = expression.length?.evalWith(this)?.asInt(),
            operator = ComparisonOperator.GT
        )
    }

    override fun eval(expression: LookupGeExpr): Value = proxyLogging(expression) {
        lookup(
            array = expression.array.evalWith(this) as ArrayValue,
            arrayType = expression.array.type() as ArrayType,
            searchedValue = expression.searchedValue.evalWith(this),
            start = expression.start?.evalWith(this)?.asInt(),
            length = expression.length?.evalWith(this)?.asInt(),
            operator = ComparisonOperator.GE
        )
    }

    override fun eval(expression: LookupLtExpr): Value = proxyLogging(expression) {
        lookup(
            array = expression.array.evalWith(this) as ArrayValue,
            arrayType = expression.array.type() as ArrayType,
            searchedValue = expression.searchedValue.evalWith(this),
            start = expression.start?.evalWith(this)?.asInt(),
            length = expression.length?.evalWith(this)?.asInt(),
            operator = ComparisonOperator.LT
        )
    }

    override fun eval(expression: LookupLeExpr): Value = proxyLogging(expression) {
        lookup(
            array = expression.array.evalWith(this) as ArrayValue,
            arrayType = expression.array.type() as ArrayType,
            searchedValue = expression.searchedValue.evalWith(this),
            start = expression.start?.evalWith(this)?.asInt(),
            length = expression.length?.evalWith(this)?.asInt(),
            operator = ComparisonOperator.LE
        )
    }

    override fun eval(expression: ArrayAccessExpr): Value = proxyLogging(expression) {
        val arrayValueRaw = expression.array.evalWith(this)
        val arrayValue = arrayValueRaw as? ArrayValue
            ?: throw IllegalStateException("Array access to something that does not look like an array: ${expression.render()} (${expression.position})")
        val indexValue = expression.index.evalWith(this)
        arrayValue.getElement(indexValue.asInt().value.toInt())
    }

    override fun eval(expression: HiValExpr) = proxyLogging(expression) { HiValValue } as HiValValue
    override fun eval(expression: LowValExpr) = proxyLogging(expression) { LowValValue } as LowValValue
    override fun eval(expression: ZeroExpr) = proxyLogging(expression) { ZeroValue } as ZeroValue
    override fun eval(expression: AllExpr) = proxyLogging(expression) {
        AllValue(eval(expression.charsToRepeat).asString().value)
    } as AllValue
    override fun eval(expression: TranslateExpr): Value = proxyLogging(expression) {
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
        StringValue(pair.first + right)
    }

    override fun eval(expression: LogicalAndExpr): Value = proxyLogging(expression) {
        val left = evalAsBoolean(expression.left)
        return@proxyLogging if (left) {
            expression.right.evalWith(this)
        } else {
            BooleanValue.FALSE
        }
    }

    override fun eval(expression: LogicalOrExpr): Value = proxyLogging(expression) {
        val left = evalAsBoolean(expression.left)
        return@proxyLogging if (left) {
            BooleanValue.TRUE
        } else {
            expression.right.evalWith(this)
        }
    }

    override fun eval(expression: LogicalCondition): Value = proxyLogging(expression) {
        if (expression.ands.any { !evalAsBoolean(it) } && !expression.ors.any { evalAsBoolean(it) }) {
            return@proxyLogging BooleanValue.FALSE
        }

        if (evalAsBoolean(expression.expression)) {
            return@proxyLogging BooleanValue.TRUE
        }

        BooleanValue(expression.ors.any { evalAsBoolean(it) })
    }

    override fun eval(expression: OnRefExpr) = proxyLogging(expression) { BooleanValue.TRUE } as BooleanValue
    override fun eval(expression: NotExpr) = proxyLogging(expression) {
        BooleanValue(!evalAsBoolean(expression.base))
    } as BooleanValue
    override fun eval(expression: ScanExpr): Value = proxyLogging(expression) {
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
        IntValue(if (result == -1) 0 else result.toLong() + 1)
    }

    override fun eval(expression: CheckExpr): Value = proxyLogging(expression) {
        var startPos = 0
        if (expression.start != null) {
            startPos = expression.start.evalWith(this).asInt().value.toInt()
            if (startPos > 0) {
                startPos -= 1
            }
        }
        val comparator = expression.value.evalWith(this).asString().value
        val base = expression.source.evalWith(this).asString().value.toCharArray()

        var result = 0
        for (i in startPos until base.size) {
            val currChar = base[i]
            if (!comparator.contains(currChar)) {
                result = i + 1
                break
            }
        }

        IntValue(result.toLong())
    }

    override fun eval(expression: SubstExpr): Value = proxyLogging(expression) {
        val length = if (expression.length != null) expression.length.evalWith(this).asInt().value.toInt() else 0
        val start = expression.start.evalWith(this).asInt().value.toInt() - 1
        val originalString = expression.string.evalWith(this).asString().value
        return@proxyLogging if (length == 0) {
            StringValue(originalString.padEnd(start + 1).substring(start))
        } else {
            StringValue(originalString.padEnd(start + length + 1).substring(start, start + length))
        }
    }

    override fun eval(expression: SubarrExpr): Value = proxyLogging(expression) {
        val start = expression.start.evalWith(this).asInt().value.toInt() - 1
        val numberOfElement: Int? = if (expression.numberOfElements != null) expression.numberOfElements.evalWith(this).asInt().value.toInt() else null
        val originalArray: ArrayValue = expression.array.evalWith(this).asArray()
        val to: Int = if (numberOfElement == null) {
            originalArray.arrayLength()
        } else {
            (start) + numberOfElement
        }
        originalArray.take(start, to)
    }

    override fun eval(expression: LenExpr): Value = proxyLogging(expression) {
        return@proxyLogging when (val value = expression.value.evalWith(this)) {
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

    override fun eval(expression: OffRefExpr) = proxyLogging(expression) { BooleanValue.FALSE } as BooleanValue

    override fun eval(expression: IsoFormatExpr) = proxyLogging(expression) { IsoValue } as IsoValue
    override fun eval(expression: JulFormatExpr) = proxyLogging(expression) { JulValue } as JulValue

    override fun eval(expression: NegationExpr): DecimalValue = proxyLogging(expression) {
        val value = expression.value1.evalWith(this).asDecimal().value
        DecimalValue(-value)
    } as DecimalValue

    override fun eval(expression: IndicatorExpr): BooleanValue = proxyLogging(expression) {
        // if index is passed through expression, it means that it is a dynamic indicator
        val runtimeIndex = expression.indexExpression?.evalWith(this)?.asInt()?.value?.toInt() ?: expression.index
        interpreterStatus.indicator(runtimeIndex)
    } as BooleanValue

    override fun eval(expression: FunctionCall): Value = proxyLogging(expression) {
        val functionToCall = expression.function.name
        val function = systemInterface.findFunction(interpreterStatus.symbolTable, functionToCall)
            ?: throw RuntimeException("Function $functionToCall cannot be found (${expression.position.line()})")
        FunctionWrapper(function = function, functionName = functionToCall, expression).let { functionWrapper ->
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

    override fun eval(expression: TimeStampExpr): Value = proxyLogging(expression) {
        if (expression.value == null) {
            return@proxyLogging TimeStampValue.now()
        } else {
            val evaluated = expression.value.evalWith(this)
            if (evaluated is StringValue) {
                return@proxyLogging TimeStampValue.of(evaluated.value)
            }
            TODO("TimeStamp parsing: " + evaluated)
        }
    }

    override fun eval(expression: EditcExpr): Value = proxyLogging(expression) {
        val n = expression.value.evalWith(this)
        val format = expression.format.evalWith(this)
        if (format !is StringValue) throw UnsupportedOperationException("Required string value, but got $format at ${expression.position}")
        n.asDecimal().formatAs(format.value, expression.value.type(), localizationContext.decedit)
    }

    override fun eval(expression: DiffExpr): Value = proxyLogging(expression) {
        val v1 = expression.value1.evalWith(this)
        val v2 = expression.value2.evalWith(this)
        return@proxyLogging when (expression.durationCode) {
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

    override fun eval(expression: DivExpr): Value = proxyLogging(expression) {
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
        DecimalValue(res)
    }

    override fun eval(expression: ExpExpr): Value = proxyLogging(expression) {
        val v1 = expression.left.evalWith(this)
        val v2 = expression.right.evalWith(this)
        DecimalValue(BigDecimal(v1.asInt().value.toDouble().pow(v2.asInt().value.toDouble())))
    }

    override fun eval(expression: TrimrExpr): Value = proxyLogging(expression) {
        return@proxyLogging if (expression.charactersToTrim == null) {
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

    override fun eval(expression: TrimlExpr): Value = proxyLogging(expression) {
        return@proxyLogging if (expression.charactersToTrim == null) {
            StringValue(evalAsString(expression.value).trimStart())
        } else {
            val prefix = evalAsString(expression.charactersToTrim)
            var result = evalAsString(expression.value)
            while (result.startsWith(prefix)) {
                result = result.substringAfter(prefix)
            }
            StringValue(result)
        }
    }

    override fun eval(expression: TrimExpr) = proxyLogging(expression) {
        StringValue(evalAsString(expression.value).trim())
    } as StringValue
    override fun eval(expression: FoundExpr): Value = proxyLogging(expression) {
        // TODO fix this bad implementation
        if (expression.name == null) {
            return@proxyLogging BooleanValue(interpreterStatus.lastFound)
        }
        TODO("Line ${expression.position?.line()} - %FOUND expression with file names is not implemented yet")
    }

    override fun eval(expression: EofExpr): Value = proxyLogging(expression) {
        if (expression.name == null) {
            return@proxyLogging BooleanValue(interpreterStatus.lastDBFile?.eof() ?: false)
        }
        TODO("Line ${expression.position?.line()} - %EOF expression with file names is not implemented yet")
    }

    override fun eval(expression: EqualExpr): Value = proxyLogging(expression) {
        if (expression.name == null) {
            return@proxyLogging BooleanValue(interpreterStatus.lastDBFile?.equal() ?: false)
        }
        TODO("Line ${expression.position?.line()} - %EQUAL expression with file names is not implemented yet")
    }

    override fun eval(expression: AbsExpr): Value = proxyLogging(expression) {
        val value = evalAsDecimal(expression.value)
        DecimalValue(BigDecimal.valueOf(abs(value.toDouble())))
    }

    override fun eval(expression: EditwExpr): Value = proxyLogging(expression) {
        val n = expression.value.evalWith(this)
        val format = evalAsString(expression.format)
        n.asDecimal().formatAsWord(format, expression.value.type())
    }

    override fun eval(expression: IntExpr): Value = proxyLogging(expression) {
        return@proxyLogging when (val value = expression.value.evalWith(this)) {
            is StringValue ->
                IntValue(cleanNumericString(value.value).asLong())

            is DecimalValue ->
                value.asInt()

            is UnlimitedStringValue ->
                IntValue(cleanNumericString(value.value).asLong())

            else -> throw UnsupportedOperationException("I do not know how to handle $value with %INT")
        }
    }

    override fun eval(expression: InthExpr): Value = proxyLogging(expression) {
        when (val value = expression.value.evalWith(this)) {
            is StringValue ->
                IntValue(value.value.toDouble().roundToLong())

            is DecimalValue ->
                IntValue(value.value.toDouble().roundToLong())

            is UnlimitedStringValue ->
                IntValue(value.value.toDouble().roundToLong())

            else -> throw UnsupportedOperationException("I do not know how to handle $value with %INTH")
        }
    }

    override fun eval(expression: RemExpr): Value = proxyLogging(expression) {
        val n = evalAsLong(expression.dividend)
        val m = evalAsLong(expression.divisor)
        IntValue(n % m)
    }

    override fun eval(expression: QualifiedAccessExpr): Value = proxyLogging(expression) {
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
        return@proxyLogging dataStringValue[expression.field.referred
            ?: throw IllegalStateException("Referenced to field not resolved: ${expression.field.name}")]
    }

    override fun eval(expression: ReplaceExpr): Value = proxyLogging(expression) {
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
        return@proxyLogging if (result.length > expression.source.type().elementSize()) {
            StringValue(result.substring(0, expression.source.type().elementSize()), expression.source.type().hasVariableSize())
        } else {
            StringValue(result, expression.source.type().hasVariableSize())
        }
    }

    override fun eval(expression: SqrtExpr): Value = proxyLogging(expression) {
        val value = evalAsDecimal(expression.value)
        DecimalValue(BigDecimal.valueOf(sqrt(value.toDouble())))
    }

    override fun eval(expression: AssignmentExpr) =
        throw RuntimeException("AssignmentExpr should be handled by the interpreter: $expression")

    override fun eval(expression: GlobalIndicatorExpr): Value = proxyLogging(expression) {
        val value = StringBuilder()
        for (i in IndicatorType.Predefined.range) {
            value.append(if (interpreterStatus.indicators[i] == null) "0" else if (interpreterStatus.indicators[i]!!.value) "1" else "0")
        }
        StringValue(value.toString())
    }

    override fun eval(expression: ParmsExpr): Value = proxyLogging(expression) { IntValue(interpreterStatus.params.toLong()) }

    override fun eval(expression: OpenExpr): Value = proxyLogging(expression) {
        val name = expression.name
        require(name != null) {
            "Line ${expression.position?.line()} - %OPEN require a table name"
        }
        val enrichedDBFile = interpreterStatus.dbFileMap.get(name)
            ?: throw RuntimeException("Table $name cannot be found (${expression.position.line()})")
        BooleanValue(enrichedDBFile.open)
    }

    override fun eval(expression: SizeExpr): Value = proxyLogging(expression) {
        return@proxyLogging when (expression.value) {
            is DataRefExpr -> (expression.value as DataRefExpr).variable.referred?.type?.size?.let { IntValue(it.toLong()) }
                ?: throw UnsupportedOperationException("${(expression.value as DataRefExpr).variable.name} not referred with %SIZE")
            // Could be:
            //  - literal
            //  - array{:*ALL}
            //  - table{:*ALL}
            //  - multiple-occurrence data structure{:*ALL}
            else -> throw UnsupportedOperationException("I do not know how to handle ${expression.value} with %SIZE. Is '${expression.value.javaClass.simpleName}' class.")
        }
    }

    override fun eval(expression: MockExpression): Value {
        MainExecutionContext.getConfiguration().jarikoCallback.onMockExpression(expression)
        return NullValue
    }

    private fun lookupLinearSearch(values: List<Value>, target: Value): Int {
        for ((index, value) in values.withIndex()) {
            if (areEquals(value, target))
                return index
        }

        return -1
    }

    private inline fun lookupBinarySearchWithComparator(
        values: List<Value>,
        predicate: (Value) -> Boolean,
        isAscending: Boolean,
        searchingForLower: Boolean
    ): Int {
        var (left, right) = Pair(0, values.lastIndex)
        var bestCandidateIndex = -1

        while (left <= right) {
            val mid = left + (right - left) / 2
            val currentValue = values[mid]
            val matchesCondition = predicate(currentValue)
            if (matchesCondition) bestCandidateIndex = mid

            /*
             * Detect if sorting order follows search direction in order to decide which endpoint to move
             * - if it follows direction: move left on match and move right else-wise
             * - if it does not follow direction: move right on match and move left else-wise
             */
            val shouldSearchRight = if (isAscending == searchingForLower) matchesCondition else !matchesCondition
            if (shouldSearchRight) left = mid + 1 else right = mid - 1
        }

        return bestCandidateIndex
    }

    private inline fun lookupBinarySearchWithCondition(values: List<Value>, predicate: (Value) -> Boolean, target: Value): Int {
        var (left, right) = Pair(0, values.lastIndex)

        while (left <= right) {
            val mid = left + (right - left) / 2
            val currentValue = values[mid]

            if (predicate(currentValue)) {
                return mid
            }

            if (currentValue < target) {
                right = mid - 1
                continue
            }

            // This means currentValue > target
            left = mid + 1
        }

        return -1
    }

    private fun lookupBinarySearch(
        values: List<Value>,
        target: Value,
        operator: ComparisonOperator,
        isAscending: Boolean
    ): Int {
        return when (operator) {
            ComparisonOperator.LE ->
                lookupBinarySearchWithComparator(values, { it <= target }, isAscending, true)
            ComparisonOperator.LT ->
                lookupBinarySearchWithComparator(values, { it < target }, isAscending, true)
            ComparisonOperator.GE ->
                lookupBinarySearchWithComparator(values, { it >= target }, isAscending, false)
            ComparisonOperator.GT ->
                lookupBinarySearchWithComparator(values, { it > target }, isAscending, false)
            ComparisonOperator.EQ -> lookupBinarySearchWithCondition(values, { areEquals(it, target) }, target)
            else -> -1
        }
    }

    private fun lookup(
        array: ArrayValue,
        arrayType: ArrayType,
        searchedValue: Value,
        start: IntValue?,
        length: IntValue?,
        operator: ComparisonOperator
    ): Value {
        val arrayLength = arrayType.numberOfElements()
        val isSequenced = arrayType.ascend != null

        when (operator) {
            ComparisonOperator.GE, ComparisonOperator.GT, ComparisonOperator.LE, ComparisonOperator.LT -> {
                if (!isSequenced)
                    throw UnsupportedOperationException("Array must be defined with ASCEND or DESCEND keyword")
            }
            else -> {}
        }

        val computedLength = length?.value?.toInt() ?: arrayLength
        val lowerBound: Int = start?.value?.let { it - 1 }?.toInt() ?: 0
        val upperBound = min(lowerBound + computedLength, arrayLength)
        val searchRange = array.elements().slice(lowerBound until upperBound)

        val index = if (isSequenced)
            lookupBinarySearch(searchRange, searchedValue, operator, arrayType.ascend!!)
        else lookupLinearSearch(searchRange, searchedValue)

        val offsetIndex = if (index == -1) 0 else index + lowerBound + 1
        return offsetIndex.asValue()
    }

    private fun cleanNumericString(s: String): String {
        val result = s.moveEndingString("-")
        return when {
            result.contains(".") -> result.substringBefore(".")
            result.contains(",") -> result.substringBefore(",")
            else -> result
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
}
