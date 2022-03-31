package com.github.sdp.ratemyepfl.model.serializer

import com.github.sdp.ratemyepfl.model.items.Reviewable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Defines the serialization strategies for an item. Please use these functions as JSON.encodeToString
 * may fails for unknown reason.
 */
object ItemSerializer {

    /**
     * Define the serialization strategy for an item
     *
     * @param item: reviewable to serialize
     * @return JSON of the item as a String
     */
    fun serialize(item: Reviewable): String =
        Json.encodeToString(Reviewable.serializer(), item)

    /**
     * Define the deserialization strategy for an item encoded in JSON string
     *
     * @param item: reviewable to deserialized, encoded in JSON and provided as a string
     * @return the deserialized reviewable, or null if it fails
     */
    fun deserialize(item: String): Reviewable? =
        try {
            Json.decodeFromString(Reviewable.serializer(), item)
        } catch (e: SerializationException) {
            null
        }
}