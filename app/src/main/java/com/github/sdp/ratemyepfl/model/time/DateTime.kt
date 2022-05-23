package com.github.sdp.ratemyepfl.model.time

import com.github.sdp.ratemyepfl.model.time.Date.Companion.DEFAULT_DATE
import com.github.sdp.ratemyepfl.model.time.Date.Companion.toDate
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * It represents a [Date] associated with a [Time] reference for the day.
 * A Serializable and Firebase compatible version of [LocalDateTime] with lower complexity.
 * It aims basic usages such as time referencing (e.g., the date of an event, ...), and does not
 * support many operations.
 *
 * To perform date manipulation, please convert it using [toLocalDateTime], compute, and then convert
 * it back with [LocalDateTime.toDateTime].
 *
 * @see LocalDateTime
 * @see Date
 * @see Time
 */
@Serializable
data class DateTime(val date: Date = DEFAULT_DATE, val time: Time = Time.MIDNIGHT): Comparable<DateTime> {

    constructor(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int) : this(
        Date(
            year,
            month,
            dayOfMonth
        ), Time(hour, minute)
    )

    infix fun plus(duration: Duration): DateTime =
        toLocalDateTime().plusMinutes(duration.minutes.toLong())
            .plusHours(duration.hours.toLong())
            .plusDays(duration.days.toLong())
            .toDateTime()

    override fun toString(): String = "$date $time"

    /**
     * Convert into [LocalDateTime].
     */
    fun toLocalDateTime(): LocalDateTime =
        LocalDateTime.of(date.year, date.month, date.dayOfMonth, time.hours, time.minutes)

    companion object {
        val DEFAULT_DATE_TIME = DateTime(DEFAULT_DATE, Time.MIDNIGHT)

        fun LocalDateTime.toDateTime(): DateTime =
            DateTime(toLocalDate().toDate(), Time(hour, minute))

        fun now(): DateTime =
            LocalDateTime.now().toDateTime()
    }

    override fun compareTo(other: DateTime): Int {
        val diffDate = date.compareTo(other.date)
        val diffTime = time.compareTo(other.time)

        return when {
            diffDate != 0 -> diffDate
            diffTime != 0 -> diffTime
            else -> 0
        }
    }
}