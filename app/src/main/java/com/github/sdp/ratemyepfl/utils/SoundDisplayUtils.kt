package com.github.sdp.ratemyepfl.utils

import android.graphics.Color

object SoundDisplayUtils {

    val COLOR_ORANGE = Color.rgb(255, 165, 0)

    fun decibelMap(intensity: Int): Pair<String, Int> =
        if (intensity < 30) {
            Pair("Quiet", Color.CYAN)
        } else if (intensity < 60) {
            Pair("Calm", Color.GREEN)
        } else if (intensity < 90) {
            Pair("Loud", COLOR_ORANGE)
        } else {
            Pair("Painful", Color.RED)
        }
}