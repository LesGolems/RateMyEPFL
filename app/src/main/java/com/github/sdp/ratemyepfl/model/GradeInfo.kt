package com.github.sdp.ratemyepfl.model

import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.backend.database.firebase.GradeInfoRepositoryImpl
import kotlinx.serialization.Serializable

/**
 * Class containing all relevant information to compute the overall grade of an item
 */
@Serializable
data class GradeInfo(
    val itemId: String = "",
    val reviewsData: Map<String, ReviewInfo> = mapOf()
) : RepositoryItem {

    override fun getId(): String = itemId
    /**
     * Compute the overall grade the number of reviews from all the grade info
     * (FOR NOW BASIC AVERAGE)
     */
    fun computeGrade(): Double {
        var totalGrade = 0.0
        var total = 0.0
        for (ri in reviewsData.values) {
            val w = reviewWeight(ri.likeRatio)
            totalGrade += ri.reviewGrade * w
            total += w
        }
        return totalGrade / total
    }

    /**
     * Computes the weight of a review, the weight goes down as the review like ratio goes negative.
     * The weight is based on the approximate number of users of the app
     */
    private fun reviewWeight(likeRatio: Int): Double {
        return when {
            likeRatio >= 0 -> 1.0
            likeRatio < -GradeInfoRepositoryImpl.NUM_USERS -> 0.0
            else -> 1 - (-likeRatio) / GradeInfoRepositoryImpl.NUM_USERS
        }
    }
}