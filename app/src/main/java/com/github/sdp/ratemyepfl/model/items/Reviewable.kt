package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

/**
 * Represents a reviewable item
 *
 * Note: Do not use a property name 'type', as it conflicts with
 * the serialization
 */
@Serializable
sealed class Reviewable {
    abstract val id: String
    abstract val collectionPath: String
}