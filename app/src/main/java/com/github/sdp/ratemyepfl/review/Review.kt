package com.github.sdp.ratemyepfl.review

import kotlinx.serialization.Serializable

@Serializable
abstract class Review(open val rating: ReviewRating, open val comment: String) {

}
