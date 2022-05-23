package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.model.time.DateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Note: "@Transient" impeaches the properties of this abstract class to be serialized
 * before they are overridden by the sub-class
 */
@Serializable
abstract class Post constructor(
    @Transient
    open val title: String = "",
    @Transient
    open val comment: String = "",
    @Transient
    open val date: DateTime = DateTime.DEFAULT_DATE_TIME,
    @Transient
    open val uid: String? = null,
    @Transient
    open var likers: List<String> = listOf(),
    @Transient
    open var dislikers: List<String> = listOf()
) : RepositoryItem {

    abstract var postId: String

    override fun getId(): String = postId

    /**
     * Set the id of the [Post]
     *
     * @param id: Unique identifier of the post
     *
     * @return the [Post] with modified id
     */
    abstract fun withId(id: String): Post

    /**
     * Allows to create a Post incrementally.
     * NB: Even if a user can create a post incrementally, he
     * must specify every property of the post.
     *
     * Mandatory: [title], [comment], [date], [uid], [likers], [dislikers]
     */
    abstract class Builder<T : Post>(
        protected var title: String? = null,
        protected var comment: String? = null,
        protected var date: DateTime? = null,
        protected var uid: String? = null,
        protected var likers: List<String>? = listOf(),
        protected var dislikers: List<String>? = listOf(),
    ) {

        /**
         * Sets the title of the post
         * @param title: the new title of the post
         * @return this
         */
        fun setTitle(title: String?) = apply {
            this.title = title
        }

        /**
         * Sets the comment of the post
         * @param comment: the new comment of the post
         * @return this
         */
        fun setComment(comment: String?) = apply {
            this.comment = comment
        }

        /**
         * Sets the date of publication the post
         * @param date: the new date of the post
         * @return this
         */
        fun setDate(date: DateTime?) = apply {
            this.date = date
        }

        /**
         * Sets the uid of the author of the post
         * @param uid: uid of author
         * @return this
         */
        fun setUid(uid: String?) = apply {
            this.uid = uid
        }

        fun setLikers(likers: List<String>?) = apply {
            this.likers = likers
        }

        fun setDislikers(dislikers: List<String>?) = apply {
            this.dislikers = dislikers
        }

        /**
         * Builds the corresponding Post
         *
         * @throws IllegalStateException if one of the properties is null
         */
        abstract fun build(): T

        infix fun <T> asMandatory(field: T?): T =
            field ?: throw IllegalStateException("A mandatory field cannot be null")
    }
}
