package com.github.sdp.ratemyepfl.database

interface FirestoreItem {

    /** Unique id of the item **/
    fun getId(): String

    /**
     * Convert the object to a HashMap for Firestore storage
     */
    fun toHashMap(): HashMap<String, Any?>
}