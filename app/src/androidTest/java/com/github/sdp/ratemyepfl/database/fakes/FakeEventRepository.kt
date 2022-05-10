package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDateTime
import javax.inject.Inject

class FakeEventRepository @Inject constructor() : EventRepository {

    companion object {
        val DATE = LocalDateTime.now()
        val EVENT_LIST = listOf(
            Event(
                "",
                name = "Evenement de dingue", 0,
                100, listOf(), "", 0.0, 0.0, DATE
            ),
            Event(
                "",
                name = "Bas les masques", 0,
                70, listOf(), "", 0.0, 0.0, DATE
            ),
            Event(
                "",
                name = "La paix verte", 0,
                50, listOf(), "", 0.0, 0.0, DATE
            )
        )

        val DEFAULT_EVENT = Event(
            "", name = "Evenement de dingue", 0,
            100, listOf(), "", 0.0, 0.0, DATE
        )

        var eventById = DEFAULT_EVENT

        var rate: ReviewRating = ReviewRating.AVERAGE
    }

    override suspend fun getEvents(): List<Event> = EVENT_LIST

    override suspend fun getEventById(id: String): Event = eventById

    override suspend fun updateParticipants(eventId: String, userId: String): Boolean =
        true

    override suspend fun updateEditedEvent(
        eventId: String,
        name: String,
        limPart: Int,
        lat: Double,
        long: Double,
        date: LocalDateTime
    ) {
        val e = Event(eventId, name, 0, limPart, listOf(), "", lat, long, date)
    }
}