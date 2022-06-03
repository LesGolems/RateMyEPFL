package com.github.sdp.ratemyepfl.model.post

import kotlin.math.roundToInt

// Defines the integer values corresponding to each rating
const val EXCELLENT_RATING = 5
const val GOOD_RATING = 4
const val AVERAGE_RATING = 3
const val POOR_RATING = 2
const val TERRIBLE_RATING = 1

/**
 * Defines the rating for a review.
 * Consists of 5 different types, each with an integer
 * corresponding to a grade on a scale from 1 to 5
 */
enum class ReviewRating(val rating: Int) {
    TERRIBLE(TERRIBLE_RATING),
    POOR(POOR_RATING),
    AVERAGE(AVERAGE_RATING),
    GOOD(GOOD_RATING),
    EXCELLENT(EXCELLENT_RATING);

    /**
     * Convert a rating to the correspond integer.
     *
     * @return the corresponding integer
     */
    fun toValue(): Int = when (this) {
        TERRIBLE -> TERRIBLE_RATING
        POOR -> POOR_RATING
        AVERAGE -> AVERAGE_RATING
        GOOD -> GOOD_RATING
        EXCELLENT -> EXCELLENT_RATING
    }

    override fun toString(): String {
        return when (this) {
            TERRIBLE -> "Terrible"
            POOR -> "Poor"
            AVERAGE -> "Average"
            GOOD -> "Good"
            EXCELLENT -> "Excellent"
        }
    }

    companion object {
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



