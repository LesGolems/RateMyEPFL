package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction

interface ReviewRepository : Repository<Review> {
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

    /**
     * Adds an up vote from [userId] to the review with id [reviewId]. If the user
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that adds the upvote
     */
    fun addUpVote(reviewId: String, userId: String): Task<Transaction>

    /**
     * Remove an up vote from [userId] to the review with id [reviewId]. A user can only add one
     * vote
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that remove the upvote
     */
    fun removeUpVote(reviewId: String, userId: String): Task<Transaction>

    /**
     * Adds a down vote from [userId] to the review with id [reviewId]. A user can only remove a
     * vote that he made
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that adds the down vote
     */
    fun addDownVote(reviewId: String, userId: String): Task<Transaction>

    /**
     * Remove a down vote from [userId] to the review with id [reviewId]
     *
     * @param reviewId: id of the review
     * @param userId: id of the user that adds the down vote
     */
    fun removeDownVote(reviewId: String, userId: String): Task<Transaction>
}