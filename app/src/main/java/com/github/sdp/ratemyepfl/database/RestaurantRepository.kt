package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class RestaurantRepository @Inject constructor() : RestaurantRepositoryInterface,
    Repository(RESTAURANT_COLLECTION_PATH) {

    companion object {
        const val RESTAURANT_COLLECTION_PATH = "restaurants"

        fun DocumentSnapshot.toRestaurant(): Restaurant? {
            return try {
                val occupancy = (get("occupancy") as Int?) ?: 0
                val lat = getDouble("latitude") ?: 0.0
                val lon = getDouble("longitude") ?: 0.0

                return Restaurant(id, occupancy, lat, lon)
            } catch (e: IllegalStateException) {
                null
            }
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
            val occupancy = snapshot.get("occupancy") as Int?
            if (occupancy != null){
                transaction.update(docRef, "occupancy", occupancy + 1)
            }
            null
        }
    }

    fun decrementOccupancy(id: String) {
        val docRef = collection.document(id)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.get("occupancy") as Int?
            if (occupancy != null){
                transaction.update(docRef, "occupancy", occupancy - 1)
            }
            null
        }
    }

}