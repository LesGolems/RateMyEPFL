package com.github.sdp.ratemyepfl.database.util

import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.google.firebase.firestore.DocumentSnapshot

@Suppress("UNCHECKED_CAST")
data class ArrayItem(private val id: String, val data: List<Int>) : RepositoryItem {

    companion object {
        const val DATA_FIELD = "data"

        fun DocumentSnapshot.toArrayItem(): ArrayItem? = try {
            ArrayItem(id, get(DATA_FIELD) as List<Int>)
        } catch (e: Exception) {
            null
        }

    }

    override fun getId(): String =
        id


    override fun toHashMap(): HashMap<String, Any?> = hashMapOf(
        DATA_FIELD to data
    )

}