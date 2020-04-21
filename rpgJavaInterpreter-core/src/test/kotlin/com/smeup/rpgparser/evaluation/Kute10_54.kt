import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.interpreter.*
import org.junit.Test
import org.junit.experimental.categories.Category
import java.nio.charset.Charset
import kotlin.test.assertTrue

class Kute10_54 {

    val charset = Charset.forName("Cp037")
    var actualElapsedTimeInMillisec = 0L
    val expectedElapsedTimeInMillisec = 306L

    @Test
    @Category(PerformanceTest::class)
    public fun performanceComparing() {
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

        var message = "Expected execution takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message)
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