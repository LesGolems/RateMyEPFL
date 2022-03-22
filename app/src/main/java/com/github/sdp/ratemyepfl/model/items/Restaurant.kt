package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable
import java.lang.Exception

@Serializable
data class Restaurant(
    override val id: String,
    override var numRatings: Int = 0,
    override var avgRating: Double = 0.0
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
        private const val TAG = "Restaurant"
    }






}