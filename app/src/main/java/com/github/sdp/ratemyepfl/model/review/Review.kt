package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.model.serializer.LocalDateSerializer
import com.github.sdp.ratemyepfl.model.user.User
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

@Serializable
data class Review @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: String?,
    val rating: ReviewRating,
    val title: String,
    val comment: String,
    val reviewableId: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val author: User? = null,
    var opinion: ReviewOpinion = ReviewOpinion.NO_OPINION
) {
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

        private const val TAG = "review"
    }

    fun serialize(): String = Companion.serialize(this)

    @OptIn(ExperimentalSerializationApi::class)
    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            ReviewRepository.TITLE_FIELD_NAME to title,
            ReviewRepository.RATING_FIELD_NAME to rating.toString(),
            ReviewRepository.COMMENT_FIELD_NAME to comment,
            ReviewRepository.REVIEWABLE_ID_FIELD_NAME to reviewableId,
            ReviewRepository.DATE_FIELD_NAME to date.toString()
        )
    }

    /**
     * Allows to create a ReviewRating incrementally.
     * NB: Even if a user can create a review incrementally, he
     * must specify every property of the review.
     */
    data class Builder(
        private var id: String? = null,
        private var rating: ReviewRating? = null,
        private var title: String? = null,
        private var comment: String? = null,
        private var reviewableId: String? = null,
        private var date: LocalDate? = null,
    ) {
        /**
         * Sets the id of the review
         * @param id: the new id of the review
         * @return this
         */
        fun setId(id: String?) = apply {
            this.id = id
        }

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
         * Sets the date of publication the review
         * @param date: the new date of the review
         * @return this
         */
        fun setDate(date: LocalDate?) = apply {
            this.date = date
        }

        /**
         * Builds the corresponding CourseReview
         *
         * @throws IllegalStateException if one of the properties is null
         */
        fun build(): Review {
            val id = this.id
            val rate = this asMandatory rating
            val title = this asMandatory title
            val comment = this asMandatory comment
            val reviewableId = this asMandatory reviewableId
            val date = this asMandatory date

            return Review(id, rate, title, comment, reviewableId, date)
        }

        private infix fun <T> asMandatory(field: T?): T =
            field ?: throw IllegalStateException("A mandatory field cannot be null")
    }
}
