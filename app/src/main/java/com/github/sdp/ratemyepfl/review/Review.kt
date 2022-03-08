package com.github.sdp.ratemyepfl.review

import com.github.sdp.ratemyepfl.review.ReviewRating
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
abstract class Review(open val rating: ReviewRating, open val comment: String) {

}
