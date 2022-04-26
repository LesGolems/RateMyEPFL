package com.github.sdp.ratemyepfl.database.util

import com.github.sdp.ratemyepfl.database.FirestoreItem
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import kotlinx.serialization.Serializable

@Serializable
data class Item(private val id: String, val data: Int) :
    FirestoreItem {
    override fun getId(): String = id

    override fun toHashMap(): HashMap<String, Any?> =
        hashMapOf(
            ID_FIELD to id,
            DATA_FIELD to data,
        )

    class Builder(val id: String? = null, val data: Int? = null) {
        fun build(): Item? = if (id != null && data != null) {
            Item(id, data)
        } else null
    }

    companion object {
        const val ID_FIELD = "id"
        const val DATA_FIELD = "data"

        fun DocumentSnapshot.toItem(): Item? {
            val id: String = id
            val data = getField<Int>("data")

            return Builder(id, data)
                .build()
        }


    }
}