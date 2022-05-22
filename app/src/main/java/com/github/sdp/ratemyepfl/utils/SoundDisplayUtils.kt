package com.github.sdp.ratemyepfl.utils

import android.graphics.Color
import com.github.sdp.ratemyepfl.R

object SoundDisplayUtils {

    val COLOR_ORANGE = Color.rgb(255, 165, 0)

    fun decibelMap(intensity: Int): Triple<String, Int, Int> =
        if (intensity < 30) {
            Triple("Quiet", Color.CYAN, R.raw.emoji_happy)
        } else if (intensity < 60) {
            Triple("Calm", Color.GREEN, R.raw.emoji_normal)
        } else if (intensity < 90) {
            Triple("Loud", COLOR_ORANGE, R.raw.emoji_angry)
        } else {
            Triple("Painful", Color.RED, R.raw.emoji_head_blowing)
        }
}