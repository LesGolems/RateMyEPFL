package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.items.Restaurant
import javax.inject.Inject


class FakeItemsRepository @Inject constructor() : ItemsRepositoryInterface {

    override suspend fun getCourses(): List<Course?> {
        return listOf(
            Course(
                title = "Software development project",
                section = "IC",
                teacher = "George Candea",
                credits = 4,
                id = "CS-306"
            ),
            Course(
                title = "Calcul quantique",
                section = "IC",
                teacher = "Nicolas Macris",
                credits = 4,
                id = "CS-308"
            ),
            Course(
                title = "Intelligence artificielle",
                section = "IC",
                teacher = "Boi Faltings",
                credits = 4,
                id = "CS-330"
            ),
            Course(
                title = "Projet de systems-on-chip",
                section = "IC",
                teacher = "René Beuchat",
                credits = 3,
                id = "CS-309"
            ),
            Course(
                title = "Introduction to database systems",
                section = "IC",
                teacher = "Christoph Koch",
                credits = 4,
                id = "CS-332"
            )
        )
    }

    override suspend fun getByIdCourses(id: String): Course? = Course(
        title = "Software development project",
        section = "IC",
        teacher = "George Candea",
        credits = 4,
        id = "CS-306"
    )

    override suspend fun getClassrooms(): List<Classroom?> {
        return listOf(
            Classroom(
                id = "CM3",
            ),
            Classroom(
                id = "CE-1515",
            ),
            Classroom(
                id = "AAC 2 31",
            ),
            Classroom(
                id = "ELA 2",
            )
        )
    }

    override suspend fun getByIdClassrooms(id: String): Classroom? = Classroom(id = "CM3")

    override suspend fun getRestaurants(): List<Restaurant?> {
        return listOf(
            Restaurant(id = "Roulotte du Soleil"),
            Restaurant(id = "Arcadie"),
            Restaurant(id = "Takinoa")
        )
    }

    override suspend fun getByIdRestaurants(id: String): Restaurant? = Restaurant(id = "Roulotte du Soleil")


    override suspend fun getById(id: String?): Reviewable? = Classroom("Fake id")

    override fun updateRating(rating: ReviewRating, item: Reviewable) {}

}