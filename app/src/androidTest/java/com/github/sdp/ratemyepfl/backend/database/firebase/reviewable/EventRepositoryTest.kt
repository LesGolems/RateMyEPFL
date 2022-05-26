package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.time.Period
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class EventRepositoryTest {

    companion object {
        private const val USER_ID = "Kevin du 13"
    }

    private val testEvent = Event(
        "Fake id", "Fake id", 0,
        1, listOf(), "creator", 0.0, 0, 0.0, 0.0, Period.DEFAULT_PERIOD
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var eventRepo: EventRepositoryImpl

    @Before
    fun setup() = runTest {
        hiltRule.inject()
        eventRepo.addEventWithId(testEvent).last()
    }

    @After
    fun clean() {
        runTest {
            eventRepo.remove(testEvent.getId()).last()
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
            assertEquals(testEvent.numReviews, event.numReviews)
        }
    }

    @Test
    fun getEventByIdWorks() {
        runTest {
            val event = eventRepo.getEventById(testEvent.eventId)
            assertNotNull(event)
            assertEquals(testEvent.eventId, event.eventId)
            assertEquals(testEvent.name, event.name)
            assertEquals(testEvent.lat, event.lat, 0.1)
            assertEquals(testEvent.long, event.long, 0.1)
            assertEquals(testEvent.grade, event.grade, 0.1)
            assertEquals(testEvent.numReviews, event.numReviews)
        }
    }

    @Test
    fun changeNumParticipantsWorks() {
        runTest {
            eventRepo.updateParticipants(testEvent.eventId, USER_ID)
            var event = eventRepo.getEventById(testEvent.eventId)
            assertNotNull(event)
            assertEquals(testEvent.eventId, event.eventId)
            assertEquals(testEvent.name, event.name)
            assertEquals(1, event.numParticipants)
            assert(event.participants.contains(USER_ID))

            eventRepo.updateParticipants(testEvent.eventId, USER_ID)
            event = eventRepo.getEventById(testEvent.eventId)
            assertNotNull(event)
            assertEquals(testEvent.eventId, event.eventId)
            assertEquals(testEvent.name, event.name)
            assertEquals(0, event.numParticipants)
            assert(!event.participants.contains(USER_ID))
        }
    }
}