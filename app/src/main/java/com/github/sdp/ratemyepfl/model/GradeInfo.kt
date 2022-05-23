package com.github.sdp.ratemyepfl.model

import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
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
}