package com.github.sdp.ratemyepfl.model.time

/**
 * A class that represents the [Duration] of a short-term event. It may spans on several days.
 *
 * @see Time
 * @see Date
 *
 * @throws IllegalArgumentException: if one of [days], [hours] or [minutes] does not satisfy its
 * common bounds.
 */
data class Duration(
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0
) {

    /**
     * Constructs a [Duration] from an amount of minutes
     */
    constructor(minutes: Long) : this(
        (minutes / MINUTES_PER_DAY).toInt(),
        ((minutes / MINUTES_PER_HOUR) % HOURS_PER_DAY).toInt(),
        (minutes % MINUTES_PER_HOUR).toInt()
    )

    init {
        Time.checkMinutes(minutes)
        Time.checkHours(hours)
        if (days < 0) {
            throw IllegalArgumentException("A duration must have a positive number of days")
        }
    }

    infix operator fun plus(that: Duration): Duration {
        val minutes = this.minutes + that.minutes
        val remainingMinutes = minutes % Time.MAX_MINUTE

        val hours = this.hours + that.hours + (minutes - remainingMinutes) / Time.MAX_MINUTE
        val remainingHours = hours % Time.MAX_HOUR

        val days = this.days + that.days + (hours - remainingHours) / Time.MAX_HOUR

        return Duration(days, remainingHours, remainingMinutes)
    }

    companion object {
        const val HOURS_PER_DAY = 24
        const val MINUTES_PER_DAY = HOURS_PER_DAY * 60
        const val MINUTES_PER_HOUR = 60
    }
}