package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating

interface EventRepositoryInterface {
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
    suspend fun incrementParticipants(id: String)


    /**
     *  Decrement the number of participants of given event
     */
    suspend fun decrementParticipants(id: String)

    /**
     *  Updates the rating of the event using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    suspend fun updateEventRating(id: String, rating: ReviewRating)
}