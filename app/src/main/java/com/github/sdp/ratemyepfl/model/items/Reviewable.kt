package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.FirestoreItem
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
import kotlinx.serialization.Serializable

/**
 * Represents a reviewable item
 *
 * Note: Do not use a property name 'type', as it conflicts with
 * the serialization
 */
@Serializable
sealed class Reviewable : FirestoreItem {
    abstract val numReviews: Int
    abstract val averageGrade: Double

    override fun toHashMap(): HashMap<String, Any?> = hashMapOf(
        ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME to numReviews,
        ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME to averageGrade,
    )
}