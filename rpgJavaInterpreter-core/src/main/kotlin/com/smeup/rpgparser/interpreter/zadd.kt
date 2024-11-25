package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import java.math.BigDecimal

/**
 * Implements the Z-ADD (Zero-Add) operation, which adds a value to a target, coercing and truncating
 * the value as necessary to conform to the target's type, based on AS400 programming behavior.
 *
 * Z-ADD is a legacy operation commonly used in RPG programming to assign values to variables.
 * It ensures compatibility between different numeric types by applying truncation rules
 * for both integer and decimal portions of numbers.
 *
 * Behavior and examples (aligned with AS400 behavior):
 * - When the source value is too large to fit into the target's specified number of integer
 *   or decimal digits, truncation occurs. For example:
 *     - A source with value 241122 assigned to a target with type (4,0) results in: 1122
 *     - A source with value 123.456 assigned to a target with type (4,2) results in: 23.45
 * - Special handling is applied for specific value types, such as UDateRefExpr, UYearRefExpr,
 *   UMonthRefExpr, and UDayRefExpr, which are evaluated as strings and converted to integers.
 *
 * @param value the value to be added or assigned, which may require coercion or truncation
 * @param target the target to which the value will be assigned
 * @param interpreterCore the core interpreter managing evaluation and assignments
 * @return the resulting value after assignment
 *
 * For more information, see this documentation: https://www.ibm.com/docs/en/i/7.5?topic=codes-z-add-zero-add
 */
fun zadd(
    value: Expression,
    target: AssignableExpression,
    interpreterCore: InterpreterCore
): Value {
    val valueCoerced = when {
        // U<Date|Year|Month|Day>RefExpr is evaluated as String on Jariko. So, is possible to convert this result to integer.
        value is UDateRefExpr || value is UYearRefExpr || value is UMonthRefExpr || value is UDayRefExpr -> {
            interpreterCore.eval(value).asInt()
        }
        else -> interpreterCore.eval(value)
    }

    return when {
        /*
         * On AS400 there is a truncation for the entire and decimal part based of source and target declarations.
         * For example:
         *  - D SRC             S              6  0 INZ(241122)
         *    C                   Z-ADD     SRC           RES               4 0
         *    produces this result: 1122
         *  - D SRC             S              6  3 INZ(123.456)
         *    C                   Z-ADD     SRC           RES               4 2
         *    produces this result: 23.45
         * The comma isolates the truncation between entire and decimal part.
         *
         * IMPORTANT: This is possible ONLY when ZAddLegacy FeatureFlag is enabled.
         */
        valueCoerced is NumberValue && target.type() is NumberType && interpreterCore.getSystemInterface().getFeaturesFactory().isZAddLegacy() -> {
            val targetEntireDigits = (target.type() as NumberType).entireDigits
            val targetDecimalDigits = (target.type() as NumberType).decimalDigits

            val integerValueTruncated = valueCoerced.asString().value
                .substringBefore(".")
                .reversed()
                .secureSubstring(0, targetEntireDigits)
                .reversed()
            val decimalValueTruncated = if (valueCoerced is IntValue) "" else valueCoerced.asString().value
                .substringAfter(".")
                .secureSubstring(0, targetDecimalDigits)
            val stringValueTruncated = if (decimalValueTruncated.isNotBlank()) "$integerValueTruncated.$decimalValueTruncated" else integerValueTruncated

            interpreterCore.assign(target, DecimalValue(BigDecimal(stringValueTruncated)))
        }
        else -> interpreterCore.assign(target, valueCoerced)
    }
}

/**
 * Safely extracts a substring from the given string, ensuring the end index does not exceed the string's length.
 *
 * This function prevents `StringIndexOutOfBoundsException` by resizing the end index
 * if it is larger than the string's actual length.
 *
 * @param start the starting index of the substring (inclusive)
 * @param end the ending index of the substring (exclusive); adjusted if it exceeds the string's length
 * @return the extracted substring from the specified range, or the substring up to the end of the string
 *         if the provided end index is out of bounds
 */
private fun String.secureSubstring(start: Int, end: Int): String {
    var endResized = end
    if (this.length < end) {
        endResized = this.length
    }
    return substring(start, endResized)
}