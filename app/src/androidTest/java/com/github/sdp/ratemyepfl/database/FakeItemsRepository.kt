package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import javax.inject.Inject


class FakeItemsRepository @Inject constructor(): ItemsRepositoryInterface {

    override suspend fun getCourses(): List<Course?> {
        return listOf(
            Course(
                name="Software development project",
                faculty="IC",
                teacher="George Candea",
                credits=4,
                courseCode="CS-306"
            ),
            Course(
                name="Calcul quantique",
                faculty="IC",
                teacher="Nicolas Macris",
                credits=4,
                courseCode="CS-308"
            ),
            Course(
                name="Intelligence artificielle",
                faculty="IC",
                teacher="Boi Faltings",
                credits=4,
                courseCode="CS-330"
            ),
            Course(
                name="Projet de systems-on-chip",
                faculty="IC",
                teacher="René Beuchat",
                credits=3,
                courseCode="CS-309"
            ),
            Course(
                name="Introduction to database systems",
                faculty="IC",
                teacher="Christoph Koch",
                credits=4,
                courseCode="CS-332"
            )
        )
    }


    override suspend fun getClassrooms(): List<Classroom?> {
        return listOf(
            Classroom(
                id = "CM3",
                name = "Salle"
            ),
            Classroom(
                id = "CE-1515",
                name = "Salle polyvalente"
            ),
            Classroom(
                id = "AAC 2 31",
                name = "Salle de cours"
            ),
            Classroom(
                id = "ELA 2",
                name = "Salle de cours"
            )
        )
    }
}