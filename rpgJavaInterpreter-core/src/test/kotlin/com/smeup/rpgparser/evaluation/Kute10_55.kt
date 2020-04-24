import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.strumenta.kolasu.model.ReferenceByName
import org.junit.Test
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
    Kute10_55.kt is the "hybrid" way to perform the similar MUTE10_55.rpgle DOW (do-while) loop code.

    - (MUTE10_55.rpgle)
       C                   DOW       $X<10000001
       C                   EVAL      $X=$X+1
       C                   ENDDO

    - (Kute10_55.kt)
       while (eval(lessThanExpr).asBoolean().value) {
           execute(evalStatement)
           loopCounter++
       }

    "Hybrid" means it uses both Kotlin technology and the Jariko intepreter implementation, so, for example
    the increment of the variable checked for each iteration is done using "EvalStmt" (not simply the plus "+" operator)
    and the value of the variable is stored (read/write) in globalSymbolTable (SymbolTable) Map.
    This approach should be useful for developer to be more efficient to develop/debug code due to
    performance tuning, in a more concise developing environment.

 */

class Kute10_55 {

    private val charset = Charset.forName("Cp037")
    private var globalSymbolTable = SymbolTable()
    private val expectedElapsedTimeInMillisec = 83L
    private var loopCounter = 0L

    @Test
    public fun performanceComparing() {

        // The variable name, 'X' instead of '$X' due to Kotlin syntax
        val name = "X"
        val type = NumberType(10, 0, "I")
        var referred: DataDefinition = DataDefinition(name, type)
        var variable: ReferenceByName<AbstractDataDefinition> = ReferenceByName("X", referred)
        var dataRefExpr = DataRefExpr(variable)

        // Store the variable
        globalSymbolTable[referred] = IntValue.ZERO.asInt()

        // Create the EvalStmt (similar to 'toAst' does)
        val rightInt = IntLiteral(10000000)
        val lessThanExpr = LessThanExpr(dataRefExpr, rightInt)
        val intLiteral = IntLiteral(1)
        val plusExpr = PlusExpr(dataRefExpr, intLiteral)
        var evalStatement = dummyEvalStmt(dataRefExpr, plusExpr, NORMAL_ASSIGNMENT)

        val actualElapsedTimeInMillisec = measureTimeMillis {
            // 'DOW' implementation
            doWhile(lessThanExpr, evalStatement)
        }

        // Results
        var message = "Expected execution of ${rightInt.value} iterations ($loopCounter done) takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message)
        message = "Expected execution of ${rightInt.value} iterations, actual is $loopCounter iterations."
        assertEquals(rightInt.value, loopCounter, message)
    }

    private fun doWhile(lessThanExpr: Expression, evalStatement: EvalStmt) {
        while (eval(lessThanExpr).asBoolean().value) {
            execute(evalStatement)
            loopCounter++
        }
    }

    private fun execute(statement: EvalStmt) {
        val result = if (statement.target.type().isArray() &&
                statement.target.type().asArray().element.canBeAssigned(statement.expression.type())) {
            assignEachElement(statement.target, statement.expression, statement.operator)
        } else {
            assign(statement.target, statement.expression, statement.operator)
        }
    }

    private fun condition(loopCounter: Long, endLimit: Long): BooleanValue {
        return BooleanValue(loopCounter <= endLimit)
    }

    private fun eval(expression: Expression): Value {
        return when (expression) {
            is AssignmentExpr -> {
                assign(expression.target, expression.value)
            }
            else -> interpret(expression)
        }
    }

    private fun assign(target: AssignableExpression, value: Expression, operator: AssignmentOperator = NORMAL_ASSIGNMENT): Value {
        return when (operator) {
        NORMAL_ASSIGNMENT -> assign(target, eval(value))
        PLUS_ASSIGNMENT -> assign(target, eval(PlusExpr(target, value)))
        MINUS_ASSIGNMENT -> assign(target, eval(MinusExpr(target, value)))
        MULT_ASSIGNMENT -> assign(target, eval(MultExpr(target, value)))
        DIVIDE_ASSIGNMENT -> assign(target, eval(DivExpr(target, value)))
        EXP_ASSIGNMENT -> assign(target, eval(ExpExpr(target, value)))
        }
    }

    private fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value {
        val coercedValue = coerce(value, dataDefinition.type)
        set(dataDefinition, coercedValue)
        return coercedValue
    }

    private fun assign(target: AssignableExpression, value: Value): Value {
        when (target) {
            is DataRefExpr -> {
                return assign(target.variable.referred!!, value)
            }
            else -> TODO(target.toString())
        }
    }

    private fun assignEachElement(
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

    private fun assignEachElement(target: AssignableExpression, value: Value): Value {
        val arrayType = target.type().asArray()
        return assign(target, value.toArray(arrayType.nElements, arrayType.element))
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
                            ?: throw IllegalStateException("[Kute10_55] Unsolved reference ${expression.variable.name} at ${expression.position}")
            )
            is LessThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return isSmallerThan(left, right, charset).asValue()
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

    private fun dummyEvalStmt(dataRefExpr: DataRefExpr, plusExpr: Expression, plusAssignmentOperator: AssignmentOperator): EvalStmt {
        val extenders = charArrayOf('H', 'M', 'R')
        val flags = EvalFlags(
                halfAdjust = 'H' in extenders,
                maximumNumberOfDigitsRule = 'M' in extenders,
                resultDecimalPositionRule = 'R' in extenders
        )
        return EvalStmt(
                dataRefExpr,
                plusExpr,
                plusAssignmentOperator,
                flags = flags,
                position = null)
    }

    private inline fun measureTimeMillis(block: () -> Unit): Long {
        val start = System.currentTimeMillis()
        block()
        return System.currentTimeMillis() - start
    }
}