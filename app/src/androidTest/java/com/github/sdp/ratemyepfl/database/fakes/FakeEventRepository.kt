package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDateTime
import javax.inject.Inject

class FakeEventRepository @Inject constructor() : EventRepository {

    companion object {
        val DATE = LocalDateTime.now()
        private val baseEvent =
            Event("name", 0, 0, listOf(), 0.0, 0,0.0,0.0, DATE)
        val EVENT_LIST = listOf(
            baseEvent.copy(
                name = "Evenement de dingue",
                limitParticipants = 100,
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

    override suspend fun getEvents(): List<Event> = EVENT_LIST

    override suspend fun getEventById(id: String): Event = eventById

    override suspend fun updateParticipants(eventId: String, userId: String): Boolean =
        true
}