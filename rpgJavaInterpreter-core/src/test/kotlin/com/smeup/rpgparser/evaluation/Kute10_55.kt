import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.interpreter.*
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.assertTrue

class Kute10_55 {

    var actualElapsedTimeInMillisec = 0L
    val expectedElapsedTimeInMillisec = 83L
    var loopCounter = 0L

    @Test
    @Category(PerformanceTest::class)
    public fun performanceComparing() {

        val endLimit: Long = 10000000L
        val startTime = System.currentTimeMillis()
        while (condition(loopCounter, endLimit).asBoolean().value) {
            execute()
        }
        actualElapsedTimeInMillisec = System.currentTimeMillis() - startTime
        var message = "Expected execution takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message)
    }

    private fun condition(loopCounter: Long, endLimit: Long): BooleanValue {
        return BooleanValue(loopCounter <= endLimit)
    }

    private fun execute() {
        loopCounter++
    }
}