import com.smeup.rpgparser.PerformanceTest
import org.junit.Test
import org.junit.experimental.categories.Category
import kotlin.test.assertTrue

class Kute10_53 {

    var actualElapsedTimeInMillisec = 0L
    val expectedElapsedTimeInMillisec = 68L

    @Test
    @Category(PerformanceTest::class)
    public fun performanceComparing() {
        var myIterValue = 0L
        val endLimit: Long = 10000000L
        val startTime = System.currentTimeMillis()
        while (myIterValue <= endLimit) {
            myIterValue++
        }
        actualElapsedTimeInMillisec = System.currentTimeMillis() - startTime
        var message = "Expected execution takes less or same to $expectedElapsedTimeInMillisec ms. Actual is $actualElapsedTimeInMillisec ms."
        assertTrue(actualElapsedTimeInMillisec <= expectedElapsedTimeInMillisec, message)
    }
}