package com.github.sdp.ratemyepfl.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RoomNoiseInfoTest {

    @Test
    fun constructorWithAllFieldsWorks() {
        val roomNoiseInfo = RoomNoiseInfo("id", listOf(NoiseInfo("2022-05-10T18:45:18.640", 38)))
        assertEquals("id", roomNoiseInfo.roomId)
        assertEquals(38, roomNoiseInfo.noiseData[0].measure)
    }

    @Test
    fun builderWorks() {
        val builder = RoomNoiseInfo.Builder("id")
        val roomNoiseInfo = builder.build()
        assertEquals("id", roomNoiseInfo.roomId)
    }

    @Test
    fun builderThrowsForMissingId() {
        val builder = RoomNoiseInfo.Builder(null)
        assertThrows(IllegalStateException::class.java) { builder.build() }
    }

}