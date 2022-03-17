package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import java.time.LocalDate
import javax.inject.Inject

class FakeClassroomsRepository @Inject constructor(): ClassroomsRepositoryInterface {

    override suspend fun get(): List<Classroom?> {
        return listOf(
            Classroom(
                id = "CM3",
                name = "Salle",
                reviews = mutableListOf(
                    ClassroomReview(15, "bien", LocalDate.now()),
                    ClassroomReview(16, "pas mal du tout", LocalDate.now())
                ),
            ),
            Classroom(
                id = "CE-1515",
                name = "Salle polyvalente",
                reviews = mutableListOf(
                    ClassroomReview(17, "en vrai c ienb", LocalDate.now()),
                    ClassroomReview(20, "excellent", LocalDate.now())
                )
            )
        )
    }

}