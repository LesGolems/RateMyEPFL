package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.post.ReviewRating
import com.github.sdp.ratemyepfl.model.time.Period
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeEventRepository @Inject constructor(val repository: FakeLoaderRepository<Event>) :
    EventRepository, ReviewableRepository<Event>, LoaderRepository<Event> by repository {


    override val offlineData: List<Event> = listOf()

    companion object {
        private val baseEvent = Event(
            "name", "name", 0, 0, listOf(),
            "creator", 0.0, 0, 0.0, 0.0, Period.DEFAULT_PERIOD
        )
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


    override fun addEventWithId(event: Event): Flow<String> = repository.add(event)

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