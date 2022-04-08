package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RestaurantRepository @Inject constructor(db: FirebaseFirestore) :
    RestaurantRepositoryInterface,
    Repository(db, RESTAURANT_COLLECTION_PATH) {

    companion object {
        const val RESTAURANT_COLLECTION_PATH = "restaurants"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"
        const val OCCUPANCY_FIELD_NAME = "occupancy"

        fun DocumentSnapshot.toRestaurant(): Restaurant? {
            val occupancy = getString(OCCUPANCY_FIELD_NAME)?.toInt() ?: 0
            val lat = getString(LATITUDE_FIELD_NAME)?.toDouble() ?: 0.0
            val lon = getString(LONGITUDE_FIELD_NAME)?.toDouble() ?: 0.0
            val numReviews = getString(NUM_REVIEWS_FIELD_NAME)?.toInt() ?: 0
            val averageGrade = getString(AVERAGE_GRADE_FIELD_NAME)?.toDouble() ?: 0.0
            return Restaurant(id, occupancy, lat, lon, numReviews, averageGrade)
        }
    }

    fun toItem(snapshot: DocumentSnapshot): Restaurant? = snapshot.toRestaurant()

    override suspend fun getRestaurants(): List<Restaurant> {
        return take(DEFAULT_LIMIT).mapNotNull { obj -> toItem(obj) }
    }

    override suspend fun getRestaurantById(id: String): Restaurant? = toItem(getById(id))

    override suspend fun incrementOccupancy(id: String) {
        val docRef = collection.document(id)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getString("occupancy")?.toInt()
            if (occupancy != null) {
                transaction.update(docRef, "occupancy", (occupancy + 1).toString())
            }
            null
        }.await()
    }

    override suspend fun decrementOccupancy(id: String) {
        val docRef = collection.document(id)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getString("occupancy")?.toInt()
            if (occupancy != null) {
                transaction.update(docRef, "occupancy", (occupancy - 1).toString())
            }
            null
        }.await()
    }

    override suspend fun updateRestaurantRating(id: String, rating: ReviewRating) = updateRating(id, rating)

    fun add(restaurant: Restaurant) {
        collection.document(restaurant.id).set(restaurant.toHashMap())
    }
}