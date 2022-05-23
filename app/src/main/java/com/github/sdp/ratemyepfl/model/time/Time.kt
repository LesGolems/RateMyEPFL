package com.github.sdp.ratemyepfl.model.time

import kotlinx.serialization.Serializable
import java.text.DecimalFormat

/**
 * A Serializable object that represents the time of a day. It is Firebase compatible as it defines
 * a default constructor.
 */
@Serializable
data class Time(val hours: Int = MIDNIGHT.hours, val minutes: Int = MIDNIGHT.minutes) :
    Comparable<Time> {
    init {
        checkHours(hours)
        checkMinutes(minutes)
    }

    override fun toString(): String = "${TIME_FORMAT.format(hours)}h${TIME_FORMAT.format(minutes)}"

    companion object {
        const val MIN_HOUR = 0
        const val MAX_HOUR = 24
        const val MIN_MINUTE = 0
        const val MAX_MINUTE = 60

        val TIME_FORMAT = DecimalFormat("00")

        /**
         * Check if the provided hours satisfies the bound constraints.
         *
         * @throws IllegalArgumentException: if the argument is not valid.
         */
        fun checkHours(hours: Int) {
            if (hours !in MIN_HOUR until MAX_HOUR) {
                throw IllegalArgumentException("Hours must be in [$MIN_HOUR; $MAX_HOUR)")
            }
        }

        /**
         * Check if the provided minutes satisfies the bound constraints.
         *
         * @throws IllegalArgumentException: if the argument is not valid.
         */
        fun checkMinutes(minutes: Int) {
            if (minutes !in MIN_MINUTE until MAX_MINUTE) {
                throw IllegalArgumentException("Minutes must be in [$MIN_MINUTE; $MAX_MINUTE)")
            }
        }

        val MIDNIGHT = Time(0, 0)
    }

    override fun compareTo(other: Time): Int =
        if (this == other) {
            0
        } else {
            val diffHours = this.hours.compareTo(other.hours)
            val diffMin = this.minutes.compareTo(other.minutes)

            when {
                diffHours != 0 -> diffHours
                diffMin != 0 -> diffMin
                else -> 0
            }
        }
}
