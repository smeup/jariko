package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.XFootStmt
import java.math.BigDecimal

fun xfoot(statement: XFootStmt, interpreterCore: InterpreterCore) {
    // ConcreteArrayValue
    val arrayValue = interpreterCore.eval(statement.left).asArray()
    val type = arrayValue.elementType as NumberType

    // list of DecimalValue
    val searchedArray = arrayValue.elements()
    val sum = searchedArray.fold(0.0) { acc, curr ->
        // scale value
        val bd: BigDecimal = (curr as DecimalValue).value
        val newDecimalValue = DecimalValue(BigDecimal(bd.unscaledValue(), type.decimalDigits))
        acc + newDecimalValue.asDecimal().value.toDouble()
    }

    val sumValue = DecimalValue(BigDecimal(sum))
    interpreterCore.assign(statement.result, sumValue)
    // TODO ???  searchResult.setIndicators(interpreterCore, statement)
}
