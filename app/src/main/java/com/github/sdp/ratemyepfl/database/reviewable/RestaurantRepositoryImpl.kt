package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RestaurantRepositoryImpl(val repository: ReviewableRepositoryImpl<Restaurant>) :
    RestaurantRepository, ReviewableRepository<Restaurant> by repository,
    Repository<Restaurant> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(
        ReviewableRepositoryImpl(
            db,
            RESTAURANT_COLLECTION_PATH,
            RESTAURANT_NAME_FIELD_NAME
        ) { documentSnapshot ->
            documentSnapshot.toRestaurant()
        })

    companion object {
        const val RESTAURANT_NAME_FIELD_NAME: String = "name"
        const val RESTAURANT_COLLECTION_PATH = "restaurants"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"
        const val OCCUPANCY_FIELD_NAME = "occupancy"

        fun DocumentSnapshot.toRestaurant(): Restaurant? {
            val name = getString(RESTAURANT_NAME_FIELD_NAME)
            val occupancy = getField<Int>(OCCUPANCY_FIELD_NAME)
            val lat = getDouble(LATITUDE_FIELD_NAME)
            val lon = getDouble(LONGITUDE_FIELD_NAME)
            val numReviews = getField<Int>(NUM_REVIEWS_FIELD_NAME)
            val averageGrade = getDouble(AVERAGE_GRADE_FIELD_NAME)
            return Restaurant.Builder(name, occupancy, lat, lon, numReviews, averageGrade)
                .build()
        }
    }

    override suspend fun getRestaurants(): List<Restaurant> {
        return repository.take(Query.DEFAULT_QUERY_LIMIT.toLong())
            .mapNotNull { obj -> obj.toRestaurant() }
    }

    override suspend fun getRestaurantById(id: String): Restaurant? =
        repository.getById(id).toRestaurant()

    override suspend fun incrementOccupancy(id: String) {
        val docRef = repository.collection.document(id)
        repository.database.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getField<Int>(OCCUPANCY_FIELD_NAME)
            if (occupancy != null) {
                transaction.update(docRef, OCCUPANCY_FIELD_NAME, occupancy + 1)
            }
            null
        }.await()
    }

    override suspend fun decrementOccupancy(id: String) {
        val docRef = repository.collection.document(id)
        repository.database.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val occupancy = snapshot.getField<Int>(OCCUPANCY_FIELD_NAME)
            if (occupancy != null) {
                transaction.update(docRef, OCCUPANCY_FIELD_NAME, occupancy - 1)
            }
            null
        }.await()
    }

    override suspend fun updateRestaurantRating(id: String, rating: ReviewRating) =
        repository.updateRating(id, rating)

    override fun search(prefix: String): QueryResult<List<Restaurant>> =
        repository.search(RESTAURANT_NAME_FIELD_NAME, prefix)

}