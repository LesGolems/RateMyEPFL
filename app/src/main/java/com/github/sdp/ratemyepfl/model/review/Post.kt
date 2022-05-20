package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.post.PostRepository
import com.github.sdp.ratemyepfl.model.serializer.LocalDateSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

@Serializable
abstract class Post @OptIn(ExperimentalSerializationApi::class) constructor(
    open val title: String,
    open val comment: String,
    @Serializable(with = LocalDateSerializer::class)
    open val date: LocalDate,
    open val uid: String? = null,
    open var likers: List<String> = listOf(),
    open var dislikers: List<String> = listOf()
) : RepositoryItem {

    abstract var postId: String

    companion object {
        /**
         * Function to serialize a Post easily, based on kotlin serialization plugin.
         *
         * @param post: Post to serialize
         * @return the serialization (in JSON) of the post
         */
        fun serialize(post: Post): String = Json.encodeToString(post)

        /**
         * Function to deserialize a Post
         *
         * @param post: Post to deserialize (in JSON)
         * @return the deserialized post
         */
        fun deserialize(post: String): Post = Json.decodeFromString(post)
    }

    fun serialize(): String = serialize(this)

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
     * Creates a hash map of the post
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            PostRepository.TITLE_FIELD_NAME to title,
            PostRepository.COMMENT_FIELD_NAME to comment,
            PostRepository.DATE_FIELD_NAME to date.toString(),
            PostRepository.UID_FIELD_NAME to uid,
            PostRepository.LIKERS_FIELD_NAME to likers,
            PostRepository.DISLIKERS_FIELD_NAME to dislikers
        )
    }

    /**
     * Allows to create a Post incrementally.
     * NB: Even if a user can create a post incrementally, he
     * must specify every property of the post.
     *
     * Mandatory: [title], [comment], [date], TODO
     */
    abstract class Builder<T: Post>(
        protected var title: String? = null,
        protected var comment: String? = null,
        protected var date: LocalDate? = null,
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
        fun setDate(date: LocalDate?) = apply {
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
