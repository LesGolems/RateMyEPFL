package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.model.review.Post

interface PostRepository<T : Post> : Repository<T> {

    companion object {
        const val TITLE_FIELD_NAME = "title"
        const val COMMENT_FIELD_NAME = "comment"
        const val DATE_FIELD_NAME = "date"
        const val UID_FIELD_NAME = "uid"
        const val LIKERS_FIELD_NAME = "likers"
        const val DISLIKERS_FIELD_NAME = "dislikers"
    }

    /**
     * Add a [Post] with an auto-generated ID, and returns this ID
     *
     * @param item: the [Post] to add
     */
    suspend fun addAndGetId(item: T): String

    /**
     * Adds an up vote from [userId] to the post with id [postId]. If the user
     *
     * @param postId: id of the post
     * @param userId: id of the user that adds the upvote
     */
    suspend fun addUpVote(postId: String, userId: String)

    /**
     * Remove an up vote from [userId] to the post with id [postId]. A user can only add one
     * vote
     *
     * @param postId: id of the post
     * @param userId: id of the user that remove the upvote
     */
    suspend fun removeUpVote(postId: String, userId: String)

    /**
     * Adds a down vote from [userId] to the post with id [postId]. A user can only remove a
     * vote that he made
     *
     * @param postId: id of the post
     * @param userId: id of the user that adds the down vote
     */
    suspend fun addDownVote(postId: String, userId: String)

    /**
     * Remove a down vote from [userId] to the post with id [postId]
     *
     * @param postId: id of the post
     * @param userId: id of the user that adds the down vote
     */
    suspend fun removeDownVote(postId: String, userId: String)
}