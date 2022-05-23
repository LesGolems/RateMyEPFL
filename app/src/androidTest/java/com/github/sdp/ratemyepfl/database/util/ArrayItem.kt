package com.github.sdp.ratemyepfl.database.util

import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.google.firebase.firestore.DocumentSnapshot

@Suppress("UNCHECKED_CAST")
data class ArrayItem(private val id: String = "", val data: List<Int> = listOf()) : RepositoryItem {

    companion object {
        const val DATA_FIELD = "data"

        fun DocumentSnapshot.toArrayItem(): ArrayItem? = toItem()

    }

    override fun getId(): String =
        id

}