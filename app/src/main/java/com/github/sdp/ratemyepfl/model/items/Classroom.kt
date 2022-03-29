package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("classroom")
class Classroom(
    override val id: String,
    val roomKind: String? = null,
) : Reviewable() {

    /**
     * Converts the json data into a classroom object
     */
    companion object {
        fun DocumentSnapshot.toClassroom(): Classroom = Classroom(id)
    }

    override fun toString(): String {
        return id
    }

    override val collectionPath = "rooms"
}
