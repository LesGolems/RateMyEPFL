package com.github.sdp.ratemyepfl.database.util

import com.github.sdp.ratemyepfl.database.FirestoreItem
import com.google.firebase.firestore.DocumentSnapshot

data class ArrayItem(private val id: String, val data: List<Int>) : FirestoreItem {

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