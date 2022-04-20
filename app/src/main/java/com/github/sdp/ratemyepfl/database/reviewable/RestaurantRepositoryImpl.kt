package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.QueryResult
import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RestaurantRepositoryImpl(val repository: ReviewableRepositoryImpl<Restaurant>) :
    RestaurantRepository, ReviewableRepository<Restaurant> by repository,
    Repository<Restaurant> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(
        ReviewableRepositoryImpl(
            db,
            RESTAURANT_COLLECTION_PATH
        ) { documentSnapshot ->
            documentSnapshot.toRestaurant()
        })

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

    override suspend fun getRestaurants(): List<Restaurant> {
        return repository.take(Repository.DEFAULT_QUERY_LIMIT)
            .mapNotNull { obj -> obj.toRestaurant() }
    }

    override suspend fun getRestaurantById(id: String): Restaurant? =
        repository.getById(id).toRestaurant()

    override suspend fun incrementOccupancy(id: String) {
        val docRef = repository.collection.document(id)
        repository.database.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getString(OCCUPANCY_FIELD_NAME)?.toInt()
            if (occupancy != null) {
                transaction.update(docRef, OCCUPANCY_FIELD_NAME, (occupancy + 1).toString())
            }
            null
        }.await()
    }

    override suspend fun decrementOccupancy(id: String) {
        val docRef = repository.collection.document(id)
        repository.database.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getString(OCCUPANCY_FIELD_NAME)?.toInt()
            if (occupancy != null) {
                transaction.update(docRef, OCCUPANCY_FIELD_NAME, (occupancy - 1).toString())
            }
            null
        }.await()
    }

    override suspend fun updateRestaurantRating(id: String, rating: ReviewRating) =
        repository.updateRating(id, rating)

    override fun search(pattern: String): QueryResult<List<Restaurant>> =
        repository.search(pattern)

}