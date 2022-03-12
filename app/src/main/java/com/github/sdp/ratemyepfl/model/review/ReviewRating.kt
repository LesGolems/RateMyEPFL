package com.github.sdp.ratemyepfl.model.review

/**
 * Defines the rating for a review.
 * Consists of 5 different types, each with an integer
 * corresponding to a grade on a scale from 1 to 5
 */
enum class ReviewRating(val rating: Int) {
    TERRIBLE(ReviewRating.TERRIBLE_RATING),
    POOR(ReviewRating.POOR_RATING),
    AVERAGE(ReviewRating.AVERAGE_RATING),
    GOOD(ReviewRating.GOOD_RATING),
    EXCELLENT(ReviewRating.EXCELLENT_RATING);

    companion object {
        // Defines the integer values corresponding to each rating
        const val EXCELLENT_RATING = 5
        const val GOOD_RATING = 4
        const val AVERAGE_RATING = 3
        const val POOR_RATING = 2
        const val TERRIBLE_RATING = 1
    }
}



