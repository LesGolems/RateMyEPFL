package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review

interface ReviewRepositoryInterface {
    /**
     * Add a review to the repository
     *
     */
    fun add(value: HashMap<String, Any?>)

    /**
     * Remove a review from the repository
     *
     * @param reviewId: the id of the review to remove
     */
    fun remove(reviewId: String)

    /**
     * Retrieve the reviews from the repository
     *
     * @return a list of non-null reviews
     */
    suspend fun getReviews(): List<Review>

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
    suspend fun getByReviewableId(id: String?): List<Review>

    suspend fun addUidInArray(fieldName: String, id: String, uid: String)
    suspend fun removeUidInArray(fieldName: String, id: String, uid: String)
}