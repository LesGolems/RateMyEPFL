package com.github.sdp.ratemyepfl.model.time

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.Month

/**
 * A Serializable and Firebase compatible [LocalDate]. It represents a date with a year, a month and
 * a day of month.
 *
 * Most of [LocalDate] operations are not supported. To perform those, convert this into [LocalDate],
 * transform it, and convert it back into [Date] with [LocalDate.toDate].
 *
 * @throws IllegalArgumentException: if the provided [year], [month] and [dayOfMonth] are not valid.
 * (e.g., 2022-13-01 or 2022-1-32 is not a valid date)
 *
 * @see LocalDate
 * @see DateTime
 */
@Serializable
data class Date(
    val year: Int = DEFAULT_DATE.year,
    val month: Month = DEFAULT_DATE.month,
    val dayOfMonth: Int = DEFAULT_DATE.dayOfMonth
): Comparable<Date> {

    constructor(year: Int, month: Int, day: Int) : this(year, Month.of(month), day)

    /**
     * Compute the date that corresponds to the current date plus a given number of days.
     *
     * @param days: The number of days to add
     */
    infix fun plus(days: Long): Date =
        toLocalDate().plusDays(days).toDate()

    init {
        if (!DayOfMonth.isValid(dayOfMonth, month, year)) {
            throw IllegalArgumentException("Invalid day for the date $this")
        }
    }

    override fun toString(): String = "$year-$month-$dayOfMonth"

    fun toLocalDate(): LocalDate = LocalDate.of(year, month, dayOfMonth)
    
    companion object {
        val DEFAULT_DATE = Date(2022, 1, 1)
        fun LocalDate.toDate(): Date = Date(year, month, dayOfMonth)

        /**
         * Provide the current [Date].
         */
        fun now(): Date = LocalDate
            .now()
            .toDate()
    }

    override infix fun compareTo(other: Date): Int {
        return if (this == other) {
            0
        } else {
            val diffYears = year.compareTo(other.year)
            val diffMonths = month.compareTo(other.month)
            val diffDays = dayOfMonth.compareTo(other.dayOfMonth)
            when {
                diffYears != 0 -> {
                    diffYears
                }
                diffMonths != 0 -> {
                    diffMonths
                }
                diffDays != 0 -> {
                    diffDays
                }
                else -> 0
            }
        }
    }
}
