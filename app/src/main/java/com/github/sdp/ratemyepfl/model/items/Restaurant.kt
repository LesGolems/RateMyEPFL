package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
    val lat: Double,
    val long: Double
) : Reviewable(){

    override fun toString(): String {
        return id
    }

    companion object {
        fun DocumentSnapshot.toRestaurant() : Restaurant? {
            return try {
                val lat: Double = getString("lat")!!.toDouble()
                val long: Double = getString("long")!!.toDouble()
                Restaurant(id, lat, long)
            } catch(e: Exception){
                null
            }
        }
    }

    override val collectionPath = "restaurants"
}