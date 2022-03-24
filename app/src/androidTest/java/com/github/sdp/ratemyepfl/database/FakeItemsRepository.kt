package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
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
}