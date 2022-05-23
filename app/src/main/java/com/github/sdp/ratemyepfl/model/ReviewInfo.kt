package com.github.sdp.ratemyepfl.model

import kotlinx.serialization.Serializable

/**
 * Informations for a specific review.
 */
@Serializable
data class ReviewInfo(
    val reviewGrade: Int = 0,
    val likeRatio: Int = 0,
) {
    companion object {
        val DEFAULT_REVIEW_INFO = ReviewInfo(0, 0)
    }
}