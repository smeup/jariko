import com.smeup.rpgparser.interpreter.*
import java.nio.charset.Charset
import kotlin.test.assertTrue

class Kute10_54 {

    val charset = Charset.forName("Cp037")
    var actualElapsedTimeInMillisec = 0L
    val expectedElapsedTimeInMillisec = 306L

    public fun performanceComparing(): Array<String> {
        var loopCounter = 0L
        var startValue: Value = IntValue(0)
        val endLimit: Value = IntValue(10000000)
        val stepValue: Value = IntValue(1)
        val downward: Boolean = false
        val startTime = System.currentTimeMillis()
        try {
            while (enterCondition(startValue, endLimit, downward)) {
                startValue = increment(startValue, step(stepValue, downward))
                loopCounter++
            }
            actualElapsedTimeInMillisec = System.currentTimeMillis() - startTime
        } catch (e: LeaveException) {
            // leaving
            actualElapsedTimeInMillisec = System.currentTimeMillis() - startTime
        }

        var message1 = "Expected execution takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message1)

        return arrayOf(message1)
    }

    private fun increment(startValue: Value, amount: Long = 1): Value {
        var value = startValue
        if (value is IntValue) value = IntValue(value.value + amount) else {
            throw UnsupportedOperationException()
        }
        return value
    }

    private fun enterCondition(index: Value, end: Value, downward: Boolean): Boolean =
        if (downward) {
            isEqualOrGreater(index, end, charset)
        } else {
            isEqualOrSmaller(index, end, charset)
        }

    private fun step(value: Value, downward: Boolean): Long {
        val sign = if (downward) {
            -1
        } else {
            1
        }
        return value.asInt().value * sign
    }
}

fun main(args: Array<String>) {
    Kute10_54().performanceComparing().forEach { println(it) }
}