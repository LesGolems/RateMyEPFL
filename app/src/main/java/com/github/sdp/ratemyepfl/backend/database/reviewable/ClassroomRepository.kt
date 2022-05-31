package com.github.sdp.ratemyepfl.backend.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Classroom

interface ClassroomRepository : ReviewableRepository<Classroom> {
    /**
     * Retrieve the rooms from the repository
     *
     * @return a list of non-null rooms
     */
    suspend fun getClassrooms(): List<Classroom>

    /**
     * Retrieve an rooms from id.
     *
     * @return the rooms if found
     *
     * @throws NoSuchElementException if no elemnt with the provided id exists
     */
    suspend fun getRoomByName(name: String): Classroom

}