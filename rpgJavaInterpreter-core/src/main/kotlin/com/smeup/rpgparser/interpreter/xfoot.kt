package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.XFootStmt
import java.math.BigDecimal

fun xfoot(array: ArrayValue): DecimalValue {
    val type: NumberType = array.elementType as NumberType
    // list of DecimalValue
    val searchedArray: List<Value> = array.elements()
    var sum = 0.0
    for (value in searchedArray) {
        // scale value
        val bd = (value as DecimalValue).value
        val newDecimalValue = BigDecimal(bd.unscaledValue(), type.decimalDigits).asValue()
        sum += newDecimalValue.asDecimal().value.toDouble()
    }
    return BigDecimal(sum).asValue()
}

fun xfoot(statement: XFootStmt, interpreterCore: InterpreterCore) {
    // ConcreteArrayValue
    val arrayValue = interpreterCore.eval(statement.left).asArray()
    val result = xfoot(arrayValue)
    interpreterCore.assign(statement.result, result)
    // TODO ???  searchResult.setIndicators(interpreterCore, statement)
}
