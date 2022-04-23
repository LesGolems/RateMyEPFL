package com.github.sdp.ratemyepfl.model.items

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDateTime

class EventTest {
    private val ID = "Evenement de dingue"
    private val DATE = LocalDateTime.now()
    private val EXPECTED_EVENT = Event(ID, 10, 4.0,
        64, 70, 46.52, 6.569, DATE)
    private val EXPECTED_HASH_MAP = hashMapOf(
        "numParticipants" to "64",
        "limitParticipants" to "70",
        "lat" to "46.52",
        "long" to "6.569",
        "date" to DATE.toString(),
        "numReviews" to "10",
        "averageGrade" to "4.0"
    )

    @Test
    fun defaultConstructorWorks() {
        val e = EXPECTED_EVENT
        assertEquals(ID, e.id)
        assertEquals(10, e.numReviews)
        assertEquals(4.0, e.averageGrade, 0.01)
        assertEquals(64, e.numParticipants)
        assertEquals(70, e.limitParticipants)
        assertEquals(46.52, e.lat, 0.01)
        assertEquals(6.569, e.long, 0.01)
    }

    @Test
    fun toStringWorks() {
        assertEquals(EXPECTED_EVENT.toString(), ID)
    }

    @Test
    fun toHashMapWorks() {
        assertEquals(EXPECTED_EVENT.toHashMap(), EXPECTED_HASH_MAP)
    }

    @Test
    fun builderThrowsForMissingId() {
        val builder = Event.Builder()
            .setNumReviews(10)
            .setAverageGrade(4.0)
            .setId(null)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSucceedsForMissingNonMandatoryProperties() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0
        val builder = Event.Builder()
            .setId(fake)
            .setNumReviews(10)
            .setAverageGrade(4.0)
            .setLat(lat)
            .setLong(long)
            .setDate(DATE)
            .setLimitParticipants(70)

        val event = builder.build()
        val expected = Event(fake, 10, 4.0, 0, 70, lat, long, DATE)
        assertEquals(event.id, expected.id)
        assertEquals(event.numReviews, expected.numReviews)
        assertEquals(event.averageGrade, expected.averageGrade, 0.01)
        assertEquals(event.lat, expected.lat, 0.01)
        assertEquals(event.long, expected.long, 0.01)
        assertEquals(event.date, expected.date)
        assertEquals(event.numParticipants, expected.numParticipants)
        assertEquals(event.limitParticipants, expected.limitParticipants)
    }
}