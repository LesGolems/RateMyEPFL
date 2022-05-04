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
) : RepositoryItem {

    override fun getId(): String = itemId

    /**
     * Creates an hash map of the grade info
     */
    override fun toHashMap(): HashMap<String, Any?> {
        val json = Gson().toJson(reviewsData)
        return hashMapOf(
            GradeInfoRepositoryImpl.ITEM_ID_FIELD to itemId,
            GradeInfoRepositoryImpl.REVIEWS_INFO_FIELD to json
        )
    }

    /**
     * Compute the overall grade the number of reviews from all the grade info
     */
    fun computeGrade(): Pair<Double, Int> {
        var total = 0.0
        for (ri in reviewsData.values){
            total += ri.reviewGrade
        }
        return Pair(total/reviewsData.size, reviewsData.size)
    }

    class Builder(
        private val itemId: String?,
        private val reviewsData: Map<String, ReviewInfo>? = mapOf(),
    ) {
        fun build(): GradeInfo {
            val itemId = this.itemId
            val reviewsData = this.reviewsData ?: mapOf()

            if (itemId != null ) {
                return GradeInfo(itemId, reviewsData)
            } else {
                throw IllegalStateException("Cannot build a grade info with null arguments")
            }
        }
    }
}