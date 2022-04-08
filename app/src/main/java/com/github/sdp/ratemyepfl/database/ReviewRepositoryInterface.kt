package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review

interface ReviewRepositoryInterface {
    /**
     * Add a review to the repository
     *
     */
    fun add(value: HashMap<String, Any?>)

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

    suspend fun addLiker(id: String, uid: String)
    suspend fun removeLiker(id: String, uid: String)
    suspend fun addDisliker(id: String, uid: String)
    suspend fun removeDisliker(id: String, uid: String)

}