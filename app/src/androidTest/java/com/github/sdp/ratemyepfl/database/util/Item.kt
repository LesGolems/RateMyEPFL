package com.github.sdp.ratemyepfl.database.util

import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    private val id: String = "",
    val data: Int = 0
) :
    RepositoryItem {
    override fun getId(): String = id

    companion object {
        const val ID_FIELD = "id"
        const val DATA_FIELD = "data"

        fun DocumentSnapshot.toItem(): Item? = toObject(Item::class.java)
    }
}
