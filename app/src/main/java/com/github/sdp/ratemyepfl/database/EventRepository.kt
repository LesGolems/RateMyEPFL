package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Event
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class EventRepository(val repository: RepositoryImpl<Event>) :
    EventRepositoryInterface,
    Repository<Event> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl(db, EVENT_COLLECTION_PATH))

    private val collection = repository.collection
    private val db = repository.database

    companion object {
        const val NAME_FIELD_NAME: String = "name"
        const val EVENT_COLLECTION_PATH = "events"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"
        const val NUMBER_PARTICIPANTS_FIELD_NAME = "numParticipants"
        const val LIMIT_PARTICIPANTS_FIELD_NAME = "limitParticipants"
        const val DATE_FIELD_NAME = "date"

        fun DocumentSnapshot.toEvent(): Event? {
            val name = getString(NAME_FIELD_NAME)
            val lat = getDouble(LATITUDE_FIELD_NAME)
            val long = getDouble(LONGITUDE_FIELD_NAME)
            val numReviews = getField<Int>(NUM_REVIEWS_FIELD_NAME)
            val averageGrade = getDouble(AVERAGE_GRADE_FIELD_NAME)
            val numParticipants = getField<Int>(NUMBER_PARTICIPANTS_FIELD_NAME)
            val limitParticipants = getField<Int>(LIMIT_PARTICIPANTS_FIELD_NAME)
            val date = LocalDateTime.parse(getString(DATE_FIELD_NAME))
            return try {
                Event.Builder(
                    name,
                    numReviews,
                    averageGrade,
                    numParticipants,
                    limitParticipants,
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

    override suspend fun incrementParticipants(id: String) {
        coroutineScope {
            this.launch(Dispatchers.IO) {
                changeParticipants(id, 1).await()
            }
        }
    }

    override suspend fun decrementParticipants(id: String) {
        coroutineScope {
            this.launch(Dispatchers.IO) {
                changeParticipants(id, -1).await()
            }
        }
    }

    private fun changeParticipants(id: String, incDec: Int): Task<Transaction> {
        val docRef = collection.document(id)
        return db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            snapshot.getField<Int>(NUMBER_PARTICIPANTS_FIELD_NAME)
                ?.let { number ->
                    transaction.update(
                        docRef,
                        NUMBER_PARTICIPANTS_FIELD_NAME,
                        (number + incDec)
                    )
                }
        }
    }

}