package com.github.sdp.ratemyepfl.model.time

import java.time.Month
import java.time.Year

object DayOfMonth {
    fun isValid(dayOfMonth: Int, month: Month, year: Int): Boolean =
        0 < dayOfMonth && dayOfMonth <= month.length(Year.of(year).isLeap)
}