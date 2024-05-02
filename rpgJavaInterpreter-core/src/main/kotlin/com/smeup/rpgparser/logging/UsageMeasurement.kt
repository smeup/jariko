package com.smeup.rpgparser.logging

import kotlin.time.Duration

/**
 * Object used to keep track of the usage of statements, expression or other parts of the execution.
 * @see ProgramUsageStats
 */
data class UsageMeasurement(val duration: Duration, val hit: Long) {
    companion object {
        /**
         * Create a new empty UsageMeasurement.
         */
        fun new(): UsageMeasurement = UsageMeasurement(
            duration = Duration.ZERO,
            hit = 0
        )
    }

    /**
     * Record a hit
     */
    fun hit(time: Duration) = UsageMeasurement(
        hit = hit + 1,
        duration = duration + time
    )
}