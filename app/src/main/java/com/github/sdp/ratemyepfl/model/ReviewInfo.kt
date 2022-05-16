package com.github.sdp.ratemyepfl.model

import kotlinx.serialization.Serializable

/**
 * Information for a specific review.
 */
@Serializable
data class ReviewInfo(
    val reviewGrade: Int,
    val likeRatio: Int,
)