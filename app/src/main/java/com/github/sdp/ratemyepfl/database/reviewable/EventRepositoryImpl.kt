package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.LoaderRepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Event
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
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
        const val NAME_FIELD_NAME: String = "name"
        const val EVENT_COLLECTION_PATH = "events"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"
        const val NUMBER_PARTICIPANTS_FIELD_NAME = "numParticipants"
        const val LIMIT_PARTICIPANTS_FIELD_NAME = "limitParticipants"
        const val PARTICIPANTS_FIELD_NAME = "participants"
        const val DATE_FIELD_NAME = "date"

        private val OFFLINE_EVENTS = listOf<Event>()
        fun DocumentSnapshot.toEvent(): Event? {
            val name = getString(NAME_FIELD_NAME)
            val lat = getDouble(LATITUDE_FIELD_NAME)
            val long = getDouble(LONGITUDE_FIELD_NAME)
            val numParticipants = getField<Int>(NUMBER_PARTICIPANTS_FIELD_NAME)
            val limitParticipants = getField<Int>(LIMIT_PARTICIPANTS_FIELD_NAME)
            val participants = get(PARTICIPANTS_FIELD_NAME) as List<String>
            val date = LocalDateTime.parse(getString(DATE_FIELD_NAME))
            val grade = getDouble(AVERAGE_GRADE_FIELD_NAME)
            return try {
                Event.Builder(
                    name,
                    numParticipants,
                    limitParticipants,
                    participants,
                    grade,
                    lat,
                    long,
                    date
                )
                    .build()
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    override suspend fun getEvents(): List<Event> = take(DEFAULT_QUERY_LIMIT.toLong())
        .mapNotNull { it.toEvent() }

    override suspend fun getEventById(id: String): Event? = getById(id).toEvent()

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
        }.await()
        return success
    }

}