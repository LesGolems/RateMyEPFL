package com.github.sdp.ratemyepfl.model.items

import android.location.Location
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
    val occupancy: Int = 0,
    val latitude: Long,
    val longitude: Long
) : Reviewable() {

    override val collectionPath = "restaurants"

    override fun toString(): String {
        return id
    }

    companion object {
        /**
         * Converts the json data into a Restaurant
         *
         * @return the restaurant held by the data
         */
        fun DocumentSnapshot.toRestaurant(): Restaurant? {
            return try {
                val occupancy = get("occupancy")!! as Int
                val lat = getLong("latitude")!!
                val long = getLong("longitude")!!
                Restaurant(id, occupancy, lat, long)
            } catch (e: Exception) {
                null
            }
        }
    }
}