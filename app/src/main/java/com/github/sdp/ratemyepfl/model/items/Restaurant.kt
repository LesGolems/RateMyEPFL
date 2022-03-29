package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("restaurant")
data class Restaurant(
    override val id: String,
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
        fun DocumentSnapshot.toRestaurant(): Restaurant = Restaurant(id)
    }
}