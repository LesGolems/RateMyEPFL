package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Classroom.Companion.toClassroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Restaurant.Companion.toRestaurant
import javax.inject.Inject

class ItemsRepository @Inject constructor() : ItemsRepositoryInterface, Repository() {
    private val collectionCourses = db.collection("courses")
    private val collectionRooms = db.collection("rooms")
    private val collectionRestaurants = db.collection("restaurants")

    override suspend fun getCourses(): List<Course> {
        return getLimit(collectionCourses, 50)
            .mapNotNull { obj -> obj.toCourse() }
    }

    override suspend fun getByIdCourses(id: String): Course? {
        return getById(collectionCourses, id).toCourse()
    }

    override suspend fun getClassrooms(): List<Classroom> {
        return getLimit(collectionRooms, 50)
            .mapNotNull { obj -> obj.toClassroom() }
    }

    override suspend fun getByIdClassrooms(id: String): Classroom? {
        return getById(collectionRooms, id).toClassroom()
    }

    override suspend fun getRestaurants(): List<Restaurant> {
        return getLimit(collectionRestaurants, 50)
            .mapNotNull { obj -> obj.toRestaurant() }
    }

    override suspend fun getByIdRestaurants(id: String): Restaurant? {
        return getById(collectionRestaurants, id).toRestaurant()
    }

    // Function to get for a generic Reviewable
    override suspend fun getById(id : String?): Reviewable? {
        if(id == null) return null
        val result = getByIdClassrooms(id)
        if (result != null) {
            return result
        }
        val result2 = getByIdCourses(id)
        if (result2 != null) {
            return result2
        }
        return getByIdRestaurants(id)
    }
}
