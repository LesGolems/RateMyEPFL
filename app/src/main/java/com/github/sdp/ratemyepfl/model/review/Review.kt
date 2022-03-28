package com.github.sdp.ratemyepfl.model.review

import android.util.Log
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.model.serializer.LocalDateSerializer
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

@Serializable
data class Review @OptIn(ExperimentalSerializationApi::class) constructor(
    val rating: ReviewRating,
    val title: String,
    val comment: String,
    val reviewableId: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val author: User? = null
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

        fun DocumentSnapshot.toReview(): Review? {
            return try {
                val rating = ReviewRating.valueOf(getString("rating")!!)
                val title = getString("title")!!
                val comment = getString("comment")!!
                val reviewableId = getString("reviewableId")!!
                val date = LocalDate.parse(getString("date")!!)
                Review(rating, title, comment, reviewableId, date)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting course review", e)
                null
            }
        }

        private const val TAG = "review"
    }

    fun serialize(): String = Companion.serialize(this)

    @OptIn(ExperimentalSerializationApi::class)
    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            "title" to title, "rating" to rating.toString(),
            "comment" to comment, "reviewableId" to reviewableId, "date" to date.toString()
        )
    }

    /**
     * Allows to create a ReviewRating incrementally.
     * NB: Even if a user can create a review incrementally, he
     * must specify every property of the review.
     */
    data class Builder(
        private var rating: ReviewRating? = null,
        private var title: String? = null,
        private var comment: String? = null,
        private var reviewableId: String? = null,
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
         * Sets the id of the reviewed item
         * @param id: reviewed item id
         * @return this
         */
        fun setReviewableID(id: String) = apply {
            this.reviewableId = id
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
            val reviewableId = this.reviewableId
            val date = this.date

            if (rate != null && title != null && comment != null && reviewableId != null && date != null) {
                return Review(
                    rate, title, comment, reviewableId, date
                )
            } else throw IllegalStateException("Cannot build a review made of null elements")
        }
    }
}
