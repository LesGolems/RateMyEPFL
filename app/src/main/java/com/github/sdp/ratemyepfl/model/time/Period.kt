package com.github.sdp.ratemyepfl.model.time

import kotlinx.serialization.Serializable


/**
 * A Serializable and Firebase compatible representation of a time slot. It represents a period of
 * time between two [DateTime], or equivalently a period of time that starts at a given [DateTime]
 * and lasts for a given [Duration].
 */
@Serializable
data class Period(
    val start: DateTime = DateTime.DEFAULT_DATE_TIME,
    val end: DateTime = DateTime.DEFAULT_DATE_TIME
) {

    constructor(start: DateTime, duration: Duration) : this(
        start,
        start.plus(duration)
    )

    init {
        checkPeriod(this)
    }

    companion object {
        val DEFAULT_PERIOD: Period = Period(DateTime.DEFAULT_DATE_TIME, DateTime.DEFAULT_DATE_TIME)
        fun checkPeriod(period: Period) {
            if (period.start > period.end) {
                throw IllegalArgumentException("The period should start before ending")
            }
        }
    }

}