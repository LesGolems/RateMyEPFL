package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
) : Reviewable(){

    override fun toString(): String {
        return "$id"
    }

    companion object {
        fun DocumentSnapshot.toRestaurant() : Restaurant? {
            return try {
                Restaurant(id)
            } catch(e:Exception){
                null
            }
        }
    }

    override val collectionPath = "restaurants"
}