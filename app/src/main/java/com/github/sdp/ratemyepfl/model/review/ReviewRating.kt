package com.github.sdp.ratemyepfl.model.review

import kotlin.math.roundToInt

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

    /**
     * Convert a rating to the correspond integer.
     *
     * @param rating: the rating to convert
     * @return the corresponding integer
     */
    fun toValue(): Int = when (this) {
        TERRIBLE -> TERRIBLE_RATING
        POOR -> POOR_RATING
        AVERAGE -> AVERAGE_RATING
        GOOD -> GOOD_RATING
        EXCELLENT -> EXCELLENT_RATING
    }

    companion object {
        // Defines the integer values corresponding to each rating
        const val EXCELLENT_RATING = 5
        const val GOOD_RATING = 4
        const val AVERAGE_RATING = 3
        const val POOR_RATING = 2
        const val TERRIBLE_RATING = 1

        /**
         * Convert a float to the correspond rating. It rounds the value to the nearest integer,
         * and associate it a rating according to its integer value.
         *
         * @param value: the value to convert
         * @return the corresponding rating, and null if it does not correspond to anything
         */
        fun fromValue(value: Float): ReviewRating? = when (value.roundToInt()) {
            TERRIBLE_RATING -> TERRIBLE
            POOR_RATING -> POOR
            AVERAGE_RATING -> AVERAGE
            GOOD_RATING -> GOOD
            EXCELLENT_RATING -> EXCELLENT
            else -> null
        }
    }
}



