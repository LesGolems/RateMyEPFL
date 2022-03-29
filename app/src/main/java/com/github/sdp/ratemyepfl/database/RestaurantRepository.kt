package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Restaurant.Companion.toRestaurant
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class RestaurantRepository @Inject constructor(): ItemRepositoryImpl<Restaurant>(RESTAURANT_COLLECTION_PATH) {

    companion object {
        const val RESTAURANT_COLLECTION_PATH = "restaurants"
    }

    override fun toItem(snapshot: DocumentSnapshot): Restaurant = snapshot.toRestaurant()
}