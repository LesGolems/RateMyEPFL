package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.LoaderRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery.Companion.MAX_QUERY_LIMIT
import com.github.sdp.ratemyepfl.backend.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Event
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class EventRepositoryImpl private constructor(
    val repository: LoaderRepository<Event>,
) :
    EventRepository,
    ReviewableRepository<Event>,
    LoaderRepository<Event> by repository {

    override val offlineData: List<Event> = OFFLINE_EVENTS

    @Inject
    constructor(db: FirebaseFirestore) : this(
        LoaderRepositoryImpl<Event>(
            RepositoryImpl(db, EVENT_COLLECTION_PATH)
            { documentSnapshot ->
                documentSnapshot.toEvent()
            })
    )


    companion object {
        const val EVENT_COLLECTION_PATH = "events"

        private val OFFLINE_EVENTS = listOf<Event>()

        fun DocumentSnapshot.toEvent(): Event? = toItem()
    }

    /**
     * Add a [Event] with an auto-generated ID.
     *
     * @param item: the [Event] to add
     */
    override fun add(item: Event): Flow<String> {
        return repository.add(item.withId(item.hashCode().toString()))
    }

    override fun addEventWithId(event: Event) = repository.add(event)

    override suspend fun getEvents(): List<Event> = get(MAX_QUERY_LIMIT.toLong())
        .last()

    override suspend fun getEventById(id: String): Event = getById(id)
        .last()

    override suspend fun updateParticipants(eventId: String, userId: String): Boolean {
        var success = true
        repository.update(eventId) { event ->
            if (!event.participants.contains(userId)) {
                if (event.numParticipants == event.limitParticipants) {
                    success = false
                    event
                } else {
                    event.copy(
                        numParticipants = event.numParticipants + 1,
                        participants = event.participants.plus(userId)
                    )
                }
            } else {
                event.copy(
                    numParticipants = event.numParticipants - 1,
                    participants = event.participants.minus(userId)
                )
            }
        }.collect()
        return success
    }

}