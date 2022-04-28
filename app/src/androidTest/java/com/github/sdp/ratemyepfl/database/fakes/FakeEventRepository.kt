package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.model.items.Event
import java.time.LocalDateTime
import javax.inject.Inject

class FakeEventRepository @Inject constructor() : EventRepository {

    companion object {
        val DATE = LocalDateTime.now()
        val EVENT_LIST = listOf(
            Event(name = "Evenement de dingue", 15, 2.5, 0,
                100, 0.0, 0.0, DATE),
            Event(name = "Bas les masques", 8, 3.0, 0,
                70, 0.0, 0.0, DATE),
            Event(name = "La paix verte", 1, 4.0, 0,
                50, 0.0, 0.0, DATE)
        )

        val DEFAULT_EVENT = Event(name = "Evenement de dingue", 15, 2.5, 0,
            100, 0.0, 0.0, DATE)

        val EVENT_WITH_REVIEWS = Event(name = "Evenement de dingue", 15, 2.5, 0,
            100, 0.0, 0.0, DATE)
        val EVENT_WITHOUT_REVIEWS = Event(name = "Evenement de dingue", 0, 0.0, 0,
            100, 0.0, 0.0, DATE)

        var eventById = EVENT_WITH_REVIEWS

        var participantsCounter = 0
    }

    override suspend fun getEvents(): List<Event> = EVENT_LIST

    override suspend fun getEventById(id: String): Event = eventById

    override suspend fun incrementParticipants(id: String) {
        participantsCounter += 1
    }

    override suspend fun decrementParticipants(id: String) {
        participantsCounter -= 1
    }
}