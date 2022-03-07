package com.github.sdp.ratemyepfl.review

enum class ReviewRating(val rating: Int) {
    TERRIBLE(ReviewRating.TERRIBLE_RATING),
    POOR(ReviewRating.POOR_RATING),
    AVERAGE(ReviewRating.AVERAGE_RATING),
    GOOD(ReviewRating.GOOD_RATING),
    EXCELLENT(ReviewRating.EXCELLENT_RATING);

    companion object {
        const val EXCELLENT_RATING = 5
        const val GOOD_RATING = 4
        const val AVERAGE_RATING = 3
        const val POOR_RATING = 2
        const val TERRIBLE_RATING = 1
    }
}



