package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl.Companion.CREATOR_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl.Companion.ID_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl.Companion.NAME_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl.Companion.toEvent
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Event
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
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
    private val USER_ID = "Kevin du 13"
    private val testEvent = Event(
            "Fake id", "Fake id", 0,
        1, listOf(), "creator", 0.0,0.0, 0.0, LocalDateTime.now())

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var eventRepo: EventRepositoryImpl

    @Before
    fun setup() = runTest {
        hiltRule.inject()
        eventRepo.addEventWithId(testEvent).await()
    }

    @After
    fun clean() {
        runTest {
            eventRepo.remove(testEvent.getId()).await()
        }
    }

    @Test
    fun getEventsWorks() {
        runTest {
            val events = eventRepo.getEvents()
            assertEquals(events.size, 1)

            val event = events[0]
            assertEquals(testEvent.eventId, event.eventId)
            assertEquals(testEvent.name, event.name)
            assertEquals(testEvent.lat, event.lat, 0.1)
            assertEquals(testEvent.long, event.long, 0.1)
            assertEquals(testEvent.grade, event.grade, 0.1)
        }
    }

    @Test
    fun getEventByIdWorks() {
        runTest {
            val event = eventRepo.getEventById(testEvent.eventId)
            assertNotNull(event)
            assertEquals(testEvent.eventId, event!!.eventId)
            assertEquals(testEvent.name, event.name)
            assertEquals(testEvent.lat, event.lat, 0.1)
            assertEquals(testEvent.long, event.long, 0.1)
            assertEquals(testEvent.grade, event.grade, 0.1)
        }
    }

    @Test
    fun changeNumParticipantsWorks() {
        runTest {
            eventRepo.updateParticipants(testEvent.eventId, USER_ID)
            var event = eventRepo.getEventById(testEvent.eventId)
            assertNotNull(event)
            assertEquals(testEvent.eventId, event!!.eventId)
            assertEquals(testEvent.name, event.name)
            assertEquals(1, event.numParticipants)
            assert(event.participants.contains(USER_ID))

            eventRepo.updateParticipants(testEvent.eventId, USER_ID)
            event = eventRepo.getEventById(testEvent.eventId)
            assertNotNull(event)
            assertEquals(testEvent.eventId, event!!.eventId)
            assertEquals(testEvent.name, event.name)
            assertEquals(0, event.numParticipants)
            assert(!event.participants.contains(USER_ID))
        }
    }

    @Test
    fun editEventWorks() {
        runTest {
            eventRepo.updateEditedEvent(testEvent.eventId, "new name",
                10, 0.0, 0.0, LocalDateTime.now())
            val event = eventRepo.getEventById(testEvent.eventId)
            assertNotNull(event)
            assertEquals(testEvent.eventId, event!!.eventId)
            assertEquals("new name", event.name)
            assertEquals(10, event.limitParticipants)
        }
    }

    @Test
    fun returnsAnEventForCompleteSnapshot() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0
        val numParticipants = 0
        val limitParticipants = 0
        val g = 2.5
        val participants = listOf<String>()
        val date = LocalDateTime.now()

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(ID_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(NAME_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getField<Int>(NUM_REVIEWS_FIELD_NAME)).thenReturn(15)
        Mockito.`when`(snapshot.getDouble(AVERAGE_GRADE_FIELD_NAME)).thenReturn(g)
        Mockito.`when`(snapshot.getDouble(EventRepositoryImpl.LATITUDE_FIELD_NAME)).thenReturn(lat)
        Mockito.`when`(snapshot.getDouble(EventRepositoryImpl.LONGITUDE_FIELD_NAME))
            .thenReturn(long)
        Mockito.`when`(snapshot.getField<Int>(EventRepositoryImpl.NUMBER_PARTICIPANTS_FIELD_NAME))
            .thenReturn(numParticipants)
        Mockito.`when`(snapshot.getField<Int>(EventRepositoryImpl.LIMIT_PARTICIPANTS_FIELD_NAME))
            .thenReturn(limitParticipants)
        Mockito.`when`(snapshot.get(EventRepositoryImpl.PARTICIPANTS_FIELD_NAME))
            .thenReturn(participants)
        Mockito.`when`(snapshot.getString(CREATOR_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(EventRepositoryImpl.DATE_FIELD_NAME))
            .thenReturn(date.toString())

        val event = snapshot.toEvent()!!
        val expected = Event.Builder()
            .setId(fake)
            .name(fake)
            .setLat(lat)
            .setLong(long)
            .setNumParticipants(numParticipants)
            .setLimitParticipants(limitParticipants)
            .setParticipants(participants)
            .setDate(date)
            .setGrade(g)
            .setCreator(fake)
            .build()
        assertEquals(event.eventId, expected.eventId)
        assertEquals(event.name, expected.name)
        assertEquals(event.lat, expected.lat, 0.01)
        assertEquals(event.long, expected.long, 0.01)
        assertEquals(event.date, expected.date)
        assertEquals(event.numParticipants, expected.numParticipants)
        assertEquals(event.limitParticipants, expected.limitParticipants)
        assertEquals(event.participants, expected.participants)
        assertEquals(expected.grade, event.grade, 0.1)
        assertEquals(event.creator, expected.creator)
    }
}