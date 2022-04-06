package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom

interface ClassroomRepositoryInterface {
    /**
     * Retrieve the rooms from the repository
     *
     * @return a list of non-null rooms
     */
    suspend fun getClassrooms(): List<Classroom>

    /**
     * Retrieve an rooms from id.
     *
     * @return the rooms if found, otherwise null
     */
    suspend fun getRoomById(id: String): Classroom?
}