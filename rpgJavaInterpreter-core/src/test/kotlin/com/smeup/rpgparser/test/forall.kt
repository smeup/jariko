package com.smeup.rpgparser.test

import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong
import kotlin.test.fail

fun <T> forAll(
    generator: (Random) -> T,
    iterations: Int = 100,
    random: Random = Random.Default,
    propertyName: String = "",
    property: T.() -> Boolean
) {
    require(iterations > 0) {
        "Iterations must be greater than 0, but they were $iterations"
    }

    for (i in 1..iterations) {
        val sample = generator(random)
        val result = try {
            sample.property()
        } catch (assertionError: AssertionError) {
            throw assertionError
        } catch (t: Throwable) {
            throw AssertionError("Property $propertyName generated an exception for $sample", t)
        }
        if (!result) {
            fail("Property $propertyName not satisfied for $sample")
        }
    }
}

fun integerGenerator(intRange: IntRange): (Random) -> Int = { random: Random -> random.nextInt(intRange) }

fun longGenerator(longRange: LongRange): (Random) -> Long = { random: Random -> random.nextLong(longRange) }

fun <A, B> pairGenerator(generator1: (Random) -> A, generator2: (Random) -> B) = { random: Random -> Pair(generator1(random), generator2(random)) }

fun <A, B, C> tripleGenerator(generator1: (Random) -> A, generator2: (Random) -> B, generator3: (Random) -> C) = { random: Random -> Triple(generator1(random), generator2(random), generator3(random)) }
