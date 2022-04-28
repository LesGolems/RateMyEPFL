package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.EventRepository
import com.github.sdp.ratemyepfl.database.EventRepository.Companion.NAME_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
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
        NAME_FIELD_NAME to EXPECTED_EVENT.name,
        EventRepository.NUMBER_PARTICIPANTS_FIELD_NAME to EXPECTED_EVENT.numParticipants,
        EventRepository.LIMIT_PARTICIPANTS_FIELD_NAME to EXPECTED_EVENT.limitParticipants,
        EventRepository.LATITUDE_FIELD_NAME to EXPECTED_EVENT.lat,
        EventRepository.LONGITUDE_FIELD_NAME to EXPECTED_EVENT.long,
        EventRepository.DATE_FIELD_NAME to DATE.toString(),
        ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME to EXPECTED_EVENT.numReviews,
        ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME to EXPECTED_EVENT.averageGrade
    )

    @Test
    fun defaultConstructorWorks() {
        val e = EXPECTED_EVENT
        assertEquals(ID, e.name)
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
            .name(fake)
            .setNumReviews(10)
            .setAverageGrade(4.0)
            .setLat(lat)
            .setLong(long)
            .setDate(DATE)
            .setLimitParticipants(70)

        val event = builder.build()
        val expected = Event(fake, 10, 4.0, 0, 70, lat, long, DATE)
        assertEquals(event.name, expected.name)
        assertEquals(event.numReviews, expected.numReviews)
        assertEquals(event.averageGrade, expected.averageGrade, 0.01)
        assertEquals(event.lat, expected.lat, 0.01)
        assertEquals(event.long, expected.long, 0.01)
        assertEquals(event.date, expected.date)
        assertEquals(event.numParticipants, expected.numParticipants)
        assertEquals(event.limitParticipants, expected.limitParticipants)
    }
}