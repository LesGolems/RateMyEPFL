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
    val itemId: String = "",
    val reviewsData: Map<String, ReviewInfo> = mapOf()
) : RepositoryItem {

    override fun getId(): String = itemId

    class Builder(
        private val itemId: String?,
        private val reviewsData: Map<String, ReviewInfo>? = mapOf()
    ) {
        fun build(): GradeInfo {
            val itemId = this.itemId
            val reviewsData = this.reviewsData ?: mapOf()

            if (itemId != null) {
                return GradeInfo(itemId, reviewsData)
            } else {
                throw IllegalStateException("Cannot build a grade info with null arguments")
            }
        }
    }
}