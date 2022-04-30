package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction

interface EventRepository {
    /**
     * Retrieve the event from the repository
     *
     * @return a list of non-null events
     */
    suspend fun getEvents(): List<Event>

    /**
     * Retrieve an event from id.
     *
     * @return the event if found, otherwise null
     */
    suspend fun getEventById(id: String): Event?

    /**
     *  Increment the number of participants of given event
     */
    suspend fun updateParticipants(eventId: String, userId: String): Task<Transaction>

    /**
     *  Updates the rating of the event using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    suspend fun updateEventRating(id: String, rating: ReviewRating)
}