package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.SearchableRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating

interface ClassroomRepository : SearchableRepository<Classroom> {
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

    /**
     * Updates the rating of the classroom using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    suspend fun updateClassroomRating(id: String, rating: ReviewRating)

}