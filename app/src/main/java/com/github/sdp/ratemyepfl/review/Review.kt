package com.github.sdp.ratemyepfl.review

import com.github.sdp.ratemyepfl.review.ReviewRating

abstract class Review(open val rating: ReviewRating, open val comment: String) {

}
