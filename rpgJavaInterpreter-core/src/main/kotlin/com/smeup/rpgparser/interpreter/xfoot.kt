package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.XFootStmt
import java.math.BigDecimal

fun xfoot(statement: XFootStmt, interpreterCore: InterpreterCore) {
    // ConcreteArrayValue
    val arrayValue: ArrayValue = interpreterCore.eval(statement.left).asArray()
    val type: NumberType = arrayValue.elementType as NumberType
    // list of DecimalValue
    val searchedArray: List<Value> = arrayValue.elements()
    var sum: Double = 0.0
    for (value in searchedArray) {
        // scale value
        val bd: BigDecimal = (value as DecimalValue).value
        val newDecimalValue = DecimalValue(BigDecimal(bd.unscaledValue(), type.decimalDigits))
        sum += newDecimalValue.asDecimal().value.toDouble()
    }
    val sumValue = DecimalValue(BigDecimal(sum))
    interpreterCore.assign(statement.result, sumValue)
    // TODO ???  searchResult.setIndicators(interpreterCore, statement)
}
