package com.github.sdp.ratemyepfl.model.post

import com.github.sdp.ratemyepfl.model.time.DateTime
import kotlinx.serialization.Serializable

@Serializable
data class Review constructor(
    override var postId: String = "",
    val rating: ReviewRating = ReviewRating.AVERAGE,
    override val title: String = "",
    override val comment: String = "",
    val reviewableId: String = "",
    override val date: DateTime = DateTime.DEFAULT_DATE_TIME,
    override val uid: String? = null,
    override var likers: List<String> = listOf(),
    override var dislikers: List<String> = listOf()
) : Post(postId, title, comment, date, uid, likers, dislikers) {

    override fun withId(id: String): Review {
        return this.apply {
            this.postId = id
        }
    }


    /**
     * Allows to create a [Review] incrementally.
     * NB: Even if a user can create a review incrementally, he
     * must specify every property of the review.
     *
     * Mandatory: [rating], [title], [comment], [reviewableId], [date], [uid], [likers], [dislikers]
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
         * Builds the corresponding [Review]
         *
         * @throws IllegalStateException if one of the mandatory properties is null
         */
        override fun build(): Review {
            val postId = this.postId ?: ""
            val rating = this asMandatory rating
            val title = this asMandatory title
            val comment = this asMandatory comment
            val reviewableId = this asMandatory reviewableId
            val date = this asMandatory date
            val uid = this.uid
            val likers = this asMandatory this.likers
            val dislikers = this asMandatory this.dislikers

            return Review(
                postId,
                rating,
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
