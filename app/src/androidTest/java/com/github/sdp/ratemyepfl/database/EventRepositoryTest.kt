package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.EventRepository.Companion.toEvent
import com.github.sdp.ratemyepfl.model.items.Event
import com.google.firebase.firestore.DocumentSnapshot
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
    lateinit var eventRepo: EventRepository

    @Before
    fun setup() {
        hiltRule.inject()
        eventRepo.add(testEvent)
    }

    @After
    fun clean(){
        eventRepo.remove(testEvent.id)
    }

    @Test
    fun getEventsWorks() {
        runTest {
            val events = eventRepo.getEvents()
            assertEquals(events.size, 1)

            val event = events[0]
            assertEquals(testEvent.id, event.id)
            assertEquals(testEvent.lat, event.lat, 0.1)
            assertEquals(testEvent.long, event.long, 0.1)
        }
    }

    @Test
    fun getEventByIdWorks() {
        runTest {
            val event = eventRepo.getEventById(testEvent.id)
            assertNotNull(event)
            assertEquals(testEvent.id, event!!.id)
            assertEquals(testEvent.lat, event.lat, 0.1)
            assertEquals(testEvent.long, event.long, 0.1)
        }
    }

    @Test
    fun changeNumParticipantsWorks() {
        runTest {
            eventRepo.incrementParticipants(testEvent.id)
            var event = eventRepo.getEventById(testEvent.id)
            assertNotNull(event)
            assertEquals(testEvent.id, event!!.id)
            assertEquals(2, event.numParticipants)

            eventRepo.decrementParticipants(testEvent.id)
            event = eventRepo.getEventById(testEvent.id)
            assertNotNull(event)
            assertEquals(testEvent.id, event!!.id)
            assertEquals(1, event.numParticipants)
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
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD_NAME)).thenReturn("15")
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD_NAME)).thenReturn("2.5")
        Mockito.`when`(snapshot.getString(EventRepository.LATITUDE_FIELD_NAME)).thenReturn(lat.toString())
        Mockito.`when`(snapshot.getString(EventRepository.LONGITUDE_FIELD_NAME)).thenReturn(long.toString())
        Mockito.`when`(snapshot.getString(EventRepository.NUMBER_PARTICIPANTS_FIELD_NAME)).thenReturn(numParticipants.toString())
        Mockito.`when`(snapshot.getString(EventRepository.LIMIT_PARTICIPANTS_FIELD_NAME)).thenReturn(limitParticipants.toString())
        Mockito.`when`(snapshot.getString(EventRepository.DATE_FIELD_NAME)).thenReturn(date.toString())

        val event = snapshot.toEvent()!!
        val expected = Event.Builder()
            .setId(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)
            .setLat(lat)
            .setLong(long)
            .setNumParticipants(numParticipants)
            .setLimitParticipants(limitParticipants)
            .setDate(date)
            .build()
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