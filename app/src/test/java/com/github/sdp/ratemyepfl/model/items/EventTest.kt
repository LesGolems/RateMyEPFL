package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl.Companion.NAME_FIELD_NAME
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDateTime

class EventTest {
    private val ID = "Evenement de dingue"
    private val USER_ID = "Kevin du 13"
    private val SHOW_PARTICIPANTS = "Participants: 64/70"
    private val DATE = LocalDateTime.now()
    private val EXPECTED_EVENT = Event(
        ID, ID,
        64, 70,
        listOf(USER_ID), USER_ID,
        46.52, 6.569, DATE
    )
    private val EXPECTED_HASH_MAP = hashMapOf(
        EventRepositoryImpl.ID_FIELD_NAME to EXPECTED_EVENT.eventId,
        NAME_FIELD_NAME to EXPECTED_EVENT.name,
        EventRepositoryImpl.NUMBER_PARTICIPANTS_FIELD_NAME to EXPECTED_EVENT.numParticipants,
        EventRepositoryImpl.LIMIT_PARTICIPANTS_FIELD_NAME to EXPECTED_EVENT.limitParticipants,
        EventRepositoryImpl.PARTICIPANTS_FIELD_NAME to EXPECTED_EVENT.participants,
        EventRepositoryImpl.CREATOR_FIELD_NAME to EXPECTED_EVENT.creator,
        EventRepositoryImpl.LATITUDE_FIELD_NAME to EXPECTED_EVENT.lat,
        EventRepositoryImpl.LONGITUDE_FIELD_NAME to EXPECTED_EVENT.long,
        EventRepositoryImpl.DATE_FIELD_NAME to DATE.toString()
    )

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
            .name(null)

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
            .name(fake)
            .setLat(lat)
            .setLong(long)
            .setDate(DATE)
            .setLimitParticipants(70)
            .setCreator(USER_ID)

        val event = builder.build()
        val expected = Event(fake, fake, 0, 70,
            listOf(), USER_ID, lat, long, DATE)

        assertEquals(event.eventId, expected.eventId)
        assertEquals(event.name, expected.name)
        assertEquals(event.lat, expected.lat, 0.01)
        assertEquals(event.long, expected.long, 0.01)
        assertEquals(event.date, expected.date)
        assertEquals(event.numParticipants, expected.numParticipants)
        assertEquals(event.limitParticipants, expected.limitParticipants)
        assertEquals(event.participants, expected.participants)
        assertEquals(event.creator, expected.creator)
    }
}