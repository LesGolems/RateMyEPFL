package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.QueryResult
import com.github.sdp.ratemyepfl.database.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeClassroomRepository @Inject constructor() : ClassroomRepository {

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

    override suspend fun updateClassroomRating(id: String, rating: ReviewRating) {}

    override fun search(pattern: String): QueryResult<List<Classroom>> = QueryResult(
        flow { emit(QueryState.success(listOf())) }
    )
}