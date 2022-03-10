package com.github.sdp.ratemyepfl.review

import java.lang.IllegalStateException
import java.time.LocalDate

class CourseReview(
    rating: ReviewRating,
    val title: String,
    comment: String,
    val date: LocalDate
) : Review(rating, comment) {

    override fun toString(): String {
        return String.format("Rating: %s \n Title: %s \n \t %s",
            rating.toString(),
            title,
            comment)
    }

    data class Builder(
        private var rate: ReviewRating? = null,
        private var title: String? = null,
        private var comment: String? = null,
    ) {
        fun setRate(rate: ReviewRating) = apply {
            this.rate = rate
        }

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setComment(comment: String) = apply {
            this.comment = comment
        }

        fun build(): CourseReview =
            if (rate == null || title == null || comment == null)
                throw IllegalStateException()
            else CourseReview(
                rate!!, title!!, comment!!, LocalDate.now()
            )
    }

}