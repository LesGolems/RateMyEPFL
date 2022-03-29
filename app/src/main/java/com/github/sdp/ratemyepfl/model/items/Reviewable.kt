package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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