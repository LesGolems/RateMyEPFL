package com.github.sdp.ratemyepfl.utils

import android.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class SoundDisplayUtilsTest {

    @Test
    fun quietIsCorrectlyMapped() {
        val pair = SoundDisplayUtils.decibelMap(29)
        assertEquals("Quiet", pair.first)
        assertEquals(Color.CYAN, pair.second)
    }

    @Test
    fun calmIsCorrectlyMapped() {
        val pair = SoundDisplayUtils.decibelMap(59)
        assertEquals("Calm", pair.first)
        assertEquals(Color.GREEN, pair.second)
    }

    @Test
    fun loudIsCorrectlyMapped() {
        val pair = SoundDisplayUtils.decibelMap(89)
        assertEquals("Loud", pair.first)
        assertEquals(Color.YELLOW, pair.second)
    }

    @Test
    fun painfulIsCorrectlyMapped() {
        val pair = SoundDisplayUtils.decibelMap(90)
        assertEquals("Painful", pair.first)
        assertEquals(Color.RED, pair.second)
    }


}