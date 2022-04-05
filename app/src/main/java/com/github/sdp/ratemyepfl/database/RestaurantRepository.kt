package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class RestaurantRepository @Inject constructor() : RestaurantRepositoryInterface, Repository(RESTAURANT_COLLECTION_PATH) {

    companion object {
        const val RESTAURANT_COLLECTION_PATH = "restaurants"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"

        fun DocumentSnapshot.toRestaurant(): Restaurant? {
            val builder = Restaurant.Builder()
                .setId(id)
                .setLat(getString(LATITUDE_FIELD_NAME)?.toDouble())
                .setLong(getString(LONGITUDE_FIELD_NAME)?.toDouble())

            return try {
                builder.build()
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
}