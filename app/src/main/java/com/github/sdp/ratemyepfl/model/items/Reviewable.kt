package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import kotlinx.serialization.Serializable

/**
 * Represents a reviewable item
 *
 * Note: Do not use a property name 'type', as it conflicts with
 * the serialization
 */
@Serializable
sealed class Reviewable : RepositoryItem {
    abstract val grade: Double

    override fun toHashMap(): HashMap<String, Any?> = hashMapOf(
        Pair(ReviewableRepository.AVERAGE_GRADE_FIELD_NAME, grade)
    )
}