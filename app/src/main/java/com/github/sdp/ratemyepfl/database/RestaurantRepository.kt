package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class RestaurantRepository @Inject constructor() : RestaurantRepositoryInterface,
    Repository(RESTAURANT_COLLECTION_PATH) {

    companion object {
        const val RESTAURANT_COLLECTION_PATH = "restaurants"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"

        fun DocumentSnapshot.toRestaurant(): Restaurant? {
            val occupancy = getString("occupancy")?.toInt() ?: 0
            val lat = getString("lat")?.toDouble() ?: 0.0
            val lon = getString("long")?.toDouble() ?: 0.0

            if(id != null)
                return Restaurant(id, occupancy, lat, lon)

            return null
        }
    }

    fun toItem(snapshot: DocumentSnapshot): Restaurant? = snapshot.toRestaurant()

    override suspend fun getRestaurants(): List<Restaurant> {
        return take(DEFAULT_LIMIT).mapNotNull { obj -> toItem(obj) }
    }

    override suspend fun getRestaurantById(id: String): Restaurant? = toItem(getById(id))

    fun incrementOccupancy(id: String) {
        val docRef = collection.document(id)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getString("occupancy")?.toInt()
            if (occupancy != null) {
                transaction.update(docRef, "occupancy", (occupancy + 1).toString())
            }
            null
        }
    }

    fun decrementOccupancy(id: String) {
        val docRef = collection.document(id)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getString("occupancy")?.toInt()
            if (occupancy != null) {
                transaction.update(docRef, "occupancy", (occupancy - 1).toString())
            }
            null
        }
    }

}