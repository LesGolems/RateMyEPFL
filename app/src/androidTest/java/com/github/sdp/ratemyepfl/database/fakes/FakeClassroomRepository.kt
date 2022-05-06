package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeClassroomRepository @Inject constructor() : ClassroomRepository {

    companion object {
        val CLASSROOM_LIST = listOf(
            Classroom(
                name = "CM3",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Classroom(
                name = "CE-1515",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Classroom(
                name = "AAC 2 31",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Classroom(
                name = "ELA 2",
                numReviews = 15,
                averageGrade = 2.5
            )
        )

        val ROOM_WITH_REVIEWS = Classroom(name = "CM3", numReviews = 15, averageGrade = 2.5)
        val ROOM_WITHOUT_REVIEWS = Classroom(name = "CM3", numReviews = 0, averageGrade = 0.0)

        var roomById = ROOM_WITH_REVIEWS
    }


    override suspend fun getClassrooms(): List<Classroom> = CLASSROOM_LIST

    override suspend fun getRoomById(id: String): Classroom = roomById

    override suspend fun updateClassroomRating(id: String, rating: ReviewRating) {}
    override suspend fun load(number: UInt): QueryResult<List<Classroom>> =
        QueryResult {
            emit(QueryState.success(CLASSROOM_LIST))
        }


    override fun loaded(): List<Classroom> = CLASSROOM_LIST

    override fun search(prefix: String): QueryResult<List<Classroom>> = QueryResult(
        flow { emit(QueryState.success(listOf())) }
    )
}