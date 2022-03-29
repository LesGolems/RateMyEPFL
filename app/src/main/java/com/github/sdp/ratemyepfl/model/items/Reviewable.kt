package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

/**
 * Represents a reviewable item
 */
@Serializable
sealed class Reviewable {
    abstract val id: String
    abstract val collectionPath: String
}