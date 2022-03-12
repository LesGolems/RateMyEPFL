package com.github.sdp.ratemyepfl.model.review

import kotlinx.serialization.Serializable

@Serializable
abstract class Review() {
    abstract val rating: ReviewRating
    abstract val comment: String
}
