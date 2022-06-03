package com.github.sdp.ratemyepfl.backend.database.post

import com.github.sdp.ratemyepfl.model.post.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository : PostRepository<Review> {

    /**
     * Retrieve a review from id.
     *
     * @return the review if found, otherwise null
     */
    suspend fun getReviewById(id: String): Review?

    /**
     * Retrieve the list of reviews of a reviewable from its id
     *
     * @return the list non-null reviews of the reviewable
     */
    fun getByReviewableId(id: String): Flow<List<Review>>
}