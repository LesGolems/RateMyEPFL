package com.github.sdp.ratemyepfl.utils

import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {

    // year-Month-day-Hour-minute-second-milliseconds
    private const val FILENAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"

    /**
     * Create a time-stamped name for an image
     */
    fun createImageId(): String =
        SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis())
}