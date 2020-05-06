package com.smeup.rpgparser.performance

import com.smeup.rpgparser.fastmaps.IntArrayMap
import java.util.AbstractMap
import java.util.LinkedHashMap
import kotlin.system.measureTimeMillis

const val ITERATIONS = 1_000_100
const val NUM_VARS = 100

fun loop(map: AbstractMap<Int, Int>, numVar: Int) {
    val elapsed = measureTimeMillis {
        for (i in 1..numVar) {
            map[i] = 0
        }
        for (i in 1..ITERATIONS) {
            for (i in 1..numVar) {
                map[i] = map[i]!! + 1
            }
        }
    }
    println("elapsed: $elapsed")
}

fun main() {
    for (i in 1..10) {
        println("LinkedHashMap")
        loop(LinkedHashMap<Int, Int>(), NUM_VARS)
        println("IntArrayMap")
        loop(IntArrayMap<Int>(0, 1000), NUM_VARS)
    }
}
