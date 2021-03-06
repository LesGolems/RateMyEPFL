package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.model.time.Period
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class EventTest {

    companion object {
        private const val ID = "Evenement de dingue"
        private const val USER_ID = "Kevin du 13"
        private const val SHOW_PARTICIPANTS = "Participants: 64/70"
        private val DATE = Period.DEFAULT_PERIOD
        private val EXPECTED_EVENT = Event(
            ID, ID,
            64, 70, listOf(USER_ID), USER_ID, 0.0, 0, 46.52, 6.569, DATE
        )
    }

    @Test
    fun defaultConstructorWorks() {
        val e = EXPECTED_EVENT
        assertEquals(ID, e.eventId)
        assertEquals(ID, e.name)
        assertEquals(64, e.numParticipants)
        assertEquals(70, e.limitParticipants)
        assertEquals(listOf(USER_ID), e.participants)
        assertEquals(USER_ID, e.creator)
        assertEquals(46.52, e.lat, 0.01)
        assertEquals(6.569, e.long, 0.01)
    }

    @Test
    fun showParticipationWorks() {
        assertEquals(EXPECTED_EVENT.showParticipation(), SHOW_PARTICIPANTS)
    }

    @Test
    fun withIdWorks() {
        val e = EXPECTED_EVENT.withId("Team JUL")
        assertEquals("Team JUL", e.eventId)
    }

    @Test
    fun toStringWorks() {
        assertEquals(EXPECTED_EVENT.toString(), ID)
    }


    @Test
    fun builderThrowsForMissingId() {
        val builder = Event.Builder()
            .name(null)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSucceedsForMissingNonMandatoryProperties() {
        val fake = "fake"
        val part = listOf("Jean mi du 13")
        val lat = 0.0
        val long = 0.0
        val g = 1.0
        val n = 1
        val builder = Event.Builder()
            .setId(fake)
            .name(fake)
            .setCreator(USER_ID)
            .setLat(lat)
            .setLong(long)
            .setPeriod(DATE)
            .setGrade(g)
            .setNumReviews(n)
            .setNumParticipants(0)
            .setLimitParticipants(70)
            .setParticipants(part)

        val event = builder.build()
        val expected = Event(fake, fake, 0, 70, part, USER_ID, g, n, lat, long, DATE)
        assertEquals(event.name, expected.name)

        assertEquals(event.eventId, expected.eventId)
        assertEquals(event.name, expected.name)
        assertEquals(event.lat, expected.lat, 0.01)
        assertEquals(event.long, expected.long, 0.01)
        assertEquals(event.period, expected.period)
        assertEquals(event.numParticipants, expected.numParticipants)
        assertEquals(event.limitParticipants, expected.limitParticipants)
        assertEquals(event.participants, expected.participants)
        assertEquals(event.grade, expected.grade, 0.01)
        assertEquals(event.numReviews, expected.numReviews)
    }

}