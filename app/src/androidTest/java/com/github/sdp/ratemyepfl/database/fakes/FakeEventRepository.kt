package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject

class FakeEventRepository @Inject constructor(val repository: FakeLoaderRepository<Event>) :
    EventRepository, ReviewableRepository<Event>, LoaderRepository<Event> by repository {

    override val offlineData: List<Event> = listOf()
    companion object {
        val DATE = LocalDateTime.now()
        private val baseEvent =
            Event("name", "name", 0, 0, listOf(), "creator", 0.0, 0.0, 0.0, DATE)
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

        var eventById = DEFAULT_EVENT

        var rate: ReviewRating = ReviewRating.AVERAGE
    }

    override fun add(event: Event): Task<Void> {
        return Mockito.mock(Task::class.java) as Task<Void>
    }

    override fun addEventWithId(event: Event): Task<Void> {
        return Mockito.mock(Task::class.java) as Task<Void>
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
        val e = baseEvent
    }
}