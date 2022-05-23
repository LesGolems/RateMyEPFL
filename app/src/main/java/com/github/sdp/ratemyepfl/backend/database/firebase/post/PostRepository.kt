package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.model.review.Post
import com.google.android.gms.tasks.Task

interface PostRepository<T : Post> : Repository<T> {

    /**
     * Add a [Post] with a provided id. This should be used carefully as it may overwrite data.
     *
     * @param item: [Post] to add
     * @param withId: a provided unique identifier
     *
     */
    fun addWithId(item: T, withId: String): Task<String>

    /**
     * Add an up vote from [userId] to the post with id [postId].
     * A user can only add one up vote
     *
     * @param postId: id of the post
     * @param userId: id of the user that adds the upvote
     */
    suspend fun addUpVote(postId: String, userId: String)

    /**
     * Remove an up vote from [userId] to the post with id [postId].
     * A user can only remove an up vote that he made
     *
     * @param postId: id of the post
     * @param userId: id of the user that remove the upvote
     */
    suspend fun removeUpVote(postId: String, userId: String)

    /**
     * Add a down vote from [userId] to the post with id [postId].
     * A user can only add one down vote
     *
     * @param postId: id of the post
     * @param userId: id of the user that adds the down vote
     */
    suspend fun addDownVote(postId: String, userId: String)

    /**
     * Remove a down vote from [userId] to the post with id [postId].
     * A user can only remove a down vote that he made
     *
     * @param postId: id of the post
     * @param userId: id of the user that adds the down vote
     */
    suspend fun removeDownVote(postId: String, userId: String)
}