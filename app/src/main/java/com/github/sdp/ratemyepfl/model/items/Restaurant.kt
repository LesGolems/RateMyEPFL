package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
    val occupancy: Int = 0
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
            return try{
                val p = get("occupancy")!! as Int
                Restaurant(id, p)
            } catch (e : Exception){
                null
            }
        }
    }
}