package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
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
    abstract val numReviews: Int

}