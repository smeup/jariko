package com.smeup.rpgparser.performance

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*

open class Kute {

    private val localizationContext: LocalizationContext = LocalizationContext()
    var globalSymbolTable = SymbolTable()

    fun enterCondition(index: Value, end: Value, downward: Boolean): Boolean =
            if (downward) {
                isEqualOrGreater(index, end, localizationContext.charset)
            } else {
                isEqualOrSmaller(index, end, localizationContext.charset)
            }

    fun step(byValue: Expression, downward: Boolean): Long {
        val sign = if (downward) {
            -1
        } else {
            1
        }
        return eval(byValue).asInt().value * sign
    }

    fun increment(dataDefinition: AbstractDataDefinition, amount: Long = 1) {
        val value = this[dataDefinition]
        if (value is IntValue) {
            this[dataDefinition] = IntValue(value.value + amount)
        } else {
            throw UnsupportedOperationException()
        }
    }

    fun optimizedIntExpression(expression: Expression): () -> Long =
            if (expression is IntLiteral || expression is FigurativeConstantRef) {
                val constValue = eval(expression).asInt().value
                { constValue }
            } else {
                { eval(expression).asInt().value }
            }

    fun eval(expression: Expression): Value {
        return when (expression) {
            is AssignmentExpr -> {
                assign(expression.target, expression.value)
            }
            else -> interpret(expression)
        }
    }

    fun assign(target: AssignableExpression, value: Expression, operator: AssignmentOperator = NORMAL_ASSIGNMENT): Value {
        return when (operator) {
            NORMAL_ASSIGNMENT -> assign(target, eval(value))
            PLUS_ASSIGNMENT -> assign(target, eval(PlusExpr(target, value)))
            MINUS_ASSIGNMENT -> assign(target, eval(MinusExpr(target, value)))
            MULT_ASSIGNMENT -> assign(target, eval(MultExpr(target, value)))
            DIVIDE_ASSIGNMENT -> assign(target, eval(DivExpr(target, value)))
            EXP_ASSIGNMENT -> assign(target, eval(ExpExpr(target, value)))
        }
    }

    fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value {
        val coercedValue = coerce(value, dataDefinition.type)
        set(dataDefinition, coercedValue)
        return coercedValue
    }

    fun assign(target: AssignableExpression, value: Value): Value {
        when (target) {
            is DataRefExpr -> {
                return assign(target.variable.referred!!, value)
            }
            else -> TODO(target.toString())
        }
    }

    fun assignEachElement(target: AssignableExpression, value: Value): Value {
        val arrayType = target.type().asArray()
        return assign(target, value.toArray(arrayType.nElements, arrayType.element))
    }

    fun assignEachElement(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator = NORMAL_ASSIGNMENT
    ): Value {
    return when (operator) {
        NORMAL_ASSIGNMENT -> assignEachElement(target, eval(value))
        PLUS_ASSIGNMENT -> assignEachElement(target, eval(PlusExpr(target, value)))
        MINUS_ASSIGNMENT -> assignEachElement(target, eval(MinusExpr(target, value)))
        MULT_ASSIGNMENT -> assignEachElement(target, eval(MultExpr(target, value)))
        DIVIDE_ASSIGNMENT -> assignEachElement(target, eval(DivExpr(target, value)))
        EXP_ASSIGNMENT -> assignEachElement(target, eval(ExpExpr(target, value)))
    }
    }

    private fun interpret(expression: Expression): Value {
        val value = interpretConcrete(expression)
        return value
    }

    private fun interpretConcrete(expression: Expression): Value {
        // PAY ATTENTION: only a reduced set of expressions is available
        return when (expression) {
            is StringLiteral -> StringValue(expression.value)
            is IntLiteral -> IntValue(expression.value)
            is DataRefExpr -> get(
                    expression.variable.referred
                            ?: throw IllegalStateException("[Kute] Unsolved reference ${expression.variable.name} at ${expression.position}")
            )
            is LessThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return isSmallerThan(left, right, localizationContext.charset).asValue()
            }
            is EqualityExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return areEquals(left, right).asValue()
            }
            is PlusExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
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
            else -> TODO(expression.toString())
        }
    }

    fun execute(statement: EvalStmt) {
        val result = if (statement.target.type().isArray() &&
                statement.target.type().asArray().element.canBeAssigned(statement.expression.type())) {
            assignEachElement(statement.target, statement.expression, statement.operator)
        } else {
            assign(statement.target, statement.expression, statement.operator)
        }
    }

    inline fun measureTimeMillis(block: () -> Unit): Long {
        val start = System.currentTimeMillis()
        block()
        return System.currentTimeMillis() - start
    }

    operator fun get(data: AbstractDataDefinition): Value {
        return globalSymbolTable[data]
    }

    operator fun get(dataName: String) = globalSymbolTable[dataName]

    operator fun set(data: AbstractDataDefinition, value: Value) {
        require(data.canBeAssigned(value)) {
            "${data.name} of type ${data.type} defined at line ${data.position.line()} cannot be assigned the value $value"
        }

        when (data) {
            // Field are stored within the Data Structure definition
            is FieldDefinition -> {
                val ds = data.parent as DataDefinition
                if (data.declaredArrayInLine != null) {
                    val dataStructValue = get(ds.name) as DataStructValue
                    var startOffset = data.startOffset
                    var size = data.endOffset - data.startOffset

                    // for (i in 1..data.declaredArrayInLine!!) {
                    // If the size of the arrays are different
                    val maxElements = Math.min(value.asArray().arrayLength(), data.declaredArrayInLine!!)
                    for (i in 1..maxElements) {
                        // Added coerce
                        val valueToAssign = coerce(value.asArray().getElement(i), data.type.asArray().element)
                        dataStructValue.setSubstring(startOffset, startOffset + size,
                                data.type.asArray().element.toDataStructureValue(valueToAssign))
                        startOffset += data.stepSize.toInt()
                    }
                } else {
                    when (val containerValue = get(ds.name)) {
                        is ArrayValue -> {
                            val valuesToAssign = value as ArrayValue
                            require(containerValue.arrayLength() == valuesToAssign.arrayLength())
                            // The container value is an array of datastructurevalues
                            // we assign to each data structure the corresponding field value
                            for (i in 1..containerValue.arrayLength()) {
                                val dataStructValue = containerValue.getElement(i) as DataStructValue
                                dataStructValue.setSingleField(data, valuesToAssign.getElement(i))
                            }
                        }
                        is DataStructValue -> {
                            containerValue.set(data, value)
                        }
                        else -> TODO()
                    }
                }
            }
            else -> {
                var previous: Value? = null
                if (data.name in globalSymbolTable) {
                    previous = globalSymbolTable[data.name]
                }
                globalSymbolTable[data] = coerce(value, data.type)
            }
        }
    }
}