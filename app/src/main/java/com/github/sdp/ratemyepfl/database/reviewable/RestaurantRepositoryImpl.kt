package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.LoaderRepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RestaurantRepositoryImpl private constructor(private val repository: LoaderRepository<Restaurant>) :
    RestaurantRepository, ReviewableRepository<Restaurant>,
    LoaderRepository<Restaurant> by repository {
    @Inject
    constructor(db: FirebaseFirestore) : this(
        LoaderRepositoryImpl<Restaurant>(
            RepositoryImpl(db, RESTAURANT_COLLECTION_PATH)
            { documentSnapshot ->
                documentSnapshot.toRestaurant()
            })
    )

    companion object {
        const val RESTAURANT_NAME_FIELD_NAME: String = "name"
        const val RESTAURANT_COLLECTION_PATH = "restaurants"
        const val LATITUDE_FIELD_NAME = "lat"
        const val LONGITUDE_FIELD_NAME = "long"
        const val OCCUPANCY_FIELD_NAME = "occupancy"

        val OFFLINE_RESTAURANTS = listOf<Restaurant>(
            Restaurant(
                name = "Arcadie",
                occupancy = 0,
                lat = 46.5,
                long = 6.6,
                grade = 0.0,
                numReviews = 0
            ),
            Restaurant(
                name = "Epicure",
                occupancy = 0,
                lat = 46.5,
                long = 6.6,
                grade = 0.0,
                numReviews = 0
            ),
            Restaurant(
                name = "Niki",
                occupancy = 0,
                lat = 46.5,
                long = 6.6,
                grade = 0.0,
                numReviews = 0
            ),
            Restaurant(
                name = "Roulotte du Soleil",
                occupancy = 0,
                lat = 46.5,
                long = 6.6,
                grade = 0.0,
                numReviews = 0
            )
        )

        fun DocumentSnapshot.toRestaurant(): Restaurant? = toItem()

    }

    override suspend fun getRestaurants(): List<Restaurant> = repository.take(Query.DEFAULT_QUERY_LIMIT.toLong())


    override suspend fun getRestaurantById(id: String): Restaurant? =
        repository.getById(id)

    override suspend fun incrementOccupancy(id: String) {
        repository.update(id) { restaurant ->
            restaurant.copy(occupancy = restaurant.occupancy + 1)
        }.await()
    }

    override suspend fun decrementOccupancy(id: String) {
        repository.update(id) { restaurant ->
            restaurant.copy(occupancy = restaurant.occupancy - 1)
        }.await()
    }


    override val offlineData: List<Restaurant> = OFFLINE_RESTAURANTS

}