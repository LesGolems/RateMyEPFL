package com.github.sdp.ratemyepfl.model

import com.github.sdp.ratemyepfl.database.GradeInfoRepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.google.gson.Gson
import kotlinx.serialization.Serializable

/**
 * Class containing all relevant information to compute the overall grade of an item
 */
@Serializable
data class GradeInfo(
    val itemId: String,
    val reviewsData: Map<String, ReviewInfo> = mapOf(),
    val currentGrade: Double = 0.0,
    val numReviews: Int = 0
) : RepositoryItem {

    override fun getId(): String = itemId

    /**
     * Creates an hash map of the grade info
     */
    override fun toHashMap(): HashMap<String, Any?> {
        val json = Gson().toJson(reviewsData)
        return hashMapOf(
            GradeInfoRepositoryImpl.ITEM_ID_FIELD to itemId,
            GradeInfoRepositoryImpl.REVIEWS_INFO_FIELD to json,
            GradeInfoRepositoryImpl.CURRENT_GRADE_FIELD to currentGrade,
            GradeInfoRepositoryImpl.NUM_REVIEWS_FIELD to numReviews
        )
    }

    class Builder(
        private val itemId: String?,
        private val reviewsData: Map<String, ReviewInfo>? = mapOf(),
        private val currentGrade: Double? = 0.0,
        private val numReviews: Int? = 0
    ) {
        fun build(): GradeInfo {
            val itemId = this.itemId
            val reviewsData = this.reviewsData ?: mapOf()
            val currentGrade = this.currentGrade ?: 0.0
            val numReviews = this.numReviews ?: 0

            if (itemId != null ) {
                return GradeInfo(itemId, reviewsData, currentGrade, numReviews)
            } else {
                throw IllegalStateException("Cannot build a grade info with null arguments")
            }
        }
    }
}