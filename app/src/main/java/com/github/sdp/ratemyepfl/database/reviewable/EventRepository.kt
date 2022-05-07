package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating

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
     *  Update the number of participants of given event if possible, and returns
     *  weither this function has succeeded or not
     */
    suspend fun updateParticipants(eventId: String, userId: String): Boolean

    /**
     *  Updates the rating of the event using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    suspend fun updateEventRating(id: String, rating: ReviewRating)
}