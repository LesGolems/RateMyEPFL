package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.model.time.DateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Review constructor(
    val rating: ReviewRating = ReviewRating.AVERAGE,
    val title: String = "",
    val comment: String = "",
    val reviewableId: String = "",
    val date: DateTime = DateTime.DEFAULT_DATE_TIME,
    val uid: String? = null,
    var likers: List<String> = listOf(),
    var dislikers: List<String> = listOf()
) : RepositoryItem {

    // By default, the id of the review is a hash of this review
    private var id: String = this.hashCode().toString()

    companion object {
        /**
         * Function to serialize a Review easily, based on kotlin serialization plugin.
         *
         * @param review: Review to serialize
         * @return the serialization (in JSON) of the review
         */
        fun serialize(review: Review): String = Json.encodeToString(review)

        /**
         * Function to deserialize a Review
         *
         * @param review: Review to deserialize (in JSON)
         * @return the deserialized review
         */
        fun deserialize(review: String): Review = Json.decodeFromString(review)

    }

    fun serialize(): String = Companion.serialize(this)

    override fun getId(): String = id

    /**
     * Set the id of the [Review]
     *
     * @param id: Unique identifier of the review
     *
     * @return the [Review] with modified id
     */
    fun withId(id: String): Review = this.apply {
        this.id = id
    }

    /**
     * Allows to create a ReviewRating incrementally.
     * NB: Even if a user can create a review incrementally, he
     * must specify every property of the review.
     *
     * Mandatory: [rating], [title], [comment], [reviewableId], [date]
     */
    data class Builder(
        private var rating: ReviewRating? = null,
        private var title: String? = null,
        private var comment: String? = null,
        private var reviewableId: String? = null,
        private var date: DateTime? = null,
        private var uid: String? = null,
        private var likers: List<String>? = listOf(),
        private var dislikers: List<String>? = listOf(),
    ) {
        /**
         * Sets the rating of the review
         * @param rating: the new rating of the review
         * @return this
         */
        fun setRating(rating: ReviewRating?) = apply {
            this.rating = rating
        }

        /**
         * Sets the title of the review
         * @param title: the new title of the review
         * @return this
         */
        fun setTitle(title: String?) = apply {
            this.title = title
        }

        /**
         * Sets the comment of the review
         * @param comment: the new comment of the review
         * @return this
         */
        fun setComment(comment: String?) = apply {
            this.comment = comment
        }


        /**
         * Sets the id of the reviewed item
         * @param id: reviewed item id
         * @return this
         */
        fun setReviewableID(id: String?) = apply {
            this.reviewableId = id
        }

        /**
         * Sets the uid of the author of the review
         * @param uid: uid of author
         * @return this
         */
        fun setUid(uid: String?) = apply {
            this.uid = uid
        }

        /**
         * Sets the date of publication the review
         * @param date: the new date of the review
         * @return this
         */
        fun setDate(date: DateTime?) = apply {
            this.date = date
        }

        fun setLikers(likers: List<String>?) = apply {
            this.likers = likers
        }

        fun setDislikers(dislikers: List<String>?) = apply {
            this.dislikers = dislikers
        }

        /**
         * Builds the corresponding CourseReview
         *
         * @throws IllegalStateException if one of the properties is null
         */
        fun build(): Review {
            val rate = this asMandatory rating
            val title = this asMandatory title
            val comment = this asMandatory comment
            val reviewableId = this asMandatory reviewableId
            val date = this asMandatory date
            val uid = this.uid
            val likers = this asMandatory this.likers
            val dislikers = this asMandatory this.dislikers

            return Review(
                rate,
                title,
                comment,
                reviewableId,
                date,
                uid,
                likers = likers,
                dislikers = dislikers
            )
        }

        private infix fun <T> asMandatory(field: T?): T =
            field ?: throw IllegalStateException("A mandatory field cannot be null")
    }
}
