package com.github.sdp.ratemyepfl.backend.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository : ReviewableRepository<Event> {

    /**
     * Add an event which already has an id to the database
     */
    fun addEventWithId(event: Event): Flow<String>

    /**
     * Retrieve the event from the repository
     *
     * @return a list of non-null events
     */
    suspend fun getEvents(): List<Event>

    /**
     * Retrieve an event from id.
     *
     * @return the event if found
     *
     * @throws NoSuchElementException if no element with the provided id exists
     */
    suspend fun getEventById(id: String): Event

    /**
     *  Update the number of participants of given event if possible, and returns
     *  either this function has succeeded or not
     */
    suspend fun updateParticipants(eventId: String, userId: String): Boolean
}