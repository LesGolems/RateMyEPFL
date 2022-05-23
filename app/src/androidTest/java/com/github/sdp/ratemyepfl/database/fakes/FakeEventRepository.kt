package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject

class FakeEventRepository(val repository: FakeLoaderRepository<Event>) :
    EventRepository, ReviewableRepository<Event>, LoaderRepository<Event> by repository {

    @Inject
    constructor() : this(FakeLoaderRepository(baseEvent))

    override val offlineData: List<Event> = listOf()

    companion object {
        val DATE = LocalDateTime.now()
        private val baseEvent = Event("name", "name", 0, 0, listOf(), "creator", 0.0, 0, 0.0, 0.0)
        val EVENT_LIST = listOf(
            baseEvent.copy(
                name = "Evenement de dingue",
                limitParticipants = 100,
                creator = "12345"
            ),
            baseEvent.copy(
                name = "Bas les masques",
                limitParticipants = 70,
            ),
            baseEvent.copy(
                name = "La paix verte",
                limitParticipants = 50,
            )
        )

        var eventList = EVENT_LIST

        val DEFAULT_EVENT = baseEvent.copy(
            name = "Evenement de dingue",
            limitParticipants = 100
        )

        val EVENT_NO_REVIEW = baseEvent.copy(
            grade = 0.0,
            numReviews = 0
        )

        val EVENT_WITH_REVIEW = baseEvent.copy(
            grade = 5.5,
            numReviews = 1
        )

        var eventById = DEFAULT_EVENT

        var rate: ReviewRating = ReviewRating.AVERAGE
    }

    override fun add(event: Event): Task<String> {
        return Mockito.mock(Task::class.java) as Task<String>
    }

    override fun addEventWithId(event: Event): Task<String> {
        return Mockito.mock(Task::class.java) as Task<String>
    }

    override suspend fun getEvents(): List<Event> = eventList

    override suspend fun getEventById(id: String): Event = eventById

    override suspend fun updateParticipants(eventId: String, userId: String): Boolean {
        eventList = EVENT_LIST
        eventList = if (eventList[0].participants.contains(userId)) {
            listOf(baseEvent.copy(participants = listOf()))
        } else {
            listOf(baseEvent.copy(participants = listOf(userId)))
        }
        return true
    }
}