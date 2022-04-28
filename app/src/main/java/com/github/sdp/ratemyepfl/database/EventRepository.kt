package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class EventRepository @Inject constructor(db: FirebaseFirestore) :
    EventRepositoryInterface,
    Repository(db, EVENT_COLLECTION_PATH) {

    companion object {
        const val EVENT_COLLECTION_PATH = "events"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"
        const val NUMBER_PARTICIPANTS_FIELD_NAME = "numParticipants"
        const val LIMIT_PARTICIPANTS_FIELD_NAME = "limitParticipants"
        const val DATE_FIELD_NAME = "date"

        fun DocumentSnapshot.toEvent(): Event? {
            val lat = getString(LATITUDE_FIELD_NAME)?.toDouble() ?: 0.0
            val long = getString(LONGITUDE_FIELD_NAME)?.toDouble() ?: 0.0
            val numReviews = getString(NUM_REVIEWS_FIELD_NAME)?.toInt() ?: 0
            val averageGrade = getString(AVERAGE_GRADE_FIELD_NAME)?.toDouble() ?: 0.0
            val numParticipants = getString(NUMBER_PARTICIPANTS_FIELD_NAME)?.toInt() ?: 0
            val limitParticipants = getString(LIMIT_PARTICIPANTS_FIELD_NAME)?.toInt() ?: 0
            val date = LocalDateTime.parse(getString(DATE_FIELD_NAME))
            return Event(id, numReviews, averageGrade, numParticipants, limitParticipants, lat, long, date)
        }
    }

    fun toItem(snapshot: DocumentSnapshot): Event? = snapshot.toEvent()

    override suspend fun getEvents(): List<Event> = take(DEFAULT_LIMIT).mapNotNull { obj -> toItem(obj) }

    override suspend fun getEventById(id: String): Event? = toItem(getById(id))

    override suspend fun updateEventRating(id: String, rating: ReviewRating) = updateRating(id, rating)

    override suspend fun incrementParticipants(id: String) {
        changeParticipants(id, 1)
    }

    override suspend fun decrementParticipants(id: String) {
        changeParticipants(id, -1)
    }

    private suspend fun changeParticipants(id: String, incDec: Int) {
        val docRef = collection.document(id)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val numParticipants = snapshot.getString(NUMBER_PARTICIPANTS_FIELD_NAME)?.toInt()
            if (numParticipants != null) {
                transaction.update(docRef, NUMBER_PARTICIPANTS_FIELD_NAME, (numParticipants + incDec).toString())
            }
            null
        }.await()
    }

    fun add(event: Event) {
        collection.document(event.id).set(event.toHashMap())
    }
}