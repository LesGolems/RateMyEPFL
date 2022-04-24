package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

interface FirestoreItem {

    /** Unique id of the item **/
    fun getId(): String

    /**
     * Convert the object to a HashMap for Firestore storage
     */
    fun toHashMap(): HashMap<String, Any?>
}