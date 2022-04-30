package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl.Companion.NAME_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl.Companion.toEvent
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
class EventRepositoryTest {
    private val testEvent = Event(
        "Fake id", 0, 0.0, 0,
        0, 0.0, 0.0, LocalDateTime.now()
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var eventRepo: EventRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        eventRepo.add(testEvent)
    }

    @After
    fun clean() {
        eventRepo.remove(testEvent.name)
    }

    @Test
    fun getEventsWorks() {
        runTest {
            val events = eventRepo.getEvents()
            assertEquals(events.size, 1)

            val event = events[0]
            assertEquals(testEvent.name, event.name)
            assertEquals(testEvent.lat, event.lat, 0.1)
            assertEquals(testEvent.long, event.long, 0.1)
        }
    }

    @Test
    fun getEventByIdWorks() {
        runTest {
            val event = eventRepo.getEventById(testEvent.name)
            assertNotNull(event)
            assertEquals(testEvent.name, event!!.name)
            assertEquals(testEvent.lat, event.lat, 0.1)
            assertEquals(testEvent.long, event.long, 0.1)
        }
    }

    @Test
    fun changeNumParticipantsWorks() {
        runTest {
            eventRepo.incrementParticipants(testEvent.name)
            var event = eventRepo.getEventById(testEvent.name)
            assertNotNull(event)
            assertEquals(testEvent.name, event!!.name)
            assertEquals(1, event.numParticipants)

            eventRepo.decrementParticipants(testEvent.name)
            event = eventRepo.getEventById(testEvent.name)
            assertNotNull(event)
            assertEquals(testEvent.name, event!!.name)
            assertEquals(0, event.numParticipants)
        }
    }

    @Test
    fun updateEventRatingWorks() {
        runTest {
            eventRepo.updateEventRating(testEvent.getId(), ReviewRating.EXCELLENT)
            val event = eventRepo.getEventById(testEvent.getId())
            assertNotNull(event)
            assertEquals(testEvent.getId(), event!!.getId())
            assertEquals(1, event.numReviews)
            assertEquals(5.0, event.averageGrade, 0.1)
        }
    }

    @Test
    fun returnsAnEventForCompleteSnapshot() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0
        val numParticipants = 0
        val limitParticipants = 0
        val date = LocalDateTime.now()

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(NAME_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getField<Int>(NUM_REVIEWS_FIELD_NAME)).thenReturn(15)
        Mockito.`when`(snapshot.getDouble(AVERAGE_GRADE_FIELD_NAME)).thenReturn(2.5)
        Mockito.`when`(snapshot.getDouble(EventRepositoryImpl.LATITUDE_FIELD_NAME)).thenReturn(lat)
        Mockito.`when`(snapshot.getDouble(EventRepositoryImpl.LONGITUDE_FIELD_NAME))
            .thenReturn(long)
        Mockito.`when`(snapshot.getField<Int>(EventRepositoryImpl.NUMBER_PARTICIPANTS_FIELD_NAME))
            .thenReturn(numParticipants)
        Mockito.`when`(snapshot.getField<Int>(EventRepositoryImpl.LIMIT_PARTICIPANTS_FIELD_NAME))
            .thenReturn(limitParticipants)
        Mockito.`when`(snapshot.getString(EventRepositoryImpl.DATE_FIELD_NAME))
            .thenReturn(date.toString())

        val event = snapshot.toEvent()!!
        val expected = Event.Builder()
            .name(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)
            .setLat(lat)
            .setLong(long)
            .setNumParticipants(numParticipants)
            .setLimitParticipants(limitParticipants)
            .setDate(date)
            .build()
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