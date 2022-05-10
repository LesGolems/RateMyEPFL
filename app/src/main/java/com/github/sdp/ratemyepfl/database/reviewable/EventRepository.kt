package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Event
import com.google.android.gms.tasks.Task
import java.time.LocalDateTime

interface EventRepository {
    /**
     * Add an event which already has an id to the database
     */
    fun addEventWithId(event: Event): Task<Void>

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
     *  either this function has succeeded or not
     */
    suspend fun updateParticipants(eventId: String, userId: String): Boolean

    /**
     *  Update the given event after edition
     */
    suspend fun updateEditedEvent(eventId: String, name: String,
                                  limPart: Int, lat: Double, long: Double,
                                  date: LocalDateTime)
}