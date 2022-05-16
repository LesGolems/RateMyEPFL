package com.github.sdp.ratemyepfl.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


object TimeUtils {

    private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    private val EPFL_ZONE_ID = ZoneId.of("Europe/Zurich")
    private val EPFL_TIME_ZONE = TimeZone.getTimeZone(EPFL_ZONE_ID)

    /**
     * Create a time-stamped name for an image
     */
    fun timeStamp(): String =
        now().toString()

    /**
     * Return the current time in the format yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    fun now(): LocalDateTime =
        LocalDateTime.now(EPFL_ZONE_ID)

    /**
     * Describes the elapsed time since [date] formatted with formatted like with "ago".
     * Ex: 30 minutes ago, 1 hour ago
     */
    fun prettyTime(date: String): CharSequence? {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        sdf.timeZone = EPFL_TIME_ZONE
        val time = sdf.parse(date)!!.time

        val now = System.currentTimeMillis()

        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
    }
}