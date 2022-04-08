package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.ClassroomRepositoryInterface
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

        val ROOM_WITH_REVIEWS = Classroom(id = "CM3", numReviews = 15, averageGrade = 2.5)
        val ROOM_WITHOUT_REVIEWS = Classroom(id = "CM3", numReviews = 0, averageGrade = 0.0)

        var roomById = ROOM_WITH_REVIEWS
    }


    override suspend fun getClassrooms(): List<Classroom> = CLASSROOM_LIST

    override suspend fun getRoomById(id: String): Classroom = roomById

    override fun updateClassroomRating(id: String, rating: ReviewRating) {
    }
}