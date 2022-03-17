package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

@Serializable
abstract class Review() {
    abstract val rating: ReviewRating
    abstract val title: String
    abstract val comment: String
    @Serializable(with = LocalDateSerializer::class)
    abstract val date: LocalDate

    companion object {
        /**
         * Function to serialize a CourseReview easily, based on kotlin serialization plugin.
         *
         * @param review: Review to serialize
         * @return the serialization (in JSON) of the review
         */
        fun serialize(review: Review): String = Json.encodeToString(review)

        /**
         * Function to deserialize a CourseReview
         *
         * @param review: Review to deserialize (in JSON)
         * @return the deserialized review
         */
        fun deserialize(review: String): Review = Json.decodeFromString(review)
    }

    /**
     * Allows to create a ReviewRating incrementally.
     * NB: Even if a user can create a review incrementally, he
     * must specify every property of the review.
     */
    open class Builder(
        private var rating: ReviewRating? = null,
        private var title: String? = null,
        private var comment: String? = null,
        private var date: LocalDate? = null,
    ) {
        /**
         * Sets the rating of the review
         * @param rating: the new rating of the review
         * @return this
         */
        fun setRating(rating: ReviewRating) = apply {
            this.rating = rating
        }

        /**
         * Sets the title of the review
         * @param title: the new title of the review
         * @return this
         */
        fun setTitle(title: String) = apply {
            this.title = title
        }

        /**
         * Sets the comment of the review
         * @param comment: the new comment of the review
         * @return this
         */
        fun setComment(comment: String) = apply {
            this.comment = comment
        }

        /**
         * Sets the date of publication the review
         * @param date: the new date of the review
         * @return this
         */
        fun setDate(date: LocalDate) = apply {
            this.date = date
        }

        /**
         * Builds the corresponding CourseReview
         *
         * @throws IllegalStateException if one of the properties is null
         */
        fun build(): Review {
            val rate = this.rating
            val title = this.title
            val comment = this.comment
            val date = this.date

            if (rate != null && title != null && comment != null && date != null) {
                return CourseReview(
                    rate, title, comment, date
                )
            } else throw IllegalStateException("Cannot build a review made of null elements")
        }
    }
}
