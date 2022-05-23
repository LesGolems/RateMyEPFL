package com.github.sdp.ratemyepfl.backend.database

import com.github.sdp.ratemyepfl.model.review.Review

interface ReviewRepository : Repository<Review> {

    /**
     * Add a [Review] with an auto-generated ID, and returns this ID
     *
     * @param item: the [Review] to add
     */
    suspend fun addAndGetId(item: Review): String

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

    /**
     * Adds an up vote from [userId] to the review with id [reviewId]. If the user
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that adds the upvote
     */
    suspend fun addUpVote(reviewId: String, userId: String)

    /**
     * Remove an up vote from [userId] to the review with id [reviewId]. A user can only add one
     * vote
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that remove the upvote
     */
    suspend fun removeUpVote(reviewId: String, userId: String)

    /**
     * Adds a down vote from [userId] to the review with id [reviewId]. A user can only remove a
     * vote that he made
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that adds the down vote
     */
    suspend fun addDownVote(reviewId: String, userId: String)

    /**
     * Remove a down vote from [userId] to the review with id [reviewId]
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that adds the down vote
     */
    suspend fun removeDownVote(reviewId: String, userId: String)
}