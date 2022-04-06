package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import javax.inject.Inject

class FakeClassroomRepository @Inject constructor() : ClassroomRepositoryInterface {

    companion object {
        val CLASSROOM_LIST = listOf(
            Classroom(
                id = "CM3",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Classroom(
                id = "CE-1515",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Classroom(
                id = "AAC 2 31",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Classroom(
                id = "ELA 2",
                numReviews = 15,
                averageGrade = 2.5
            )
        )

        val DEFAULT_ROOM = Classroom(id = "CM3", numReviews = 15, averageGrade = 2.5)
    }


    override suspend fun getClassrooms(): List<Classroom> = CLASSROOM_LIST

    override suspend fun getRoomById(id: String): Classroom = DEFAULT_ROOM

    override fun updateClassroomRating(id: String, rating: ReviewRating) {
    }
}