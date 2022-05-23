package com.github.sdp.ratemyepfl.model

import kotlinx.serialization.Serializable

/**
 * Information for a specific review.
 */
@Serializable
data class ReviewInfo(

    val reviewGrade: Int = DEFAULT_REVIEW_INFO.reviewGrade,
    val likeRatio: Int = DEFAULT_REVIEW_INFO.likeRatio,
) {
    companion object {
        val DEFAULT_REVIEW_INFO = ReviewInfo(0, 0)
    }
}
