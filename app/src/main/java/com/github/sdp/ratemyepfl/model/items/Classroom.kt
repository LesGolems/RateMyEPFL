package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    override val id: String,
    val type: String? = null,
) : Reviewable() {
    // This converts a json object from firebase into a Classroom object
    companion object {
        fun DocumentSnapshot.toClassroom(): Classroom? {
            return try {
                Classroom(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun toString(): String {
        return "$id"
    }

    override val collectionPath = "rooms"
}
