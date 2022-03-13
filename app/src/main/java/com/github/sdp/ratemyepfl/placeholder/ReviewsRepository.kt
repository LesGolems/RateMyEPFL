package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.review.Review

class ReviewsRepository(reviews : Database<Review>) : Repository<Review>(reviews) {
}