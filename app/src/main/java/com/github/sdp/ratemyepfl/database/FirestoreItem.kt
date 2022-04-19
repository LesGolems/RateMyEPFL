package com.github.sdp.ratemyepfl.database

interface FirestoreItem {
    /**
     * Convert the object to a HashMap for Firestore storage
     */
    fun toHashMap(): HashMap<String, Any?>
}