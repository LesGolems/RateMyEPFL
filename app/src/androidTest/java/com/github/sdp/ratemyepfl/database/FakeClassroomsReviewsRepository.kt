package com.github.sdp.ratemyepfl.database

import java.time.LocalDate
import javax.inject.Inject

class FakeClassroomsReviewsRepository @Inject constructor(): ClassroomsReviewsRepositoryInterface{
    override fun add(value: ClassroomReview) {

    }

    override suspend fun get(): List<ClassroomReview?> {
        return listOf(
            ClassroomReview(15, "bien", LocalDate.now()),
            ClassroomReview(16, "pas mal du tout", LocalDate.now())
        )
    }

    override suspend fun getByClassroom(id: String?): List<ClassroomReview?> {
        return listOf(
            ClassroomReview(15, "bien", LocalDate.now()),
            ClassroomReview(16, "pas mal du tout", LocalDate.now())
        )
    }
}