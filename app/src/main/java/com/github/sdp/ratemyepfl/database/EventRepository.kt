package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Event
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import javax.inject.Inject

class EventRepository @Inject constructor(db: FirebaseFirestore) :
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
}