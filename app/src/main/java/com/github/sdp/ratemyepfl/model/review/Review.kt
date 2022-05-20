package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.database.post.ReviewRepositoryImpl
import java.time.LocalDate

data class Review constructor(
    val rating: ReviewRating,
    override val title: String,
    override val comment: String,
    val reviewableId: String,
    override val date: LocalDate,
    override val uid: String? = null,
    override var likers: List<String> = listOf(),
    override var dislikers: List<String> = listOf()
) : Post(title, comment, date, uid, likers, dislikers) {

    override var postId: String = this.hashCode().toString()

    override fun withId(id: String): Review {
        return this.apply {
            this.postId = id
        }
    }

    /**
     * Creates a hash map of the review
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            ReviewRepositoryImpl.RATING_FIELD_NAME to rating,
            ReviewRepositoryImpl.REVIEWABLE_ID_FIELD_NAME to reviewableId
        ).apply { this.putAll(super.toHashMap()) }
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
        private var reviewableId: String? = null,
    ) : Post.Builder<Review>() {

        /**
         * Sets the rating of the review
         * @param rating: the new rating of the review
         * @return this
         */
        fun setRating(rating: ReviewRating?) = apply {
            this.rating = rating
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
         * Builds the corresponding CourseReview
         *
         * @throws IllegalStateException if one of the properties is null
         */
        override fun build(): Review {
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
                likers,
                dislikers
            )
        }
    }
}
