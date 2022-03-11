package com.github.sdp.ratemyepfl.review

import com.github.sdp.ratemyepfl.serializer.LocalDateSerializer
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.LocalDate

/**
 * Represents a course review
 */
@Serializable
data class CourseReview private constructor(
    override val rating: ReviewRating,
    val title: String,
    override val comment: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
) : Review() {

    companion object {
        /**
         * Function to serialize a CourseReview easily, based on kotlin serialization plugin.
         *
         * @param review: Review to serialize
         * @return the serialization (in JSON) of the review
         */
        fun serialize(review: CourseReview): String = Json.encodeToString(review)

        /**
         * Function to deserialize a CourseReview
         *
         * @param review: Review to deserialize (in JSON)
         * @return the deserialized review
         */
        fun deserialize(review: String): CourseReview = Json.decodeFromString(review)
    }

    fun serialize(): String = Companion.serialize(this)

    /**
     * Allows to create a ReviewRating incrementally.
     * NB: Even if a user can create a review incrementally, he
     * must specify every property of the review.
     */
    data class Builder(
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
        fun build(): CourseReview {
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